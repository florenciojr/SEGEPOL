<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Lista de Mandados de Prisão</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/dataTables.bootstrap5.min.css">
    
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #3498db;
        }
        
        .table-header {
            background-color: var(--primary-color);
            color: white;
        }
        
        .badge-status {
            padding: 0.5em 0.75em;
            font-weight: 500;
            font-size: 0.9em;
        }
        
        .action-buttons .btn {
            margin-right: 5px;
            margin-bottom: 5px;
        }
        
        .card-custom {
            border-left: 5px solid var(--secondary-color);
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <div class="container-fluid py-4">
        <div class="card card-custom shadow-sm">
            <div class="card-header table-header">
                <div class="d-flex justify-content-between align-items-center">
                    <h3 class="mb-0">
                        <i class="fas fa-file-alt me-2"></i>Lista de Mandados de Prisão
                    </h3>
                    <div>
                        <a href="${pageContext.request.contextPath}/mandados?action=new" class="btn btn-light btn-sm">
                            <i class="fas fa-plus-circle me-1"></i> Novo Mandado
                        </a>
                    </div>
                </div>
            </div>
            
            <div class="card-body">
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success alert-dismissible fade show">
                        <i class="fas fa-check-circle me-2"></i>${successMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <div class="table-responsive">
                    <table id="tabelaMandados" class="table table-hover table-striped">
                        <thead class="table-light">
                            <tr>
                                <th>Número</th>
                                <th>Suspeito</th>
                                <th>Data Emissão</th>
                                <th>Tipo</th>
                                <th>Status</th>
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
                                        <span class="badge-status badge 
                                            ${mandado.status == 'Ativo' ? 'bg-success' : 
                                              mandado.status == 'Cancelado' ? 'bg-danger' : 'bg-warning'}">
                                            ${mandado.status}
                                        </span>
                                    </td>
                                    <td class="action-buttons">
                                        <a href="${pageContext.request.contextPath}/mandados?action=view&id=${mandado.idMandado}" 
                                           class="btn btn-sm btn-outline-primary" title="Visualizar">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/mandados?action=edit&id=${mandado.idMandado}" 
                                           class="btn btn-sm btn-outline-warning" title="Editar">
                                            <i class="fas fa-edit"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/mandados?action=delete&id=${mandado.idMandado}" 
                                           class="btn btn-sm btn-outline-danger" title="Excluir"
                                           onclick="return confirm('Tem certeza que deseja excluir este mandado?')">
                                            <i class="fas fa-trash-alt"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            
            <div class="card-footer">
                <nav aria-label="Navegação de páginas">
                    <ul class="pagination justify-content-center mb-0">
                        <c:if test="${paginaAtual > 1}">
                            <li class="page-item">
                                <a class="page-link" href="${pageContext.request.contextPath}/mandados?action=list&pagina=${paginaAtual - 1}">
                                    <i class="fas fa-chevron-left"></i>
                                </a>
                            </li>
                        </c:if>
                        
                        <c:forEach begin="1" end="${totalPaginas}" var="i">
                            <li class="page-item ${i == paginaAtual ? 'active' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/mandados?action=list&pagina=${i}">${i}</a>
                            </li>
                        </c:forEach>
                        
                        <c:if test="${paginaAtual < totalPaginas}">
                            <li class="page-item">
                                <a class="page-link" href="${pageContext.request.contextPath}/mandados?action=list&pagina=${paginaAtual + 1}">
                                    <i class="fas fa-chevron-right"></i>
                                </a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </div>
        </div>
    </div>

    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    
    <!-- DataTables -->
    <script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.4/js/dataTables.bootstrap5.min.js"></script>
    
    <!-- Custom Script -->
    <script>
        $(document).ready(function() {
            // Inicializa a DataTable
            $('#tabelaMandados').DataTable({
                language: {
                    url: 'https://cdn.datatables.net/plug-ins/1.13.4/i18n/pt-BR.json'
                },
                dom: '<"top"f>rt<"bottom"lip><"clear">',
                pageLength: 10,
                responsive: true,
                ordering: true,
                order: [[2, 'desc']] // Ordena por data de emissão decrescente
            });
            
            // Tooltips
            $('[title]').tooltip();
        });
    </script>
</body>
</html>
