/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author JR5
 */


import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.*;
import java.io.File;
import java.io.InputStream;
import dao.UsuarioDAO;
import model.Usuario;

@WebServlet(name = "UsuarioServlet", urlPatterns = {"/usuarios"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 2,
    maxRequestSize = 1024 * 1024 * 10
)
public class UsuarioServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO;
    
    // Configurações
    private static final String[] EXTENSOES_PERMITIDAS = {".jpg", ".jpeg", ".png", ".gif"};
    private static final int MAX_FOTO_SIZE_KB = 2048;
    private static final String UPLOAD_DIR = "uploads/usuarios";
    private static final String PAGINA_LISTAGEM = "usuarios?action=listar";
    private static final String VIEW_FORMULARIO = "/WEB-INF/views/usuarios/formUsuario.jsp";
    private static final String VIEW_LISTAGEM = "/WEB-INF/views/usuarios/listUsuario.jsp";
    private static final String VIEW_VISUALIZAR = "/WEB-INF/views/usuarios/visualizarUsuario.jsp";

    @Override
    public void init() throws ServletException {
        super.init();
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = Optional.ofNullable(request.getParameter("action")).orElse("listar");
            
            switch (action) {
                case "novo":
                    mostrarFormularioNovo(request, response);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response);
                    break;
                case "visualizar":
                    visualizarUsuario(request, response);
                    break;
                case "desativar":
                    alterarStatusUsuario(request, response, false);
                    break;
                case "ativar":
                    alterarStatusUsuario(request, response, true);
                    break;
                case "buscar":
                    buscarUsuarios(request, response);
                    break;
                default:
                    listarUsuarios(request, response);
            }
        } catch (SQLException ex) {
            tratarErro(request, response, "Erro ao processar requisição: " + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = Optional.ofNullable(request.getParameter("action")).orElse("listar");
            
            switch (action) {
                case "inserir":
                    inserirUsuario(request, response);
                    break;
                case "atualizar":
                    atualizarUsuario(request, response);
                    break;
                case "autenticar":
                    autenticarUsuario(request, response);
                    break;
                default:
                    listarUsuarios(request, response);
            }
        } catch (SQLException ex) {
            tratarErro(request, response, "Erro ao processar requisição: " + ex.getMessage());
        }
    }

    // --- Métodos principais ---

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        request.setAttribute("usuarios", usuarioDAO.listarTodosUsuarios());
        encaminharParaView(VIEW_LISTAGEM, request, response);
    }

    private void buscarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String termo = Optional.ofNullable(request.getParameter("termo")).orElse("");
        
        if (termo.trim().isEmpty()) {
            listarUsuarios(request, response);
            return;
        }
        
        List<Usuario> usuarios = usuarioDAO.buscarUsuariosPorTermo(termo.trim());
        request.setAttribute("usuarios", usuarios);
        request.setAttribute("termoBusca", termo);
        encaminharParaView(VIEW_LISTAGEM, request, response);
    }

    private void mostrarFormularioNovo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        carregarDadosFormulario(request);
        encaminharParaView(VIEW_FORMULARIO, request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = extrairId(request);
        Usuario usuario = usuarioDAO.buscarUsuarioPorId(id);
        
        if (usuario == null) {
            redirecionarComMensagem(response, PAGINA_LISTAGEM, "Usuário não encontrado", "erro");
            return;
        }
        
        request.setAttribute("usuario", usuario);
        carregarDadosFormulario(request);
        encaminharParaView(VIEW_FORMULARIO, request, response);
    }

    private void visualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = extrairId(request);
        Usuario usuario = usuarioDAO.buscarUsuarioPorId(id);
        
        if (usuario == null) {
            redirecionarComMensagem(response, PAGINA_LISTAGEM, "Usuário não encontrado", "erro");
            return;
        }
        
        request.setAttribute("usuario", usuario);
        encaminharParaView(VIEW_VISUALIZAR, request, response);
    }

    private void inserirUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        Usuario usuario = criarUsuarioFromRequest(request);
        
        try {
            processarFotoPerfil(request, usuario);
            
            if (usuarioDAO.inserirUsuario(usuario)) {
                redirecionarComMensagem(response, PAGINA_LISTAGEM, "Usuário cadastrado com sucesso", "sucesso");
            } else {
                throw new SQLException("Falha ao inserir usuário");
            }
        } catch (Exception e) {
            prepararErroFormulario(request, response, usuario, e, true);
        }
    }

    private void atualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = extrairId(request);
        Usuario usuario = usuarioDAO.buscarUsuarioPorId(id);
        
        if (usuario == null) {
            redirecionarComMensagem(response, PAGINA_LISTAGEM, "Usuário não encontrado", "erro");
            return;
        }

        try {
            atualizarUsuarioFromRequest(usuario, request);
            processarFotoPerfil(request, usuario);
            
            if (usuarioDAO.atualizarUsuario(usuario)) {
                redirecionarComMensagem(response, 
                    "usuarios?action=visualizar&id=" + usuario.getId_usuario(), 
                    "Usuário atualizado com sucesso", "sucesso");
            } else {
                throw new SQLException("Falha ao atualizar usuário");
            }
        } catch (Exception e) {
            prepararErroFormulario(request, response, usuario, e, false);
        }
    }

    private void autenticarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String identificacao = Optional.ofNullable(request.getParameter("identificacao")).orElse("");
        String senha = Optional.ofNullable(request.getParameter("senha")).orElse("");
        
        if (identificacao.isEmpty() || senha.isEmpty()) {
            request.setAttribute("erro", "Credenciais inválidas");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        Usuario usuario = usuarioDAO.autenticarUsuario(identificacao, senha, request.getRemoteAddr());
        
        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogado", usuario);
            redirecionarPorPerfil(usuario.getPerfil(), response);
        } else {
            request.setAttribute("erro", "Credenciais inválidas ou usuário inativo");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void alterarStatusUsuario(HttpServletRequest request, HttpServletResponse response, 
                                   boolean ativar) throws SQLException, IOException {
        try {
            int id = extrairId(request);
            boolean sucesso = ativar ? 
                usuarioDAO.ativarUsuario(id) : 
                usuarioDAO.desativarUsuario(id);
            
            redirecionarComMensagem(response, PAGINA_LISTAGEM,
                sucesso ? "Status atualizado com sucesso" : "Falha na atualização",
                sucesso ? "sucesso" : "erro");
        } catch (NumberFormatException e) {
            redirecionarComMensagem(response, PAGINA_LISTAGEM, "ID inválido", "erro");
        }
    }

    // --- Métodos de construção de objetos ---

    private Usuario criarUsuarioFromRequest(HttpServletRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNome(obterParametro(request, "nome"));
        usuario.setEmail(obterParametro(request, "email").toLowerCase());
        usuario.setSenha(obterParametro(request, "senha")); // Já deve vir hasheado
        usuario.setCargo(obterEnumCargo(request));
        usuario.setPerfil(obterEnumPerfil(request));
        usuario.setStatus(UsuarioDAO.StatusUsuario.ATIVO.getDescricao());
        usuario.setTelefone(obterParametro(request, "telefone"));
        usuario.setNumero_identificacao(obterParametro(request, "numero_identificacao"));
        usuario.setIp_ultimo_login(request.getRemoteAddr());
        return usuario;
    }

    private void atualizarUsuarioFromRequest(Usuario usuario, HttpServletRequest request) {
        usuario.setNome(obterParametro(request, "nome"));
        usuario.setEmail(obterParametro(request, "email").toLowerCase());
        
        String senha = obterParametro(request, "senha");
        if (!senha.isEmpty()) {
            usuario.setSenha(senha);
        }
        
        usuario.setCargo(obterEnumCargo(request, usuario.getCargo()));
        usuario.setPerfil(obterEnumPerfil(request, usuario.getPerfil()));
        usuario.setTelefone(obterParametro(request, "telefone"));
        usuario.setNumero_identificacao(obterParametro(request, "numero_identificacao"));
        usuario.setIp_ultimo_login(request.getRemoteAddr());
    }

    // --- Métodos auxiliares ---

    private String obterEnumCargo(HttpServletRequest request) {
        return obterEnum(request, "cargo", UsuarioDAO.Cargo.values(), UsuarioDAO.Cargo.COMANDANTE);
    }

    private String obterEnumCargo(HttpServletRequest request, String valorAtual) {
        return obterEnum(request, "cargo", UsuarioDAO.Cargo.values(), valorAtual);
    }

    private String obterEnumPerfil(HttpServletRequest request) {
        return obterEnum(request, "perfil", UsuarioDAO.Perfil.values(), UsuarioDAO.Perfil.OPERACIONAL);
    }

    private String obterEnumPerfil(HttpServletRequest request, String valorAtual) {
        return obterEnum(request, "perfil", UsuarioDAO.Perfil.values(), valorAtual);
    }

    private String obterEnum(HttpServletRequest request, String paramName, 
                           UsuarioDAO.Cargo[] valores, UsuarioDAO.Cargo padrao) {
        String paramValue = request.getParameter(paramName);
        if (paramValue == null || paramValue.isEmpty()) {
            return padrao.getDescricao();
        }
        
        for (UsuarioDAO.Cargo cargo : valores) {
            if (cargo.name().equalsIgnoreCase(paramValue)) {
                return cargo.getDescricao();
            }
        }
        return padrao.getDescricao();
    }

    private String obterEnum(HttpServletRequest request, String paramName, 
                           UsuarioDAO.Perfil[] valores, UsuarioDAO.Perfil padrao) {
        String paramValue = request.getParameter(paramName);
        if (paramValue == null || paramValue.isEmpty()) {
            return padrao.getDescricao();
        }
        
        for (UsuarioDAO.Perfil perfil : valores) {
            if (perfil.name().equalsIgnoreCase(paramValue)) {
                return perfil.getDescricao();
            }
        }
        return padrao.getDescricao();
    }

    private String obterEnum(HttpServletRequest request, String paramName, 
                           UsuarioDAO.Cargo[] valores, String valorAtual) {
        String paramValue = request.getParameter(paramName);
        if (paramValue == null || paramValue.isEmpty()) {
            return valorAtual;
        }
        
        for (UsuarioDAO.Cargo cargo : valores) {
            if (cargo.name().equalsIgnoreCase(paramValue)) {
                return cargo.getDescricao();
            }
        }
        return valorAtual;
    }

    private String obterEnum(HttpServletRequest request, String paramName, 
                           UsuarioDAO.Perfil[] valores, String valorAtual) {
        String paramValue = request.getParameter(paramName);
        if (paramValue == null || paramValue.isEmpty()) {
            return valorAtual;
        }
        
        for (UsuarioDAO.Perfil perfil : valores) {
            if (perfil.name().equalsIgnoreCase(paramValue)) {
                return perfil.getDescricao();
            }
        }
        return valorAtual;
    }

    private String obterParametro(HttpServletRequest request, String paramName) {
        return Optional.ofNullable(request.getParameter(paramName)).orElse("").trim();
    }

    private void carregarDadosFormulario(HttpServletRequest request) {
        request.setAttribute("cargos", UsuarioDAO.Cargo.values());
        request.setAttribute("perfis", UsuarioDAO.Perfil.values());
        request.setAttribute("statusOptions", UsuarioDAO.StatusUsuario.values());
    }

    private void processarFotoPerfil(HttpServletRequest request, Usuario usuario)
            throws IOException, ServletException {
        Part filePart = request.getPart("foto_perfil");
        
        if (filePart != null && filePart.getSize() > 0) {
            if (usuario.getFoto_perfil() != null && !usuario.getFoto_perfil().isEmpty()) {
                removerFotoAntiga(usuario.getFoto_perfil());
            }
            
            String fileName = processarUploadFoto(filePart, usuario.getNome());
            usuario.setFoto_perfil(fileName);
        }
    }

    private String processarUploadFoto(Part filePart, String prefixoNome) throws IOException {
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        
        if (!Arrays.asList(EXTENSOES_PERMITIDAS).contains(fileExtension)) {
            throw new IOException("Extensão de arquivo não permitida");
        }
        
        if (filePart.getSize() > MAX_FOTO_SIZE_KB * 1024L) {
            throw new IOException("Tamanho do arquivo excede o limite permitido");
        }
        
        String uploadDirPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        Path uploadPath = Paths.get(uploadDirPath);
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        String nomeSeguro = prefixoNome.replaceAll("[^a-zA-Z0-9]", "_");
        String novoNome = nomeSeguro + "_" + System.currentTimeMillis() + fileExtension;
        Path filePath = uploadPath.resolve(novoNome);
        
        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        
        return UPLOAD_DIR + "/" + novoNome;
    }

    private void prepararErroFormulario(HttpServletRequest request, HttpServletResponse response,
                                      Usuario usuario, Exception e, boolean novoUsuario) 
            throws ServletException, IOException, SQLException {
        request.setAttribute("erro", "Erro ao " + (novoUsuario ? "cadastrar" : "atualizar") + 
                               ": " + e.getMessage());
        request.setAttribute("usuario", usuario);
        carregarDadosFormulario(request);
        
        if (novoUsuario) {
            mostrarFormularioNovo(request, response);
        } else {
            mostrarFormularioEditar(request, response);
        }
    }

    // --- Métodos utilitários ---

    private int extrairId(HttpServletRequest request) throws NumberFormatException {
        String idParam = obterParametro(request, "id");
        if (idParam.isEmpty()) {
            throw new NumberFormatException("ID não fornecido");
        }
        return Integer.parseInt(idParam);
    }

    private void removerFotoAntiga(String caminhoFoto) throws IOException {
        if (caminhoFoto != null && !caminhoFoto.isEmpty()) {
            Path fullPath = Paths.get(getServletContext().getRealPath(""), caminhoFoto);
            Files.deleteIfExists(fullPath);
        }
    }

    private void redirecionarPorPerfil(String perfil, HttpServletResponse response) throws IOException {
        String pagina = "dashboard.jsp";
        
        if (perfil != null) {
            switch (perfil) {
                case "Super Admin":
                    pagina = "dashboard-admin.jsp";
                    break;
                case "Comando":
                    pagina = "dashboard-comando.jsp";
                    break;
                case "Operacional":
                    pagina = "dashboard-operacional.jsp";
                    break;
            }
        }
        response.sendRedirect(pagina);
    }

    private void encaminharParaView(String view, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(view).forward(request, response);
    }

    private void redirecionarComMensagem(HttpServletResponse response, String url, 
                                       String mensagem, String tipo) throws IOException {
        response.sendRedirect(url + "&" + tipo + "=" + mensagem);
    }

    private void tratarErro(HttpServletRequest request, HttpServletResponse response, String mensagem)
            throws ServletException, IOException {
        request.setAttribute("erro", mensagem);
        encaminharParaView("/WEB-INF/views/erro.jsp", request, response);
    }
}
