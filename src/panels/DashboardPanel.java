package panels;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import components.BasicTextField;
import dao.Database;
import model.Article;
import windows.MainWindow;

public class DashboardPanel extends JPanel {
	
	private MainWindow mWindow;
	
	private java.util.List<String> historyList;
	private Article latestArticle;
	private int publishedArticles;
	
	private JLabel lblHeadline;
	private JLabel lblLatestArticle;
	private JLabel lblArticleIndex;
	private JLabel lblPublished;
	
	private JTextField txtLatestArticle;
	private String counterText;
	private JTextField txtArticleIndex;
	private JTextField txtPublished;
	
	private DefaultListModel<String> historyModel;
	private JList<String> listArticles;
	private JScrollPane jSpArticleHistory;
	
	private Box horizontalBox;
		
	private GridBagConstraints gbc;
	private GridBagLayout gbl;
	
	public DashboardPanel(MainWindow mWindow) {
		
		this.mWindow = mWindow;
		
		this.initialize();
		this.registerEvents();
		this.configJPanel();
		this.createLayout();
		
	}
	
	private void initialize() {
		
		this.historyList = getHistoryList();
		this.latestArticle = getLatestArticle();
		
		this.lblHeadline = new JLabel("<html><h1>Dashboard</h1></html>");
		this.lblLatestArticle = new JLabel("Latest added article");
		this.lblArticleIndex = new JLabel("Total number of articles");
		this.lblPublished = new JLabel("Number of published articles");
		
		this.publishedArticles = latestArticle.getPublishedArticles();
		this.counterText = String.valueOf(latestArticle.doCountArticles());
		
		this.txtLatestArticle = createJTextField(latestArticle.getTitle());
		this.txtArticleIndex = createJTextField(counterText);
		this.txtPublished = createJTextField(String.valueOf(publishedArticles));
		
		this.historyModel = new DefaultListModel<String>();
		this.listArticles = new JList<String>(historyModel);
		
		this.jSpArticleHistory = createJScrollPane(listArticles, 
		"Latest 10 articles");
		
		this.horizontalBox = Box.createHorizontalBox();
		
		this.gbc = new GridBagConstraints();
		this.gbl = new GridBagLayout();		
		
	}
	
	private void registerEvents() {
		
			
	}
	
	private void configJPanel() {
		
		this.setBorder(null);
		this.setLayout(gbl);
		
	}
	
