package com.kakao.infra.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LockInfo {

	@Getter @Setter private String id;
	@Getter @Setter private String tid;
	@Getter @Setter private String nid;
	@Getter @Setter private String state;
	@Getter @Setter private int owned;
	
	public LockInfo(String id){
		this.id = id;
	}

	public LockInfo() {
		// TODO Auto-generated constructor stub
	}
	
}
