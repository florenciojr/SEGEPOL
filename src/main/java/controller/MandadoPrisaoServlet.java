package controller;

import dao.MandadoPrisaoDAO;
import dao.SuspeitoDAO;
import model.MandadoPrisao;
import model.Suspeito;
import util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "MandadoPrisaoServlet", urlPatterns = {"/mandados"})
public class MandadoPrisaoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action") != null ? 
            request.getParameter("action") : "list";
        
        try {
            switch (action.toLowerCase()) {
                case "list":
                    listarMandados(request, response);
                    break;
                case "new":
                    mostrarFormularioNovo(request, response);
                    break;
                case "create":
                    criarMandado(request, response);
                    break;
                case "edit":
                    mostrarFormularioEdicao(request, response);
                    break;
                case "update":
                    atualizarMandado(request, response);
                    break;
                case "delete":
                    deletarMandado(request, response);
                    break;
                case "view":
                    visualizarMandado(request, response);
                    break;
                case "search":
                    buscarMandados(request, response);
                    break;
                case "listbystatus":
                    listarPorStatus(request, response);
                    break;
                default:
                    listarMandados(request, response);
            }
        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    private void listarMandados(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MandadoPrisaoDAO dao = new MandadoPrisaoDAO();
        
        int pagina = 1;
        int registrosPorPagina = 10;
        
        try {
            pagina = Integer.parseInt(request.getParameter("pagina"));
        } catch (NumberFormatException e) {
            // Mantém o valor padrão
        }
        
        List<MandadoPrisao> mandados = dao.listarTodos(pagina, registrosPorPagina);
        int totalRegistros = dao.contarTotalRegistros();
        int totalPaginas = (int) Math.ceil((double) totalRegistros / registrosPorPagina);
        
        request.setAttribute("mandados", mandados);
        request.setAttribute("paginaAtual", pagina);
        request.setAttribute("totalPaginas", totalPaginas);
        request.setAttribute("registrosPorPagina", registrosPorPagina);
        
        request.getRequestDispatcher("/WEB-INF/views/mandados/listar.jsp").forward(request, response);
    }

private void mostrarFormularioNovo(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    MandadoPrisao mandado = new MandadoPrisao();
    // Definir valores padrão para novo mandado
    mandado.setDataEmissao(new java.sql.Date(System.currentTimeMillis())); // Data atual como padrão
    mandado.setStatus("Ativo"); // Status padrão
    mandado.setTipo("Prisão"); // Tipo padrão
    
    request.setAttribute("acao", "create");
    request.setAttribute("mandado", mandado);
    // Carregar listas necessárias (suspeitos, tipos, status)
    carregarListasParaFormulario(request);
    
    request.getRequestDispatcher("/WEB-INF/views/mandados/formulario.jsp").forward(request, response);
}

private void mostrarFormularioEdicao(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        int id = Integer.parseInt(request.getParameter("id"));
        MandadoPrisaoDAO dao = new MandadoPrisaoDAO(); // Adicione esta linha
        MandadoPrisao mandado = dao.buscarPorId(id);
        
        if (mandado == null) {
            throw new ServletException("Mandado não encontrado com ID: " + id);
        }
        
        request.setAttribute("acao", "update");
        request.setAttribute("mandado", mandado);
        carregarListasParaFormulario(request);
        
        request.getRequestDispatcher("/WEB-INF/views/mandados/formulario.jsp").forward(request, response);
    } catch (NumberFormatException e) {
        throw new ServletException("ID inválido: " + request.getParameter("id"));
    }
}

