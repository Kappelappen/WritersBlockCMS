package threads;

import java.awt.*;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import dao.Database;
import model.Article;
import model.ArticleTableModel;
import windows.NewArticleDialog;

public class ArticleInfoManager extends Thread {

	private NewArticleDialog jDialogNewArticle;
	
	public ArticleInfoManager(NewArticleDialog jDialogNewArticle) {
		
		this.jDialogNewArticle = jDialogNewArticle;
				
	}
	
	private void grabArticleData() {
		
		int articleId = getArticleId();
		if (articleId == -1) return;
		
		try {
			
		this.savePhoto(articleId);
		this.saveArticle(articleId);
				
		} catch (SQLException ex) { ex.printStackTrace(); } 
			catch (BadLocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private void savePhoto(int photoId) throws SQLException {
		
		String imagePath = jDialogNewArticle.txtImagePath.getText().trim();
		
		String sql = "INSERT INTO photo (article_id," + 
		"image_path) VALUES (?,?)";
		
		Database.loadDriver();
		Connection conn = DriverManager.getConnection(Database.DB_URL);
		
		PreparedStatement stat = conn.prepareStatement(sql);
		
		stat.setInt(1, photoId);
		stat.setString(2, imagePath);
		stat.executeUpdate();
		
		stat.close();
		conn.close();
		
	}
	
	private void saveArticle(int articleId) throws 
		SQLException, BadLocationException, IOException {
	    
		HTMLEditorKit kit = (HTMLEditorKit) jDialogNewArticle.txtArticleView.getEditorKit();
	    Document doc = jDialogNewArticle.txtArticleView.getDocument();

	    StringWriter writer = new StringWriter();
	    kit.write(writer, doc, 0, doc.getLength());
	    String htmlText = writer.toString();

	    String sql = "INSERT INTO story (article_id, article_text) VALUES (?, ?)";
	    Database.loadDriver();
	    try (Connection conn = DriverManager.getConnection(Database.DB_URL);
	         PreparedStatement stat = conn.prepareStatement(sql)) {
	        stat.setInt(1, articleId);
	        stat.setString(2, htmlText);
	        stat.executeUpdate();
	    }
	}
	
	@Override
	public void run() {
		
		Runnable rbl = new Runnable() {
			
			@Override
			public void run() {
				
				grabArticleData();
				
				ArticleTableModel tableModel = (ArticleTableModel) 
					jDialogNewArticle.mWindow.jTbArticleIndex.getModel();
				
				java.util.List<Article> list = jDialogNewArticle.mWindow.getAllArticles();
	
				tableModel.setArticles(list);				
				jDialogNewArticle.mWindow.insertArticles(list);
				
			}			
		};
		
		SwingUtilities.invokeLater(rbl);
		
	}	
	
	private int getArticleId() {
		
		int numValue = -1;
		
		String title = jDialogNewArticle.txtArticleTitle.getText().trim();
		String description = jDialogNewArticle.txtDescription.getText().trim();
		String author = jDialogNewArticle.txtAuthor.getText().trim();
		String category = (String) jDialogNewArticle.jCbCategory.getSelectedItem();
		String status = (String) jDialogNewArticle.jCbStatus.getSelectedItem();
		int dateIndex = jDialogNewArticle.jCbPublishDate.getSelectedIndex();
		
		String publishDate = 
			(String) jDialogNewArticle.jCbPublishDate.getItemAt(dateIndex);
		
		try {
			
		String sql = "INSERT INTO article (title,description,author," + 
		"category,status,publish_date) VALUES (?,?,?,?,?,?)";
			
		Database.loadDriver();
		Connection conn = DriverManager.getConnection(Database.DB_URL);
			
		PreparedStatement stat = conn.prepareStatement(sql,
			PreparedStatement.RETURN_GENERATED_KEYS);
		
		stat.setString(1, title);
		stat.setString(2, description);
		stat.setString(3, author);
		stat.setString(4, category);
		stat.setString(5, status);
		stat.setString(6, publishDate);
		stat.executeUpdate();
		
		ResultSet rs = stat.getGeneratedKeys();
		
		if (rs.next()) {
			
			numValue = rs.getInt(1);
			
		}
		
		} catch (SQLException ex) { ex.printStackTrace(); }
				
		return numValue;
		
	}
}
