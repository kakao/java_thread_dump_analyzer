package com.kakao.infra.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.kakao.infra.mappers.DumpInfoMapper;
import com.kakao.infra.mappers.LockInfoMapper;
import com.kakao.infra.mappers.LockThreadInfoMapper;
import com.kakao.infra.mappers.ThreadInfoMapper;
import com.kakao.infra.models.DumpInfo;
import com.kakao.infra.models.LockInfo;
import com.kakao.infra.models.LockThreadInfo;
import com.kakao.infra.models.ThreadInfo;

@Service
public class SqlHelper {

	@Autowired
    JdbcTemplate jdbcTemplate;

	public List<ThreadInfo> getThreadInfo(String hostname, String timeStamp, String state){
		String sql = "SELECT name, tid, nid, state, raw_data FROM thread_info WHERE hostname = '" + hostname + "' AND timestamp = '" + timeStamp + "' AND state = '" + state + "'";
		return jdbcTemplate.query(sql, new ThreadInfoMapper());	
	}
	
	public void setThreadInfo(String hostname, String timeStamp, String dateTime, ThreadInfo threadInfo){
		String sql = "INSERT INTO thread_info (hostname, timestamp, date, name, tid, nid, state, raw_data) VALUES ";
		sql +=		 "('" + hostname + "','" + timeStamp + "','" + dateTime + "','" + threadInfo.getName() + "','" + threadInfo.getTid() + "','" + threadInfo.getNid() + "','" + threadInfo.getState() + "','" + threadInfo.getRaw_data() + "')";
		sql +=       " ON DUPLICATE KEY UPDATE name = VALUES(name), state = VALUES(state), raw_data = VALUES(raw_data), date = VALUES(date)";
		jdbcTemplate.batchUpdate(sql);
	}
	
	public List<LockInfo> getLockInfo(String hostname, String timeStamp) {
		String sql = "SELECT DISTINCT(lock_id) FROM lock_info WHERE hostname = '" + hostname + "' AND timestamp = '" + timeStamp + "'";
		return jdbcTemplate.query(sql, new LockInfoMapper());	
	}
		
	public void setLockInfo(String hostname, String timeStamp, LockInfo lockInfo){
		String sql = "INSERT INTO lock_info (hostname, timestamp, lock_id, tid, nid, state, owned) VALUES";
		sql +=		 "('" + hostname + "','" + timeStamp + "','" + lockInfo.getId() + "','" + lockInfo.getTid() + "','" + lockInfo.getNid() + "','" + lockInfo.getState() + "','" + lockInfo.getOwned() + "')";
		sql +=       " ON DUPLICATE KEY UPDATE lock_id = VALUES(lock_id), state = VALUES(state), owned = VALUES(owned)";
		jdbcTemplate.batchUpdate(sql);
	}
	
	public List<LockThreadInfo> getLockThreadInfo(String hostname, String timeStamp, String id, String owned){
		String sql = "SELECT tid, nid, state, owned FROM lock_info WHERE hostname = '" + hostname + "' AND timestamp = '" + timeStamp + "' AND lock_id = '" + id +"' AND owned = '" + owned + "'";
		return jdbcTemplate.query(sql, new LockThreadInfoMapper());	
	}
	
	public List<DumpInfo> getDumpInfo(){
		String sql = "SELECT hostname, timestamp, date FROM thread_info GROUP BY hostname, timestamp ORDER BY date DESC";
		return jdbcTemplate.query(sql, new DumpInfoMapper());
	}
	
	public List<DumpInfo> getDumpInfo(String hostname, String timeStamp){
		String sql = "SELECT hostname, timestamp, date FROM thread_info WHERE hostname = '" + hostname + "' AND timeStamp = '" + timeStamp + "' LIMIT 1";
		return jdbcTemplate.query(sql, new DumpInfoMapper());
	}
	
	public void deleteDumpInfo(String hostname, String timeStamp){
		String sql = "DELETE FROM thread_info WHERE hostname = '" + hostname + "' AND timeStamp='" + timeStamp + "'";
		jdbcTemplate.batchUpdate(sql);
		
		sql = "DELETE FROM lock_info WHERE hostname = '" + hostname + "' AND timeStamp='" + timeStamp + "'";
		jdbcTemplate.batchUpdate(sql);		
	}
}
