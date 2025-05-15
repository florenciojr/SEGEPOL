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


import dao.InformanteDAO;
import model.Informante;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "InformanteServlet", urlPatterns = {"/informantes"})
public class InformanteServlet extends HttpServlet {

    private InformanteDAO informanteDAO;

    @Override
    public void init() {
        informanteDAO = new InformanteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "insert":
                    insertInformante(request, response);
                    break;
                case "delete":
                    deleteInformante(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "update":
                    updateInformante(request, response);
                    break;
                case "listByQueixa":
                    listInformantesByQueixa(request, response);
                    break;
                case "list":
                default:
                    listInformantes(request, response);
                    break;
            }
} catch (Exception ex) {
    try {
        request.setAttribute("error", "Erro: " + ex.getMessage());
        listInformantes(request, response);
    } catch (Exception e) {
        // Fallback seguro se listInformantes falhar
        response.sendRedirect(URLEncoder.encode(ex.getMessage(), "UTF-8") + 
                request.getContextPath() + "/errorHandler?message=");
    }
}
    }
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void listInformantes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Informante> listInformantes = informanteDAO.listAll();
        request.setAttribute("listInformantes", listInformantes);
        request.getRequestDispatcher("/WEB-INF/views/informantes/listInformantes.jsp").forward(request, response);
    }

    private void listInformantesByQueixa(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
        List<Informante> listInformantes = informanteDAO.findByQueixa(idQueixa);
        request.setAttribute("listInformantes", listInformantes);
        request.setAttribute("idQueixa", idQueixa);
        request.getRequestDispatcher("/WEB-INF/views/informantes/listInformantes.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Map<String, String>> cidadaos = informanteDAO.listarCidadaosParaDropdown();
        List<Map<String, String>> queixas = informanteDAO.listarQueixasParaDropdown();
        
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("queixas", queixas);
        
        String idQueixaParam = request.getParameter("idQueixa");
        if (idQueixaParam != null && !idQueixaParam.isEmpty()) {
            request.setAttribute("idQueixaSelecionado", idQueixaParam);
        }
        
        request.getRequestDispatcher("/WEB-INF/views/informantes/formInformante.jsp").forward(request, response);
    }

    private void insertInformante(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        try {
            int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
            int idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
            String relato = request.getParameter("relato");
            String confiabilidade = request.getParameter("confiabilidade");
            boolean anonimato = request.getParameter("anonimato") != null;
            
            if (relato == null || relato.isEmpty()) {
                throw new ServletException("Relato é obrigatório");
            }
            
            Informante novoInformante = new Informante();
            novoInformante.setIdCidadao(idCidadao);
            novoInformante.setIdQueixa(idQueixa);
            novoInformante.setRelato(relato);
            novoInformante.setConfiabilidade(confiabilidade);
            novoInformante.setAnonimato(anonimato);
            novoInformante.setDataRegistro(new java.sql.Timestamp(System.currentTimeMillis()));
            
            informanteDAO.create(novoInformante);
            response.sendRedirect("informantes?action=listByQueixa&idQueixa=" + idQueixa);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            showNewForm(request, response);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        Informante informante = informanteDAO.read(id);
        
        if (informante == null) {
            throw new ServletException("Informante não encontrado");
        }
        
        List<Map<String, String>> cidadaos = informanteDAO.listarCidadaosParaDropdown();
        List<Map<String, String>> queixas = informanteDAO.listarQueixasParaDropdown();
        
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("queixas", queixas);
        request.setAttribute("informante", informante);
        request.setAttribute("idQueixaSelecionado", informante.getIdQueixa());
        
        request.getRequestDispatcher("/WEB-INF/views/informantes/formInformante.jsp").forward(request, response);
    }

    private void updateInformante(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
            int idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
            String relato = request.getParameter("relato");
            String confiabilidade = request.getParameter("confiabilidade");
            boolean anonimato = request.getParameter("anonimato") != null;
            
            if (relato == null || relato.isEmpty()) {
                throw new ServletException("Relato é obrigatório");
            }
            
            Informante informante = new Informante();
            informante.setIdInformante(id);
            informante.setIdCidadao(idCidadao);
            informante.setIdQueixa(idQueixa);
            informante.setRelato(relato);
            informante.setConfiabilidade(confiabilidade);
            informante.setAnonimato(anonimato);
            informante.setDataRegistro(new java.sql.Timestamp(System.currentTimeMillis()));
            
            informanteDAO.update(informante);
            response.sendRedirect("informantes?action=listByQueixa&idQueixa=" + idQueixa);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            showEditForm(request, response);
        }
    }

    private void deleteInformante(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
        
        informanteDAO.delete(id);
        response.sendRedirect("informantes?action=listByQueixa&idQueixa=" + idQueixa);
    }
}
