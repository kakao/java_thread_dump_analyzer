package com.kakao.infra.workers;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kakao.infra.models.LockInfo;
import com.kakao.infra.models.ThreadInfo;
import com.kakao.infra.utils.SqlHelper;

@Service
public class JavaDumpParser {
	
	@Autowired
	SqlHelper sqlHelper;

	public boolean deleteData(String hostname, String timeStamp){
		sqlHelper.deleteDumpInfo(hostname, timeStamp);
		return true;
	}
	
	public String parseData(MultipartFile dumpFile, String hostname){
		
		InputStream inputStream = null;
			
		ThreadInfo threadInfo = null;

		String dateTime = "";
		String timeStamp = "";
		String tid = "";
		String nid = "";
		String state = "";
		
		try {
			inputStream = dumpFile.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			String rawData = "";			
			
			Pattern namePattern = Pattern.compile("^\"(.*)\".*prio=[0-9]+ tid=(\\w*) nid=(\\w*)\\s\\w*");		    
			Pattern statePattern = Pattern.compile("\\s+java.lang.Thread.State: (.*)");
			Pattern lockWaitPattern = Pattern.compile("\\s+- parking to wait for\\s+<(.*)>\\s+\\(.*\\)");
			Pattern lockedPattern = Pattern.compile("\\s+- locked\\s+<(.*)>\\s+\\(.*\\)");
			
			line = bufferedReader.readLine();
			try {
				dateTime = line;
				timeStamp = Long.toString(new SimpleDateFormat("yyyy-mm-dd kk:mm:ss").parse(line).getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			while ((line = bufferedReader.readLine()) != null) {
				if (line.startsWith("\"")){			
					
					if (threadInfo != null) {
						threadInfo.setRaw_data(rawData);
						sqlHelper.setThreadInfo(hostname, timeStamp, dateTime, threadInfo);
						rawData = "";
					}
					
					Matcher matcher = namePattern.matcher(line);
					matcher.find();
					
					threadInfo = new ThreadInfo();
					threadInfo.setName(matcher.group(1));
					threadInfo.setTid(matcher.group(2));
					threadInfo.setNid(matcher.group(3));	
					
					tid = matcher.group(2);
					nid = matcher.group(3);
					
				}
				if (line.contains("Thread.State:")){
					Matcher matcher = statePattern.matcher(line);
					matcher.find();
					
					state = matcher.group(1);
					state = state.split(" ")[0];
					
					threadInfo.setState(state);					
				}
				if (line.contains("parking to wait for")){
					Matcher matcher = lockWaitPattern.matcher(line);
					matcher.find();
					
					LockInfo lockInfo = new LockInfo();
					lockInfo.setId(matcher.group(1));
					lockInfo.setNid(nid);
					lockInfo.setTid(tid);
					lockInfo.setState(state);
					lockInfo.setOwned(0);
					
					sqlHelper.setLockInfo(hostname, timeStamp, lockInfo);					
				}
				if (line.contains("- locked")){
					Matcher matcher = lockedPattern.matcher(line);
					matcher.find();
					
					LockInfo lockInfo = new LockInfo();
					lockInfo.setId(matcher.group(1));
					lockInfo.setNid(nid);
					lockInfo.setTid(tid);
					lockInfo.setState(state);
					lockInfo.setOwned(1);
					
					sqlHelper.setLockInfo(hostname, timeStamp, lockInfo);
				}
				
				rawData += line + "\n";				
			}			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return "/dump/java/"+hostname+"/"+timeStamp;
		
	}

}
