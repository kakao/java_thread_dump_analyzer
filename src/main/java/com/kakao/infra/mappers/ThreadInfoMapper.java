package com.kakao.infra.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.kakao.infra.models.ThreadInfo;

public class ThreadInfoMapper implements RowMapper<ThreadInfo> {

	@Override
	public ThreadInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		ThreadInfo threadInfo = new ThreadInfo(rs.getString("name"), rs.getString("tid"), rs.getString("nid"), rs.getString("state"), rs.getString("raw_data"));
		return threadInfo;
	}

}
