package ci.monitor.server.controller;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RootPage {
	
	@RequestMapping("/")
	public String home(HashMap<String, Object> map) {
		return "../static/home";
	}
	
	@RequestMapping("/about")
	public String about(HashMap<String, Object> map) {
		map.put("message", "Back-end Server");
		return "index";
	}
	
}