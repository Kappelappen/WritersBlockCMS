package panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import actions.ExportPdfAction;
import actions.PublishArticleAction;
import dao.Database;
import model.Article;
import model.ArticleTableModel;
import model.CategoryNodeData;
import threads.CategoryLoader;
import windows.AddCategoryDialog;
import windows.EditArticleDialog;
import windows.EditCategoryDialog;
import windows.MainWindow;
import windows.NewArticleDialog;

public class CommandPanel extends JPanel
	implements ActionListener {

	private MainWindow mWindow;
		
	private JMenuItem jMiAddCategory;
	private JMenuItem jMiEditCategory;
	private JMenuItem jMiDeleteCategory;
	
	private JMenuItem jMiAddArticle;
	private JMenuItem jMiEditArticle;
	private JMenuItem jMiDeleteArticle;
		
	private JPopupMenu jPmCategories;
	private JPopupMenu jPmArticles;	
	
	private JButton jBtnArticles;
	private JButton jBtnCategories;
	private JButton jBtnPublishArticle;	
	public JButton jBtnExportPdf;
	
	private GridBagConstraints gbc;
	private GridBagLayout gbl;
	
	public CommandPanel(MainWindow mWindow) {
		
		this.mWindow = mWindow;
		
		this.initialize();
		this.configJPanel();
		this.createLayout();
		
	}	
	
	private void initialize() {
				
		this.jMiAddCategory = createJMenuItem(110, "Add");
		this.jMiEditCategory = createJMenuItem(110, "Edit");
        this.jMiDeleteCategory = createJMenuItem(110, "Delete");
		
        this.jMiAddArticle = createJMenuItem(110, "Add");
		this.jMiEditArticle = createJMenuItem(110, "Edit");
		this.jMiDeleteArticle = createJMenuItem(110, "Delete");
		        
        this.jPmCategories = createCategoryMenu();
        this.jPmArticles = createArticleMenu();
        
		this.jBtnArticles = createJButton(110,"Articles");
		this.jBtnCategories = createJButton(110,"Categories");
		
		this.jBtnPublishArticle = createJButton(110, 
			new PublishArticleAction(mWindow));
		
		this.jBtnExportPdf = createJButton(110, 
			new ExportPdfAction(mWindow));
		
		this.gbc = new GridBagConstraints();
		this.gbl = new GridBagLayout();
		
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
        gbc.insets = new Insets(3,3,0,0);
        gbl.setConstraints(jBtnArticles,  gbc);
        this.add(jBtnArticles);
		
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.NONE;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(3,3,0,0);
        gbl.setConstraints(jBtnCategories,  gbc);
        this.add(jBtnCategories);
        
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.NONE;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(3,3,0,0);
        gbl.setConstraints(jBtnPublishArticle,  gbc);
        this.add(jBtnPublishArticle);
        
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(3,3,0,0);
        gbl.setConstraints(jBtnExportPdf,  gbc);
        this.add(jBtnExportPdf);
        
	}	
	
	private void jMiAddCategoryPressed() {
		
		jBtnCategories.setEnabled(false);    			
        new AddCategoryDialog(mWindow).setVisible(true);    
        jBtnCategories.setEnabled(true);    
        
	}

    private void jMiEditCategoryPressed() {
    	
        TreePath selectedPath = mWindow.jTrCmsMenu.getSelectionPath();
        
        if (selectedPath == null) {
            
        	JOptionPane.showMessageDialog(mWindow, 
            "Please select a category first.");
            return;
        
        }
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
        
        if (node.isLeaf()) {
        	
        	EditCategoryDialog dialog = new EditCategoryDialog(mWindow);
        	dialog.setVisible(true);        	
            
        }
    }

    private void jMiDeleteCategoryPressed() {
    	
        TreePath selectedPath = mWindow.jTrCmsMenu.getSelectionPath();
        
        if (selectedPath == null) {
        
        	JOptionPane.showMessageDialog(this, 
        	"Please select a category to delete.");        
            return;
        }
        
        int row = mWindow.jTbArticleIndex.getSelectedRow();
        
        if (selectedPath != null) {
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
        Object obj = node.getUserObject();
        
        if (obj instanceof CategoryNodeData) {
            
        	try {
            
        		CategoryNodeData data = (CategoryNodeData) obj;
                int id = data.getId();

                Database.loadDriver();
                Connection conn = DriverManager.getConnection(Database.DB_URL);

                String sql = "DELETE FROM category WHERE id = ?";
                PreparedStatement stat = conn.prepareStatement(sql);
                stat.setInt(1, id);
                stat.executeUpdate();
                stat.close();
                conn.close();

                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
                
                parentNode.removeAllChildren();
                new CategoryLoader(mWindow).start();
                mWindow.treeModel.reload(mWindow.root);

            	} catch (SQLException ex) {
                ex.printStackTrace();
         
            	}
        	}       
        }        
    }
    
    private void jMiAddArticlePressed() {
    	
    	jBtnArticles.setEnabled(false);
    	    	
    	NewArticleDialog dialog = new NewArticleDialog(mWindow);
		dialog.setVisible(true);
    	
		jBtnArticles.setEnabled(true);    	
		
    }
    
    private void jMiEditArticlePressed() {
    	
    	int viewRow = mWindow.jTbArticleIndex.getSelectedRow();
    	
    	if (viewRow != -1) {
    	
    		int modelRow = mWindow.jTbArticleIndex.convertRowIndexToModel(viewRow);
    	    ArticleTableModel model = (ArticleTableModel) mWindow.jTbArticleIndex.getModel();
    	    Article article = model.getArticleAt(modelRow);

    	    if (article != null) {
    	    
    	    	doEditArticle(article);
    	    	this.mWindow.jPlArticle.updateArticleView(article);
    	    
    	    }
    	    
    	    return;
    	    
    	}
    	
    	TreePath selectedPath = mWindow.jTrCmsMenu.getSelectionPath();
        if (selectedPath != null) {
        	
        	DefaultMutableTreeNode node = 
        		(DefaultMutableTreeNode) selectedPath.getLastPathComponent();
        	
        	Object obj = node.getUserObject();
        	
        	if (obj instanceof Article) {
        		
        		Article article = (Article) obj;
        		this.doEditArticle(article);
        		        		
        	}        	
        }
    	
        else {
            
        	JOptionPane.showMessageDialog(mWindow, 
            "Please select an article first.");
            return;
            
        }
    }
    
    private void doEditArticle(Article article) {
    	
    	EditArticleDialog dialog = new EditArticleDialog(article, mWindow);
        dialog.setVisible(true);
            	
    }
    
    private void jMiDeleteArticlePressed() {

        int row = mWindow.jTbArticleIndex.getSelectedRow();
        TreePath selectedPath = mWindow.jTrCmsMenu.getSelectionPath();

        if (row != -1) {

        	java.util.List<Article> list = mWindow.getAllArticles();
            if (row >= 0 && row < list.size()) {
                Article article = list.get(row);
                int id = article.getArticleId();

                DefaultMutableTreeNode node = null;
                if (selectedPath != null) {
                    Object nObj = selectedPath.getLastPathComponent();
                    if (nObj instanceof DefaultMutableTreeNode) {
                        node = (DefaultMutableTreeNode) nObj;
                    }
                }

                this.doDeleteArticle(id, node);
                mWindow.jPlDashboard.updateDashboard();                
                return;
            }
        }

        if (selectedPath != null) {

        	Object last = selectedPath.getLastPathComponent();
            if (last instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) last;
                Object obj = node.getUserObject();

                if (obj instanceof Article && node.isLeaf()) {
                    Article article = (Article) obj;
                    int id = article.getArticleId();
                    
                    this.doDeleteArticle(id, node);
                    mWindow.jPlDashboard.updateDashboard();
                    return;
                
                }
            }
            JOptionPane.showMessageDialog(this, 
                "Please select a valid article node in the tree.");
            return;
        }

        JOptionPane.showMessageDialog(this, 
            "Please select an article to delete.");
    }
    
    private void doDeleteArticle(int id, DefaultMutableTreeNode node) {

    	try {
        
    		Database.loadDriver();
            try (Connection conn = DriverManager.getConnection(Database.DB_URL);
                 PreparedStatement stat = conn.prepareStatement("DELETE FROM article WHERE id = ?")) {

                stat.setInt(1, id);
                stat.executeUpdate();
            }

            if (node != null) {
            	
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
                
                if (parentNode != null) {
                	
                    node.removeFromParent();
                    ((DefaultTreeModel) mWindow.jTrCmsMenu.getModel()).reload(parentNode);
                }
            }

            java.util.List<Article> list = mWindow.getAllArticles();
            mWindow.insertArticles(list);
            
            ArticleTableModel model = (ArticleTableModel) 
            	mWindow.jTbArticleIndex.getModel();
            
            model.setArticles(list);
            model.fireTableDataChanged();            

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Could not delete article.\n" + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
	
		Object obj = e.getSource();
				
		if (obj.equals(jMiAddCategory)) jMiAddCategoryPressed();
        else if (obj.equals(jMiEditCategory)) jMiEditCategoryPressed();
        else if (obj.equals(jMiDeleteCategory)) jMiDeleteCategoryPressed();
		
        else if (obj.equals(jMiAddArticle)) jMiAddArticlePressed();
        else if (obj.equals(jMiEditArticle)) jMiEditArticlePressed();
        else if (obj.equals(jMiDeleteArticle)) jMiDeleteArticlePressed();
        
        else if (obj.equals(jBtnCategories)) {
            
        	jPmCategories.show(jBtnCategories, 0, 
            jBtnCategories.getHeight());
        
        }
		
        else if (obj.equals(jBtnArticles)) {
                    	
        	jPmArticles.show(jBtnArticles, 0, 
            jBtnArticles.getHeight());
        	        	
        }
	}
	
	private JButton createJButton(int width, String text) {
		
		Dimension dim = new Dimension(width,33);
		JButton jBtnComponent = new JButton();
		
		jBtnComponent.setFocusable(false);
		jBtnComponent.setMinimumSize(dim);
		jBtnComponent.setPreferredSize(dim);
		jBtnComponent.setMaximumSize(dim);
		jBtnComponent.setText(text);
		jBtnComponent.addActionListener(this);
		
		return jBtnComponent;
		
	}
	
	private JButton createJButton(int width, Action action) {
		
		Dimension dim = new Dimension(width,33);
		JButton jBtnComponent = new JButton();
		
		jBtnComponent.setFocusable(false);
		jBtnComponent.setPreferredSize(dim);
		jBtnComponent.setAction(action);
		
		return jBtnComponent;
		
	}
	
	private JMenuItem createJMenuItem(int width, String text) {
    	
    	Dimension dim = new Dimension();
    	dim.width = 200;
        dim.height = 33;
            	
    	JMenuItem jMiComponent = new JMenuItem(text);
        jMiComponent.setFocusable(false);
        
        jMiComponent.setMinimumSize(dim);
        jMiComponent.setPreferredSize(dim);
        jMiComponent.setMaximumSize(dim);
        
        jMiComponent.addActionListener(this);
        
        return jMiComponent;
    }
	
	public JPopupMenu createCategoryMenu() {
        
    	JPopupMenu menu = new JPopupMenu();
        
    	menu.add(jMiAddCategory);
        menu.add(jMiEditCategory);
        menu.add(jMiDeleteCategory);
        
        return menu;
        
    }
	
	public JPopupMenu createArticleMenu() {
        
    	JPopupMenu menu = new JPopupMenu();
        
    	menu.add(jMiAddArticle);
        menu.add(jMiEditArticle);
        menu.add(jMiDeleteArticle);
        
        return menu;
        
    }
	
}
