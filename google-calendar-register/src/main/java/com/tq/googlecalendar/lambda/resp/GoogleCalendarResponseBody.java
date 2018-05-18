package com.tq.googlecalendar.lambda.resp;

public abstract class GoogleCalendarResponseBody {
	private Object object;

	public GoogleCalendarResponseBody(Object object) {
		this.object = object;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return "GoogleCalendarResponseBody [object=" + object + "]";
	}
	
}
