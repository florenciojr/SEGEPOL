<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Testemunhas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .tipo-ocular { background-color: #e6f7ff; }
        .tipo-auditiva { background-color: #fff2e6; }
        .tipo-outra { background-color: #f9f9f9; }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h1>Lista de Testemunhas</h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        
        <c:if test="${not empty idQueixa}">
            <a href="testemunhas?action=new&idQueixa=${idQueixa}" class="btn btn-primary mb-3">Nova Testemunha</a>
        </c:if>
        
        <c:if test="${empty idQueixa}">
            <a href="testemunhas?action=new" class="btn btn-primary mb-3">Nova Testemunha</a>
        </c:if>
        
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Cidadão</th>
                    <th>Queixa</th>
                    <th>Tipo</th>
                    <th>Descrição</th>
                    <th>Data Registro</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="testemunha" items="${listTestemunhas}">
                    <tr class="tipo-${testemunha.tipoTestemunha.toLowerCase()}">
                        <td>${testemunha.idTestemunha}</td>
                        <td>${testemunha.idCidadao}</td>
                        <td>${testemunha.idQueixa}</td>
                        <td>${testemunha.tipoTestemunha}</td>
                        <td>${testemunha.descricao}</td>
                        <td>${testemunha.dataRegistro}</td>
                        <td>
                            <a href="testemunhas?action=edit&id=${testemunha.idTestemunha}" 
                               class="btn btn-sm btn-warning">Editar</a>
                            <a href="testemunhas?action=delete&id=${testemunha.idTestemunha}&idQueixa=${testemunha.idQueixa}" 
                               class="btn btn-sm btn-danger" 
                               onclick="return confirm('Tem certeza que deseja excluir esta testemunha?')">Excluir</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
