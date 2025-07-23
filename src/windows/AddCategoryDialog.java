package windows;

import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import components.BasicTextField;
import dao.Database;
import model.CategoryNodeData;

public class AddCategoryDialog extends JDialog {
	
	public MainWindow mWindow;
	
	private JLabel lblCategoryName;
	private JLabel lblDescription;
	
	private JTextField txtCategoryName;
	private JTextField txtDescription;
		
	private JButton jBtnCancel;
	private JButton jBtnAddCategory;
	
	private Box horizontalBox;
		
	private GridBagConstraints gbc;
	private GridBagLayout gbl;

	private JPanel jPlMainView;
	private JPanel jPlCommand;
	private JPanel jPlContainer;
	
	public AddCategoryDialog(MainWindow mWindow) {
		
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
		this.jBtnAddCategory = createJButton(95,33,"Add");
		
		this.horizontalBox = Box.createHorizontalBox();
		
		this.gbc = new GridBagConstraints();
		this.gbl = new GridBagLayout();
		
		this.jPlMainView = new JPanel(gbl);
		this.jPlCommand = new JPanel(gbl);
		this.jPlContainer = new JPanel(gbl);
		
	}	
	
	private void configJDialog() {
		
		this.setTitle("Add category");
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
		jPlButtonView.add(jBtnAddCategory);
		
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
		
		this.jBtnAddCategory.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				jBtnAddCategory.setEnabled(false);
				jBtnAddCategoryPressed();
				jBtnAddCategory.setEnabled(true);
						        
			}						
		});
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				
				AddCategoryDialog.this.dispose();
						
			}
		});
	}
	
	private void jBtnCancelPressed() {
		
		AddCategoryDialog.this.dispose();
		
	}
	
	private void jBtnAddCategoryPressed() {
		
		String categoryName = txtCategoryName.getText().trim();
		String description = txtDescription.getText().trim();
		
		if (categoryName == null || categoryName.isEmpty()) {
	        
        	JOptionPane.showMessageDialog(mWindow, 
        	"Please enter a category.", "Missing Category Name", 
        	JOptionPane.WARNING_MESSAGE);
            return;
        
        }
		
		if (description == null || description.isEmpty()) {
	        
        	JOptionPane.showMessageDialog(mWindow, 
        	"Please enter a description.", "Missing Description Name", 
        	JOptionPane.WARNING_MESSAGE);
            return;
        
        }
		
		Database.loadDriver();
		
		try {
			
	    String sql = "INSERT INTO category " + 
	    "(name, description) VALUES (?, ?)";	
		
	    Connection conn = DriverManager.getConnection(Database.DB_URL);
		PreparedStatement stat = conn.prepareStatement(sql);
		stat.setString(1, categoryName);
		stat.setString(2, description);		
		stat.executeUpdate();
		stat.close();
		conn.close();
		
		} catch (SQLException ex) { ex.printStackTrace(); }
		
		CategoryNodeData data = 
			new CategoryNodeData(categoryName, description);
		
		DefaultMutableTreeNode node = 
			new DefaultMutableTreeNode(data);
		
		DefaultMutableTreeNode root = 
			(DefaultMutableTreeNode) mWindow.treeModel.getRoot();
		
		mWindow.treeModel.insertNodeInto(node, 
		mWindow.categoryNode, mWindow.categoryNode.getChildCount());		
		mWindow.treeModel.reload(mWindow.categoryNode);
				
		this.jBtnCancelPressed();
		
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
