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



import dao.UsuarioDAO;
import model.Usuario;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.List;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.net.URLEncoder;

@WebServlet(name = "UsuarioServlet", urlPatterns = {"/usuarios"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1MB
    maxFileSize = 1024 * 1024 * 2,      // 2MB
    maxRequestSize = 1024 * 1024 * 10   // 10MB
)
public class UsuarioServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "listar";
        }

        try {
            switch (action) {
                case "novo":
                    mostrarFormulario(request, response, false);
                    break;
                case "editar":
                    editarUsuario(request, response);
                    break;
                case "visualizar":
                    visualizarUsuario(request, response);
                    break;
                case "ocultar":
                    alterarStatusUsuario(request, response, false);
                    break;
                case "restaurar":
                    alterarStatusUsuario(request, response, true);
                    break;
                case "listarOcultos":
                    listarUsuariosOcultos(request, response);
                    break;
                case "buscar":
                    buscarUsuario(request, response);
                    break;
                default:
                    listarUsuarios(request, response);
            }
        } catch (SQLException ex) {
            handleError(request, response, "Erro ao processar requisição", ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("salvar".equals(action)) {
                salvarUsuario(request, response);
            } else if ("atualizar".equals(action)) {
                atualizarUsuario(request, response);
            } else {
                listarUsuarios(request, response);
            }
        } catch (SQLException ex) {
            handleError(request, response, "Erro ao processar requisição", ex);
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        int pagina = getIntParameter(request, "pagina", 1);
        int itensPorPagina = getIntParameter(request, "itensPorPagina", 10);
        
        List<Usuario> listaUsuarios = usuarioDAO.listarUsuariosVisiveis(pagina, itensPorPagina);
        int totalUsuarios = usuarioDAO.contarUsuariosVisiveis();
        int totalPaginas = (int) Math.ceil((double) totalUsuarios / itensPorPagina);
        
        request.setAttribute("listaUsuarios", listaUsuarios);
        request.setAttribute("totalPaginas", totalPaginas);
        request.setAttribute("paginaAtual", pagina);
        request.setAttribute("itensPorPagina", itensPorPagina);
        
        forwardToJsp(request, response, "/view/usuarios/listar.jsp");
    }

    private void listarUsuariosOcultos(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Usuario> usuariosOcultos = usuarioDAO.listarUsuariosOcultos();
        request.setAttribute("usuariosOcultos", usuariosOcultos);
        forwardToJsp(request, response, "/view/usuarios/listarOcultos.jsp");
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, boolean isEdicao)
            throws SQLException, ServletException, IOException {
        request.setAttribute("cargosDisponiveis", Arrays.asList(UsuarioDAO.CARGOS_DISPONIVEIS));
        request.setAttribute("perfisDisponiveis", Arrays.asList(UsuarioDAO.PERFIS_DISPONIVEIS));
        forwardToJsp(request, response, "/view/usuarios/formulario.jsp");
    }

    private void editarUsuario(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        int id = getIntParameter(request, "id", 0);
        Usuario usuario = usuarioDAO.buscarUsuarioPorId(id);
        
        if (usuario == null) {
            redirectWithError(response, "usuarios", "Usuário não encontrado");
            return;
        }
        
        request.setAttribute("usuario", usuario);
        request.setAttribute("cargosDisponiveis", Arrays.asList(UsuarioDAO.CARGOS_DISPONIVEIS));
        request.setAttribute("perfisDisponiveis", Arrays.asList(UsuarioDAO.PERFIS_DISPONIVEIS));
        forwardToJsp(request, response, "/view/usuarios/formulario.jsp");
    }

    private void visualizarUsuario(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        int id = getIntParameter(request, "id", 0);
        Usuario usuario = usuarioDAO.buscarUsuarioPorId(id);
        
        if (usuario == null) {
            redirectWithError(response, "usuarios", "Usuário não encontrado");
            return;
        }
        
        request.setAttribute("usuario", usuario);
        forwardToJsp(request, response, "/view/usuarios/visualizar.jsp");
    }

    private void buscarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String documento = request.getParameter("documento");
        Usuario usuario = usuarioDAO.buscarUsuarioPorDocumento(documento);
        request.setAttribute("usuario", usuario);
        forwardToJsp(request, response, "/view/usuarios/detalhesUsuario.jsp");
    }

    private void salvarUsuario(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
        Usuario usuario = criarUsuarioFromRequest(request);
        
        try {
            processarUploadFoto(request, usuario);
            
            usuarioDAO.iniciarTransacao();
            boolean sucesso = usuarioDAO.inserirUsuario(usuario);
            usuarioDAO.commitarTransacao();
            
            if (sucesso) {
                redirectWithSuccess(response, "usuarios", "Usuário cadastrado com sucesso");
            } else {
                throw new SQLException("Falha ao inserir usuário");
            }
        } catch (SQLException | IOException e) {
            usuarioDAO.rollbackTransacao();
            request.setAttribute("erro", "Erro ao cadastrar: " + e.getMessage());
            request.setAttribute("usuario", usuario);
            mostrarFormulario(request, response, false);
        }
    }

    private void atualizarUsuario(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
        int id = getIntParameter(request, "id", 0);
        Usuario usuario = usuarioDAO.buscarUsuarioPorId(id);
        
        if (usuario == null) {
            redirectWithError(response, "usuarios", "Usuário não encontrado");
            return;
        }
        
        atualizarUsuarioFromRequest(usuario, request);
        
        try {
            processarUploadFoto(request, usuario);
            
            usuarioDAO.iniciarTransacao();
            boolean sucesso = usuarioDAO.atualizarUsuario(usuario);
            usuarioDAO.commitarTransacao();
            
            if (sucesso) {
                redirectWithSuccess(response, "usuarios", "Usuário atualizado com sucesso");
            } else {
                throw new SQLException("Falha ao atualizar usuário");
            }
        } catch (SQLException | IOException e) {
            usuarioDAO.rollbackTransacao();
            request.setAttribute("erro", "Erro ao atualizar: " + e.getMessage());
            request.setAttribute("usuario", usuario);
            editarUsuario(request, response);
        }
    }

    private void alterarStatusUsuario(HttpServletRequest request, HttpServletResponse response, boolean restaurar) 
            throws SQLException, IOException {
        int id = getIntParameter(request, "id", 0);
        
        try {
            usuarioDAO.iniciarTransacao();
            boolean sucesso = restaurar ? usuarioDAO.restaurarUsuario(id) : usuarioDAO.ocultarUsuario(id);
            usuarioDAO.commitarTransacao();
            
            if (sucesso) {
                String msg = restaurar ? "Usuário restaurado com sucesso" : "Usuário ocultado com sucesso";
                redirectWithSuccess(response, "usuarios", msg);
            } else {
                throw new SQLException("Falha ao alterar status do usuário");
            }
        } catch (SQLException e) {
            usuarioDAO.rollbackTransacao();
            redirectWithError(response, "usuarios", "Falha ao alterar status do usuário");
        }
    }

    private Usuario criarUsuarioFromRequest(HttpServletRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNome(request.getParameter("nome"));
        usuario.setEmail(request.getParameter("email"));
        usuario.setSenha(request.getParameter("senha"));
        usuario.setCargo(request.getParameter("cargo"));
        usuario.setTelefone(request.getParameter("telefone"));
        usuario.setStatus("Ativo");
        usuario.setPerfil(request.getParameter("perfil"));
        usuario.setNumero_identificacao(request.getParameter("numero_identificacao"));
        return usuario;
    }

    private void atualizarUsuarioFromRequest(Usuario usuario, HttpServletRequest request) {
        usuario.setNome(request.getParameter("nome"));
        usuario.setEmail(request.getParameter("email"));
        usuario.setSenha(request.getParameter("senha"));
        usuario.setCargo(request.getParameter("cargo"));
        usuario.setTelefone(request.getParameter("telefone"));
        usuario.setPerfil(request.getParameter("perfil"));
        usuario.setNumero_identificacao(request.getParameter("numero_identificacao"));
    }

    private void processarUploadFoto(HttpServletRequest request, Usuario usuario) 
            throws IOException, ServletException {
        Part filePart = request.getPart("foto_perfil");
        if (filePart != null && filePart.getSize() > 0) {
            if (usuario.getFoto_perfil() != null && !usuario.getFoto_perfil().isEmpty()) {
                usuarioDAO.removerFotoAntiga(usuario.getFoto_perfil(), request.getServletContext());
            }
            
            String fileName = uploadFoto(filePart, usuario.getNome(), request.getServletContext());
            usuario.setFoto_perfil(fileName);
        }
    }

    private String uploadFoto(Part filePart, String nomeUsuario, ServletContext context) 
            throws IOException {
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        
        if (!Arrays.asList(UsuarioDAO.EXTENSOES_PERMITIDAS).contains(fileExtension)) {
            throw new IOException("Extensão não permitida. Use: " + Arrays.toString(UsuarioDAO.EXTENSOES_PERMITIDAS));
        }
        
        if (filePart.getSize() > UsuarioDAO.MAX_FOTO_SIZE_KB * 1024) {
            throw new IOException("Tamanho máximo excedido (" + UsuarioDAO.MAX_FOTO_SIZE_KB + "KB)");
        }
        
        String uploadPath = context.getRealPath("") + UsuarioDAO.UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        
        String novoNome = "user_" + System.currentTimeMillis() + fileExtension;
        String filePath = uploadPath + File.separator + novoNome;
        
        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        }
        
        return UsuarioDAO.UPLOAD_DIR + "/" + novoNome;
    }

    // Métodos auxiliares
    private int getIntParameter(HttpServletRequest request, String paramName, int defaultValue) {
        try {
            return Integer.parseInt(request.getParameter(paramName));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void forwardToJsp(HttpServletRequest request, HttpServletResponse response, String jspPath)
            throws ServletException, IOException {
        request.getRequestDispatcher(jspPath).forward(request, response);
    }

    private void redirectWithError(HttpServletResponse response, String path, String errorMsg)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/" + path + "?erro=" + URLEncoder.encode(errorMsg, "UTF-8"));
    }

    private void redirectWithSuccess(HttpServletResponse response, String path, String successMsg)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/" + path + "?sucesso=" + URLEncoder.encode(successMsg, "UTF-8"));
    }ttpServletResponse response, 
                           String message, Exception ex) throws ServletException, IOException {
        request.setAttribute("erro", message + ": " + ex.getMessage());
        forwardToJsp(request, response, "/view/error.jsp");
    }
}