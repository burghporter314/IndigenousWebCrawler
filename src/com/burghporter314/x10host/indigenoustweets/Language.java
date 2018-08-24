package com.burghporter314.x10host.indigenoustweets;


/**
 * 
 * Models the IndigenousTweets.com format for a language.
 * 
 * @author Dylan Porter
 *
 */

public class Language implements Formatted{
	
	private static final int NUM_PARAMS = 7-1;

	private String url;
	private String language;
	private int users;
	private int tweets;
	private String topUser;
	private int topUserTweets;
	private IndigenousUser[] correspondingUsers;
	
	public Language(String url, String language, int users, 
							int tweets, String topUser, int topUserTweets) {
		
		this.url 			= url;
		this.language 		= language;
		this.users 			= users;
		this.tweets 		= tweets;
		this.topUser		= topUser;
		this.topUserTweets 	= topUserTweets;
		
		if(users >= 500) 	{ correspondingUsers = new IndigenousUser[500]; }
		else if(users == 0) { correspondingUsers = null; }
		else 				{ correspondingUsers = new IndigenousUser[users]; }
		
	}
	
	public Language(String url, String language) {
		this(url, language, 0, 0, "N/A", 0);
	}
	
	public Language(Object... objects) {
		this.url = (String) objects[0];
		this.language = (String) objects[1];
		this.users = (int) objects[2];
		this.tweets = (int) objects[3];
		this.topUser = (String) objects[4];
		this.topUserTweets = (int) objects[5];
	}
	
	public Language(String url) {
		this(url, "Unknown Language", 0, 0, "N/A", 0);
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public void setUsers(int users) {
		this.users = users;
	}
	
	public void setTweets(int tweets) {
		this.tweets = tweets;
	}
	
	public void setTopUser(String topUser) {
		this.topUser = topUser;
	}
	
	public void setTopUserTweets(int topUserTweets) {
		this.topUserTweets = topUserTweets;
	}
	
	public void setCorrespondingUsers(IndigenousUser[] correspondingUsers) {
		this.correspondingUsers = sort(correspondingUsers, 0);
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getLanguage() {
		return this.language;
	}
	
	public int getUsers() {
		return this.users;
	}
	
	public int getTweets() {
		return this.tweets;
	}
	
	public String getTopUser() {
		return this.topUser;
	}
	
	public int getTopUserTweets() {
		return this.topUserTweets;
	}
	
	public IndigenousUser[] getCorrespondingUsers() {
		return this.correspondingUsers;
	}
	
	public IndigenousUser[] sort(IndigenousUser[] correspondingUsers, int index) {
		
		if(index == correspondingUsers.length - 1) return correspondingUsers;
		
		for(int i = index+1; i < correspondingUsers.length; i++) {
			if(correspondingUsers[index].compareTo(correspondingUsers[i]) < 0) {
				IndigenousUser temp = correspondingUsers[index];
				correspondingUsers[index] = correspondingUsers[i];
				correspondingUsers[i] = temp;
			} 
		}
		
		return sort(correspondingUsers, ++index);
		
	}

	@Override
	public Parameter[] getParameters() {
		
		Parameter[] params = {
				new Parameter<String>(this.url),
				new Parameter<String>(this.language),
				new Parameter<Integer>(this.users),
				new Parameter<Integer>(this.tweets),
				new Parameter<String>(this.topUser),
				new Parameter<Integer>(this.topUserTweets),
		};
		
		return params;
		
	}

	@Override
	public int getNumParameters() {
		return this.NUM_PARAMS;
	}
}
