/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;


import dao.VeiculoRoubadoDAO;
import model.VeiculoRoubado;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author JR5
 */


import dao.VeiculoRoubadoDAO;
import dao.QueixaDAO;
import model.VeiculoRoubado;
import model.Queixa;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet(name = "VeiculoRoubadoServlet", urlPatterns = {"/veiculos"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1MB
    maxFileSize = 1024 * 1024 * 5,        // 5MB
    maxRequestSize = 1024 * 1024 * 10     // 10MB
)
public class VeiculoRoubadoServlet extends HttpServlet {

    private VeiculoRoubadoDAO veiculoDAO;
    private QueixaDAO queixaDAO;

@Override
public void init() {
    veiculoDAO = new VeiculoRoubadoDAO(getServletContext());
    queixaDAO = new QueixaDAO();
}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Adicionando logs para depuração
        System.out.println("Método doPost acionado");
        System.out.println("Parâmetros recebidos:");
        request.getParameterMap().forEach((k, v) -> System.out.println(k + " = " + String.join(", ", v)));

        String action = request.getParameter("action");
        
        if (action == null || action.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parâmetro 'action' não fornecido");
            return;
        }

        try {
            switch (action.toLowerCase()) { // Convertendo para lowercase para evitar problemas de case sensitivity
                case "inserir":
                    inserirVeiculo(request, response);
                    break;
                case "atualizar":
                    atualizarVeiculo(request, response);
                    break;
                case "deletar":
                    deletarVeiculo(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação inválida: " + action);
                    break;
            }
} catch (SQLException ex) {
    ex.printStackTrace();
    request.setAttribute("erro", "Erro no banco de dados: " + ex.getMessage());
    try {
        if ("atualizar".equals(action)) {
            mostrarFormEdicao(request, response);
        } else {
            mostrarFormNovo(request, response);
        }
    } catch (ServletException | IOException e) {
        ex.addSuppressed(e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                         "Erro ao exibir formulário após erro no banco de dados");
    }       catch (SQLException ex1) {
                Logger.getLogger(VeiculoRoubadoServlet.class.getName()).log(Level.SEVERE, null, ex1);
            }
} catch (Exception ex) {
    ex.printStackTrace();
    request.setAttribute("erro", "Erro inesperado: " + ex.getMessage());
    try {
        if ("atualizar".equals(action)) {
            mostrarFormEdicao(request, response);
        } else {
            mostrarFormNovo(request, response);
        }
    } catch (ServletException | IOException e) {
        ex.addSuppressed(e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                         "Erro ao exibir formulário após erro inesperado");
    }       catch (SQLException ex1) {
                Logger.getLogger(VeiculoRoubadoServlet.class.getName()).log(Level.SEVERE, null, ex1);
            }
}}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null) {
                listarVeiculos(request, response);
            } else {
                switch (action.toLowerCase()) {
                    case "editar":
                        mostrarFormEdicao(request, response);
                        break;
                    case "novo":
                        mostrarFormNovo(request, response);
                        break;
                    case "buscar":
                        buscarPorMatricula(request, response);
                        break;
                    case "detalhes":
                        mostrarDetalhes(request, response);
                        break;
                    default:
                        listarVeiculos(request, response);
                        break;
                }
            }
        } catch (SQLException ex) {
            throw new ServletException("Erro no banco de dados", ex);
        }
    }

    private void listarVeiculos(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<VeiculoRoubado> veiculos = veiculoDAO.listarTodos();
        List<Queixa> queixas = queixaDAO.listarQueixas();
        
        request.setAttribute("veiculos", veiculos);
        request.setAttribute("queixas", queixas);
        request.getRequestDispatcher("/WEB-INF/views/VeiculosRoubados/listVeiculosRoubados.jsp").forward(request, response);
    }

    private void mostrarFormNovo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Queixa> queixas = queixaDAO.listarQueixas();
            request.setAttribute("queixas", queixas);
            request.setAttribute("modo", "inserir");
            request.getRequestDispatcher("/WEB-INF/views/VeiculosRoubados/formVeiculosRoubados.jsp").forward(request, response);
        } catch (SQLException ex) {
            throw new ServletException("Erro ao carregar queixas", ex);
        }
    }

    private void mostrarFormEdicao(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            VeiculoRoubado veiculo = veiculoDAO.buscarPorId(id);
            List<Queixa> queixas = queixaDAO.listarQueixas();
            
            request.setAttribute("veiculo", veiculo);
            request.setAttribute("queixas", queixas);
            request.setAttribute("modo", "editar");
            request.getRequestDispatcher("/WEB-INF/views/VeiculosRoubados/formVeiculosRoubados.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            throw new ServletException("ID inválido", e);
        }
    }

    private void inserirVeiculo(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        VeiculoRoubado veiculo = criarVeiculoAPartirRequest(request);
        veiculo.setDataRegistro(new Timestamp(System.currentTimeMillis()));
        
        try {
            // Processar upload de foto
            Part filePart = request.getPart("fotoVeiculo");
            if (filePart != null && filePart.getSize() > 0) {
                String caminhoFoto = veiculoDAO.uploadFoto(filePart, veiculo.getMatricula());
                veiculo.setFotoVeiculo(caminhoFoto);
            }
            
            if (veiculoDAO.inserir(veiculo)) {
                response.sendRedirect(request.getContextPath() + "/veiculos?sucesso=Veículo+cadastrado+com+sucesso");
            } else {
                request.setAttribute("erro", "Não foi possível cadastrar o veículo");
                mostrarFormNovo(request, response);
            }
        } catch (IOException e) {
            request.setAttribute("erro", "Erro ao fazer upload da foto: " + e.getMessage());
            mostrarFormNovo(request, response);
        }
    }

    private void atualizarVeiculo(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            VeiculoRoubado veiculo = criarVeiculoAPartirRequest(request);
            veiculo.setIdVeiculo(id);
            
            // Processar foto
            processarFoto(request, veiculo);
            
            if (veiculoDAO.atualizar(veiculo)) {
                response.sendRedirect(request.getContextPath() + "/veiculos?sucesso=Veículo+atualizado+com+sucesso");
            } else {
                request.setAttribute("erro", "Não foi possível atualizar o veículo");
                mostrarFormEdicao(request, response);
            }
        } catch (NumberFormatException e) {
            throw new ServletException("ID inválido", e);
        } catch (IOException e) {
            request.setAttribute("erro", "Erro ao processar foto: " + e.getMessage());
            mostrarFormEdicao(request, response);
        }
    }

    private void processarFoto(HttpServletRequest request, VeiculoRoubado veiculo) 
            throws IOException, ServletException {
        // Verificar se uma nova foto foi enviada
        Part filePart = request.getPart("fotoVeiculo");
        
        if (filePart != null && filePart.getSize() > 0) {
            // Nova foto enviada - fazer upload
            String caminhoFoto = veiculoDAO.uploadFoto(filePart, veiculo.getMatricula());
            veiculo.setFotoVeiculo(caminhoFoto);
        } else if ("true".equals(request.getParameter("fotoRemovida"))) {
            // Foto foi removida pelo usuário
            veiculo.setFotoVeiculo(null);
        } else {
            // Manter a foto existente
            String fotoExistente = request.getParameter("fotoExistente");
            if (fotoExistente != null && !fotoExistente.isEmpty()) {
                veiculo.setFotoVeiculo(fotoExistente);
            }
        }
    }

    private void deletarVeiculo(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            if (veiculoDAO.deletar(id)) {
                response.sendRedirect(request.getContextPath() + "/veiculos?sucesso=Veículo+removido+com+sucesso");
            } else {
                response.sendRedirect(request.getContextPath() + "/veiculos?erro=Não+foi+possível+remover+o+veículo");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/veiculos?erro=ID+inválido");
        }
    }

    private void buscarPorMatricula(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String matricula = request.getParameter("matricula");
        List<VeiculoRoubado> veiculos = veiculoDAO.buscarPorMatricula(matricula);
        List<Queixa> queixas = queixaDAO.listarQueixas();
        
        request.setAttribute("veiculos", veiculos);
        request.setAttribute("queixas", queixas);
        request.getRequestDispatcher("/WEB-INF/views/VeiculosRoubados/listVeiculosRoubados.jsp").forward(request, response);
    }

    private void mostrarDetalhes(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            VeiculoRoubado veiculo = veiculoDAO.buscarPorId(id);
            request.setAttribute("veiculo", veiculo);
            request.getRequestDispatcher("/WEB-INF/views/VeiculosRoubados/detalhesVeiculosRoubados.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            throw new ServletException("ID inválido", e);
        }
    }

    private VeiculoRoubado criarVeiculoAPartirRequest(HttpServletRequest request) {
        VeiculoRoubado veiculo = new VeiculoRoubado();
        
        // Definir idQueixa (pode ser nulo)
        String idQueixa = request.getParameter("idQueixa");
        if (idQueixa != null && !idQueixa.isEmpty()) {
            veiculo.setIdQueixa(Integer.parseInt(idQueixa));
        }
        
        // Definir demais propriedades
        veiculo.setMarca(request.getParameter("marca"));
        veiculo.setModelo(request.getParameter("modelo"));
        veiculo.setCor(request.getParameter("cor"));
        veiculo.setMatricula(request.getParameter("matricula"));
        
        // Definir ano (pode ser nulo)
        String ano = request.getParameter("ano");
        if (ano != null && !ano.isEmpty()) {
            veiculo.setAno(Integer.parseInt(ano));
        }
        
        return veiculo;
    }
}
