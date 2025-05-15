<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${empty vitima ? 'Nova' : 'Editar'} Vítima</title>
    <style>
        .error { color: red; }
        form { max-width: 500px; margin: 20px auto; }
        label { display: block; margin-top: 15px; }
        input, select, textarea { width: 100%; padding: 8px; }
        button { margin-top: 20px; padding: 8px 15px; }
        .form-actions { margin-top: 20px; }
    </style>
</head>
<body>
    <h1>${empty vitima ? 'Nova' : 'Editar'} Vítima</h1>
    
    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>
    
    <form method="post" action="${pageContext.request.contextPath}/vitimas?action=${action}">
        <c:if test="${not empty vitima}">
            <input type="hidden" name="id" value="${vitima.idVitima}">
        </c:if>
        
        <label>Queixa:
            <select name="idQueixa" required>
                <option value="">Selecione uma queixa</option>
                <c:forEach items="${queixas}" var="queixa">
                    <option value="${queixa.key}" ${vitima.idQueixa eq queixa.key ? 'selected' : ''}>
                        Queixa #${queixa.key} - ${queixa.value}
                    </option>
                </c:forEach>
            </select>
        </label>
        
        <label>Cidadão:
            <select name="idCidadao" required>
                <option value="">Selecione um cidadão</option>
                <c:forEach items="${cidadaos}" var="cidadao">
                    <option value="${cidadao.key}" ${vitima.idCidadao eq cidadao.key ? 'selected' : ''}>
                        ${cidadao.value} (${cidadao.key})
                    </option>
                </c:forEach>
            </select>
        </label>
        
        <label>Tipo de Vítima:
            <select name="tipoVitima" required>
                <c:forEach items="${tiposVitima}" var="tipo">
                    <option value="${tipo}" ${vitima.tipoVitima eq tipo ? 'selected' : ''}>
                        ${tipo}
                    </option>
                </c:forEach>
            </select>
        </label>
        
        <label>Descrição:
            <textarea name="descricao" rows="4">${vitima.descricao}</textarea>
        </label>
        
        <div class="form-actions">
            <button type="submit">Salvar</button>
            <a href="${pageContext.request.contextPath}/vitimas">Cancelar</a>
        </div>
    </form>
</body>
</html>
