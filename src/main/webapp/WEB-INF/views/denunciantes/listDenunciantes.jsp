<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Denunciantes</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Lista de Denunciantes</h1>
        
        <c:if test="${not empty idQueixa}">
            <a href="denunciantes?action=new&idQueixa=${idQueixa}" class="btn btn-primary mb-3">Novo Denunciante</a>
        </c:if>
        
        <c:if test="${empty idQueixa}">
            <a href="denunciantes?action=new" class="btn btn-primary mb-3">Novo Denunciante</a>
        </c:if>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Cidadão</th>
                    <th>Queixa</th>
                    <th>Modo de Denúncia</th>
                    <th>Data</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="denunciante" items="${listDenunciantes}">
                    <tr>
                        <td>${denunciante.idDenunciante}</td>
                        <td>${denunciante.idCidadao}</td>
                        <td>${denunciante.idQueixa}</td>
                        <td>${denunciante.modoDenuncia}</td>
                        <td>${denunciante.dataDenuncia}</td>
                        <td>
                            <a href="denunciantes?action=edit&id=${denunciante.idDenunciante}" class="btn btn-sm btn-warning">Editar</a>
                            <a href="denunciantes?action=delete&id=${denunciante.idDenunciante}&idQueixa=${denunciante.idQueixa}" 
                               class="btn btn-sm btn-danger" 
                               onclick="return confirm('Tem certeza que deseja excluir?')">Excluir</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
