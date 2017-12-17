/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vng.zalo.hackathon.cf;

import com.vng.zalo.hackathon.common.FileWriter;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

// $example on$
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// $example off$

public class CFModel {

	private static Logger _Logger = LoggerFactory.getLogger(CFModel.class.getName());
	private static MySqlClient cli = new MySqlClient();
	private static final FileWriter resultFile = new FileWriter("/home/ubuntu/result.txt");
	private static final AtomicLong COUNTER = new AtomicLong(0);
	private static Set<String> listenSet = new HashSet<>();
	
	// $example on$
	public static class Rating implements Serializable {

		private long userId = 0;
		private long songId = 0;
		private float rating = 0.0f;
		private long timestamp = 0;

		public Rating() {
		}

		public Rating(long userId, long movieId, float rating, long timestamp) {
			this.userId = userId;
			this.songId = movieId;
			this.rating = rating;
			this.timestamp = timestamp;
		}

		public long getUserId() {
			return userId;
		}

		public long getSongId() {
			return songId;
		}

		public float getRating() {
			return rating;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setUserId(long userId) {
			this.userId = userId;
		}

		public void setSongId(long movieId) {
			this.songId = movieId;
		}

		public void setRating(float rating) {
			this.rating = rating;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		@Override
		public String toString() {
			return "Rating{" + "userId=" + userId + ", songId=" + songId + ", rating=" + rating + ", timestamp=" + timestamp + '}';
		}

		public static Rating parseRating(String str) {
			Rating result = new Rating();
			try {
				String[] fields = str.split("\t");
				if (fields.length != 5) {
					_Logger.error("Invalid log:" + str);
					return result;
				}

				long userId = Long.parseLong(fields[0]);
				long songId = Long.parseLong(fields[1]);
				long ts = 0;
				try {
					ts = (long) Double.parseDouble(fields[2]);
				} catch (Exception e) {

				}

				Double listenTime = Double.parseDouble(fields[3]);
				String key = String.format("%d_%d", userId, songId);
				boolean listen = listenSet.contains(key);
				float rating = 0;
				if (listen) {
					rating = 2;
				} else if (listenTime > 30000) {
					rating = 1;
				} else {
					rating = 0;
				}
				listenSet.add(key);
				result.setUserId(userId);
				result.setSongId(songId);
				result.setRating(rating);
				result.setTimestamp(ts);
				//debugFile.write(result.toString());
				COUNTER.incrementAndGet();
				if (COUNTER.get() % 1000 == 0) {
					_Logger.info("Process:" + COUNTER.get());
				}

				return result;
			} catch (Exception e) {
				_Logger.error(e.getMessage(), e);
				_Logger.error(str);
			}
			return result;
		}
	}
	// $example off$

	public static void main(String[] args) {

		SparkSession spark = SparkSession
				.builder()
				.appName("JavaALSExample")
				.getOrCreate();

		// $example on$
		JavaRDD<Rating> ratingsRDD = spark
				.read().textFile("/home/ubuntu/zmp3_log_2.txt").javaRDD()
				.map(Rating::parseRating);
//		JavaRDD<Rating> ratingsRDD = spark
//				.read().textFile("/home/ubuntu/zmp3_small.txt").javaRDD()
//				.map(Rating::parseRating);

		Dataset<Row> ratings = spark.createDataFrame(ratingsRDD, Rating.class).dropDuplicates();
		Dataset<Row>[] splits = ratings.randomSplit(new double[]{0.8, 0.2});
		Dataset<Row> training = splits[0];
		Dataset<Row> test = splits[1];

		// Build the recommendation model using ALS on the training data
		ALS als = new ALS()
				.setMaxIter(5)
				.setRegParam(0.01)
				.setUserCol("userId")
				.setItemCol("songId")
				.setRatingCol("rating");
		ALSModel model = als.fit(training);

		// Evaluate the model by computing the RMSE on the test data
		// Note we set cold start strategy to 'drop' to ensure we don't get NaN evaluation metrics
		model.setColdStartStrategy("drop");
//		Dataset<Row> predictions = model.transform(test);
//
//		RegressionEvaluator evaluator = new RegressionEvaluator()
//				.setMetricName("rmse")
//				.setLabelCol("rating")
//				.setPredictionCol("prediction");
//		Double rmse = evaluator.evaluate(predictions);
		//debugFile.write("Root-mean-square error = " + rmse);

		// Generate top 10 movie recommendations for each user
		Dataset<Row> userRecs = model.recommendForAllUsers(25);
		userRecs.show();

		//org.json.simple.JSONArray array = new org.json.simple.JSONArray();
		List<Row> rows = userRecs.collectAsList();
		//debugFile.write("Total:" + rows.toString());
		try {
			for (Row row : rows) {
				int user = row.getInt(0);
				List<Long> suggestList = new ArrayList<>();
				List<Object> objects = row.getList(1);
				if (objects.size() > 25) {
					objects = objects.subList(0, 25);
				}
				for (Object o : objects) {
					String str = o.toString();
					str = str.substring(1, str.length() - 1);
					String[] split = str.split(",");
					long suggest = Long.parseLong(split[0]);
					if (suggest == 0) {
						continue;
					}
					suggestList.add(suggest);
				}

				//	_Logger.info("What the hell:" + objects.get(0).toString());
				//debugFile.write(user + ":" + array.toJSONString());
				String result = String.format("%d\t%s", user, suggestList.toString());
				resultFile.write(result);
			}
		} catch (Exception e) {
			_Logger.error(e.getMessage(), e);
		}
		spark.stop();
		resultFile.close();
	}

}