	private void createLayout() {
		
		gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 10;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,8,0,6);
        gbl.setConstraints(lblHeadline, gbc);
        this.add(lblHeadline);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.NONE;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,8,3,0);
        gbl.setConstraints(lblLatestArticle, gbc);
        this.add(lblLatestArticle);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,8,10,10);
        gbl.setConstraints(txtLatestArticle, gbc);
        this.add(txtLatestArticle);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.NONE;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,8,3,0);
        gbl.setConstraints(lblArticleIndex, gbc);
        this.add(lblArticleIndex);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,8,10,10);
        gbl.setConstraints(txtArticleIndex, gbc);
        this.add(txtArticleIndex);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,6,10,6);
        gbl.setConstraints(jSpArticleHistory, gbc);
        this.add(jSpArticleHistory);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.NONE;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,8,3,0);
        gbl.setConstraints(lblPublished, gbc);
        this.add(lblPublished);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,8,10,10);
        gbl.setConstraints(txtPublished, gbc);
        this.add(txtPublished);
        
        gbc.gridx = 0;
        gbc.gridy = 50;
        gbc.gridwidth = 50;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0,0,0,0);
        gbl.setConstraints(horizontalBox, gbc);
        this.add(horizontalBox);
		
        this.populateHistoryList();
        
	}
	
	private void populateHistoryList() {
		
		this.historyModel.clear();
		
		for (int i = 0; i < historyList.size(); i++) {
			
			String item = historyList.get(i);
			this.historyModel.addElement(item);
			
		}		
	}
	
	public void updateDashboard() {
				
		Article addedArticle = getLatestArticle();
		int articleIndex = addedArticle.doCountArticles();
		String indexValue = String.valueOf(articleIndex);
		int published = addedArticle.getPublishedArticles();
		String articlesPublished = String.valueOf(published);
		
		this.historyModel.removeAllElements();
		this.populateHistoryList();
				
		this.txtLatestArticle.setText(addedArticle.getArticleText());
		this.txtArticleIndex.setText(indexValue);
		this.txtPublished.setText(articlesPublished);
				
	}
	
	private java.util.List<String> getHistoryList() {
				
		java.util.List<String> list = new ArrayList<String>();
		
		String sql = "SELECT * FROM article t1 " + 
		"LEFT JOIN story t2 ON t2.article_id = t1.id " +
		"ORDER BY created_at DESC LIMIT 10";
		
		try {
			
		Database.loadDriver();
		Connection conn = DriverManager.getConnection(Database.DB_URL);
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery(sql);
		
		while (rs.next()) {
			
			String title = rs.getString("title");
			list.add(title);
			
		}
		
		rs.close();
		stat.close();
		conn.close();
		
			
		} catch (SQLException ex) { ex.printStackTrace(); }
		
		return list;
		
	}
	
	private Article getLatestArticle() {
		
		Article latestArticle = null;
		
		String sql = "SELECT *,t1.id AS NUM FROM article t1 " + 
		"LEFT JOIN story t2 ON t2.article_id = NUM " + 
		"LEFT JOIN photo t3 ON t3.article_id = NUM " + 
		"ORDER BY id DESC LIMIT 1";
				
		try {
			
		Database.loadDriver();
		Connection conn = DriverManager.getConnection(Database.DB_URL);
		PreparedStatement stat = conn.prepareStatement(sql);
		ResultSet rs = stat.executeQuery();
		
		if (rs.next()) {
			
			latestArticle = new Article();
			latestArticle.setArticleId(rs.getInt("NUM"));
			latestArticle.setTitle(rs.getString("title"));
			latestArticle.setDescription(rs.getString("description"));
			latestArticle.setArticleText(rs.getString("article_text"));
			latestArticle.setArticleImage(rs.getString("image_path"));
						
		}
		
		rs.close();
		stat.close();
		conn.close();
			
		} catch (SQLException ex) { ex.printStackTrace(); }
						
		return latestArticle;
		
	}
	
	private JScrollPane createJScrollPane(Component listComp, 
		String title) {
		
		Dimension dim = new Dimension();
		dim.height = 250;
		
		EtchedBorder etchedBorder = new EtchedBorder();
		TitledBorder titledBorder = new TitledBorder(etchedBorder,title);
		EmptyBorder emptyBorder = new EmptyBorder(6,6,6,6);
		
		CompoundBorder compBorder = 
			new CompoundBorder(titledBorder,emptyBorder);
		
		JScrollPane jSpComponent = new JScrollPane();
		JViewport port = jSpComponent.getViewport();
		
		port.setView(listComp);
		jSpComponent.setMinimumSize(dim);
		jSpComponent.setPreferredSize(dim);
		jSpComponent.setMaximumSize(dim);
		jSpComponent.setViewportBorder(etchedBorder);
		jSpComponent.setBorder(compBorder);
		
		return jSpComponent;
		
	}
	
	private JTextField createJTextField(String cmsText) {
		
		LineBorder lineBorder = new LineBorder(SystemColor.controlDkShadow);
		EmptyBorder emptyBorder = new EmptyBorder(0,6,0,6);
		CompoundBorder compBorder = new CompoundBorder(lineBorder,emptyBorder);
		
		JTextField txtComponent = new BasicTextField(0);
		txtComponent.setText(cmsText);
		txtComponent.setBorder(compBorder);
		txtComponent.setDisabledTextColor(SystemColor.textText);
		txtComponent.setEnabled(false);
		
		return txtComponent;
		
	}
}
