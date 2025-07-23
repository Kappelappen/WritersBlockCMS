package panels;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import components.BasicTextField;
import dao.Database;
import model.Article;
import model.ArticleTableModel;
import windows.MainWindow;

public class SearchPanel extends JPanel {
	
	private MainWindow mWindow;
	
	private JLabel lblSearch;
	private JTextField txtSearch;
	
	private GridBagConstraints gbc;
	private GridBagLayout gbl;
	
	public SearchPanel(MainWindow mWindow) {
		
		this.mWindow = mWindow;
		
		this.initialize();
		this.registerEvents();
		this.configJPanel();
		this.createLayout();
		
	}
	
	private void initialize() {
				
		this.lblSearch = new JLabel("Search");
		
		this.txtSearch = new BasicTextField(0);
		
		this.gbc = new GridBagConstraints();
		this.gbl = new GridBagLayout();		
		
	}
	
	private void registerEvents() {
		
		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
		
			public void insertUpdate(DocumentEvent e) { doFilterTable(); }
		    public void removeUpdate(DocumentEvent e) { doFilterTable(); }
		    public void changedUpdate(DocumentEvent e) { doFilterTable(); }
		
		});		
	}
	
	private void configJPanel() {
		
		this.setBorder(null);
		this.setLayout(gbl);
		
	}
	
	private void createLayout() {
		
		gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.NONE;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(6,6,0,6);
        gbl.setConstraints(lblSearch, gbc);
        this.add(lblSearch);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(6,0,0,6);
        gbl.setConstraints(txtSearch, gbc);
        this.add(txtSearch);
		
	}
	
	private void doFilterTable() {
		
		String text = txtSearch.getText();
        java.util.List<Article> filtered = loadFilteredArticles(text);
        
        ArticleTableModel model = 
        	(ArticleTableModel) mWindow.jTbArticleIndex.getModel();
		
        model.setArticles(filtered);
        model.fireTableDataChanged();
                
	}
	
	private java.util.List<Article> loadFilteredArticles(String searchText) {
		
		java.util.List<Article> result = new ArrayList<Article>();
		
	    String sql = "SELECT * FROM article";
	    
	    if (searchText != null || !searchText.trim().isEmpty()) {
	    	
	        sql += " WHERE title LIKE ? OR author LIKE ? OR category LIKE ?";
	    
	        try {
	    	
	            String pattern = "%" + searchText.trim() + "%";
	        	
	        	Database.loadDriver();
	        	Connection conn = DriverManager.getConnection(Database.DB_URL);
	        	PreparedStatement stat = conn.prepareStatement(sql);	    
	        	stat.setString(1, pattern);
	        	stat.setString(2, pattern);
	        	stat.setString(3, pattern);
	        	
	        	ResultSet rs = stat.executeQuery();
	        	
	        	while (rs.next()) {
	        		
	        		Article a = new Article();
	                a.setArticleId(rs.getInt("id"));
	                a.setTitle(rs.getString("title"));
	                a.setAuthor(rs.getString("author"));
	                a.setCategory(rs.getString("category"));
	                a.setStatus(rs.getString("status"));
	                a.setPublishDate(rs.getString("publish_date"));
	                result.add(a);
	        		
	        	}	
	        	
	        	rs.close();
	        	stat.close();
	        	conn.close();
	        	
	        } catch (SQLException ex) { ex.printStackTrace(); }		
	    }
	        
		return result;
		
	}
}
