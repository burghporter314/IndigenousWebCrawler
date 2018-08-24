package com.burghporter314.x10host.indigenoustweets;

public class Parameter <T>{

	T param;
	
	public Parameter(T param) {
		this.param = param;
	}
	
	public void setParameter(T param) {
		this.param = param;
	}
	
	public T getParameter() {
		return this.param;
	}
	
	public Class<? extends Object> getInstance() {
		return this.param.getClass();
	}
	
}
