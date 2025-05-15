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


import dao.ClassificacaoCidadaoDAO;
import model.ClassificacaoCidadao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ClassificacaoCidadaoServlet", urlPatterns = {"/classificacoes"})
public class ClassificacaoCidadaoServlet extends HttpServlet {

    private ClassificacaoCidadaoDAO classificacaoDAO;

    @Override
    public void init() {
        classificacaoDAO = new ClassificacaoCidadaoDAO();
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
                insertClassificacao(request, response);
                break;
            case "delete":
                deleteClassificacao(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "update":
                updateClassificacao(request, response);
                break;
            case "listByCidadao":
                listClassificacoesByCidadao(request, response);
                break;
            case "list":
            default:
                listClassificacoes(request, response);
                break;
        }
    } catch (SQLException ex) {
        request.setAttribute("error", "Erro no banco de dados: " + ex.getMessage());
        try {
            listClassificacoes(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao listar classificações", e);
        }
    } catch (ServletException | IOException ex) {
        throw ex; // Re-lança exceções que devem ser tratadas pelo container
    } catch (Exception ex) {
        request.setAttribute("error", "Ocorreu um erro inesperado: " + ex.getMessage());
        try {
            listClassificacoes(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao listar classificações", e);
        }
    }
}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void listClassificacoes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<ClassificacaoCidadao> listClassificacoes = classificacaoDAO.listAll();
        request.setAttribute("listClassificacoes", listClassificacoes);
        request.getRequestDispatcher("/WEB-INF/views/classificacoes/listClassificacoes.jsp").forward(request, response);
    }

    private void listClassificacoesByCidadao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
        List<ClassificacaoCidadao> listClassificacoes = classificacaoDAO.findByCidadao(idCidadao);
        request.setAttribute("listClassificacoes", listClassificacoes);
        request.setAttribute("idCidadao", idCidadao);
        request.getRequestDispatcher("/WEB-INF/views/classificacoes/listClassificacoes.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Map<String, String>> cidadaos = classificacaoDAO.listarCidadaosParaDropdown();
        
        request.setAttribute("cidadaos", cidadaos);
        
        String idCidadaoParam = request.getParameter("idCidadao");
        if (idCidadaoParam != null && !idCidadaoParam.isEmpty()) {
            request.setAttribute("idCidadaoSelecionado", idCidadaoParam);
        }
        
        request.getRequestDispatcher("/WEB-INF/views/classificacoes/formClassificacao.jsp").forward(request, response);
    }

    private void insertClassificacao(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        try {
            int idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
            String classificacao = request.getParameter("classificacao");
            String observacoes = request.getParameter("observacoes");
            
            if (classificacao == null || classificacao.isEmpty()) {
                throw new ServletException("Classificação é obrigatória");
            }
            
            ClassificacaoCidadao novaClassificacao = new ClassificacaoCidadao();
            novaClassificacao.setIdCidadao(idCidadao);
            novaClassificacao.setClassificacao(classificacao);
            novaClassificacao.setObservacoes(observacoes);
            novaClassificacao.setDataClassificacao(new java.sql.Timestamp(System.currentTimeMillis()));
            
            classificacaoDAO.create(novaClassificacao);
            
            if (request.getParameter("idCidadao") != null) {
                response.sendRedirect("classificacoes?action=listByCidadao&idCidadao=" + idCidadao);
            } else {
                response.sendRedirect("classificacoes");
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            showNewForm(request, response);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        ClassificacaoCidadao classificacao = classificacaoDAO.read(id);
        
        if (classificacao == null) {
            throw new ServletException("Classificação não encontrada");
        }
        
        List<Map<String, String>> cidadaos = classificacaoDAO.listarCidadaosParaDropdown();
        
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("classificacao", classificacao);
        request.setAttribute("idCidadaoSelecionado", classificacao.getIdCidadao());
        
        request.getRequestDispatcher("/WEB-INF/views/classificacoes/formClassificacao.jsp").forward(request, response);
    }

    private void updateClassificacao(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            int idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
            String classificacao = request.getParameter("classificacao");
            String observacoes = request.getParameter("observacoes");
            
            if (classificacao == null || classificacao.isEmpty()) {
                throw new ServletException("Classificação é obrigatória");
            }
            
            ClassificacaoCidadao classif = new ClassificacaoCidadao();
            classif.setIdClassificacao(id);
            classif.setIdCidadao(idCidadao);
            classif.setClassificacao(classificacao);
            classif.setObservacoes(observacoes);
            classif.setDataClassificacao(new java.sql.Timestamp(System.currentTimeMillis()));
            
            classificacaoDAO.update(classif);
            
            if (request.getParameter("idCidadao") != null) {
                response.sendRedirect("classificacoes?action=listByCidadao&idCidadao=" + idCidadao);
            } else {
                response.sendRedirect("classificacoes");
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            showEditForm(request, response);
        }
    }

    private void deleteClassificacao(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        int idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
        
        classificacaoDAO.delete(id);
        response.sendRedirect("classificacoes?action=listByCidadao&idCidadao=" + idCidadao);
    }
}
