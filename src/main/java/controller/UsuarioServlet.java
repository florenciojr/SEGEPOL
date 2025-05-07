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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;

@WebServlet(name = "UsuarioServlet", urlPatterns = {"/usuarios"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,  // 1MB
    maxFileSize = 1024 * 1024 * 2,       // 2MB
    maxRequestSize = 1024 * 1024 * 10    // 10MB
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
        try {
            String action = request.getParameter("action");
            
            if (action == null) {
                listarUsuarios(request, response);
            } else {
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
                        desativarUsuario(request, response);
                        break;
                    case "ativar":
                        ativarUsuario(request, response);
                        break;
                    case "ocultar":
                        ocultarUsuario(request, response);
                        break;
                    case "buscar":
                        buscarUsuarios(request, response);
                        break;
                    default:
                        listarUsuarios(request, response);
                }
            }
        } catch (SQLException ex) {
            tratarErro(request, response, "Erro ao processar requisição: " + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            
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

    
    
    
    
private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, ServletException, IOException {
    int pagina = 1;
    int itensPorPagina = 10;
    
    if (request.getParameter("pagina") != null) {
        pagina = Integer.parseInt(request.getParameter("pagina"));
    }
    
    List<Usuario> listaUsuarios = usuarioDAO.listarUsuarios(pagina, itensPorPagina); // Mude o nome da variável
    int total = usuarioDAO.contarUsuarios();
    int totalPaginas = (int) Math.ceil((double) total / itensPorPagina);
    
    request.setAttribute("listaUsuarios", listaUsuarios); // Corrija o nome do atributo
    request.setAttribute("totalPaginas", totalPaginas);
    request.setAttribute("paginaAtual", pagina);
    
    encaminharParaView("/WEB-INF/views/usuarios/listUsuario.jsp", request, response);
}

    private void buscarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String termo = request.getParameter("termo");
        List<Usuario> usuarios = new ArrayList<>();
        
        if (termo != null && !termo.isEmpty()) {
            // Busca por nome, email ou número de identificação
            usuarios = usuarioDAO.buscarUsuariosPorTermo(termo);
        }
        
        request.setAttribute("usuarios", usuarios);
        request.setAttribute("termoBusca", termo);
        encaminharParaView("/WEB-INF/views/usuarios/listUsuario.jsp", request, response);
    }

    private void mostrarFormularioNovo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        carregarDadosFormulario(request);
        encaminharParaView("/WEB-INF/views/usuarios/formUsuario.jsp", request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Usuario usuario = usuarioDAO.buscarUsuarioPorId(id);
        
        if (usuario == null) {
            response.sendRedirect("usuarios?erro=Usuário não encontrado");
            return;
        }
        
        request.setAttribute("usuario", usuario);
        carregarDadosFormulario(request);
        encaminharParaView("/WEB-INF/views/usuarios/formUsuario.jsp", request, response);
    }

    private void visualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Usuario usuario = usuarioDAO.buscarUsuarioPorId(id);
        
        if (usuario == null) {
            response.sendRedirect("usuarios?erro=Usuário não encontrado");
            return;
        }
        
        request.setAttribute("usuario", usuario);
        encaminharParaView("/WEB-INF/views/usuarios/visualizarUsuario.jsp", request, response);
    }

  private void inserirUsuario(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException, ServletException {
    
    // DEBUG: Verificar parâmetros recebidos
    System.out.println("DEBUG - Parâmetros recebidos:");
    Enumeration<String> params = request.getParameterNames();
    while(params.hasMoreElements()) {
        String paramName = params.nextElement();
        System.out.println(paramName + ": " + request.getParameter(paramName));
    }
    
    Usuario usuario = new Usuario();
    usuario.setNome(request.getParameter("nome"));
    usuario.setEmail(request.getParameter("email"));
    usuario.setSenha(request.getParameter("senha")); // Deveria ser hasheada
    usuario.setCargo(request.getParameter("cargo"));
    usuario.setTelefone(request.getParameter("telefone"));
    usuario.setStatus(request.getParameter("status"));
    usuario.setPerfil(request.getParameter("perfil"));
    usuario.setNumero_identificacao(request.getParameter("numero_identificacao"));
    
    try {
        // Processar foto se existir
        Part filePart = request.getPart("foto_perfil");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = processarUploadFoto(filePart, usuario.getNome());
            usuario.setFoto_perfil(fileName);
        }
        
        // DEBUG
        System.out.println("DEBUG - Usuário a ser inserido: " + usuario.toString());
        
        boolean sucesso = usuarioDAO.inserirUsuario(usuario);
        
        if (sucesso) {
            response.sendRedirect(request.getContextPath() + "/usuarios?sucesso=Usuário cadastrado com sucesso");
        } else {
            throw new SQLException("Falha ao inserir usuário");
        }
    } catch (SQLException e) {
        // DEBUG
        System.err.println("ERRO ao inserir: " + e.getMessage());
        e.printStackTrace();
        
        request.setAttribute("erro", "Erro ao cadastrar: " + e.getMessage());
        request.setAttribute("usuario", usuario);
        carregarDadosFormulario(request);
        mostrarFormularioNovo(request, response);
    }
}
  
private void atualizarUsuario(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException, ServletException {
    
    int id = Integer.parseInt(request.getParameter("id"));
    Usuario usuario = usuarioDAO.buscarUsuarioPorId(id);
    
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/usuarios?erro=Usuário não encontrado");
        return;
    }

    try {
        // Atualizar campos
        usuario.setNome(request.getParameter("nome"));
        usuario.setEmail(request.getParameter("email"));
        
        // Atualizar senha apenas se foi fornecida
        String senha = request.getParameter("senha");
        if (senha != null && !senha.isEmpty()) {
            usuario.setSenha(hashSenha(senha));
        }
        
        usuario.setCargo(request.getParameter("cargo"));
        usuario.setTelefone(request.getParameter("telefone"));
        usuario.setStatus(request.getParameter("status"));
        usuario.setPerfil(request.getParameter("perfil"));
        usuario.setNumero_identificacao(request.getParameter("numero_identificacao"));
        
        // Processar foto se existir
        Part filePart = request.getPart("foto_perfil");
        if (filePart != null && filePart.getSize() > 0) {
            // Remove foto antiga se existir
            if (usuario.getFoto_perfil() != null && !usuario.getFoto_perfil().isEmpty()) {
                usuarioDAO.removerFotoAntiga(usuario.getFoto_perfil(), getServletContext());
            }
            
            String fileName = processarUploadFoto(filePart, usuario.getNome());
            usuario.setFoto_perfil(fileName);
        }
        
        boolean sucesso = usuarioDAO.atualizarUsuario(usuario);
        
        if (sucesso) {
            // Redireciona para a visualização do usuário atualizado
            response.sendRedirect(request.getContextPath() + "/usuarios?action=visualizar&id=" + usuario.getId_usuario() + "&sucesso=Usuário atualizado com sucesso");
        } else {
            throw new SQLException("Falha ao atualizar usuário no banco de dados");
        }
        
    } catch (Exception e) {
        // Em caso de erro, mantém na página de edição com os dados preenchidos
        request.setAttribute("erro", "Erro ao atualizar: " + e.getMessage());
        request.setAttribute("usuario", usuario);
        carregarDadosFormulario(request);
        request.getRequestDispatcher("/WEB-INF/views/usuarios/formUsuario.jsp").forward(request, response);
    }
}

    private void autenticarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String identificacao = request.getParameter("identificacao");
        String senha = request.getParameter("senha"); // Senha já deve vir hashada
        
        Usuario usuario = usuarioDAO.autenticarUsuario(identificacao, senha);
        
        if (usuario != null) {
            // Criar sessão
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogado", usuario);
            
            // Redirecionar conforme perfil
            redirecionarPorPerfil(usuario.getPerfil(), response);
        } else {
            request.setAttribute("erro", "Credenciais inválidas ou usuário inativo");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void desativarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        if (usuarioDAO.desativarUsuario(id)) {
            response.sendRedirect("usuarios?sucesso=Usuário desativado com sucesso");
        } else {
            response.sendRedirect("usuarios?erro=Falha ao desativar usuário");
        }
    }

    private void ativarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        if (usuarioDAO.reativarUsuario(id)) {
            response.sendRedirect("usuarios?sucesso=Usuário ativado com sucesso");
        } else {
            response.sendRedirect("usuarios?erro=Falha ao ativar usuário");
        }
    }

    private void ocultarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean oculto = Boolean.parseBoolean(request.getParameter("oculto"));
        
        if (usuarioDAO.ocultarUsuario(id, oculto)) {
            response.sendRedirect("usuarios?sucesso=Usuário " + (oculto ? "ocultado" : "tornado visível") + " com sucesso");
        } else {
            response.sendRedirect("usuarios?erro=Falha ao alterar visibilidade do usuário");
        }
    }

    // Métodos auxiliares
    private void carregarDadosFormulario(HttpServletRequest request) {
        request.setAttribute("cargos", UsuarioDAO.CARGOS_DISPONIVEIS);
        request.setAttribute("perfis", UsuarioDAO.PERFIS_DISPONIVEIS);
        request.setAttribute("statusOptions", new String[]{"Ativo", "Inativo"});
    }

