package model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ArticleTableModel extends AbstractTableModel {

    private String[] columnNames = {
        "ID", "Title", "Author", "Category", "Status", "Publish Date"
    };

    private List<Article> articles;

    public ArticleTableModel(List<Article> articles) {
        if (articles != null) {
            this.articles = articles;
        } else {
            this.articles = new ArrayList<Article>();
        }
    }

    @Override
    public int getRowCount() {
        return articles.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        if (column >= 0 && column < columnNames.length) {
            return columnNames[column];
        }
        return "";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= articles.size()) {
            return null;
        }
        Article a = articles.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return a.getArticleId();
            case 1:
                return a.getTitle();
            case 2:
                return a.getAuthor();
            case 3:
                return a.getCategory();
            case 4:
                return a.getStatus();
            case 5:
                return a.getPublishDate();
            default:
                return null;
        }
    }

    public void setArticles(List<Article> articles) {
        
    	if (articles != null) {
        
    		this.articles = articles;
        
    	} else {
        
        	this.articles = new ArrayList<Article>();
        
        }
    	
        fireTableDataChanged();
    }

    public Article getArticleAt(int row) {
        
    	if (row < 0 || row >= articles.size()) {
            return null;
        }
    	
        return articles.get(row);
    }
}
