package ufc.npi.clinicas.util.api;

import ufc.npi.clinicas.util.alert.Alert;
import ufc.npi.clinicas.util.alert.AlertSet;

public class Response {
	private ResponseStatus status;
	private Object object;
	private Alert alert;
	
	public Response() {
		this(ResponseStatus.DONE, null, null);
	}
	
	public Response(ResponseStatus status, Object object, Alert alert) {
		this.status = status;
		this.object = object;
		this.alert = alert;
	}
	
	public Response withDoneStatus() {
		this.status = ResponseStatus.DONE;
		return this;
	}
	
	public Response withFailStatus() {
		this.status = ResponseStatus.FAIL;
		return this;
	}
	
	public Response withInfoMessage(String message) {
		this.setAlert(new AlertSet().withInfo(message).get(0));
		return this;
	}
	
	public Response withSuccessMessage(String message) {
		this.setAlert(new AlertSet().withSuccess(message).get(0));
		return this;
	}
	
	public Response withWarningMessage(String message) {
		this.alert = new AlertSet().withWarning(message).get(0);
		return this;
	}
	
	public Response withErrorMessage(String message) {
		this.setAlert(new AlertSet().withError(message).get(0));
		return this;
	}
	
	public Response withObject(Object object) {
		this.setObject(object);
		return this;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}
	
}
