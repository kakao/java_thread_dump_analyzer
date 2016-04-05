package com.kakao.infra.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.kakao.infra.models.LockInfo;

public class LockInfoMapper implements RowMapper<LockInfo> {

	@Override
	public LockInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		LockInfo lockInfo = new LockInfo(rs.getString("lock_id"));
		return lockInfo;
	}

}
