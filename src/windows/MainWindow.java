package windows;

import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;
import javax.swing.tree.*;

import actions.ExitAction;
import actions.ExportPdfAction;
import actions.SaveAction;
import dao.Database;
import model.Article;
import model.ArticleTableModel;
import model.CategoryNodeData;
import panels.ArticlePanel;
import panels.CommandPanel;
import panels.DashboardPanel;
import panels.SearchPanel;
import threads.CategoryLoader;

public class MainWindow extends JFrame {

    private String title;

    public java.util.List<Article> articleList;
    private ActionMap actionMap;
        
    private JMenu jMenuFile;
    
    private JMenuItem itemSave;
    private JMenuItem itemExport;
    private JMenuItem itemExit;
    
    private JMenuBar jMbTopView;
    
    private JLabel lblHeadline;
    
    public DashboardPanel jPlDashboard;
    private JScrollPane jSpDashboard;
    
    public ArticleTableModel jTableModelArticles;
    public JTable jTbArticleIndex;
    private JScrollPane jSpArticleIndex;    
    
    public DefaultMutableTreeNode articleNode;
    public DefaultMutableTreeNode categoryNode;
    public DefaultMutableTreeNode root;
    
    public DefaultTreeModel treeModel;
    public JTree jTrCmsMenu;
    private JScrollPane jSpCmsMenu;
    
    public ArticlePanel jPlArticle;
    public SearchPanel jPlSearch;
        
    public JTabbedPane jTpMainView;

    private Map<String, java.util.List<Article>> dataMap;

    private CommandPanel jPlCommand;
    
    private GridBagConstraints gbc;
    private GridBagLayout gbl;

    private JPanel jPlLeftView;
    private JPanel jPlContainer;
    
    private JSplitPane jSpMainView;

    public MainWindow(String title) {
        
    	this.title = title;
        
        initialize();
        registerEvents();
        configJFrame();
        createLayout();
    
    }

    private void initialize() {

    	this.articleList = getAllArticles();            	
    	
    	this.actionMap = createActionMap();
    	this.jMenuFile = new JMenu("File");
    	this.itemSave = createJMenuItem(jMenuFile,130,"Save");
    	this.itemExport = createJMenuItem(jMenuFile,130,"Export to pdf");
    	this.itemExit = createJMenuItem(jMenuFile,130,"Exit");
    	this.jMbTopView = new JMenuBar();
    	
    	this.lblHeadline = new JLabel("<html><h1>WritersBlockCMS (Demo only - work in progress)</h1></html>");
    	
    	this.jPlDashboard = new DashboardPanel(this);
    	this.jSpDashboard = new JScrollPane(jPlDashboard);
    	
    	this.jTableModelArticles = new ArticleTableModel(articleList);
    	this.jTbArticleIndex = new JTable();
    	this.jSpArticleIndex = new JScrollPane(jTbArticleIndex);
        
    	this.articleNode = new DefaultMutableTreeNode("Articles");
    	this.categoryNode = new DefaultMutableTreeNode("Categories");
    	this.root = new DefaultMutableTreeNode("Menu");
        
    	this.treeModel = new DefaultTreeModel(root);
    	this.jTrCmsMenu = new JTree(treeModel);
    	this.jSpCmsMenu = new JScrollPane(jTrCmsMenu);
        
        this.jPlArticle = new ArticlePanel(this);
        this.jPlSearch = new SearchPanel(this);
        
        this.jTpMainView = new JTabbedPane(JTabbedPane.TOP);
        
        this.dataMap = new HashMap<>();

        this.jPlCommand = new CommandPanel(this);
        
        this.gbc = new GridBagConstraints();
        this.gbl = new GridBagLayout();

        this.jPlLeftView = new JPanel(gbl);
        this.jPlContainer = new JPanel(gbl);

        this.jSpMainView = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    
    }

