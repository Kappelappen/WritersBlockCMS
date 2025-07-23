package panels;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import dao.Database;
import model.Article;
import windows.MainWindow;

public class ArticlePanel extends JPanel {
	
	private MainWindow mWindow;
	
	public JEditorPane jEpArticleView;
	private JScrollPane jSpArticleView;
	
	private GridBagConstraints gbc;
	private GridBagLayout gbl;
	
	public ArticlePanel(MainWindow mWindow) {
		
		this.mWindow = mWindow;
		
		this.initialize();
		this.registerEvents();
		this.configJEditorPane();
		this.configJPanel();
		this.createLayout();
		
	}
	
	private void initialize() {
		
		this.jEpArticleView = new JEditorPane();
		this.jSpArticleView = new JScrollPane(jEpArticleView);
		
		this.gbc = new GridBagConstraints();
		this.gbl = new GridBagLayout();		
		
	}
	
	private void registerEvents() {
		
		jEpArticleView.addHyperlinkListener(new HyperlinkListener() {
		    @Override
		    public void hyperlinkUpdate(HyperlinkEvent e) {
		        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		            try {
		                java.awt.Desktop.getDesktop().browse(e.getURL().toURI());
		            } catch (Exception ex) {
		                ex.printStackTrace();
		            }
		        }
		    }
		});
		
	}
	
	private void configJEditorPane() {
    	
    	this.jEpArticleView.setContentType("text/html");
    	this.jEpArticleView.setEditable(false);
    	this.jEpArticleView.setCaretColor(Color.white);
    	this.jEpArticleView.setFont(new Font("Arial",Font.PLAIN,16));
    	
    }
	
	private void configJPanel() {
		
		this.setBorder(null);
		this.setLayout(gbl);
		
	}
	
	private void createLayout() {
		
		this.jSpArticleView.setBorder(null);
		this.jSpArticleView.setViewportBorder(null);
		
		gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(6, 6, 6, 6);
        gbl.setConstraints(jSpArticleView, gbc);
        this.add(jSpArticleView);
		
	}
	
	public void updateArticleView(Article article) {
    			
    	int id = article.getArticleId();
    	    	
    	String sql = "SELECT * FROM article t1 " + 
    	"LEFT JOIN story t2 ON t2.article_id = t1.id " + 
    	"LEFT JOIN photo t3 ON t3.article_id = t1.id " + 
    	"WHERE t1.id = ?";
    	
    	try {
    		
    	Database.loadDriver();
    	Connection conn = DriverManager.getConnection(Database.DB_URL);
    	
    	PreparedStatement stat = conn.prepareStatement(sql);
    	stat.setInt(1, id);
    	
    	ResultSet rs = stat.executeQuery();
    	
    	if (rs.next()) {
    		    		
    		String story = rs.getString("article_text");
    		String description = rs.getString("description");
    		String author = rs.getString("author");
    		String photo = rs.getString("image_path");
    		
    		String html = "<html>" + 
    		"<body style='margin-left: 6px; padding: 0; font-family:arial;'>" +
    		"<h1 style='margin: 0; 0; font-size:20px;'>" + article.getTitle() + "</h1>";

    		if (photo != null && !photo.isEmpty()) {
    		
    			html += "<div style='margin-top: 0; margin-bottom: 10px'>"; 
    			html += "<img src='file:///" + photo + "' width='400' border='0'>";
    			html += "</div>";
    			
    		}
    			
    		html += "<div><b>Author:</b> " + author + "</div>" +
    		"<div style='margin-bottom: 0;'><b>Description:</b> " + description + "</div>" +
    		"<div style='margin-top: 10px; margin-bottom: 10px;'>" + story + "</div>" +
    		"</body></html>";

    		this.jEpArticleView.setText(html);
    		
    	}   
    	
    	rs.close();
    	stat.close();
    	conn.close();
    		
    	} catch (SQLException ex) { ex.printStackTrace(); }
    }
}
