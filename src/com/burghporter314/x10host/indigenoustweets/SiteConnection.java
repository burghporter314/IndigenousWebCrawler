package com.burghporter314.x10host.indigenoustweets;

import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SiteConnection {
	
	private String url;
	private String query;
	private String queryUsers;
	private Document doc;
	private Language[] languages;
	private Logger LOGGER;
	
	public SiteConnection(String url, String query, String queryUsers, Logger logger) {
		this.LOGGER = logger;
		connect(url, query, queryUsers);
	}
	
	public void connect(String url, String query, String queryUsers) {
		try {
			
			this.url = url;
			this.query = query;
			this.queryUsers = queryUsers;
			this.doc = Jsoup.connect(url).get();
			setLanguages();
			setUsers();
			
		} catch(Exception e) {
			
			System.err.println("Error: Connection Failed");
			e.printStackTrace();
			
		}

	}
	
	public Elements getQuerySelectResult(String query) {
		return doc.select(query); 
	}
	
	public Language[] getLanguages() {
		return this.languages;
	}
	
	/**
	 * 
	 * Collect All Languages on the IndigenousTweets.com Site
	 * We need url and name of the language -- I collect other
	 * attributes for safe-keeping.
	 * 
	 */
	
	private void setLanguages() {
		
		Elements elems = this.doc.select(this.query);
		int size = elems.size();
		
		this.languages = new Language[size];
		
		for(int i = 0; i < size; i++) {
			
			Elements children = elems.get(i).children();
			
			this.LOGGER.info("Added language: " + children.select("a").get(0).text());
			this.languages[i] = new Language(
					children.select("a").get(0).absUrl("href"),
					children.select("a").get(0).text(),
					Integer.parseInt(children.get(1).text()),
					Integer.parseInt(children.get(2).text()),
					children.get(3).text(),
					Integer.parseInt(children.get(4).text()));
			
		}
		
	}
	
	/**
	 * 
	 * Collect all the users for each language Collected
	 * Lesson search query for less memory load.
	 * 
	 */
	
	private void setUsers() {
		
		for(Language language : this.languages) {
			try {
				
				this.LOGGER.info("Finding all users under: " + language.getLanguage());
				this.doc = Jsoup.connect(language.getUrl()).get();
				
				/*Process Each User in the Language*/
				Elements elems = this.doc.select(this.queryUsers);
				int size = elems.size();
				
				/*Collect all of the IndigenousUsers for later Twitter API Use*/
				IndigenousUser[] users = new IndigenousUser[size];
				
				/*Iterate through all users and store them in Objects*/
				for(int i = 0; i < size; i++) {

					Elements children = elems.get(i).children();

					users[i] = new IndigenousUser(
							children.select("a").get(0).absUrl("href"),
							Integer.parseInt(children.get(3).text()),
							Integer.parseInt(children.get(4).text()),
							Double.parseDouble(children.get(5).text()),
							Integer.parseInt(children.get(6).text()),
							Integer.parseInt(children.get(7).text()),
							children.get(8).text()
					);
					
				}
				
				language.setCorrespondingUsers(users);

			} catch (Exception e) {
				this.LOGGER.severe("ERROR: setUsers() method failed to connect with " + language.getUrl());
				e.printStackTrace();
			}
		}
	}
}

