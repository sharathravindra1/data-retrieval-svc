package com.personal.dataretrievalsvc.service;

// Extracting Images from a PDF using java

//import com.aspose.ocr.AsposeOCR;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

@Component
class PdfToTex {




    //@PostConstruct
    public static void main(String[] args) throws Exception
    {
        // Existing PDF Document
        // to be Loaded using file iox
        File rootResourceDir = new File("/Users/sravindra1/code/personal/data-retrieval-svc/src/main/resources/static/");
        File newFile
                = new File(rootResourceDir, "US20140258514A1.pdf");
        PDDocument pdfDocument = PDDocument.load(newFile);

        int totalPages = 0;
        Iterator<PDPage> iterator = pdfDocument.getPages().iterator();
        while(true) {

            if (!iterator.hasNext()) break;
            totalPages++;
            iterator.next();
        }

        System.out.println("Total Pages:"+ totalPages);

        // PDFRenderer class to be Instantiated
        // i.e. creating it's object

        PDFRenderer pdfRenderer
                = new PDFRenderer(pdfDocument);
        ITesseract _tesseract = new Tesseract();
        _tesseract.setDatapath("tessdata");
        _tesseract.setDatapath(ServiceConstants.TESSDATA_PATH);
        _tesseract.setTessVariable(ServiceConstants.USER_DEFINED_DPI, ServiceConstants.USER_DEFINED_DPI_VALUE);
        _tesseract.setLanguage(ServiceConstants.ENG_LANGUAGE);

        for(int i = 0;i<totalPages;i++){
            // Rendering an image
            // from the PDF document
            // using BufferedImage class
            BufferedImage img = pdfRenderer.renderImageWithDPI(i,300, ImageType.RGB);
            // Writing the extracted
            // image to a new file
            File outputImageFile = new File(rootResourceDir,"US20140258514A1-" + i + ".PNG");
            ImageIO.write(
                    img, "PNG",
                    outputImageFile);
            System.out.println(
                    "Image  has been extracted successfully:" + i);
           //String text =  processFilesWithAspose(img);
            String text =  processFilesWithTesserat(outputImageFile, _tesseract);
           Files.write(Paths.get( new File(rootResourceDir,"US20140258514A1-" + i + "-tesseract.txt").toURI()),text.getBytes(StandardCharsets.UTF_8));


        }

        // Closing the PDF document
        pdfDocument.close();
    }



    private static String processFilesWithTesserat(File file, ITesseract _tesseract) {
        String result = "";
        try {
            result = _tesseract.doOCR(file);
        } catch (TesseractException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Error occurred running OCR on file");
        }

        return result;
       
    }
}
