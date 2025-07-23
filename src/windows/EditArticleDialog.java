package windows;

import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import components.BasicTextField;
import components.DaoComboBox;
import dao.Database;
import model.Article;
import model.ArticleTableModel;
import components.ComboBoxCalendar;
import threads.ArticleInfoManager;

public class EditArticleDialog extends JDialog {
	
	private Article article;
	public MainWindow mWindow;
	
	private JLabel lblArticleTitle;
	private JLabel lblDescription;
	private JLabel lblAuthor;
	private JLabel lblCategory;
	private JLabel lblStatus;
	private JLabel lblPublishDate;
	private JLabel lblImagePath;
	private JLabel lblImageArea;
	
	public JTextField txtArticleTitle;
	public JTextField txtDescription;
	public JTextField txtAuthor;
	public JTextField txtImagePath;
	
	public JComboBox<String> jCbCategory;
	public JComboBox<String> jCbStatus;		
	public JComboBox<String> jCbPublishDate;
	
	private JButton jBtnImagePath;
	private JButton jBtnCancel;
	private JButton jBtnSaveArticle;
	
	public JTextArea txtArticleView;
	private JScrollPane jSpArticleView;
	
	private Box horizontalBox;
		
	private GridBagConstraints gbc;
	private GridBagLayout gbl;

	private JScrollPane jSpImageArea;
	
	private JPanel jPlIntroView;
	private JPanel jPlImageView;
	private JPanel jPlArticleView;
	
	private JTabbedPane jTbMainView;
		
	private JPanel jPlMainView;
	private JPanel jPlCommand;
	private JPanel jPlContainer;
		
	public EditArticleDialog(Article article, 
		MainWindow mWindow) {
		
		super(mWindow);
		
		this.article = article;
		this.mWindow = mWindow;
		
		this.initialize();
		this.configJDialog();
		this.createLayout();
		this.registerEvents();
		
	}
	
	private void initialize() {
				
		this.horizontalBox = Box.createHorizontalBox();
		
		this.lblArticleTitle = new JLabel("Title");
		this.lblAuthor = new JLabel("Author");
		this.lblDescription = new JLabel("Description");
		this.lblCategory = new JLabel("Category");
		this.lblStatus = new JLabel("Status");
		this.lblPublishDate = new JLabel("Publish date");
		this.lblImagePath = new JLabel("Path");
		this.lblImageArea = new JLabel();
		
		this.txtArticleTitle = new BasicTextField(0);
		this.txtAuthor = new BasicTextField(0);
		this.txtDescription = new BasicTextField(0);
		this.txtImagePath = new BasicTextField(0);
				
		this.jCbCategory = new DaoComboBox("category","name", 200);
		this.jCbStatus = new DaoComboBox("article_status","status_name",200);
		this.jCbPublishDate = new ComboBoxCalendar(true,mWindow,150,33); 								    
		
		this.jSpImageArea = createJScrollPane(lblImageArea);
		
		this.jBtnImagePath = createJButton(95,33,"Browse");
		this.jBtnCancel = createJButton(95,33,"Cancel");
		this.jBtnSaveArticle = createJButton(95,33,"Save");
		
		this.txtArticleView = new JTextArea();
		this.jSpArticleView = createJScrollPane(txtArticleView);
		
		this.horizontalBox = Box.createHorizontalBox();
		
		this.gbc = new GridBagConstraints();
		this.gbl = new GridBagLayout();
	
		this.jPlIntroView = new JPanel(gbl);
		this.jPlImageView = new JPanel(gbl);
		this.jPlArticleView = new JPanel(gbl);
		
		this.jTbMainView = new JTabbedPane(JTabbedPane.TOP);
		
		this.jPlMainView = new JPanel(gbl);
		this.jPlCommand = new JPanel(gbl);
		this.jPlContainer = new JPanel(gbl);
		
	}	
	
	private void configJDialog() {
		
		this.setTitle("Edit article");
		this.setSize(750,550);
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setContentPane(jPlContainer);
						
	}
	
	private void createLayout() {
		
	    gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.BOTH;
        gbc.insets = new Insets(6,6,6,6);
        gbl.setConstraints(jTbMainView,  gbc);
        this.jPlContainer.add(jTbMainView);
                
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,0,0,0);
        gbl.setConstraints(jPlCommand,  gbc);
        this.jPlContainer.add(jPlCommand);
		
        this.populateJTbMainView();
        this.populateJPlIntroView();
        this.populateJPlImageView();
        this.populateJPlArticleView();
        this.populateJPlCommand();
        
