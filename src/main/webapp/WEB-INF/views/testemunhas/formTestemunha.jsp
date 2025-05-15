<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${testemunha == null ? 'Nova' : 'Editar'} Testemunha</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>${testemunha == null ? 'Nova' : 'Editar'} Testemunha</h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        
        <form action="testemunhas" method="post">
            <input type="hidden" name="id" value="${testemunha.idTestemunha}">
            
            <div class="mb-3">
                <label for="idCidadao" class="form-label">Cidadão</label>
                <select class="form-select" id="idCidadao" name="idCidadao" required>
                    <option value="">Selecione um cidadão</option>
                    <c:forEach var="cidadao" items="${cidadaos}">
                        <option value="${cidadao.key}" 
                            ${testemunha != null && testemunha.idCidadao == Integer.parseInt(cidadao.key) ? 'selected' : ''}>
                            ${cidadao.value}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="mb-3">
                <label for="idQueixa" class="form-label">Queixa</label>
                <select class="form-select" id="idQueixa" name="idQueixa" required>
                    <option value="">Selecione uma queixa</option>
                    <c:forEach var="queixa" items="${queixas}">
                        <option value="${queixa.key}" 
                            ${(testemunha != null && testemunha.idQueixa == Integer.parseInt(queixa.key)) || 
                              (idQueixaSelecionado != null && idQueixaSelecionado == queixa.key) ? 'selected' : ''}>
                            ${queixa.value}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="mb-3">
                <label for="tipoTestemunha" class="form-label">Tipo de Testemunha</label>
                <select class="form-select" id="tipoTestemunha" name="tipoTestemunha" required>
                    <option value="">Selecione o tipo</option>
                    <option value="Ocular" ${testemunha != null && testemunha.tipoTestemunha == 'Ocular' ? 'selected' : ''}>Ocular</option>
                    <option value="Auditiva" ${testemunha != null && testemunha.tipoTestemunha == 'Auditiva' ? 'selected' : ''}>Auditiva</option>
                    <option value="Outra" ${testemunha != null && testemunha.tipoTestemunha == 'Outra' ? 'selected' : ''}>Outra</option>
                </select>
            </div>
            
            <div class="mb-3">
                <label for="descricao" class="form-label">Descrição</label>
                <textarea class="form-control" id="descricao" name="descricao" rows="3">${testemunha.descricao}</textarea>
            </div>
            
            <button type="submit" class="btn btn-primary">
                ${testemunha == null ? 'Adicionar' : 'Atualizar'}
            </button>
            
            <c:choose>
                <c:when test="${not empty idQueixaSelecionado}">
                    <a href="testemunhas?action=listByQueixa&idQueixa=${idQueixaSelecionado}" class="btn btn-secondary">Cancelar</a>
                </c:when>
                <c:otherwise>
                    <a href="testemunhas" class="btn btn-secondary">Cancelar</a>
                </c:otherwise>
            </c:choose>
            
            <input type="hidden" name="action" value="${testemunha == null ? 'insert' : 'update'}">
        </form>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
