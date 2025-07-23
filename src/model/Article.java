package model;

import java.sql.*;

import dao.Database;

public class Article {
	
	private int articleId;
	private String title;
	private String description;
	private String author;
	private String category;
	private String status;
	private String publishDate;
	private String articleImage;
	private String articleText;
	private boolean flagged;   // Nytt fält för flaggning
	
	public Article() {}
	
	public Article(int articleId, 
		String title, String description, 
		String author, String category, 
		String status, String publishDate, 	
		String articleImage,
		String articleText,
		boolean flagged) {      // flaggad också
		
		this.articleId = articleId;
		this.title = title;
		this.description = description;
		this.author = author;
		this.category = category;
		this.status = status;
		this.publishDate = publishDate;
		this.articleImage = articleImage;
		this.articleText = articleText;
		this.flagged = flagged;
	}
	
	// setters och getters
	public void setArticleId(int id) { this.articleId = id; }
	public void setTitle(String title) { this.title = title; }
	public void setDescription(String desc) { this.description = desc; }
	public void setAuthor(String author) { this.author = author; }
	public void setCategory(String category) { this.category = category; }
	public void setStatus(String status) { this.status = status; }
	public void setPublishDate(String date) { this.publishDate = date; }
	public void setArticleImage(String image) { this.articleImage = image; }
	public void setArticleText(String text) { this.articleText = text; }
	public void setFlagged(boolean flagged) { this.flagged = flagged; }
	
	public int getArticleId() { return articleId; }
	public String getTitle() { return title; }
	public String getDescription() { return description; }
	public String getAuthor() { return author; }
	public String getCategory() { return category; }
	public String getStatus() { return status; }
	public String getPublishDate() { return publishDate; }
	public String getArticleImage() { return articleImage; }
	public String getArticleText() { return articleText; }
	public boolean isFlagged() { return flagged; }
	
	public int doCountArticles() {
		
		int number = -1;
		
		try {
			
		Database.loadDriver();
		
		String sql = "SELECT COUNT(*) AS COUNTER FROM article";
		Connection conn = DriverManager.getConnection(Database.DB_URL);
		Statement stat = conn.createStatement();		
		ResultSet rs = stat.executeQuery(sql);
		
		if (rs.next()) {
			
			number = rs.getInt("COUNTER");
			
		}
		
		conn.close();
		stat.close();
		rs.close();
		
		} catch (SQLException ex) { ex.printStackTrace(); }
		
		return number;
		
	}
	
	public int getPublishedArticles() {
		
		int number = -1;
		
		try {
			
		Database.loadDriver();
		
		String sql = "SELECT COUNT(*) AS COUNTER " + 
		"FROM article WHERE status = 'Published'";
		
		Connection conn = DriverManager.getConnection(Database.DB_URL);
		Statement stat = conn.createStatement();		
		ResultSet rs = stat.executeQuery(sql);
		
		if (rs.next()) {
			
			number = rs.getInt("COUNTER");
			
		}
		
		conn.close();
		stat.close();
		rs.close();
		
		} catch (SQLException ex) { ex.printStackTrace(); }
		
		return number;
		
	}
	
	// Kollar om status är "Published"
	public boolean isPublished() {
		return "Published".equalsIgnoreCase(status);
	}
	
	@Override
	public String toString() { return title; }
}
