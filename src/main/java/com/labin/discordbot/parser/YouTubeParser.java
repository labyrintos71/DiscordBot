package com.labin.discordbot.parser;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;

public class YouTubeParser extends Thread {
	private String keyword;
	private YouTube youtube;
	public YouTubeParser(String key) {
		keyword=key;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
		
		youtube=new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
	        public void initialize(HttpRequest request){}
	      }).setApplicationName("labinbot-parser").build();
	
	      YouTube.Search.List search;
		
		  search = youtube.search().list("id,snippet");
	      search.setKey("AIzaSyDfg2nkldVmQZnU3wR3pbHB1ElGy7wg0fk");
	      search.setQ(keyword);
	      //video, playlist, channel
	      search.setType("video");
	      //search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
	      search.setMaxResults((long)5);
	      SearchListResponse searchResponse = search.execute();

	      List<SearchResult> searchResultList = searchResponse.getItems();
	      
	      if (searchResultList != null) {
	        prettyPrint(searchResultList.iterator(),keyword);
	      }
	      
	      } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	  private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

	    System.out.println("\n=============================================================");
	    System.out.println(
	        "   First " + 5 + " videos for search on \"" + query + "\".");
	    System.out.println("=============================================================\n");

	    if (!iteratorSearchResults.hasNext()) {
	      System.out.println(" There aren't any results for your query.");
	    }

	    while (iteratorSearchResults.hasNext()) {

	      SearchResult singleVideo = iteratorSearchResults.next();
	      ResourceId rId = singleVideo.getId();

	      // Double checks the kind is video.
	      if (rId.getKind().equals("youtube#video")) {
	        String str=singleVideo.getSnippet().getThumbnails().get("default").toString();
	        System.out.println(" Video Id" + rId.getVideoId());
	        System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
	        System.out.println(" Thumbnail: " + singleVideo.getSnippet().getThumbnails().getDefault().getUrl());
	        str=singleVideo.getSnippet().getChannelTitle();
	        System.out.println(" AA : " + str);
	        System.out.println("\n-------------------------------------------------------------\n");
	      }
	    }
	  }
}
