/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zalo.hackathon.dtn.musicrecommendation.server;

import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.RewriteRegexRule;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlets.gzip.GzipHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.resource.Resource;
import zalo.hackathon.dtn.musicrecommendation.common.AppConfig;
import zalo.hackathon.dtn.musicrecommendation.handler.ApiHandler;
import zalo.hackathon.dtn.musicrecommendation.handler.IndexHandler;
import zalo.hackathon.dtn.musicrecommendation.handler.UserSongHandler;

/**
 *
 * @author Kiss
 */
public class HttpServer {

    private final Server _server;
    private final int _port;

    public HttpServer() {          
        _turnOffJettyLog();            
        _port = AppConfig.SERVER_PORT;
        _server = new Server(_port);

        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(ApiHandler.class, "/ajax");
		handler.addServletWithMapping(IndexHandler.class, "/index");
		handler.addServletWithMapping(UserSongHandler.class, "/user");

        ContextHandler context = new ContextHandler("/stc");
        ResourceHandler rh = new ResourceHandler();
        rh.setBaseResource(Resource.newResource(this.getClass().getClassLoader().getResource("template")));
		rh.setCacheControl("max-age=604800");
		
//		ContextHandler contextImages = new ContextHandler("/images");
//        ResourceHandler rhImg = new ResourceHandler();
//        rhImg.setResourceBase("images");
//		rhImg.setDirectoriesListed(true);
//		contextImages.setHandler(rhImg);

		
		GzipHandler gzipResource = new GzipHandler();
		gzipResource.setMimeTypes("text/html,text/plain,text/xml,text/css,application/javascript,text/javascript,application/x-javascript");
        gzipResource.setHandler(rh);
        context.setHandler(gzipResource);
		
		RewriteHandler rewriteApiUrl = new RewriteHandler();
		rewriteApiUrl.setRewriteRequestURI(true);
		rewriteApiUrl.setRewritePathInfo(false);
		rewriteApiUrl.setOriginalPathAttribute("requestedPath");		

		RewriteRegexRule reverse = new RewriteRegexRule();
		reverse.setRegex("/api/(\\w+)");
		reverse.setReplacement("/ajax?q=$1");
		rewriteApiUrl.addRule(reverse);
		rewriteApiUrl.setHandler(handler);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{context, rewriteApiUrl});

        _server.setHandler(handlers);        
  
    }

    public boolean startHServer() {
        try {
            _server.start();
            System.out.println("Http server starts listening at port " + _port + "...");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Server getServer() {
        return _server;
    }
    
    private void _turnOffJettyLog(){
        Log.setLog(new NoLogging());
		System.out.println("Turn off jetty log!");
    }

    private static class NoLogging implements Logger {

        @Override
        public String getName() {
            return "no";
        }

        @Override
        public void warn(String msg, Object... args) {
        }

        @Override
        public void warn(Throwable thrown) {
        }

        @Override
        public void warn(String msg, Throwable thrown) {
        }

        @Override
        public void info(String msg, Object... args) {
        }

        @Override
        public void info(Throwable thrown) {
        }

        @Override
        public void info(String msg, Throwable thrown) {
        }

        @Override
        public boolean isDebugEnabled() {
            return false;
        }

        @Override
        public void setDebugEnabled(boolean enabled) {
        }

        @Override
        public void debug(String msg, Object... args) {
        }

        @Override
        public void debug(Throwable thrown) {
        }

        @Override
        public void debug(String msg, Throwable thrown) {
        }

        @Override
        public Logger getLogger(String name) {
            return this;
        }

        @Override
        public void ignore(Throwable ignored) {
        }

        @Override
        public void debug(String string, long l) {            
        }
    }

}
