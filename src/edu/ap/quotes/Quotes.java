package edu.ap.quotes;

import java.io.*;
import javax.ws.rs.*;
import javax.json.*;

import jersey.*;
import redis.clients.jedis.Jedis;


@Path("/quotes")
public class Quotes {
	Jedis redis = null;
	
	@GET
	@Produces({"text/html"})
	public String getQuotesHTML() {
		String htmlString = "<html><body>";
		try {
			JsonReader reader = Json.createReader(new StringReader(getRedisDB()));
			JsonObject rootObj = reader.readObject();
			JsonArray array = rootObj.getJsonArray("products");
			
			
			for(int i = 0 ; i < array.size(); i++) {
				JsonObject obj = array.getJsonObject(i);
				htmlString += "<b>Name : " + obj.getString("name") + "</b><br>";
				htmlString += "ID : " + obj.getString("id") + "<br>";
				htmlString += "Brand : " + obj.getString("brand") + "<br>";
				htmlString += "Description : " + obj.getString("description") + "<br>";
				htmlString += "Price : " + obj.getString("price") + "<br>";
				htmlString += "<br><br>";
			}
		}
		catch(Exception ex) {
			htmlString = "<html><body>" + ex.getMessage();
		}
	
}
	
	@POST
	@Consumes({"application/json"})
	public String FillRedisDatabase() {
		String returnCode = "";
		try {
			redis.set("quote:1", "I may be drunk, Miss, but in the morning I will be sober and you will still be ugly.");
			redis.set("quote:1", "You have enemies? Good. That means you've stood up for something, sometime in your life.");
			redis.set("quote:2", "No doubt exists that all women are crazy; it's only a question of degree.");
		} 
		catch (Exception ex) {
			returnCode = ex.getMessage();
		}
		
		return returnCode;
	}
	
	@GET
	@Produces({"application/json"})
	public String getRedisDB() {
		String jsonString = "";
		try {
			redis.get("quote:*");
			
		} 
		catch (Exception ex) {
			jsonString = ex.getMessage();
		}
		
		return jsonString;
	}
	
	
}