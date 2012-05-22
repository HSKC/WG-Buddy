package de.htwg.lpn.model;

import java.sql.Date;

public class User 
{
	private Integer id;
	private Integer wgId;
	private String username;
	private String forename;
	private String surname;
	private String email;
	private String password;
	private String changeKey;
	private Date changeTimestamp;
	private Integer points;
	private Date createdDate;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the wgId
	 */
	public Integer getWgId() {
		return wgId;
	}
	/**
	 * @param wgId the wgId to set
	 */
	public void setWgId(Integer wgId) {
		this.wgId = wgId;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the forename
	 */
	public String getForename() {
		return forename;
	}
	/**
	 * @param forename the forename to set
	 */
	public void setForename(String forename) {
		this.forename = forename;
	}
	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the changeKey
	 */
	public String getChangeKey() {
		return changeKey;
	}
	/**
	 * @param changeKey the changeKey to set
	 */
	public void setChangeKey(String changeKey) {
		this.changeKey = changeKey;
	}
	/**
	 * @return the changeTimestamp
	 */
	public Date getChangeTimestamp() {
		return changeTimestamp;
	}
	/**
	 * @param changeTimestamp the changeTimestamp to set
	 */
	public void setChangeTimestamp(Date changeTimestamp) {
		this.changeTimestamp = changeTimestamp;
	}
	/**
	 * @return the points
	 */
	public Integer getPoints() {
		return points;
	}
	/**
	 * @param points the points to set
	 */
	public void setPoints(Integer points) {
		this.points = points;
	}
	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the lastTimeActive
	 */
	public Date getLastTimeActive() {
		return lastTimeActive;
	}
	/**
	 * @param lastTimeActive the lastTimeActive to set
	 */
	public void setLastTimeActive(Date lastTimeActive) {
		this.lastTimeActive = lastTimeActive;
	}
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	private Date lastTimeActive;
	private Integer status;

}
