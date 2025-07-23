package model;

public class ArticleNodeData {
    
	public int id;
    public String title;
    public String content;
    public String status;

    public ArticleNodeData(int id, String title, 
    	String content, String status) {
    
    	this.id = id;
        this.title = title;
        this.content = content;
        this.status = status;
    
    }
    
    public int getId() {
    	
    	return id;
    
    }
    
    public String getTitle() {
    	
    	return title;
    	
    }
    
    public String getContent() {
    	
    	return content;
    	
    }
    
    public String getStatus() {
    	
    	return status;
    	
    }
    
    @Override
    public String toString() {
        return title; 
    }
}
