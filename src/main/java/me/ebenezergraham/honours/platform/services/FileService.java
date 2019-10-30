package me.ebenezergraham.honours.platform.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ebenezer Graham
 * Created on 9/30/19
 */
@Service
public class FileService {
    ReportService reportService;
    static final Path rootLocation = Paths.get("files");

    public FileService(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostConstruct
    public void init() {
        boolean res = new File("files").mkdir();
        if (res) System.out.println("File directory created");
    }

    @PreDestroy
    public void delete() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public List<String> analyze(MultipartFile file) {
        List<String> list = new ArrayList<>();
        String password = generateRandomString();
        String name = generateRandomString();
        list.add(password);
        list.add(name.concat(".pdf"));

        try {
            com.itextpdf.text.Document report = reportService.create(name, password);
            report.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public String generateRandomString() {
        return RandomStringUtils.randomAlphanumeric(8);
    }
}
