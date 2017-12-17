/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zalo.hackathon.dtn.musicrecommendation.common;

import zalo.hackathon.dtn.musicrecommendation.util.ConfigHelper;

/**
 *
 * @author Kiss
 */
public class AppConfig {

	public static final int SESSION_EXPIRE;
	public static final String SECRET_TOKEN_KEY;
	public static final int RENEW_TOKEN_INTERVAL;
	public static final boolean GET_METHOD_ENABLE;
	public static final int SERVER_PORT;
	public static final String DB_HOST;
	public static final String DB_NAME;
	public static final String UPLOAD_DIR;
	public static final String IMAGES_DIR;
	public static final int MAX_UPLOAD_FILE_SIZE;
	public static final boolean DEV_MODE;

	public static final int JOB_QUEUE_MAX_SIZE;
	public static final int MAPPING_WORKERS;

	public static final int MAPPING_RESULT_SIZE;

	public static final String FACEBOOK_AUTHEN_URL;
	public static final String GOOGLE_AUTHEN_URL;
	
	public static final String BKAREER_DOMAIN;
	
	public static final String ADMIN_EMAIL;
	public static final String ADMIN_EMAIL_PWD;

	static {
		SESSION_EXPIRE = ConfigHelper.Instance.getInt("session_expire", 604800);// default: 7 days
		RENEW_TOKEN_INTERVAL = ConfigHelper.Instance.getInt("renew_token_interval", 259200); //renew session after 3 day
		SECRET_TOKEN_KEY = ConfigHelper.Instance.getString("token_key", "BK@R33R_token_key");
		GET_METHOD_ENABLE = ConfigHelper.Instance.getBoolean("get_method_enable", false);
		SERVER_PORT = ConfigHelper.Instance.getInt("server_port", 8080);
		DB_HOST = ConfigHelper.Instance.getString("db_host", "localhost");
		UPLOAD_DIR = ConfigHelper.Instance.getString("upload_dir", "upload");
		IMAGES_DIR = ConfigHelper.Instance.getString("upload_images", "images");
		MAX_UPLOAD_FILE_SIZE = ConfigHelper.Instance.getInt("max_upload_size", 2097152); //2mb
		DB_NAME = ConfigHelper.Instance.getString("db_name", "BKareerDB");
		DEV_MODE = ConfigHelper.Instance.getBoolean("dev_mode", false);
		JOB_QUEUE_MAX_SIZE = ConfigHelper.Instance.getInt("job_queue_size", 1000000);
		MAPPING_WORKERS = ConfigHelper.Instance.getInt("mapping_workers", 4);
		MAPPING_RESULT_SIZE = ConfigHelper.Instance.getInt("mapping_result_size", 5);
		FACEBOOK_AUTHEN_URL = ConfigHelper.Instance.getString("fb_authen_url", "https://graph.facebook.com/v2.8/me?fields=name,email,picture&access_token=");
		GOOGLE_AUTHEN_URL = ConfigHelper.Instance.getString("gg_authen_url", "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=");
		BKAREER_DOMAIN = ConfigHelper.Instance.getString("bkareer_domain", "");
		ADMIN_EMAIL = ConfigHelper.Instance.getString("admin_email", "");
		ADMIN_EMAIL_PWD = ConfigHelper.Instance.getString("admin_email_pwd", "");
	}
}
