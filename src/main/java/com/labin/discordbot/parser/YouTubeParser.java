package com.labin.discordbot.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.labin.discordbot.data.YouTubeVideoData;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;

public class YouTubeParser {
	private YouTube youtube;
	private ArrayList<YouTubeVideoData> musicdata=new ArrayList<>();
	private YouTube.Search.List search;
	private SearchListResponse searchResponse;
	private List<SearchResult> searchResultList;
	private SearchResult singleVideo;
	private ResourceId rId;
	
	public ArrayList<YouTubeVideoData> getVideo(String keyword,long num) {
		// TODO Auto-generated method stub
		try {
			 musicdata=new ArrayList<>();
			  youtube=new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
		        public void initialize(HttpRequest request){}
		      }).setApplicationName("labinbot-parser").build();
			  
			  search = youtube.search().list("id,snippet");
		      search.setKey("AIzaSyDfg2nkldVmQZnU3wR3pbHB1ElGy7wg0fk");
		      search.setQ(keyword);
		      //video, playlist, channel
		      search.setType("video");
		      //search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
		      search.setMaxResults(num);
		      searchResponse = search.execute();
	
		      searchResultList = searchResponse.getItems();
		      //검색결과가 있다면
		      if (searchResultList != null) {
		    	  for(int i=0;i<searchResultList.size();i++) {	
			  	     singleVideo = searchResultList.iterator().next();
			  	     rId = singleVideo.getId();
		
			  	      // Double checks the kind is video.
			  	      if (rId.getKind().equals("youtube#video")) {
			  	    	  musicdata.add(new YouTubeVideoData(singleVideo.getSnippet().getTitle(),rId.getVideoId(),singleVideo.getSnippet().getThumbnails().getDefault().getUrl(),singleVideo.getSnippet().getChannelTitle()));
			  	        //singleVideo.getSnippet().getThumbnails().get("default").toString();
			  	      }  
		    	 }
		      }
	      } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return musicdata;
	}
}
