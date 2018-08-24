package com.burghporter314.x10host.twitter;

import java.io.File;
import java.io.PrintWriter;
import java.util.logging.Logger;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import twitter4j.ResponseList;
import twitter4j.Status;

public class TwitterFileWriter {
	
	private PrintWriter writer;
	private Logger LOGGER;
	
	private File directory;
	private String fileName;
	
	private ObjectWriter ow;

	public TwitterFileWriter(File directory, String fileName, Logger LOGGER) {
		
		try {
			
			/*If the Directory does not exist*/
			directory.mkdirs();
			
			/*Initialize logger and writer -- we will write in UTF-16 for all characters*/
			this.LOGGER = LOGGER;
			
			/*Set the Directory and FileName for this Object Instance*/
			this.directory = directory;
			this.fileName = fileName;
			
			/*Create the Writer and JSON Writer*/
			this.ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			initWriter(this.directory, this.fileName);
			
		} catch(Exception e) {
			
			/*Fatal Error Occurred*/
			this.LOGGER.severe("ERROR: Could not Create File Writer");
			e.printStackTrace();
			
		}
		
	}
	
	public TwitterFileWriter(String directory, String fileName, Logger LOGGER) {
		this(new File(directory), fileName, LOGGER);
	}
	
	public TwitterFileWriter() {
		/*Create the JSON Writer*/
		this.ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	}
	
	public void setDirectory(String directory, String fileName) {
		
		/*Need a new file object to reset writer*/
		File directoryFile = new File(directory);
		
		/*Set Global Variables Appropriately*/
		this.directory = directoryFile;
		this.fileName = fileName;
		
		/*Reset the Writer*/
		initWriter(directoryFile, fileName);
		
	}
	
	public void setFileName(String fileName) {
		
		/*Reset the Writer and the FileName*/
		this.fileName = fileName;
		initWriter(this.directory, this.fileName);
		
	}
	
	/*Returns the Current Directory*/
	public File getDirectory() {
		return this.directory;
	}
	
	/*Returns the Current File Name*/
	public String getFileName() {
		return this.fileName;
	}
	
	/*Closes the Current File Writer*/
	public void closeFileWriter() {
		this.writer.close();
	}
	
	/*Writes a new line to File Writer*/
	public void appendLine(String line) {
		this.writer.println(line);
	}
	
	/*Writes text without adding a new line*/
	public void appendText(String text) {
		this.writer.print(text);
	}
	
	/*Writes a new Tweet in JSON format to FileWriter*/
	public void appendTweets(Object status) {
		try {
			String json = ow.writeValueAsString(status);
			this.writer.println(json);
		} catch(Exception e) {
			this.LOGGER.severe("ERROR: Could not write status to JSON Format");
			e.printStackTrace();
		}

	}
	
	private void initWriter(File directory, String fileName) {
		try {
			
			/*Reset Writer*/
			directory.mkdirs();
			this.writer = new PrintWriter(directory.getAbsolutePath() + "\\" +  fileName, "UTF-16");
			
		} catch(Exception e) {
			
			/*Fatal Error Occurred*/
			this.LOGGER.severe("ERROR: Could not Create File Writer");
			e.printStackTrace();
			
		}
	}
	
}
