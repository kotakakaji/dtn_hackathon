package com.vng.zalo.hackathon.common;

import java.io.IOException;
import java.io.PrintWriter;

public class FileWriter {

	private PrintWriter writer = null;

	public FileWriter(String path) {
		try {
			writer = new PrintWriter(path, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(String data) {
		if (writer == null) {
			return;
		}
		writer.println(data);
		writer.flush();
	}

	public void close() {
		if (writer == null) {
			return;
		}
		writer.close();
	}
}
