/*
 * Copyright (c) 2012-2017 by Zalo Group.
 * All Rights Reserved.
 */
package zalo.hackathon.dtn.musicrecommendation.model;

import hapax.Template;
import hapax.TemplateDataDictionary;
import hapax.TemplateDictionary;
import hapax.TemplateException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import zalo.hackathon.dtn.musicrecommendation.common.Song;

/**
 *
 * @author datbt
 */
public class UserSongModel extends BaseModel {

	private static final Logger _Logger = Logger.getLogger(UserSongModel.class);

	public static final UserSongModel Instance = new UserSongModel();
	
	private UserSongModel() {
		
	}
	
	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp) {
		try {
			Template template = tmplLoader.getTemplate("usersong");
			TemplateDataDictionary dic = TemplateDictionary.create();
			
			long uid = getLongParam(req, "uid", 0l);
			
			
			if (uid == 0) {
				dic.setVariable("TITLE", "User id not found");
			}
			dic.setVariable("TITLE", "User " + uid);
			dic.setVariable("USER_ID", uid + "");
			
			

			List<Long> listSongListen = DatabaseModel.Instance.getListSongListened(uid);
			
			List<Song> listSongInfo = DatabaseModel.Instance.getListSongInfo(listSongListen);
			String listListen = renderSongItem(listSongInfo);
			//System.out.println(listListen);
			dic.setVariable("LIST_LISTENED", listListen);			
			
			List<Long> listSongRecommended = DatabaseModel.Instance.getListSongRecommended(uid);
			
			listSongInfo = DatabaseModel.Instance.getListSongInfo(listSongRecommended);
			
			dic.setVariable("LIST_RECOMMEND", renderSongItem(listSongInfo));
			
			prepareHeaderHtml(resp);
			response(req, resp, template.renderToString(dic));
		} catch (Exception e) {
			_Logger.error(e);
		}
	}
	
	private String renderSongItem(List<Song> listSong) throws TemplateException {
		TemplateDataDictionary dic = TemplateDictionary.create();
		Template tmpl = tmplLoader.getTemplate("song-item");
		if (listSong == null) {
			return "";
		}
		
		for (Song song : listSong) {
			TemplateDataDictionary streamDic = dic.addSection("SONG");			
			streamDic.setVariable("SONG_ID", song.id + "");
			streamDic.setVariable("TITLE", song.name);
			streamDic.setVariable("SINGER", song.singer);
			streamDic.setVariable("ALBUM", song.album);
			streamDic.setVariable("COMPOSER", song.composer);
			streamDic.setVariable("GENRE", song.genre);
		}
		return tmpl.renderToString(dic);
	}
}
