package windows;

import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import components.BasicTextField;
import dao.Database;
import model.CategoryNodeData;
import threads.CategoryLoader;

public class EditCategoryDialog extends JDialog {
	
	public MainWindow mWindow;
	
	private JLabel lblCategoryName;
	private JLabel lblDescription;
	
	private JTextField txtCategoryName;
	private JTextField txtDescription;
		
	private JButton jBtnCancel;
	private JButton jBtnSaveCategory;
	
	private Box horizontalBox;
		
	private GridBagConstraints gbc;
	private GridBagLayout gbl;

	private JPanel jPlMainView;
	private JPanel jPlCommand;
	private JPanel jPlContainer;
	
	public EditCategoryDialog(MainWindow mWindow) {
		
		super(mWindow);
		
		this.mWindow = mWindow;
		
		this.initialize();
		this.configJDialog();
		this.createLayout();
		this.registerEvents();
		
	}
	
	private void initialize() {
		
		this.horizontalBox = Box.createHorizontalBox();
		
		this.lblCategoryName = new JLabel("Category");
		this.lblDescription = new JLabel("Description");
		
		this.txtCategoryName = new BasicTextField(0);
		this.txtDescription = new BasicTextField(0);
		
		this.jBtnCancel = createJButton(95,33,"Cancel");
		this.jBtnSaveCategory = createJButton(95,33,"Save");
		
		this.horizontalBox = Box.createHorizontalBox();
		
		this.gbc = new GridBagConstraints();
		this.gbl = new GridBagLayout();
		
		this.jPlMainView = new JPanel(gbl);
		this.jPlCommand = new JPanel(gbl);
		this.jPlContainer = new JPanel(gbl);
		
	}	
	
	private void configJDialog() {
		
		this.setTitle("Edit category");
		this.setSize(585,265);
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
        gbc.insets = new Insets(6,10,10,10);
        gbl.setConstraints(jPlMainView,  gbc);
        this.jPlContainer.add(jPlMainView);
                
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
		
        this.populateJPlMainView();
        this.populateJPlCommand();
        
	}
	
	private void populateJPlMainView() {
		
		gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.NONE;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(10,6,6,0);
        gbl.setConstraints(lblCategoryName,  gbc);
        this.jPlMainView.add(lblCategoryName);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(10,16,6,10);
        gbl.setConstraints(txtCategoryName,  gbc);
        this.jPlMainView.add(txtCategoryName);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.NONE;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,6,6,0);
        gbl.setConstraints(lblDescription,  gbc);
        this.jPlMainView.add(lblDescription);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.NONE;
        gbc.insets = new Insets(0,16,6,10);
        gbl.setConstraints(txtDescription,  gbc);
        this.jPlMainView.add(txtDescription);
		
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
        this.jPlMainView.add(horizontalBox);
		
	}
	
	private void populateJPlCommand() {
		
		GridLayout layout = new GridLayout(0,2,3,3);
		JPanel jPlButtonView = new JPanel(layout);
		jPlButtonView.add(jBtnCancel);
		jPlButtonView.add(jBtnSaveCategory);
		
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
	
	private void registerEvents() {
		
		this.jBtnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				jBtnCancelPressed();
				
			}						
		});
		
		this.jBtnSaveCategory.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				jBtnSaveCategory.setEnabled(false);
				jBtnSaveCategoryPressed();
				jBtnSaveCategory.setEnabled(true);
						        
			}						
		});
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				
				EditCategoryDialog.this.dispose();
						
			}			
			
			@Override
			public void windowOpened(WindowEvent e) {
	
				updateFromJTree();
				
			}			
		});
	}
	
	private void updateFromJTree() {
		
		TreePath selectedPath = mWindow.jTrCmsMenu.getSelectionPath();
		
		if (selectedPath != null) {
			
			DefaultMutableTreeNode node = 
				(DefaultMutableTreeNode) selectedPath.getLastPathComponent();
			
			Object obj = node.getUserObject();
			
			if (obj instanceof CategoryNodeData) {
			
				CategoryNodeData data = 
					(CategoryNodeData) node.getUserObject();
				
				int id = data.getId();
				String nodeData = data.getName();
				String description = data.getDescription();
				
				this.txtCategoryName.setText(nodeData);
				this.txtDescription.setText(description);				
				this.txtCategoryName.putClientProperty("id", String.valueOf(id));
			
			}
		}
	}
	
	private void updateFromJTable() {
		
		int row = mWindow.jTbArticleIndex.getSelectedRow();
		
		if (row != -1) {
			
			int modelIndex = mWindow.jTbArticleIndex.convertRowIndexToModel(row);
						
		}		
	}
	
	private void jBtnCancelPressed() {
		
		EditCategoryDialog.this.dispose();
		
	}
	
	private void jBtnSaveCategoryPressed() {
		
		String sql = "UPDATE category SET " + 
		"name = ?, description = ? WHERE id = ?";
		
		try {
		
		String idValue = (String) txtCategoryName.getClientProperty("id");
		int idNumber = Integer.parseInt(idValue);
		
		String name = txtCategoryName.getText().trim();
		String description = txtDescription.getText().trim();
			
		Database.loadDriver();
		Connection conn = DriverManager.getConnection(Database.DB_URL);
		PreparedStatement stat = conn.prepareStatement(sql);
		
		stat.setString(1, name);
		stat.setString(2, description);
		stat.setInt(3, idNumber);
		
		stat.executeUpdate();
		stat.close();
		conn.close();
		
		this.jBtnCancelPressed();
		this.mWindow.treeModel.reload();
		this.mWindow.categoryNode.removeAllChildren();
		
		CategoryLoader loader = 
			new CategoryLoader(mWindow);
		
		loader.start();
		
		} catch (SQLException ex) { ex.printStackTrace(); }		
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