private Usuario criarUsuarioFromRequest(HttpServletRequest request) {
    Usuario usuario = new Usuario();
    usuario.setNome(request.getParameter("nome"));
    usuario.setEmail(request.getParameter("email"));
    
    // Hash da senha antes de armazenar
    String senha = request.getParameter("senha");
    if (senha != null && !senha.isEmpty()) {
        String senhaHash = hashSenha(senha); // Implemente este método
        usuario.setSenha(senhaHash);
    }
    
    usuario.setCargo(request.getParameter("cargo"));
    usuario.setPerfil(request.getParameter("perfil"));
    usuario.setStatus(request.getParameter("status"));
    usuario.setTelefone(request.getParameter("telefone"));
    usuario.setNumero_identificacao(request.getParameter("numero_identificacao"));
    usuario.setOculto(false);
    return usuario;
}

// Método para hash de senha (adicione na classe)
private String hashSenha(String senha) {
    // Implemente um hash seguro (adicione a biblioteca BCrypt ao seu projeto)
    // Exemplo com BCrypt:
    // return BCrypt.hashpw(senha, BCrypt.gensalt());
    
    // Solução temporária (não recomendada para produção):
    try {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(senha.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();
        
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        
        return hexString.toString();
    } catch (Exception e) {
        throw new RuntimeException("Erro ao gerar hash da senha", e);
    }
}
    private void atualizarUsuarioFromRequest(Usuario usuario, HttpServletRequest request) {
        usuario.setNome(request.getParameter("nome"));
        usuario.setEmail(request.getParameter("email"));
        
        // Só atualiza a senha se foi fornecida
        String senha = request.getParameter("senha");
        if (senha != null && !senha.isEmpty()) {
            usuario.setSenha(senha);
        }
        
        usuario.setCargo(request.getParameter("cargo"));
        usuario.setPerfil(request.getParameter("perfil"));
        usuario.setStatus(request.getParameter("status"));
        usuario.setTelefone(request.getParameter("telefone"));
        usuario.setNumero_identificacao(request.getParameter("numero_identificacao"));
        
        if (request.getParameter("oculto") != null) {
            usuario.setOculto(Boolean.parseBoolean(request.getParameter("oculto")));
        }
    }

    private void processarFotoPerfil(HttpServletRequest request, Usuario usuario)
            throws IOException, ServletException {
        Part filePart = request.getPart("foto_perfil");
        
        if (filePart != null && filePart.getSize() > 0) {
            // Remove foto antiga se existir
            if (usuario.getFoto_perfil() != null && !usuario.getFoto_perfil().isEmpty()) {
                usuarioDAO.removerFotoAntiga(usuario.getFoto_perfil(), getServletContext());
            }
            
            // Processa novo upload
            String fileName = processarUploadFoto(filePart, usuario.getNome());
            usuario.setFoto_perfil(fileName);
        }
    }

    private String processarUploadFoto(Part filePart, String prefixoNome) throws IOException {
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        
        // Valida extensão
        if (!Arrays.asList(UsuarioDAO.EXTENSOES_PERMITIDAS).contains(fileExtension)) {
            throw new IOException("Extensão de arquivo não permitida");
        }
        
        // Valida tamanho
        if (filePart.getSize() > UsuarioDAO.MAX_FOTO_SIZE_KB * 1024) {
            throw new IOException("Tamanho do arquivo excede o limite permitido");
        }
        
        // Cria diretório se não existir
        String uploadDirPath = getServletContext().getRealPath("") + File.separator + UsuarioDAO.UPLOAD_DIR;
        Files.createDirectories(Paths.get(uploadDirPath));
        
        // Gera nome único para o arquivo
        String novoNome = prefixoNome.replaceAll("[^a-zA-Z0-9]", "_") + "_" + 
                         System.currentTimeMillis() + fileExtension;
        String filePath = uploadDirPath + File.separator + novoNome;
        
        // Salva o arquivo
        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        }
        
        return UsuarioDAO.UPLOAD_DIR + "/" + novoNome;
    }

    private void redirecionarPorPerfil(String perfil, HttpServletResponse response) throws IOException {
        switch (perfil) {
            case "Comando":
                response.sendRedirect("dashboard-comando.jsp");
                break;
            case "Super Admin":
                response.sendRedirect("dashboard-admin.jsp");
                break;
            case "Operacional":
                response.sendRedirect("dashboard-operacional.jsp");
                break;
            default:
                response.sendRedirect("dashboard.jsp");
        }
    }

    private void encaminharParaView(String view, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            System.out.println("[DEBUG] Encaminhando para: " + view);
    String realPath = getServletContext().getRealPath(view);
    System.out.println("[DEBUG] Caminho real: " + realPath);
        
        
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }

    private void tratarErro(HttpServletRequest request, HttpServletResponse response, String mensagem)
            throws ServletException, IOException {
        request.setAttribute("erro", mensagem);
        encaminharParaView("/view/erro.jsp", request, response);
    }
}