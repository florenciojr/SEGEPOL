<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Suspeitos | Sistema Policial</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome para ícones -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .navbar-brand {
            font-weight: bold;
        }
        .card {
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            border: none;
        }
        .card-header {
            background-color: #343a40;
            color: white;
            border-radius: 10px 10px 0 0 !important;
            padding: 15px 20px;
        }
        .table th {
            background-color: #495057;
            color: white;
            border-bottom: none;
        }
        .table td {
            vertical-align: middle;
        }
        .badge {
            font-size: 0.85em;
            padding: 5px 10px;
            border-radius: 50px;
        }
        .badge-primary {
            background-color: #007bff;
        }
        .badge-secondary {
            background-color: #6c757d;
        }
        .badge-success {
            background-color: #28a745;
        }
        .badge-danger {
            background-color: #dc3545;
        }
        .badge-warning {
            background-color: #ffc107;
            color: #212529;
        }
        .btn-action {
            padding: 5px 10px;
            font-size: 0.85rem;
            margin-right: 5px;
        }
        .principal {
            color: #dc3545;
            font-weight: bold;
        }
        .cumplice {
            color: #fd7e14;
            font-weight: bold;
        }
        .acessorio {
            color: #6c757d;
            font-weight: bold;
        }
        .testemunha {
            color: #28a745;
            font-weight: bold;
        }
        .no-description {
            color: #6c757d;
            font-style: italic;
        }
    </style>
</head>
<body>
    <!-- Inclui o header -->
    <%@include file="/WEB-INF/views/templates/header.jsp" %>

    <div class="container">
        <div class="card mb-4">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0"><i class="fas fa-user-secret me-2"></i> Lista de Suspeitos</h5>
                <div>
                    <c:if test="${not empty idQueixa}">
                        <a href="queixas?action=list" class="btn btn-sm btn-outline-light">
                            <i class="fas fa-arrow-left me-1"></i> Voltar para Queixas
                        </a>
                    </c:if>
                    <a href="suspeitos?action=new${not empty idQueixa ? '&idQueixa=' += idQueixa : ''}" 
                       class="btn btn-sm btn-success ms-2">
                        <i class="fas fa-plus me-1"></i> ${not empty idQueixa ? 'Adicionar à Queixa' : 'Novo Suspeito'}
                    </a>
                </div>
            </div>
            <div class="card-body">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>
                
                <c:if test="${not empty success}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${success}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th width="5%">ID</th>
                                <th width="20%">Queixa</th>
                                <th width="20%">Cidadão</th>
                                <th width="25%">Descrição</th>
                                <th width="15%">Papel</th>
                                <th width="10%">Registro</th>
                                <th width="5%" class="text-center">Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${listSuspeitos}" var="suspeito">
                                <tr>
                                    <td class="fw-bold">#${suspeito.idSuspeito}</td>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <span class="badge bg-primary me-2">Q#${suspeito.idQueixa}</span>
                                            <c:choose>
                                                <c:when test="${not empty suspeito.queixaDescricao}">
                                                    <span class="text-truncate" style="max-width: 200px;" 
                                                          title="${suspeito.queixaDescricao}">
                                                        ${suspeito.queixaDescricao}
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Sem descrição</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty suspeito.cidadaoNome}">
                                                <div class="d-flex align-items-center">
                                                    <span class="badge bg-success me-2">C#${suspeito.idCidadao}</span>
                                                    <span>${suspeito.cidadaoNome}</span>
                                                </div>
                                            </c:when>
                                            <c:when test="${suspeito.idCidadao != null}">
                                                <div class="d-flex align-items-center">
                                                    <span class="badge bg-warning me-2">C#${suspeito.idCidadao}</span>
                                                    <span class="text-muted">Não cadastrado</span>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-danger">Não identificado</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty suspeito.descricao}">
                                                <span class="d-inline-block text-truncate" style="max-width: 200px;" 
                                                      title="${suspeito.descricao}">
                                                    ${suspeito.descricao}
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="no-description">Sem descrição</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${suspeito.papelIncidente == 'Principal'}">
                                                <span class="principal">
                                                    <i class="fas fa-user-shield me-1"></i>${suspeito.papelIncidente}
                                                </span>
                                            </c:when>
                                            <c:when test="${suspeito.papelIncidente == 'Cúmplice'}">
                                                <span class="cumplice">
                                                    <i class="fas fa-user-friends me-1"></i>${suspeito.papelIncidente}
                                                </span>
                                            </c:when>
                                            <c:when test="${suspeito.papelIncidente == 'Acessório'}">
                                                <span class="acessorio">
                                                    <i class="fas fa-user me-1"></i>${suspeito.papelIncidente}
                                                </span>
                                            </c:when>
                                            <c:when test="${suspeito.papelIncidente == 'Testemunha'}">
                                                <span class="testemunha">
                                                    <i class="fas fa-eye me-1"></i>${suspeito.papelIncidente}
                                                </span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <small class="text-muted">${suspeito.dataRegistro}</small>
                                    </td>
                                    <td class="text-center">
                                        <div class="d-flex justify-content-center">
                                            <a href="suspeitos?action=edit&id=${suspeito.idSuspeito}" 
                                               class="btn btn-sm btn-primary btn-action me-1"
                                               title="Editar">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <a href="suspeitos?action=delete&id=${suspeito.idSuspeito}&idQueixa=${suspeito.idQueixa}" 
                                               class="btn btn-sm btn-danger btn-action"
                                               title="Excluir"
                                               onclick="return confirm('Tem certeza que deseja excluir este suspeito?')">
                                                <i class="fas fa-trash-alt"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty listSuspeitos}">
                                <tr>
                                    <td colspan="7" class="text-center text-muted py-4">
                                        <i class="fas fa-user-slash fa-2x mb-2"></i>
                                        <p class="mb-0">Nenhum suspeito cadastrado</p>
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Inclui o footer -->
    <%@include file="/WEB-INF/views/templates/footer.jsp" %>

    <!-- Bootstrap JS e dependências -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Função para confirmar exclusão
        function confirmarExclusao(event) {
            if (!confirm('Tem certeza que deseja excluir este suspeito?')) {
                event.preventDefault();
            }
        }
        
        // Adiciona o evento de confirmação a todos os links de exclusão
        document.querySelectorAll('a[onclick]').forEach(link => {
            link.addEventListener('click', confirmarExclusao);
        });
    </script>
</body>
</html>
