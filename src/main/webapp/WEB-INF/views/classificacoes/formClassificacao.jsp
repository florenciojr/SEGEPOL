<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${classificacao == null ? 'Nova' : 'Editar'} Classificação</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body>
    <div class="container mt-4">
        <h1>${classificacao == null ? 'Nova' : 'Editar'} Classificação</h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        
        <form action="classificacoes" method="post">
            <input type="hidden" name="id" value="${classificacao.idClassificacao}">
            
            <div class="mb-3">
                <label for="idCidadao" class="form-label">Cidadão</label>
                <select class="form-select" id="idCidadao" name="idCidadao" required>
                    <option value="">Selecione um cidadão</option>
                    <c:forEach var="cidadao" items="${cidadaos}">
                        <option value="${cidadao.key}" 
                            ${(classificacao != null && classificacao.idCidadao == Integer.parseInt(cidadao.key)) || 
                              (idCidadaoSelecionado != null && idCidadaoSelecionado == cidadao.key) ? 'selected' : ''}>
                            ${cidadao.value}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
<!-- Mantenha todo o cabeçalho e estrutura do formulário igual -->

<div class="mb-3">
    <label for="classificacao" class="form-label">Classificação</label>
    <select class="form-select" id="classificacao" name="classificacao" required>
        <option value="">Selecione uma classificação</option>
        <option value="Comum" ${classificacao.classificacao == 'Comum' ? 'selected' : ''}>Cidadão Comum</option>
        <option value="Vitima" ${classificacao.classificacao == 'Vitima' ? 'selected' : ''}>Vitima</option>
        <option value="Suspeito" ${classificacao.classificacao == 'Suspeito' ? 'selected' : ''}>Suspeito</option>
        <option value="Testemunha" ${classificacao.classificacao == 'Testemunha' ? 'selected' : ''}>Testemunha</option>
        <option value="Informante" ${classificacao.classificacao == 'Informante' ? 'selected' : ''}>Informante</option>
        <option value="Denunciante" ${classificacao.classificacao == 'Denunciante' ? 'selected' : ''}>Denunciante</option>
        <option value="Detido" ${classificacao.classificacao == 'Detido' ? 'selected' : ''}>Detido</option>
    </select>
</div>

<!-- Mantenha o restante do formulário igual -->
            
            <div class="mb-3">
                <label for="observacoes" class="form-label">Observações</label>
                <textarea class="form-control" id="observacoes" name="observacoes" rows="4">${classificacao.observacoes}</textarea>
            </div>
            
            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                <button type="submit" class="btn btn-primary me-md-2">
                    <i class="bi bi-save"></i> ${classificacao == null ? 'Adicionar' : 'Atualizar'}
                </button>
                
                <c:choose>
                    <c:when test="${not empty idCidadaoSelecionado}">
                        <a href="classificacoes?action=listByCidadao&idCidadao=${idCidadaoSelecionado}" class="btn btn-secondary">
                            <i class="bi bi-x-circle"></i> Cancelar
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="classificacoes" class="btn btn-secondary">
                            <i class="bi bi-x-circle"></i> Cancelar
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <input type="hidden" name="action" value="${classificacao == null ? 'insert' : 'update'}">
        </form>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
