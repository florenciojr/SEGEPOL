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



import dao.VitimaDAO;
import model.Vitima;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "VitimaServlet", urlPatterns = {"/vitimas"})
public class VitimaServlet extends HttpServlet {

    private VitimaDAO vitimaDAO;

    @Override
    public void init() {
        vitimaDAO = new VitimaDAO();
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
                    deleteVitima(request, response);
                    break;
                case "listByQueixa":
                    listVitimasByQueixa(request, response);
                    break;
                default:
                    listVitimas(request, response);
                    break;
            }
        } catch (SQLException ex) {
            handleError(request, response, "Erro no banco de dados: " + ex.getMessage());
        } catch (Exception ex) {
            handleError(request, response, "Erro inesperado: " + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            switch (action) {
                case "insert":
                    insertVitima(request, response);
                    break;
                case "update":
                    updateVitima(request, response);
                    break;
                default:
                    listVitimas(request, response);
                    break;
            }
        } catch (SQLException ex) {
            handleError(request, response, "Erro no banco de dados: " + ex.getMessage());
        } catch (Exception ex) {
            handleError(request, response, "Erro inesperado: " + ex.getMessage());
        }
    }

    private void listVitimas(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Vitima> listVitimas = vitimaDAO.listAll();
        request.setAttribute("listVitimas", listVitimas);
        request.getRequestDispatcher("/WEB-INF/views/vitimas/listVitimas.jsp").forward(request, response);
    }

    private void listVitimasByQueixa(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
        List<Map<String, Object>> vitimasDetalhadas = vitimaDAO.findDetailedByQueixa(idQueixa);
        
        request.setAttribute("vitimas", vitimasDetalhadas);
        request.setAttribute("idQueixa", idQueixa);
        request.getRequestDispatcher("/WEB-INF/views/vitimas/listVitimas.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
        List<Map<String, String>> cidadaos = vitimaDAO.listarCidadaosParaDropdown();
        
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("idQueixa", idQueixa);
        request.setAttribute("tiposVitima", Vitima.TipoVitima.values());
        request.getRequestDispatcher("/WEB-INF/views/vitimas/formVitima.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Vitima vitima = vitimaDAO.read(id);
        
        if (vitima == null) {
            throw new ServletException("Vítima não encontrada");
        }
        
        List<Map<String, String>> cidadaos = vitimaDAO.listarCidadaosParaDropdown();
        
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("vitima", vitima);
        request.setAttribute("tiposVitima", Vitima.TipoVitima.values());
        request.getRequestDispatcher("/WEB-INF/views/vitimas/formVitima.jsp").forward(request, response);
    }

    private void insertVitima(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
        int idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
        String descricao = request.getParameter("descricao");
        Vitima.TipoVitima tipo = Vitima.TipoVitima.valueOf(request.getParameter("tipoVitima"));
        
        // Verifica se o cidadão já é vítima desta queixa
        if (vitimaDAO.isCidadaoVitima(idQueixa, idCidadao)) {
            response.sendRedirect("vitimas?action=listByQueixa&idQueixa=" + idQueixa + "&error=Cidadão já cadastrado como vítima");
            return;
        }
        
        Vitima novaVitima = new Vitima();
        novaVitima.setIdQueixa(idQueixa);
        novaVitima.setIdCidadao(idCidadao);
        novaVitima.setDescricao(descricao);
        novaVitima.setTipoVitima(tipo);
        
        vitimaDAO.create(novaVitima);
        response.sendRedirect("vitimas?action=listByQueixa&idQueixa=" + idQueixa);
    }

    private void updateVitima(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
        int idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
        String descricao = request.getParameter("descricao");
        Vitima.TipoVitima tipo = Vitima.TipoVitima.valueOf(request.getParameter("tipoVitima"));
        
        Vitima vitima = new Vitima();
        vitima.setIdVitima(id);
        vitima.setIdQueixa(idQueixa);
        vitima.setIdCidadao(idCidadao);
        vitima.setDescricao(descricao);
        vitima.setTipoVitima(tipo);
        
        vitimaDAO.update(vitima);
        response.sendRedirect("vitimas?action=listByQueixa&idQueixa=" + idQueixa);
    }

    private void deleteVitima(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
        
        vitimaDAO.delete(id);
        response.sendRedirect("vitimas?action=listByQueixa&idQueixa=" + idQueixa);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String message) 
            throws ServletException, IOException {
        request.setAttribute("error", message);
        try {
            listVitimas(request, response);
        } catch (Exception e) {
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
