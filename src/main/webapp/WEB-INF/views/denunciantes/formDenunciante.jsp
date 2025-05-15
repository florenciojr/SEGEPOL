<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Formulário de Denunciante</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>${denunciante == null ? 'Novo' : 'Editar'} Denunciante</h1>
        
        <form action="denunciantes" method="post">
            <input type="hidden" name="id" value="${denunciante.idDenunciante}">
            
            <div class="mb-3">
                <label for="idCidadao" class="form-label">Cidadão</label>
                <select class="form-select" id="idCidadao" name="idCidadao" required>
                    <option value="">Selecione um cidadão</option>
                    <c:forEach var="cidadao" items="${cidadaos}">
                        <option value="${cidadao.key}" ${denunciante != null && denunciante.idCidadao == Integer.parseInt(cidadao.key) ? 'selected' : ''}>
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
                            ${(denunciante != null && denunciante.idQueixa == Integer.parseInt(queixa.key)) || 
                              (idQueixaSelecionado != null && idQueixaSelecionado == queixa.key) ? 'selected' : ''}>
                            ${queixa.value}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="mb-3">
                <label for="modoDenuncia" class="form-label">Modo de Denúncia</label>
                <input type="text" class="form-control" id="modoDenuncia" name="modoDenuncia" 
                       value="${denunciante.modoDenuncia}" required>
            </div>
            
            <button type="submit" class="btn btn-primary">
                ${denunciante == null ? 'Adicionar' : 'Atualizar'}
            </button>
            <a href="denunciantes" class="btn btn-secondary">Cancelar</a>
            
            <input type="hidden" name="action" value="${denunciante == null ? 'insert' : 'update'}">
        </form>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
