package ci.monitor.server.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class JobStatus {
	@JsonIgnore
	@JSONField(serialize = false)
	public static final String IN_PROGRESS = "IN_PROGRESS";
	@JsonIgnore
	@JSONField(serialize = false)
	public static final String DONE = "DONE";
	@JsonIgnore
	@JSONField(serialize = false)
	public static final String FAILED = "FAILED";
	@JsonIgnore
	@JSONField(serialize = false)
	public static final String UNSTABLE = "UNSTABLE";
	@JsonIgnore
	@JSONField(serialize = false)
	public static final String SUCCESS = "SUCCESS";
	@JsonIgnore
	@JSONField(serialize = false)
	public static final String FAILURE = "FAILURE";
	@JsonIgnore
	@JSONField(serialize = false)
	public static final String ABORTED = "ABORTED";

	private String jobName;
	private String currentTaskNum;
	private String lastestCompleteTaskNum;
	private String lastestFailedTaskNum;
	private String jobURL;
	private String currentTaskURL;
	private String status;
	private String previousStatus;
	private double currentTaskBeginTime;
	private double currentTaskEndTime;
	private double avgCompleteTime;
	private double currentPercentRate;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getCurrentTaskNum() {
		return currentTaskNum;
	}

	public void setCurrentTaskNum(String currentTaskNum) {
		this.currentTaskNum = currentTaskNum;
	}

	public String getLastestCompleteTaskNum() {
		return lastestCompleteTaskNum;
	}

	public void setLastestCompleteTaskNum(String lastestCompleteTaskNum) {
		this.lastestCompleteTaskNum = lastestCompleteTaskNum;
	}

	public String getLastestFailedTaskNum() {
		return lastestFailedTaskNum;
	}

	public void setLastestFailedTaskNum(String lastestFailedTaskNum) {
		this.lastestFailedTaskNum = lastestFailedTaskNum;
	}

	public String getJobURL() {
		return jobURL;
	}

	public void setJobURL(String jobURL) {
		this.jobURL = jobURL;
	}

	public String getCurrentTaskURL() {
		return currentTaskURL;
	}

	public void setCurrentTaskURL(String currentTaskURL) {
		this.currentTaskURL = currentTaskURL;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPreviousStatus() {
		return previousStatus;
	}

	public void setPreviousStatus(String previousStatus) {
		this.previousStatus = previousStatus;
	}

	public double getCurrentTaskBeginTime() {
		return currentTaskBeginTime;
	}

	public void setCurrentTaskBeginTime(double currentTaskBeginTime) {
		this.currentTaskBeginTime = currentTaskBeginTime;
	}

	public double getCurrentTaskEndTime() {
		return currentTaskEndTime;
	}

	public void setCurrentTaskEndTime(double currentTaskEndTime) {
		this.currentTaskEndTime = currentTaskEndTime;
	}

	public double getAvgCompleteTime() {
		return avgCompleteTime;
	}

	public void setAvgCompleteTime(double avgCompleteTime) {
		this.avgCompleteTime = avgCompleteTime;
	}

	public double getCurrentPercentRate() {
		return currentPercentRate;
	}

	public void setCurrentPercentRate(double currentPercentRate) {
		this.currentPercentRate = currentPercentRate;
	}

	public JobStatus(String jobName, String currentTaskNum, String lastestCompleteTaskNum, String lastestFailedTaskNum, String jobURL, String currentTaskURL, String status, String previousStatus, double currentTaskBeginTime, double currentTaskEndTime, double avgCompleteTime,
			double currentPercentRate) {
		super();
		this.jobName = jobName;
		this.currentTaskNum = currentTaskNum;
		this.lastestCompleteTaskNum = lastestCompleteTaskNum;
		this.lastestFailedTaskNum = lastestFailedTaskNum;
		this.jobURL = jobURL;
		this.currentTaskURL = currentTaskURL;
		this.status = status;
		this.previousStatus = previousStatus;
		this.currentTaskBeginTime = currentTaskBeginTime;
		this.currentTaskEndTime = currentTaskEndTime;
		this.avgCompleteTime = avgCompleteTime;
		this.currentPercentRate = currentPercentRate;
	}

	public JobStatus() {
		super();
	}

	@Override
	public String toString() {
		return "JobStatus [jobName=" + jobName + ", currentTaskNum=" + currentTaskNum + ", lastestCompleteTaskNum=" + lastestCompleteTaskNum + ", lastestFailedTaskNum=" + lastestFailedTaskNum + ", jobURL=" + jobURL + ", currentTaskURL=" + currentTaskURL + ", status=" + status + ", previousStatus="
				+ previousStatus + ", currentTaskBeginTime=" + currentTaskBeginTime + ", currentTaskEndTime=" + currentTaskEndTime + ", avgCompleteTime=" + avgCompleteTime + ", currentPercentRate=" + currentPercentRate + "]";
	}

}
