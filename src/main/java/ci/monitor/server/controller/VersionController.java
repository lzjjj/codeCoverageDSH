package ci.monitor.server.controller;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import ci.monitor.server.service.VersionService;
import ci.monitor.server.service.param.VersionParam;

@Controller
public class VersionController {
	@Resource
	VersionService versionService;
	
	@RequestMapping("/versions")
	@ResponseBody
	public JSONObject versions(@RequestParam(name="param",required=true)String param) {
		VersionParam versionParam = JSONObject.parseObject(param,VersionParam.class);
		JSONObject result = new JSONObject();
		try {
			JSONObject content = versionService.process(versionParam);
			result.put("success", true);
			result.put("result", content);
		} catch (IOException e) {
			result.put("success", false);
			result.put("result", e.getMessage());
		}
		return result;
	}
	
}
