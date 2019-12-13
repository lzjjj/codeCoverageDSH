package ci.monitor.server.utils.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author JimmyHuang
 */
public class URLUtils {
	private static final String DEFAULT="###";
	
	public static String replaceParam(String link,String param) {
		String encodeParam = null;
		try {
			encodeParam = URLEncoder.encode(param, "utf-8").replace("+","%20");
		} catch (UnsupportedEncodingException e) {
			encodeParam = param;
		}
		return link.replace(DEFAULT, encodeParam);
	}

}
