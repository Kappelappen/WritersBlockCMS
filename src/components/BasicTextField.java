package components;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class BasicTextField extends JTextField {
    	
    private int width;
    
    public BasicTextField(int width) {
                
        this.width = width;
        
        this.configJTextField();
            
    }
    
    private void configJTextField() {
    	
    	this.setMargin(new Insets(0,6,0,6));
    	this.setDisabledTextColor(SystemColor.textText);
    	this.setFont(new Font("Arial",Font.PLAIN,14));
    	
    }
    
    @Override
    public Insets getInsets() { return new Insets(0,8,0,8); }
    
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
}
