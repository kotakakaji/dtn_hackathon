/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zalo.hackathon.cf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author namlq2
 */
public class ThreadImport implements Runnable {

	private static Logger _Logger = LoggerFactory.getLogger(ThreadImport.class.getName());
	private String path;

	public ThreadImport(String path) {
		this.path = path;
	}

	public void run() {
		BufferedReader reader = null;
		try {
			MySqlClient cli = new MySqlClient();
			String baseSql = "INSERT INTO user_cf_recommendation (user_id, data) VALUES (%d, %s)";
			int count = 0;
			reader = new BufferedReader(new FileReader(path));
			String line;
			while ((line = reader.readLine()) != null) {
				++count;
				String[] split = line.split("\t");
				if (split.length != 2) {
					return;
				}

				long userId = Long.parseLong(split[0]);
				String data = split[1];

				String sql = MySqlClient.createSql(baseSql, userId, data);
				MySqlClient.SqlResult sqlResult = null;
				try {
					sqlResult = cli.updateData(sql);
					if (sqlResult.updateResult != 1) {
						throw new Exception("Insert to db fail");
					}
				} catch (Exception e) {
					_Logger.error(e.getMessage() + " - " + sql + " - " + count);
					return;
				} finally {
					cli.realeaseResource(sqlResult);
				}
			}
		} catch (FileNotFoundException ex) {
			_Logger.error(ex.getMessage(), ex);
		} catch (IOException ex) {
			_Logger.error(ex.getMessage(), ex);
		} catch (Exception e) {
			_Logger.error(e.getMessage(), e);
		} finally {
			try {
				reader.close();
			} catch (IOException ex) {
				_Logger.error(ex.getMessage(), ex);
			}
		}
	}

}
