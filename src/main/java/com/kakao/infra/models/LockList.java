package com.kakao.infra.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class LockList {

	@Getter @Setter private String id;
	@Getter @Setter private List<LockThreadInfo> lockedThreads;
	@Getter @Setter private List<LockThreadInfo> waitingThreads;
	
	public LockList(String id){
		this.id = id;
		this.lockedThreads = new ArrayList<LockThreadInfo>();
		this.waitingThreads = new ArrayList<LockThreadInfo>();
	}
	
}
