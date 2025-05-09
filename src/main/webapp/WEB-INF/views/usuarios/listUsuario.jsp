<%-- 
    Document   : listar
    Created on : 2 de mai de 2025, 19:14:55
    Author     : JR5
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Gestão de Usuários | SIGEPOL</title>
    <%@include file="/WEB-INF/views/templates/header.jsp" %>
    <style>
        :root {
            --police-blue: #0d47a1;
            --police-dark: #002171;
            --police-light: #e3f2fd;
            --police-accent: #ffab00;
            --police-gray: #455a64;
        }
        
        .page-header {
            border-bottom: 3px solid var(--police-accent);
            padding-bottom: 0.75rem;
            margin-bottom: 2rem;
        }
        
        .card-users {
            border: none;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }
        
        .card-header-users {
            background: linear-gradient(135deg, var(--police-dark), var(--police-blue));
            color: white;
            padding: 1rem 1.5rem;
            border-bottom: 3px solid var(--police-accent);
        }
        
        .table-responsive {
            overflow-x: auto;
        }
        
        .table-users {
            margin-bottom: 0;
        }
        
        .table-users thead th {
            background-color: var(--police-light);
            color: var(--police-blue);
            font-weight: 600;
            border-bottom-width: 2px;
        }
        
        .table-users tbody tr:hover {
            background-color: rgba(13, 71, 161, 0.05);
        }
        
        .user-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            object-fit: cover;
            border: 2px solid var(--police-light);
        }
        
        .badge-status {
            padding: 0.35em 0.65em;
            font-weight: 500;
            border-radius: 4px;
        }
        
        .badge-active {
            background-color: #d1e7dd;
            color: #0f5132;
        }
        
        .badge-inactive {
            background-color: #f8d7da;
            color: #842029;
        }
        
        .badge-suspended {
            background-color: #fff3cd;
            color: #664d03;
        }
        
        .badge-leave {
            background-color: #cfe2ff;
            color: #084298;
        }
        
        .search-box {
            position: relative;
            max-width: 400px;
        }
        
        .search-box .form-control {
            padding-left: 2.5rem;
            border-radius: 20px;
        }
        
        .search-box .search-icon {
            position: absolute;
            left: 1rem;
            top: 50%;
            transform: translateY(-50%);
            color: var(--police-gray);
        }
        
        .btn-police {
            background-color: var(--police-blue);
            color: white;
            border: none;
            padding: 0.5rem 1.25rem;
            border-radius: 4px;
            font-weight: 500;
        }
        
        .btn-police:hover {
            background-color: var(--police-dark);
            color: white;
        }
        
        .btn-outline-police {
            border: 1px solid var(--police-blue);
            color: var(--police-blue);
            background-color: transparent;
        }
        
        .btn-outline-police:hover {
            background-color: var(--police-light);
        }
        
        .pagination .page-item.active .page-link {
            background-color: var(--police-blue);
            border-color: var(--police-blue);
        }
        
        .pagination .page-link {
            color: var(--police-blue);
        }
        
        .action-btns .btn {
            padding: 0.25rem 0.5rem;
            font-size: 0.875rem;
        }
    </style>
