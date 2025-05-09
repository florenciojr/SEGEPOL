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



import dao.UsuarioDAO;
import model.Usuario;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;



@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogado") != null) {
            // Usuário já logado, redireciona para página inicial
            redirecionarPorCargo((Usuario) session.getAttribute("usuarioLogado"), request, response);
            return;
        }
        // Mostra a página de login
        request.getRequestDispatcher("/view/login/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String identificacao = request.getParameter("identificacao");
        String senha = request.getParameter("senha");
        String ip = request.getRemoteAddr();

        try {
            Usuario usuario = usuarioDAO.autenticarUsuario(identificacao, senha,ip);
            
            if (usuario != null) {
                if (!"ativo".equalsIgnoreCase(usuario.getStatus())) {
                    request.setAttribute("erro", "Sua conta está desativada");
                    request.getRequestDispatcher("/view/login/login.jsp").forward(request, response);
                    return;
                }
                
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogado", usuario);
                
                // Debug seguro
                logDebugInfo(usuario);
                
                // Redireciona conforme o cargo
                redirecionarPorCargo(usuario, request, response);
                return;
            } else {
                request.setAttribute("erro", "Credenciais inválidas");
                request.getRequestDispatcher("/view/login/login.jsp").forward(request, response);
                return;
            }
        } catch (SQLException e) {
            request.setAttribute("erro", "Erro no servidor");
            request.getRequestDispatcher("/view/login/login.jsp").forward(request, response);
        }
    }

    /**
     * MÉTODO PARA REDIRECIONAMENTO POR CARGO (IMPLEMENTE NO FUTURO)
     * 
     * TODO: Implementar lógica completa de redirecionamento baseado no cargo
     * 
     * Cargos planejados:
     * - "Administrador" -> /admin/dashboard
     * - "Comandante" -> /comandante/dashboard
     * - "Agente" -> /agente/reclamacoes
     * - Outros -> /view/pagina-inicial.jsp
     * 
     * @param usuario Usuário autenticado
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    private void redirecionarPorCargo(Usuario usuario, HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        
        // ATUALMENTE REDIRECIONA TODOS PARA TESTPAGE (REMOVA NO FUTURO)
        response.sendRedirect(request.getContextPath() + "/view/testpage.jsp");
        
        /* FUTURA IMPLEMENTAÇÃO (DESCOMENTE QUANDO PRONTO)
        String cargo = usuario.getCargo().toLowerCase();
        String redirectPage;
        
        switch(cargo) {
            case "administrador":
                redirectPage = "/admin/dashboard.jsp";
                break;
            case "comandante":
                redirectPage = "/comandante/dashboard.jsp";
                break;
            case "agente":
                redirectPage = "/agente/reclamacoes.jsp";
                break;
            default:
                redirectPage = "/view/pagina-inicial.jsp";
        }
        
        // Verifica se é uma requisição AJAX ou normal
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.setContentType("application/json");
            response.getWriter().write("{\"redirect\":\"" + redirectPage + "\"}");
        } else {
            response.sendRedirect(request.getContextPath() + redirectPage);
        }
        */
    }

    private void logDebugInfo(Usuario usuario) {
        System.out.println("\n=== DEBUG: DADOS DO USUÁRIO ===");
        System.out.println("ID: " + usuario.getId_usuario());
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Perfil: " + usuario.getPerfil());
        System.out.println("Status: " + usuario.getStatus());
        System.out.println("Cargo: " + usuario.getCargo());
        System.out.println("=============================\n");
    }
}
