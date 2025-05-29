<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mandados com status: ${statusFiltro}</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        :root {
            --primary-color: #2c3e50;
            --status-color: ${statusFiltro == 'Ativo' ? '#28a745' : 
                            statusFiltro == 'Cancelado' ? '#dc3545' : '#ffc107'};
        }
        
        .status-header {
            background-color: var(--primary-color);
            color: white;
        }
        
        .badge-status {
            background-color: var(--status-color);
            color: ${statusFiltro == 'Ativo' ? 'white' : 'black'};
        }
    </style>
</head>
<body>
    <div class="container py-4">
        <div class="card shadow">
            <div class="card-header status-header">
                <div class="d-flex justify-content-between align-items-center">
                    <h3 class="mb-0">
                        <i class="fas fa-filter me-2"></i>
                        Mandados com status: 
                        <span class="badge badge-status">${statusFiltro}</span>
                    </h3>
                    <a href="${pageContext.request.contextPath}/mandados?action=list" class="btn btn-light">
                        <i class="fas fa-arrow-left me-1"></i> Voltar
                    </a>
                </div>
            </div>
            
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead class="table-light">
                            <tr>
                                <th>Número</th>
                                <th>Suspeito</th>
                                <th>Data Emissão</th>
                                <th>Tipo</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${mandados}" var="mandado">
                                <tr>
                                    <td>${mandado.numeroMandado}</td>
                                    <td>${mandado.suspeito.cidadaoNome}</td>
                                    <td><fmt:formatDate value="${mandado.dataEmissao}" pattern="dd/MM/yyyy"/></td>
                                    <td>${mandado.tipo}</td>
                                    <td>
                                        <div class="btn-group btn-group-sm">
                                            <a href="${pageContext.request.contextPath}/mandados?action=view&id=${mandado.idMandado}" 
                                               class="btn btn-outline-primary" title="Visualizar">
                                                <i class="fas fa-eye"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/mandados?action=edit&id=${mandado.idMandado}" 
                                               class="btn btn-outline-warning" title="Editar">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            
            <div class="card-footer text-muted">
                Total de mandados: ${mandados.size()}
            </div>
        </div>
    </div>

    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Tooltip Activation -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var tooltipTriggerList = [].slice.call(document.querySelectorAll('[title]'));
            var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl);
            });
        });
    </script>
</body>
</html>
