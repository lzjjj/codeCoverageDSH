package ci.monitor.server.utils.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author JimmyHuang
 */
public class PropertiesUtil {
	private static PropertiesUtil propertiesUtil;

	private String properiesName = "config.properties";

	public PropertiesUtil() {

	}

	public static PropertiesUtil getInstance() {
		if (propertiesUtil == null) {
			propertiesUtil = new PropertiesUtil();
		}
		return propertiesUtil;
	}

	public PropertiesUtil(String fileName) {
		this.properiesName = fileName;
	}

	/**
	 * 按key获取值
	 * 
	 * @param key
	 * @return
	 */
	public String readProperty(String key) {
		String value = "";
		InputStream is = null;
		try {
			is = PropertiesUtil.class.getClassLoader().getResourceAsStream(properiesName);
			Properties p = new Properties();
			p.load(is);
			value = p.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	/**
	 * 获取整个配置信息
	 * 
	 * @return
	 */
	public Properties getProperties() {
		Properties p = new Properties();
		InputStream is = null;
		try {
			is = PropertiesUtil.class.getClassLoader().getResourceAsStream(properiesName);
			p.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return p;
	}

	/**
	 * key-value写入配置文件
	 * 
	 * @param key
	 * @param value
	 */
	public void writeProperty(String key, String value) {
		InputStream is = null;
		OutputStream os = null;
		Properties p = new Properties();
		try {
			is = new FileInputStream(properiesName);
//            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(properiesName);
			p.load(is);
//            os = new FileOutputStream(PropertiesUtil.class.getClassLoader().getResource(properiesName).getFile());
			os = new FileOutputStream(properiesName);

			p.setProperty(key, value);
			p.store(os, key);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != is)
					is.close();
				if (null != os)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		System.out.println(new PropertiesUtil("config.properties").getProperties().get("sonar.host"));
	}

}