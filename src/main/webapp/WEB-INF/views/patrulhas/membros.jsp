<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Gerenciar Membros - ${patrulha.nome}</title>
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
        .membros-list {
            max-height: 400px;
            overflow-y: auto;
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
        .btn-remover {
            min-width: 100px;
        }
        .info-box {
            border-left: 4px solid #6c757d;
            background-color: #f8f9fa;
        }
        .responsavel-box {
            border-left: 4px solid #fd7e14;
            background-color: #fff8f1;
        }
        .membros-box {
            border-left: 4px solid #198754;
            background-color: #f1f8f4;
        }
        .adicionar-box {
            border-left: 4px solid #0d6efd;
            background-color: #f0f7ff;
        }
        @media (max-width: 768px) {
            .gerenciamento-container {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
        <div class="container">
            <a class="navbar-brand" href="#">
                <i class="fas fa-users-cog me-2"></i>Gerenciar Membros - ${patrulha.nome}
            </a>
            <div class="navbar-nav">
                <a class="nav-link" href="${pageContext.request.contextPath}/patrulhas?action=detalhes&id=${patrulha.idPatrulha}">
                    <i class="fas fa-arrow-left me-1"></i>Voltar
                </a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="row g-4 gerenciamento-container">
            <div class="col-lg-6">
                <div class="card h-100 responsavel-box">
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
            
            <div class="col-lg-6">
                <div class="card h-100 membros-box">
                    <div class="card-header bg-white d-flex justify-content-between align-items-center">
                        <h3 class="h5 mb-0"><i class="fas fa-users text-success me-2"></i>Membros Atuais</h3>
                        <span class="badge bg-success">${patrulha.membrosIds.size()} membros</span>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty patrulha.membrosIds}">
                                <div class="membros-list">
                                    <c:forEach items="${patrulha.membrosIds}" var="membroId">
                                        <div class="membro-item d-flex justify-content-between align-items-center">
                                            <div>
                                                <c:if test="${not empty usuariosPatrulha[membroId]}">
                                                    <h6 class="mb-1">${usuariosPatrulha[membroId].nome}</h6>
                                                    <p class="text-muted mb-0">${usuariosPatrulha[membroId].cargo}</p>
                                                    <small class="text-muted">ID: ${membroId}</small>
                                                </c:if>
                                                <c:if test="${empty usuariosPatrulha[membroId]}">
                                                    <div class="alert alert-warning mb-0">
                                                        <i class="fas fa-exclamation-triangle me-2"></i>
                                                        ID: ${membroId}
                                                    </div>
                                                </c:if>
                                            </div>
                                            <a href="patrulhas?action=remover-membro&idPatrulha=${patrulha.idPatrulha}&idMembro=${membroId}" 
                                               class="btn btn-sm btn-outline-danger btn-remover" 
                                               onclick="return confirm('Remover este membro da patrulha?')">
                                                <i class="fas fa-user-minus me-1"></i>Remover
                                            </a>
                                        </div>
                                    </c:forEach>
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
            
            <div class="col-lg-12">
                <div class="card adicionar-box">
                    <div class="card-header bg-white">
                        <h3 class="h5 mb-0"><i class="fas fa-user-plus text-primary me-2"></i>Adicionar Novo Membro</h3>
                    </div>
                    <div class="card-body">
                        <form class="row g-3" action="patrulhas?action=adicionar-membro&idPatrulha=${patrulha.idPatrulha}" method="post">
                            <div class="col-md-8">
                                <label for="idMembro" class="form-label">Selecione um usuário</label>
                                <select id="idMembro" name="idMembro" class="form-select" required>
                                    <option value="">-- Selecione um usuário --</option>
                                    <c:forEach items="${usuariosDisponiveis}" var="usuario">
                                        <option value="${usuario.id_usuario}">
                                            ${usuario.nome} (${usuario.cargo})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-4 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-user-plus me-2"></i>Adicionar à Patrulha
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            
            <div class="col-lg-12">
                <div class="card info-box">
                    <div class="card-header bg-white">
                        <h3 class="h5 mb-0"><i class="fas fa-info-circle text-secondary me-2"></i>Informações</h3>
                    </div>
                    <div class="card-body">
                        <ul class="mb-0">
                            <li>O responsável já é automaticamente um membro da patrulha</li>
                            <li>Somente usuários ativos aparecem na lista</li>
                            <li>Para alterar o responsável, edite a patrulha</li>
                            <li>Clique no botão "Remover" para excluir um membro</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
