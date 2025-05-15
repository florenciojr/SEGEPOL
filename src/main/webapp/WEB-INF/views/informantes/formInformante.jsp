<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${informante == null ? 'Novo' : 'Editar'} Informante</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>${informante == null ? 'Novo' : 'Editar'} Informante</h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        
        <form action="informantes" method="post">
            <input type="hidden" name="id" value="${informante.idInformante}">
            
            <div class="mb-3">
                <label for="idCidadao" class="form-label">Cidadão</label>
                <select class="form-select" id="idCidadao" name="idCidadao" required>
                    <option value="">Selecione um cidadão</option>
                    <c:forEach var="cidadao" items="${cidadaos}">
                        <option value="${cidadao.key}" 
                            ${informante != null && informante.idCidadao == Integer.parseInt(cidadao.key) ? 'selected' : ''}>
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
                            ${(informante != null && informante.idQueixa == Integer.parseInt(queixa.key)) || 
                              (idQueixaSelecionado != null && idQueixaSelecionado == queixa.key) ? 'selected' : ''}>
                            ${queixa.value}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="mb-3">
                <label for="relato" class="form-label">Relato</label>
                <textarea class="form-control" id="relato" name="relato" rows="5" required>${informante.relato}</textarea>
            </div>
            
            <div class="mb-3">
                <label for="confiabilidade" class="form-label">Confiabilidade</label>
                <select class="form-select" id="confiabilidade" name="confiabilidade" required>
                    <option value="">Selecione a confiabilidade</option>
                    <option value="Alta" ${informante != null && informante.confiabilidade == 'Alta' ? 'selected' : ''}>Alta</option>
                    <option value="Média" ${informante != null && informante.confiabilidade == 'Média' ? 'selected' : ''}>Média</option>
                    <option value="Baixa" ${informante != null && informante.confiabilidade == 'Baixa' ? 'selected' : ''}>Baixa</option>
                </select>
            </div>
            
            <div class="mb-3 form-check">
                <input type="checkbox" class="form-check-input" id="anonimato" name="anonimato" 
                       ${informante != null && informante.anonimato ? 'checked' : ''}>
                <label class="form-check-label" for="anonimato">Anonimato</label>
            </div>
            
            <button type="submit" class="btn btn-primary">
                ${informante == null ? 'Adicionar' : 'Atualizar'}
            </button>
            
            <c:choose>
                <c:when test="${not empty idQueixaSelecionado}">
                    <a href="informantes?action=listByQueixa&idQueixa=${idQueixaSelecionado}" class="btn btn-secondary">Cancelar</a>
                </c:when>
                <c:otherwise>
                    <a href="informantes" class="btn btn-secondary">Cancelar</a>
                </c:otherwise>
            </c:choose>
            
            <input type="hidden" name="action" value="${informante == null ? 'insert' : 'update'}">
        </form>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
