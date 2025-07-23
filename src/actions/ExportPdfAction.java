package actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import model.Article;
import windows.MainWindow;

public class ExportPdfAction extends AbstractAction {

    private MainWindow mWindow;

    public ExportPdfAction(MainWindow mWindow) {
        this.mWindow = mWindow;
                
        super.putValue(Action.NAME, "Export to pdf");
    
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            	    	
    	String text = mWindow.jPlArticle.jEpArticleView.getText();
        
        if (text == null || text.trim().isEmpty()) {
            
        	super.putValue("enabled", Boolean.FALSE);
        	
        	JOptionPane.showMessageDialog(mWindow, 
        	"No article content to export.");
            super.putValue("enabled", Boolean.TRUE);            
            return;
            
        }
        
        int row = mWindow.jTbArticleIndex.getSelectedRow();
        if (row == -1) {
                	
        	JOptionPane.showMessageDialog(mWindow, 
        	"No article selected.");
            return;
        }

        java.util.List<Article> articleList = mWindow.getAllArticles();
        Article article = articleList.get(row);
        String path = article.getArticleImage();
                
        doExportFile(mWindow.jPlArticle.jEpArticleView, path);
        super.putValue("enabled", Boolean.TRUE);
        
    }

    public void doExportFile(JEditorPane editorPane, 
    	String defaultImagePath) {
        
    	try {
                	
        	FileNameExtensionFilter pdfFilter = 
        		new FileNameExtensionFilter("PDF Files", "pdf");
                	
        	JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Export PDF");            
            chooser.setFileFilter(pdfFilter);
            chooser.setAcceptAllFileFilterUsed(false);
            
            if (chooser.showSaveDialog(null) != 
            	JFileChooser.APPROVE_OPTION) return;

            File file = chooser.getSelectedFile();
            
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                file = new File(file.getParentFile(), file.getName() + ".pdf");
            }

            PDDocument pdfDoc = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            pdfDoc.addPage(page);

            PDPageContentStream cs = new PDPageContentStream(pdfDoc, page);

            float margin = 50f;
            float pageWidth = page.getMediaBox().getWidth();
            float y = page.getMediaBox().getHeight() - margin;

            String html = editorPane.getText();
            String imgSrc = null;
            Pattern p = Pattern.compile("<img[^>]+src=\"([^\"]+)\"");
            Matcher m = p.matcher(html);
            if (m.find()) {
                imgSrc = m.group(1);
            }

            if (imgSrc != null) {
                File imgFile = new File(imgSrc);
                if (!imgFile.exists() && defaultImagePath != null) {
                    imgFile = new File(defaultImagePath);
                }
                if (imgFile.exists()) {
                    PDImageXObject pdImage = PDImageXObject.createFromFileByContent(imgFile, pdfDoc);

                    float maxImgWidth = pageWidth - (2 * margin);
                    float maxImgHeight = 400; // generous height for nice size
                    float scaleW = maxImgWidth / pdImage.getWidth();
                    float scaleH = maxImgHeight / pdImage.getHeight();
                    float scale = Math.min(Math.min(scaleW, scaleH), 1.0f); // never scale up beyond original

                    float imgWidth = pdImage.getWidth() * scale;
                    float imgHeight = pdImage.getHeight() * scale;

                    // position image to the left with margin
                    float xImg = margin;

                    cs.drawImage(pdImage, xImg, y - imgHeight, imgWidth, imgHeight);
                    y -= (imgHeight + 30); // leave space before text
                }
            }

            String plainText = editorPane.getDocument().getText(0, editorPane.getDocument().getLength());

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.newLineAtOffset(margin, y);

            float maxWidth = pageWidth - 2 * margin;
            float leading = 16f; 

            for (String line : plainText.split("\n")) {
                while (line.length() > 0) {
                    int fit = findCharsThatFit(line, maxWidth, PDType1Font.HELVETICA, 12);
                    String toWrite = line.substring(0, fit);
                    cs.showText(toWrite);
                    cs.newLineAtOffset(0, -leading);
                    line = line.substring(fit);
                }
                
                cs.newLineAtOffset(0, -leading);
            }

            cs.endText();
            cs.close();

            pdfDoc.save(file);
            pdfDoc.close();

            super.putValue("enabled", Boolean.TRUE);
            
            JOptionPane.showMessageDialog(null, "PDF exported:\n" + file.getAbsolutePath(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
        	
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mWindow,
            "Error exporting PDF:\n" + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        
        }
    }

    private static int findCharsThatFit(String text, float maxWidth,
                                        PDType1Font font, int fontSize) throws Exception {
        int fit = 0;
        while (fit < text.length()) {
            String sub = text.substring(0, fit + 1);
            float width = font.getStringWidth(sub) / 1000 * fontSize;
            if (width > maxWidth) {
                break;
            }
            fit++;
        }
        return (fit > 0) ? fit : 1;
    }
}
