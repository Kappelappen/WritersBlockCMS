package actions;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;

import windows.MainWindow;

public class SaveAction extends AbstractAction {

    private MainWindow mWindow;

    public SaveAction(MainWindow mWindow) {
    	
        this.mWindow = mWindow;
        super.putValue(Action.NAME, "Save");
    
    }
    
    private void doSaveFile() {

        FileNameExtensionFilter filter =
            new FileNameExtensionFilter("HTML Files", "html");

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save HTML File");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);

        int key = chooser.showSaveDialog(mWindow);

        if (key == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String path = file.getAbsolutePath();

            if (!path.toLowerCase().endsWith(".html")) {
                file = new File(path + ".html");
            }

            try {
                // Hämta HTML som text
                HTMLEditorKit kit = (HTMLEditorKit) mWindow.jPlArticle.jEpArticleView.getEditorKit();
                HTMLDocument doc = (HTMLDocument) mWindow.jPlArticle.jEpArticleView.getDocument();
                StringWriter writer = new StringWriter();
                kit.write(writer, doc, 0, doc.getLength());
                String htmlContent = writer.toString();

                // Ersätt alla <img src="..."> med inbäddade base64-data
                htmlContent = embedImagesAsBase64(htmlContent);

                // Skriv ut resultatet till fil
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(htmlContent.getBytes("UTF-8"));
                }

                System.out.println("File saved: " + file.getAbsolutePath());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	
    	int selectedIndex = mWindow.jTpMainView.getSelectedIndex();

    	if (selectedIndex <= 0) {
    		
    		JOptionPane.showMessageDialog(mWindow,
        	"No article is selected","Error", 
        	JOptionPane.ERROR_MESSAGE);
    		
    	}
    	
    	if (selectedIndex != -1) {
    		
    	    String title = mWindow.jTpMainView.getTitleAt(selectedIndex);
    	    String text = mWindow.jPlArticle.jEpArticleView.getText().trim();
    	    
    	    HTMLDocument doc = 
    	    	(HTMLDocument) mWindow.jPlArticle.jEpArticleView.getDocument();
    	    
    	    if (title.equals("Story") && doc.getLength() == 0) {
    	    	
    	    	JOptionPane.showMessageDialog(mWindow,
    	        "No article is selected","Error", 
    	        JOptionPane.ERROR_MESSAGE);
    	    	
    	    } else if (title.equals("Story") && doc.getLength() != 0){
    	    
    	    	this.doSaveFile();
    	    	
    	    }
    	}    	        
    }
    
    private String embedImagesAsBase64(String html) {
    
    	java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<img[^>]*src=[\"']([^\"']+)[\"'][^>]*>");
        java.util.regex.Matcher matcher = pattern.matcher(html);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String imgPath = matcher.group(1);

            File imgFile = null;
            
            try {
                
            	if (imgPath.startsWith("file:/")) {
            	
            		String cleanPath = imgPath.replace("file:/", "").replace("file:///", "");
            	    cleanPath = cleanPath.replace("/", File.separator);
            	    imgFile = new File(cleanPath);
            	
            	} else {
            		
            	    imgFile = new File(imgPath);
            	
            	}            	
            	
            } catch (Exception ex) {
            	
            	ex.printStackTrace();
            
            }

            if (imgFile != null && imgFile.exists()) {
                try {
  
                	String mimeType = java.nio.file.Files.probeContentType(imgFile.toPath());
                    if (mimeType == null) {
                    
                    	mimeType = "image/png";
                    
                    }

                    byte[] imageBytes = java.nio.file.Files.readAllBytes(imgFile.toPath());
                    String base64 = java.util.Base64.getEncoder().encodeToString(imageBytes);
                    String dataUrl = "src=\"data:" + mimeType + ";base64," + base64 + "\"";

                    String newImgTag = matcher.group(0).replaceAll("src=[\"'][^\"']+[\"']", dataUrl);
                    matcher.appendReplacement(sb, java.util.regex.Matcher.quoteReplacement(newImgTag));
                } catch (Exception e) {
                    
                	matcher.appendReplacement(sb, java.util.regex.Matcher.quoteReplacement(matcher.group(0)));
                
                }
                
            } else {
                matcher.appendReplacement(sb, java.util.regex.Matcher.quoteReplacement(matcher.group(0)));
            }
        }
    
        matcher.appendTail(sb);
        return sb.toString();
    
    }
}
