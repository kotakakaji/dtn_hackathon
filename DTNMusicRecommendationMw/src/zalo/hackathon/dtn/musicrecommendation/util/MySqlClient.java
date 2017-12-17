/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zalo.hackathon.dtn.musicrecommendation.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 *
 * @author datbt
 */
public class MySqlClient {

	private static final Logger _Logger = Logger.getLogger(MySqlClient.class);
	private final String _host;
	private final String _dbname;
	private final String _user;
	private final String _password;
	private BlockingQueue<Connection> pool;
	private String url;

	public MySqlClient() {
		_host = ConfigHelper.Instance.getString("db_host", "localhost") + ":" + ConfigHelper.Instance.getString("db_port", "3306");
		_dbname = ConfigHelper.Instance.getString("db_name", "");
		_user = ConfigHelper.Instance.getString("db_username", "root");
		_password = ConfigHelper.Instance.getString("db_pwd", "root");
		this.init(ConfigHelper.Instance.getInt("poolsize", 10));
	}

	private boolean init(int poolsize) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			url = "jdbc:mysql://" + _host + "/" + _dbname + "?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&interactiveClient=true&" + "user=" + _user
					+ "&password=" + _password;

			BlockingQueue<Connection> cnnPool = new LinkedBlockingQueue<>(poolsize);
			while (cnnPool.size() < (poolsize > 3 ? 3 : poolsize)) {
				cnnPool.offer(DriverManager.getConnection(url));
			}
			pool = cnnPool;
			_Logger.info("MySqlClient init pool success");
		} catch (Exception ex) {
			_Logger.error(ex.getMessage(), ex);
			return false;
		}
		return true;
	}

	public Connection getDbConnection() {
		Connection conn = null;
		int retry = 0;
		do {
			try {
				conn = pool.poll(1000, TimeUnit.MILLISECONDS);
				if (conn == null || !conn.isValid(0)) {
					conn = DriverManager.getConnection(url);
				}
			} catch (Exception ex) {
				_Logger.warn(ex);
			}
			++retry;
		} while (conn == null && retry < 3);
		return conn;
	}

	private void releaseDbConnection(Connection conn, Statement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				_Logger.error(e, e);
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				_Logger.error(e, e);
			}
		}
		if (conn != null) {
			try {
				pool.offer(conn);
			} catch (Exception ex) {
				_Logger.error(ex.getMessage(), ex);
			}
		}
	}
	
	public void realeaseResource(SqlResult res) {
		releaseDbConnection(res.connection, res.statement, res.queryResult);
	}

	public SqlResult queryData(String sql) throws SQLException {
		Connection conn = getDbConnection();
		Statement statement = conn.createStatement();
		ResultSet res = statement.executeQuery(sql);
		return new SqlResult(conn, statement, res);
	}

	public SqlResult updateData(String sql) throws SQLException {
		Connection conn = getDbConnection();
		Statement statement = conn.createStatement();
		int res = statement.executeUpdate(sql);
		return new SqlResult(conn, statement, res);
	}

	public static String createSql(String baseSql, Object... values) {
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				if (values[i] instanceof String) {
					values[i] = ((String) values[i]).replace("'", "''");
				}
			}
		}
		return String.format(baseSql.replaceAll("(?<=[^'\"])(%s)(?=[^'\"])|(\"%s\")", "'%s'"), values);
	}

	public static class SqlResult {

		public final Connection connection;
		
		public final Statement statement;
		
		public final ResultSet queryResult;
		
		public final int updateResult;

		public SqlResult(Connection conn, Statement stmt, ResultSet queryRes) {
			this.connection = conn;
			this.statement = stmt;
			this.queryResult = queryRes;
			this.updateResult = -1;
		}
		
		public SqlResult(Connection conn, Statement stmt, int updateRes) {
			this.connection = conn;
			this.statement = stmt;
			this.queryResult = null;
			this.updateResult = updateRes;
		}		
		
	}
}
