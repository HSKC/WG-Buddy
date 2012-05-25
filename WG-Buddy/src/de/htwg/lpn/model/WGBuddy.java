package de.htwg.lpn.model;

import android.app.Application;

public class WGBuddy extends Application
{
	private String webserver = "http://wgbuddy.domoprojekt.de/";	
	
	private String user;
	private String md5Password;
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
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * @return the md5Password
	 */
	public String getMd5Password() {
		return md5Password;
	}
	/**
	 * @param md5Password the md5Password to set
	 */
	public void setMd5Password(String md5Password) {
		this.md5Password = md5Password;
	}
	
	
}
