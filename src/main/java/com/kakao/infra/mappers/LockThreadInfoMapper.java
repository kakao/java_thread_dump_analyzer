package com.kakao.infra.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.kakao.infra.models.LockThreadInfo;

public class LockThreadInfoMapper implements RowMapper<LockThreadInfo> {

	@Override
	public LockThreadInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		LockThreadInfo lockThreadInfo = new LockThreadInfo(rs.getString("tid"), rs.getString("nid"), rs.getString("state"));
		return lockThreadInfo;
	}

}
