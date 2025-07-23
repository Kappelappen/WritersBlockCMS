package actions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import windows.MainWindow;

public class ExitAction extends AbstractAction {

    private MainWindow mWindow;

    public ExitAction(MainWindow mWindow) {
        this.mWindow = mWindow;
                
        super.putValue(Action.NAME, "Exit");
    
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	
    	this.mWindow.dispose();
    	System.exit(0);
    	        
    }
}
