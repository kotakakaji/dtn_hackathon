/*
 * Copyright (c) 2012-2017 by Zalo Group.
 * All Rights Reserved.
 */
package zalo.hackathon.dtn.musicrecommendation.model;

import hapax.Template;
import hapax.TemplateDataDictionary;
import hapax.TemplateDictionary;
import hapax.TemplateException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import zalo.hackathon.dtn.musicrecommendation.common.Song;

/**
 *
 * @author datbt
 */
public class IndexModel extends BaseModel {

	private static final Logger _Logger = Logger.getLogger(IndexModel.class);

	public static final IndexModel Instance = new IndexModel();
	
	private IndexModel() {
		
	}
	
	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp) {
		try {
			Template template = tmplLoader.getTemplate("index");
			TemplateDataDictionary dic = TemplateDictionary.create();			
			
			int pos = getIntParam(req, "pos", 0);
			
			List<Long> listUserId = new LinkedList<>();
			
			pos = DatabaseModel.Instance.getListUserId(pos, listUserId);
			StringBuilder lsUser = new StringBuilder();
			for (long uid : listUserId) {
				String l = String.format("<a href='/user?uid=%d' target='_blank'>%d</a><br><br>", uid, uid);
				lsUser.append(l);
			}
			dic.setVariable("LIST_USERID", lsUser.toString());
			dic.setVariable("POS", pos + "");
			
			prepareHeaderHtml(resp);
			response(req, resp, template.renderToString(dic));
		} catch (Exception e) {
			_Logger.error(e);
		}
	}	
	
}
