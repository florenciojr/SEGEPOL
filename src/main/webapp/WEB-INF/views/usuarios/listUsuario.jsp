<%-- 
    Document   : listar
    Created on : 2 de mai de 2025, 15:22:35
    Author     : JR5
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
        
        .card-prm {
            border-left: 5px solid var(--police-blue);
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        
        .table-prm {
            border-collapse: separate;
            border-spacing: 0;
        }
        
        .table-prm thead {
            background: linear-gradient(90deg, var(--police-blue), var(--police-dark));
            color: white;
            position: sticky;
            top: 0;
        }
        
        .table-prm thead th {
            border: none;
            padding: 12px 16px;
            font-weight: 500;
            text-transform: uppercase;
            font-size: 0.85rem;
        }
        
        .table-prm tbody tr {
            transition: all 0.2s;
        }
        
        .table-prm tbody tr:hover {
            background-color: rgba(13, 71, 161, 0.05);
        }
        
        .table-prm tbody td {
            padding: 12px 16px;
            vertical-align: middle;
            border-bottom: 1px solid #eee;
        }
        
        .badge-ativo {
            background-color: var(--police-blue);
            color: white;
            padding: 5px 10px;
            border-radius: 4px;
            font-weight: 500;
        }
        
        .badge-inativo {
            background-color: var(--police-gray);
            color: white;
            padding: 5px 10px;
            border-radius: 4px;
            font-weight: 500;
        }
        
        .badge-perfil {
            background-color: var(--police-gray);
            color: white;
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 0.8rem;
        }
        
        .btn-prm {
            background-color: var(--police-blue);
            color: white;
            border: none;
            border-radius: 4px;
            padding: 8px 16px;
            font-weight: 500;
        }
        
        .btn-prm:hover {
            background-color: var(--police-dark);
            color: white;
        }
        
        .user-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            object-fit: cover;
            border: 2px solid var(--police-light);
            margin-right: 10px;
        }
        
        .action-btns .btn {
            width: 35px;
            height: 35px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            margin: 0 2px;
        }
        
        .pagination .page-item.active .page-link {
            background-color: var(--police-blue);
            border-color: var(--police-blue);
        }
        
        .pagination .page-link {
            color: var(--police-blue);
        }
    </style>
