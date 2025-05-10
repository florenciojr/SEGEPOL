<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${queixa == null ? 'Nova' : 'Editar'} Queixa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        :root {
            --police-blue: #00477A;
            --police-dark: #002D4A;
            --police-light: #E6F2FF;
            --police-accent: #FFD700;
            --police-red: #D10000;
        }
        
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .form-container {
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
            padding: 2rem;
            margin-top: 2rem;
            border-top: 4px solid var(--police-blue);
        }
        
        .page-title {
            color: var(--police-dark);
            border-bottom: 2px solid var(--police-accent);
            padding-bottom: 0.5rem;
            margin-bottom: 1.5rem;
        }
        
        .btn-police {
            background-color: var(--police-blue);
            color: white;
            border: none;
            padding: 0.5rem 1.5rem;
        }
        
        .btn-police:hover {
            background-color: var(--police-dark);
            color: white;
        }
        
        .form-label {
            font-weight: 600;
            color: var(--police-dark);
        }
        
        .required-field::after {
            content: " *";
            color: var(--police-red);
        }
        
        .badge-police {
            background-color: var(--police-blue);
        }
    </style>
</head>
<body>
    <!-- Inclui o header -->
    <%@include file="/WEB-INF/views/templates/header.jsp" %>

    <div class="container">
        <div class="form-container">
            <h1 class="page-title mb-4">
                <i class="bi bi-clipboard2-pulse"></i> 
                ${queixa == null ? 'Nova Queixa' : 'Editar Queixa'}
                <c:if test="${queixa != null}">
                    <span class="badge badge-police bg-secondary ms-2">ID: ${queixa.idQueixa}</span>
                </c:if>
            </h1>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            
            <form action="queixas" method="post">
                <input type="hidden" name="idQueixa" value="${queixa.idQueixa}">
                <input type="hidden" name="action" value="${queixa == null ? 'insert' : 'update'}">
                
                <div class="row">
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="titulo" class="form-label required-field">Título:</label>
                            <input type="text" class="form-control" id="titulo" name="titulo" 
                                   value="${queixa.titulo}" required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="dataIncidente" class="form-label">Data do Incidente:</label>
                            <input type="date" class="form-control" id="dataIncidente" name="dataIncidente" 
                                   value="${queixa.dataIncidente}">
                        </div>
                    </div>
                </div>
                
                <div class="mb-3">
                    <label for="descricao" class="form-label required-field">Descrição:</label>
                    <textarea class="form-control" id="descricao" name="descricao" rows="5" required>${queixa.descricao}</textarea>
                </div>
                
                <div class="row">
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="localIncidente" class="form-label">Local do Incidente:</label>
                            <input type="text" class="form-control" id="localIncidente" name="localIncidente" 
                                   value="${queixa.localIncidente}">
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="coordenadas" class="form-label">Coordenadas:</label>
                            <input type="text" class="form-control" id="coordenadas" name="coordenadas" 
                                   value="${queixa.coordenadas}" placeholder="Ex: -23.5505, -46.6333">
                        </div>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="idCidadao" class="form-label required-field">Cidadão:</label>
                            <select class="form-select" id="idCidadao" name="idCidadao" required>
                                <option value="">-- Selecione um cidadão --</option>
                                <c:forEach items="${cidadaos}" var="cidadao">
                                    <option value="${cidadao.key}" ${queixa != null && queixa.idCidadao == cidadao.key ? 'selected' : ''}>
                                        ${cidadao.value} (ID: ${cidadao.key})
                                    </option>
                                </c:forEach>
                            </select>
                            <c:if test="${empty cidadaos}">
                                <div class="alert alert-warning mt-2">
                                    <i class="bi bi-exclamation-triangle"></i> Nenhum cidadão cadastrado. 
                                    <a href="cadastroCidadao.jsp" class="alert-link">Cadastrar novo cidadão</a>
                                </div>
                            </c:if>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="idTipo" class="form-label required-field">Tipo de Queixa:</label>
                            <select class="form-select" id="idTipo" name="idTipo" required>
                                <option value="">Selecione um tipo...</option>
                                <c:forEach items="${tiposQueixa}" var="tipo">
                                    <option value="${tipo.idTipo}" ${queixa != null && queixa.idTipo == tipo.idTipo ? 'selected' : ''}>
                                        ${tipo.nomeTipo} (${tipo.gravidade})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="idUsuario" class="form-label required-field">Usuário Responsável:</label>
                            <select class="form-select" id="idUsuario" name="idUsuario" required>
                                <option value="">Selecione um usuário</option>
                                <c:forEach items="${usuarios}" var="usuario">
                                    <option value="${usuario.key}" ${queixa.idUsuario eq usuario.key ? 'selected' : ''}>
                                        ${usuario.value}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <c:if test="${queixa != null}">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="status" class="form-label">Status:</label>
                                <select class="form-select" id="status" name="status">
                                    <c:forEach items="${statusPermitidos}" var="status">
                                        <option value="${status}" ${queixa.status == status ? 'selected' : ''}>${status}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </c:if>
                </div>
                
                <div class="d-flex justify-content-between mt-4">
                    <button type="submit" class="btn btn-police">
                        <i class="bi bi-save"></i> Salvar
                    </button>
                    <a href="queixas" class="btn btn-outline-secondary">
                        <i class="bi bi-x-circle"></i> Cancelar
                    </a>
                </div>
            </form>
        </div>
    </div>

    <!-- Inclui o footer -->
    <%@include file="/WEB-INF/views/templates/footer.jsp" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</body>
</html>
