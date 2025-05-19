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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet({"/cidadao", "/cidadaos"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 10,      // 10 MB
    maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class CidadaoServlet extends HttpServlet {
    private CidadaoDAO cidadaoDAO;
    private static final String UPLOAD_DIR = "uploads";

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
            
            // Obter classificações disponíveis do banco de dados
List<String> classificacoes = cidadaoDAO.getClassificacoesDisponiveis();
request.setAttribute("classificacoes", classificacoes);
            switch (action) {
                case "buscarPorNome":
                    buscarCidadaosPorNome(request, response);
                    break;
                case "buscarPorCidade":
                    buscarCidadaosPorCidade(request, response);
                    break;
                case "verificarDocumento":
                    verificarDocumentoExistente(request, response);
                    break;
                case "listarPaginado":
                    listarCidadaosPaginados(request, response);
                    break;
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
                case "visualizarImagem":
                    visualizarImagem(request, response);
                    break;
                default:
                    listarCidadaos(request, response);
                    break;
                                        case "view":
    visualizarCidadao(request, response);
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
        throws ServletException, IOException, SQLException {
    // Obter classificações disponíveis do banco de dados
    List<String> classificacoes = cidadaoDAO.getClassificacoesDisponiveis();
    request.setAttribute("classificacoes", classificacoes);
    
    // Obter lista de cidadãos
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
        
        // Processar upload da imagem
        Part filePart = request.getPart("imagem");
        if (filePart != null && filePart.getSize() > 0) {
            String caminhoImagem = processarUploadImagem(filePart, request);
            cidadao.setCaminhoImagem(caminhoImagem);
        }
        
        cidadaoDAO.inserirCidadao(cidadao);
        response.sendRedirect(request.getContextPath() + "/cidadao");
    }

private void atualizarCidadao(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    Cidadao cidadao = criarCidadaoAPartirRequest(request);
    cidadao.setIdCidadao(Integer.parseInt(request.getParameter("id")));
    
    // Processar upload da nova imagem (se fornecida)
    Part filePart = request.getPart("imagem");
    if (filePart != null && filePart.getSize() > 0) {
        String caminhoImagem = processarUploadImagem(filePart, request);
        cidadao.setCaminhoImagem(caminhoImagem);
    } else {
        // Manter a imagem existente se nenhuma nova for enviada
        Cidadao cidadaoExistente = cidadaoDAO.buscarCidadaoPorId(cidadao.getIdCidadao());
        cidadao.setCaminhoImagem(cidadaoExistente.getCaminhoImagem());
    }
    
    cidadaoDAO.atualizarCidadao(cidadao);
    response.sendRedirect(request.getContextPath() + "/cidadao");
}
    private void deletarCidadao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        // Opcional: deletar a imagem associada ao cidadão
        Cidadao cidadao = cidadaoDAO.buscarCidadaoPorId(id);
        if (cidadao.getCaminhoImagem() != null && !cidadao.getCaminhoImagem().isEmpty()) {
            try {
                Path imagemPath = Paths.get(getServletContext().getRealPath(""), cidadao.getCaminhoImagem());
                Files.deleteIfExists(imagemPath);
            } catch (IOException e) {
                System.err.println("Erro ao deletar imagem: " + e.getMessage());
            }
        }
        
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
    
    private void visualizarImagem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Cidadao cidadao = cidadaoDAO.buscarCidadaoPorId(id);
        
        if (cidadao != null && cidadao.getCaminhoImagem() != null && !cidadao.getCaminhoImagem().isEmpty()) {
            Path imagemPath = Paths.get(getServletContext().getRealPath(""), cidadao.getCaminhoImagem());
            
            if (Files.exists(imagemPath)) {
                response.setContentType(getServletContext().getMimeType(imagemPath.toString()));
                Files.copy(imagemPath, response.getOutputStream());
                return;
            }
        }
        
        // Se não houver imagem, retornar uma imagem padrão ou código 404
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

private Cidadao criarCidadaoAPartirRequest(HttpServletRequest request) {
    Cidadao cidadao = new Cidadao();
    cidadao.setNome(request.getParameter("nome"));
    cidadao.setGenero(request.getParameter("genero"));
    cidadao.setDataNascimento(LocalDate.parse(request.getParameter("dataNascimento")));
    cidadao.setDocumentoIdentificacao(request.getParameter("documentoIdentificacao"));
    cidadao.setTipoDocumento(request.getParameter("tipoDocumento"));
    cidadao.setTelefone(request.getParameter("telefone"));
    cidadao.setEmail(request.getParameter("email"));
    cidadao.setNaturalidade(request.getParameter("naturalidade"));
    cidadao.setRua(request.getParameter("rua"));
    cidadao.setBairro(request.getParameter("bairro"));
    cidadao.setCidade(request.getParameter("cidade"));
    cidadao.setProvincia(request.getParameter("provincia"));
    cidadao.setClassificacao(request.getParameter("classificacao"));
    cidadao.setCaracteristicasFisicas(request.getParameter("caracteristicasFisicas"));
    
    return cidadao;
}
    
    private String processarUploadImagem(Part filePart, HttpServletRequest request) throws IOException {
        // Obter o diretório de uploads absoluto
        String uploadPath = getServletContext().getRealPath("") + UPLOAD_DIR;
        Files.createDirectories(Paths.get(uploadPath));
        
        // Gerar um nome único para o arquivo
        String fileName = System.currentTimeMillis() + "_" + Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        
        // Salvar o arquivo
        try (InputStream fileContent = filePart.getInputStream()) {
            Path filePath = Paths.get(uploadPath, fileName);
            Files.copy(fileContent, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        
        // Retornar o caminho relativo para armazenar no banco de dados
        return UPLOAD_DIR + "/" + fileName;
    }
    
    private void buscarCidadaosPorNome(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nome = request.getParameter("nomes");
        List<Cidadao> cidadaos = cidadaoDAO.buscarCidadaosPorNome(nome);
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("termoBusca", nome);
        request.getRequestDispatcher("/WEB-INF/views/cidadaos/listCidadaos.jsp").forward(request, response);
    }

    private void buscarCidadaosPorCidade(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cidade = request.getParameter("cidade");
        List<Cidadao> cidadaos = cidadaoDAO.buscarCidadaosPorCidade(cidade);
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("cidadeBusca", cidade);
        request.getRequestDispatcher("/WEB-INF/views/cidadaos/listCidadaos.jsp").forward(request, response);
    }

    private void verificarDocumentoExistente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String documento = request.getParameter("documento");
        boolean existe = cidadaoDAO.verificarDocumentoExistente(documento);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"existe\": " + existe + "}");
    }

    private void listarCidadaosPaginados(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int pagina = Integer.parseInt(request.getParameter("pagina"));
        int itensPorPagina = Integer.parseInt(request.getParameter("itensPorPagina"));
        int offset = (pagina - 1) * itensPorPagina;
        
        List<Cidadao> cidadaos = cidadaoDAO.listarCidadaosComPaginacao(offset, itensPorPagina);
        int totalCidadaos = cidadaoDAO.contarTotalCidadaos();
        int totalPaginas = (int) Math.ceil((double) totalCidadaos / itensPorPagina);
        
        request.setAttribute("cidadaos", cidadaos);
        request.setAttribute("paginaAtual", pagina);
        request.setAttribute("totalPaginas", totalPaginas);
        request.setAttribute("itensPorPagina", itensPorPagina);
        request.getRequestDispatcher("/WEB-INF/views/cidadaos/listCidadaos.jsp").forward(request, response);
    }
    
    
private void visualizarCidadao(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        int id = Integer.parseInt(request.getParameter("id"));
        Cidadao cidadao = cidadaoDAO.buscarCidadaoPorId(id);
        
        if (cidadao == null) {
            request.setAttribute("error", "Cidadão não encontrado com ID: " + id);
            request.getRequestDispatcher("/WEB-INF/views/cidadaos/detalhesCidadao.jsp").forward(request, response);
            return;
        }
        
        request.setAttribute("cidadao", cidadao);
        request.getRequestDispatcher("/WEB-INF/views/cidadaos/detalhesCidadao.jsp").forward(request, response);
        
    } catch (NumberFormatException e) {
        request.setAttribute("error", "ID inválido");
        request.getRequestDispatcher("/WEB-INF/views/cidadaos/detalhesCidadao.jsp").forward(request, response);
    }
}}
