package ci.monitor.server.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import ci.monitor.server.service.basic.BaseCacheService;
import ci.monitor.server.utils.common.PropertiesUtil;
import ci.monitor.server.utils.common.URLUtils;
import ci.monitor.server.utils.http.GET;
import ci.monitor.server.utils.http.LoginUtils;
import ci.monitor.server.utils.http.constants.RequestType;
import ci.monitor.server.vo.SonarMeasure;

/**
 * @author JimmyHuang
 */
@Service("sonarqubeService")
public class SonarqubeService extends BaseCacheService<Object> {
	private static final String HOST = PropertiesUtil.getInstance().readProperty("sonar.host");
	private static final String ADMIN_USER = PropertiesUtil.getInstance().readProperty("sonar.admin.user");
	private static final String ADMIN_PASSWORD = PropertiesUtil.getInstance().readProperty("sonar.admin.password");
	private static final String GET_PROJECT_URL = HOST	+ "/api/components/search_projects?ps=50&facets=reliability_rating,security_rating,sqale_rating,coverage,duplicated_lines_density,ncloc,alert_status,languages,tags&f=analysisDate,leakPeriodDate&s=analysisDate&asc=false";
	private static final String GET_UNCOVERAGE_LIST = HOST	+ "/api/measures/component_tree?metricSortFilter=withMeasuresOnly&asc=false&ps=100&metricSort=uncovered_lines&s=metric&baseComponentKey=###&metricKeys=uncovered_lines%2Cline_coverage&strategy=leaves";// IRIS_TNM
	private static final String GET_FILE_CODE = HOST + "/api/sources/lines?key=###";
	private static final String LOGIN_URL = HOST + "/api/authentication/login";
	private String cookies = null;
	
	private static final String GET_PROJECT_DETAILS = HOST + "/api/measures/component?additionalFields=periods&componentKey=###&metricKeys=new_technical_debt,blocker_violations,bugs,burned_budget,business_value,classes,code_smells,cognitive_complexity,comment_lines,comment_lines_density,branch_coverage,new_branch_coverage,conditions_to_cover,new_conditions_to_cover,confirmed_issues,coverage,new_coverage,critical_violations,complexity,directories,duplicated_blocks,new_duplicated_blocks,duplicated_files,duplicated_lines,duplicated_lines_density,new_duplicated_lines,new_duplicated_lines_density,effort_to_reach_maintainability_rating_a,false_positive_issues,files,functions,generated_lines,generated_ncloc,info_violations,violations,line_coverage,new_line_coverage,lines,ncloc,lines_to_cover,new_lines_to_cover,sqale_rating,new_maintainability_rating,major_violations,minor_violations,new_blocker_violations,new_bugs,new_code_smells,new_critical_violations,new_info_violations,new_violations,new_lines,new_major_violations,new_minor_violations,new_vulnerabilities,open_issues,projects,alert_status,reliability_rating,new_reliability_rating,reliability_remediation_effort,new_reliability_remediation_effort,reopened_issues,security_rating,new_security_rating,security_remediation_effort,new_security_remediation_effort,skipped_tests,statements,team_size,sqale_index,sqale_debt_ratio,new_sqale_debt_ratio,uncovered_conditions,new_uncovered_conditions,uncovered_lines,new_uncovered_lines,test_execution_time,test_errors,test_failures,test_success_density,tests,vulnerabilities,wont_fix_issues";

	private List<String> showKeys = new ArrayList<>(); 
	
	@Override
	protected int getCacheTime() {
		return 1000;
	}
	
