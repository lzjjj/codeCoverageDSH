package ci.monitor.server.vo;

public class JenkinsResult {
	private boolean success = false;
	private String result = "";

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return "JenkinsResult [success=" + success + ", result=" + result + "]";
	}

	public JenkinsResult() {
		super();
	}

	public JenkinsResult(boolean success, String result) {
		super();
		this.success = success;
		this.result = result;
	}

}
