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

import dao.IntimacaoDAO;
import model.Intimacao;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "IntimacaoServlet", urlPatterns = {"/intimacoes"})
public class IntimacaoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Definir ação padrão como 'list' se não for especificada
        String action = request.getParameter("action") == null ? "list" : request.getParameter("action");
        
        try {
            switch (action.toLowerCase()) {
                case "list":
                    listarIntimacoes(request, response);
                    break;
                case "new":
                    mostrarFormulario(request, response, new Intimacao(), "create");
                    break;
                case "create":
                    criarIntimacao(request, response);
                    break;
                case "edit":
                    mostrarFormularioEdicao(request, response);
                    break;
                case "update":
                    atualizarIntimacao(request, response);
                    break;
                case "delete":
                    deletarIntimacao(request, response);
                    break;
                case "view":
                    visualizarIntimacao(request, response);
                    break;
                default:
                    listarIntimacoes(request, response);
            }
        } catch (SQLException ex) {
            handleDatabaseError(request, response, ex);
        } catch (NumberFormatException ex) {
            handleUserError(request, response, "ID inválido: " + ex.getMessage());
        } catch (DateTimeParseException ex) {
            handleUserError(request, response, "Formato de data inválido. Use o formato yyyy-MM-dd");
        } catch (IllegalArgumentException ex) {
            handleUserError(request, response, ex.getMessage());
        } catch (Exception ex) {
            handleUnexpectedError(request, response, ex);
        }
    }

