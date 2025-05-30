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


import dao.PatrulhaDAO;
import model.Patrulha;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import model.Usuario;

@WebServlet(name = "PatrulhaServlet", urlPatterns = {"/patrulhas"})
public class PatrulhaServlet extends HttpServlet {
    private PatrulhaDAO patrulhaDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        patrulhaDAO = new PatrulhaDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "listar";
        }

        try {
            switch (action) {
                case "listar":
                    listarPatrulhas(request, response);
                    break;
                case "nova":
                    mostrarFormulario(request, response, new Patrulha(), "nova");
                    break;
                case "editar":
                    editarPatrulha(request, response);
                    break;
                case "salvar":
                    salvarPatrulha(request, response);
                    break;
                case "atualizar":
                    atualizarPatrulha(request, response);
                    break;
                case "excluir":
                    excluirPatrulha(request, response);
                    break;
                case "detalhes":
                    mostrarDetalhes(request, response);
                    break;
                case "iniciar":
                    iniciarPatrulha(request, response);
                    break;
                case "finalizar":
                    finalizarPatrulha(request, response);
                    break;
                case "cancelar":
                    cancelarPatrulha(request, response);
                    break;
                case "gerenciar-membros":
                    gerenciarMembros(request, response);
                    break;
                case "adicionar-membro":
                    adicionarMembro(request, response);
                    break;
                case "remover-membro":
                    removerMembro(request, response);
                    break;
                default:
                    listarPatrulhas(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

protected void listarPatrulhas(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
    try {
        List<Patrulha> patrulhas = patrulhaDAO.listarTodas();
        
        // Processar patrulhas com datas passadas
        patrulhas.forEach(p -> {
            if (p.getData().isBefore(LocalDate.now()) && 
                !"Concluída".equals(p.getStatus()) && 
                !"Cancelada".equals(p.getStatus())) {
                
                try {
                    // Atualizar status no banco de dados
                    patrulhaDAO.finalizar(p.getIdPatrulha(), LocalTime.now(), 
                        "Finalizada automaticamente - data passada");
                    p.setStatus("Concluída");
                } catch (SQLException e) {
                    // Logar erro sem interromper o fluxo
                    System.err.println("Erro ao atualizar patrulha: " + e.getMessage());
                }
            }
        });
        
        request.setAttribute("patrulhas", patrulhas);
        request.getRequestDispatcher("/WEB-INF/views/patrulhas/listar.jsp").forward(request, response);
    } catch (Exception e) {
        request.setAttribute("erro", "Erro ao listar patrulhas: " + e.getMessage());
        request.getRequestDispatcher("/erro.jsp").forward(request, response);
    }
}

    private void mostrarDetalhes(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        Patrulha patrulha = patrulhaDAO.buscarPorId(id);
        
        if (patrulha != null) {
            // Buscar informações completas dos usuários envolvidos
            Set<Integer> idsUsuarios = new HashSet<>();
            idsUsuarios.add(patrulha.getResponsavelId());
            idsUsuarios.addAll(patrulha.getMembrosIds());
            
            Map<Integer, Usuario> usuariosPatrulha = patrulhaDAO.buscarUsuariosCompletos(idsUsuarios);
            
            request.setAttribute("patrulha", patrulha);
            request.setAttribute("usuariosPatrulha", usuariosPatrulha);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/patrulhas/detalhes.jsp");
            dispatcher.forward(request, response);
        } else {
            request.getSession().setAttribute("erro", "Patrulha não encontrada");
            response.sendRedirect("patrulhas");
        }
    }

private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, 
        Patrulha patrulha, String tipoForm) throws ServletException, IOException, SQLException {
    List<Usuario> usuariosDisponiveis = patrulhaDAO.listarUsuariosParaPatrulha();
    
    request.setAttribute("patrulha", patrulha);
    request.setAttribute("tipoForm", tipoForm);
    request.setAttribute("todosUsuarios", usuariosDisponiveis);
    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/patrulhas/formulario.jsp");
    dispatcher.forward(request, response);
}




private void editarPatrulha(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException, SQLException {
    int id = Integer.parseInt(request.getParameter("id"));
    Patrulha patrulha = patrulhaDAO.buscarPorId(id);
    mostrarFormulario(request, response, patrulha, "editar");
}

    private void salvarPatrulha(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        Patrulha patrulha = extrairPatrulhaRequest(request);
        int id = patrulhaDAO.inserir(patrulha);
        
        if (id > 0) {
            request.getSession().setAttribute("mensagem", "Patrulha cadastrada com sucesso!");
            response.sendRedirect("patrulhas?action=detalhes&id=" + id);
        } else {
            request.setAttribute("erro", "Falha ao cadastrar patrulha");
            mostrarFormulario(request, response, patrulha, "nova");
        }
    }

    private void atualizarPatrulha(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        Patrulha patrulha = extrairPatrulhaRequest(request);
        patrulha.setIdPatrulha(id);
        
        if (patrulhaDAO.atualizar(patrulha)) {
            request.getSession().setAttribute("mensagem", "Patrulha atualizada com sucesso!");
            response.sendRedirect("patrulhas?action=detalhes&id=" + id);
        } else {
            request.setAttribute("erro", "Falha ao atualizar patrulha");
            mostrarFormulario(request, response, patrulha, "editar");
        }
    }

    private void excluirPatrulha(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        if (patrulhaDAO.deletar(id)) {
            request.getSession().setAttribute("mensagem", "Patrulha excluída com sucesso!");
        } else {
            request.getSession().setAttribute("erro", "Falha ao excluir patrulha");
        }
        
        response.sendRedirect("patrulhas");
    }



    private void iniciarPatrulha(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        if (patrulhaDAO.iniciar(id)) {
            request.getSession().setAttribute("mensagem", "Patrulha iniciada com sucesso!");
        } else {
            request.getSession().setAttribute("erro", "Falha ao iniciar patrulha. Verifique se o status atual é 'Planejada'.");
        }
        
        response.sendRedirect("patrulhas?action=detalhes&id=" + id);
    }

    private void finalizarPatrulha(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        LocalTime horaFim = LocalTime.parse(request.getParameter("horaFim"));
        String relatorio = request.getParameter("relatorio");
        
        if (patrulhaDAO.finalizar(id, horaFim, relatorio)) {
            request.getSession().setAttribute("mensagem", "Patrulha finalizada com sucesso!");
        } else {
            request.getSession().setAttribute("erro", "Falha ao finalizar patrulha. Verifique se o status atual é 'Em Andamento'.");
        }
        
        response.sendRedirect("patrulhas?action=detalhes&id=" + id);
    }

    private void cancelarPatrulha(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        String motivo = request.getParameter("motivo");
        
        if (patrulhaDAO.cancelar(id, motivo)) {
            request.getSession().setAttribute("mensagem", "Patrulha cancelada com sucesso!");
        } else {
            request.getSession().setAttribute("erro", "Falha ao cancelar patrulha. Verifique se o status atual é 'Planejada' ou 'Em Andamento'.");
        }
        
        response.sendRedirect("patrulhas?action=detalhes&id=" + id);
    }

    private void gerenciarMembros(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        Patrulha patrulha = patrulhaDAO.buscarPorId(id);
        
        if (patrulha != null) {
            // Buscar informações completas dos membros atuais
            Set<Integer> idsUsuarios = new HashSet<>();
            idsUsuarios.add(patrulha.getResponsavelId());
            idsUsuarios.addAll(patrulha.getMembrosIds());
            
            Map<Integer, Usuario> usuariosPatrulha = patrulhaDAO.buscarUsuariosCompletos(idsUsuarios);
            
            // Lista de usuários disponíveis para adição (excluindo os já membros)
            List<Usuario> usuariosDisponiveis = patrulhaDAO.listarUsuariosParaPatrulha().stream()
                .filter(u -> !idsUsuarios.contains(u.getId_usuario()))
                .collect(Collectors.toList());
            
            request.setAttribute("patrulha", patrulha);
            request.setAttribute("usuariosPatrulha", usuariosPatrulha);
            request.setAttribute("usuariosDisponiveis", usuariosDisponiveis);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/patrulhas/membros.jsp");
            dispatcher.forward(request, response);
        } else {
            request.getSession().setAttribute("erro", "Patrulha não encontrada");
            response.sendRedirect("patrulhas");
        }
    }

    private void adicionarMembro(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        int idPatrulha = Integer.parseInt(request.getParameter("idPatrulha"));
        int idMembro = Integer.parseInt(request.getParameter("idMembro"));
        
        Patrulha patrulha = patrulhaDAO.buscarPorId(idPatrulha);
        if (patrulha != null) {
            patrulha.adicionarMembro(idMembro);
            patrulhaDAO.atualizar(patrulha);
            request.getSession().setAttribute("mensagem", "Membro adicionado com sucesso!");
        } else {
            request.getSession().setAttribute("erro", "Patrulha não encontrada");
        }
        
        response.sendRedirect("patrulhas?action=gerenciar-membros&id=" + idPatrulha);
    }

    private void removerMembro(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        int idPatrulha = Integer.parseInt(request.getParameter("idPatrulha"));
        int idMembro = Integer.parseInt(request.getParameter("idMembro"));
        
        Patrulha patrulha = patrulhaDAO.buscarPorId(idPatrulha);
        if (patrulha != null) {
            try {
                patrulha.removerMembro(idMembro);
                patrulhaDAO.atualizar(patrulha);
                request.getSession().setAttribute("mensagem", "Membro removido com sucesso!");
            } catch (IllegalStateException e) {
                request.getSession().setAttribute("erro", e.getMessage());
            }
        } else {
            request.getSession().setAttribute("erro", "Patrulha não encontrada");
        }
        
        response.sendRedirect("patrulhas?action=gerenciar-membros&id=" + idPatrulha);
    }

  private Patrulha extrairPatrulhaRequest(HttpServletRequest request) {
    Patrulha patrulha = new Patrulha();
    
    patrulha.setNome(request.getParameter("nome"));
    patrulha.setResponsavelId(Integer.parseInt(request.getParameter("responsavelId")));
    patrulha.setData(LocalDate.parse(request.getParameter("data")));
    patrulha.setHoraInicio(LocalTime.parse(request.getParameter("horaInicio")));
    
    if (request.getParameter("horaFim") != null && !request.getParameter("horaFim").isEmpty()) {
        patrulha.setHoraFim(LocalTime.parse(request.getParameter("horaFim")));
    }
    
    patrulha.setTipo(request.getParameter("tipo"));
    patrulha.setZonaAtuacao(request.getParameter("zonaAtuacao")); 
    patrulha.setObservacoes(request.getParameter("observacoes"));
   patrulha.setStatus(request.getParameter("status"));
    
    // Processar membros selecionados
    String[] membrosSelecionados = request.getParameterValues("membros");
    if (membrosSelecionados != null) {
        for (String idMembro : membrosSelecionados) {
            patrulha.adicionarMembro(Integer.parseInt(idMembro));
        }
    }
    
    return patrulha;
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

    @Override
    public String getServletInfo() {
        return "Servlet para gerenciamento de patrulhas policiais";
    }
}
