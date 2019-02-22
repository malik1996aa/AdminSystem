package com.Admin.AdminApp.domain;

import java.math.BigInteger;

public class GetIdByEmail {
private BigInteger id;

public GetIdByEmail() {}
public GetIdByEmail(BigInteger id) {
	super();
	this.id = id;
}
public BigInteger getId() {
	return id;
}
public void setId(BigInteger id) {
	this.id = id;
}


}
