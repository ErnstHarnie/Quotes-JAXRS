package edu.ap.quotes;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.ArrayList;

import javax.ws.rs.*;
import javax.json.*;
import redis.clients.jedis.Jedis;


@Path("/quotes")
public class Quotes {
	Jedis redis = null;
	
	@GET
	@Produces({"text/html"})
	public String getQuotesHTML() {
		String htmlString = "<html><body>";
		try {
			FillRedisDatabase();

			ArrayList<String> quotes = new ArrayList<String>();
			ArrayList<String> authors = new ArrayList<String>(); 

			quotes.add(redis.get("quote:*"));
			quotes.add(redis.get("author:*"));
			
			for(int i = 0 ; i < quotes.size(); i++) {
				htmlString += "<b>Author : " + authors + "</b><br>";
				htmlString += "Quote : " + quotes + "<br>";
			}
		}
		catch(Exception ex) {
			htmlString = "<html><body>" + ex.getMessage();
		}
		
		return htmlString;
	
}
	
	public String FillRedisDatabase() {
		String returnCode = "";
		try {
			redis.set("quote:1", "I may be drunk, Miss, but in the morning I will be sober and you will still be ugly.");
			redis.set("quote:1", "You have enemies? Good. That means you've stood up for something, sometime in your life.");
			redis.set("quote:2", "No doubt exists that all women are crazy; it's only a question of degree.");
			redis.set("author:1", "Winston Churchill");
			redis.set("author:2", "Albert Einstein");
			
		} 
		catch (Exception ex) {
			returnCode = ex.getMessage();
		}
		
		return returnCode;
	}
	

	@GET
	@Path("{author}")
	@Produces({"text/html"})
	public String getProductJSON(@PathParam("author") String author) {
		String string = "";
		String id = "";
		int number = 0;
		ArrayList<String> tempList = new ArrayList<String>();
		
		try {
			
			
			tempList.add(redis.get("author:*"));
			
			for (String a : tempList) {
				number++;
				String[] s = a.split(":");
				if (redis.getSet("author:" + number, "") == s[1])
				id = s[1];
			}
			
			
			string += redis.get("quote:" + id);
			
			
			
		} 
		catch (Exception ex) {
			string = ex.getMessage();
		}
		
		return string;
	}
	
	
}