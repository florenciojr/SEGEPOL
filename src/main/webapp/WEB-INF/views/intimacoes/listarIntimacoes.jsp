<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Intimações</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eee;
        }
        h1 {
            color: #333;
            margin: 0;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f8f9fa;
            font-weight: bold;
            color: #495057;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .btn {
            padding: 8px 16px;
            text-decoration: none;
            border-radius: 4px;
            font-weight: bold;
            transition: all 0.3s;
        }
        .btn-primary {
            background-color: #007bff;
            color: white;
            border: 1px solid #007bff;
        }
        .btn-primary:hover {
            background-color: #0069d9;
            border-color: #0062cc;
        }
        .status {
            font-weight: bold;
            padding: 5px 10px;
            border-radius: 4px;
            display: inline-block;
            min-width: 100px;
            text-align: center;
        }
        .status-pendente {
            background-color: #fff3cd;
            color: #856404;
        }
        .status-compareceu {
            background-color: #d4edda;
            color: #155724;
        }
        .status-nao-compareceu {
            background-color: #f8d7da;
            color: #721c24;
        }
        .status-adiado {
            background-color: #e2e3e5;
            color: #383d41;
        }
        .status-indefinido {
            background-color: #f8f9fa;
            color: #6c757d;
        }
        .actions a {
            margin-right: 8px;
            color: #6c757d;
            text-decoration: none;
            font-size: 1.1em;
            transition: color 0.3s;
        }
        .actions a:hover {
            color: #007bff;
        }
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 4px;
        }
        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .empty-message {
            text-align: center;
            padding: 20px;
            color: #6c757d;
            font-style: italic;
        }
        .badge {
            display: inline-block;
            padding: 3px 7px;
            font-size: 12px;
            font-weight: bold;
            line-height: 1;
            color: #fff;
            text-align: center;
            white-space: nowrap;
            vertical-align: middle;
            background-color: #6c757d;
            border-radius: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Lista de Intimações</h1>
            <a href="intimacoes?action=new" class="btn btn-primary">+ Nova Intimação</a>
        </div>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Cidadão</th>
                    <th>Queixa</th>
                    <th>Data Comparecimento</th>
                    <th>Local</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty intimacoes}">
                        <c:forEach items="${intimacoes}" var="intimacao">
                            <tr>
                                <td><span class="badge">#${intimacao.idIntimacao}</span></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty intimacao.nomeCidadao}">
                                            ${intimacao.nomeCidadao}
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge">Cidadão ID: ${intimacao.idCidadao}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty intimacao.tituloQueixa}">
                                            ${intimacao.tituloQueixa}
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge">Queixa ID: ${intimacao.idQueixa}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty intimacao.dataComparecimento}">
                                            <fmt:parseDate value="${intimacao.dataComparecimento.toString()}" pattern="yyyy-MM-dd" var="parsedDate" />
                                            <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy" />
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: #6c757d;">Não definida</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:out value="${not empty intimacao.localComparecimento ? intimacao.localComparecimento : 'Não informado'}" />
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty intimacao.status}">
                                            <span class="status status-${fn:replace(fn:toLowerCase(intimacao.status), 'ã', 'a').replace(' ', '-')}">
                                                ${intimacao.status}
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status status-indefinido">Indefinido</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="actions">
                                    <a href="intimacoes?action=view&id=${intimacao.idIntimacao}" title="Visualizar"><i class="fas fa-eye"></i></a>
                                    <a href="intimacoes?action=edit&id=${intimacao.idIntimacao}" title="Editar"><i class="fas fa-edit"></i></a>
                                    <a href="intimacoes?action=delete&id=${intimacao.idIntimacao}" 
                                       onclick="return confirm('Tem certeza que deseja excluir esta intimação?')" title="Excluir"><i class="fas fa-trash"></i></a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="7" class="empty-message">Nenhuma intimação encontrada</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <!-- Adicione Font Awesome para ícones -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    
    <!-- Adicione a taglib fn se ainda não estiver adicionada -->
    <%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
</body>
</html>
