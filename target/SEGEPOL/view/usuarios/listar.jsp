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
    <title>Lista de Usuários</title>
 <script>
    // Redirecionamento imediato sem delay desnecessário
    window.location.href = "${pageContext.request.contextPath}/usuarios";
</script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .table-responsive {
            overflow-x: auto;
        }
        .status-ativo {
            color: #28a745;
        }
        .status-inativo {
            color: #6c757d;
        }
    </style>
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="card shadow">
            <div class="card-header bg-primary text-white">
                <h1 class="h4 mb-0">
                    <i class="fas fa-users me-2"></i>Lista de Usuários
                </h1>
            </div>
            
            <div class="card-body">
                <!-- Mensagens de feedback -->
                <c:if test="${not empty param.sucesso}">
                    <div class="alert alert-success alert-dismissible fade show">
                        <i class="fas fa-check-circle me-2"></i>${param.sucesso}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>
                
                <c:if test="${not empty param.erro}">
                    <div class="alert alert-danger alert-dismissible fade show">
                        <i class="fas fa-exclamation-circle me-2"></i>${param.erro}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <!-- Bloco principal de verificação -->
                <c:choose>
                    <c:when test="${empty listaUsuarios}">
                        <div class="alert alert-warning text-center py-5">
                            <i class="fas fa-exclamation-triangle fa-3x mb-4"></i>
                            <h2 class="h4">A lista de usuários não foi carregada corretamente</h2>
                            <p class="mb-4">Por favor, tente novamente ou verifique a conexão com o banco de dados.</p>
                            <a href="${pageContext.request.contextPath}/usuarios" class="btn btn-primary">
                                <i class="fas fa-sync-alt me-2"></i>Recarregar Lista
                            </a>
                        </div>
                    </c:when>
                    
                    <c:otherwise>
                        <!-- Conteúdo quando a lista está carregada -->
                        <div class="d-flex justify-content-between mb-4">
                            <a href="${pageContext.request.contextPath}/usuarios?action=novo" 
                               class="btn btn-primary">
                                <i class="fas fa-user-plus me-2"></i>Novo Usuário
                            </a>
                            
                            <span class="text-muted align-self-center">
                                Total: ${listaUsuarios.size()} usuário(s)
                            </span>
                        </div>

                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>ID</th>
                                        <th>Nome</th>
                                        <th>Email</th>
                                        <th>Cargo</th>
                                        <th>Status</th>
                                        <th>Ações</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${listaUsuarios}" var="usuario">
                                        <tr>
                                            <td>${usuario.id_usuario}</td>
                                            <td>${usuario.nome}</td>
                                            <td>${usuario.email}</td>
                                            <td>${usuario.cargo}</td>
                                            <td>
                                                <span class="badge ${usuario.status eq 'Ativo' ? 'bg-success' : 'bg-secondary'}">
                                                    ${usuario.status}
                                                </span>
                                            </td>
                                            <td>
                                                <div class="btn-group btn-group-sm">
                                                    <a href="${pageContext.request.contextPath}/usuarios?action=visualizar&id=${usuario.id_usuario}" 
                                                       class="btn btn-info" title="Visualizar">
                                                        <i class="fas fa-eye"></i>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/usuarios?action=editar&id=${usuario.id_usuario}" 
                                                       class="btn btn-warning" title="Editar">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                    <c:choose>
                                                        <c:when test="${usuario.status eq 'Ativo'}">
                                                            <a href="${pageContext.request.contextPath}/usuarios?action=desativar&id=${usuario.id_usuario}" 
                                                               class="btn btn-danger" title="Desativar">
                                                                <i class="fas fa-user-slash"></i>
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="${pageContext.request.contextPath}/usuarios?action=reativar&id=${usuario.id_usuario}" 
                                                               class="btn btn-success" title="Reativar">
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
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Confirmação para ações críticas
        document.querySelectorAll('a[href*="desativar"], a[href*="reativar"]').forEach(link => {
            link.addEventListener('click', function(e) {
                const action = this.getAttribute('title').toLowerCase();
                if (!confirm(`Tem certeza que deseja ${action} este usuário?`)) {
                    e.preventDefault();
                }
            });
        });
    </script>
</body>
</html>