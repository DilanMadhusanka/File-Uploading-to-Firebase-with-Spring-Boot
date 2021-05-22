package com.example.fileupload.file_uploader_to_firebase.response;

public class MessageResponse {
	
	private String status;

	private String message;
	
	private String url;

	public MessageResponse(String message, String url) {
		super();
		this.message = message;
		this.url = url;
	}

	public MessageResponse(String status, String message, String url) {
		super();
		this.status = status;
		this.message = message;
		this.url = url;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
