package com.burghporter314.x10host.twitter;

import java.io.FileReader;
import java.util.Map;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public class TwitterCredentialParser {
	
	private TwitterAccount[] accounts;
	private int numberOfConfigurations;
	private static int currentConfiguration = 0;
	
	private TwitterFactory tf;
	private Twitter twitter;
	
	private JSONParser parser;
	private JSONArray credentials;
	
	private Logger LOGGER;
	
	public TwitterCredentialParser(String fileLoc,Logger LOGGER) {
		this.LOGGER = LOGGER;
		parseConfiguration(fileLoc);
	}
	
	private void parseConfiguration(String fileLoc) {

		this.parser = new JSONParser();
		
		try {
			
			this.LOGGER.info("Parsing Twitter Configuration File... ");
			JSONArray configurations = (JSONArray) parser.parse(new FileReader(fileLoc));
			
			this.numberOfConfigurations = configurations.size();
			this.accounts = new TwitterAccount[this.numberOfConfigurations];
			
			int placeholder = 0;
			
			for(Object obj : configurations) {
				
				JSONObject configuration = (JSONObject) obj;
				this.LOGGER.info("Processing Twitter Account: " + configuration.get("account_name"));
				this.LOGGER.info("OAuthConsumerKey: " + configuration.get("OAuthConsumerKey"));
				this.LOGGER.info("OAuthConsumerSecret: " + configuration.get("OAuthConsumerSecret"));
				this.LOGGER.info("OAuthAccessToken: " + configuration.get("OAuthAccessToken"));
				this.LOGGER.info("OAuthAccessTokenSecret: " + configuration.get("OAuthAccessTokenSecret"));
				
				this.accounts[placeholder++] = new TwitterAccount(
						(String)configuration.get("account_name"),
						(String)configuration.get("OAuthConsumerKey"),
						(String)configuration.get("OAuthConsumerSecret"),
						(String)configuration.get("OAuthAccessToken"),
						(String)configuration.get("OAuthAccessTokenSecret")
				);
				
			}
			
		} catch (Exception e) {
		   this.LOGGER.severe("ERROR: Could not Parse JSON File");
		   e.printStackTrace();
		}
		
	}
	
	public TwitterAccount getNextConfiguration() {
		
		/*Get the next Configuration if there is one Left*/
		
		this.currentConfiguration++;
		
		if(this.currentConfiguration >= this.numberOfConfigurations) {
			this.currentConfiguration = 0;
		}
		
		return this.accounts[currentConfiguration];
		
	}
	
	public TwitterAccount getCurrentConfiguration() {
		
		/*Get the Current Account Configuration for Twitter*/
		
		return this.accounts[this.currentConfiguration];
	}
	
	public int getConfigurationNum() {
		
		/*Amount of Twitter Accounts Available*/
		
		return this.numberOfConfigurations;
	}
	
}
