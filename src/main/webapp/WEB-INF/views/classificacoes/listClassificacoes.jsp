<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Classificações</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .classificacao-positiva { background-color: #d4edda; }
        .classificacao-negativa { background-color: #f8d7da; }
        .classificacao-neutra { background-color: #fff3cd; }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h1>Lista de Classificações</h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        
        <c:if test="${not empty idCidadao}">
            <a href="classificacoes?action=new&idCidadao=${idCidadao}" class="btn btn-primary mb-3">Nova Classificação</a>
        </c:if>
        
        <c:if test="${empty idCidadao}">
            <a href="classificacoes?action=new" class="btn btn-primary mb-3">Nova Classificação</a>
        </c:if>
        
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Cidadão</th>
                    <th>Classificação</th>
                    <th>Observações</th>
                    <th>Data</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="classificacao" items="${listClassificacoes}">
                    <tr class="classificacao-${classificacao.classificacao.toLowerCase().contains('positiv') ? 'positiva' : 
                                           classificacao.classificacao.toLowerCase().contains('negativ') ? 'negativa' : 'neutra'}">
                        <td>${classificacao.idClassificacao}</td>
                        <td>${classificacao.idCidadao}</td>
                        <td>${classificacao.classificacao}</td>
                        <td>${classificacao.observacoes.length() > 50 ? 
                            classificacao.observacoes.substring(0,50) + '...' : classificacao.observacoes}</td>
                        <td>${classificacao.dataClassificacao}</td>
                        <td>
                            <a href="classificacoes?action=edit&id=${classificacao.idClassificacao}" 
                               class="btn btn-sm btn-warning">Editar</a>
                            <a href="classificacoes?action=delete&id=${classificacao.idClassificacao}&idCidadao=${classificacao.idCidadao}" 
                               class="btn btn-sm btn-danger" 
                               onclick="return confirm('Tem certeza que deseja excluir esta classificação?')">Excluir</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
