<%-- 
    Document   : visualizar
    Created on : 2 de mai de 2025, 19:18:50
    Author     : JR5
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Detalhes do Usuário | SEGEPOL</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .user-profile {
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        .profile-img {
            width: 100%;
            max-width: 250px;
            border-radius: 8px;
            border: 3px solid #f8f9fa;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        .detail-card {
            background-color: #f8f9fa;
            border-radius: 8px;
            padding: 20px;
        }
        .detail-label {
            font-weight: 600;
            color: #495057;
        }
        .detail-value {
            color: #212529;
        }
        .status-badge {
            font-size: 0.9rem;
            padding: 5px 10px;
            border-radius: 20px;
        }
    </style>
</head>
<body class="bg-light">
    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <div class="card user-profile mb-4">
                    <div class="card-header bg-primary text-white">
                        <div class="d-flex justify-content-between align-items-center">
                            <h3 class="mb-0">
                                <i class="fas fa-user-circle me-2"></i>Detalhes do Usuário
                            </h3>
                            <a href="usuarios?action=editar&id=${usuario.id_usuario}" 
                               class="btn btn-sm btn-light">
                                <i class="fas fa-edit me-1"></i> Editar
                            </a>
                        </div>
                    </div>
                    
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-4 text-center mb-4 mb-md-0">
                                <c:choose>
                                    <c:when test="${not empty usuario.foto_perfil}">
                                        <img src="${usuario.foto_perfil}" 
                                             class="profile-img mb-3" 
                                             alt="Foto de ${usuario.nome}">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="profile-img bg-secondary text-white d-flex align-items-center justify-content-center">
                                            <i class="fas fa-user fa-5x"></i>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                
                                <h4>${usuario.nome}</h4>
                                <span class="badge ${usuario.status eq 'Ativo' ? 'bg-success' : 'bg-secondary'} status-badge">
                                    ${usuario.status}
                                </span>
                            </div>
                            
                            <div class="col-md-8">
                                <div class="detail-card">
                                    <div class="row g-3">
                                        <div class="col-md-6">
                                            <p class="detail-label"><i class="fas fa-id-card me-2"></i>ID</p>
                                            <p class="detail-value">${usuario.id_usuario}</p>
                                        </div>
                                        
                                        <div class="col-md-6">
                                            <p class="detail-label"><i class="fas fa-envelope me-2"></i>Email</p>
                                            <p class="detail-value">${usuario.email}</p>
                                        </div>
                                        
                                        <div class="col-md-6">
                                            <p class="detail-label"><i class="fas fa-briefcase me-2"></i>Cargo</p>
                                            <p class="detail-value">${usuario.cargo}</p>
                                        </div>
                                        
<div class="col-md-6">
    <p class="detail-label"><i class="fas fa-phone me-2"></i>Telefone</p>
    <p class="detail-value">${usuario.telefone}</p>  <!-- CORRIGIDO -->
</div>
                                        
                                        <div class="col-md-6">
                                            <p class="detail-label"><i class="fas fa-user-tag me-2"></i>Perfil</p>
                                            <p class="detail-value">${usuario.perfil}</p>
                                        </div>
                                        
                                        <div class="col-md-6">
                                            <p class="detail-label"><i class="fas fa-id-badge me-2"></i>Nº Identificação</p>
                                            <p class="detail-value">${usuario.numero_identificacao}</p>
                                        </div>
                                        
                                        <div class="col-md-6">
                                            <p class="detail-label"><i class="fas fa-calendar-plus me-2"></i>Data Cadastro</p>
                                            <p class="detail-value">
                                                <fmt:formatDate value="${usuario.data_cadastro}" pattern="dd/MM/yyyy HH:mm" />
                                            </p>
                                        </div>
                                        
                                        <div class="col-md-6">
                                            <p class="detail-label"><i class="fas fa-calendar-check me-2"></i>Última Atualização</p>
                                            <p class="detail-value">
                                                <fmt:formatDate value="${usuario.data_atualizacao}" pattern="dd/MM/yyyy HH:mm" />
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="card-footer bg-light">
                        <a href="usuarios" class="btn btn-primary">
                            <i class="fas fa-arrow-left me-2"></i> Voltar à Lista
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>