package com.tq.simplybook.resp;

public class SbmClinikoModel {
	private String event_id;
	private String unit_id;
	private int practitionerId;
	private int businessId;

	public String getEvent_id() {
		return event_id;
	}

	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}

	public String getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}

	public int getPractitionerId() {
		return practitionerId;
	}

	public void setPractitionerId(int practitionerId) {
		this.practitionerId = practitionerId;
	}

	public int getBusinessId() {
		return businessId;
	}

	public void setBusinessId(int businessId) {
		this.businessId = businessId;
	}

	@Override
	public String toString() {
		return "SbmClinikoMap [event_id=" + event_id + ", unit_id=" + unit_id + ", practitionerId=" + practitionerId
				+ ", businessId=" + businessId + "]";
	}

	public SbmClinikoModel(String event_id, String unit_id, int practitionerId, int businessId) {
	
		this.event_id = event_id;
		this.unit_id = unit_id;
		this.practitionerId = practitionerId;
		this.businessId = businessId;
	}

	public SbmClinikoModel() {
	
	}
	

	

}
