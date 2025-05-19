<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${action == 'create' ? 'Nova' : 'Editar'} Intimação</title>
    <style>
        .form-group { margin-bottom: 15px; }
        label { display: inline-block; width: 150px; }
        input, select, textarea { width: 300px; padding: 8px; }
        textarea { height: 100px; }
        .error { color: red; }
    </style>
</head>
<body>
    <h1>${action == 'create' ? 'Nova' : 'Editar'} Intimação</h1>
    
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>
    
    <form action="intimacoes?action=${action}" method="post">
        <c:if test="${action == 'update'}">
            <input type="hidden" name="id" value="${intimacao.idIntimacao}">
        </c:if>
        
        <div class="form-group">
            <label for="idCidadao">Cidadão:</label>
            <select name="idCidadao" required>
                <option value="">Selecione um cidadão</option>
                <c:forEach items="${cidadaos}" var="cidadao">
                    <option value="${cidadao.key}" ${intimacao.idCidadao == cidadao.key ? 'selected' : ''}>
                        ${cidadao.value}
                    </option>
                </c:forEach>
            </select>
        </div>
        
        <div class="form-group">
            <label for="idQueixa">Queixa:</label>
            <select name="idQueixa" required>
                <option value="">Selecione uma queixa</option>
                <c:forEach items="${queixas}" var="queixa">
                    <option value="${queixa.key}" ${intimacao.idQueixa == queixa.key ? 'selected' : ''}>
                        ${queixa.value}
                    </option>
                </c:forEach>
            </select>
        </div>
        
        <div class="form-group">
            <label for="motivo">Motivo:</label>
            <textarea name="motivo" required>${intimacao.motivo}</textarea>
        </div>
        
        <div class="form-group">
            <label for="dataEmissao">Data Emissão:</label>
            <input type="date" name="dataEmissao" value="${intimacao.dataEmissao}" required>
        </div>
        
        <div class="form-group">
            <label for="dataComparecimento">Data Comparecimento:</label>
            <input type="date" name="dataComparecimento" value="${intimacao.dataComparecimento}" required>
        </div>
        
        <div class="form-group">
            <label for="localComparecimento">Local:</label>
            <input type="text" name="localComparecimento" value="${intimacao.localComparecimento}" required>
        </div>
        
        <div class="form-group">
            <label for="status">Status:</label>
            <select name="status" required>
                <option value="">Selecione um status</option>
                <c:forEach items="${statusPermitidos}" var="status">
                    <option value="${status}" ${intimacao.status == status ? 'selected' : ''}>
                        ${status}
                    </option>
                </c:forEach>
            </select>
        </div>
        
        <div class="form-group">
            <label for="observacoes">Observações:</label>
            <textarea name="observacoes">${intimacao.observacoes}</textarea>
        </div>
        
        <input type="hidden" name="idUsuario" value="1"> <!-- Em produção, pegar da sessão -->
        
        <div class="form-group">
            <input type="submit" value="Salvar">
            <a href="intimacoes?action=list">Cancelar</a>
        </div>
    </form>
</body>
</html>
