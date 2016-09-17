package com.touchCloud.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.touchCloud.pojo.DataPoint;
import com.touchCloud.pojo.Page;

@IocBean
public class DataPointDao {
	
	@Inject
	private Dao dao;
	
	public DataPoint getData(int sensorId) {
		List<DataPoint> query = dao.query(DataPoint.class, Cnd.where("deviceId", "=",sensorId),dao.createPager(1, 1));
		return query.get(0);
	}
	
	public DataPoint getById(int dpId) {
		return dao.fetch(DataPoint.class, dpId);
	}
	
	public void update (DataPoint dp) {
		dao.update(dp);
	}
	
	public void save(DataPoint dp) {
		dao.insert(dp);
	}
	
	public void delete(DataPoint dp) {
		dao.delete(dp);
	}
	
	public List<DataPoint> getHistoty(Page page) {
		String sql = "SELECT t.id,t.timestamp,t.value,t.device_id,t.sensor_id FROM (SELECT @@row:= @@row+1 AS ROW,t.* FROM `t_data_point` t,"
				+ "(SELECT @@row := 0)r) t WHERE t.row MOD " + page.getInterval() + " = 1 "
						+ "AND t.timestamp BETWEEN '" + page.getStart() + "' AND '" + page.getEnd() + 
						"' AND t.device_id = " + page.getDeviceId() + " AND t.sensor_id = " + page.getSensorId() + " limit " + page.getPageSize() + " offset " + page.getPageSize() * page.getPageNo();
		
		Sql sqls = Sqls.create(sql);
		sqls.setCallback(new SqlCallback() {
			
			@Override
			public Object invoke(Connection paramConnection, ResultSet rs,
					Sql paramSql) throws SQLException {
				
				List<DataPoint> data = new LinkedList<DataPoint>();
				
				while(rs.next()) {
					DataPoint dp = new DataPoint();
					
					dp.setDeviceId(rs.getInt("device_id"));
					dp.setId(rs.getInt("id"));
					dp.setSensorId(rs.getInt("sensor_id"));
					dp.setTimestamp(rs.getDate("timestamp"));
					dp.setValue(rs.getString("value"));
					
					data.add(dp);
				}
				return data;
			}
		});

		dao.execute(sqls);

		return sqls.getList(DataPoint.class);
	}
}
