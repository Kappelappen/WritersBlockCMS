package components;

import java.awt.*;


import javax.swing.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import dao.Database;

public class DaoComboBox extends JComboBox<String> {
    
    private String tableName;
    private String columnName;
    private int width;
    
    private Color fgColor;
    public DaoComboModel daoModel;
        
    public DaoComboBox(String tableName, 
        String columnName, int width) {
            	
        this.tableName = tableName;
        this.columnName = columnName;
        this.width = width;
        
        this.initVars();
        this.configJComboBox();
        
    }
    
    private void initVars() {
        
        this.fgColor = SystemColor.textText;
        
        this.daoModel = new 
        	DaoComboModel(true,tableName,columnName);
        
    }
    
    private void configJComboBox() {
        
        this.setModel(daoModel);        
        this.setRenderer(new DaoComboRenderer());
                     
    }    
    
    public void styleComboBox(JComboBox<?> comboBox) {
        
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(Color.BLACK);
        comboBox.setOpaque(true);
        
        comboBox.setRenderer(new BasicComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value,
            	
            	int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBackground(isSelected ? new Color(220, 220, 220) : Color.WHITE);
                label.setForeground(Color.BLACK);
                return label;
            }
        });

        comboBox.addPropertyChangeListener("enabled", evt -> {
            if (comboBox.isEnabled()) {
                comboBox.setBackground(Color.WHITE);
            } else {
                comboBox.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
            }
        });
    }
    
    @Override
    public Font getFont() {
        
        return new Font("Arial",Font.PLAIN,16);
        
    }
    
    @Override
    public Dimension getPreferredSize() {
        
    	Dimension dim = new Dimension();
    	
    	if (width != 0) {
    		
    		dim.width = width;
    		
    	}
    	
    	dim.height = 33;
    	
    	return dim;
    	
    }

    @Override
    public Dimension getMinimumSize() {
        
    	Dimension dim = new Dimension();
    	
    	if (width != 0) {
    		
    		dim.width = width;
    		
    	}
    	
    	dim.height = 33;
    	
    	return dim;
    	
    }

    @Override
    public Dimension getMaximumSize() {
        
    	Dimension dim = new Dimension();
    	
    	if (width != 0) {
    		
    		dim.width = width;
    		
    	}
    	
    	dim.height = 33;
    	
    	return dim;
    	
    }
    
    protected class DaoComboModel extends DefaultComboBoxModel {
    	
    	private boolean flag;
    	private String tableName;
    	private String columnName;
    	
    	public DaoComboModel(boolean flag, 
    		String tableName,String columnName) {
    		
    		this.flag = flag;
    		this.tableName = tableName;
    		this.columnName = columnName;
    		
    		this.fetchComboData();
    		
    	}
    	
    	public void fetchComboData() {
    		
    		this.removeAllElements();
    		
    		try {
        	this.insertModelData();
        	} catch (SQLException ex) { ex.printStackTrace(); }    		
    	}
    	
    	private void insertModelData() throws SQLException {
    		
    		String sql = "SELECT * FROM " + tableName + 
    		" ORDER BY id ASC";
    		
    		Database.loadDriver();
    		
    		Connection dbConn = DriverManager.getConnection(Database.DB_URL);
    		Statement stat = dbConn.createStatement();
    		
    		ResultSet rs = stat.executeQuery(sql);
    		
    		super.addElement(null);
    		    		
    		while (rs.next()) {
    			
    			String columnValue = rs.getString(columnName);
    			super.addElement(columnValue);
    			
    		}	
    		
    		rs.close();
    		stat.close();
    		dbConn.close();
    		
    	}
    }
    
    protected class DaoComboRenderer extends DefaultListCellRenderer
		implements ListCellRenderer<Object> {

		@Override
		public Component getListCellRendererComponent(JList<? extends Object> list,
			Object value, int index,
			boolean isSelected, boolean cellHasFocus) {
			// Skapa en ny JLabel för varje cell (viktigt för att undvika störningar)
	    
			EmptyBorder emptyBorder = new EmptyBorder(6,6,6,6);
	    
			Dimension dim = new Dimension(0,35);
			
			JLabel label = new JLabel();
	    
			label.setOpaque(true);
			label.setBorder(emptyBorder);       
			label.setMinimumSize(dim);
			label.setPreferredSize(dim);
			label.setMaximumSize(dim);
			label.setFont(new Font("Arial",Font.PLAIN,14));
	    
			if (value != null) {
	    
				// Sätt texten med HTML för eventuell styling
				String text = value.toString();
				label.setText(text);
	    
			}
	
			// Hantera bakgrund och textfärg beroende på om cellen är vald
			if (isSelected) {
	        
				label.setBackground(list.getSelectionBackground());
				label.setForeground(list.getSelectionForeground());
				label.setOpaque(true); // Viktigt för att bakgrunden ska synas
	    
			} else {
	    
				label.setBackground(list.getBackground());
				label.setForeground(list.getForeground());
				label.setOpaque(true); // Behövs även här
			}
	
			return label; // Returnera JLabel som representerar cellen
		
		}
    }
}