</head>
<body>
    <%@include file="/WEB-INF/views/templates/navbar.jsp" %>
    
    <main class="container my-4">
        <div class="card card-prm">
            <div class="card-header d-flex justify-content-between align-items-center" 
                 style="background: linear-gradient(135deg, var(--police-blue), var(--police-dark)); color: white;">
                <h2 class="h5 mb-0">
                    <i class="fas fa-users-shield me-2"></i>GESTÃO DE USUÁRIOS
                </h2>
                <span class="badge bg-light text-dark">
                    <i class="fas fa-user me-1"></i> ${listaUsuarios.size()} registros
                </span>
            </div>
            
            <div class="card-body">
                <!-- Mensagens de feedback -->
                <%@include file="/WEB-INF/views/templates/alertas.jsp" %>
                
                <div class="d-flex justify-content-between mb-4">
                    <a href="${pageContext.request.contextPath}/usuarios?action=novo" 
                       class="btn btn-prm">
                        <i class="fas fa-user-plus me-2"></i> NOVO USUÁRIO
                    </a>
                    
                    <form class="d-flex" action="${pageContext.request.contextPath}/usuarios" method="get">
                        <input type="hidden" name="action" value="buscar">
                        <div class="input-group">
                            <input type="search" class="form-control" name="termo" 
                                   placeholder="Pesquisar por nome, identificação..." required>
                            <button class="btn btn-outline-secondary" type="submit">
                                <i class="fas fa-search"></i>
                            </button>
                        </div>
                    </form>
                </div>

                <c:choose>
                    <c:when test="${empty listaUsuarios}">
                        <div class="alert alert-warning text-center py-5">
                            <i class="fas fa-user-slash fa-3x mb-4 text-muted"></i>
                            <h3 class="h5">NENHUM USUÁRIO ENCONTRADO</h3>
                            <p class="mb-4">Não foram encontrados registros de usuários</p>
                            <a href="${pageContext.request.contextPath}/usuarios" class="btn btn-prm">
                                <i class="fas fa-redo me-2"></i> RECARREGAR
                            </a>
                        </div>
                    </c:when>
                    
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-prm table-hover align-middle">
                                <thead>
                                    <tr>
                                        <th width="80">ID</th>
                                        <th>USUÁRIO</th>
                                        <th>IDENTIFICAÇÃO</th>
                                        <th>CARGO</th>
                                        <th>PERFIL</th>
                                        <th>STATUS</th>
                                        <th width="150" class="text-center">AÇÕES</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${listaUsuarios}" var="usuario">
                                        <tr>
                                            <td class="fw-bold">${usuario.id_usuario}</td>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <c:choose>
                                                        <c:when test="${not empty usuario.foto_perfil}">
                                                            <img src="${pageContext.request.contextPath}/uploads/${usuario.foto_perfil}" 
                                                                 class="user-avatar" 
                                                                 alt="${usuario.nome}">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="user-avatar bg-secondary text-white d-flex align-items-center justify-content-center">
                                                                <i class="fas fa-user"></i>
                                                            </div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <span>${usuario.nome}</span>
                                                </div>
                                            </td>
                                            <td>${usuario.numero_identificacao}</td>
                                            <td>${usuario.cargo}</td>
                                            <td>
                                                <span class="badge-perfil">
                                                    ${usuario.perfil}
                                                </span>
                                            </td>
                                            <td>
                                                <span class="badge ${usuario.status eq 'Ativo' ? 'badge-ativo' : 'badge-inativo'}">
                                                    <i class="fas ${usuario.status eq 'Ativo' ? 'fa-check-circle' : 'fa-times-circle'} me-1"></i>
                                                    ${usuario.status}
                                                </span>
                                            </td>
                                            <td class="action-btns text-center">
                                                <div class="d-flex justify-content-center">
                                                    <a href="${pageContext.request.contextPath}/usuarios?action=visualizar&id=${usuario.id_usuario}" 
                                                       class="btn btn-sm btn-info" 
                                                       data-bs-toggle="tooltip" 
                                                       title="Visualizar">
                                                        <i class="fas fa-eye"></i>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/usuarios?action=editar&id=${usuario.id_usuario}" 
                                                       class="btn btn-sm btn-warning mx-2" 
                                                       data-bs-toggle="tooltip" 
                                                       title="Editar">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                    <c:choose>
                                                        <c:when test="${usuario.status eq 'Ativo'}">
                                                            <a href="${pageContext.request.contextPath}/usuarios?action=desativar&id=${usuario.id_usuario}" 
                                                               class="btn btn-sm btn-danger confirm-action" 
                                                               data-bs-toggle="tooltip" 
                                                               title="Desativar"
                                                               data-message="Tem certeza que deseja desativar este usuário?">
                                                                <i class="fas fa-user-slash"></i>
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="${pageContext.request.contextPath}/usuarios?action=ativar&id=${usuario.id_usuario}" 
                                                               class="btn btn-sm btn-success confirm-action" 
                                                               data-bs-toggle="tooltip" 
                                                               title="Reativar"
                                                               data-message="Tem certeza que deseja reativar este usuário?">
                                                                <i class="fas fa-user-check"></i>
                                                            </a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <!-- Paginação -->
                        <c:if test="${totalPaginas > 1}">
                            <nav aria-label="Paginação" class="mt-4">
                                <ul class="pagination justify-content-center">
                                    <li class="page-item ${paginaAtual == 1 ? 'disabled' : ''}">
                                        <a class="page-link" href="${pageContext.request.contextPath}/usuarios?pagina=${paginaAtual - 1}">
                                            <i class="fas fa-chevron-left"></i>
                                        </a>
                                    </li>
                                    
                                    <c:forEach begin="1" end="${totalPaginas}" var="i">
                                        <li class="page-item ${i == paginaAtual ? 'active' : ''}">
                                            <a class="page-link" href="${pageContext.request.contextPath}/usuarios?pagina=${i}">${i}</a>
                                        </li>
                                    </c:forEach>
                                    
                                    <li class="page-item ${paginaAtual == totalPaginas ? 'disabled' : ''}">
                                        <a class="page-link" href="${pageContext.request.contextPath}/usuarios?pagina=${paginaAtual + 1}">
                                            <i class="fas fa-chevron-right"></i>
                                        </a>
                                    </li>
                                </ul>
                            </nav>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </main>

    <%@include file="/WEB-INF/views/templates/footer.jsp" %>

    <!-- Scripts -->
    <script>
        // Tooltips
        document.addEventListener('DOMContentLoaded', function() {
            const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
            tooltipTriggerList.map(function (tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl);
            });
            
            // Confirmação de ações
            document.querySelectorAll('.confirm-action').forEach(btn => {
                btn.addEventListener('click', function(e) {
                    if (!confirm(this.getAttribute('data-message'))) {
                        e.preventDefault();
                    }
                });
            });
        });
    </script>
</body>
</html>