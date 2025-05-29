<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Detalhes do Mandado - ${mandado.numeroMandado}</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #3498db;
        }
        
        .card-header-custom {
            background-color: var(--primary-color);
            color: white;
        }
        
        .info-label {
            font-weight: 600;
            color: var(--primary-color);
        }
        
        .badge-status {
            padding: 0.5em 0.75em;
            font-weight: 500;
        }
    </style>
</head>
<body>
    <div class="container py-4">
        <div class="card shadow">
            <div class="card-header card-header-custom py-3">
                <div class="d-flex justify-content-between align-items-center">
                    <h4 class="mb-0">
                        <i class="fas fa-file-contract me-2"></i>
                        Mandado ${mandado.numeroMandado}
                    </h4>
                    <a href="${pageContext.request.contextPath}/mandados?action=list" class="btn btn-light btn-sm">
                        <i class="fas fa-arrow-left me-1"></i> Voltar
                    </a>
                </div>
            </div>
            
            <div class="card-body">
                <div class="row mb-4">
                    <div class="col-md-6">
                        <div class="card mb-3">
                            <div class="card-header bg-light">
                                <i class="fas fa-info-circle me-1"></i> Informações Básicas
                            </div>
                            <div class="card-body">
                                <div class="mb-2">
                                    <span class="info-label">Número:</span>
                                    <span>${mandado.numeroMandado}</span>
                                </div>
                                <div class="mb-2">
                                    <span class="info-label">Tipo:</span>
                                    <span>${mandado.tipo}</span>
                                </div>
                                <div class="mb-2">
                                    <span class="info-label">Status:</span>
                                    <span class="badge-status badge 
                                        ${mandado.status == 'Ativo' ? 'bg-success' : 
                                          mandado.status == 'Cancelado' ? 'bg-danger' : 'bg-warning'}">
                                        ${mandado.status}
                                    </span>
                                </div>
                                <div class="mb-2">
                                    <span class="info-label">Data de Emissão:</span>
                                    <span><fmt:formatDate value="${mandado.dataEmissao}" pattern="dd/MM/yyyy"/></span>
                                </div>
                                <div>
                                    <span class="info-label">Data de Validade:</span>
                                    <span>
                                        <c:choose>
                                            <c:when test="${mandado.dataValidade != null}">
                                                <fmt:formatDate value="${mandado.dataValidade}" pattern="dd/MM/yyyy"/>
                                            </c:when>
                                            <c:otherwise>
                                                Não especificada
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-6">
                        <div class="card mb-3">
                            <div class="card-header bg-light">
                                <i class="fas fa-users me-1"></i> Envolvidos
                            </div>
                            <div class="card-body">
                                <div class="mb-3">
                                    <span class="info-label">Suspeito:</span>
                                    <div class="mt-1 p-2 bg-light rounded">
                                        <strong>${mandado.suspeito.cidadaoNome}</strong><br>
                                        <small class="text-muted">ID: ${mandado.suspeito.idSuspeito}</small>
                                    </div>
                                </div>
                                <div>
                                    <span class="info-label">Emitido por:</span>
                                    <div class="mt-1 p-2 bg-light rounded">
                                        <strong>${sessionScope.usuarioNome}</strong><br>
                                        <small class="text-muted">ID: ${mandado.idUsuarioEmissor}</small>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="card mb-3">
                    <div class="card-header bg-light">
                        <i class="fas fa-align-left me-1"></i> Descrição
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty mandado.descricao}">
                                ${mandado.descricao}
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted">Nenhuma descrição fornecida</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                
                <div class="card">
                    <div class="card-header bg-light">
                        <i class="fas fa-history me-1"></i> Histórico
                    </div>
                    <div class="card-body">
                        <div class="timeline">
                            <div class="timeline-item">
                                <div class="timeline-point"></div>
                                <div class="timeline-content">
                                    <div class="timeline-date"><fmt:formatDate value="${mandado.dataEmissao}" pattern="dd/MM/yyyy HH:mm"/></div>
                                    <h6 class="mb-1">Mandado criado</h6>
                                    <p class="text-muted mb-0">Por: ${sessionScope.usuarioNome}</p>
                                </div>
                            </div>
                            <!-- Itens adicionais do histórico podem ser adicionados aqui -->
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="card-footer text-end">
                <a href="${pageContext.request.contextPath}/mandados?action=edit&id=${mandado.idMandado}" 
                   class="btn btn-primary me-2">
                    <i class="fas fa-edit me-1"></i> Editar
                </a>
                <a href="${pageContext.request.contextPath}/mandados?action=list" 
                   class="btn btn-outline-secondary">
                    <i class="fas fa-list me-1"></i> Voltar para Lista
                </a>
            </div>
        </div>
    </div>

    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom Style for Timeline -->
    <style>
        .timeline {
            position: relative;
            padding-left: 1.5rem;
        }
        
        .timeline-item {
            position: relative;
            padding-bottom: 1.5rem;
        }
        
        .timeline-point {
            position: absolute;
            left: -0.5rem;
            top: 0.25rem;
            width: 1rem;
            height: 1rem;
            border-radius: 50%;
            background-color: var(--secondary-color);
        }
        
        .timeline-content {
            padding-left: 1rem;
        }
        
        .timeline-date {
            font-size: 0.8rem;
            color: #6c757d;
        }
    </style>
</body>
</html>
