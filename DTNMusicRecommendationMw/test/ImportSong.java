
import java.io.BufferedReader;
import java.io.FileReader;
import zalo.hackathon.dtn.musicrecommendation.util.MySqlClient;

/*
 * Copyright (c) 2012-2017 by Zalo Group.
 * All Rights Reserved.
 */
/**
 *
 * @author datbt
 */
public class ImportSong {

	public static void main(String[] args) throws Exception {
		MySqlClient cli = new MySqlClient();

		String baseSql = "INSERT INTO song (id,name,singer,composer,album, genre) VALUES (%d, %s, %s, %s, %s, %s)";
		int count = 0;
		BufferedReader reader = new BufferedReader(new FileReader("/home/datbt/Desktop/Hackathon/mp3_data.txt"));
		String line;
		while ((line = reader.readLine()) != null) {
//			System.err.println(line);
			++count;
			String[] split = line.split("\t");
			String sql = MySqlClient.createSql(baseSql, Long.parseLong(split[0]), split[1], split[2], split[3], split[4], split[5]);
			MySqlClient.SqlResult sqlResult = null;
			try {
				sqlResult = cli.updateData(sql);
				if (sqlResult.updateResult != 1) {
					throw new Exception("Insert to db fail");
				}
				System.err.println("Import done: " + count + " - " + split[0]);
			} catch (Exception e) {
				System.err.println(e.getMessage() + " - " + sql + " - " + count);
				return;
			} finally {
				cli.realeaseResource(sqlResult);
			}
		}
	}
}
