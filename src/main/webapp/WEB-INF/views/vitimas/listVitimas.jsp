<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Vítimas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .genero-masculino { background-color: #e6f7ff; }
        .genero-feminino { background-color: #ffe6f7; }
        .genero-outro { background-color: #f9f9f9; }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h1>Lista de Vítimas</h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        
        <c:if test="${not empty idQueixa}">
            <a href="vitimas?action=new&idQueixa=${idQueixa}" class="btn btn-primary mb-3">Nova Vítima</a>
        </c:if>
        
        <c:if test="${empty idQueixa}">
            <a href="vitimas?action=new" class="btn btn-primary mb-3">Nova Vítima</a>
        </c:if>
        
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Queixa</th>
                    <th>Descrição</th>
                    <th>Nascimento</th>
                    <th>Gênero</th>
                    <th>Cidadão</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="vitima" items="${listVitimas}">
                    <tr class="genero-${vitima.genero.toLowerCase().startsWith('m') ? 'masculino' : 
                                       vitima.genero.toLowerCase().startsWith('f') ? 'feminino' : 'outro'}">
                        <td>${vitima.idVitima}</td>
                        <td>${vitima.nome}</td>
                        <td>${vitima.idQueixa}</td>
                        <td>${vitima.descricao.length() > 30 ? vitima.descricao.substring(0,30) + '...' : vitima.descricao}</td>
                        <td>${vitima.dataNascimento}</td>
                        <td>${vitima.genero}</td>
                        <td>${vitima.idCidadao}</td>
                        <td>
                            <a href="vitimas?action=edit&id=${vitima.idVitima}" class="btn btn-sm btn-warning">Editar</a>
                            <a href="vitimas?action=delete&id=${vitima.idVitima}&idQueixa=${vitima.idQueixa}" 
                               class="btn btn-sm btn-danger" 
                               onclick="return confirm('Tem certeza que deseja excluir esta vítima?')">Excluir</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
