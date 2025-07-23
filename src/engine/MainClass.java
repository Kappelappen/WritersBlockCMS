package engine;

import java.awt.Color;
import java.awt.Insets;
import java.awt.SystemColor;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import dao.Database;
import threads.CategoryLoader;
import windows.MainWindow;

public class MainClass {
	
	public static void main(String[] arigs) {
		
		Database.prepareDatabaseFile();
		Database.initializeDatabase();
		
		UIManager.put("TabbedPane.focus", new Color(0,0,0,0));
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0,0,0,0));
        UIManager.put("TabbedPane.tabAreaInsets", new Insets(0,0,0,0));
        UIManager.put("TabbedPane.tabInsets", new Insets(5,10,5,10));
		
		UIManager.put("OptionPane.yesButtonText", "Yes");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.cancelButtonText", "Cancel");
        UIManager.put("OptionPane.okButtonText", "OK");
        UIManager.put("OptionPane.closeButtonText", "Close");
		
		UIManager.put("ComboBox.disabledText", SystemColor.textText);
		UIManager.put("ComboBox.disabledForeground", SystemColor.textText);
		UIManager.put("CheckBox.disabledText", SystemColor.textText);
		UIManager.put("CheckBox.disabledForeground", SystemColor.textText);
		UIManager.put("CheckBox.foreground", SystemColor.textText);
		
		Runnable rbl = new Runnable() {
			
			@Override
			public void run() {
				
				MainWindow mWindow = 
					new MainWindow("WritersBlockCMS");
				
				CategoryLoader loader = 
					new CategoryLoader(mWindow);
					
				loader.start();
				mWindow.setVisible(true);
				
			}			
		};
		
		SwingUtilities.invokeLater(rbl);
		
	}

}
