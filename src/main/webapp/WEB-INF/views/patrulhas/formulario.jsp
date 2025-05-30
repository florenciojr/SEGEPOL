<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${tipoForm == 'nova' ? 'Nova Patrulha' : 'Editar Patrulha'}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <style>
        body {
            background-color: #f8f9fa;
            padding-top: 20px;
        }
        .form-container {
            max-width: 1000px;
            margin: 0 auto;
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            padding: 30px;
        }
        .card-header {
            font-weight: 600;
            background: linear-gradient(135deg, #3a7bd5, #00d2ff);
            color: white;
            border-radius: 8px 8px 0 0 !important;
        }
        .membros-selecao {
            max-height: 400px;
            overflow-y: auto;
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 15px;
            background-color: #f8f9fa;
        }
        .membro-item {
            padding: 12px;
            margin-bottom: 8px;
            background-color: white;
            border-radius: 6px;
            border-left: 4px solid #3a7bd5;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
        }
        .membro-item:hover {
            transform: translateX(5px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .required-field::after {
            content: " *";
            color: #dc3545;
        }
        .form-section {
            margin-bottom: 2rem;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }
        .form-section-title {
            font-size: 1.2rem;
            font-weight: 600;
            color: #3a7bd5;
            margin-bottom: 1.2rem;
            padding-bottom: 0.8rem;
            border-bottom: 2px solid #f0f0f0;
        }
        .status-badge {
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 600;
        }
        .status-planned {
            background-color: #e3f2fd;
            color: #1565c0;
        }
        .status-in-progress {
            background-color: #fff8e1;
            color: #ff8f00;
        }
        .status-completed {
            background-color: #e8f5e9;
            color: #2e7d32;
        }
        .status-cancelled {
            background-color: #ffebee;
            color: #c62828;
        }
        .animated-checkbox {
            transform: scale(1.2);
            margin-right: 10px;
        }
        .btn-action {
            transition: all 0.3s;
            font-weight: 500;
        }
        .btn-action:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .time-input-group {
            position: relative;
        }
        .time-input-group .input-icon {
            position: absolute;
            right: 10px;
            top: 50%;
            transform: translateY(-50%);
            color: #6c757d;
        }
    </style>
</head>
<body>
    <div class="container py-4">
        <div class="form-container">
            <div class="card shadow-sm">
                <div class="card-header">
                    <h3 class="mb-0">
                        <i class="fas fa-${tipoForm == 'nova' ? 'plus-circle' : 'edit'} me-2"></i>
                        ${tipoForm == 'nova' ? 'Cadastrar Nova Patrulha' : 'Editar Patrulha'}
                    </h3>
                </div>
                <div class="card-body">
                    <c:if test="${not empty erro}">
                        <div class="alert alert-danger alert-dismissible fade show animate__animated animate__shakeX">
                            <i class="fas fa-exclamation-circle me-2"></i>
                            ${erro}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form id="patrulhaForm" class="row g-3" action="patrulhas?action=${tipoForm == 'nova' ? 'salvar' : 'atualizar'}" method="post">
                        <c:if test="${tipoForm == 'editar'}">
                            <input type="hidden" name="id" value="${patrulha.idPatrulha}">
                        </c:if>
                        
                        <!-- Seção de Informações Básicas -->
                        <div class="col-md-12 form-section animate__animated animate__fadeIn">
                            <h5 class="form-section-title">
                                <i class="fas fa-info-circle me-2"></i>Informações Básicas
                            </h5>
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label for="nome" class="form-label required-field">Nome da Patrulha</label>
                                    <input type="text" class="form-control" id="nome" name="nome" 
                                           value="${patrulha.nome}" required>
                                </div>
                                
                                <div class="col-md-6">
                                    <label for="responsavelId" class="form-label required-field">Responsável</label>
                                    <select class="form-select select2" id="responsavelId" name="responsavelId" required>
                                        <option value="">Selecione o responsável</option>
                                        <c:forEach items="${todosUsuarios}" var="usuario">
                                            <option value="${usuario.id_usuario}" 
                                                ${patrulha.responsavelId == usuario.id_usuario ? 'selected' : ''}>
                                                ${usuario.nome} (${usuario.cargo})
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                
                                <div class="col-md-4">
                                    <label for="data" class="form-label required-field">Data</label>
                                    <input type="date" class="form-control datepicker" id="data" name="data" 
                                           value="${patrulha.data}" required>
                                </div>
                                
                                <div class="col-md-4">
                                    <label for="horaInicio" class="form-label required-field">Hora de Início</label>
                                    <div class="time-input-group">
                                        <input type="time" class="form-control" id="horaInicio" name="horaInicio" 
                                               value="${patrulha.horaInicio}" required>
                                        <span class="input-icon"><i class="far fa-clock"></i></span>
                                    </div>
                                </div>
                                
                                <div class="col-md-4">
                                    <label for="horaFim" class="form-label">Hora de Término</label>
                                    <div class="time-input-group">
                                        <input type="time" class="form-control" id="horaFim" name="horaFim" 
                                               value="${patrulha.horaFim}">
                                        <span class="input-icon"><i class="far fa-clock"></i></span>
                                    </div>
                                </div>
                                
                                <div class="col-md-6">
                                    <label for="tipo" class="form-label required-field">Tipo</label>
                                    <select name="tipo" class="form-select" id="tipo" required>
                                        <option value="">Selecione o tipo</option>
                                        <option value="Ronda" ${patrulha.tipo == 'Ronda' ? 'selected' : ''}>Ronda</option>
                                        <option value="Operacao" ${patrulha.tipo == 'Operacao' ? 'selected' : ''}>Operação</option>
                                        <option value="Especial" ${patrulha.tipo == 'Especial' ? 'selected' : ''}>Especial</option>
                                        <option value="Preventiva" ${patrulha.tipo == 'Preventiva' ? 'selected' : ''}>Preventiva</option>
                                    </select>
                                </div>
                                
                                <div class="col-md-6">
                                    <label for="zonaAtuacao" class="form-label">Zona de Atuação</label>
                                    <input type="text" class="form-control" id="zonaAtuacao" name="zonaAtuacao" 
                                           value="${patrulha.zonaAtuacao}">
                                </div>
                            </div>
                        </div>
                        
                        <!-- Seção de Membros -->
                        <div class="col-md-12 form-section animate__animated animate__fadeIn animate__delay-1s">
                            <h5 class="form-section-title">
                                <i class="fas fa-users me-2"></i>Membros da Patrulha
                                <small class="text-muted ms-2">(${todosUsuarios.size()} disponíveis)</small>
                            </h5>
                            <div class="d-flex align-items-center mb-3">
                                <input type="text" id="searchMember" class="form-control w-50" placeholder="Pesquisar membros...">
                                <button type="button" id="selectAllBtn" class="btn btn-sm btn-outline-primary ms-2">
                                    <i class="fas fa-check-double me-1"></i>Selecionar todos
                                </button>
                                <button type="button" id="deselectAllBtn" class="btn btn-sm btn-outline-secondary ms-2">
                                    <i class="fas fa-times-circle me-1"></i>Desmarcar todos
                                </button>
                            </div>
                            <div class="membros-selecao">
                                <c:forEach items="${todosUsuarios}" var="usuario">
                                    <c:if test="${usuario.id_usuario != patrulha.responsavelId}">
                                        <div class="membro-item" data-search="${usuario.nome.toLowerCase()} ${usuario.cargo.toLowerCase()}">
                                            <input class="form-check-input animated-checkbox" type="checkbox" 
                                                   id="membro-${usuario.id_usuario}" 
                                                   name="membros" value="${usuario.id_usuario}"
                                                   ${patrulha.contemMembro(usuario.id_usuario) ? 'checked' : ''}>
                                            <label class="form-check-label ms-2" for="membro-${usuario.id_usuario}">
                                                <strong>${usuario.nome}</strong> 
                                                <span class="text-muted">(${usuario.cargo})</span>
                                            </label>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </div>
                        
                        <!-- Seção de Outras Informações -->
                        <div class="col-md-12 form-section animate__animated animate__fadeIn animate__delay-2s">
                            <h5 class="form-section-title">
                                <i class="fas fa-clipboard-list me-2"></i>Outras Informações
                            </h5>
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label for="status" class="form-label required-field">Status</label>
                                    <select class="form-select" id="status" name="status" required>
                                        <option value="Planejada" ${patrulha.status == 'Planejada' ? 'selected' : ''}>Planejada</option>
                                        <option value="Em Andamento" ${patrulha.status == 'Em Andamento' ? 'selected' : ''}>Em Andamento</option>
                                        <option value="Concluida" ${patrulha.status == 'Concluida' ? 'selected' : ''}>Concluída</option>
                                        <option value="Cancelada" ${patrulha.status == 'Cancelada' ? 'selected' : ''}>Cancelada</option>
                                    </select>
                                </div>
                                
                                <div class="col-md-12">
                                    <label for="observacoes" class="form-label">Observações</label>
                                    <textarea class="form-control" id="observacoes" name="observacoes" 
                                              rows="4">${patrulha.observacoes}</textarea>
                                    <small class="text-muted">Máximo 2000 caracteres</small>
                                    <div class="progress mt-2" style="height: 5px;">
                                        <div id="charProgress" class="progress-bar" role="progressbar" style="width: 0%"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Botões de Ação -->
                        <div class="col-md-12 mt-4">
                            <div class="d-flex justify-content-between">
                                <button type="submit" class="btn btn-primary btn-action px-4 py-2">
                                    <i class="fas fa-save me-2"></i>
                                    ${tipoForm == 'nova' ? 'Cadastrar' : 'Atualizar'}
                                </button>
                                <a href="${pageContext.request.contextPath}/patrulhas" class="btn btn-outline-secondary btn-action px-4 py-2">
                                    <i class="fas fa-times me-2"></i>Cancelar
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/animejs@3.2.1/lib/anime.min.js"></script>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Inicializa o Select2 para o campo de responsável
            $('.select2').select2({
                placeholder: "Selecione o responsável",
                allowClear: true,
                width: '100%'
            });
            
            // Configura o datepicker
            flatpickr(".datepicker", {
                dateFormat: "Y-m-d",
                minDate: "today",
                locale: "pt" // Configuração para português
            });
            
            // Pesquisa de membros
            const searchMember = document.getElementById('searchMember');
            const memberItems = document.querySelectorAll('.membro-item');
            
            searchMember.addEventListener('input', function() {
                const searchTerm = this.value.toLowerCase();
                
                memberItems.forEach(item => {
                    const searchText = item.getAttribute('data-search');
                    if (searchText.includes(searchTerm)) {
                        item.style.display = 'flex';
                        anime({
                            targets: item,
                            translateX: [10, 0],
                            opacity: [0, 1],
                            duration: 300,
                            easing: 'easeOutQuad'
                        });
                    } else {
                        item.style.display = 'none';
                    }
                });
            });
            
            // Selecionar/Deselecionar todos
            document.getElementById('selectAllBtn').addEventListener('click', function() {
                document.querySelectorAll('.membro-item input[type="checkbox"]').forEach(checkbox => {
                    checkbox.checked = true;
                });
                showToast('Todos os membros selecionados', 'success');
            });
            
            document.getElementById('deselectAllBtn').addEventListener('click', function() {
                document.querySelectorAll('.membro-item input[type="checkbox"]').forEach(checkbox => {
                    checkbox.checked = false;
                });
                showToast('Seleção de membros limpa', 'info');
            });
            
            // Contador de caracteres para observações
            const observacoes = document.getElementById('observacoes');
            const charProgress = document.getElementById('charProgress');
            const maxChars = 2000;
            
            observacoes.addEventListener('input', function() {
                const currentLength = this.value.length;
                const percent = (currentLength / maxChars) * 100;
                
                charProgress.style.width = `${percent}%`;
                
                if (currentLength > maxChars * 0.9) {
                    charProgress.classList.remove('bg-primary');
                    charProgress.classList.add('bg-danger');
                } else {
                    charProgress.classList.remove('bg-danger');
                    charProgress.classList.add('bg-primary');
                }
            });
            
            // Validação do formulário
            document.getElementById('patrulhaForm').addEventListener('submit', function(e) {
                const responsavelId = document.getElementById('responsavelId').value;
                const membrosSelecionados = document.querySelectorAll('.membro-item input[type="checkbox"]:checked').length;
                
                if (!responsavelId) {
                    e.preventDefault();
                    showError('Selecione um responsável para a patrulha');
                    return;
                }
                
                if (membrosSelecionados < 1) {
                    e.preventDefault();
                    showError('Selecione pelo menos um membro para a patrulha');
                    return;
                }
                
                
// Mostrar confirmação antes de enviar
e.preventDefault();
const actionText = tipoForm === 'nova' ? 'cadastrar' : 'atualizar';

Swal.fire({
    title: 'Confirmar ação',
    text: `Deseja ${actionText} esta patrulha?`,
    icon: 'question',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Sim, confirmar',
    cancelButtonText: 'Cancelar'
}).then((result) => {
    if (result.isConfirmed) {
        this.submit();
    }
});
            
            // Animação de status
            const statusSelect = document.getElementById('status');
            
            function updateStatusColor() {
                statusSelect.className = 'form-select';
                
                switch(statusSelect.value) {
                    case 'Planejada':
                        statusSelect.classList.add('status-planned');
                        break;
                    case 'Em Andamento':
                        statusSelect.classList.add('status-in-progress');
                        break;
                    case 'Concluida':
                        statusSelect.classList.add('status-completed');
                        break;
                    case 'Cancelada':
                        statusSelect.classList.add('status-cancelled');
                        break;
                }
            }
            
            updateStatusColor();
            statusSelect.addEventListener('change', updateStatusColor);
            
            // Funções auxiliares
            function showToast(message, type) {
                const Toast = Swal.mixin({
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 3000,
                    timerProgressBar: true,
                    didOpen: (toast) => {
                        toast.addEventListener('mouseenter', Swal.stopTimer)
                        toast.addEventListener('mouseleave', Swal.resumeTimer)
                    }
                });
                
                Toast.fire({
                    icon: type,
                    title: message
                });
            }
            
            function showError(message) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: message,
                    confirmButtonColor: '#3085d6'
                });
            }
            
            // Efeito de carregamento
            anime({
                targets: '.form-section',
                opacity: [0, 1],
                translateY: [20, 0],
                delay: anime.stagger(100),
                easing: 'easeOutQuad'
            });
        });
    </script>
</body>
</html>
