package com.burghporter314.x10host.twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterOperator {

	private TwitterAccount twitterAccount;
	private Twitter twitter;
	private Logger LOGGER;
	
	public TwitterOperator(TwitterAccount twitterAccount, Logger LOGGER) {
		
		/*Initialize with the Corresponding Twitter Configuration*/
		this.LOGGER = LOGGER;
		this.twitterAccount = twitterAccount;
		this.twitter = twitterAccount.getTwitter();
		
	}
	
	public TwitterOperator(Twitter twitter, Logger LOGGER) {
		this.LOGGER = LOGGER;
		this.twitter = twitter;
	}
	
	public ResponseList<Status> getTweets(boolean includeRetweets, String user, Object... params) throws Exception {

		/*Create a paging query and return tweets associate with page*/
		Paging page = this.getPage(params);
		
		/*Check to see if we should include retweets or not*/
		if(includeRetweets) {
			return this.twitter.getUserTimeline(user, page);
		}
		
		/*Create a Separate instance of the Response Received*/
		ResponseList<Status> responses = this.twitter.getUserTimeline(user, page);
		
		/*Filter out Retweets*/
		for(Status status : this.twitter.getUserTimeline(user, page)) {
			if(status.isRetweet()) {
				responses.remove(status);
			}
		}
		
		return responses;

	}
	
	public Map<Status, String[]> getFilteredTweets(boolean includeRetweets, String user, Object... params) throws TwitterException {
		

		/*Create a paging query and return tweets associate with page*/
		Paging page = this.getPage(params);
		
		/*Create a Separate instance of the Response Received*/
		ResponseList<Status> responses = this.twitter.getUserTimeline(user, page);
		
		/*This will map a Tweet to a Filtered Text*/
		Map<Status, String[]> map = new HashMap<Status, String[]>();
		
		for(Status status: responses) {
			
			/*Skip over query of the user does not want retweets*/
			if(!includeRetweets && (status.isRetweet() 
					|| status.getText().contains("“") || status.getText().contains("\""))) {
				continue;
			}
			
			/*This Maps a status to an array of words in the tweet \\W+ means all non-wordcharacters occuring 1+ times*/
			map.put(status, getWordsFromString(status.getText()));
		}
		
		return map;

	}
	
	public double getRetweetToTweetRatio(String user, int granularity) {
		
		/*Calculate Retweet to Tweet Ratio*/
		double retweets = 0, nonRetweets = 0;
		
		/*Go through past Tweets and figure out ratio*/
		try {
			for(Status status : this.twitter.getUserTimeline(user, new Paging(1, granularity))) {
				if(status.isRetweet()) {
					retweets++;
				} else {
					nonRetweets++;
				}
			}
		} catch(Exception e) {
			this.LOGGER.severe("ERROR: Could not find Tweet Ratio");
			e.printStackTrace();
		}

		/*We are avoiding Division by Zero here -- returning -1 indicates all retweets, 0 means all nonRetweets*/
		return((nonRetweets == 0) ? -1 : retweets / nonRetweets);
		
	}
	
	public String convertWordArray(String[] arr) {
		
		String temp = "";
		
		/*Iterate through all elements and append word to temp*/
		for(int i = 0; i < arr.length; i++) {
			temp += arr[i] + " ";
		}
		
		return temp;
	}
	
	private Paging getPage(Object... params) {
		
		/*Determines the appropriate paging type based on params length*/
		return ((params.length == 1) ? new Paging((int)params[0]) : (params.length == 2) 
									 ? new Paging((int)params[0], (int)params[1]) : null);
		
	}
	
	private String[] getWordsFromString(String words) {
		
		/*Utilize Regular Expressions to filter String*/

		return words.replaceAll("[\\w]*[\\W]*(https?|ftp|file):\\/\\/[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "")
					.replaceAll("\\W(\\s|\n|$)", " ")
					.replaceAll("^\\W+", "")
					.replaceAll("@[^ ]+", "")
					.replaceAll("^ +", "")
					.replaceAll(" +$", "")
					.replaceAll("(\\s|^|\\W)[0-9]+(\\s|$|\\W)", " ")
					.split("\\W{2,}");
		
	}
	
}
