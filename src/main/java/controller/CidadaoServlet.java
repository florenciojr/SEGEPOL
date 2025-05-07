/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;


/**
 *
 * @author JR5
 */


import dao.CidadaoDAO;
import model.Cidadao;
import java.io.IOException;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/cidadao")
public class CidadaoServlet extends HttpServlet {
    private CidadaoDAO cidadaoDAO;

    @Override
    public void init() {
        cidadaoDAO = new CidadaoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "listar";
        }

        try {
            switch (action) {
                case "novo":
                    mostrarFormulario(request, response);
                    break;
                case "editar":
                    editarCidadao(request, response);
                    break;
                case "deletar":
                    deletarCidadao(request, response);
                    break;
                case "buscar":
                    buscarCidadao(request, response);
                    break;
                default:
                    listarCidadaos(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("salvar".equals(action)) {
                salvarCidadao(request, response);
            } else if ("atualizar".equals(action)) {
                atualizarCidadao(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listarCidadaos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("cidadaos", cidadaoDAO.listarCidadaos());
        request.getRequestDispatcher("/WEB-INF/views/cidadaos/listarCidadaos.jsp").forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/cidadaos/formCidadao.jsp").forward(request, response);
    }

    private void editarCidadao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Cidadao cidadao = cidadaoDAO.buscarCidadaoPorId(id);
        request.setAttribute("cidadao", cidadao);
        request.getRequestDispatcher("/WEB-INF/views/cidadaos/formCidadao.jsp").forward(request, response);
    }

    private void salvarCidadao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cidadao cidadao = criarCidadaoAPartirRequest(request);
        cidadaoDAO.inserirCidadao(cidadao);
        response.sendRedirect(request.getContextPath() + "/cidadao");
    }

    private void atualizarCidadao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cidadao cidadao = criarCidadaoAPartirRequest(request);
        cidadao.setIdCidadao(Integer.parseInt(request.getParameter("id")));
        cidadaoDAO.atualizarCidadao(cidadao);
        response.sendRedirect(request.getContextPath() + "/cidadao");
    }

    private void deletarCidadao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        cidadaoDAO.deletarCidadao(id);
        response.sendRedirect(request.getContextPath() + "/cidadao");
    }

    private void buscarCidadao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String documento = request.getParameter("documento");
        Cidadao cidadao = cidadaoDAO.buscarCidadaoPorDocumento(documento);
        request.setAttribute("cidadao", cidadao);
        request.getRequestDispatcher("/WEB-INF/views/cidadaos/detalhesCidadao.jsp").forward(request, response);
    }

    private Cidadao criarCidadaoAPartirRequest(HttpServletRequest request) {
        Cidadao cidadao = new Cidadao();
        cidadao.setNome(request.getParameter("nome"));
        cidadao.setGenero(request.getParameter("genero"));
        cidadao.setDataNascimento(LocalDate.parse(request.getParameter("dataNascimento")));
        cidadao.setDocumentoIdentificacao(request.getParameter("documentoIdentificacao"));
        cidadao.setTelefone(request.getParameter("telefone"));
        cidadao.setEmail(request.getParameter("email"));
        cidadao.setNaturalidade(request.getParameter("naturalidade"));
        cidadao.setRua(request.getParameter("rua"));
        cidadao.setBairro(request.getParameter("bairro"));
        cidadao.setCidade(request.getParameter("cidade"));
        cidadao.setProvincia(request.getParameter("provincia"));
        return cidadao;
    }
}