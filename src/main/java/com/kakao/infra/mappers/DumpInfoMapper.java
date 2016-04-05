package com.kakao.infra.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.kakao.infra.models.DumpInfo;

public class DumpInfoMapper implements RowMapper<DumpInfo>{

	@Override
	public DumpInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		DumpInfo dumpInfo = new DumpInfo(rs.getString("hostname"), rs.getString("date"), rs.getString("timestamp"));
		return dumpInfo;
	}

}
