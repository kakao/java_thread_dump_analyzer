package com.kakao.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kakao.infra.utils.SqlHelper;

@Component
public class ScheduledTasks {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	
	@Autowired
	SqlHelper sqlHelper;
	
	@Scheduled(fixedRate = 60000)
	public void selfDBConnectionCheck(){
		logger.info("pingDB");
		sqlHelper.pingDB();
	}
	
}
