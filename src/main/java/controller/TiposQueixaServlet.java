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


import dao.TiposQueixaDAO;
import model.TiposQueixa;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TiposQueixaServlet", urlPatterns = {"/tiposqueixa"})
public class TiposQueixaServlet extends HttpServlet {

    private TiposQueixaDAO tiposQueixaDAO;

    @Override
    public void init() {
        tiposQueixaDAO = new TiposQueixaDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null) {
                listarTiposQueixa(request, response);
            } else {
                switch (action) {
                    case "novo":
                        mostrarFormulario(request, response);
                        break;
                    case "adicionar":
                        adicionarTipoQueixa(request, response);
                        break;
                    case "buscarPorGravidade":
                        buscarPorGravidade(request, response);
                        break;
                    default:
                        listarTiposQueixa(request, response);
                        break;
                }
            }
        } catch (ServletException | IOException e) {
            throw new ServletException(e);
        }
    }

    private void listarTiposQueixa(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<TiposQueixa> listaTipos = tiposQueixaDAO.listarTodos();
        request.setAttribute("listaTipos", listaTipos);
        request.setAttribute("gravidades", new String[]{"Leve", "Média", "Grave", "Muito Grave"});
        request.getRequestDispatcher("/WEB-INF/views/tipodequixas/listTiposQueixa.jsp").forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("gravidades", new String[]{"Leve", "Média", "Grave", "Muito Grave"});
        request.getRequestDispatcher("/WEB-INF/views/tipodequixas/formTipoQueixa.jsp").forward(request, response);
    }

    private void adicionarTipoQueixa(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String descricao = request.getParameter("descricao");
        String gravidade = request.getParameter("gravidade");

        TiposQueixa novoTipo = new TiposQueixa();
        novoTipo.setNomeTipo(nome);
        novoTipo.setDescricao(descricao);
        novoTipo.setGravidade(gravidade);

        if (tiposQueixaDAO.inserir(novoTipo)) {
            request.setAttribute("mensagemSucesso", "Tipo de queixa adicionado com sucesso!");
        } else {
            request.setAttribute("mensagemErro", "Erro ao adicionar tipo de queixa.");
        }

        listarTiposQueixa(request, response);
    }

private void buscarPorGravidade(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String gravidade = request.getParameter("gravidade");
    List<TiposQueixa> listaTipos = tiposQueixaDAO.buscarPorGravidade(gravidade);
    
    request.setAttribute("listaTipos", listaTipos);
    request.setAttribute("gravidades", new String[]{"Leve", "Média", "Grave", "Muito Grave"});
    request.setAttribute("gravidadeSelecionada", gravidade);
    request.getRequestDispatcher("/WEB-INF/views/tipodequixas/listTiposQueixa.jsp").forward(request, response);
}
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
