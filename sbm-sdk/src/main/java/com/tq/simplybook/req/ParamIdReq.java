package com.tq.simplybook.req;

import java.io.Serializable;

public class ParamIdReq implements Serializable {
	private static final long serialVersionUID = 2818239090373996843L;
	private Long id = null;
	
	public ParamIdReq(Long id){
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
