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




import dao.DenuncianteDAO;
import model.Denunciante;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DenuncianteServlet", urlPatterns = {"/denunciantes"})
public class DenuncianteServlet extends HttpServlet {

    private DenuncianteDAO denuncianteDAO;

    @Override
    public void init() {
        denuncianteDAO = new DenuncianteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            switch (action != null ? action : "") {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteDenunciante(request, response);
                    break;
                case "listByQueixa":
                    listDenunciantesByQueixa(request, response);
                    break;
                default:
                    listDenunciantes(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            switch (action) {
                case "insert":
                    insertDenunciante(request, response);
                    break;
                case "update":
                    updateDenunciante(request, response);
                    break;
                default:
                    listDenunciantes(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listDenunciantes(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Denunciante> listDenunciantes = denuncianteDAO.listAll();
        request.setAttribute("listDenunciantes", listDenunciantes);
        request.getRequestDispatcher("/WEB-INF/views/denunciantes/listDenunciantes.jsp").forward(request, response);
    }

    private void listDenunciantesByQueixa(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
        List<Denunciante> listDenunciantes = denuncianteDAO.findByQueixa(idQueixa);
        request.setAttribute("listDenunciantes", listDenunciantes);
        request.setAttribute("idQueixa", idQueixa);
        request.getRequestDispatcher("/WEB-INF/views/denunciantes/listDenunciantes.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Map<String, String>> cidadaos = denuncianteDAO.listarCidadaosParaDropdown();
        List<Map<String, String>> queixas = denuncianteDAO.listarQueixasParaDropdown();
        
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("queixas", queixas);
        
        String idQueixaParam = request.getParameter("idQueixa");
        if (idQueixaParam != null && !idQueixaParam.isEmpty()) {
            request.setAttribute("idQueixaSelecionado", idQueixaParam);
        }
        
        request.getRequestDispatcher("/WEB-INF/views/denunciantes/formDenunciante.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Denunciante denunciante = denuncianteDAO.read(id);
        
        if (denunciante == null) {
            throw new ServletException("Denunciante não encontrado");
        }
        
        List<Map<String, String>> cidadaos = denuncianteDAO.listarCidadaosParaDropdown();
        List<Map<String, String>> queixas = denuncianteDAO.listarQueixasParaDropdown();
        
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("queixas", queixas);
        request.setAttribute("denunciante", denunciante);
        request.setAttribute("idQueixaSelecionado", denunciante.getIdQueixa());
        
        request.getRequestDispatcher("/WEB-INF/views/denunciantes/formDenunciante.jsp").forward(request, response);
    }

    private void insertDenunciante(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
        int idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
        String modoDenuncia = request.getParameter("modoDenuncia");
        
        Denunciante novoDenunciante = new Denunciante();
        novoDenunciante.setIdCidadao(idCidadao);
        novoDenunciante.setIdQueixa(idQueixa);
        novoDenunciante.setModoDenuncia(modoDenuncia);
        novoDenunciante.setDataDenuncia(new java.sql.Timestamp(System.currentTimeMillis()));
        
        denuncianteDAO.create(novoDenunciante);
        response.sendRedirect("denunciantes?action=listByQueixa&idQueixa=" + idQueixa);
    }

    private void updateDenunciante(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
        int idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
        String modoDenuncia = request.getParameter("modoDenuncia");
        
        Denunciante denunciante = new Denunciante();
        denunciante.setIdDenunciante(id);
        denunciante.setIdCidadao(idCidadao);
        denunciante.setIdQueixa(idQueixa);
        denunciante.setModoDenuncia(modoDenuncia);
        denunciante.setDataDenuncia(new java.sql.Timestamp(System.currentTimeMillis()));
        
        denuncianteDAO.update(denunciante);
        response.sendRedirect("denunciantes?action=listByQueixa&idQueixa=" + idQueixa);
    }

    private void deleteDenunciante(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
        
        denuncianteDAO.delete(id);
        response.sendRedirect("denunciantes?action=listByQueixa&idQueixa=" + idQueixa);
    }
}
