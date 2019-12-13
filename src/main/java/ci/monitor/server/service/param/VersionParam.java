package ci.monitor.server.service.param;

import java.util.Arrays;

public class VersionParam {
	private String[][] urlMap;

	public String[][] getUrlMap() {
		return urlMap;
	}

	public void setUrlMap(String[][] urlMap) {
		this.urlMap = urlMap;
	}

	@Override
	public String toString() {
		return "VersionParam [urlMap=" + Arrays.toString(urlMap) + "]";
	}

	public VersionParam() {
		super();
	}

	public VersionParam(String[][] urlMap) {
		super();
		this.urlMap = urlMap;
	}

}
