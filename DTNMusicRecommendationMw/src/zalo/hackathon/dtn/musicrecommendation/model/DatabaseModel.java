/*
 * Copyright (c) 2012-2017 by Zalo Group.
 * All Rights Reserved.
 */
package zalo.hackathon.dtn.musicrecommendation.model;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import org.json.simple.JSONArray;
import zalo.hackathon.dtn.musicrecommendation.common.Song;
import zalo.hackathon.dtn.musicrecommendation.util.JsonHelper;
import zalo.hackathon.dtn.musicrecommendation.util.MySqlClient;

/**
 *
 * @author datbt
 */
public class DatabaseModel {

	public static DatabaseModel Instance = new DatabaseModel();

	private final MySqlClient mysqlClient = new MySqlClient();

	private DatabaseModel() {

	}
	
	public List<Song> getListSongInfo(List<Long> listSongId) {
		List<Song> ret = new LinkedList<>();
		if (listSongId == null || listSongId.isEmpty()) {
			return ret;
		}
		try {
			StringBuilder sb = new StringBuilder();
			Long[] params = new Long[listSongId.size()];
			for (int i = 0; i < listSongId.size() ;++i) {
				sb.append(",%d");
				params[i] = listSongId.get(i);
			}
			String sql = MySqlClient.createSql("SELECT * FROM song WHERE id IN (" + sb.substring(1) + ")", params);
			
			MySqlClient.SqlResult result = mysqlClient.queryData(sql);
			ResultSet rs = result.queryResult;
			while (rs.next()) {
				long id = rs.getLong("id");
				String name = rs.getString("name");
				System.err.println(name);
				String singer = rs.getString("singer");
				String composer = rs.getString("composer");
				String album = rs.getString("album");
				String genre = rs.getString("genre");
				ret.add(new Song(id, name, singer, composer, album, genre));
			}
			mysqlClient.realeaseResource(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public List<Long> getListSongListened(long uid) {
		List<Long> ret = new LinkedList<>();
		try {			
			String sql = MySqlClient.createSql(MySqlClient.createSql("SELECT list_song FROM user_log_info WHERE user_id=%d", uid));
			
			MySqlClient.SqlResult result = mysqlClient.queryData(sql);
			ResultSet rs = result.queryResult;
			if (rs.next()) {
				String lsRet = rs.getString("list_song");
				String[] split = lsRet.split(",");
				for (String s : split) {
					ret.add(Long.parseLong(s));
				}
				
			}
			mysqlClient.realeaseResource(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public List<Long> getListSongRecommended(long uid) {
		List<Long> ret = new LinkedList<>();
		try {			
			String sql = MySqlClient.createSql(MySqlClient.createSql("SELECT data FROM user_cf_recommendation WHERE user_id=%d", uid));
			
			MySqlClient.SqlResult result = mysqlClient.queryData(sql);
			ResultSet rs = result.queryResult;
			if (rs.next()) {
				String lsRet = rs.getString("data");
				JSONArray data = (JSONArray) JsonHelper.Instance.parseJson(lsRet);
				for (Object o : data) {
					if (o instanceof Long) {
						ret.add((Long) o);
					}
				}
				
			}
			mysqlClient.realeaseResource(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public int getListUserId(int pos, List<Long> ret) {
		String sql;
		if (pos < 1) {
			sql = MySqlClient.createSql("SELECT user_id,id FROM user_cf_recommendation ORDER BY id DESC LIMIT 100");
		} else {
			sql = MySqlClient.createSql("SELECT user_id,id FROM user_cf_recommendation WHERE id < %d ORDER BY id DESC LIMIT 100", pos);
		}
		int currentPos = 0;
		try {			
			MySqlClient.SqlResult result = mysqlClient.queryData(sql);
			ResultSet rs = result.queryResult;
			while (rs.next()) {
				long userId = rs.getLong("user_id");
				int id = rs.getInt("id");
				currentPos = id;
				ret.add(userId);
			}
			mysqlClient.realeaseResource(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return currentPos;
	}
}
