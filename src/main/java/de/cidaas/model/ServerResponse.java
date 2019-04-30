package de.cidaas.model;


public class ServerResponse {

	private String error;

	public ServerResponse(String message) {
		this.error = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
