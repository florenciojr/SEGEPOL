<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${vitima == null ? 'Nova' : 'Editar'} Vítima</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body>
    <div class="container mt-4">
        <h1>${vitima == null ? 'Adicionar' : 'Editar'} Vítima</h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        
        <form action="vitimas" method="post">
            <input type="hidden" name="action" value="${vitima == null ? 'insert' : 'update'}">
            <c:if test="${vitima != null}">
                <input type="hidden" name="id" value="${vitima.idVitima}">
            </c:if>
            <input type="hidden" name="idQueixa" value="${idQueixa}">
            
            <div class="mb-3">
                <label for="idCidadao" class="form-label">Cidadão</label>
                <select class="form-select" id="idCidadao" name="idCidadao" required>
                    <option value="">Selecione um cidadão</option>
                    <c:forEach var="cidadao" items="${cidadaos}">
                        <option value="${cidadao.key}" 
                            ${vitima != null && vitima.idCidadao == Integer.parseInt(cidadao.key) ? 'selected' : ''}>
                            ${cidadao.value}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="mb-3">
                <label for="tipoVitima" class="form-label">Tipo de Vítima</label>
                <select class="form-select" id="tipoVitima" name="tipoVitima" required>
                    <option value="">Selecione o tipo</option>
                    <c:forEach var="tipo" items="${tiposVitima}">
                        <option value="${tipo}" 
                            ${vitima != null && vitima.tipoVitima == tipo ? 'selected' : ''}>
                            ${tipo.descricao}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="mb-3">
                <label for="descricao" class="form-label">Descrição</label>
                <textarea class="form-control" id="descricao" name="descricao" rows="3">${vitima.descricao}</textarea>
            </div>
            
            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                <button type="submit" class="btn btn-primary me-md-2">
                    <i class="bi bi-save"></i> Salvar
                </button>
                <a href="vitimas?action=listByQueixa&idQueixa=${idQueixa}" class="btn btn-secondary">
                    <i class="bi bi-x-circle"></i> Cancelar
                </a>
            </div>
        </form>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
