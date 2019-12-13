package ci.monitor.server.service;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import ci.monitor.server.service.basic.BaseCacheService;
import ci.monitor.server.utils.common.PropertiesUtil;
import ci.monitor.server.utils.common.URLUtils;
import ci.monitor.server.utils.http.GET;
import ci.monitor.server.utils.http.Result;
import ci.monitor.server.vo.JobStatus;

/**
 * @author JimmyHuang
 */
@Service("jenkinsService")
public class JenkinsService extends BaseCacheService<Object>{
	 
	private static final String HOST = PropertiesUtil.getInstance().readProperty("jenkins.host");
	private static final String GET_ALL_JOBS = HOST	+ "/api/json";
	private static final String GET_JOB_DETAIL = HOST + "/job/###/api/json";
	private static final String GET_JOB_DETAIL_LINK = HOST + "/job/###";
	
	@Override
	protected int getCacheTime() {
		return 1000;
	}
	
	@Override
	public JSONObject business(Object param) throws IOException {
		JSONObject result = new JSONObject();
		List<JobStatus> jobStatusList = new ArrayList<>();
		result.put("status", jobStatusList);
		
		JSONObject allJobs = new GET().setURL(GET_ALL_JOBS).setProperties(new HashMap<String, String>()).connect().getJSONResult();
		JSONArray jobs =  allJobs.getJSONArray("jobs");
		for (int i = 0; i < jobs.size(); i++) {
			JSONObject job = jobs.getJSONObject(i);
			String jobName = job.getString("name");
			JobStatus jobStatus = getJobStatus(jobName);
			jobStatusList.add(jobStatus);
		}
		return result;
	}

	private JobStatus getJobStatus(String name) throws IOException {
		JobStatus jobStatus = new JobStatus();
		jobStatus.setJobName(name);
		Result result = new Result(404);
		if(result.getCode()==404) {
			result = new GET().setURL(URLUtils.replaceParam(GET_JOB_DETAIL, name)).setProperties(new HashMap<String, String>()).connect().getResult();
			processNoneWF(name,result,jobStatus);
		}else {
			processWF(name,result,jobStatus);
		}
		return jobStatus;
	}

	private void processWF(String jobName, Result result, JobStatus jobStatus) {
		JSONArray tasks = JSONArray.parseArray(result.getContent());
		if(tasks.size() > 0) {
			double avgCompleteTime = 0;
			double currentPercentRate = 0;
			String lastestCompleteTaskNum= "";
			String lastestFailedTaskNum= "";
			jobStatus.setAvgCompleteTime(avgCompleteTime);
			jobStatus.setCurrentPercentRate(currentPercentRate);
			jobStatus.setCurrentTaskBeginTime(tasks.getJSONObject(0).getLong("startTimeMillis"));
			jobStatus.setCurrentTaskEndTime(tasks.getJSONObject(0).getLong("endTimeMillis"));
			jobStatus.setCurrentTaskNum(tasks.getJSONObject(0).getString("name"));
			jobStatus.setCurrentTaskURL(URLUtils.replaceParam(GET_JOB_DETAIL_LINK,jobName)+"/"+tasks.getJSONObject(0).getString("id")+"/");
			jobStatus.setJobName(jobName);
			jobStatus.setJobURL(URLUtils.replaceParam(GET_JOB_DETAIL_LINK,jobName));
			jobStatus.setLastestCompleteTaskNum(lastestCompleteTaskNum);
			jobStatus.setLastestFailedTaskNum(lastestFailedTaskNum);
		}
		
	}
	
	private String getStatus(JSONObject result) {
		String status = null;
		if(result.getBoolean("building")) {
			status = JobStatus.IN_PROGRESS;
		}
		if(JobStatus.UNSTABLE.equals(result.getString("result"))) {
			status = JobStatus.UNSTABLE;
		}
		if(JobStatus.SUCCESS.equals(result.getString("result"))) {
			status = JobStatus.SUCCESS;
		}
		if(JobStatus.FAILURE.equals(result.getString("result"))) {
			status = JobStatus.FAILURE;
		}
		if(JobStatus.ABORTED.equals(result.getString("result"))) {
			status = JobStatus.ABORTED;
		}
		return status;
	}

