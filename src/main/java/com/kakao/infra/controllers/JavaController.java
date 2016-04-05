package com.kakao.infra.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kakao.infra.models.DumpInfo;
import com.kakao.infra.models.LockInfo;
import com.kakao.infra.models.LockList;
import com.kakao.infra.models.LockThreadInfo;
import com.kakao.infra.models.ThreadInfo;
import com.kakao.infra.utils.SqlHelper;
import com.kakao.infra.workers.JavaDumpParser;

@Controller
@RequestMapping("/dump/java")
public class JavaController {
	
	@Value("${config.baseurl}")
	String baseUrl;
	
	@Autowired
	SqlHelper sqlHelper;
	
	@Autowired
	JavaDumpParser javaDumpParser;
	
	@RequestMapping(method = RequestMethod.GET, value="")
	public String dumpIndex(Model model){
		
		List<DumpInfo> dumpInfos = sqlHelper.getDumpInfo();
		model.addAttribute("dumpInfos", dumpInfos);
		
		return "dumpIndex";		
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/upload")
    public String uploadForm(Model model) {
		return "uploadForm";
    }
	
	@RequestMapping(method = RequestMethod.POST, value="/upload")
    public String uploadFile(@RequestParam("hostname") String hostname,
			   @RequestParam("dumpFile") MultipartFile dumpFile, Model model) {
		
		String response = javaDumpParser.parseData(dumpFile, hostname);
		String responseUrl = baseUrl + response;
		
		model.addAttribute("response", responseUrl);
		
		return "uploadComplete";
    }
	
	@RequestMapping(method = RequestMethod.GET, value="/{hostname}/{timeStamp}")
    public String showDump(@PathVariable String hostname, @PathVariable String timeStamp, Model model) {
					
		List<ThreadInfo> runnableThreads = sqlHelper.getThreadInfo(hostname, timeStamp, "RUNNABLE");
		List<ThreadInfo> waitingThreads = sqlHelper.getThreadInfo(hostname, timeStamp, "WAITING");
		List<ThreadInfo> timedWaitingThreads = sqlHelper.getThreadInfo(hostname, timeStamp, "TIMED_WAITING");
		List<ThreadInfo> blockedThreads = sqlHelper.getThreadInfo(hostname, timeStamp, "BLOCKED");	
		
		List<LockInfo> lockInfos = sqlHelper.getLockInfo(hostname, timeStamp);
		
		List<LockList> lockLists = new ArrayList<LockList>();
		
		List<LockThreadInfo> defaultThreadInfos = new ArrayList<LockThreadInfo>();
		LockThreadInfo defaultLockThreadInfo = new LockThreadInfo("N/A","N/A","N/A");
		defaultThreadInfos.add(defaultLockThreadInfo);
		
		for( int i=0 ; i<lockInfos.size() ; i++ ){
			LockList lockList = new LockList(lockInfos.get(i).getId());
			lockList.setLockedThreads(sqlHelper.getLockThreadInfo(hostname, timeStamp, lockInfos.get(i).getId(), "1"));
			if ( lockList.getLockedThreads().size() == 0 ) {
				lockList.setLockedThreads(defaultThreadInfos);
			}
			lockList.setWaitingThreads(sqlHelper.getLockThreadInfo(hostname, timeStamp, lockInfos.get(i).getId(), "0"));
			if ( lockList.getWaitingThreads().size() == 0 ) {
				lockList.setWaitingThreads(defaultThreadInfos);
			}
			lockLists.add(lockList);
		}
			
		DumpInfo dumpInfo = sqlHelper.getDumpInfo(hostname, timeStamp).get(0);
		
		model.addAttribute("hostname", hostname);
		model.addAttribute("date", dumpInfo.getDateTime());
		
		model.addAttribute("nr_runnable", runnableThreads.size());
		model.addAttribute("nr_waiting", waitingThreads.size());
		model.addAttribute("nr_timed_waiting", timedWaitingThreads.size());
		model.addAttribute("nr_blocked", blockedThreads.size());			
				
		model.addAttribute("locks", lockLists);
		
		model.addAttribute("runnables", runnableThreads);
		model.addAttribute("waitings", waitingThreads);
		model.addAttribute("timed_waitings", timedWaitingThreads);			
		model.addAttribute("blockeds", blockedThreads);
		
		return "showDump";
    }
	
}
