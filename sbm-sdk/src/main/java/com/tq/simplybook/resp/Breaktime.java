package com.tq.simplybook.resp;

import java.io.Serializable;

public class Breaktime implements Serializable {

	/**
		 * 
		 */
	private static final long serialVersionUID = -1014456940809345803L;
	private String start_time;
	private String end_time;
	
	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Breaktime(String start_time, String end_time) {
		
		this.start_time = start_time;
		this.end_time = end_time;
	}

	@Override
    public String toString() {
        return "Breaktime [start_time=" + start_time + ", end_time=" + end_time + "]";
    }

    public Breaktime() {
		
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((end_time == null) ? 0 : end_time.hashCode());
        result = prime * result + ((start_time == null) ? 0 : start_time.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Breaktime other = (Breaktime) obj;
        if (end_time == null) {
            if (other.end_time != null)
                return false;
        } else if (!end_time.equals(other.end_time))
            return false;
        if (start_time == null) {
            if (other.start_time != null)
                return false;
        } else if (!start_time.equals(other.start_time))
            return false;
        return true;
    }
    
    
	
}
