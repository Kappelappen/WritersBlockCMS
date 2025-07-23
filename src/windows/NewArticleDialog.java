package windows;

import java.awt.*;
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
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.html.HTMLEditorKit;

import components.BasicTextField;
import components.DaoComboBox;
import components.ComboBoxCalendar;
import threads.ArticleInfoManager;

public class NewArticleDialog extends JDialog {
	
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
	private JButton jBtnNewArticle;
	
	public JEditorPane txtArticleView;
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
		
	public NewArticleDialog(MainWindow mWindow) {
		
		super(mWindow);
		
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
		this.jBtnNewArticle = createJButton(95,33,"Add");
		
		this.txtArticleView = new JEditorPane();
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
		
		this.setTitle("New article");
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
		this.txtArticleView.setEditable(true);
		this.txtArticleView.setFont(new Font("Arial",Font.PLAIN,16));
		this.txtArticleView.setContentType("text/html");
		this.txtArticleView.setEditorKit(new HTMLEditorKit());		
				
	}
	
	private void populateJPlCommand() {
		
		GridLayout layout = new GridLayout(0,2,3,3);
		JPanel jPlButtonView = new JPanel(layout);
		jPlButtonView.add(jBtnCancel);
		jPlButtonView.add(jBtnNewArticle);
		
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
	
	private void jBtnNewArticlePressed() {
		
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
		
		ArticleInfoManager manager = 
			new ArticleInfoManager(NewArticleDialog.this);
		
		manager.start();
		
		this.jBtnCancelPressed();
		manager = null;
		
	}
	
	private void registerEvents() {
		
		this.jBtnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				jBtnCancelPressed();
				
			}						
		});
		
		this.jBtnNewArticle.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				jBtnNewArticle.setEnabled(false);
				jBtnNewArticlePressed();
				jBtnNewArticle.setEnabled(true);
						        
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
				NewArticleDialog.this.dispose();
						
			}			
			
			@Override
			public void windowOpened(WindowEvent e) {
	
				
				
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
