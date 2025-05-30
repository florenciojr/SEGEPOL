<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Patrulhas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <style>
        body {
            background-color: #f8f9fa;
            padding-top: 20px;
        }
        .navbar-brand {
            font-weight: 600;
        }
        .card {
            border: none;
            box-shadow: 0 0.25rem 0.75rem rgba(0,0,0,0.1);
            transition: all 0.3s ease;
        }
        .card:hover {
            box-shadow: 0 0.5rem 1.5rem rgba(0,0,0,0.15);
        }
        .table-responsive {
            overflow-x: auto;
        }
        .table th {
            white-space: nowrap;
            position: relative;
            background-color: #f8f9fa;
        }
        .status-badge {
            font-size: 0.75rem;
            font-weight: 500;
            padding: 0.35em 0.65em;
            border-radius: 50rem;
            text-transform: capitalize;
        }
        .status-planejada {
            background-color: #fff3cd;
            color: #856404;
        }
        .status-em-andamento {
            background-color: #cce5ff;
            color: #004085;
        }
        .status-concluida {
            background-color: #d4edda;
            color: #155724;
        }
        .status-cancelada {
            background-color: #f8d7da;
            color: #721c24;
        }
        .action-btn {
            width: 32px;
            height: 32px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 0;
            transition: all 0.2s;
        }
        .action-btn:hover {
            transform: scale(1.1);
        }
        .usuario-info {
            line-height: 1.2;
        }
        .usuario-nome {
            font-weight: 500;
            margin-bottom: 0;
        }
        .usuario-cargo {
            font-size: 0.85rem;
            color: #6c757d;
        }
        .filter-form .form-control, 
        .filter-form .form-select {
            font-size: 0.875rem;
        }
        .badge-count {
            font-size: 0.8rem;
            position: relative;
            top: -1px;
        }
        .table-hover tbody tr {
            transition: all 0.2s;
        }
        .table-hover tbody tr:hover {
            background-color: rgba(0,123,255,0.05);
            transform: translateX(2px);
        }
        .sortable {
            cursor: pointer;
        }
        .sortable:hover {
            background-color: #f1f1f1;
        }
        .sort-icon {
            margin-left: 5px;
            opacity: 0.5;
        }
        @media (max-width: 768px) {
            .table-responsive {
                width: 100%;
                margin-bottom: 1rem;
                overflow-y: hidden;
                border: 1px solid #dee2e6;
            }
            .action-btns {
                display: flex;
                flex-wrap: wrap;
                gap: 5px;
            }
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4 shadow">
        <div class="container">
            <a class="navbar-brand" href="#">
                <i class="fas fa-shield-alt me-2"></i>Gestão de Patrulhas
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/patrulhas?action=nova">
                            <i class="fas fa-plus-circle me-1"></i>Nova Patrulha
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/">
                            <i class="fas fa-home me-1"></i>Início
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="card shadow-sm mb-4">
            <div class="card-header bg-white d-flex justify-content-between align-items-center py-3">
                <h2 class="h5 mb-0">
                    <i class="fas fa-list me-2"></i>Patrulhas Cadastradas
                </h2>
                <div>
                    <span class="badge bg-primary rounded-pill">
                        <span id="totalCount">${patrulhas.size()}</span> registros
                    </span>
                </div>
            </div>
            <div class="card-body">
                <c:if test="${not empty mensagem}">
                    <div class="alert alert-success alert-dismissible fade show animate__animated animate__fadeIn">
                        <i class="fas fa-check-circle me-2"></i>
                        ${mensagem}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <c:if test="${not empty erro}">
                    <div class="alert alert-danger alert-dismissible fade show animate__animated animate__shakeX">
                        <i class="fas fa-exclamation-circle me-2"></i>
                        ${erro}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <form method="get" action="${pageContext.request.contextPath}/patrulhas" class="row g-3 mb-4 filter-form">
                    <input type="hidden" name="action" value="filtrar">
                    <div class="col-md-3">
                        <select name="status" class="form-select" id="statusFilter">
                            <option value="">Todos status</option>
                            <option value="Planejada" ${param.status == 'Planejada' ? 'selected' : ''}>Planejada</option>
                            <option value="Em Andamento" ${param.status == 'Em Andamento' ? 'selected' : ''}>Em Andamento</option>
                            <option value="Concluida" ${param.status == 'Concluida' ? 'selected' : ''}>Concluída</option>
                            <option value="Cancelada" ${param.status == 'Cancelada' ? 'selected' : ''}>Cancelada</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <input type="date" name="data" value="${param.data}" class="form-control datepicker" id="dateFilter">
                    </div>
                    <div class="col-md-4">
                        <input type="text" name="search" value="${param.search}" class="form-control" placeholder="Pesquisar por nome ou tipo...">
                    </div>
                    <div class="col-md-2">
                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary flex-grow-1">
                                <i class="fas fa-filter me-1"></i>Filtrar
                            </button>
                            <a href="${pageContext.request.contextPath}/patrulhas" class="btn btn-outline-secondary">
                                <i class="fas fa-sync-alt"></i>
                            </a>
                        </div>
                    </div>
                </form>

                <div class="table-responsive">
                    <table class="table table-hover align-middle" id="patrulhasTable">
                        <thead class="table-light">
                            <tr>
                                <th class="sortable" data-sort="id">ID <i class="fas fa-sort sort-icon"></i></th>
                                <th class="sortable" data-sort="nome">Nome <i class="fas fa-sort sort-icon"></i></th>
                                <th class="sortable" data-sort="data">Data <i class="fas fa-sort sort-icon"></i></th>
                                <th>Hora</th>
                                <th class="sortable" data-sort="tipo">Tipo <i class="fas fa-sort sort-icon"></i></th>
                                <th>Zona</th>
                                <th>Responsável</th>
                                <th class="sortable" data-sort="status">Status <i class="fas fa-sort sort-icon"></i></th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${patrulhas}" var="patrulha">
                                <tr data-status="${patrulha.status.toLowerCase()}" 
                                    data-date="${patrulha.data}" 
                                    data-tipo="${patrulha.tipo.toLowerCase()}">
                                    <td>${patrulha.idPatrulha}</td>
                                    <td>${patrulha.nome}</td>
                                    <td>${patrulha.data}</td>
                                    <td>
                                        ${patrulha.horaInicio}
                                        <c:if test="${not empty patrulha.horaFim}">- ${patrulha.horaFim}</c:if>
                                    </td>
                                    <td>${patrulha.tipo}</td>
                                    <td>${not empty patrulha.zonaAtuacao ? patrulha.zonaAtuacao : '-'}</td>
                                    <td>
                                        <c:if test="${not empty usuariosResponsaveis[patrulha.responsavelId]}">
                                            <div class="usuario-info">
                                                <div class="usuario-nome">${usuariosResponsaveis[patrulha.responsavelId].nome}</div>
                                                <div class="usuario-cargo">${usuariosResponsaveis[patrulha.responsavelId].cargo}</div>
                                            </div>
                                        </c:if>
                                        <c:if test="${empty usuariosResponsaveis[patrulha.responsavelId]}">
                                            <span class="text-muted">ID: ${patrulha.responsavelId}</span>
                                        </c:if>
                                    </td>
                                    <td>
                                        <span class="badge status-${patrulha.status.toLowerCase().replace(' ', '-')}">
                                            ${patrulha.status}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="d-flex gap-1 action-btns">
                                            <a href="${pageContext.request.contextPath}/patrulhas?action=detalhes&id=${patrulha.idPatrulha}" 
                                               class="btn btn-sm btn-outline-primary action-btn" title="Detalhes">
                                                <i class="fas fa-eye"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/patrulhas?action=editar&id=${patrulha.idPatrulha}" 
                                               class="btn btn-sm btn-outline-secondary action-btn" title="Editar">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            
                                            <c:choose>
                                                <c:when test="${patrulha.status == 'Planejada'}">
                                                    <a href="${pageContext.request.contextPath}/patrulhas?action=iniciar&id=${patrulha.idPatrulha}" 
                                                       class="btn btn-sm btn-outline-success action-btn" title="Iniciar"
                                                       onclick="return confirmAction('Deseja realmente iniciar esta patrulha?')">
                                                        <i class="fas fa-play"></i>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/patrulhas?action=cancelar&id=${patrulha.idPatrulha}" 
                                                       class="btn btn-sm btn-outline-danger action-btn" title="Cancelar"
                                                       onclick="return confirmAction('Deseja realmente cancelar esta patrulha?')">
                                                        <i class="fas fa-ban"></i>
                                                    </a>
                                                </c:when>
                                                <c:when test="${patrulha.status == 'Em Andamento'}">
                                                    <a href="${pageContext.request.contextPath}/patrulhas?action=finalizar&id=${patrulha.idPatrulha}" 
                                                       class="btn btn-sm btn-outline-success action-btn" title="Finalizar"
                                                       onclick="return showFinalizarDialog(${patrulha.idPatrulha})">
                                                        <i class="fas fa-stop"></i>
                                                    </a>
                                                </c:when>
                                                <c:when test="${patrulha.status == 'Concluida'}">
                                                    <button class="btn btn-sm btn-outline-secondary action-btn" disabled title="Concluída">
                                                        <i class="fas fa-check"></i>
                                                    </button>
                                                </c:when>
                                                <c:when test="${patrulha.status == 'Cancelada'}">
                                                    <button class="btn btn-sm btn-outline-secondary action-btn" disabled title="Cancelada">
                                                        <i class="fas fa-times"></i>
                                                    </button>
                                                </c:when>
                                            </c:choose>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty patrulhas}">
                                <tr>
                                    <td colspan="9" class="text-center py-4 text-muted">
                                        <i class="fas fa-info-circle me-2"></i>Nenhuma patrulha encontrada
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <!-- Paginação -->
                <c:if test="${totalPages > 1}">
                    <nav aria-label="Page navigation" class="mt-4">
                        <ul class="pagination justify-content-center">
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link" href="?page=${currentPage - 1}" aria-label="Previous">
                                    <span aria-hidden="true">&laquo;</span>
                                </a>
                            </li>
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                    <a class="page-link" href="?page=${i}">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link" href="?page=${currentPage + 1}" aria-label="Next">
                                    <span aria-hidden="true">&raquo;</span>
                                </a>
                            </li>
                        </ul>
                    </nav>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Modal para finalizar patrulha -->
    <div class="modal fade" id="finalizarModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Finalizar Patrulha</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form id="finalizarForm" method="post" action="${pageContext.request.contextPath}/patrulhas">
                    <input type="hidden" name="action" value="finalizar">
                    <input type="hidden" name="id" id="patrulhaId">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="horaFim" class="form-label">Hora de Término</label>
                            <input type="time" class="form-control" name="horaFim" required>
                        </div>
                        <div class="mb-3">
                            <label for="relatorio" class="form-label">Relatório</label>
                            <textarea class="form-control" name="relatorio" rows="3" placeholder="Informe o relatório da patrulha..."></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-primary">Finalizar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Inicializa o datepicker
            flatpickr(".datepicker", {
                dateFormat: "Y-m-d",
                locale: "pt"
            });

            // Filtros dinâmicos
            const statusFilter = document.getElementById('statusFilter');
            const dateFilter = document.getElementById('dateFilter');
            const rows = document.querySelectorAll('#patrulhasTable tbody tr');
            const totalCount = document.getElementById('totalCount');

            function filterTable() {
                const statusValue = statusFilter.value.toLowerCase();
                const dateValue = dateFilter.value;
                let visibleCount = 0;

                rows.forEach(row => {
                    const rowStatus = row.getAttribute('data-status');
                    const rowDate = row.getAttribute('data-date');
                    
                    const statusMatch = statusValue === '' || rowStatus === statusValue;
                    const dateMatch = dateValue === '' || rowDate === dateValue;
                    
                    if (statusMatch && dateMatch) {
                        row.style.display = '';
                        visibleCount++;
                    } else {
                        row.style.display = 'none';
                    }
                });

                totalCount.textContent = visibleCount;
            }

            statusFilter.addEventListener('change', filterTable);
            dateFilter.addEventListener('change', filterTable);

            // Ordenação da tabela
            const sortableHeaders = document.querySelectorAll('.sortable');
            sortableHeaders.forEach(header => {
                header.addEventListener('click', function() {
                    const table = this.closest('table');
                    const columnIndex = this.cellIndex;
                    const sortType = this.getAttribute('data-sort');
                    const isAscending = !this.classList.contains('asc');
                    
                    // Reset all headers
                    sortableHeaders.forEach(h => {
                        h.classList.remove('asc', 'desc');
                        const icon = h.querySelector('.sort-icon');
                        icon.className = 'fas fa-sort sort-icon';
                    });
                    
                    // Set current header state
                    this.classList.remove('asc', 'desc');
                    this.classList.add(isAscending ? 'asc' : 'desc');
                    const icon = this.querySelector('.sort-icon');
                    icon.className = isAscending ? 'fas fa-sort-up sort-icon' : 'fas fa-sort-down sort-icon';
                    
                    // Sort rows
                    const rows = Array.from(table.querySelectorAll('tbody tr'));
                    rows.sort((a, b) => {
                        const aValue = a.cells[columnIndex].textContent.trim();
                        const bValue = b.cells[columnIndex].textContent.trim();
                        
                        if (sortType === 'id') {
                            return isAscending ? 
                                parseInt(aValue) - parseInt(bValue) : 
                                parseInt(bValue) - parseInt(aValue);
                        } else if (sortType === 'data') {
                            return isAscending ? 
                                new Date(aValue) - new Date(bValue) : 
                                new Date(bValue) - new Date(aValue);
                        } else {
                            return isAscending ? 
                                aValue.localeCompare(bValue) : 
                                bValue.localeCompare(aValue);
                        }
                    });
                    
                    // Reappend rows
                    const tbody = table.querySelector('tbody');
                    tbody.innerHTML = '';
                    rows.forEach(row => tbody.appendChild(row));
                });
            });

            // Confirmação de ações
            window.confirmAction = function(message) {
                return confirm(message);
            };

            // Modal para finalizar patrulha
            window.showFinalizarDialog = function(patrulhaId) {
                const modal = new bootstrap.Modal(document.getElementById('finalizarModal'));
                document.getElementById('patrulhaId').value = patrulhaId;
                
                // Set current time as default
                const now = new Date();
                const hours = String(now.getHours()).padStart(2, '0');
                const minutes = String(now.getMinutes()).padStart(2, '0');
                document.querySelector('#finalizarModal input[name="horaFim"]').value = `${hours}:${minutes}`;
                
                modal.show();
                return false; // Prevent default action
            };

            // Contagem de caracteres para o relatório
            const relatorioTextarea = document.querySelector('#finalizarModal textarea[name="relatorio"]');
            if (relatorioTextarea) {
                relatorioTextarea.addEventListener('input', function() {
                    const remaining = 500 - this.value.length;
                    const counter = document.getElementById('charCounter');
                    if (counter) {
                        counter.textContent = remaining;
                        if (remaining < 0) {
                            counter.classList.add('text-danger');
                        } else {
                            counter.classList.remove('text-danger');
                        }
                    }
                });
            }
        });
    </script>
</body>
</html>
