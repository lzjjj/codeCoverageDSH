package ci.monitor.server.service.basic;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;

public interface IService<T> {
	JSONObject business(T param) throws IOException;
	JSONObject process(T param) throws IOException;
}
