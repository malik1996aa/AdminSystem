package com.Admin.AdminApp.domain;

import java.math.BigInteger;

public class PermissionResult {
	private BigInteger perId;
	private String per_name;
	public PermissionResult() {}
	public PermissionResult(BigInteger perId, String per_name) {
		this.perId = perId;
		this.per_name = per_name;
	}
	
	
	public PermissionResult(String per_name) {
		super();
		this.per_name = per_name;
	}
	public BigInteger getPerId() {
		return perId;
	}
	public void setPerId(BigInteger perId) {
		this.perId = perId;
	}
	public String getPer_name() {
		return per_name;
	}
	public void setPer_name(String per_name) {
		this.per_name = per_name;
	}
	
}
