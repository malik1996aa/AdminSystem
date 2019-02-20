package com.Admin.AdminApp.domain;

public class LogInCircleGraph {
	private int logInYes;
	private int logInNo;
	public LogInCircleGraph() {}
	public LogInCircleGraph(int logInYes, int logInNo) {
		super();
		this.logInYes = logInYes;
		this.logInNo = logInNo;
	}
	public int getLogInYes() {
		return logInYes;
	}
	public void setLogInYes(int logInYes) {
		this.logInYes = logInYes;
	}
	public int getLogInNo() {
		return logInNo;
	}
	public void setLogInNo(int logInNo) {
		this.logInNo = logInNo;
	}
	

}