</head>
<body>
    <%@include file="/WEB-INF/views/templates/navbar.jsp" %>
    
    <main class="container py-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h1 class="h3 page-header mb-0">
                    <i class="fas fa-users me-2"></i>Gestão de Usuários
                </h1>
            </div>
            <a href="${pageContext.request.contextPath}/usuarios?action=novo" 
               class="btn btn-police">
                <i class="fas fa-user-plus me-2"></i>Novo Usuário
            </a>
        </div>
        
        <!-- Mensagens de feedback -->
        <c:if test="${not empty sucesso}">
            <div class="alert alert-success alert-dismissible fade show mb-4">
                <i class="fas fa-check-circle me-2"></i>${sucesso}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty erro}">
            <div class="alert alert-danger alert-dismissible fade show mb-4">
                <i class="fas fa-exclamation-circle me-2"></i>${erro}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty aviso}">
            <div class="alert alert-warning alert-dismissible fade show mb-4">
                <i class="fas fa-info-circle me-2"></i>${aviso}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Card de listagem -->
        <div class="card card-users">
            <div class="card-header card-header-users d-flex justify-content-between align-items-center">
                <h2 class="h5 mb-0">
                    <i class="fas fa-list me-2"></i>Lista de Usuários
                </h2>
                
                <form action="${pageContext.request.contextPath}/usuarios" method="get" class="search-box">
                    <input type="hidden" name="action" value="buscar">
                    <i class="fas fa-search search-icon"></i>
                    <input type="text" name="termo" class="form-control" placeholder="Pesquisar..." 
                           value="${termoBusca}">
                </form>
            </div>
            
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-users table-hover align-middle">
                        <thead>
                            <tr>
                                <th width="50px"></th>
                                <th>Nome</th>
                                <th>Cargo</th>
                                <th>Perfil</th>
                                <th>Status</th>
                                <th>Último Acesso</th>
                                <th width="150px" class="text-center">Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty usuarios}">
                                    <c:forEach items="${usuarios}" var="usuario">
                                        <tr>
                                            <td>
                                                <img src="${not empty usuario.foto_perfil ? 
                                                      pageContext.request.contextPath.concat('/uploads/').concat(usuario.foto_perfil) : 
                                                      'https://via.placeholder.com/150?text=Sem+Foto'}" 
                                                     class="user-avatar">
                                            </td>
                                            <td>
                                                <strong>${usuario.nome}</strong><br>
                                                <small class="text-muted">${usuario.numero_identificacao}</small>
                                            </td>
                                            <td>${usuario.cargo}</td>
                                            <td>${usuario.perfil}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${usuario.status == 'Ativo'}">
                                                        <span class="badge badge-status badge-active">
                                                            <i class="fas fa-check-circle me-1"></i>Ativo
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${usuario.status == 'Inativo'}">
                                                        <span class="badge badge-status badge-inactive">
                                                            <i class="fas fa-times-circle me-1"></i>Inativo
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${usuario.status == 'Suspenso'}">
                                                        <span class="badge badge-status badge-suspended">
                                                            <i class="fas fa-exclamation-triangle me-1"></i>Suspenso
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${usuario.status == 'Licença'}">
                                                        <span class="badge badge-status badge-leave">
                                                            <i class="fas fa-umbrella-beach me-1"></i>Licença
                                                        </span>
                                                    </c:when>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:if test="${not empty usuario.ultimo_login}">
                                                    <small>${usuario.ultimo_login}</small>
                                                </c:if>
                                                <c:if test="${empty usuario.ultimo_login}">
                                                    <small class="text-muted">Nunca acessou</small>
                                                </c:if>
                                            </td>
                                            <td class="text-center action-btns">
                                                <div class="btn-group" role="group">
                                                    <a href="${pageContext.request.contextPath}/usuarios?action=visualizar&id=${usuario.id_usuario}" 
                                                       class="btn btn-sm btn-outline-police" title="Visualizar">
                                                        <i class="fas fa-eye"></i>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/usuarios?action=editar&id=${usuario.id_usuario}" 
                                                       class="btn btn-sm btn-outline-police" title="Editar">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                    <c:choose>
                                                        <c:when test="${usuario.status == 'Ativo'}">
                                                            <a href="${pageContext.request.contextPath}/usuarios?action=desativar&id=${usuario.id_usuario}" 
                                                               class="btn btn-sm btn-outline-police" title="Desativar">
                                                                <i class="fas fa-user-slash"></i>
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="${pageContext.request.contextPath}/usuarios?action=ativar&id=${usuario.id_usuario}" 
                                                               class="btn btn-sm btn-outline-police" title="Ativar">
                                                                <i class="fas fa-user-check"></i>
                                                            </a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="7" class="text-center py-4">
                                            <i class="fas fa-user-times fa-2x mb-3" style="color: #e3f2fd;"></i>
                                            <h5 class="text-muted">Nenhum usuário encontrado</h5>
                                            <c:if test="${not empty termoBusca}">
                                                <a href="${pageContext.request.contextPath}/usuarios" 
                                                   class="btn btn-outline-police btn-sm mt-2">
                                                    Limpar busca
                                                </a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
                
                <!-- Paginação -->
                <c:if test="${totalPaginas > 1}">
                    <nav aria-label="Paginação" class="mt-4">
                        <ul class="pagination justify-content-center">
                            <li class="page-item ${paginaAtual == 1 ? 'disabled' : ''}">
                                <a class="page-link" 
                                   href="${pageContext.request.contextPath}/usuarios?action=listar&pagina=${paginaAtual - 1}">
                                    Anterior
                                </a>
                            </li>
                            
                            <c:forEach begin="1" end="${totalPaginas}" var="i">
                                <li class="page-item ${paginaAtual == i ? 'active' : ''}">
                                    <a class="page-link" 
                                       href="${pageContext.request.contextPath}/usuarios?action=listar&pagina=${i}">
                                        ${i}
                                    </a>
                                </li>
                            </c:forEach>
                            
                            <li class="page-item ${paginaAtual == totalPaginas ? 'disabled' : ''}">
                                <a class="page-link" 
                                   href="${pageContext.request.contextPath}/usuarios?action=listar&pagina=${paginaAtual + 1}">
                                    Próxima
                                </a>
                            </li>
                        </ul>
                    </nav>
                </c:if>
            </div>
        </div>
    </main>

    <%@include file="/WEB-INF/views/templates/footer.jsp" %>

    <!-- Script para confirmação de ações -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Confirmação para desativar/ativar usuários
            const actionLinks = document.querySelectorAll('a[title="Desativar"], a[title="Ativar"]');
            actionLinks.forEach(link => {
                link.addEventListener('click', function(e) {
                    const action = this.title.toLowerCase();
                    if (!confirm(`Tem certeza que deseja ${action} este usuário?`)) {
                        e.preventDefault();
                    }
                });
            });
            
            // Tooltips para botões de ação
            const tooltipTriggerList = [].slice.call(document.querySelectorAll('[title]'));
            tooltipTriggerList.map(function (element) {
                return new bootstrap.Tooltip(element);
            });
        });
    </script>
</body>
</html>
