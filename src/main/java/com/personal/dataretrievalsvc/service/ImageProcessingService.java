package com.personal.dataretrievalsvc.service;


import com.personal.dataretrievalsvc.entity.PatentDetails;
import com.personal.dataretrievalsvc.model.ServiceConstants;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

@Component
@Slf4j
class ImageProcessingService {



    @Autowired
    private PersistanceService persistanceService;

    //@PostConstruct
    public void test() throws Exception {

        File rootResourceDir = new File("/Users/sravindra1/code/personal/data-retrieval-svc/src/main/resources/static/");
        File newFile
                = new File(rootResourceDir, "US20140258514A1.pdf");
        processFile(newFile, null);
    }

    public void processFile(File f, PatentDetails patentDetails) throws Exception {
        log.info("Processing file started:{}", f);
        System.setProperty("jna.library.path", "/usr/local/lib");
        PDDocument pdfDocument = PDDocument.load(f);
        int totalPages = getTotalPages(pdfDocument);
        log.info("Total Pages:" + totalPages);
        PDFRenderer pdfRenderer
                = new PDFRenderer(pdfDocument);
        StringBuffer rawText = new StringBuffer();
        if (totalPages > 5) {
            // limit this for testing purposes
            totalPages = 5;
        }

        for (int i = 0; i < totalPages; i++) {
            BufferedImage img = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB);
            log.info("Image has been extracted successfully:" + i);
            rawText.append(processFilesWithTesserat(img));
        }
        pdfDocument.close();

        patentDetails.setRaw_text(rawText.toString());
        persistanceService.save(patentDetails);
        log.info("Processing file ended:{}", f);
    }

    private int getTotalPages(PDDocument pdfDocument) {
        int totalPages = 0;
        Iterator<PDPage> iterator = pdfDocument.getPages().iterator();
        while (true) {
            if (!iterator.hasNext()) break;
            totalPages++;
            iterator.next();
        }
        return totalPages;
    }


    private String processFilesWithTesserat(BufferedImage img) {
        try {
            log.info("Extracting text from tessaract");
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath(ServiceConstants.TESSDATA_PATH);
            tesseract.setDatapath(ServiceConstants.TESSDATA_PATH);
            tesseract.setTessVariable(ServiceConstants.USER_DEFINED_DPI, ServiceConstants.USER_DEFINED_DPI_VALUE);
            tesseract.setLanguage(ServiceConstants.ENG_LANGUAGE);
            String text = tesseract.doOCR(img);
            log.info("Finished extracting text from tessaract");
            return text;
        } catch (TesseractException e) {
            throw new RuntimeException("Error occurred running OCR on file", e);
        }

    }



}
