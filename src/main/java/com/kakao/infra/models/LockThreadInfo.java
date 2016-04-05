package com.kakao.infra.models;

import lombok.Getter;
import lombok.Setter;

public class LockThreadInfo {

	@Getter @Setter private String tid;
	@Getter @Setter private String nid;
	@Getter @Setter private String state;
	
	public LockThreadInfo(String tid, String nid, String state){
		this.tid = tid;
		this.nid = nid;
		this.state = state;
	}
	
}
