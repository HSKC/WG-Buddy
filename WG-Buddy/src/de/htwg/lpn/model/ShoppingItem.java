package de.htwg.lpn.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingItem extends ObjectBase
{
	public ShoppingItem(HashMap<String, String> map) 
	{
		super(map);
		// TODO Auto-generated constructor stub
	}
	private Integer id;
	private Integer wgId;
	private Integer userId;
	private String name;
	private String comment;
	private Integer rating;
	private Date createdDate;
	private Date deadline;
	private Integer status;
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
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return the rating
	 */
	public Integer getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(Integer rating) {
		this.rating = rating;
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
	 * @return the deadline
	 */
	public Date getDeadline() {
		return deadline;
	}
	/**
	 * @param deadline the deadline to set
	 */
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
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
	
	public static ArrayList<HashMap<String, String>> get()
	{
		String url = "http://wgbuddy.domoprojekt.de/shopping.php";
		return JSON.getMapListOfJsonArray(url, "Item");		
	}
	
}
