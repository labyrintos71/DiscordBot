package com.labin.discordbot.data;

public class YouTubeVideoData {
	private String VideoTitle, VideoID, Thumbnail, Uploader;
	public YouTubeVideoData(String title, String id, String thumbnail, String uploader) {
		// TODO Auto-generated constructor stub
		VideoTitle=title;
		VideoID=id;
		Thumbnail=thumbnail;
		Uploader=uploader;
	}
	
	public String getTitle() {
		return VideoTitle;
	}
	
	public String getID() {
		return VideoID;
	}
	
	public String getThumbnail() {
		return Thumbnail;
	}
	
	public String getUploader() {
		return Uploader;
	}
}
