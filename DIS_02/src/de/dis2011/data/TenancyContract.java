package de.dis2011.data;

import java.util.Date;


public class TenancyContract extends Contract {
	private Date startDate;
	private int duration;
	private double additionalCosts;
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public double getAdditionalCosts() {
		return additionalCosts;
	}
	
	public void setAdditionalCosts(double additionalCosts) {
		this.additionalCosts = additionalCosts;
	}
	
}