private void carregarListasParaFormulario(HttpServletRequest request) {
    // Carrega listas necessárias para o formulário
    SuspeitoDAO suspeitoDAO = new SuspeitoDAO();
    request.setAttribute("suspeitos", suspeitoDAO.listarTodos());
    request.setAttribute("tiposMandado", Arrays.asList("Prisão", "Busca e Apreensão", "Comparência"));
    request.setAttribute("statusMandado", Arrays.asList("Ativo", "Cancelado", "Cumprido", "Expirado"));
}

    private void visualizarMandado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        MandadoPrisaoDAO dao = new MandadoPrisaoDAO();
        MandadoPrisao mandado = dao.buscarPorId(id);
        
        if (mandado == null) {
            throw new ServletException("Mandado não encontrado com ID: " + id);
        }
        
        request.setAttribute("mandado", mandado);
        request.getRequestDispatcher("/WEB-INF/views/mandados/visualizar.jsp").forward(request, response);
    }

    private void criarMandado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        HttpSession session = request.getSession();
        MandadoPrisao mandado = extrairMandadoDaRequisicao(request);
        
        // Definir usuário emissor (da sessão)
        Integer idUsuario = (Integer) session.getAttribute("usuarioId");
        mandado.setIdUsuarioEmissor(idUsuario != null ? idUsuario : 1); // Default para dev
        
        MandadoPrisaoDAO dao = new MandadoPrisaoDAO();
        
        if (dao.existeNumeroMandado(mandado.getNumeroMandado())) {
            throw new ServletException("Já existe um mandado com este número");
        }
        
        if (dao.inserir(mandado)) {
            session.setAttribute("successMessage", "Mandado criado com sucesso!");
            response.sendRedirect(request.getContextPath() + "/mandados?action=list");
        } else {
            throw new ServletException("Falha ao criar mandado");
        }
    }

    private void atualizarMandado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        int id = Integer.parseInt(request.getParameter("id"));
        MandadoPrisaoDAO dao = new MandadoPrisaoDAO();
        MandadoPrisao mandadoExistente = dao.buscarPorId(id);
        
        if (mandadoExistente == null) {
            throw new ServletException("Mandado não encontrado com ID: " + id);
        }
        
        MandadoPrisao mandadoAtualizado = extrairMandadoDaRequisicao(request);
        mandadoAtualizado.setIdMandado(id);
        mandadoAtualizado.setVersao(mandadoExistente.getVersao());
        mandadoAtualizado.setIdUsuarioEmissor(mandadoExistente.getIdUsuarioEmissor());
        
        if (dao.atualizar(mandadoAtualizado)) {
            request.getSession().setAttribute("successMessage", "Mandado atualizado com sucesso!");
            response.sendRedirect(request.getContextPath() + "/mandados?action=list");
        } else {
            throw new ServletException("Falha ao atualizar mandado. Verifique se o mandado foi modificado por outro usuário.");
        }
    }

    private void deletarMandado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        MandadoPrisaoDAO dao = new MandadoPrisaoDAO();
        
        if (dao.remover(id)) {
            request.getSession().setAttribute("successMessage", "Mandado removido com sucesso!");
        } else {
            request.getSession().setAttribute("error", "Falha ao remover mandado");
        }
        
        response.sendRedirect(request.getContextPath() + "/mandados?action=list");
    }

    private void listarPorStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String status = request.getParameter("status");
        MandadoPrisaoDAO dao = new MandadoPrisaoDAO();
        List<MandadoPrisao> mandados = dao.listarPorStatus(status);
        
        request.setAttribute("mandados", mandados);
        request.setAttribute("statusFiltro", status);
        request.getRequestDispatcher("/WEB-INF/views/mandados/listarPorStatus.jsp").forward(request, response);
    }

    private void buscarMandados(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String termo = request.getParameter("termo");
        MandadoPrisaoDAO dao = new MandadoPrisaoDAO();
        List<MandadoPrisao> mandados = dao.buscarPorTermo(termo);
        
        request.setAttribute("mandados", mandados);
        request.setAttribute("termoBusca", termo);
        request.getRequestDispatcher("/WEB-INF/views/mandados/resultadoBusca.jsp").forward(request, response);
    }

    private MandadoPrisao extrairMandadoDaRequisicao(HttpServletRequest request) throws ParseException {
        MandadoPrisao mandado = new MandadoPrisao();
        
        // Dados básicos
        mandado.setIdSuspeito(Integer.parseInt(request.getParameter("idSuspeito")));
        mandado.setNumeroMandado(request.getParameter("numeroMandado"));
        
        // Datas
        String dataEmissaoStr = request.getParameter("dataEmissao");
        mandado.setDataEmissao(DateUtil.stringParaDate(dataEmissaoStr));
        
        String dataValidadeStr = request.getParameter("dataValidade");
        if (dataValidadeStr != null && !dataValidadeStr.isEmpty()) {
            mandado.setDataValidade(DateUtil.stringParaDate(dataValidadeStr));
        }
        
        mandado.setTipo(request.getParameter("tipo"));
        mandado.setStatus(request.getParameter("status"));
        mandado.setDescricao(request.getParameter("descricao"));
        
        return mandado;
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
