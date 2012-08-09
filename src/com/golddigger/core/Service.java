package com.golddigger.core;

import java.io.PrintWriter;

import com.golddigger.core.AppContext;

/**
 * The Base Specification of all services
 * 
 * A Service is an object that reacts to a client's connection to the server.
 * The Service uses the incoming request URL to determine if it wants to react.
 * If a Service "consumes" this request, then other services arn't run, unless
 * they specify that they do not care about consumption.
 *  
 * @author Brett Wandel
 */
public abstract class Service implements Comparable<Service>{
	public static final int BASE_PRIORITY = -1;
	
	/**
	 * The offset for specific components of a URL
	 */
	public static final int URL_HOST = 0,
							URL_CONTEXT= 1,
							URL_TARGET = 2,
							URL_PLAYER = 3,
							URL_ACTION = 4,
							URL_EXTRA1 = 5,
							URL_EXTRA2 = 6,
							URL_EXTRA3 = 7;
	
	private int priority = BASE_PRIORITY;
	/**
	 * Used to allow a service to run before other services that would normally run
	 * @return The priority level, higher runs before lower
	 */
	public int getPriority(){ return this.priority;}
	public Service(int priority){
		this.priority = priority;
	}
	
	/**
	 * Determines if this service wants to do something with an incoming URL
	 * @param url The URL to check
	 * @return True if yes, False if no
	 */
	public abstract boolean runnable(String url);
	
	/**
	 * Let this service execute <br />
	 * At this stage, the following has already been checked: <br />
	 * <ol>
	 * 	<li>The player in the URL does exist</li>
	 *  <li>The player is in a valid game</li>
	 * </ol>
	 * @param url The incoming URL
	 * @param out The PrintWriter to use to return information to the Competitor
	 * @return <b>true</b> if this service "consumed" the request
	 */
	public abstract boolean execute(String url, PrintWriter out);
	
	/**
	 * Used to allow a service run even if another service has "consumed" the request.
	 * @return
	 */
	public boolean caresAboutConsumption(){
		return true;
	}
	
	/**
	 * Used to accurately decompose the url into its components.
	 * @param url The URL to decompose
	 * @param component The component that you want
	 * @return The value of that component
	 */
	public static String parseURL(String url, int component){
		String[] x = url.split("/"); 
		if (x.length < component+1) return null;
		else return x[component];
	}
	
	public static String getContextIDFromURL(String url){
		return parseURL(url, URL_HOST)+"/"+parseURL(url, URL_CONTEXT);
	}
	
	public static AppContext getContextFromURL(String url){
		return AppContext.getContext(getContextIDFromURL(url));
	}
	
	@Override
	public int compareTo(Service s){
		return s.getPriority() - priority;
	}
}
