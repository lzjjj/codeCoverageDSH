package ci.monitor.server.utils.http;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ci.monitor.server.utils.http.constants.RequestType;

/**
 * @author JimmyHuang
 */
public class LoginUtils {
	public static String getCookies(String loginUrl, String requestType, Map<String, String> param) {
		try {
			URL url = new URL(loginUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod(requestType);
			String content = map2requestParamLine(param);
			OutputStream os = connection.getOutputStream();
			os.write(content.toString().getBytes("GBK"));
			os.close();
			return connection.getHeaderField("Set-Cookie");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String map2requestParamLine(Map<String, String> param) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String key : param.keySet()) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append("&");
			}
			stringBuilder.append(key);
			stringBuilder.append("=");
			stringBuilder.append(param.get(key));
		}
		return stringBuilder.toString();
	}

	public static void main(String[] args) {
		Map<String, String> param = new HashMap<>();
		param.put("login", "admin");
		param.put("password", "admin");
		System.out.println(getCookies("http://zha-spslab02-w10:9000/api/authentication/login", RequestType.POST, param));
		System.out.println(map2requestParamLine(param));
	}
}
