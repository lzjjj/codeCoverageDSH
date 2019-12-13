package ci.monitor.server.service;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import ci.monitor.server.service.basic.BaseCacheService;
import ci.monitor.server.service.param.VersionParam;
import ci.monitor.server.utils.http.GET;
import ci.monitor.server.vo.Version;

/**
 * @author JimmyHuang
 */
@Service("versionService")
public class VersionService extends BaseCacheService<VersionParam> {

	@Override
	protected int getCacheTime() {
		return 60000;
	}
	
	public Version getVersion(String url) throws IOException {
		Version version = null;
		try {
			version = (Version) new GET().setURL(url).setProperties(new HashMap<String, String>()).connect().getResultObject(Version.class);
		} catch (Exception e) {
			version = new Version("UNKNOWN", "UNKNOWN", "UNKNOWN");
		}
		return version;
	}

	@Override
	public JSONObject business(VersionParam param) throws IOException {
		JSONObject jsonObject= new JSONObject();
		JSONArray results = new JSONArray();
		for (int i = 0; i < param.getUrlMap().length; i++) {
			JSONArray item = new JSONArray();
			item.add(param.getUrlMap()[i][0]);
			item.add(getVersion(param.getUrlMap()[i][1]));
			results.add(item);
		}
		jsonObject.put("list", results);
		return jsonObject;
	}
}
