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
import java.io.File;
import model.Usuario;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if (action == null) {
                listarUsuarios(request, response);
            } else {
                switch (action) {
                    case "novo":
                        mostrarFormularioNovoUsuario(request, response);
                        break;
                    case "editar":
                        mostrarFormularioEditarUsuario(request, response);
                        break;
                    case "visualizar":
                        visualizarUsuario(request, response);
                        break;
                    case "desativar":
                        desativarUsuario(request, response);
                        break;
                    case "reativar":
                        reativarUsuario(request, response);
                        break;
                    default:
                        listarUsuarios(request, response);
                }
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if (action != null) {
                switch (action) {
                    case "inserir":
                        inserirUsuario(request, response);
                        break;
                    case "atualizar":
                        atualizarUsuario(request, response);
                        break;
                    default:
                        listarUsuarios(request, response);
                }
            } else {
                listarUsuarios(request, response);
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        // Paginação
        int pagina = 1;
        int itensPorPagina = 10;
        
        if (request.getParameter("pagina") != null) {
            pagina = Integer.parseInt(request.getParameter("pagina"));
        }
        
        List<Usuario> listaUsuarios = usuarioDAO.listarUsuarios(pagina, itensPorPagina);
        int totalUsuarios = usuarioDAO.contarUsuarios();
        int totalPaginas = (int) Math.ceil((double) totalUsuarios / itensPorPagina);
        
        request.setAttribute("listaUsuarios", listaUsuarios);
        request.setAttribute("totalPaginas", totalPaginas);
        request.setAttribute("paginaAtual", pagina);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/usuarios/listar.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioNovoUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cargosDisponiveis", UsuarioDAO.CARGOS_DISPONIVEIS);
        request.setAttribute("estadosDisponiveis", UsuarioDAO.ESTADOS_DISPONIVEIS);
        request.setAttribute("perfisDisponiveis", UsuarioDAO.PERFIS_DISPONIVEIS);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/usuarios/formulario.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioEditarUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Usuario usuario = usuarioDAO.buscarUsuarioPorId(id);
        
        if (usuario != null) {
            request.setAttribute("usuario", usuario);
            request.setAttribute("cargosDisponiveis", UsuarioDAO.CARGOS_DISPONIVEIS);
            request.setAttribute("estadosDisponiveis", UsuarioDAO.ESTADOS_DISPONIVEIS);
            request.setAttribute("perfisDisponiveis", UsuarioDAO.PERFIS_DISPONIVEIS);
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("/view/usuarios/formulario.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("usuarios?erro=Usuário não encontrado");
        }
    }

    private void visualizarUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Usuario usuario = usuarioDAO.buscarUsuarioPorId(id);
        
        if (usuario != null) {
            request.setAttribute("usuario", usuario);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/view/usuarios/visualizar.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("usuarios?erro=Usuário não encontrado");
        }
    }

    private void inserirUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        Usuario usuario = new Usuario();
        preencherUsuarioFromRequest(usuario, request);
        
        try {
            // Upload da foto de perfil
            Part filePart = request.getPart("foto_perfil");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = processarUploadFoto(filePart, usuario.getNome());
                usuario.setFoto_perfil(fileName);
            }
            
            if (usuarioDAO.inserirUsuario(usuario)) {
                response.sendRedirect("usuarios?sucesso=Usuário cadastrado com sucesso");
            } else {
                throw new SQLException("Falha ao inserir usuário");
            }
        } catch (SQLException e) {
            request.setAttribute("erro", "Erro ao cadastrar usuário: " + e.getMessage());
            request.setAttribute("usuario", usuario);
            mostrarFormularioNovoUsuario(request, response);
        }
    }

    private void atualizarUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Usuario usuario = usuarioDAO.buscarUsuarioPorId(id);
        
        if (usuario == null) {
            response.sendRedirect("usuarios?erro=Usuário não encontrado");
            return;
        }
        
        preencherUsuarioFromRequest(usuario, request);
        
        try {
            // Upload da nova foto de perfil, se fornecida
            Part filePart = request.getPart("foto_perfil");
            if (filePart != null && filePart.getSize() > 0) {
                // Remove a foto antiga se existir
                if (usuario.getFoto_perfil() != null && !usuario.getFoto_perfil().isEmpty()) {
                    usuarioDAO.removerFotoAntiga(usuario.getFoto_perfil());
                }
                
                String fileName = processarUploadFoto(filePart, usuario.getNome());
                usuario.setFoto_perfil(fileName);
            }
            
            if (usuarioDAO.atualizarUsuario(usuario)) {
                response.sendRedirect("usuarios?sucesso=Usuário atualizado com sucesso");
            } else {
                throw new SQLException("Falha ao atualizar usuário");
            }
        } catch (SQLException e) {
            request.setAttribute("erro", "Erro ao atualizar usuário: " + e.getMessage());
            request.setAttribute("usuario", usuario);
            mostrarFormularioEditarUsuario(request, response);
        }
    }

    private void desativarUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        if (usuarioDAO.desativarUsuario(id)) {
            response.sendRedirect("usuarios?sucesso=Usuário desativado com sucesso");
        } else {
            response.sendRedirect("usuarios?erro=Falha ao desativar usuário");
        }
    }

    private void reativarUsuario(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        if (usuarioDAO.reativarUsuario(id)) {
            response.sendRedirect("usuarios?sucesso=Usuário reativado com sucesso");
        } else {
            response.sendRedirect("usuarios?erro=Falha ao reativar usuário");
        }
    }

    private void preencherUsuarioFromRequest(Usuario usuario, HttpServletRequest request) {
        usuario.setNome(request.getParameter("nome"));
        usuario.setEmail(request.getParameter("email"));
        usuario.setSenha(request.getParameter("senha")); // Na prática, deveria ser hash da senha
        usuario.setCargo(request.getParameter("cargo"));
        usuario.setContacto(request.getParameter("telefone"));
        usuario.setStatus(request.getParameter("status"));
        usuario.setPerfil(request.getParameter("perfil"));
        usuario.setEstado(request.getParameter("estado"));
        usuario.setNumero_identificacao(request.getParameter("numero_identificacao"));
        
        // Se for edição, mantém o ID
        if (request.getParameter("id") != null) {
            usuario.setId_usuario(Integer.parseInt(request.getParameter("id")));
        }
    }

    private String processarUploadFoto(Part filePart, String nomeUsuario) throws IOException {
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        
        // Verifica extensão permitida
        boolean extensaoValida = false;
        for (String ext : UsuarioDAO.EXTENSOES_PERMITIDAS) {
            if (ext.equals(fileExtension)) {
                extensaoValida = true;
                break;
            }
        }
        
        if (!extensaoValida) {
            throw new IOException("Extensão de arquivo não permitida. Use: " + 
                String.join(", ", UsuarioDAO.EXTENSOES_PERMITIDAS));
        }
        
        // Verifica tamanho do arquivo
        if (filePart.getSize() > UsuarioDAO.MAX_FOTO_SIZE_KB * 1024) {
            throw new IOException("Tamanho do arquivo excede o limite de " + 
                UsuarioDAO.MAX_FOTO_SIZE_KB + "KB.");
        }
        
        // Cria diretório de upload se não existir
        File uploadDir = new File(UsuarioDAO.UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // Gera nome único para o arquivo
        String novoNome = nomeUsuario.replaceAll("\\s+", "_").toLowerCase() + 
            "_" + System.currentTimeMillis() + fileExtension;
        String caminhoCompleto = UsuarioDAO.UPLOAD_DIR + novoNome;
        
        // Salva o arquivo
        filePart.write(caminhoCompleto);
        
        return caminhoCompleto;
    }
}