package com.burghporter314.x10host.twitter;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAccount {

	private String accountName;
	private String OAuthConsumerKey;
	private String OAuthConsumerSecret;
	private String OAuthAccessToken;
	private String OAuthAccessTokenSecret;
	
	private ConfigurationBuilder cb;
	
	private TwitterFactory factory;
	private Twitter twitter;
	
	public TwitterAccount(String accountName, String OAuthConsumerKey, String OAuthConsumerSecret,
									String OAuthAccessToken, String OAuthAccessTokenSecret) {
		
		this.accountName = accountName;
		this.OAuthConsumerKey 		= OAuthConsumerKey;
		this.OAuthConsumerSecret 	= OAuthConsumerSecret;
		this.OAuthAccessToken 		= OAuthAccessToken;
		this.OAuthAccessTokenSecret = OAuthAccessTokenSecret;
		
		setNewConfiguration();
	}
	
	public void setNewConfiguration() {
		
		/*Resets our Twitter Connection if we have new Credentials*/
		
		this.cb = new ConfigurationBuilder();
		
		this.cb.setDebugEnabled(true)
			.setOAuthConsumerKey(this.OAuthConsumerKey)
			.setOAuthConsumerSecret(this.OAuthConsumerSecret)
			.setOAuthAccessToken(this.OAuthAccessToken)
			.setOAuthAccessTokenSecret(this.OAuthAccessTokenSecret);
		
		this.factory = new TwitterFactory(cb.build());
		this.twitter = this.factory.getInstance();
		
	}
	
	public void setOAuthConsumerKey(String OAuthConsumerKey) {
		
		/*Changes ConsumerKey and Makes a New Connection*/
		
		this.OAuthConsumerKey = OAuthConsumerKey;
		setNewConfiguration();
		
	}
	
	public void setOAuthConsumerSecret(String OAuthConsumerSecret) {
		
		/*Changes ConsumerSecret and Makes a New Connection*/
		
		this.OAuthConsumerSecret = OAuthConsumerSecret;
		setNewConfiguration();
		
	}
	
	public void setOAuthAccessToken(String OAuthAccessToken) {
		
		/*Changes OAuthAccessToken and Makes a New Connection*/
		
		this.OAuthAccessToken = OAuthAccessToken;
		setNewConfiguration();
		
	}
	
	public void setOAuthAccessTokenSecret(String OAuthAccessTokenSecret) {
		
		/*Changes OAuthAccessToken and Makes a New Connection*/
		
		this.OAuthAccessTokenSecret = OAuthAccessTokenSecret;
		setNewConfiguration();
		
	}
	
	public String getAccountName() {
		
		/*Returns Account name Corresponding to Current Connection*/
		
		return this.accountName;
	}
	
	public String getOAuthConsumerKey() {
		
		/*Returns ConsumerKey Corresponding to Current Connection*/
		
		return this.OAuthConsumerKey;
	}
	
	public String getOAuthConsumerSecret() {
		
		/*Returns ConsumerKeySecret Corresponding to Current Connection*/
		
		return this.OAuthConsumerSecret;
	}
	
	public String getOAuthAccessToken() {
		
		/*Returns OAuthAccessToken Corresponding to Current Connection*/
		
		return this.OAuthAccessToken;
	}
	
	public String getOAuthAccessTokenSecret() {
		
		/*Returns OAuthAccessTokenSecret Corresponding to Current Connection*/
		
		return this.OAuthAccessTokenSecret;
	}
	
	public Twitter getTwitter() {
		
		/*Returns the Twitter Object Associated with this Connection*/
		
		return this.twitter;
	}
}