    private void registerEvents() {
    	
    	this.jTpMainView.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				
				int tabIndex = jTpMainView.getSelectedIndex();
				String title = jTpMainView.getTitleAt(tabIndex);
				Document doc = jPlArticle.jEpArticleView.getDocument();
				int len = doc.getLength();
				
				if ("Story".equals(title) && len != 0) {
					
					jPlCommand.jBtnExportPdf.setEnabled(true);
					
				}	
				
				else {
					
					jPlCommand.jBtnExportPdf.setEnabled(false);
					
				}
			}    		
    	});
    	
    	jTbArticleIndex.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				if (!e.getValueIsAdjusting()) {
				
					int selectedRow = jTbArticleIndex.getSelectedRow();
					
					if (selectedRow >= 0) {
				    
						int modelRow = jTbArticleIndex.convertRowIndexToModel(selectedRow);

						ArticleTableModel model = (ArticleTableModel) jTbArticleIndex.getModel();
						Article selectedArticle = model.getArticleAt(modelRow);		
				    	jPlArticle.updateArticleView(selectedArticle);			
				    				
					}
				}
			}	    	        
    	});
    	
    	jTrCmsMenu.addTreeSelectionListener(new TreeSelectionListener() {

    		@Override
            public void valueChanged(TreeSelectionEvent e) {
    
            	TreePath path = e.getPath();
            	
            	DefaultMutableTreeNode selectedNode =
            		(DefaultMutableTreeNode) path.getLastPathComponent();

            	if (selectedNode == null) return;
            	
            	Object obj = selectedNode.getUserObject();
            	
            	if (obj instanceof Article) {
                    		
            		Article article = (Article) obj;
            		jPlArticle.updateArticleView(article);
            		
            	}
            	
            	if (obj instanceof CategoryNodeData) {
            		
            		CategoryNodeData data = (CategoryNodeData) obj;
            		String name = data.getName();
            		doFilterTable(selectedNode, name);
            				
            	}
            	
            	doExpandTree();            	
            	                                
            }
        });
    	
    	jTrCmsMenu.addTreeExpansionListener(new TreeExpansionListener() {

			@Override
			public void treeExpanded(TreeExpansionEvent event) {
				
				doExpandTree();
				
			}

			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
				
				doExpandTree();
				
			}
    	});
    	
    	jTrCmsMenu.addTreeWillExpandListener(new TreeWillExpandListener() {

			@Override
			public void treeWillExpand(TreeExpansionEvent event) 
				throws ExpandVetoException {
				
				
			}

			@Override
			public void treeWillCollapse(TreeExpansionEvent event) 
				throws ExpandVetoException {
				
				doExpandTree();
				
			}
    		
    	});

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                        
            	insertArticles(articleList);
            	
            	Timer timer = new Timer(15, new ActionListener() {
            		
            		@Override
            		public void actionPerformed(ActionEvent evt) {
            			            			
            			doExpandTree();
            			
            		}
            		
            	});            	
            	
            	timer.start();
            	            	            	
            }

            @Override
            public void windowClosing(WindowEvent e) {
                MainWindow.this.dispose();
                System.exit(0);
            }
        });
    }
    
    private void doFilterTable(DefaultMutableTreeNode node, 
    	String category) {

    	String name = node.toString();
    	
    	java.util.List<Article> result;
    	
    	if ("All Articles".equals(name)) {
    		
    	    result = getAllArticles(); 
    	
    	} else {
    	
    		result = getArticlesByCategory(category); 
    	
    	}

    	ArticleTableModel model = (ArticleTableModel) jTbArticleIndex.getModel();
    	model.setArticles(result);    	
    	model.fireTableDataChanged();
    	
    }        
    

    private void loadCategories() {
    	
        CategoryLoader loader = new CategoryLoader(this);
        loader.start();
    
    }

    private void configJFrame() {
    
    	this.configJMenuBar();
    	
    	this.setTitle(title);
    	this.setContentPane(jPlContainer);
        this.setSize(800,600);
        this.setJMenuBar(jMbTopView);
        this.setLocationRelativeTo(null);
    
    }
    
    private void configJMenuBar() {
    	
    	this.jMenuFile.setFocusable(false);
    	this.jMbTopView.add(jMenuFile);
    	
    }

    private void createLayout() {
    
    	gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,0,0,0);
        gbl.setConstraints(jPlCommand, gbc);
        jPlContainer.add(jPlCommand);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(3,6,3,0);
        gbl.setConstraints(lblHeadline, gbc);
        jPlContainer.add(lblHeadline);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,0,0,0);
        gbl.setConstraints(jPlSearch, gbc);
        jPlContainer.add(jPlSearch);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(6, 6, 6, 6);
        gbl.setConstraints(jSpMainView, gbc);
        jPlContainer.add(jSpMainView);

        this.configJLabel();
        this.configJTree();
        this.doExpandTree();
        this.configJTable();
        this.populateJPlLeftView();
        this.configJSplitPane();
        this.populateJTabbedPane();
        
    }
    
    private void configJLabel() {
    	
    	lblHeadline.setForeground(Color.RED);
    	lblHeadline.setFont(new Font("SansSerif", Font.BOLD, 16));
    	
    }

    private void configJTree() {
        	
    	this.root.add(articleNode);
    	this.root.add(categoryNode);
    	    	
    	jTrCmsMenu.setShowsRootHandles(true);
    	jTrCmsMenu.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        
    }
    
    public void insertArticles(java.util.List<Article> list) {
    	    	
    	this.articleNode.removeAllChildren();
    	
    	for (int i = 0; i < list.size(); i++) {
    	    		    		
    		Article article = list.get(i);
    		
    		DefaultMutableTreeNode childNode = 
    			new DefaultMutableTreeNode(article);
    		
    		this.treeModel.insertNodeInto(childNode, 
    		articleNode, articleNode.getChildCount());
    		
    	}
    	
    	this.treeModel.reload(articleNode);
    	
    	
    }
    
    private void doExpandTree() {
		
		for (int i = 0; i < jTrCmsMenu.getRowCount(); i++) {
			jTrCmsMenu.expandRow(i);
		}				
	}

    private void configJTable() {
        
    	JTableHeader header = jTbArticleIndex.getTableHeader();
    	header.setReorderingAllowed(false);
    	
    	this.jTbArticleIndex.setRowHeight(25);
    	this.jTbArticleIndex.setDefaultEditor(Object.class, null);
    	this.jTbArticleIndex.setAutoCreateColumnsFromModel(false);
                
        for (int i = 0; i < jTbArticleIndex.getColumnModel().getColumnCount(); i++) {
        	
        	TableColumn col = jTbArticleIndex.getColumnModel().getColumn(i);
        	this.jTbArticleIndex.getColumnModel().removeColumn(col);
        	
        }
        
        int len = jTableModelArticles.getColumnCount();
        
        for (int i = 0; i < len; i++) {
        	
        	TableColumn column = new TableColumn(i);
        	String name = jTableModelArticles.getColumnName(i);
        	column.setHeaderValue(name);
        	
        	if (i == 0) {
        		
        		column.setMinWidth(0);
        		column.setWidth(0);
        		column.setMaxWidth(0);
        		column.setResizable(false);
        		
        	}
        	
        	this.jTbArticleIndex.getColumnModel().addColumn(column);
        	
        }        
        
        this.jTbArticleIndex.setModel(jTableModelArticles);
        
    }

    private void populateJPlLeftView() {
        
    	this.jSpCmsMenu.setBorder(null);
    	this.jSpCmsMenu.setViewportBorder(null);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(6, 6, 6, 6);
        gbl.setConstraints(jSpCmsMenu, gbc);
        jPlLeftView.add(jSpCmsMenu);

    }

    private void configJSplitPane() {
    	
        jSpArticleIndex.setViewportBorder(BorderFactory.createEtchedBorder());
        jSpArticleIndex.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        jSpMainView.setDividerLocation(200);
        jSpMainView.setDividerSize(10);
        jSpMainView.setLeftComponent(jPlLeftView);
        jSpMainView.setRightComponent(jTpMainView);
        jSpMainView.setContinuousLayout(true);
        jSpMainView.setOneTouchExpandable(true);
    
    }
    
    private void populateJTabbedPane() {
    	
    	this.jSpDashboard.setBorder(null);
    	this.jSpDashboard.setViewportBorder(BorderFactory.createEtchedBorder());
    	
    	this.jSpArticleIndex.setBorder(null);
    	this.jSpArticleIndex.setViewportBorder(BorderFactory.createEtchedBorder());
    	
    	this.jTpMainView.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
    	    	
    	this.jTpMainView.addTab("Dashboard", jSpDashboard);
    	this.jTpMainView.addTab("Articles", jSpArticleIndex);
    	this.jTpMainView.addTab("Story", jPlArticle);
    	
    }
    
    private ActionMap createActionMap() {
    	
    	ActionMap map = new ActionMap();
    	
    	map.put("Save", new SaveAction(this));
    	map.put("Export to pdf", new ExportPdfAction(this));
    	map.put("Exit", new ExitAction(this));
    	
    	return map;
    	
    }
    
    private JMenuItem createJMenuItem(JMenu menu, 
    	int width, String text) {
    	
    	Action action = actionMap.get(text);
    	
    	Dimension dim = new Dimension(width,33);
    	JMenuItem menuItem = new JMenuItem();
    	
    	menuItem.setMinimumSize(dim);
    	menuItem.setPreferredSize(dim);
    	menuItem.setMaximumSize(dim);
    	menuItem.setFocusable(false);
    	menuItem.setAction(action);
    	menu.add(menuItem);    	
    	
    	return menuItem;
    	
    }
    
    public java.util.List getAllArticles() {
    	
    	java.util.List<Article> list = new ArrayList<>();
    	
    	try {
    		
    	Database.loadDriver();
    	
    	String sql = "SELECT " + 
    	"t1.id AS article_id," + 
    	"t1.title AS title," + 
    	"t1.description AS description," + 
    	"t1.author AS author," + 
    	 "t1.category AS category_name," + 
    	 "t1.status AS status," +
    	 "t1.publish_date AS publish_date," + 
    	 "t2.image_path AS article_image," + 
    	 "t3.article_text AS article_text " + 
    	 "FROM article t1 " + 
    	 "LEFT JOIN photo t2  ON t1.id = t2.article_id " + 
    	 "LEFT JOIN story t3  ON t1.id = t3.article_id;";
        	
    	Connection conn = DriverManager.getConnection(Database.DB_URL);
    	Statement stat = conn.createStatement();
    	
    	ResultSet rs = stat.executeQuery(sql);
    	
    	while (rs.next()) {
    		
    		int id = rs.getInt("article_id");
    		String title = rs.getString("title"); 
    		String description = rs.getString("description");
    		String author = rs.getString("author");
    		String category = rs.getString("category_name");
    		String status = rs.getString("status");
    		String publishDate = rs.getString("publish_date");
    		String imagePath = rs.getString("article_image");
    		String articleText = rs.getString("article_text");
    		
    		Article article = new Article(id,title,description,
    		author,category,status,publishDate,
    		imagePath,articleText,false);
    		
    		list.add(article);
    		    		    		
    	}   
    	
    	rs.close();
    	stat.close();
    	conn.close();
    	
    	} catch (SQLException ex) { ex.printStackTrace(); }
    	
    	return list;
    	
    }
    
    public java.util.List<Article> getArticlesByCategory(String categoryName) {
        
    	java.util.List<Article> list = new ArrayList<>();
        
    	String sql = "SELECT t1.id AS NUM,t1.title,t1.author,t1.status, " + 
    	"t1.publish_date,t1.category,t1.description, " + 
    	"t2.article_text,t3.image_path " + 
    	"FROM article t1 " + 
    	"LEFT JOIN story t2 ON  t2.article_id = NUM " +
    	"LEFT JOIN photo t3 ON t3.article_id = NUM " + 
    	"WHERE t1.category = ?";
    	
    	Database.loadDriver();
    	
        try (        	
        	
        	Connection conn = DriverManager.getConnection(Database.DB_URL);
        	PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoryName);
            try (ResultSet rs = ps.executeQuery()) {
            	            	
                while (rs.next()) {
                
                	Article a = new Article();
                    a.setArticleId(rs.getInt("NUM"));
                    a.setTitle(rs.getString("title"));
                    a.setDescription(rs.getString("description"));
                    a.setAuthor(rs.getString("author"));
                    a.setStatus(rs.getString("status"));
                    a.setPublishDate(rs.getString("publish_date"));
                    a.setCategory(rs.getString("category"));
                    a.setArticleImage(rs.getString("image_path"));
                    a.setArticleText(rs.getString("article_text"));
                    list.add(a);
                
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
}
