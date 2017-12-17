/*
 * Copyright (c) 2012-2017 by Zalo Group.
 * All Rights Reserved.
 */
package zalo.hackathon.dtn.musicrecommendation.util;

import org.json.simple.JSONAware;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author datbt
 */
public class JsonHelper {
	
	public static final JsonHelper Instance = new JsonHelper();
	
	private final ObjectPool<JSONParser> jsonParserPool = new ObjectPool(5);	
	
	private JsonHelper() {
		
	}
	
	private JSONParser borrowParser() {
		try {
			JSONParser borrow = jsonParserPool.borrow();
			if (borrow == null) {
				borrow = new JSONParser();
			}
			return borrow;
		} catch (Exception e) {
			return new JSONParser();
		}
	}
	
	private void returnParser(JSONParser parser)  {
		try {
			if (parser != null) {
				jsonParserPool.returnObject(parser);
			}
		} catch (Exception ex) {
		}
	}
	
	public JSONAware parseJson(String input) {
		JSONParser parser = borrowParser();
		try {
			return (JSONAware) parser.parse(input);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			returnParser(parser);
		}
	}
}
