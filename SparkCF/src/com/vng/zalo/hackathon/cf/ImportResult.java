package com.vng.zalo.hackathon.cf;

import java.io.BufferedReader;
import java.io.FileReader;

/*
 * Copyright (c) 2012-2017 by Zalo Group.
 * All Rights Reserved.
 */
/**
 *
 * @author datbt
 */
public class ImportResult {

	public static void main(String[] args) throws Exception {
		MySqlClient cli = new MySqlClient();

		String baseSql = "INSERT INTO user_cf_recommendation (user_id, data) VALUES (%d, %s)";
		int count = 0;
		BufferedReader reader = new BufferedReader(new FileReader("/root/working/hackaton/result/result.txt"));
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
				System.err.println(e.getMessage() + " - " + sql + " - " + count);
				return;
			} finally {
				cli.realeaseResource(sqlResult);
			}
		}
	}
}
