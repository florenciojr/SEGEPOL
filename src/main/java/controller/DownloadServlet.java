/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author JR5
 */



import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Obter e validar parâmetro
        String fileParam = request.getParameter("file");
        if (fileParam == null || fileParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Arquivo não especificado");
            return;
        }
        
        // 2. Decodificar URL
        String fileName;
        try {
            fileName = URLDecoder.decode(fileParam, StandardCharsets.UTF_8.name());
            System.out.println("Tentando baixar arquivo: " + fileName); // Log importante
        } catch (UnsupportedEncodingException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Codificação inválida");
            return;
        }
        
        // 3. Segurança - prevenir directory traversal
        if (fileName.contains("../") || fileName.contains("..\\")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Caminho inválido");
            return;
        }
        
        // 4. Construir caminho CORRETO (usando o mesmo padrão do UploadHandler)
        String basePath = getServletContext().getRealPath(""); // Pega o caminho da aplicação
        String filePath = basePath + File.separator + fileName;
        System.out.println("Caminho completo do arquivo: " + filePath); // Log crucial
        
        // 5. Verificar existência
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            System.err.println("Arquivo não encontrado no caminho: " + filePath); // Log de erro
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Arquivo não encontrado");
            return;
        }
        
        // Restante do código permanece igual...
        // 6. Determinar tipo MIME
        String mimeType = getServletContext().getMimeType(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        
        // 7. Configurar headers
        response.setContentType(mimeType);
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        
        // 8. Enviar arquivo
        try (InputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            
            byte[] buffer = new byte[4096];
            int bytesRead;
            
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}
