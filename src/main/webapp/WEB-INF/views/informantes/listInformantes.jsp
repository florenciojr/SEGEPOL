<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Informantes</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .confiabilidade-alta { background-color: #d4edda; }
        .confiabilidade-media { background-color: #fff3cd; }
        .confiabilidade-baixa { background-color: #f8d7da; }
        .anonimato-true { font-style: italic; color: #6c757d; }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h1>Lista de Informantes</h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        
        <c:if test="${not empty idQueixa}">
            <a href="informantes?action=new&idQueixa=${idQueixa}" class="btn btn-primary mb-3">Novo Informante</a>
        </c:if>
        
        <c:if test="${empty idQueixa}">
            <a href="informantes?action=new" class="btn btn-primary mb-3">Novo Informante</a>
        </c:if>
        
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Cidadão</th>
                    <th>Queixa</th>
                    <th>Relato</th>
                    <th>Confiabilidade</th>
                    <th>Anonimato</th>
                    <th>Data Registro</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="informante" items="${listInformantes}">
                    <tr class="confiabilidade-${informante.confiabilidade.toLowerCase()} ${informante.anonimato ? 'anonimato-true' : ''}">
                        <td>${informante.idInformante}</td>
                        <td>${informante.anonimato ? 'Anônimo' : informante.idCidadao}</td>
                        <td>${informante.idQueixa}</td>
                        <td>${informante.relato.length() > 50 ? informante.relato.substring(0,50) + '...' : informante.relato}</td>
                        <td>${informante.confiabilidade}</td>
                        <td>${informante.anonimato ? 'Sim' : 'Não'}</td>
                        <td>${informante.dataRegistro}</td>
                        <td>
                            <a href="informantes?action=edit&id=${informante.idInformante}" 
                               class="btn btn-sm btn-warning">Editar</a>
                            <a href="informantes?action=delete&id=${informante.idInformante}&idQueixa=${informante.idQueixa}" 
                               class="btn btn-sm btn-danger" 
                               onclick="return confirm('Tem certeza que deseja excluir este informante?')">Excluir</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
