package threads;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import dao.Database;
import model.CategoryNodeData;
import windows.MainWindow;

public class CategoryLoader extends Thread {

	private MainWindow mWindow;
	private java.util.List<CategoryNodeData> categories;
	
	public CategoryLoader (MainWindow mWindow) {
		
		this.mWindow = mWindow;
		
		this.initialize();
		
	}
	
	private void initialize() {
		
		this.categories = new ArrayList<>();
		
	}
	
	@Override
	public void run() {
		
		Runnable rbl = new Runnable() {
			
			@Override
			public void run() {
								
				try {
				openDb();
				} catch (SQLException ex) { ex.printStackTrace(); }
				
				populateJTree();
				
			}			
		};
		
		SwingUtilities.invokeLater(rbl);
		
	}	
	
	private void openDb() throws SQLException {
		
		this.categories.clear();
		Database.loadDriver();
		
		String sql = "SELECT * FROM category ORDER BY name";
		Connection conn = DriverManager.getConnection(Database.DB_URL);
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery(sql);
		
		while (rs.next()) {
			
			int id = rs.getInt("id");
			String category = rs.getString("name");
			String description = rs.getString("description");
			
			CategoryNodeData data = 
				new CategoryNodeData(id,category,description);
			
			this.categories.add(data);
			
		}
		
		rs.close();
		stat.close();
		conn.close();
		
		this.mWindow.treeModel.reload(mWindow.root);
		
	}
	
	private void populateJTree() {
		
		for (int i = 0; i < categories.size(); i++) {
			
			CategoryNodeData category = categories.get(i);
			
			DefaultMutableTreeNode node = 
				new DefaultMutableTreeNode(category);
			
			this.mWindow.treeModel.insertNodeInto(node, mWindow.categoryNode, i);
			
		}		
		
		this.mWindow.treeModel.reload(mWindow.root);
		
	}	
}
