/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp3.cbm.common;

/**
 *
 * @author lap11298-local
 */
public class Song {

	public String title = null;
	public String artists = null;
	public String composers = null;
	public String album = null;
	public String genre = null;

	public Song(String til, String art, String comp, String abl, String genre) {
		this.title = til;
		this.artists = art;
		this.composers = comp;
		this.album = abl;
		this.genre = genre;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtists() {
		return artists;
	}

	public void setArtists(String artists) {
		this.artists = artists;
	}

	public String getComposers() {
		return composers;
	}

	public void setComposers(String composers) {
		this.composers = composers;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	
}
