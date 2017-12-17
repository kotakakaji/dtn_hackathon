/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cbm;

import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import mp3.cbm.common.History;
import mp3.cbm.common.MySqlClient;
import mp3.cbm.common.Song;

/**
 *
 * @author lap11298-local
 */
public class CBM {

	public static final CBM Instance = new CBM();
	private Map<String, Song> mapSongInfo = new HashMap<>();
	private Map<String, List<History>> userHistory = new HashMap<>();
	private Map<String, Integer> artists = new HashMap<>();
	private Map<String, Integer> album = new HashMap<>();
	private Map<String, Integer> genre = new HashMap<>();

	private Set<String> userSet = new HashSet<>();
	private Map<String, Integer> userID_songID_Map = new HashMap<>();
	private Map<String, String> user_Map = new HashMap<>();

	private Map<String, String> cbArtists = new HashMap<>();
	private Map<String, String> cbAlbum = new HashMap<>();
	private Map<String, String> cbGenre = new HashMap<>();

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
//		readFileMp3();
//		CBM.Instance.warmup();
//		CBM.Instance.readData();
		CBM.Instance.putData();
	}

	public void warmup() {
		try {
			InputStream fdata = new FileInputStream("/data/ZaloHackathon/mp3_data.txt");
			for (int i = 1; i <= 13; i++) {
				Map<String, Integer> userMap = new HashMap<>();
//				InputStream flog = new FileInputStream(String.format("/data/ZaloHackathon/xa_%d", i));
//				FileWriter writeFile = new FileWriter(String.format("/data/ZaloHackathon/mp3_log_%d.txt", i));
				InputStream flog = new FileInputStream(String.format("/data/ZaloHackathon/mp3_log_%d.txt", i));
				BufferedReader bufData = new BufferedReader(new InputStreamReader(fdata));
				BufferedReader bufLog = new BufferedReader(new InputStreamReader(flog));
				String line = bufData.readLine();
				while (line != null) {
					String[] words = line.split("\t");
					if (words.length >= 4) {
						String id = words[0];
						String[] genres = words[5].split(",");

						for (String temp : genres) {
							temp = temp.toLowerCase().trim();
							if (genre.containsKey(temp)) {
								int count = genre.get(temp);
								count++;
								genre.put(temp, count);
							} else {
								genre.put(temp, 1);
							}
						}
						String[] listArtists = words[2].split(",");
						for (String temp : listArtists) {
							temp = temp.toLowerCase().trim();
							if (artists.containsKey(temp)) {
								int count = artists.get(temp);
								count++;
								artists.put(temp, count);
							} else {
								artists.put(temp, 1);
							}
						}

						String alb = words[4].toLowerCase().trim();
						if (album.containsKey(alb)) {
							int count = album.get(alb);
							count++;
							album.put(alb, count);
						} else {
							album.put(alb, 1);
						}
						mapSongInfo.put(id, new Song(words[1], words[2], words[3], alb, words[5]));
					}
					line = bufData.readLine();
				}

				line = bufLog.readLine();
				int count = 0;

				while (line != null) {
					String[] words = line.split("\t");
					if (words.length >= 4) {
						count++;
						String userID = words[0];
						String songID = words[1];
						String key = userID + "_" + songID;
						if (userMap.containsKey(key)) {
							Integer value = userMap.get(key);
							userMap.put(key, value + 1);
						} else {
							userMap.put(key, 1);
						}
					}
					line = bufLog.readLine();
				}
				System.out.println(userMap.size());
			}
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	public void readData() {
		try {
			InputStream fdata = new FileInputStream("/data/ZaloHackathon/mp3_data.txt");
			FileWriter writeFile = new FileWriter("/data/ZaloHackathon/mp3_log_final.txt");
			for (int i = 1; i <= 13; i++) {
				InputStream flog = new FileInputStream(String.format("/data/ZaloHackathon/mp3_log_%d.txt", i));
				BufferedReader bufData = new BufferedReader(new InputStreamReader(fdata));
				BufferedReader bufLog = new BufferedReader(new InputStreamReader(flog));
				String line = bufData.readLine();
				while (line != null) {
					String[] words = line.split("\t");
					if (words.length >= 4) {
						String id = words[0];
						String[] genres = words[5].split(",");

						for (String temp : genres) {
							temp = temp.toLowerCase().trim();
							if (genre.containsKey(temp)) {
								int count = genre.get(temp);
								count++;
								genre.put(temp, count);
							} else {
								genre.put(temp, 1);
							}
						}
						String[] listArtists = words[2].split(",");
						for (String temp : listArtists) {
							temp = temp.toLowerCase().trim();
							if (artists.containsKey(temp)) {
								int count = artists.get(temp);
								count++;
								artists.put(temp, count);
							} else {
								artists.put(temp, 1);
							}
						}

						String alb = words[4].toLowerCase().trim();
						if (album.containsKey(alb)) {
							int count = album.get(alb);
							count++;
							album.put(alb, count);
						} else {
							album.put(alb, 1);
						}
						mapSongInfo.put(id, new Song(words[1], words[2], words[3], alb, words[5]));
					}
					line = bufData.readLine();
				}

				line = bufLog.readLine();
				int count = 0;

				while (line != null) {
					String[] words = line.split("\t");
					String key = words[0];
					int value = Integer.parseInt(words[1]);

					if (userID_songID_Map.containsKey(key)) {
						Integer temp = userID_songID_Map.get(key);
						userID_songID_Map.put(key, value + temp);
					} else {
						userID_songID_Map.put(key, value);
					}
					line = bufLog.readLine();
				}
				System.out.println(userID_songID_Map.size());
			}
			for (Entry<String, Integer> entry : userID_songID_Map.entrySet()) {
				writeFile.write(String.format("%s\t%s\n", entry.getKey(), entry.getValue()));
			}
			writeFile.close();
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	public void processData() {
		try {
			InputStream fdata = new FileInputStream("/data/ZaloHackathon/mp3_data.txt");
			InputStream flog = new FileInputStream("/data/ZaloHackathon/mp3_log_final.txt");
			FileWriter writeFile = new FileWriter("/data/ZaloHackathon/mp3_list_song.txt");
			BufferedReader bufData = new BufferedReader(new InputStreamReader(fdata));
			BufferedReader bufLog = new BufferedReader(new InputStreamReader(flog));
			String line = bufData.readLine();
			while (line != null) {
				String[] words = line.split("\t");
				if (words.length >= 4) {
					String id = words[0];
					String[] genres = words[5].split(",");

					for (String temp : genres) {
						temp = temp.toLowerCase().trim();
						if (genre.containsKey(temp)) {
							int count = genre.get(temp);
							count++;
							genre.put(temp, count);
						} else {
							genre.put(temp, 1);
						}
					}
					String[] listArtists = words[2].split(",");
					for (String temp : listArtists) {
						temp = temp.toLowerCase().trim();
						if (artists.containsKey(temp)) {
							int count = artists.get(temp);
							count++;
							artists.put(temp, count);
						} else {
							artists.put(temp, 1);
						}
					}

					String alb = words[4].toLowerCase().trim();
					if (album.containsKey(alb)) {
						int count = album.get(alb);
						count++;
						album.put(alb, count);
					} else {
						album.put(alb, 1);
					}
					mapSongInfo.put(id, new Song(words[1], words[2], words[3], alb, words[5]));
				}
				line = bufData.readLine();
			}

			line = bufLog.readLine();
			while (line != null) {
				String[] words = line.split("\t");
				String key = words[0];
				int value = Integer.parseInt(words[1]);

				String[] userStr = key.split("_");
				if (user_Map.containsKey(userStr[0])) {
					String valueStr = user_Map.get(userStr[0]);
					user_Map.put(userStr[0], valueStr + "," + userStr[1]);
				} else {
					user_Map.put(userStr[0], userStr[1]);
				}

				if (userID_songID_Map.containsKey(key)) {
					Integer temp = userID_songID_Map.get(key);
					userID_songID_Map.put(key, value + temp);
				} else {
					userID_songID_Map.put(key, value);
				}
				line = bufLog.readLine();
			}

			for (Entry<String, String> entry : user_Map.entrySet()) {
				String[] songList = entry.getValue().split(",");
				int total = 0;
				Map<String, Double> result = new HashMap();
				for (String songID : songList) {
					String key = entry.getKey() + "_" + songID;
					Integer count = userID_songID_Map.get(key);
					Song songInfo = mapSongInfo.get(songID);
					total += count;
					
					cbGenre.
					songInfo.genre
				}
			}
			for (Entry<String, String> entry : user_Map.entrySet()) {
				writeFile.write(String.format("%s\t%s\n", entry.getKey(), entry.getValue()));
			}
			writeFile.close();

			System.out.println(user_Map.size());
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}
	private class PutWorker implements Runnable{
		long _key;
		String _list;
		public PutWorker(String keyStr, String listSong){
			_key = Long.parseLong(keyStr); _list = listSong;
		}
		public void run(){
			insertDB(_key, _list);
		}
	} 
	
	public void putData() {
		try {
			InputStream flog = new FileInputStream("/data/ZaloHackathon/mp3_list_song.txt");
			BufferedReader bufLog = new BufferedReader(new InputStreamReader(flog));

			String line = bufLog.readLine();
			ExecutorService executor = Executors.newFixedThreadPool(16);
			long xCount = 0;
			while (line != null) {
				xCount++;
				if(xCount <= 57055){
					line = bufLog.readLine();
					continue;
				}
				String[] words = line.split("\t");
				String key = words[0];
				String listSong = words[1];
				Runnable _putWrk = new PutWorker(key, listSong);
				executor.execute(_putWrk);
				line = bufLog.readLine();
			}

			System.out.println(user_Map.size());
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}
	
	MySqlClient cli = new MySqlClient();
	public void insertDB(long uid, String listSongId) {

		String baseSql = "INSERT INTO user_log_info (user_id,list_song) VALUES (%d, %s)";
		
		{
			String sql = MySqlClient.createSql(baseSql, uid, listSongId);
			MySqlClient.SqlResult sqlResult = null;
			try {
				sqlResult = cli.updateData(sql);
				if (sqlResult.updateResult != 1) {
					throw new Exception("Insert to db fail");
				}
				System.err.println("Import done: " +uid + " - " + listSongId);
			} catch (Exception e) {
				System.err.println(e.getMessage() + " - " + sql);
			} finally {
				cli.realeaseResource(sqlResult);
			}
		}
	}
}
