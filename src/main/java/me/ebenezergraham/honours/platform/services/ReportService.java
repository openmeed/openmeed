package me.ebenezergraham.honours.platform.services;
/*
ebenezergraham created on 7/22/19
*/

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Date;

@Service
public class ReportService {


    public ReportService() {
    }

    public Document create(String filename, String password) {
        Document pdfDoc = new Document(PageSize.A4);
        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, new FileOutputStream(filename.concat(".pdf")));

            pdfWriter.setEncryption(password.getBytes(), password.getBytes(),
                    PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128 | PdfWriter.DO_NOT_ENCRYPT_METADATA);
            pdfWriter.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
            pdfDoc.open();
            pdfDoc.add(new Paragraph("\n"));
            pdfDoc.add(new Paragraph("Reward Receipt", headerFont()));
            pdfDoc.add(new Paragraph("Generate on: " + new Date()));
            pdfDoc.add(new Paragraph("\n"));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }

        return pdfDoc;
    }

    public Font bobyFont() {
        Font myfont = new Font();
        myfont.setStyle(Font.NORMAL);
        myfont.setSize(11);
        return myfont;
    }

    public Font headerFont() {
        Font myfont = new Font();
        myfont.setStyle(Font.BOLD);
        myfont.setSize(14);
        return myfont;
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = FileService.rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
