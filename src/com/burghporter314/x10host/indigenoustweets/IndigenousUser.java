package com.burghporter314.x10host.indigenoustweets;

public class IndigenousUser implements Formatted, Comparable <IndigenousUser> {
	
	private static final int NUM_PARAMS = 6;
	
	private String userUrl;
	private int tweetsInLanguage;
	private int totalTweets;
	private double percentLanguage;
	private int followers;
	private int following;
	private String lastTweetDate;
	
	public IndigenousUser(String userUrl, int tweetsInLanguage, int totalTweets, double percentLanguage,
											int followers, int following, String lastTweetDate) {
		
		this.userUrl 			= userUrl;
		this.tweetsInLanguage 	= tweetsInLanguage;
		this.totalTweets 		= totalTweets;
		this.percentLanguage 	= percentLanguage;
		this.followers 			= followers;
		this.following 			= following;
		this.lastTweetDate 		= lastTweetDate;
		
	}
	
	public IndigenousUser(Object... objects) {
		this.userUrl 			= (String) objects[0];
		this.tweetsInLanguage 	= (int) objects[1];
		this.totalTweets 		= (int) objects[2];
		this.percentLanguage 	= (double) objects[3];
		this.followers 			= (int) objects[4];
		this.following 			= (int) objects[5];
		this.lastTweetDate 		= (String) objects[6];
	}
	
	public void setTweetsInLanguage(int tweetsInLanguage) {
		this.tweetsInLanguage = tweetsInLanguage;
	}
	
	public void setTotalTweets(int totalTweets) {
		this.totalTweets = totalTweets;
	}
	
	public void setPercentLanguage(Double percentLanguage) {
		this.percentLanguage = percentLanguage;
	}
	
	public void setFollowers(int followers) {
		this.followers = followers;
	}
	
	public void setFollowing(int following) {
		this.following = following;
	}
	
	public void setLastTweetDate(String lastTweetDate) {
		this.lastTweetDate = lastTweetDate;
	}
	
	public String getUserUrl() {
		return this.userUrl;
	}
	
	public int getTweetsInLanguage() {
		return this.tweetsInLanguage;
	}
	
	public int getTotalTweets() {
		return this.totalTweets;
	}
	
	public double getPercentLanguage() {
		return this.percentLanguage;
	}
	
	public int getFollowers() {
		return this.followers;
	}
	
	public int getFollowing() {
		return this.following;
	}
	
	public String getLastTweetDate() {
		return this.lastTweetDate;
	}

	@Override
	public Parameter[] getParameters() {
		
		Parameter[] params = {
				new Parameter<Integer>(this.tweetsInLanguage),
				new Parameter<Integer>(this.totalTweets),
				new Parameter<Double>(this.percentLanguage),
				new Parameter<Integer>(this.followers),
				new Parameter<Integer>(this.following),
				new Parameter<String>(this.lastTweetDate)
		};
		
		return params;
	}

	@Override
	public int getNumParameters() {
		return this.NUM_PARAMS;
	}

	@Override
	public int compareTo(IndigenousUser o) {
		
		if(this.percentLanguage > o.percentLanguage) {
			return 1;
		} else if(this.percentLanguage < o.percentLanguage) {
			return -1;
		}
		
		return 0;
	}
	
}
