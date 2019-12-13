package ci.monitor.server.controller;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import ci.monitor.server.service.JenkinsService;

@Controller
public class JenkinsController {
	@Resource
	JenkinsService jenkinsService;
	
	@RequestMapping("/jenkins")
	@ResponseBody
	public JSONObject jenkins() {
		JSONObject result = new JSONObject();
		try {
			JSONObject content = jenkinsService.process(null);
			result.put("success", true);
			result.put("result", content);
		} catch (IOException e) {
			result.put("success", false);
			result.put("result", e.getMessage());
		}
		return result;
	}
}
