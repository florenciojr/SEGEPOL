package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

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




import dao.QueixaDAO;
import model.Queixa;
import model.TiposQueixa;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;
import model.Conexao;

@WebServlet(name = "QueixaServlet", urlPatterns = {"/queixas"})
public class QueixaServlet extends HttpServlet {
    private QueixaDAO queixaDAO;

    @Override
    public void init() {
        queixaDAO = new QueixaDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            
            if (action == null || action.isEmpty()) {
                listQueixas(request, response);
                return;
            }

            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteQueixa(request, response);
                    break;
                case "view":
                    viewQueixa(request, response);
                    break;
                default:
                    listQueixas(request, response);
            }
        } catch (SQLException e) {
            handleSQLException(e, request, response);
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            
            if (action == null || action.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação não especificada");
                return;
            }

            switch (action) {
                case "insert":
                    insertQueixa(request, response);
                    break;
                case "update":
                    updateQueixa(request, response);
                    break;
                default:
                    listQueixas(request, response);
            }
        } catch (SQLException e) {
            handleSQLException(e, request, response);
        } catch (Exception e) {
            throw new ServletException("Erro ao processar requisição", e);
        }
    }

    private void listQueixas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        request.setAttribute("queixas", queixaDAO.listarQueixas());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/queixas/listQueixas.jsp");
        dispatcher.forward(request, response);
    }
    
    
    
    public Map<Integer, String> listarUsuariosRecentes() throws SQLException {
    Map<Integer, String> usuarios = new LinkedHashMap<>();
    String sql = "SELECT id_usuario, nome FROM usuarios ORDER BY id_usuario DESC LIMIT 20";
    
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            int id = rs.getInt("id_usuario");
            String nome = rs.getString("nome");
            usuarios.put(id, nome != null ? nome : "Usuário " + id);
        }
    }
    return usuarios;
}
    
    

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // Carrega dados para os dropdowns
        loadDropdownData(request);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/queixas/formQueixas.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        Queixa queixa = queixaDAO.buscarQueixaPorId(id);
        
        if (queixa == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Queixa não encontrada");
            return;
        }
        
        // Carrega dados para os dropdowns
        loadDropdownData(request);
        request.setAttribute("queixa", queixa);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/queixas/formQueixas.jsp");
        dispatcher.forward(request, response);
    }

    private void viewQueixa(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, SQLException {
    int id = Integer.parseInt(request.getParameter("id"));
    Queixa queixa = queixaDAO.buscarQueixaComRelacionamentos(id);
    
    if (queixa == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Queixa não encontrada");
        return;
    }
    
    request.setAttribute("queixa", queixa);
    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/queixas/verQueixas.jsp");
    dispatcher.forward(request, response);
}

   private void insertQueixa(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException, SQLException {
    Queixa queixa = new Queixa();
    populateQueixaFromRequest(queixa, request);
    
    // Define status padrão se não foi especificado
    if (queixa.getStatus() == null || queixa.getStatus().isEmpty()) {
        queixa.setStatus("Registrada");
    }
    
    // Verifica se um usuário foi selecionado
    if (queixa.getIdUsuario() == 0) {
        request.setAttribute("error", "Por favor, selecione um usuário responsável");
        loadDropdownData(request);
        request.setAttribute("queixa", queixa);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/queixas/formQueixas.jsp");
        dispatcher.forward(request, response);
        return;
    }

    if (queixaDAO.inserirQueixa(queixa)) {
        request.getSession().setAttribute("successMessage", "Queixa registrada com sucesso!");
        response.sendRedirect("queixas");
    } else {
        request.setAttribute("error", "Erro ao registrar queixa");
        loadDropdownData(request);
        request.setAttribute("queixa", queixa);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/queixas/formQueixas.jsp");
        dispatcher.forward(request, response);
    }
}
    

    private void updateQueixa(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        int id = Integer.parseInt(request.getParameter("idQueixa"));
        
        Queixa queixa = queixaDAO.buscarQueixaPorId(id);
        if (queixa == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Queixa não encontrada");
            return;
        }
        
        populateQueixaFromRequest(queixa, request);

        if (queixaDAO.atualizarQueixa(queixa)) {
            request.getSession().setAttribute("successMessage", "Queixa atualizada com sucesso!");
            response.sendRedirect("queixas");
        } else {
            request.setAttribute("error", "Erro ao atualizar queixa");
            loadDropdownData(request);
            request.setAttribute("queixa", queixa);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/queixas/formQueixas.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void deleteQueixa(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        if (queixaDAO.deletarQueixa(id)) {
            request.getSession().setAttribute("successMessage", "Queixa excluída com sucesso!");
        } else {
            request.getSession().setAttribute("errorMessage", "Erro ao excluir queixa");
        }
        
        response.sendRedirect("queixas");
    }

 private void populateQueixaFromRequest(Queixa queixa, HttpServletRequest request) {
    queixa.setTitulo(request.getParameter("titulo"));
    queixa.setDescricao(request.getParameter("descricao"));
    
    if (request.getParameter("dataIncidente") != null && !request.getParameter("dataIncidente").isEmpty()) {
        queixa.setDataIncidente(LocalDate.parse(request.getParameter("dataIncidente")));
    }
    
    queixa.setLocalIncidente(request.getParameter("localIncidente"));
    queixa.setCoordenadas(request.getParameter("coordenadas"));
    
    if (request.getParameter("status") != null) {
        queixa.setStatus(request.getParameter("status"));
    }
    
    queixa.setIdCidadao(Integer.parseInt(request.getParameter("idCidadao")));
    queixa.setIdTipo(Integer.parseInt(request.getParameter("idTipo")));
    
    // Adiciona o ID do usuário (temporário)
    if (request.getParameter("idUsuario") != null) {
        queixa.setIdUsuario(Integer.parseInt(request.getParameter("idUsuario")));
    }
}

private void loadDropdownData(HttpServletRequest request) throws SQLException {
    // Carrega cidadãos para dropdown
    request.setAttribute("cidadaos", queixaDAO.listarCidadaosRecentes());
    
    // Carrega tipos de queixa para dropdown
    request.setAttribute("tiposQueixa", queixaDAO.listarTiposQueixa());
    
    // Carrega status permitidos
    request.setAttribute("statusPermitidos", queixaDAO.getStatusPermitidos());
    
    // Carrega usuários para dropdown (temporário)
    request.setAttribute("usuarios", queixaDAO.listarUsuariosRecentes());
}

    private void handleSQLException(SQLException e, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        e.printStackTrace();
        request.setAttribute("error", "Erro no banco de dados: " + e.getMessage());
        request.getRequestDispatcher("/WEB-INF/views/queixas/error.jsp").forward(request, response);
    }
    

    
}
