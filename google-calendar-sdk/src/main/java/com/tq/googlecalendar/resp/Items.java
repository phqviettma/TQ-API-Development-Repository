package com.tq.googlecalendar.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Items implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 5782179393178299149L;
	private String kind;
	private String id;
	private String created;
	private String updated;
	private Start start;
	private String status;
	private End end;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreated() {
		return created;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public Start getStart() {
		return start;
	}

	public void setStart(Start start) {
		this.start = start;
	}

	public End getEnd() {
		return end;
	}

	public void setEnd(End end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "Items [kind=" + kind + ", id=" + id + ", created=" + created + ", updated=" + updated + ", start="
				+ start + ", status=" + status + ", end=" + end + "]";
	}

	public Items(String kind, String id, String created, String updated, Start start, String status, End end) {

		this.kind = kind;
		this.id = id;
		this.created = created;
		this.updated = updated;
		this.start = start;
		this.status = status;
		this.end = end;
	}

	public Items() {

	}

}
