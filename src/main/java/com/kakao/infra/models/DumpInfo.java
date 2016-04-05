package com.kakao.infra.models;

import lombok.Getter;
import lombok.Setter;

public class DumpInfo {
	
	@Getter @Setter private String hostname;
	@Getter @Setter private String dateTime;
	@Getter @Setter private String timeStamp;
	
	public DumpInfo(String hostname, String dateTime, String timeStamp){
		this.hostname = hostname;
		this.dateTime = dateTime;
		this.timeStamp = timeStamp;
	}

}
