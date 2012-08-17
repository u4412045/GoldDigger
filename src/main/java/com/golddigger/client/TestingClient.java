package com.golddigger.client;

import java.io.IOException;
import java.net.MalformedURLException;

import org.xml.sax.SAXException;

import com.golddigger.model.Direction;
import com.meterware.httpunit.WebConversation;

/**
 * Simple HTML Client for the server, build for testing a running server.
 * @author Brett
 *
 */
public class TestingClient {
	private WebConversation wc;
	private String player, baseURL;
	
	/**
	 * Create a new TestingClient for a particular player.
	 * @param player the players name (used in the url)
	 * @param baseURL the base url of the server (everything before the servlet's context).
	 */
	public TestingClient(String player, String baseURL){
		this.player = player;
		this.baseURL = baseURL;
		wc = new WebConversation();
	}
	/**
	 * check how many gold your unit is carrying
	 * @return the number of gold
	 */
	public String carrying(){
		return doGET("/carrying");
	}
	
	/**
	 * get your score
	 * @return the number of gold you have banked
	 */
	public String score(){
		return doGET("/score");
	}
	
	/**
	 * move to the next game
	 * @return "OK" if successful, "FAILED" otherwise
	 */
	public String next(){
		return doGET("/next");
	}
	
	/**
	 * grab gold on the ground
	 * @return the number of gold you have picked up, "FAILED" you couldn't pick any up.
	 */
	public String grab(){
		return doGET("/grab");
	}
	
	/**
	 * drop gold on the ground
	 * @return the number of gold you dropped, "FAILED" if you couldn't drop any
	 */
	public String drop(){
		return doGET("/drop");
	}
	
	/**
	 * Get the diggers view
	 * @return String representation of the view
	 */
	public String view(){
		return doGET("/view");
	}
	
	/**
	 * Move the digger
	 * @param d the direction you want to move
	 * @return "OK" if successful, "FAILED" otherwise
	 */
	public String move(Direction d){
		return doGET("/move/"+d.toString());
	}

	private String doGET(String action){
		try {
			return wc.getResponse(baseURL+"/golddigger/digger/"+player+action).getText();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return null;
	}
}