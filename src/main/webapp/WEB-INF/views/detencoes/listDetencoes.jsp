<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Detenções</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .status-detetido { background-color: #ffcccc; }
        .status-liberado { background-color: #ccffcc; }
        .status-outro { background-color: #ffffcc; }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h1>Lista de Detenções</h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        
        <c:if test="${not empty idCidadao}">
            <a href="detencoes?action=new&idCidadao=${idCidadao}" class="btn btn-primary mb-3">Nova Detenção</a>
        </c:if>
        
        <c:if test="${empty idCidadao}">
            <a href="detencoes?action=new" class="btn btn-primary mb-3">Nova Detenção</a>
        </c:if>
        
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Cidadão</th>
                    <th>Motivo</th>
                    <th>Local</th>
                    <th>Status</th>
                    <th>Responsável</th>
                    <th>Data</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="detencao" items="${listDetencoes}">
                    <tr class="status-${detencao.status.toLowerCase()}">
                        <td>${detencao.idDetencao}</td>
                        <td>${detencao.idCidadao}</td>
                        <td>${detencao.motivo}</td>
                        <td>${detencao.localDetencao}</td>
                        <td>${detencao.status}</td>
                        <td>${detencao.idUsuarioResponsavel}</td>
                        <td>${detencao.dataDetencao}</td>
                        <td>
                            <a href="detencoes?action=edit&id=${detencao.idDetencao}" 
                               class="btn btn-sm btn-warning">Editar</a>
                            <a href="detencoes?action=delete&id=${detencao.idDetencao}&idCidadao=${detencao.idCidadao}" 
                               class="btn btn-sm btn-danger" 
                               onclick="return confirm('Tem certeza que deseja excluir esta detenção?')">Excluir</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
