package ufc.npi.clinicas.util.alert;

public class Alert {
	
	private Type type;
	private String message;
	private long delay; 
	
	public Alert(Type type, String message, long delay) {
		this.type = type;
		this.message = message;
		this.delay = delay;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}
	
}
