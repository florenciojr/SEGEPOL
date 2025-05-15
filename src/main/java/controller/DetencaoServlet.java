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


import dao.DetencaoDAO;
import model.Detencao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DetencaoServlet", urlPatterns = {"/detencoes"})
public class DetencaoServlet extends HttpServlet {

    private DetencaoDAO detencaoDAO;

    @Override
    public void init() {
        detencaoDAO = new DetencaoDAO();
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
                insertDetencao(request, response);
                break;
            case "delete":
                deleteDetencao(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "update":
                updateDetencao(request, response);
                break;
            case "listByCidadao":
                listDetencoesByCidadao(request, response);
                break;
            case "list":
            default:
                listDetencoes(request, response);
                break;
        }
    } catch (SQLException ex) {
        handleDatabaseError(request, response, ex);
    } catch (ServletException | IOException ex) {
        throw ex;
    } catch (Exception ex) {
        handleGenericError(request, response, ex);
    }
}

// Métodos auxiliares para tratamento de erro
private void handleDatabaseError(HttpServletRequest request, HttpServletResponse response, 
        SQLException ex) throws ServletException, IOException {
    log("Erro de banco de dados", ex);
    request.setAttribute("error", "Erro ao acessar o banco de dados: " + ex.getMessage());
    try {
        listDetencoes(request, response);
    } catch (SQLException e) {
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
}

private void handleGenericError(HttpServletRequest request, HttpServletResponse response,
        Exception ex) throws ServletException, IOException {
    log("Erro inesperado", ex);
    request.setAttribute("error", "Ocorreu um erro inesperado: " + ex.getMessage());
    try {
        listDetencoes(request, response);
    } catch (Exception e) {
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
}
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void listDetencoes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Detencao> listDetencoes = detencaoDAO.listAll();
        request.setAttribute("listDetencoes", listDetencoes);
        request.getRequestDispatcher("/WEB-INF/views/detencoes/listDetencoes.jsp").forward(request, response);
    }

    private void listDetencoesByCidadao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
        List<Detencao> listDetencoes = detencaoDAO.findByCidadao(idCidadao);
        request.setAttribute("listDetencoes", listDetencoes);
        request.setAttribute("idCidadao", idCidadao);
        request.getRequestDispatcher("/WEB-INF/views/detencoes/listDetencoes.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Map<String, String>> cidadaos = detencaoDAO.listarCidadaosParaDropdown();
        List<Map<String, String>> usuarios = detencaoDAO.listarUsuariosParaDropdown();
        
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("usuarios", usuarios);
        
        String idCidadaoParam = request.getParameter("idCidadao");
        if (idCidadaoParam != null && !idCidadaoParam.isEmpty()) {
            request.setAttribute("idCidadaoSelecionado", idCidadaoParam);
        }
        
        request.getRequestDispatcher("/WEB-INF/views/detencoes/formDetencao.jsp").forward(request, response);
    }

    private void insertDetencao(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        try {
            int idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
            String motivo = request.getParameter("motivo");
            String localDetencao = request.getParameter("localDetencao");
            String status = request.getParameter("status");
            int idUsuarioResponsavel = Integer.parseInt(request.getParameter("idUsuarioResponsavel"));
            
            if (motivo == null || motivo.isEmpty() || status == null || status.isEmpty()) {
                throw new ServletException("Motivo e status são obrigatórios");
            }
            
            Detencao novaDetencao = new Detencao();
            novaDetencao.setIdCidadao(idCidadao);
            novaDetencao.setMotivo(motivo);
            novaDetencao.setLocalDetencao(localDetencao);
            novaDetencao.setStatus(status);
            novaDetencao.setIdUsuarioResponsavel(idUsuarioResponsavel);
            novaDetencao.setDataDetencao(new java.sql.Timestamp(System.currentTimeMillis()));
            
            detencaoDAO.create(novaDetencao);
            
            if (request.getParameter("idCidadao") != null) {
                response.sendRedirect("detencoes?action=listByCidadao&idCidadao=" + idCidadao);
            } else {
                response.sendRedirect("detencoes");
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            showNewForm(request, response);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        Detencao detencao = detencaoDAO.read(id);
        
        if (detencao == null) {
            throw new ServletException("Detenção não encontrada");
        }
        
        List<Map<String, String>> cidadaos = detencaoDAO.listarCidadaosParaDropdown();
        List<Map<String, String>> usuarios = detencaoDAO.listarUsuariosParaDropdown();
        
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("usuarios", usuarios);
        request.setAttribute("detencao", detencao);
        request.setAttribute("idCidadaoSelecionado", detencao.getIdCidadao());
        
        request.getRequestDispatcher("/WEB-INF/views/detencoes/formDetencao.jsp").forward(request, response);
    }

    private void updateDetencao(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            int idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
            String motivo = request.getParameter("motivo");
            String localDetencao = request.getParameter("localDetencao");
            String status = request.getParameter("status");
            int idUsuarioResponsavel = Integer.parseInt(request.getParameter("idUsuarioResponsavel"));
            
            if (motivo == null || motivo.isEmpty() || status == null || status.isEmpty()) {
                throw new ServletException("Motivo e status são obrigatórios");
            }
            
            Detencao detencao = new Detencao();
            detencao.setIdDetencao(id);
            detencao.setIdCidadao(idCidadao);
            detencao.setMotivo(motivo);
            detencao.setLocalDetencao(localDetencao);
            detencao.setStatus(status);
            detencao.setIdUsuarioResponsavel(idUsuarioResponsavel);
            detencao.setDataDetencao(new java.sql.Timestamp(System.currentTimeMillis()));
            
            detencaoDAO.update(detencao);
            
            if (request.getParameter("idCidadao") != null) {
                response.sendRedirect("detencoes?action=listByCidadao&idCidadao=" + idCidadao);
            } else {
                response.sendRedirect("detencoes");
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            showEditForm(request, response);
        }
    }

    private void deleteDetencao(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        int idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
        
        detencaoDAO.delete(id);
        response.sendRedirect("detencoes?action=listByCidadao&idCidadao=" + idCidadao);
    }
}
