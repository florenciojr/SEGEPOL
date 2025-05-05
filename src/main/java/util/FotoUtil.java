/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author JR5
 */


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;

public class FotoUtil {
    public static String processarUpload(Part filePart, String uploadDir, String[] extensoesPermitidas, 
                                        int maxSizeKB, ServletContext context) throws IOException {
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        
        // Verificar extensão do arquivo
        boolean extensaoValida = false;
        String fileExt = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        
        for (String ext : extensoesPermitidas) {
            if (ext.equalsIgnoreCase(fileExt)) {
                extensaoValida = true;
                break;
            }
        }
        
        if (!extensaoValida) {
            throw new IOException("Extensão de arquivo não permitida. Use: " + String.join(", ", extensoesPermitidas));
        }
        
        // Verificar tamanho do arquivo
        if (filePart.getSize() > maxSizeKB * 1024) {
            throw new IOException("Tamanho do arquivo excede o limite de " + maxSizeKB + "KB");
        }
        
        // Criar diretório se não existir
        String fullPath = context.getRealPath("") + File.separator + uploadDir;
        Path uploadPath = Paths.get(fullPath);
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Gerar nome único para o arquivo
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        Path filePath = uploadPath.resolve(uniqueFileName);
        
        // Salvar arquivo
        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        
        return uniqueFileName;
    }

    public static void removerFotoAntiga(String caminhoFoto, ServletContext context) {
        if (caminhoFoto != null && !caminhoFoto.isEmpty() && context != null) {
            try {
                String fullPath = context.getRealPath("") + File.separator + caminhoFoto;
                Path path = Paths.get(fullPath);
                Files.deleteIfExists(path);
            } catch (IOException e) {
                System.err.println("Aviso: Não foi possível remover a foto antiga: " + e.getMessage());
            }
        }
    }
}