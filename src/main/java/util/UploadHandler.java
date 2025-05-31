/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author JR5
 */


import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class UploadHandler {
    private static final String UPLOAD_DIR = "uploads";

    public String processarUpload(Part part) throws IOException {
        String applicationPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + 
                              File.separator + "webapp";
        String uploadPath = applicationPath + File.separator + UPLOAD_DIR;
        
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        String fileName = getFileName(part);
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        String filePath = uploadPath + File.separator + uniqueFileName;
        
        try (InputStream input = part.getInputStream()) {
            Files.copy(input, new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        
        return UPLOAD_DIR + "/" + uniqueFileName;
    }

    public void removerArquivo(String caminhoArquivo) {
        if (caminhoArquivo != null && !caminhoArquivo.isEmpty()) {
            String filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + 
                           File.separator + "webapp" + File.separator + caminhoArquivo;
            File arquivo = new File(filePath);
            if (arquivo.exists()) {
                arquivo.delete();
            }
        }
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }
}
