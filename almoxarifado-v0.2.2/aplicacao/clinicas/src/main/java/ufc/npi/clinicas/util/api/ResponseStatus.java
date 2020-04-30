package ufc.npi.clinicas.util.api;

public enum ResponseStatus {

	DONE("DONE"), 
	FAIL("FAIL");
	
	private String detail;
	
	private ResponseStatus(String detail) {
		this.detail = detail;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
}
