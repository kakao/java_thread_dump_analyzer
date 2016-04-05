package com.kakao.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kakao.infra.workers.JavaDumpParser;

@RestController
@RequestMapping("/api/v1/dump/java")
public class JavaRestAPIController {
	
	@Value("${config.baseurl}")
	String baseUrl;
	
	@Autowired
	JavaDumpParser javaDumpParser;
	
	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	public String handleFileUpload(@RequestParam("hostname") String hostname,
			   @RequestParam("dumpFile") MultipartFile dumpFile){
		
		String response = javaDumpParser.parseData(dumpFile, hostname);
		
		String responseUrl = baseUrl + response;
		
		return responseUrl;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/delete")
	public boolean deleteDump(@RequestParam("hostname") String hostname, @RequestParam("timeStamp") String timeStamp){
		
		System.out.println("HOSTNAME");
		System.out.println(hostname);
		
		javaDumpParser.deleteData(hostname, timeStamp);		
		return true;
		
	}
}
