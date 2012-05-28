package de.htwg.lpn.wgbuddy;

import android.app.Application;

public class Store extends Application 
{
	private String webserver = "http://wgbuddy.domoprojekt.de/";	
	
	private String userid;
	private String username;
	private String useremail;
	private String userpwd;
	  
	private String wgname;
	private String wgpasswd;
	  
	private boolean admin= false;
	private boolean initiated = false;

	/**
	 * @return the webserver
	 */
	public String getWebserver() {
		return webserver;
	}

	/**
	 * @param webserver the webserver to set
	 */
	public void setWebserver(String webserver) {
		this.webserver = webserver;
	}

	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getWgpasswd() {
		return wgpasswd;
	}

	public void setWgpasswd(String wgpasswd) {
		this.wgpasswd = wgpasswd;
	}

	public String getWgname() {
		return wgname;
	}

	public void setWgname(String wgname) {
		this.wgname = wgname;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isInitiated() {
		return initiated;
	}

	public void setInitiated(boolean initiated) {
		this.initiated = initiated;
	}
	}