	private void initCookies() {
		Map<String, String> crParam = new HashMap<>();
		crParam.put("login", ADMIN_USER);
		crParam.put("password", ADMIN_PASSWORD);
		cookies = LoginUtils.getCookies(LOGIN_URL, RequestType.POST, crParam);
	}

	
	@Override
	public JSONObject business(Object param) throws IOException {
		initCookies();
		JSONObject result = new JSONObject();
		JSONArray details = new JSONArray();
		Map<String, Integer> summerize= new HashMap<>();
		Integer uncoverCount = 0;
		
		JSONObject projectJSONResult = new GET().setURL(GET_PROJECT_URL).setProperties(new HashMap<String, String>())
				.connect().getJSONResult();
		JSONArray projects = projectJSONResult.getJSONArray("components");
		for (int i = 0; i < projects.size(); i++) {
			JSONObject project = projects.getJSONObject(i);
			
			if(showKeys.size() > 0 && !getShowKeys().contains(project.getString("key"))) {
				continue;
			}
			
			String leakPeriodDate = project.getString("leakPeriodDate");
			System.out.println(URLUtils.replaceParam(GET_UNCOVERAGE_LIST, project.getString("key")));
			JSONObject projectUncoverList = new GET().setURL(URLUtils.replaceParam(GET_UNCOVERAGE_LIST, project.getString("key"))).setProperties(new HashMap<String, String>()).connect().getJSONResult();
			JSONArray projectUncoverFiles = projectUncoverList.getJSONArray("components");
			
			JSONObject projectDetail = new JSONObject();
			JSONArray projectDetailArray = new JSONArray();
			Map<String, Integer> projectSummerize= new HashMap<>();
			Integer projectUncoverCount = 0;
			for (int j = 0; j < projectUncoverFiles.size(); j++) {
				JSONObject projectUncoverFile=projectUncoverFiles.getJSONObject(j);
				JSONArray projectUncoverFileMeasures=projectUncoverFile.getJSONArray("measures");
				
				for (int k = 0; k < projectUncoverFileMeasures.size(); k++) {
					if("uncovered_lines".equals(projectUncoverFileMeasures.getJSONObject(k).get("metric"))) {
						if(!"[{\"index\":1,\"value\":\"0\"}]".equals(projectUncoverFileMeasures.getJSONObject(k).getString("periods"))) {
							Map<String, Integer> fileDetail=calUncoverCodeByWho(leakPeriodDate, projectUncoverFile.getString("key"));
							for(String key:fileDetail.keySet()) {
								Integer count = fileDetail.get(key);
								projectUncoverCount+=count;
								uncoverCount+=count;
								if(summerize.get(key) == null) {
									summerize.put(key, count);
								}else {
									summerize.put(key, summerize.get(key) + count);
								}
								if(projectSummerize.get(key) == null) {
									projectSummerize.put(key, count);
								}else {
									projectSummerize.put(key, projectSummerize.get(key) + count);
								}
							}
							JSONObject object = new JSONObject();
							object.put("name", projectUncoverFile.getString("key"));
							object.put("uncoverList", fileDetail);
							projectDetailArray.add(object);
						}
					}
				}
				
			}
			projectDetail.put("projectDetails", getProjectDetails(project.getString("key")));
			projectDetail.put("projectKey", project.getString("key"));
			projectDetail.put("projectUncoverCount", projectUncoverCount);
			projectDetail.put("projectSummerize", projectSummerize);
			projectDetail.put("projectFiles", projectDetailArray);
			details.add(projectDetail);

		}
		result.put("details", details);
		result.put("summerize", summerize);
		result.put("uncoverCount", uncoverCount);
		result.put("host", HOST);
		return result;
	}


	private Map<String, Integer> calUncoverCodeByWho(String leakPeriodDate, String fileKey) throws IOException {
		Map<String, Integer> result= new HashMap<>();
		JSONObject lines = new GET()
				.setCookies(cookies)
				.setURL(URLUtils.replaceParam(GET_FILE_CODE, fileKey))
				.setProperties(new HashMap<String, String>()).connect().getJSONResult();
		JSONArray sources=lines.getJSONArray("sources");
		for (int i = 0; i < sources.size(); i++) {
			JSONObject line = sources.getJSONObject(i);
			if (line == null || StringUtils.isEmpty(line.getString("scmDate"))) {
				continue;
			}
			if(line.getString("scmDate").compareTo(leakPeriodDate) > 0){
				if(result.get(line.getString("scmAuthor"))==null) {
					result.put(line.getString("scmAuthor"), 1);
				}else {
					result.put(line.getString("scmAuthor"), result.get(line.getString("scmAuthor"))+1);
				}
			}
		}
		return result;

	}

	private Map<String, SonarMeasure> getProjectDetails(String projectKey) throws IOException {
		Map<String, SonarMeasure> result= new HashMap<>();
		JSONObject response = new GET()
				.setURL(URLUtils.replaceParam(GET_PROJECT_DETAILS, projectKey))
				.setProperties(new HashMap<String, String>()).connect().getJSONResult();
		JSONObject component = response.getJSONObject("component");
		JSONArray measures = component.getJSONArray("measures");
		for (Object object : measures) {
			JSONObject tmp = (JSONObject)object;
			SonarMeasure measure = new SonarMeasure();
			measure.setMetric(tmp.getString("metric"));
			measure.setValue(tmp.getString("value"));
			JSONArray periods = tmp.getJSONArray("periods");
			if(periods != null && periods.size() > 0 ) {
				measure.setPeriods(periods.getJSONObject(0).getString("value"));				
			}
			result.put(measure.getMetric(), measure);
		}
		return result;
	}
	
	public List<String> getShowKeys() {
		return showKeys;
	}

	public void setShowKeys(List<String> showKeys) {
		this.showKeys = showKeys;
	}

}
