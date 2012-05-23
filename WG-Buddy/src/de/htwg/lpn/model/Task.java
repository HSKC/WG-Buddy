package de.htwg.lpn.model;

import java.sql.Date;

public class Task 
{
	private Integer id;
	private Integer wgId;
	private Integer userId;
	private String comment;
	private Integer Points;
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
	 * @return the points
	 */
	public Integer getPoints() {
		return Points;
	}
	/**
	 * @param points the points to set
	 */
	public void setPoints(Integer points) {
		Points = points;
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
	 * @return the voteDeadline
	 */
	public Date getVoteDeadline() {
		return voteDeadline;
	}
	/**
	 * @param voteDeadline the voteDeadline to set
	 */
	public void setVoteDeadline(Date voteDeadline) {
		this.voteDeadline = voteDeadline;
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
	private Date deadline;
	private Date voteDeadline;
	private Integer status;
}
