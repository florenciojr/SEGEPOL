<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Gestão de Tipos de Queixa</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-4">
            <h1 class="mb-4">Tipos de Queixa</h1>
            
            <c:if test="${not empty mensagemSucesso}">
                <div class="alert alert-success alert-dismissible fade show">
                    ${mensagemSucesso}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            
            <c:if test="${not empty mensagemErro}">
                <div class="alert alert-danger alert-dismissible fade show">
                    ${mensagemErro}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            
            <!-- Inclui o formulário -->
            <jsp:include page="/WEB-INF/partials/formTipoQueixa.jsp" />
            
            <!-- Tabela de listagem -->
            <div class="table-responsive mt-4">
                <table class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Gravidade</th>
                            <th>Data Cadastro</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="tipo" items="${listaTipos}">
                            <tr>
                                <td>${tipo.idTipo}</td>
                                <td>${tipo.nomeTipo}</td>
                                <td>
                                    <span class="badge 
                                        ${tipo.gravidade == 'Leve' ? 'bg-info' : 
                                          tipo.gravidade == 'Média' ? 'bg-primary' : 
                                          tipo.gravidade == 'Grave' ? 'bg-warning' : 'bg-danger'}">
                                        ${tipo.gravidade}
                                    </span>
                                </td>
                                <td>${tipo.dataCadastro}</td>
                                <td>
                                    <a href="tiposqueixa?action=editar&id=${tipo.idTipo}" 
                                       class="btn btn-sm btn-primary">Editar</a>
                                    <a href="tiposqueixa?action=excluir&id=${tipo.idTipo}" 
                                       class="btn btn-sm btn-danger"
                                       onclick="return confirm('Tem certeza que deseja excluir este tipo?')">Excluir</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
