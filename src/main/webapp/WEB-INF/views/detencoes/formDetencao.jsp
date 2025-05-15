<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${detencao == null ? 'Nova' : 'Editar'} Detenção</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>${detencao == null ? 'Nova' : 'Editar'} Detenção</h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        
        <form action="detencoes" method="post">
            <input type="hidden" name="id" value="${detencao.idDetencao}">
            
            <div class="mb-3">
                <label for="idCidadao" class="form-label">Cidadão</label>
                <select class="form-select" id="idCidadao" name="idCidadao" required>
                    <option value="">Selecione um cidadão</option>
                    <c:forEach var="cidadao" items="${cidadaos}">
                        <option value="${cidadao.key}" 
                            ${(detencao != null && detencao.idCidadao == Integer.parseInt(cidadao.key)) || 
                              (idCidadaoSelecionado != null && idCidadaoSelecionado == cidadao.key) ? 'selected' : ''}>
                            ${cidadao.value}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="mb-3">
                <label for="motivo" class="form-label">Motivo</label>
                <input type="text" class="form-control" id="motivo" name="motivo" 
                       value="${detencao.motivo}" required>
            </div>
            
            <div class="mb-3">
                <label for="localDetencao" class="form-label">Local da Detenção</label>
                <input type="text" class="form-control" id="localDetencao" name="localDetencao" 
                       value="${detencao.localDetencao}">
            </div>
            
            <div class="mb-3">
                <label for="status" class="form-label">Status</label>
                <select class="form-select" id="status" name="status" required>
                    <option value="">Selecione o status</option>
                    <option value="Detido" ${detencao != null && detencao.status == 'Detido' ? 'selected' : ''}>Detido</option>
                    <option value="Liberado" ${detencao != null && detencao.status == 'Liberado' ? 'selected' : ''}>Liberado</option>
                    <option value="Transferido" ${detencao != null && detencao.status == 'Transferido' ? 'selected' : ''}>Transferido</option>
                </select>
            </div>
            
            <div class="mb-3">
                <label for="idUsuarioResponsavel" class="form-label">Usuário Responsável</label>
                <select class="form-select" id="idUsuarioResponsavel" name="idUsuarioResponsavel" required>
                    <option value="">Selecione o responsável</option>
                    <c:forEach var="usuario" items="${usuarios}">
                        <option value="${usuario.key}" 
                            ${detencao != null && detencao.idUsuarioResponsavel == Integer.parseInt(usuario.key) ? 'selected' : ''}>
                            ${usuario.value}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <button type="submit" class="btn btn-primary">
                ${detencao == null ? 'Adicionar' : 'Atualizar'}
            </button>
            
            <c:choose>
                <c:when test="${not empty idCidadaoSelecionado}">
                    <a href="detencoes?action=listByCidadao&idCidadao=${idCidadaoSelecionado}" class="btn btn-secondary">Cancelar</a>
                </c:when>
                <c:otherwise>
                    <a href="detencoes" class="btn btn-secondary">Cancelar</a>
                </c:otherwise>
            </c:choose>
            
            <input type="hidden" name="action" value="${detencao == null ? 'insert' : 'update'}">
        </form>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
