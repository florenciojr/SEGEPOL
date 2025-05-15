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
import model.Vitima.TipoVitima;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "VitimaServlet", urlPatterns = {"/vitimas"})
public class VitimaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(VitimaServlet.class.getName());
    
    private VitimaDAO vitimaDAO;

    @Override
    public void init() {
        vitimaDAO = new VitimaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action") != null ? request.getParameter("action") : "list";
        
        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteVitima(request, response);
                    break;
                default:
                    listVitimas(request, response);
                    break;
            }
        } catch (SQLException ex) {
            handleError(request, response, "Erro no banco de dados: " + ex.getMessage(), ex);
        } catch (NumberFormatException ex) {
            handleError(request, response, "ID inválido: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            handleError(request, response, "Erro inesperado: " + ex.getMessage(), ex);
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
            handleError(request, response, "Erro no banco de dados: " + ex.getMessage(), ex);
        } catch (NumberFormatException ex) {
            handleError(request, response, "ID inválido: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            handleError(request, response, "Erro inesperado: " + ex.getMessage(), ex);
        }
    }

private void listVitimas(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, ServletException, IOException {
    String filterQueixa = request.getParameter("filterQueixa");
    
    if (filterQueixa != null && !filterQueixa.isEmpty()) {
        int idQueixa = Integer.parseInt(filterQueixa);
        List<Map<String, Object>> vitimasDetalhadas = vitimaDAO.findDetailedByQueixa(idQueixa);
        
        // Padroniza os nomes das chaves para camelCase
        for (Map<String, Object> vitimaMap : vitimasDetalhadas) {
            if (vitimaMap.containsKey("id_vitima")) {
                vitimaMap.put("idVitima", vitimaMap.remove("id_vitima"));
            }
            if (vitimaMap.containsKey("id_queixa")) {
                vitimaMap.put("idQueixa", vitimaMap.remove("id_queixa"));
            }
            if (vitimaMap.containsKey("id_cidadao")) {
                vitimaMap.put("idCidadao", vitimaMap.remove("id_cidadao"));
            }
            if (vitimaMap.containsKey("tipo_vitima")) {
                vitimaMap.put("tipoVitima", vitimaMap.remove("tipo_vitima"));
            }
        }
        
        request.setAttribute("vitimas", vitimasDetalhadas);
        request.setAttribute("filterQueixa", idQueixa);
    } else {
        List<Vitima> vitimas = vitimaDAO.listAll(0, 100);
        request.setAttribute("vitimas", vitimas);
    }
    
    request.setAttribute("queixas", vitimaDAO.listarQueixasParaDropdown());
    forwardToView(request, response, "/WEB-INF/views/vitimas/listVitimas.jsp");
}

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        request.setAttribute("cidadaos", vitimaDAO.listarCidadaosParaDropdown());
        request.setAttribute("queixas", vitimaDAO.listarQueixasParaDropdown());
        request.setAttribute("tiposVitima", TipoVitima.values());
        request.setAttribute("action", "insert");
        forwardToView(request, response, "/WEB-INF/views/vitimas/formVitima.jsp");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Vitima vitima = vitimaDAO.read(id);
        
        if (vitima == null) {
            throw new ServletException("Vítima não encontrada com ID: " + id);
        }
        
        request.setAttribute("cidadaos", vitimaDAO.listarCidadaosParaDropdown());
        request.setAttribute("queixas", vitimaDAO.listarQueixasParaDropdown());
        request.setAttribute("vitima", vitima);
        request.setAttribute("tiposVitima", TipoVitima.values());
        request.setAttribute("action", "update");
        forwardToView(request, response, "/WEB-INF/views/vitimas/formVitima.jsp");
    }

    private void insertVitima(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        Vitima vitima = extractVitimaFromRequest(request);
        
        if (vitimaDAO.isCidadaoVitima(vitima.getIdQueixa(), vitima.getIdCidadao())) {
            request.setAttribute("error", "Este cidadão já está cadastrado como vítima nesta queixa");
            request.setAttribute("cidadaos", vitimaDAO.listarCidadaosParaDropdown());
            request.setAttribute("queixas", vitimaDAO.listarQueixasParaDropdown());
            request.setAttribute("vitima", vitima);
            request.setAttribute("tiposVitima", TipoVitima.values());
            request.setAttribute("action", "insert");
            forwardToView(request, response, "/WEB-INF/views/vitimas/formVitima.jsp");
            return;
        }
        
        vitimaDAO.create(vitima);
        response.sendRedirect(request.getContextPath() + "/vitimas?success=V%C3%ADtima+cadastrada+com+sucesso");
    }

    private void updateVitima(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        Vitima vitima = extractVitimaFromRequest(request);
        vitima.setIdVitima(Integer.parseInt(request.getParameter("id")));
        
        vitimaDAO.update(vitima);
        response.sendRedirect(request.getContextPath() + "/vitimas?success=V%C3%ADtima+atualizada+com+sucesso");
    }

    private void deleteVitima(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        vitimaDAO.delete(id);
        response.sendRedirect(request.getContextPath() + "/vitimas?success=V%C3%ADtima+removida+com+sucesso");
    }

    private Vitima extractVitimaFromRequest(HttpServletRequest request) {
        Vitima vitima = new Vitima();
        vitima.setIdQueixa(Integer.parseInt(request.getParameter("idQueixa")));
        vitima.setIdCidadao(Integer.parseInt(request.getParameter("idCidadao")));
        vitima.setDescricao(request.getParameter("descricao"));
        vitima.setTipoVitima(TipoVitima.valueOf(request.getParameter("tipoVitima")));
        return vitima;
    }

    private void forwardToView(HttpServletRequest request, HttpServletResponse response, String viewPath)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, 
                           String message, Exception ex) throws ServletException, IOException {
        logger.log(Level.SEVERE, message, ex);
        request.setAttribute("error", message);
        forwardToView(request, response, "/WEB-INF/views/error.jsp");
    }
}
