package ci.monitor.server.controller;

import java.util.Arrays;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import ci.monitor.server.service.SonarqubeService;

@Controller
public class SonarqubeController {

	@Resource
	SonarqubeService sonarqubeService;
	
	@RequestMapping("/sonar")
	@ResponseBody
	public JSONObject sonar(@RequestParam(name="keys",required=false) String keys) {
		JSONObject result = new JSONObject();
		try {
			if(keys!=null && !"".equals(keys)) {
				String [] keys_group=keys.split(",");
				sonarqubeService.setShowKeys(Arrays.asList(keys_group));
			}
			JSONObject content = sonarqubeService.process(null);
			result.put("success", true);
			result.put("content", content);
		} catch (Exception e) {
			result.put("success", false);
			result.put("content", e.getMessage());
		}
		return result;
	}
}
