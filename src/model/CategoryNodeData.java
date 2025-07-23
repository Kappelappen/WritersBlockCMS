package model;

public class CategoryNodeData {
    
	public int id;
    public String name;
    public String description;
    
    public CategoryNodeData(String name, 
    	String description) {
    	
    	this.name = name;
    	this.description = description;
    	
    }

    public CategoryNodeData(int id, String name, 
    	String description) {
        
    	this.id = id;
        this.name = name;
        this.description = description;
    
    }
    
    public int getId() {
    	
    	return id;
    	
    }
    
    public String getName() {
    	
    	return name;
    	
    }
    
    public String getDescription() {
    	
    	return description;
    	
    }

    @Override
    public String toString() {
        return name;
    }
}
