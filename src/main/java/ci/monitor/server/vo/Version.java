package ci.monitor.server.vo;

public class Version {
	private String artifactId="";
	private String version="";
	private String buildtime="";

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBuildtime() {
		return buildtime;
	}

	public void setBuildtime(String buildtime) {
		this.buildtime = buildtime;
	}

	@Override
	public String toString() {
		return "Version [artifactId=" + artifactId + ", version=" + version + ", buildtime=" + buildtime + "]";
	}

	public Version() {
		super();
	}

	public Version(String artifactId, String version, String buildtime) {
		super();
		this.artifactId = artifactId;
		this.version = version;
		this.buildtime = buildtime;
	}

}