        TreePath path = mWindow.jTrCmsMenu.getSelectionPath();
        if (path != null) {
        
        	this.updateFormFields(article);
        	return;
        
        }
        
        int selectedRow = mWindow.jTbArticleIndex.getSelectedRow();
                
        if (selectedRow != -1) {
        	
        	//int modelRow = mWindow.jTbArticleIndex.convertRowIndexToModel(selectedRow);
        	//ArticleTableModel model = (ArticleTableModel) mWindow.jTbArticleIndex.getModel();
        	//Article selectedArticle = model.getArticleAt(modelRow);
        	
        	this.updateFormFields(article);
        	return;
        	
        }        
	}
	
	private void populateJTbMainView() {
		
		JScrollPane jSpIntroView = createJScrollPane(jPlIntroView);
		JScrollPane jSpImageView = createJScrollPane(jPlImageView);
		JScrollPane jSpArticleView = createJScrollPane(jPlArticleView);
		
		this.jTbMainView.addTab("Introduction", jSpIntroView);
		this.jTbMainView.addTab("Image", jSpImageView);
		this.jTbMainView.addTab("Text", jSpArticleView);
		
	}
	
	private void populateJPlIntroView() {
		
		gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.NONE;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(10,10,6,6);
        gbl.setConstraints(lblArticleTitle,  gbc);
        this.jPlIntroView.add(lblArticleTitle);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(10,16,6,10);
        gbl.setConstraints(txtArticleTitle,  gbc);
        this.jPlIntroView.add(txtArticleTitle);        
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.NONE;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,10,6,6);
        gbl.setConstraints(lblDescription,  gbc);
        this.jPlIntroView.add(lblDescription);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,16,6,10);
        gbl.setConstraints(txtDescription,  gbc);
        this.jPlIntroView.add(txtDescription);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.NONE;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,10,6,6);
        gbl.setConstraints(lblAuthor,  gbc);
        this.jPlIntroView.add(lblAuthor);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,16,6,10);
        gbl.setConstraints(txtAuthor,  gbc);
        this.jPlIntroView.add(txtAuthor);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.NONE;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,10,6,6);
        gbl.setConstraints(lblCategory,  gbc);
        this.jPlIntroView.add(lblCategory);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,16,6,10);
        gbl.setConstraints(jCbCategory,  gbc);
        this.jPlIntroView.add(jCbCategory);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.NONE;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,10,6,6);
        gbl.setConstraints(lblStatus,  gbc);
        this.jPlIntroView.add(lblStatus);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,16,6,10);
        gbl.setConstraints(jCbStatus,  gbc);
        this.jPlIntroView.add(jCbStatus);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.NONE;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,10,6,6);
        gbl.setConstraints(lblPublishDate,  gbc);
        this.jPlIntroView.add(lblPublishDate);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,16,6,10);
        gbl.setConstraints(jCbPublishDate,  gbc);
        this.jPlIntroView.add(jCbPublishDate);
        
        gbc.gridx = 0;
        gbc.gridy = 50;
        gbc.gridwidth = 50;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0,0,0,0);
        gbl.setConstraints(horizontalBox,  gbc);
        this.jPlIntroView.add(horizontalBox);
		
	}
	
	private void populateJPlImageView() {
		
		EmptyBorder emptyBorder = new EmptyBorder(6,6,6,6);
		EtchedBorder etchedBorder = new EtchedBorder();
		TitledBorder titledBorder = new TitledBorder(etchedBorder, "Preview");
		
		CompoundBorder compBorder = 
			new CompoundBorder(titledBorder,emptyBorder);
		
		this.jSpImageArea.setBorder(compBorder);
		
		gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.NONE;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(10,10,6,6);
        gbl.setConstraints(lblImagePath,  gbc);
        this.jPlImageView.add(lblImagePath);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(10,16,6,0);
        gbl.setConstraints(txtImagePath,  gbc);
        this.jPlImageView.add(txtImagePath);
        
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.NONE;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(10,3,6,10);
        gbl.setConstraints(jBtnImagePath,  gbc);
        this.jPlImageView.add(jBtnImagePath);
		
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.BOTH;
        gbc.insets = new Insets(6,6,6,6);
        gbl.setConstraints(jSpImageArea,  gbc);
        this.jPlImageView.add(jSpImageArea);
        
	}
	
	private void populateJPlArticleView() {
		
		this.configJTextArea();
		
		gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0,0,0,0);
        gbl.setConstraints(jSpArticleView,  gbc);
        this.jPlArticleView.add(jSpArticleView);
		        
	}
	
	private void configJTextArea() {
		
		this.txtArticleView.setMargin(new Insets(6,6,6,6));
		this.txtArticleView.setFont(new Font("Arial",Font.PLAIN,16));
		this.txtArticleView.setLineWrap(true);
		this.txtArticleView.setWrapStyleWord(true);
		this.txtArticleView.setTabSize(1);		
		
	}
	
	private void populateJPlCommand() {
		
		GridLayout layout = new GridLayout(0,2,3,3);
		JPanel jPlButtonView = new JPanel(layout);
		jPlButtonView.add(jBtnCancel);
		jPlButtonView.add(jBtnSaveArticle);
		
		gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,0,10,10);
        gbl.setConstraints(jPlButtonView,  gbc);
        this.jPlCommand.add(jPlButtonView);
		
	}
	
	private void updateFormFields(Object obj) {
			
		if (obj instanceof Article) {
				
			Article article = (Article) obj;
			
			String title = article.getTitle();
			String description = article.getDescription();
			String author = article.getAuthor();
			String category = article.getCategory();
			String status = article.getStatus();
			String date = article.getPublishDate();
			String imagePath = article.getArticleImage();
			String articleText = article.getArticleText();
				
			this.txtArticleTitle.setText(title);
			this.txtDescription.setText(description);
			this.txtAuthor.setText(author);
			this.jCbCategory.setSelectedItem(category);
			this.jCbStatus.setSelectedItem(status);
				
			this.jCbPublishDate.removeAllItems();
			this.jCbPublishDate.addItem(date);
				
			this.txtImagePath.setText(imagePath);
			this.txtArticleView.setText(articleText);
				
			this.updateImageLabel(imagePath);
					
		}
	}
	
	private void updateImageLabel(String path) {
		
		ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(-1, 400, Image.SCALE_SMOOTH); 
        ImageIcon scaledIcon = new ImageIcon(scaled);
        lblImageArea.setIcon(scaledIcon);
        lblImageArea.setText(null);
		
	}
	
	private void jBtnImagePathPressed() {
		
		JFileChooser chooser = new JFileChooser();
		
		FileNameExtensionFilter imageFilter = 
			new FileNameExtensionFilter("Image files", "jpg", 
			"jpeg", "png", "gif");

		chooser.setFileFilter(imageFilter);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int opener = chooser.showOpenDialog(mWindow);
		
		if (opener == JFileChooser.APPROVE_OPTION) {
			
			File file = chooser.getSelectedFile();
			String path = file.getAbsolutePath();			
			this.txtImagePath.setText(path);	
			
			ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();
            Image scaled = img.getScaledInstance(-1, 400, Image.SCALE_SMOOTH); 
            ImageIcon scaledIcon = new ImageIcon(scaled);
            lblImageArea.setIcon(scaledIcon);
            lblImageArea.setText(null);
			
		}		
	}
	
	private void jBtnCancelPressed() {
		
		this.dispose();
		
	}
	
	private void jBtnSaveArticlePressed() {
		
		String title = txtArticleTitle.getText().trim();
		String author = txtAuthor.getText().trim();
		String text = txtArticleView.getText();
		
		if (title == null || title.isEmpty()) {
	        
        	JOptionPane.showMessageDialog(mWindow, 
        	"Please enter a title", "Missing title error", 
        	JOptionPane.WARNING_MESSAGE);
            return;
        
        }
		
		if (author == null || author.isEmpty()) {
	        
        	JOptionPane.showMessageDialog(mWindow, 
        	"Please enter name of author", "Missing author", 
        	JOptionPane.WARNING_MESSAGE);
            return;
        
        }		
		
		if (text == null || text.isEmpty()) {
	        
        	JOptionPane.showMessageDialog(mWindow, 
        	"You must write an article", "Empty text", 
        	JOptionPane.WARNING_MESSAGE);
            return;
        
        }		
		
		try {
		
			String description = txtDescription.getText();	
		    String category = (String) jCbCategory.getSelectedItem();
		    String status = (String) jCbStatus.getSelectedItem();

		    int dateIndex = jCbPublishDate.getSelectedIndex();
		    String dateValue = (String) jCbPublishDate.getItemAt(dateIndex);

		    int articleId = article.getArticleId();

		    String sql = "UPDATE article SET title = ?, description = ?, " +
		    "author = ?, category = ?, status = ?, publish_date = ? " +
		    "WHERE id = ?";

		    Database.loadDriver();

		    try (Connection conn = DriverManager.getConnection(Database.DB_URL);
		         PreparedStatement stat = conn.prepareStatement(sql)) {

		        stat.setString(1, title);
		        stat.setString(2, description);
		        stat.setString(3, author);
		        stat.setString(4, category);
		        stat.setString(5, status);
		        stat.setString(6, dateValue);
		        stat.setInt(7, articleId);
		        
		        stat.executeUpdate();
		        stat.close();
		        conn.close();
		        
		        this.updateStory(articleId);
		        this.updatePhoto(articleId);
		        
		    }

		} catch (SQLException ex) {
		    ex.printStackTrace();
		    System.err.println("SQL State: " + ex.getSQLState());
		    System.err.println("Error Code: " + ex.getErrorCode());
		}
		
		java.util.List<Article> list = mWindow.getAllArticles();
		
		this.mWindow.insertArticles(list);		
		this.mWindow.jTableModelArticles.setArticles(list);
		this.mWindow.jPlArticle.updateArticleView(article);
		
		this.jBtnCancelPressed();
		
	}
		
	private void updatePhoto(int articleId) throws SQLException {
	    
		String text = txtImagePath.getText().trim();

	    Database.loadDriver();
	    Connection conn = DriverManager.getConnection(Database.DB_URL);

	    try {
	    	
	        String checkSql = "SELECT COUNT(*) FROM photo WHERE article_id = ?";
	        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
	        checkStmt.setInt(1, articleId);

	        boolean exists = false;
	        
	        try (ResultSet rs = checkStmt.executeQuery()) {
	        
	        	if (rs.next()) {
	                exists = rs.getInt(1) > 0;
	            }
	        }
	        
	        checkStmt.close();

	        if (exists) {
	            
	        	String updateSql = "UPDATE photo SET image_path = ? " + 
	        	"WHERE article_id = ?";
	            
	        	PreparedStatement stat = conn.prepareStatement(updateSql);
	            stat.setString(1, text);
	            stat.setInt(2, articleId);
	            stat.executeUpdate();
	            stat.close();
	            
	        } else {
	            
	        	String insertSql = "INSERT INTO photo (article_id, " + 
	        	"image_path) VALUES (?,?)";
	            
	        	PreparedStatement insertStmt = conn.prepareStatement(insertSql);
	            insertStmt.setInt(1, articleId);
	            insertStmt.setString(2, text);
	            insertStmt.executeUpdate();
	            insertStmt.close();
	        
	        }
	        
	    } finally {
	        conn.close();
	    }
	}
		
	private void updateStory(int articleId) throws SQLException {
				
		String articleText = txtArticleView.getText();
		if (articleText == null) return;
		
		String sql = "UPDATE story SET article_text = ? " + 
		"WHERE article_id = ?";
		
		Database.loadDriver();
		Connection conn = DriverManager.getConnection(Database.DB_URL);
		
		PreparedStatement stat = conn.prepareStatement(sql);
		stat.setString(1, articleText);
		stat.setInt(2, articleId);
		stat.executeUpdate();
		
		conn.close();
		stat.close();
				
	}
	
	private void registerEvents() {
		
		this.jBtnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				jBtnCancelPressed();
				
			}						
		});
		
		this.jBtnSaveArticle.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				jBtnSaveArticlePressed();
						        
			}						
		});
		
		this.jBtnImagePath.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				jBtnImagePath.setEnabled(false);
				jBtnImagePathPressed();
				jBtnImagePath.setEnabled(true);
						        
			}						
		});
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				
				mWindow.jPlDashboard.updateDashboard();
				EditArticleDialog.this.dispose();
						
			}
		});
	}
	
	private JScrollPane createJScrollPane(Component tabComp) {
		
		JScrollPane jSpComponent = new JScrollPane();
		
		jSpComponent.setViewportView(tabComp);
		jSpComponent.setBorder(null);
		jSpComponent.setViewportBorder(null);
		
		return jSpComponent;
		
	}
	
	private JButton createJButton(int width, 
		int height, String label) {
		
		Dimension dim = new Dimension(width,height);
		JButton button = new JButton();
		button.setFocusable(false);
		button.setText(label);
		button.setMinimumSize(dim);
		button.setPreferredSize(dim);
		button.setMaximumSize(dim);
		
		return button;
		
	}
}
