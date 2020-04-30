package ufc.npi.clinicas.exceptions;

public class ClinicasException extends Exception{


	private static final long serialVersionUID = 1L;
	
	private String message;

	public ClinicasException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}	
}
