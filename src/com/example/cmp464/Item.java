package com.example.cmp464;

import java.util.Date;

public class Item {
	
	private int id;
	private String title;
	private String description;
	private String link;
	private Date pubDate;
	
	public Item() {}
	public Item(int id) {this.id = id;}
	
	public Item(int id, String title, String description, String link,
			Date date) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.link = link;
		this.pubDate = date;
	}
	
	public int getId() {return id;}
	public String getTitle() {return title;}
	public String getDescription() {return description;}
	public String getLink() {return link;}
	public Date getDate() {return pubDate;}
	
	public void setTitle(String title) {this.title = title;}
	public void setDescription(String description) {this.description = description;}
	public void setLink(String link) {this.link = link;}
	public void setDate(Date date) {this.pubDate = date;}
	
}
