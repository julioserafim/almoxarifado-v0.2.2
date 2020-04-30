package ufc.npi.clinicas.util.alert;

import java.util.ArrayList;

public class AlertSet extends ArrayList<Alert> {

	private static final long serialVersionUID = 1L;
	private static final int SHORT_DELAY = 3000;
	private static final int LONG_DELAY = 5000;
		
	public AlertSet withInfo(String message) {
		add(new Alert(Type.INFO, message, SHORT_DELAY));
		return this;
	}
	
	public AlertSet withSuccess(String message) {
		add(new Alert(Type.SUCCESS, message, SHORT_DELAY));
		return this;
	}
	
	public AlertSet withWarning(String message) {
		add(new Alert(Type.WARNING, message, SHORT_DELAY));
		return this;
	}
	
	public AlertSet withError(String message) {
		add(new Alert(Type.ERROR, message, SHORT_DELAY));
		return this;
	}
	
	public AlertSet withLongInfo(String message) {
		add(new Alert(Type.INFO, message, LONG_DELAY));
		return this;
	}
	
	public AlertSet withLongSuccess(String message) {
		add(new Alert(Type.SUCCESS, message, LONG_DELAY));
		return this;
	}
	
	public AlertSet withLongWarning(String message) {
		add(new Alert(Type.WARNING, message, LONG_DELAY));
		return this;
	}
	
	public AlertSet withLongError(String message) {
		add(new Alert(Type.ERROR, message, LONG_DELAY));
		return this;
	}
	
}
