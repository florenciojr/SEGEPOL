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


import dao.TestemunhaDAO;
import model.Testemunha;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TestemunhaServlet", urlPatterns = {"/testemunhas"})
public class TestemunhaServlet extends HttpServlet {

    private TestemunhaDAO testemunhaDAO;

    @Override
    public void init() {
        testemunhaDAO = new TestemunhaDAO();
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
                insertTestemunha(request, response);
                break;
            case "delete":
                deleteTestemunha(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "update":
                updateTestemunha(request, response);
                break;
            case "listByQueixa":
                listTestemunhasByQueixa(request, response);
                break;
            case "list":
            default:
                listTestemunhas(request, response);
                break;
        }
    } catch (SQLException ex) {
        request.setAttribute("error", "Erro no banco de dados: " + ex.getMessage());
        try {
            listTestemunhas(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao listar testemunhas", e);
        }
    } catch (ServletException | IOException ex) {
        throw ex; // Re-lança exceções que devem ser tratadas pelo container
    } catch (Exception ex) {
        request.setAttribute("error", "Ocorreu um erro inesperado: " + ex.getMessage());
        try {
            listTestemunhas(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao listar testemunhas", e);
        }
    }
}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void listTestemunhas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Testemunha> listTestemunhas = testemunhaDAO.listAll();
        request.setAttribute("listTestemunhas", listTestemunhas);
        request.getRequestDispatcher("/WEB-INF/views/testemunhas/listTestemunhas.jsp").forward(request, response);
    }

    private void listTestemunhasByQueixa(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
        List<Testemunha> listTestemunhas = testemunhaDAO.findByQueixa(idQueixa);
        request.setAttribute("listTestemunhas", listTestemunhas);
        request.setAttribute("idQueixa", idQueixa);
        request.getRequestDispatcher("/WEB-INF/views/testemunhas/listTestemunhas.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // Carrega listas para dropdowns
        List<Map<String, String>> cidadaos = testemunhaDAO.listarCidadaosParaDropdown();
        List<Map<String, String>> queixas = testemunhaDAO.listarQueixasParaDropdown();
        
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("queixas", queixas);
        
        // Verifica se foi passado idQueixa como parâmetro
        String idQueixaParam = request.getParameter("idQueixa");
        if (idQueixaParam != null && !idQueixaParam.isEmpty()) {
            request.setAttribute("idQueixaSelecionado", idQueixaParam);
        }
        
        request.getRequestDispatcher("/WEB-INF/views/testemunhas/formTestemunha.jsp").forward(request, response);
    }

    private void insertTestemunha(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        try {
            int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
            int idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
            String tipoTestemunha = request.getParameter("tipoTestemunha");
            String descricao = request.getParameter("descricao");
            
            // Validações básicas
            if (tipoTestemunha == null || tipoTestemunha.isEmpty()) {
                throw new ServletException("Tipo de testemunha é obrigatório");
            }
            
            Testemunha novaTestemunha = new Testemunha();
            novaTestemunha.setIdCidadao(idCidadao);
            novaTestemunha.setIdQueixa(idQueixa);
            novaTestemunha.setTipoTestemunha(tipoTestemunha);
            novaTestemunha.setDescricao(descricao);
            novaTestemunha.setDataRegistro(new java.sql.Timestamp(System.currentTimeMillis()));
            
            testemunhaDAO.create(novaTestemunha);
            response.sendRedirect("testemunhas?action=listByQueixa&idQueixa=" + idQueixa);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dados numéricos inválidos");
            showNewForm(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            showNewForm(request, response);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        Testemunha testemunha = testemunhaDAO.read(id);
        
        if (testemunha == null) {
            throw new ServletException("Testemunha não encontrada");
        }
        
        // Carrega listas para dropdowns
        List<Map<String, String>> cidadaos = testemunhaDAO.listarCidadaosParaDropdown();
        List<Map<String, String>> queixas = testemunhaDAO.listarQueixasParaDropdown();
        
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("queixas", queixas);
        request.setAttribute("testemunha", testemunha);
        request.setAttribute("idQueixaSelecionado", testemunha.getIdQueixa());
        
        request.getRequestDispatcher("/WEB-INF/views/testemunhas/formTestemunha.jsp").forward(request, response);
    }

    private void updateTestemunha(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
            int idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
            String tipoTestemunha = request.getParameter("tipoTestemunha");
            String descricao = request.getParameter("descricao");
            
            // Validações básicas
            if (tipoTestemunha == null || tipoTestemunha.isEmpty()) {
                throw new ServletException("Tipo de testemunha é obrigatório");
            }
            
            Testemunha testemunha = new Testemunha();
            testemunha.setIdTestemunha(id);
            testemunha.setIdCidadao(idCidadao);
            testemunha.setIdQueixa(idQueixa);
            testemunha.setTipoTestemunha(tipoTestemunha);
            testemunha.setDescricao(descricao);
            testemunha.setDataRegistro(new java.sql.Timestamp(System.currentTimeMillis()));
            
            testemunhaDAO.update(testemunha);
            response.sendRedirect("testemunhas?action=listByQueixa&idQueixa=" + idQueixa);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dados numéricos inválidos");
            showEditForm(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            showEditForm(request, response);
        }
    }

    private void deleteTestemunha(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
        
        testemunhaDAO.delete(id);
        response.sendRedirect("testemunhas?action=listByQueixa&idQueixa=" + idQueixa);
    }
}
