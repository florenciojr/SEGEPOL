<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Patrulhas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .navbar-brand {
            font-weight: 600;
        }
        .card {
            border: none;
            box-shadow: 0 0.125rem 0.25rem rgba(0,0,0,0.075);
        }
        .table-responsive {
            overflow-x: auto;
        }
        .table th {
            white-space: nowrap;
            position: relative;
        }
        .status-badge {
            font-size: 0.75rem;
            font-weight: 500;
            padding: 0.35em 0.65em;
            border-radius: 50rem;
        }
        .status-planejada {
            background-color: #fff3cd;
            color: #856404;
        }
        .status-em-andamento {
            background-color: #cce5ff;
            color: #004085;
        }
        .status-concluída {
            background-color: #d4edda;
            color: #155724;
        }
        .status-cancelada {
            background-color: #f8d7da;
            color: #721c24;
        }
        .action-btn {
            width: 32px;
            height: 32px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 0;
        }
        .usuario-info {
            line-height: 1.2;
        }
        .usuario-nome {
            font-weight: 500;
            margin-bottom: 0;
        }
        .usuario-cargo {
            font-size: 0.85rem;
            color: #6c757d;
        }
        .filter-form .form-control, 
        .filter-form .form-select {
            font-size: 0.875rem;
        }
        @media (max-width: 768px) {
            .table-responsive {
                width: 100%;
                margin-bottom: 1rem;
                overflow-y: hidden;
                -ms-overflow-style: -ms-autohiding-scrollbar;
                border: 1px solid #dee2e6;
            }
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
        <div class="container">
            <a class="navbar-brand" href="#">
                <i class="fas fa-shield-alt me-2"></i>Gestão de Patrulhas
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/patrulhas?action=nova">
                            <i class="fas fa-plus-circle me-1"></i>Nova Patrulha
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/">
                            <i class="fas fa-home me-1"></i>Início
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="card shadow-sm mb-4">
            <div class="card-header bg-white d-flex justify-content-between align-items-center py-3">
                <h2 class="h5 mb-0">Patrulhas Cadastradas</h2>
                <div>
                    <span class="badge bg-primary">Total: ${patrulhas.size()}</span>
                </div>
            </div>
            <div class="card-body">
                <c:if test="${not empty mensagem}">
                    <div class="alert alert-success alert-dismissible fade show">
                        ${mensagem}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <c:if test="${not empty erro}">
                    <div class="alert alert-danger alert-dismissible fade show">
                        ${erro}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <form method="get" action="${pageContext.request.contextPath}/patrulhas" class="row g-2 mb-4 filter-form">
                    <input type="hidden" name="action" value="filtrar">
                    <div class="col-md-3">
                        <select name="status" class="form-select">
                            <option value="">Todos status</option>
                            <option value="Planejada" ${param.status == 'Planejada' ? 'selected' : ''}>Planejada</option>
                            <option value="Em Andamento" ${param.status == 'Em Andamento' ? 'selected' : ''}>Em Andamento</option>
                            <option value="Concluída" ${param.status == 'Concluída' ? 'selected' : ''}>Concluída</option>
                            <option value="Cancelada" ${param.status == 'Cancelada' ? 'selected' : ''}>Cancelada</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <input type="date" name="data" value="${param.data}" class="form-control">
                    </div>
                    <div class="col-md-4">
                        <div class="input-group">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-filter me-1"></i>Filtrar
                            </button>
                            <a href="${pageContext.request.contextPath}/patrulhas" class="btn btn-outline-secondary">
                                <i class="fas fa-times me-1"></i>Limpar
                            </a>
                        </div>
                    </div>
                </form>

                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Nome</th>
                                <th>Data</th>
                                <th>Hora</th>
                                <th>Tipo</th>
                                <th>Zona</th>
                                <th>Responsável</th>
                                <th>Status</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${patrulhas}" var="patrulha">
                                <tr>
                                    <td>${patrulha.idPatrulha}</td>
                                    <td>${patrulha.nome}</td>
                                    <td>${patrulha.data}</td>
                                    <td>
                                        ${patrulha.horaInicio}
                                        <c:if test="${not empty patrulha.horaFim}">- ${patrulha.horaFim}</c:if>
                                    </td>
                                    <td>${patrulha.tipo}</td>
                                    <td>${not empty patrulha.zonaAtuacao ? patrulha.zonaAtuacao : '-'}</td>
                                    <td>
                                        <c:if test="${not empty usuariosResponsaveis[patrulha.responsavelId]}">
                                            <div class="usuario-info">
                                                <div class="usuario-nome">${usuariosResponsaveis[patrulha.responsavelId].nome}</div>
                                                <div class="usuario-cargo">${usuariosResponsaveis[patrulha.responsavelId].cargo}</div>
                                            </div>
                                        </c:if>
                                        <c:if test="${empty usuariosResponsaveis[patrulha.responsavelId]}">
                                            <span class="text-muted">ID: ${patrulha.responsavelId}</span>
                                        </c:if>
                                    </td>
                                    <td>
                                        <span class="badge status-${patrulha.status.toLowerCase().replace(' ', '-')}">
                                            ${patrulha.status}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="d-flex gap-1">
                                            <a href="${pageContext.request.contextPath}/patrulhas?action=detalhes&id=${patrulha.idPatrulha}" 
                                               class="btn btn-sm btn-outline-primary action-btn" title="Detalhes">
                                                <i class="fas fa-eye"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/patrulhas?action=editar&id=${patrulha.idPatrulha}" 
                                               class="btn btn-sm btn-outline-secondary action-btn" title="Editar">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <c:if test="${patrulha.status == 'Planejada'}">
                                                <a href="${pageContext.request.contextPath}/patrulhas?action=iniciar&id=${patrulha.idPatrulha}" 
                                                   class="btn btn-sm btn-outline-success action-btn" title="Iniciar"
                                                   onclick="return confirm('Iniciar esta patrulha?')">
                                                    <i class="fas fa-play"></i>
                                                </a>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty patrulhas}">
                                <tr>
                                    <td colspan="9" class="text-center py-4 text-muted">
                                        Nenhuma patrulha encontrada
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
