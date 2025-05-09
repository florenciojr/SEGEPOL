<%-- 
    Document   : listarCidadaos
    Created on : 4 de mai de 2025, 00:47:42
    Author     : JR5
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Cidadãos</title>
   <%@include file="/WEB-INF/views/templates/header.jsp" %>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            border-bottom: 1px solid #eee;
            padding-bottom: 10px;
        }
        .action-buttons {
            margin: 20px 0;
            display: flex;
            justify-content: space-between;
        }
        .btn {
            padding: 8px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            font-size: 14px;
        }
        .btn-primary {
            background-color: #4CAF50;
            color: white;
        }
        .btn-secondary {
            background-color: #f0ad4e;
            color: white;
        }
        .btn-danger {
            background-color: #d9534f;
            color: white;
        }
        .btn-info {
            background-color: #5bc0de;
            color: white;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #4CAF50;
            color: white;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .search-form {
            display: flex;
            gap: 10px;
        }
        .search-form input {
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .search-form button {
            padding: 8px 15px;
            background-color: #5bc0de;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Lista de Cidadãos</h1>
        
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/cidadao?action=novo" class="btn btn-primary">Novo Cidadão</a>
            
            <form class="search-form" action="${pageContext.request.contextPath}/cidadao" method="GET">
                <input type="hidden" name="action" value="buscar">
                <input type="text" name="documento" placeholder="Buscar por documento">
                <button type="submit">Buscar</button>
            </form>
        </div>
        
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Documento</th>
                    <th>Telefone</th>
                    <th>Email</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="cidadao" items="${cidadaos}">
                    <tr>
                        <td>${cidadao.idCidadao}</td>
                        <td>${cidadao.nome}</td>
                        <td>${cidadao.documentoIdentificacao}</td>
                        <td>${cidadao.telefone}</td>
                        <td>${cidadao.email}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/cidadao?action=editar&id=${cidadao.idCidadao}" class="btn btn-secondary">Editar</a>
                            <a href="${pageContext.request.contextPath}/cidadao?action=deletar&id=${cidadao.idCidadao}" class="btn btn-danger" onclick="return confirm('Tem certeza?')">Excluir</a>
                            <a href="${pageContext.request.contextPath}/cidadao?action=buscar&documento=${cidadao.documentoIdentificacao}" class="btn btn-info">Detalhes</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${cidadaos.isEmpty()}">
                    <tr>
                        <td colspan="6" style="text-align: center;">Nenhum cidadão cadastrado</td>
                    </tr>
                </c:if>
            </tbody>


        </table>
    </div>
<%@include file="/WEB-INF/views/templates/footer.jsp" %>
</body>

</html>
