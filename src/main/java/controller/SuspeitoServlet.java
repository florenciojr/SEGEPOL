/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;



/**
 *
 * @author JR5
 */


import dao.SuspeitoDAO;
import model.Suspeito;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SuspeitoServlet", urlPatterns = {"/suspeitos"})
public class SuspeitoServlet extends HttpServlet {

    private SuspeitoDAO suspeitoDAO;

    @Override
    public void init() {
        suspeitoDAO = new SuspeitoDAO();
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
                    insertSuspeito(request, response);
                    break;
                case "delete":
                    deleteSuspeito(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "update":
                    updateSuspeito(request, response);
                    break;
                case "listByQueixa":
                    listSuspeitosByQueixa(request, response);
                    break;
                case "list":
                default:
                    listSuspeitos(request, response);
                    break;
            }
        } catch (Exception ex) {
            request.setAttribute("error", "Ocorreu um erro: " + ex.getMessage());
            listSuspeitos(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void listSuspeitos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Suspeito> listSuspeitos = suspeitoDAO.listarTodos();
            request.setAttribute("listSuspeitos", listSuspeitos);
            request.getRequestDispatcher("/WEB-INF/views/suspeitos/listSuspeitos.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Erro ao listar suspeitos: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    private void listSuspeitosByQueixa(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
            List<Suspeito> listSuspeitos = suspeitoDAO.buscarPorQueixa(idQueixa);
            request.setAttribute("listSuspeitos", listSuspeitos);
            request.setAttribute("idQueixa", idQueixa);
            request.getRequestDispatcher("/WEB-INF/views/suspeitos/listSuspeitos.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID da queixa inválido");
            listSuspeitos(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Erro ao listar suspeitos: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

private void showNewForm(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        // Carrega listas para dropdowns
        List<Map<String, String>> cidadaos = suspeitoDAO.listarCidadaosParaDropdown();
        List<Map<String, String>> queixas = suspeitoDAO.listarQueixasParaDropdown();
        
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("queixas", queixas);
        
        // Verifica se foi passado idQueixa como parâmetro
        String idQueixaParam = request.getParameter("idQueixa");
        if (idQueixaParam != null && !idQueixaParam.isEmpty()) {
            request.setAttribute("idQueixaSelecionado", idQueixaParam);
        }
        
        request.getRequestDispatcher("/WEB-INF/views/suspeitos/formSuspeito.jsp").forward(request, response);
    } catch (Exception e) {
        request.setAttribute("error", "Erro ao carregar formulário: " + e.getMessage());
        listSuspeitos(request, response);
    }
}

private void insertSuspeito(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    try {
        // Obter e validar ID da Queixa
        int idQueixa = Integer.parseInt(request.getParameter("idQueixa"));
        if (idQueixa <= 0) {
            throw new ServletException("Selecione uma queixa válida");
        }

        // Tratar ID do Cidadão (pode ser null para não identificado)
        Integer idCidadao = null;
        String idCidadaoParam = request.getParameter("idCidadao");
        if (idCidadaoParam != null && !idCidadaoParam.isEmpty()) {
            try {
                idCidadao = Integer.parseInt(idCidadaoParam);
            } catch (NumberFormatException e) {
                throw new ServletException("ID do cidadão inválido");
            }
        }

        // Obter outros parâmetros
        String descricao = request.getParameter("descricao");
        String papelIncidente = request.getParameter("papelIncidente");

        // Validar campos obrigatórios
        if (papelIncidente == null || papelIncidente.isEmpty()) {
            throw new ServletException("Selecione o papel no incidente");
        }

        // Criar e salvar o suspeito
        Suspeito novoSuspeito = new Suspeito();
        novoSuspeito.setIdQueixa(idQueixa);
        novoSuspeito.setIdCidadao(idCidadao); // Pode ser null
        novoSuspeito.setDescricao(descricao);
        novoSuspeito.setPapelIncidente(papelIncidente);

        if (suspeitoDAO.inserir(novoSuspeito)) {
            response.sendRedirect("suspeitos?action=listByQueixa&idQueixa=" + idQueixa);
        } else {
            throw new ServletException("Não foi possível cadastrar o suspeito. Verifique os dados e tente novamente.");
        }
    } catch (NumberFormatException e) {
        request.setAttribute("error", "Valores numéricos inválidos. Verifique os dados informados.");
        showNewForm(request, response);
    } catch (Exception e) {
        request.setAttribute("error", e.getMessage());
        showNewForm(request, response);
    }
}

private void showEditForm(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            throw new ServletException("ID do suspeito não fornecido");
        }
        
        int id = Integer.parseInt(idParam);
        Suspeito suspeito = suspeitoDAO.buscarPorId(id);
        
        if (suspeito == null) {
            throw new ServletException("Suspeito não encontrado");
        }
        
        // Carrega listas para dropdowns
        List<Map<String, String>> cidadaos = suspeitoDAO.listarCidadaosParaDropdown();
        List<Map<String, String>> queixas = suspeitoDAO.listarQueixasParaDropdown();
        
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("queixas", queixas);
        request.setAttribute("suspeito", suspeito);
        request.setAttribute("idQueixaSelecionado", suspeito.getIdQueixa());
        
        request.getRequestDispatcher("/WEB-INF/views/suspeitos/formSuspeito.jsp").forward(request, response);
    } catch (NumberFormatException e) {
        request.setAttribute("error", "ID inválido");
        listSuspeitos(request, response);
    } catch (Exception e) {
        request.setAttribute("error", e.getMessage());
        listSuspeitos(request, response);
    }
}

 
    private void updateSuspeito(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String idParam = request.getParameter("id");
            String idQueixaParam = request.getParameter("idQueixa");
            
            if (idParam == null || idParam.isEmpty() || idQueixaParam == null || idQueixaParam.isEmpty()) {
                throw new ServletException("IDs não fornecidos");
            }
            
            int id = Integer.parseInt(idParam);
            int idQueixa = Integer.parseInt(idQueixaParam);
            Integer idCidadao = null;
            
            if (request.getParameter("idCidadao") != null && !request.getParameter("idCidadao").isEmpty()) {
                idCidadao = Integer.parseInt(request.getParameter("idCidadao"));
            }
            
            String descricao = request.getParameter("descricao");
            String papelIncidente = request.getParameter("papelIncidente");

            if (descricao == null || descricao.isEmpty() || papelIncidente == null || papelIncidente.isEmpty()) {
                throw new ServletException("Descrição e papel no incidente são obrigatórios");
            }

            Suspeito suspeito = new Suspeito();
            suspeito.setIdSuspeito(id);
            suspeito.setIdQueixa(idQueixa);
            suspeito.setIdCidadao(idCidadao);
            suspeito.setDescricao(descricao);
            suspeito.setPapelIncidente(papelIncidente);

            if (suspeitoDAO.atualizar(suspeito)) {
                response.sendRedirect("suspeitos?action=listByQueixa&idQueixa=" + idQueixa);
            } else {
                throw new ServletException("Falha ao atualizar suspeito no banco de dados");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dados numéricos inválidos");
            showEditForm(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            showEditForm(request, response);
        }
    }

    private void deleteSuspeito(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String idParam = request.getParameter("id");
            String idQueixaParam = request.getParameter("idQueixa");
            
            if (idParam == null || idParam.isEmpty() || idQueixaParam == null || idQueixaParam.isEmpty()) {
                throw new ServletException("IDs não fornecidos");
            }
            
            int id = Integer.parseInt(idParam);
            int idQueixa = Integer.parseInt(idQueixaParam);

            if (suspeitoDAO.remover(id)) {
                response.sendRedirect("suspeitos?action=listByQueixa&idQueixa=" + idQueixa);
            } else {
                throw new ServletException("Falha ao remover suspeito");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "IDs inválidos");
            listSuspeitos(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            listSuspeitos(request, response);
        }
    }
}