	private void processNoneWF(String name,Result result, JobStatus jobStatus) throws IOException {
		JSONObject tasks = JSONObject.parseObject(result.getContent());
		if(tasks.size() > 0) {
			double avgCompleteTime = 0;
			double currentPercentRate = 0;
			double currentTaskBeginTime = 0;
			double currentTaskEndTime = 0;
			String status = "";
			String previousStatus = "";
			String currentTaskURL = tasks.getJSONObject("lastBuild")!=null?tasks.getJSONObject("lastBuild").getString("url")+"api/json":null;
			Integer currentTaskNum = tasks.getJSONObject("lastBuild")!=null?tasks.getJSONObject("lastBuild").getInteger("number"):0;
			JSONObject currentTaskResult = null;
			JSONObject previousTaskResult = null;
			if(currentTaskURL!=null) {
				currentTaskResult = new GET().setURL(currentTaskURL).setProperties(new HashMap<String, String>()).connect().getJSONResult();
				boolean foundPrevios = false;
				int previousTaskNum = currentTaskNum-1;
				while(!foundPrevios && previousTaskNum > 0) {
					try {
						String previousTaskURL = tasks.getJSONObject("lastBuild")!=null?tasks.getJSONObject("lastBuild").getString("url").replace("/"+String.valueOf(currentTaskNum)+"/", "/"+String.valueOf(previousTaskNum)+"/")+"api/json":null;
						previousTaskResult = new GET().setURL(previousTaskURL).setProperties(new HashMap<String, String>()).connect().getJSONResult();
					} catch (Exception e) {
						foundPrevios = false;
						previousTaskNum--;
						continue;
					}
					foundPrevios = true;
				}
				if(currentTaskResult!=null) {
					status = getStatus(currentTaskResult);
					avgCompleteTime = currentTaskResult.getLong("estimatedDuration");
					currentTaskBeginTime = currentTaskResult.getLong("timestamp");
					currentTaskEndTime = currentTaskResult.getLong("timestamp") + currentTaskResult.getLong("duration");
					if(currentTaskBeginTime ==  currentTaskEndTime) {
						if(foundPrevios) {
							currentPercentRate = (new Date().getTime()-currentTaskResult.getLong("timestamp"))*1.0 / currentTaskResult.getLong("estimatedDuration");
						}else {
							currentPercentRate = (new Date().getTime()-currentTaskResult.getLong("timestamp"))*1.0 / 100000.0;
						}
					}else {
						currentPercentRate = 1;
					}
				}
				if(previousTaskResult!=null) {
					previousStatus = getStatus(previousTaskResult);
				}
			}

			jobStatus.setAvgCompleteTime(avgCompleteTime);
			jobStatus.setCurrentPercentRate(currentPercentRate);
			jobStatus.setCurrentTaskBeginTime(currentTaskBeginTime);
			jobStatus.setCurrentTaskEndTime(currentTaskEndTime);
			jobStatus.setCurrentTaskNum(tasks.getJSONObject("lastBuild")!=null?tasks.getJSONObject("lastBuild").getString("number"):null);
			jobStatus.setCurrentTaskURL(tasks.getJSONObject("lastBuild")!=null?tasks.getJSONObject("lastBuild").getString("url"):null);
			jobStatus.setJobName(tasks.getString("name"));
			jobStatus.setJobURL(tasks.getString("url"));
			jobStatus.setLastestCompleteTaskNum(tasks.getJSONObject("lastCompletedBuild")!=null?tasks.getJSONObject("lastCompletedBuild").getString("number"):null);
			jobStatus.setLastestFailedTaskNum(tasks.getJSONObject("lastFailedBuild")!=null?tasks.getJSONObject("lastFailedBuild").getString("number"):null);
			jobStatus.setPreviousStatus(previousStatus);
			jobStatus.setStatus(status);
			
			
		}
	}
}
