/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author JR5
 */



import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;



public class UploadHandler {
    private final String UPLOAD_DIRECTORY = "uploads/provas";
    private final String basePath;

    public UploadHandler(ServletContext context) {
        this.basePath = context.getRealPath("");
        System.out.println("Diretório base de upload: " + getUploadPath()); // Log importante
        createUploadDirectory();
        
            
        // Para produção - usar um diretório absoluto externo
        // this.basePath = "/var/meuapp/uploads";
    }

 
    public String processarUpload(Part part) throws IOException {
        if (part == null || part.getSize() == 0) {
            throw new IOException("Nenhum arquivo enviado ou arquivo vazio");
        }

        String fileName = extractFileName(part);
        String safeFileName = generateSafeFileName(fileName);
        String filePath = getUploadPath() + File.separator + safeFileName;

        System.out.println("Salvando arquivo em: " + filePath); // Log importante

        try (InputStream input = part.getInputStream()) {
            Files.copy(input, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        }

        // Retorna o caminho relativo consistente
        return UPLOAD_DIRECTORY + "/" + safeFileName;
    }



   
     
    
        
   

    private void createUploadDirectory() {
        File uploadDir = new File(getUploadPath());
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }



    public void removerArquivo(String caminhoRelativo) {
        if (caminhoRelativo != null && !caminhoRelativo.isEmpty()) {
            String filePath = basePath + File.separator + caminhoRelativo;
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                System.err.println("Erro ao remover arquivo: " + filePath);
                e.printStackTrace();
            }
        }
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

    private String generateSafeFileName(String originalName) {
        String safeName = originalName.replaceAll("[^a-zA-Z0-9.-]", "_");
        return UUID.randomUUID().toString() + "_" + safeName;
    }

    private String getUploadPath() {
        return basePath + File.separator + UPLOAD_DIRECTORY;
    }
    
    public void printUploadPath() {
    System.out.println("Arquivos estão sendo salvos em: " + getUploadPath());
}
    
}
