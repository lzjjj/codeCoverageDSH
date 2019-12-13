package ci.monitor.server.vo;

public class SonarMeasure {
	private String metric;
	private String value;
	private String periods;

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPeriods() {
		return periods;
	}

	public void setPeriods(String periods) {
		this.periods = periods;
	}

	@Override
	public String toString() {
		return "SonarMeasure [metric=" + metric + ", value=" + value + ", periods=" + periods + "]";
	}

	public SonarMeasure(String metric, String value, String periods) {
		super();
		this.metric = metric;
		this.value = value;
		this.periods = periods;
	}

	public SonarMeasure() {
		super();
	}

}
