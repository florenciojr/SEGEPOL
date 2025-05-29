<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Detalhes da Patrulha</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .card {
            border: none;
            box-shadow: 0 0.125rem 0.25rem rgba(0,0,0,0.075);
        }
        .card-header {
            font-weight: 500;
        }
        .info-card {
            border-left: 4px solid #0d6efd;
        }
        .membros-card {
            border-left: 4px solid #198754;
        }
        .observacoes-card {
            border-left: 4px solid #6f42c1;
        }
        .responsavel-card {
            border-left: 4px solid #fd7e14;
        }
        .status-badge {
            font-size: 0.9rem;
            font-weight: 500;
            padding: 0.4em 0.75em;
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
        .membro-item {
            padding: 0.75rem;
            margin-bottom: 0.5rem;
            background-color: white;
            border-radius: 0.25rem;
            border: 1px solid #dee2e6;
            transition: all 0.2s;
        }
        .membro-item:hover {
            background-color: #f8f9fa;
        }
        .modal-content {
            border: none;
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
        }
        .info-label {
            font-weight: 500;
            color: #495057;
        }
        .info-value {
            color: #212529;
        }
        .action-btn {
            min-width: 120px;
        }
        .membros-list {
            max-height: 300px;
            overflow-y: auto;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
        <div class="container">
            <a class="navbar-brand" href="#">
                <i class="fas fa-shield-alt me-2"></i>Detalhes da Patrulha
            </a>
            <div class="navbar-nav">
                <a class="nav-link" href="${pageContext.request.contextPath}/patrulhas">
                    <i class="fas fa-arrow-left me-1"></i>Voltar
                </a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="card shadow-sm mb-4">
            <div class="card-header bg-white d-flex justify-content-between align-items-center py-3">
                <h2 class="h4 mb-0">${patrulha.nome}</h2>
                <span class="badge status-${patrulha.status.toLowerCase().replace(' ', '-')}">
                    ${patrulha.status}
                </span>
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

                <div class="row g-4">
                    <div class="col-md-6">
                        <div class="card h-100 info-card">
                            <div class="card-header bg-white">
                                <h3 class="h5 mb-0"><i class="fas fa-info-circle text-primary me-2"></i>Informações Básicas</h3>
                            </div>
                            <div class="card-body">
                                <div class="mb-3">
                                    <h6 class="info-label">Tipo</h6>
                                    <p class="info-value">${patrulha.tipo}</p>
                                </div>
                                <div class="mb-3">
                                    <h6 class="info-label">Data</h6>
                                    <p class="info-value">${patrulha.data}</p>
                                </div>
                                <div class="mb-3">
                                    <h6 class="info-label">Horário</h6>
                                    <p class="info-value">
                                        ${patrulha.horaInicio}
                                        <c:if test="${not empty patrulha.horaFim}"> - ${patrulha.horaFim}</c:if>
                                    </p>
                                </div>
                                <div>
                                    <h6 class="info-label">Zona de Atuação</h6>
                                    <p class="info-value">${not empty patrulha.zonaAtuacao ? patrulha.zonaAtuacao : 'Não especificada'}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-6">
                        <div class="card h-100 responsavel-card">
                            <div class="card-header bg-white">
                                <h3 class="h5 mb-0"><i class="fas fa-user-shield text-warning me-2"></i>Responsável</h3>
                            </div>
                            <div class="card-body">
                                <div class="d-flex align-items-center">
                                    <div class="flex-grow-1 ms-3">
                                        <c:if test="${not empty usuariosPatrulha[patrulha.responsavelId]}">
                                            <h5 class="mb-1">${usuariosPatrulha[patrulha.responsavelId].nome}</h5>
                                            <p class="text-muted mb-1">${usuariosPatrulha[patrulha.responsavelId].cargo}</p>
                                            <small class="text-muted">ID: ${patrulha.responsavelId}</small>
                                        </c:if>
                                        <c:if test="${empty usuariosPatrulha[patrulha.responsavelId]}">
                                            <div class="alert alert-warning mb-0">
                                                <i class="fas fa-exclamation-triangle me-2"></i>
                                                Usuário não encontrado (ID: ${patrulha.responsavelId})
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-12">
                        <div class="card membros-card">
                            <div class="card-header bg-white d-flex justify-content-between align-items-center">
                                <h3 class="h5 mb-0"><i class="fas fa-users text-success me-2"></i>Membros da Patrulha</h3>
                                <a href="patrulhas?action=gerenciar-membros&id=${patrulha.idPatrulha}" 
                                   class="btn btn-sm btn-outline-success">
                                    <i class="fas fa-user-edit me-1"></i> Gerenciar
                                </a>
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${not empty patrulha.membrosIds}">
                                        <div class="membros-list">
                                            <div class="row g-2">
                                                <c:forEach items="${patrulha.membrosIds}" var="membroId">
                                                    <div class="col-md-6">
                                                        <div class="membro-item">
                                                            <c:if test="${not empty usuariosPatrulha[membroId]}">
                                                                <div class="d-flex justify-content-between align-items-center">
                                                                    <div>
                                                                        <h6 class="mb-1">${usuariosPatrulha[membroId].nome}</h6>
                                                                        <p class="text-muted mb-0">${usuariosPatrulha[membroId].cargo}</p>
                                                                        <small class="text-muted">ID: ${membroId}</small>
                                                                    </div>
                                                                </div>
                                                            </c:if>
                                                            <c:if test="${empty usuariosPatrulha[membroId]}">
                                                                <div class="alert alert-warning mb-0">
                                                                    <i class="fas fa-exclamation-triangle me-2"></i>
                                                                    ID: ${membroId} (Usuário não encontrado)
                                                                </div>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="alert alert-info mb-0">
                                            <i class="fas fa-info-circle me-2"></i>
                                            Nenhum membro adicional nesta patrulha
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-12">
                        <div class="card observacoes-card">
                            <div class="card-header bg-white">
                                <h3 class="h5 mb-0"><i class="fas fa-clipboard text-purple me-2"></i>Observações</h3>
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${not empty patrulha.observacoes}">
                                        ${patrulha.observacoes}
                                    </c:when>
                                    <c:otherwise>
                                        <em class="text-muted">Nenhuma observação registrada</em>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="d-flex justify-content-between mt-4">
                    <div>
                        <a href="patrulhas?action=editar&id=${patrulha.idPatrulha}" class="btn btn-primary">
                            <i class="fas fa-edit me-2"></i> Editar
                        </a>
                    </div>
                    <div>
                        <c:if test="${patrulha.status == 'Planejada'}">
                            <a href="patrulhas?action=iniciar&id=${patrulha.idPatrulha}" 
                               class="btn btn-success" 
                               onclick="return confirm('Deseja iniciar esta patrulha?')">
                                <i class="fas fa-play me-2"></i> Iniciar Patrulha
                            </a>
                        </c:if>
                        
                        <c:if test="${patrulha.status == 'Em Andamento'}">
                            <button type="button" onclick="mostrarModalFinalizar()" class="btn btn-info">
                                <i class="fas fa-stop me-2"></i> Finalizar Patrulha
                            </button>
                            
                            <a href="patrulhas?action=cancelar&id=${patrulha.idPatrulha}" 
                               class="btn btn-danger" 
                               onclick="return confirm('Deseja cancelar esta patrulha?')">
                                <i class="fas fa-ban me-2"></i> Cancelar
                            </a>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Modal para finalizar patrulha -->
    <div class="modal fade" id="modalFinalizar" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><i class="fas fa-stop me-2"></i>Finalizar Patrulha</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="patrulhas?action=finalizar&id=${patrulha.idPatrulha}" method="post">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="horaFim" class="form-label">Hora de Término</label>
                            <input type="time" class="form-control" id="horaFim" name="horaFim" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="relatorio" class="form-label">Relatório Final</label>
                            <textarea class="form-control" id="relatorio" name="relatorio" rows="5" required></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">
                            <i class="fas fa-times me-2"></i>Cancelar
                        </button>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-check me-2"></i>Confirmar Finalização
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function mostrarModalFinalizar() {
            const modal = new bootstrap.Modal(document.getElementById('modalFinalizar'));
            modal.show();
        }
    </script>
</body>
</html>
