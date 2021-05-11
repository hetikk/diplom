package diplom.utils;


import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Reader {

    public static String readFile(File file) throws IOException {
        if (file.getName().endsWith(".txt")) {
            return String.join(" ", Files.readAllLines(Paths.get(file.getAbsolutePath())));
        }

        if (file.getName().endsWith(".docx")) {
            return readWord(file);
        }

        throw new RuntimeException("invalid file extension");
    }

    private static String readWord(File file) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(
                Files.newInputStream(Paths.get(file.getAbsolutePath())))) {
            XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(doc);
            return xwpfWordExtractor.getText();
        }
    }

}
