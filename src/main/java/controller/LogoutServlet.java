/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UsuarioDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Usuario;

/**
 *
 * @author JR5
 */


@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
            if (usuario != null) {
                try {
                    // Registrar logout no banco de dados
                    new UsuarioDAO().registrarAcesso(usuario.getId_usuario(), false, request.getRemoteAddr());
                } catch (SQLException ex) {
                    Logger.getLogger(LogoutServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            session.invalidate();
        }
        
        // Limpar cookies de autenticação
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/login?logout=true");
    }
}
