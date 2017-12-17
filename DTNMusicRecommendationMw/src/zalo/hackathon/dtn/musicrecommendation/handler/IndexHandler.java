/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zalo.hackathon.dtn.musicrecommendation.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import zalo.hackathon.dtn.musicrecommendation.common.AppConfig;
import zalo.hackathon.dtn.musicrecommendation.model.IndexModel;
import zalo.hackathon.dtn.musicrecommendation.model.UserSongModel;

/**
 *
 * @author Kiss
 */
public class IndexHandler extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (AppConfig.GET_METHOD_ENABLE) {
			doProcess(req, resp);
		} else {
			resp.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
		}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doProcess(req, resp);
    }
    
    private void doProcess(HttpServletRequest req, HttpServletResponse resp){
        IndexModel.Instance.process(req, resp);
    }
}
