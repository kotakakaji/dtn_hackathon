/*
 * Copyright (c) 2012-2017 by Zalo Group.
 * All Rights Reserved.
 */
package zalo.hackathon.dtn.musicrecommendation.common;

/**
 *
 * @author datbt
 */
public class Song {

	public long id;
	public String name;
	public String singer;
	public String composer;
	public String album;
	public String genre;

	public Song(long id, String name, String singer, String composer, String album, String genre) {
		this.id = id;
		this.name = name;
		this.singer = singer;
		this.composer = composer;
		this.album = album;
		this.genre = genre;
	}

}