private void listarIntimacoes(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, ServletException, IOException {
    IntimacaoDAO dao = new IntimacaoDAO();
    List<Intimacao> intimacoes = dao.listarIntimacoes();
    
    // Log para depuração
    System.out.println("Número de intimações recuperadas: " + intimacoes.size());
    if (!intimacoes.isEmpty()) {
        System.out.println("Primeira intimação - ID: " + intimacoes.get(0).getIdIntimacao());
        System.out.println("Local comparecimento: " + intimacoes.get(0).getLocalComparecimento());
    }
    
    request.setAttribute("intimacoes", intimacoes);
    request.setAttribute("pageTitle", "Lista de Intimações");
    forwardToView("/WEB-INF/views/intimacoes/listarIntimacoes.jsp", request, response);
}

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, 
            Intimacao intimacao, String action) throws SQLException, ServletException, IOException {
        
        IntimacaoDAO dao = new IntimacaoDAO();
        Map<Integer, String> cidadaos = dao.listarCidadaosRecentes();
        Map<Integer, String> queixas = dao.listarQueixasRecentes();
        
        request.setAttribute("intimacao", intimacao);
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("queixas", queixas);
        request.setAttribute("statusPermitidos", dao.getStatusPermitidos());
        request.setAttribute("action", action);
        request.setAttribute("pageTitle", action.equals("create") ? "Nova Intimação" : "Editar Intimação");
        
        forwardToView("/WEB-INF/views/intimacoes/formIntimacao.jsp", request, response);
    }

    private void mostrarFormularioEdicao(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        IntimacaoDAO dao = new IntimacaoDAO();
        Intimacao intimacao = dao.buscarIntimacaoPorId(id);
        
        if (intimacao == null) {
            throw new IllegalArgumentException("Intimação não encontrada com ID: " + id);
        }
        
        mostrarFormulario(request, response, intimacao, "update");
    }

    private void visualizarIntimacao(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        IntimacaoDAO dao = new IntimacaoDAO();
        Intimacao intimacao = dao.buscarIntimacaoPorId(id);
        
        if (intimacao == null) {
            throw new IllegalArgumentException("Intimação não encontrada com ID: " + id);
        }
        
        request.setAttribute("intimacao", intimacao);
        request.setAttribute("pageTitle", "Detalhes da Intimação");
        forwardToView("/WEB-INF/views/intimacoes/visualizar.jsp", request, response);
    }

    private void criarIntimacao(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        
        HttpSession session = request.getSession();
        Intimacao intimacao = extrairIntimacaoDaRequisicao(request, session);
        intimacao.setIdIntimacao(0); // Garante novo registro
        
        IntimacaoDAO dao = new IntimacaoDAO();
        if (dao.inserirIntimacao(intimacao)) {
            session.setAttribute("successMessage", "Intimação criada com sucesso!");
            response.sendRedirect(request.getContextPath() + "/intimacoes?action=list");
        } else {
            request.setAttribute("error", "Falha ao criar intimação. Por favor, tente novamente.");
            mostrarFormulario(request, response, intimacao, "create");
        }
    }

    private void atualizarIntimacao(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        
        HttpSession session = request.getSession();
        int id = Integer.parseInt(request.getParameter("id"));
        
        IntimacaoDAO dao = new IntimacaoDAO();
        Intimacao intimacaoExistente = dao.buscarIntimacaoPorId(id);
        
        if (intimacaoExistente == null) {
            throw new IllegalArgumentException("Intimação não encontrada com ID: " + id);
        }
        
        Intimacao intimacaoAtualizada = extrairIntimacaoDaRequisicao(request, session);
        intimacaoAtualizada.setIdIntimacao(id);
        
        if (dao.atualizarIntimacao(intimacaoAtualizada)) {
            session.setAttribute("successMessage", "Intimação atualizada com sucesso!");
            response.sendRedirect(request.getContextPath() + "/intimacoes?action=list");
        } else {
            request.setAttribute("error", "Falha ao atualizar intimação. Por favor, tente novamente.");
            mostrarFormulario(request, response, intimacaoAtualizada, "update");
        }
    }

    private void deletarIntimacao(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        HttpSession session = request.getSession();
        int id = Integer.parseInt(request.getParameter("id"));
        
        IntimacaoDAO dao = new IntimacaoDAO();
        if (dao.deletarIntimacao(id)) {
            session.setAttribute("successMessage", "Intimação removida com sucesso!");
        } else {
            session.setAttribute("error", "Falha ao remover intimação. Por favor, tente novamente.");
        }
        
        response.sendRedirect(request.getContextPath() + "/intimacoes?action=list");
    }

    private Intimacao extrairIntimacaoDaRequisicao(HttpServletRequest request, HttpSession session) {
        Intimacao intimacao = new Intimacao();
        
        // Validar e extrair parâmetros
        try {
            intimacao.setIdCidadao(Integer.parseInt(request.getParameter("idCidadao")));
            intimacao.setIdQueixa(Integer.parseInt(request.getParameter("idQueixa")));
            
            // Pegar ID do usuário da sessão em produção
            Integer idUsuario = (Integer) session.getAttribute("usuarioId");
            if (idUsuario == null) {
                idUsuario = 1; // Apenas para desenvolvimento
            }
            intimacao.setIdUsuario(idUsuario);
            
            intimacao.setMotivo(validarCampoObrigatorio(request.getParameter("motivo"), "Motivo"));
            intimacao.setDataEmissao(LocalDate.parse(validarCampoObrigatorio(request.getParameter("dataEmissao"), "Data de Emissão")));
            intimacao.setDataComparecimento(LocalDate.parse(validarCampoObrigatorio(request.getParameter("dataComparecimento"), "Data de Comparecimento")));
            intimacao.setLocalComparecimento(validarCampoObrigatorio(request.getParameter("localComparecimento"), "Local de Comparecimento"));
            intimacao.setStatus(validarCampoObrigatorio(request.getParameter("status"), "Status"));
            intimacao.setObservacoes(request.getParameter("observacoes")); // Opcional
            
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Formato de data inválido. Use o formato yyyy-MM-dd");
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("ID inválido: " + ex.getMessage());
        }
        
        return intimacao;
    }
    
    

    private String validarCampoObrigatorio(String valor, String nomeCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(nomeCampo + " é obrigatório");
        }
        return valor.trim();
    }

    private void forwardToView(String viewPath, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(viewPath).forward(request, response);
    }

    private void handleDatabaseError(HttpServletRequest request, HttpServletResponse response, SQLException ex)
            throws ServletException, IOException {
        request.setAttribute("error", "Erro no banco de dados: " + ex.getMessage());
        forwardToView("/WEB-INF/views/error.jsp", request, response);
    }

    private void handleUserError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        forwardToView("/WEB-INF/views/error.jsp", request, response);
    }

    private void handleUnexpectedError(HttpServletRequest request, HttpServletResponse response, Exception ex)
            throws ServletException, IOException {
        request.setAttribute("error", "Erro inesperado: " + ex.getMessage());
        forwardToView("/WEB-INF/views/error.jsp", request, response);
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
