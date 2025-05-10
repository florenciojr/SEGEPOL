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
    
    // Constantes para caminhos e mensagens
    private static final String LOGIN_PAGE = "/WEB-INF/views/login/login.jsp";
    private static final String TEST_PAGE = "/WEB-INF/views/testpage.jsp";
    private static final String ERRO_SERVIDOR = "Ocorreu um erro no servidor. Por favor, tente novamente mais tarde.";
    private static final String CONTA_INATIVA = "Sua conta está desativada. Entre em contato com o administrador.";
    private static final String CREDENCIAIS_INVALIDAS = "Credenciais inválidas. Verifique seus dados e tente novamente.";

    @Override
    public void init() throws ServletException {
        super.init();
        this.usuarioDAO = new UsuarioDAO();
    }
    
     // Implementar o método redirecionarPorPerfil corretamente
    private void redirecionarPorPerfil(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }}
    
    
    
    
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        if (isUsuarioLogado(request)) {
            redirecionarPorPerfil(request, response);
            return;
        }
        
        exibirPaginaLogin(request, response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String identificacao = request.getParameter("identificacao");
        String senha = request.getParameter("senha");
        
        if (!validarCampos(identificacao, senha)) {
            exibirPaginaLogin(request, response, "Por favor, preencha todos os campos corretamente.");
            return;
        }
        
        try {
            Usuario usuario = usuarioDAO.autenticarUsuario(
                identificacao.trim(), 
                senha, 
                request.getRemoteAddr()
            );
            
            if (usuario == null) {
                exibirPaginaLogin(request, response, CREDENCIAIS_INVALIDAS);
                return;
            }
            
            if (!"ativo".equalsIgnoreCase(usuario.getStatus())) {
                exibirPaginaLogin(request, response, CONTA_INATIVA);
                return;
            }
            
            criarSessaoUsuario(request, usuario);
            logAcesso(usuario, request);
            redirecionarPorPerfil(request, response);
            
        } catch (SQLException e) {
            logError("Erro durante autenticação", e);
            exibirPaginaLogin(request, response, ERRO_SERVIDOR);
        }
    }

    // ========== MÉTODOS AUXILIARES ==========
    
    private boolean isUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("usuarioLogado") != null;
    }
    
    private boolean validarCampos(String identificacao, String senha) {
        return identificacao != null && !identificacao.trim().isEmpty() &&
               senha != null && !senha.trim().isEmpty();
    }
    
    private void exibirPaginaLogin(HttpServletRequest request, HttpServletResponse response, String mensagemErro) 
            throws ServletException, IOException {
        
        if (mensagemErro != null) {
            request.setAttribute("erro", mensagemErro);
        }
        request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
    }
    
    private void criarSessaoUsuario(HttpServletRequest request, Usuario usuario) {
        HttpSession session = request.getSession();
        session.setAttribute("usuarioLogado", usuario);
        
        // Configura tempo de inatividade da sessão (30 minutos)
        session.setMaxInactiveInterval(30 * 60);
    }
    
    private void logAcesso(Usuario usuario, HttpServletRequest request) {
        System.out.printf("[LOGIN] Usuário %s (%s) acessou de %s%n",
            usuario.getNome(),
            usuario.getPerfil(),
            request.getRemoteAddr());
    }
    
    private void logError(String mensagem, Exception e) {
        System.err.println("[ERRO] " + mensagem);
        e.printStackTrace();
    }
    
    // ========== REDIRECIONAMENTO ==========
    
//    private void redirecionarPorPerfil(HttpServletRequest request, HttpServletResponse response)
//            throws IOException, ServletException {
//        
//        // TEMPORÁRIO: Redireciona todos para testpage
//        request.getRequestDispatcher(TEST_PAGE).forward(request, response);
//        
//        /* FUTURA IMPLEMENTAÇÃO:
//        HttpSession session = request.getSession(false);
//        if (session == null) {
//            response.sendRedirect(request.getContextPath() + "/login");
//            return;
//        }
//        
//        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
//        if (usuario == null) {
//            response.sendRedirect(request.getContextPath() + "/login");
//            return;
//        }
//        
//        String paginaDestino = determinarPaginaPorPerfil(usuario.getPerfil());
//        
//        if (isRequisicaoAjax(request)) {
//            enviarRespostaJson(response, paginaDestino);
//        } else {
//            request.getRequestDispatcher(paginaDestino).forward(request, response);
//        }
//        */
//    }
//    
    /* MÉTODOS PARA IMPLEMENTAÇÃO FUTURA:
    
    private String determinarPaginaPorPerfil(String perfil) {
        // Mapeamento de perfis para páginas
        switch(perfil.toLowerCase()) {
            case "administrador":
                return "/WEB-INF/views/admin/dashboard.jsp";
            case "comandante":
                return "/WEB-INF/views/comandante/dashboard.jsp";
            case "agente":
                return "/WEB-INF/views/agente/reclamacoes.jsp";
            default:
                return "/WEB-INF/views/user/pagina-inicial.jsp";
        }
    }
    
    private boolean isRequisicaoAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
    
    private void enviarRespostaJson(HttpServletResponse response, String redirectPage) 
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format("{\"redirect\":\"%s\"}", redirectPage));
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Implementação de logout seguro
        HttpSession session = request.getSession(false);
        if (session != null) {
            registrarLogout((Usuario) session.getAttribute("usuarioLogado"));
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login");
    }
    
    private void registrarLogout(Usuario usuario) {
        // Registrar ação de logout no sistema
        System.out.printf("[LOGOUT] Usuário %s saiu do sistema%n", usuario.getNome());
    }
    */
}
