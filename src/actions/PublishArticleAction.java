package actions;

import java.sql.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import dao.Database;
import model.Article;
import model.ArticleTableModel;
import windows.MainWindow;

public class PublishArticleAction extends AbstractAction {

    private MainWindow mainWindow;

    public PublishArticleAction(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        super.putValue(Action.NAME, "Publish");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	
    	super.putValue("enabled", Boolean.FALSE);
    	
    	java.util.List<Article> articles = mainWindow.getAllArticles();
        int selectedRow = mainWindow.jTbArticleIndex.getSelectedRow();

        if (selectedRow == -1) {
        
        	JOptionPane.showMessageDialog(mainWindow,
                "Please select an article to publish.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            
        	super.putValue("enabled", Boolean.TRUE);        	
        	return;
        
        }

        Article article = articles.get(selectedRow);

        if (article.isPublished()) {
            
        	JOptionPane.showMessageDialog(mainWindow,
                "The article is already published.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
        	
        	super.putValue("enabled", Boolean.TRUE);
        	
            return;
        }

        if (article.isFlagged()) {
            JOptionPane.showMessageDialog(mainWindow,
                "The article is flagged and cannot be published.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Database.loadDriver();

            String sql = "UPDATE article SET status = 'Published' WHERE id = ?";
            try (Connection conn = DriverManager.getConnection(Database.DB_URL);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, article.getArticleId());
                int updatedRows = stmt.executeUpdate();

                if (updatedRows == 0) {
            
                	JOptionPane.showMessageDialog(mainWindow,
                        "Failed to publish the article. It might have been removed.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                
                }
            }

            article.setStatus("Published");

            ArticleTableModel model = (ArticleTableModel) mainWindow.jTbArticleIndex.getModel();
            java.util.List<Article> refreshedArticles = mainWindow.getAllArticles();
            model.setArticles(refreshedArticles);
            model.fireTableDataChanged();

            JOptionPane.showMessageDialog(mainWindow,
            	"The article has been published successfully.");

            super.putValue("enabled", Boolean.TRUE);        	
            
        	} catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainWindow,
                "An error occurred while publishing the article: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
