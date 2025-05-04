<%-- 
    Document   : newjsp
    Created on : 2 de mai de 2025, 15:22:35
    Author     : JR5
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Usuários</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Lista de Usuários</h1>
        
        <!-- Mensagens de sucesso/erro -->
        <c:if test="${not empty param.sucesso}">
            <div class="alert alert-success">${param.sucesso}</div>
        </c:if>
        <c:if test="${not empty param.erro}">
            <div class="alert alert-danger">${param.erro}</div>
        </c:if>
        
        <div class="d-flex justify-content-between mb-3">
            <a href="usuarios?action=novo" class="btn btn-primary">Novo Usuário</a>
        </div>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Email</th>
                    <th>Cargo</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${listaUsuarios}" var="usuario">
                    <tr>
                        <td>${usuario.id_usuario}</td>
                        <td>${usuario.nome}</td>
                        <td>${usuario.email}</td>
                        <td>${usuario.cargo}</td>
                        <td>${usuario.status}</td>
                        <td>
                            <a href="usuarios?action=visualizar&id=${usuario.id_usuario}" class="btn btn-sm btn-info">Visualizar</a>
                            <a href="usuarios?action=editar&id=${usuario.id_usuario}" class="btn btn-sm btn-warning">Editar</a>
                            <c:choose>
                                <c:when test="${usuario.status eq 'Ativo'}">
                                    <a href="usuarios?action=desativar&id=${usuario.id_usuario}" class="btn btn-sm btn-danger">Desativar</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="usuarios?action=reativar&id=${usuario.id_usuario}" class="btn btn-sm btn-success">Reativar</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <!-- Paginação -->
        <nav aria-label="Page navigation">
            <ul class="pagination">
                <c:forEach begin="1" end="${totalPaginas}" var="i">
                    <li class="page-item ${i == paginaAtual ? 'active' : ''}">
                        <a class="page-link" href="usuarios?pagina=${i}">${i}</a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>