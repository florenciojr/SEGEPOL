<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Queixas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
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
        
        .police-table {
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
        }
        
        .police-table thead {
            background-color: var(--police-dark) !important;
            color: white;
        }
        
        .police-table th {
            border-bottom: 2px solid var(--police-accent) !important;
        }
        
        .page-title {
            color: var(--police-dark);
            border-bottom: 2px solid var(--police-accent);
            padding-bottom: 0.5rem;
            margin-bottom: 1.5rem;
            display: inline-block;
        }
        
        .btn-police {
            background-color: var(--police-blue);
            color: white;
            border: none;
        }
        
        .btn-police:hover {
            background-color: var(--police-dark);
            color: white;
        }
        
        .status-badge {
            padding: 0.35em 0.65em;
            font-size: 0.75em;
            font-weight: 700;
            border-radius: 0.25rem;
        }
        
        .status-pendente {
            background-color: #fff3cd;
            color: #856404;
        }
        
        .status-resolvido {
            background-color: #d4edda;
            color: #155724;
        }
        
        .status-andamento {
            background-color: #cce5ff;
            color: #004085;
        }
        
        .status-cancelado {
            background-color: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>
    <!-- Inclui o header -->
    <%@include file="/WEB-INF/views/templates/header.jsp" %>

    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1 class="page-title">
                <i class="bi bi-clipboard2-pulse-fill"></i> Lista de Queixas
            </h1>
            <a href="queixas?action=new" class="btn btn-police">
                <i class="bi bi-plus-circle"></i> Nova Queixa
            </a>
        </div>
        
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show">
                <i class="bi bi-check-circle-fill"></i> ${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>
        
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show">
                <i class="bi bi-exclamation-triangle-fill"></i> ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="errorMessage" scope="session"/>
        </c:if>
        
        <div class="table-responsive">
            <table class="table table-hover police-table">
                <thead>
                    <tr>
                        <th><i class="bi bi-fingerprint"></i> ID</th>
                        <th><i class="bi bi-card-heading"></i> Título</th>
                        <th><i class="bi bi-calendar-event"></i> Data Registro</th>
                        <th><i class="bi bi-flag-fill"></i> Status</th>
                        <th><i class="bi bi-activity"></i> Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="queixa" items="${queixas}">
                        <tr>
                            <td>${queixa.idQueixa}</td>
                            <td>${queixa.titulo}</td>
                            <td>${queixa.dataRegistro}</td>
                            <td>
                                <span class="status-badge status-${queixa.status.toLowerCase()}">
                                    ${queixa.status}
                                </span>
                            </td>
                            <td>
                                <div class="btn-group" role="group">
                                    <a href="queixas?action=view&id=${queixa.idQueixa}" 
                                       class="btn btn-sm btn-info"
                                       data-bs-toggle="tooltip" title="Ver detalhes">
                                        <i class="bi bi-eye-fill"></i>
                                    </a>
                                    <a href="queixas?action=edit&id=${queixa.idQueixa}" 
                                       class="btn btn-sm btn-warning"
                                       data-bs-toggle="tooltip" title="Editar">
                                        <i class="bi bi-pencil-fill"></i>
                                    </a>
                                    <a href="queixas?action=delete&id=${queixa.idQueixa}" 
                                       class="btn btn-sm btn-danger"
                                       onclick="return confirm('Tem certeza que deseja excluir esta queixa?')"
                                       data-bs-toggle="tooltip" title="Excluir">
                                        <i class="bi bi-trash-fill"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty queixas}">
                        <tr>
                            <td colspan="5" class="text-center text-muted py-4">
                                <i class="bi bi-database-exclamation" style="font-size: 2rem;"></i>
                                <p class="mt-2">Nenhuma queixa registrada</p>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Inclui o footer -->
    <%@include file="/WEB-INF/views/templates/footer.jsp" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Ativa tooltips
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    </script>
</body>
</html>
