<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tipos de Queixa - Gestão Policial</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .badge-leva { background-color: #28a745; }
            .badge-media { background-color: #ffc107; color: #212529; }
            .badge-grave { background-color: #fd7e14; }
            .badge-muito-grave { background-color: #dc3545; }
            .filter-card { background-color: #f8f9fa; }
            .table-responsive { max-height: 500px; overflow-y: auto; }
        </style>
    </head>
    <body>
        <div class="container-fluid mt-4">
            <div class="row mb-4">
                <div class="col">
                    <h1 class="display-6">
                        <i class="bi bi-clipboard2-pulse"></i> Tipos de Queixa Cadastrados
                    </h1>
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}">Início</a></li>
                            <li class="breadcrumb-item active">Tipos de Queixa</li>
                        </ol>
                    </nav>
                </div>
                <div class="col-auto">
                    <a href="tiposqueixa?action=novo" class="btn btn-primary">
                        <i class="bi bi-plus-circle"></i> Novo Tipo
                    </a>
                </div>
            </div>

            <c:if test="${not empty mensagemSucesso}">
                <div class="alert alert-success alert-dismissible fade show">
                    ${mensagemSucesso}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <c:if test="${not empty mensagemErro}">
                <div class="alert alert-danger alert-dismissible fade show">
                    ${mensagemErro}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <div class="card filter-card mb-4">
                <div class="card-body">
                    <form action="tiposqueixa" method="get">
                        <input type="hidden" name="action" value="buscarPorGravidade">
                        
                        <div class="row g-3 align-items-end">
                            <div class="col-md-4">
                                <label for="gravidade" class="form-label">Filtrar por gravidade:</label>
                                <select class="form-select" id="gravidade" name="gravidade" required>
                                    <option value="">Todas as gravidades</option>
                                    <option value="Leve" ${param.gravidade eq 'Leve' ? 'selected' : ''}>Leve</option>
                                    <option value="Média" ${param.gravidade eq 'Média' ? 'selected' : ''}>Média</option>
                                    <option value="Grave" ${param.gravidade eq 'Grave' ? 'selected' : ''}>Grave</option>
                                    <option value="Muito Grave" ${param.gravidade eq 'Muito Grave' ? 'selected' : ''}>Muito Grave</option>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <button type="submit" class="btn btn-outline-primary w-100">
                                    <i class="bi bi-funnel"></i> Filtrar
                                </button>
                            </div>
                            <div class="col-md-2">
                                <a href="tiposqueixa" class="btn btn-outline-secondary w-100">
                                    <i class="bi bi-arrow-counterclockwise"></i> Limpar
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">Lista de Tipos</h5>
                    <span class="badge bg-primary">Total: ${listaTipos.size()}</span>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover table-striped mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th width="5%">ID</th>
                                    <th width="20%">Nome do Tipo</th>
                                    <th width="40%">Descrição</th>
                                    <th width="15%">Gravidade</th>
                                    <th width="20%">Data Cadastro</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty listaTipos}">
                                        <c:forEach var="tipo" items="${listaTipos}">
                                            <tr>
                                                <td>${tipo.idTipo}</td>
                                                <td>${tipo.nomeTipo}</td>
                                                <td>${tipo.descricao}</td>
                                                <td>
                                                    <span class="badge ${tipo.gravidade == 'Leve' ? 'badge-leva' : 
                                                                  tipo.gravidade == 'Média' ? 'badge-media' : 
                                                                  tipo.gravidade == 'Grave' ? 'badge-grave' : 'badge-muito-grave'}">
                                                        ${tipo.gravidade}
                                                    </span>
                                                </td>
                                                <td>${tipo.dataCadastro}</td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="5" class="text-center py-4">
                                                <c:choose>
                                                    <c:when test="${not empty param.gravidade}">
                                                        Nenhum tipo encontrado para a gravidade selecionada
                                                    </c:when>
                                                    <c:otherwise>
                                                        Nenhum tipo de queixa cadastrado
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
        
        <script>
            // Validação do filtro
            document.querySelector('form').addEventListener('submit', function(e) {
                const gravidade = document.getElementById('gravidade').value;
                
                if (!gravidade) {
                    e.preventDefault();
                    alert('Por favor, selecione uma gravidade para filtrar!');
                    return false;
                }
                return true;
            });
        </script>
    </body>
</html>
