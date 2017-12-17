/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zalo.hackathon.dtn.musicrecommendation.model;

import hapax.TemplateLoader;
import hapax.TemplateResourceLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Kiss
 */
public abstract class BaseModel {

	public abstract void process(HttpServletRequest req, HttpServletResponse resp);
	
	protected final TemplateLoader tmplLoader = TemplateResourceLoader.create("template/");

	protected void invalidateCookie(HttpServletRequest req, HttpServletResponse resp) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookie.setValue("");
				cookie.setPath("/");
				cookie.setMaxAge(0);
				resp.addCookie(cookie);
			}
		}
	}

	protected void response(HttpServletRequest req, HttpServletResponse resp, Object content) {
		try (PrintWriter out = getResponseWriter(req, resp)) {
			out.print(content);
		} catch (Exception ex) {
			System.err.println(ex.getMessage() + " while processing URI \"" + req.getRequestURI() + "?" + req.getQueryString() + "\"");
		}
	}

	protected PrintWriter getResponseWriter(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter writer;
		String acceptEncoding = getHeader(req, "Accept-Encoding");
		if (acceptEncoding.contains("gzip")) {
			resp.addHeader("Content-Encoding", "gzip");
			writer = new PrintWriter(new GZIPOutputStream(resp.getOutputStream()));
		} else {
			writer = resp.getWriter();
		}
		return writer;
	}

	protected void prepareHeaderHtml(HttpServletResponse resp) {
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html; charset=UTF-8");
	}

	protected void prepareHeaderJs(HttpServletResponse resp) {
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/javascript; charset=UTF-8");
	}

	protected void prepareHeaderJson(HttpServletResponse resp) {
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
	}

	protected String getStringParam(HttpServletRequest req, String key) {
		String parameter = req.getParameter(key);
		if (parameter == null) {
			return "";
		} else {
			return parameter;
		}
	}

	protected int getIntParam(HttpServletRequest req, String key, int defaultVal) {
		try {
			String parameter = req.getParameter(key);
			return Integer.parseInt(parameter);
		} catch (Exception e) {
			return defaultVal;
		}
	}

	protected long getLongParam(HttpServletRequest req, String key, long defaultVal) {
		String parameter = req.getParameter(key);
		try {
			return Long.parseLong(parameter);
		} catch (Exception e) {
			return defaultVal;
		}
	}

	protected List<String> getParamArray(HttpServletRequest req, String key) {
		String[] params = req.getParameterValues(key);
		if (params == null) {
			return new ArrayList<>();
		} else {
			return new ArrayList<>(Arrays.asList(params));
		}
	}

	protected String getCookie(HttpServletRequest req, String key) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(key)) {
					return cookie.getValue();
				}
			}
		}
		return "";
	}

	protected String getHeader(HttpServletRequest req, String key) {
		String parameter = req.getHeader(key);
		if (parameter == null) {
			return "";
		} else {
			return parameter;
		}
	}

	protected JSONObject getJsonFromBody(HttpServletRequest req) {
		JSONParser parser = new JSONParser();
		JSONObject ret;
		try {
			BufferedReader reader = req.getReader();
			ret = (JSONObject) parser.parse(reader);
		} catch (IOException | ParseException e) {
			ret = new JSONObject();
		}
		return ret;
	}	

	protected String getJsonValue(JSONObject obj, String key) {
		Object get = obj.get(key);
		if (get == null) {
			return "";
		} else {
			return String.valueOf(get);
		}
	}

	protected String[] toStringArray(JSONArray arr) {
		ArrayList<String> arrStr = new ArrayList<>();
		for (Object o : arr) {
			arrStr.add((String) o);
		}
		return arrStr.toArray(new String[arrStr.size()]);
	}

	protected String getParamFromBody(InputStream is) {
		if (is == null) {
			return "";
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}
}
