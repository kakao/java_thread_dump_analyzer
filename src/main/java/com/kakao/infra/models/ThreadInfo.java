package com.kakao.infra.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ThreadInfo {
	
	@Getter @Setter private String name;
	@Getter @Setter private String tid;
	@Getter @Setter private String nid;
	@Getter @Setter private String state;
	@Getter @Setter private String raw_data;
	
	public ThreadInfo(String name, String tid, String nid, String state, String raw_data){
		this.name = name;
		this.tid = tid;
		this.nid = nid;
		this.state = state;
		this.raw_data = raw_data;
	}

	public ThreadInfo() {
		// TODO Auto-generated constructor stub
	}

}
