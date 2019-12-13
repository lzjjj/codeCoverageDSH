package ci.monitor.server.utils;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import ci.monitor.server.utils.http.GET;

public class GETTest {
	
	@Test
	public void test() throws IOException {
		System.out.println(new GET().setURL("http://www.baidu.com").setProperties(new HashMap<String, String>()).connect().getResult().getContent());
	}

}
