<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Vítimas</title>
    <style>
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }
        .success { color: green; }
        .error { color: red; }
        .actions a { margin-right: 10px; }
    </style>
</head>
<body>
    <h1>Lista de Vítimas</h1>
    
    <c:if test="${not empty param.success}">
        <p class="success">${param.success}</p>
    </c:if>
    
    <form method="get" action="${pageContext.request.contextPath}/vitimas">
        <label>Filtrar por Queixa:
            <select name="filterQueixa">
                <option value="">Todas</option>
                <c:forEach items="${queixas}" var="queixa">
                    <option value="${queixa.key}" ${param.filterQueixa eq queixa.key ? 'selected' : ''}>
                        Queixa #${queixa.key} - ${queixa.value}
                    </option>
                </c:forEach>
            </select>
        </label>
        <button type="submit">Filtrar</button>
        <a href="${pageContext.request.contextPath}/vitimas?action=new">Nova Vítima</a>
    </form>
    
    <table>
        <tr>
            <th>ID</th>
            <th>Queixa</th>
            <th>Cidadão</th>
            <th>Tipo</th>
            <th>Ações</th>
        </tr>
        <c:forEach items="${vitimas}" var="vitima">
            <c:choose>
                <c:when test="${vitima.getClass().simpleName eq 'Vitima'}">
                    <!-- Quando é objeto Vitima -->
                    <tr>
                        <td>${vitima.idVitima}</td>
                        <td>Queixa #${vitima.idQueixa}</td>
                        <td>Cidadão #${vitima.idCidadao}</td>
                        <td>${vitima.tipoVitima}</td>
                        <td class="actions">
                            <a href="${pageContext.request.contextPath}/vitimas?action=edit&id=${vitima.idVitima}">Editar</a>
                            <a href="${pageContext.request.contextPath}/vitimas?action=delete&id=${vitima.idVitima}" 
                               onclick="return confirm('Tem certeza?')">Excluir</a>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <!-- Quando é Map -->
                    <tr>
                        <td>${vitima.idVitima}</td>
                        <td>Queixa #${vitima.idQueixa}</td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty vitima.nome}">
                                    ${vitima.nome} (${vitima.idCidadao})
                                </c:when>
                                <c:otherwise>
                                    Cidadão #${vitima.idCidadao}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${vitima.tipoVitima}</td>
                        <td class="actions">
                            <a href="${pageContext.request.contextPath}/vitimas?action=edit&id=${vitima.idVitima}">Editar</a>
                            <a href="${pageContext.request.contextPath}/vitimas?action=delete&id=${vitima.idVitima}" 
                               onclick="return confirm('Tem certeza?')">Excluir</a>
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </table>
</body>
</html>
