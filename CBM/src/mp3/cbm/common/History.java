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
public class History {

	public String songID = null;
	public String timestamp  = null;
	public String listenDuration = null;
	public String songDuration = null;

	public History(String songID, String timestamp, String listenDuration, String songDuration) {
		this.songID = songID;
		this.timestamp = timestamp;
		this.listenDuration = listenDuration;
		this.songDuration = songDuration;
	}

	public String getSongID() {
		return songID;
	}

	public void setSongID(String songID) {
		this.songID = songID;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getListenDuration() {
		return listenDuration;
	}

	public void setListenDuration(String listenDuration) {
		this.listenDuration = listenDuration;
	}

	public String getSongDuration() {
		return songDuration;
	}

	public void setSongDuration(String songDuration) {
		this.songDuration = songDuration;
	}
	
}
