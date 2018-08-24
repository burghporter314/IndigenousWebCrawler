package com.burghporter314.x10host.indigenoustweets;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.burghporter314.x10host.graphicaluserinterface.GUIComponent;
import com.burghporter314.x10host.twitter.*;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

public class Main {
	
	private  static Logger LOGGER = Logger.getLogger(Main.class.getName());;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		/*I hated the log format, so I changed it*/
		System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-4s] %5$s %n");	
		
		/*All Command Line Arguments Needed*/
		final int USERS_PER_LANGUAGE 				= Integer.valueOf(parseArgument(args[0]));
		int AMOUNT_OF_LANGUAGES_PROCESSED 			= Integer.valueOf(parseArgument(args[1]));
		final int TWEETS_PER_USER 					= Integer.valueOf(parseArgument(args[2]));
		
		final boolean INCLUDE_RETWEETS 				= Boolean.valueOf(parseArgument(args[3]));
		final String DIRECTORY_PATH 				= parseArgument(args[4]);
		final String INPUT_FILE_PATH_CREDENTIALS 	= parseArgument(args[5]);
		final String INPUT_FILE_PATH_WELCOME 		= parseArgument(args[6]);
		final String INPUT_FILE_PATH_LOG			= parseArgument(args[7]);
		
		GUIComponent component = new GUIComponent();

		FileHandler fh = new FileHandler(INPUT_FILE_PATH_LOG, true);
		LOGGER.addHandler(fh);
		
		SimpleFormatter formatter = new SimpleFormatter();
		fh.setFormatter(formatter);
		
		/*Display Welcome Message*/
		Scanner input = new Scanner(new File(INPUT_FILE_PATH_WELCOME));
		while(input.hasNextLine()) { System.out.println(input.nextLine()); }
		
		TimeUnit.SECONDS.sleep(5);
		
		/*This initializes all of the connections to IndigenousTweets.com*/
		SiteConnection connection = new SiteConnection("http://indigenoustweets.com", "tbody tr", "tbody tr", LOGGER);
		
		/*Utilize Twitter API*/
		TwitterCredentialParser parser = new TwitterCredentialParser(INPUT_FILE_PATH_CREDENTIALS, LOGGER);
		
		/*Use our current Account Connection to Perform Operations*/
		TwitterOperator operator = new TwitterOperator(parser.getCurrentConfiguration(), LOGGER);
		
		/*Create a new File Writer that we be used later*/
		TwitterFileWriter writer = new TwitterFileWriter();
		
		/*Main Program*/
		try {
			
			LOGGER.info("Program Started");
			
			/*Get all of the languages collected from indigenoustweets.com*/
			Language[] languages = connection.getLanguages();
			component.setListComponents(languages, "Select Items");

			if(AMOUNT_OF_LANGUAGES_PROCESSED > languages.length) { AMOUNT_OF_LANGUAGES_PROCESSED = languages.length; } 
			
			/*Iterate through specified number of languages, adjust final field if necessary*/
			for(int i = 0; i < AMOUNT_OF_LANGUAGES_PROCESSED; i++) {
				
				LOGGER.info("Processing Language: " + languages[i].getLanguage());
				
				/*All the users belonging to the current language that we are iterating through*/
				IndigenousUser[] users = languages[i].getCorrespondingUsers();
				
				/*Iterate through each user, create a directory for them, and store their tweets in JSON format*/
				for(int z = 0; z < USERS_PER_LANGUAGE; z++) {
					
					String user = "";
					/*Write their tweets to a text file -- if our rate limit is exceeded, pause the program for 5 mins*/
					try {
						
						/*This extracts the username from the twitterURL http://twitter.com/name --> name*/
						user = users[z].getUserUrl().replace("http://twitter.com/","");
						LOGGER.info("Processing User: " + user);
						
						/*Append Tweets in JSON format*/
						writer.setDirectory(DIRECTORY_PATH + languages[i].getLanguage(), user + ".txt");
						
						Map<Status, String[]> map = operator.getFilteredTweets(INCLUDE_RETWEETS, user, 1, TWEETS_PER_USER);
						for(Status status: map.keySet()) {
							writer.appendLine(operator.convertWordArray(map.get(status)));
						}
						
						/*ResponseList<Status> list = operator.getTweets(INCLUDE_RETWEETS, user, 1, TWEETS_PER_USER);
						writer.appendTweets(list);*/
						
						writer.closeFileWriter();
						
					} catch (java.lang.ArrayIndexOutOfBoundsException e) {
						
						/*Not enough users in the Language*/
						LOGGER.info(languages[i].getLanguage() + " does not have enough users to meet the specifications, moving on to the next language.");
						break;
						
					} catch(TwitterException e) {
						
						/*Two Errors could Occur, so check to see if it was a rate limit problem*/
						if(e.exceededRateLimitation()) {
							
							/*If Rate Limit Exceeded, Switch to Other Account*/
							LOGGER.info("Rate Limited Exceeded for account: " + 
									parser.getCurrentConfiguration().getAccountName() + 
									" Switching to Account: " + parser.getNextConfiguration().getAccountName());
							
							operator = new TwitterOperator(parser.getCurrentConfiguration(), LOGGER);
							TimeUnit.SECONDS.sleep(5);
							
							/*Revert Back to the Attempted User that Failed since Rate Limit Exceeded*/
							z--;
							
						} else {
							LOGGER.info("User: " + user + " could not be processed! Account is most likely Deleted.");
						}
					}
				}
			}
			
			LOGGER.info("Program Completed");
			
		} catch(Exception e) {
			LOGGER.severe("Unknown Error in Main Program");
			e.printStackTrace();
		}
	}
	
	public static String parseArgument(String argument) { return argument.substring(argument.indexOf("=")+1).trim(); }

}
