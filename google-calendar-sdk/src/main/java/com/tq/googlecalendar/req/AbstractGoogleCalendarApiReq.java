package com.tq.googlecalendar.req;

public abstract class AbstractGoogleCalendarApiReq implements GoogleCalendarReq {
	private final String httpMethod;
	private final String accessToken;
	private final String contentType;
	private String endPoint = null;
	private final Object object;

	public AbstractGoogleCalendarApiReq(String accessToken,String httpMethod, String contentType, String resource,
			Object object) {

		this.httpMethod = httpMethod;
		this.contentType = contentType;
		this.endPoint = API_BASE_URL + "/" + resource;
		this.object = object;
		this.accessToken = accessToken;
	}

	public Object getObject() {
		return object;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public String getContentType() {
		return contentType;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public String getAccessToken() {
		return accessToken;
	}
	
}
