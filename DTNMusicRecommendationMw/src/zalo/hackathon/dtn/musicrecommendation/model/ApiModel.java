/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zalo.hackathon.dtn.musicrecommendation.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
/**
 *
 * @author Kiss
 */
public class ApiModel extends BaseModel {
	
	private static final Logger _Logger = Logger.getLogger(ApiModel.class);

	public static final ApiModel Instance = new ApiModel();

	private ApiModel() {

	}

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp) {
		//return ajax content as json
		prepareHeaderJson(resp);
		String q = getStringParam(req, "q");
		switch (q) {				
			default:
				resp.setStatus(HttpStatus.BAD_REQUEST_400);
				break;
		}
	}
}
