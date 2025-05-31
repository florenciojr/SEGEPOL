/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ProvaDAO;
import dao.QueixaDAO;
import dao.UsuarioDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.Prova;
import model.ProvaDetalhada;
import util.UploadHandler;

/**
 *
 * @author JR5
 */




import dao.ProvaDAO;
import dao.QueixaDAO;
import dao.UsuarioDAO;
import model.Prova;
import model.ProvaDetalhada;
import util.UploadHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@WebServlet("/provas")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
    maxFileSize = 1024 * 1024 * 10,       // 10 MB
    maxRequestSize = 1024 * 1024 * 100    // 100 MB
)
public class ProvaServlet extends HttpServlet {
    private ProvaDAO provaDAO;
    private QueixaDAO queixaDAO;
    private UsuarioDAO usuarioDAO;
    private UploadHandler uploadHandler;

@Override
public void init() {
    provaDAO = new ProvaDAO();
    queixaDAO = new QueixaDAO();
    usuarioDAO = new UsuarioDAO();
    uploadHandler = new UploadHandler(getServletContext());
    uploadHandler.printUploadPath();
}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if (action == null || action.isEmpty()) {
                listarProvas(request, response);
            } else {
                switch (action) {
                    case "nova":
                        mostrarFormulario(request, response);
                        break;
                    case "editar":
                        mostrarFormularioEdicao(request, response);
                        break;
                    case "detalhes":
                        mostrarDetalhes(request, response);
                        break;
                    case "remover":
                        removerProva(request, response);
                        break;
                    default:
                        listarProvas(request, response);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Erro no banco de dados", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if (action == null || action.isEmpty()) {
                listarProvas(request, response);
            } else {
                switch (action) {
                    case "salvar":
                        salvarProva(request, response);
                        break;
                    case "atualizar":
                        atualizarProva(request, response);
                        break;
                    default:
                        listarProvas(request, response);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Erro no banco de dados", e);
        }
    }

    private void listarProvas(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        List<Prova> provas = provaDAO.listarTodasComRelacionamentos();
        request.setAttribute("provas", provas);
        request.getRequestDispatcher("/WEB-INF/views/provas/listar.jsp").forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        List<Map<String, Object>> listaQueixas = queixaDAO.listarQueixasParaCombo();
        List<Map<String, Object>> listaUsuarios = usuarioDAO.listarUsuariosParaCombo();

        request.setAttribute("prova", new Prova());
        request.setAttribute("queixas", listaQueixas);
        request.setAttribute("usuarios", listaUsuarios);
        
        request.getRequestDispatcher("/WEB-INF/views/provas/formulario.jsp").forward(request, response);
    }

    private void mostrarFormularioEdicao(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            ProvaDetalhada prova = provaDAO.buscarProvaDetalhada(id);
            
            if (prova == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Prova não encontrada");
                return;
            }
            
            List<Map<String, Object>> listaQueixas = queixaDAO.listarQueixasParaCombo();
            List<Map<String, Object>> listaUsuarios = usuarioDAO.listarUsuariosParaCombo();
            
            if (prova.getCaminhoArquivo() == null) {
                prova.setCaminhoArquivo("");
            }
            if (prova.getDescricao() == null) {
                prova.setDescricao("");
            }
            
            request.setAttribute("prova", prova);
            request.setAttribute("queixas", listaQueixas);
            request.setAttribute("usuarios", listaUsuarios);
            
            request.getRequestDispatcher("/WEB-INF/views/provas/formulario.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        }
    }

    private void mostrarDetalhes(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ProvaDetalhada prova = provaDAO.buscarProvaDetalhada(id);
        
        if (prova == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        request.setAttribute("prova", prova);
        request.getRequestDispatcher("/WEB-INF/views/provas/detalhes.jsp").forward(request, response);
    }

    private void salvarProva(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        Prova prova = new Prova();
        preencherProva(request, prova);
        
        List<String> caminhosArquivos = new ArrayList<>();
        
        // Processar todos os arquivos enviados
        for (Part part : request.getParts()) {
            if (part.getName().equals("arquivos") && part.getSize() > 0) {
                String caminhoArquivo = uploadHandler.processarUpload(part);
                caminhosArquivos.add(caminhoArquivo);
            }
        }
        
        if (!caminhosArquivos.isEmpty()) {
            prova.setCaminhoArquivo(String.join(",", caminhosArquivos));
        }
        
        if (provaDAO.inserir(prova)) {
            request.getSession().setAttribute("mensagem", "Prova cadastrada com sucesso!");
            response.sendRedirect("provas");
        } else {
            request.setAttribute("erro", "Falha ao cadastrar prova");
            mostrarFormularioComErro(request, response, prova);
        }
    }

    private void atualizarProva(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Prova prova = provaDAO.buscarPorId(id);
        
        if (prova == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        preencherProva(request, prova);
        
        List<String> caminhosArquivos = new ArrayList<>();
        
        // Manter arquivos existentes não removidos
        String[] arquivosExistentes = request.getParameterValues("arquivosExistentes");
        if (arquivosExistentes != null) {
            caminhosArquivos.addAll(Arrays.asList(arquivosExistentes));
        }
        
        // Adicionar novos arquivos
        for (Part part : request.getParts()) {
            if (part.getName().equals("arquivos") && part.getSize() > 0) {
                String caminhoArquivo = uploadHandler.processarUpload(part);
                caminhosArquivos.add(caminhoArquivo);
            }
        }
        
        // Remover arquivos marcados para exclusão
        String[] arquivosRemovidos = request.getParameterValues("arquivosRemovidos");
        if (arquivosRemovidos != null) {
            for (String arquivo : arquivosRemovidos) {
                uploadHandler.removerArquivo(arquivo);
                caminhosArquivos.remove(arquivo);
            }
        }
        
        if (!caminhosArquivos.isEmpty()) {
            prova.setCaminhoArquivo(String.join(",", caminhosArquivos));
        } else {
            prova.setCaminhoArquivo(null);
        }
        
        if (provaDAO.atualizar(prova)) {
            request.getSession().setAttribute("mensagem", "Prova atualizada com sucesso!");
            response.sendRedirect("provas");
        } else {
            request.setAttribute("erro", "Falha ao atualizar prova");
            mostrarFormularioComErro(request, response, prova);
        }
    }

    private void removerProva(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        Prova prova = provaDAO.buscarPorId(id);
        if (prova != null && prova.getCaminhoArquivo() != null) {
            // Remover arquivos associados
            for (String arquivo : prova.getCaminhoArquivo().split(",")) {
                uploadHandler.removerArquivo(arquivo);
            }
        }
        
        if (provaDAO.remover(id)) {
            request.getSession().setAttribute("mensagem", "Prova removida com sucesso!");
        } else {
            request.getSession().setAttribute("erro", "Falha ao remover prova");
        }
        
        response.sendRedirect("provas");
    }

    private void preencherProva(HttpServletRequest request, Prova prova) throws ServletException {
        try {
            if (request.getParameter("idQueixa") == null || request.getParameter("idQueixa").isEmpty()) {
                throw new ServletException("Queixa é obrigatória");
            }
            prova.setIdQueixa(Integer.parseInt(request.getParameter("idQueixa")));
            
            if (request.getParameter("tipo") == null || request.getParameter("tipo").isEmpty()) {
                throw new ServletException("Tipo de prova é obrigatório");
            }
            prova.setTipo(request.getParameter("tipo"));
            
            prova.setDescricao(request.getParameter("descricao"));
            
            if (prova.getIdProva() == 0) {
                prova.setDataUpload(new Timestamp(new Date().getTime()));
            }
            
            if (request.getParameter("idUsuario") == null || request.getParameter("idUsuario").isEmpty()) {
                throw new ServletException("Usuário responsável é obrigatório");
            }
            prova.setIdUsuario(Integer.parseInt(request.getParameter("idUsuario")));
            
            String dataColetaStr = request.getParameter("dataColeta");
            if (dataColetaStr != null && !dataColetaStr.isEmpty()) {
                prova.setDataColeta(Timestamp.valueOf(dataColetaStr + " 00:00:00"));
            }
        } catch (NumberFormatException e) {
            throw new ServletException("ID inválido", e);
        } catch (IllegalArgumentException e) {
            throw new ServletException("Data inválida", e);
        }
    }

    private void mostrarFormularioComErro(HttpServletRequest request, HttpServletResponse response, Prova prova) 
            throws ServletException, IOException, SQLException {
        List<Map<String, Object>> listaQueixas = queixaDAO.listarQueixasParaCombo();
        List<Map<String, Object>> listaUsuarios = usuarioDAO.listarUsuariosParaCombo();
        
        request.setAttribute("prova", prova);
        request.setAttribute("queixas", listaQueixas);
        request.setAttribute("usuarios", listaUsuarios);
        
        request.getRequestDispatcher("/WEB-INF/views/provas/formulario.jsp").forward(request, response);
    }
}
