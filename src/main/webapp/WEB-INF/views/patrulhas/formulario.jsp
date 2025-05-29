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
    <style>
        body {
            background-color: #f8f9fa;
        }
        .form-container {
            max-width: 900px;
            margin: 0 auto;
        }
        .card {
            border: none;
            box-shadow: 0 0.125rem 0.25rem rgba(0,0,0,0.075);
        }
        .card-header {
            font-weight: 500;
        }
        .membros-selecao {
            max-height: 300px;
            overflow-y: auto;
            border: 1px solid #dee2e6;
            border-radius: 0.375rem;
            padding: 1rem;
            background-color: #f8f9fa;
        }
        .membro-item {
            padding: 0.75rem;
            margin-bottom: 0.5rem;
            background-color: white;
            border-radius: 0.25rem;
            border-left: 3px solid #0d6efd;
            transition: all 0.2s;
        }
        .membro-item:hover {
            background-color: #f0f7ff;
        }
        .membro-item .form-check-input {
            margin-right: 0.75rem;
        }
        .required-field::after {
            content: " *";
            color: #dc3545;
        }
        .form-section {
            margin-bottom: 1.5rem;
        }
        .form-section-title {
            font-size: 1.1rem;
            font-weight: 500;
            color: #495057;
            margin-bottom: 1rem;
            padding-bottom: 0.5rem;
            border-bottom: 1px solid #dee2e6;
        }
        @media (max-width: 768px) {
            .form-container {
                padding: 0 15px;
            }
        }
    </style>
</head>
<body>
    <div class="container py-4">
        <div class="form-container">
            <div class="card shadow">
                <div class="card-header bg-primary text-white">
                    <h3 class="mb-0">
                        <i class="fas fa-${tipoForm == 'nova' ? 'plus-circle' : 'edit'} me-2"></i>
                        ${tipoForm == 'nova' ? 'Cadastrar Nova Patrulha' : 'Editar Patrulha'}
                    </h3>
                </div>
                <div class="card-body">
                    <c:if test="${not empty erro}">
                        <div class="alert alert-danger alert-dismissible fade show">
                            ${erro}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form class="row g-3" action="patrulhas?action=${tipoForm == 'nova' ? 'salvar' : 'atualizar'}" method="post">
                        <c:if test="${tipoForm == 'editar'}">
                            <input type="hidden" name="id" value="${patrulha.idPatrulha}">
                        </c:if>
                        
                        <div class="col-md-12 form-section">
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
                                    <select class="form-select" id="responsavelId" name="responsavelId" required>
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
                                    <input type="date" class="form-control" id="data" name="data" 
                                           value="${patrulha.data}" required>
                                </div>
                                
                                <div class="col-md-4">
                                    <label for="horaInicio" class="form-label required-field">Hora de Início</label>
                                    <input type="time" class="form-control" id="horaInicio" name="horaInicio" 
                                           value="${patrulha.horaInicio}" required>
                                </div>
                                
                                <div class="col-md-4">
                                    <label for="horaFim" class="form-label">Hora de Término</label>
                                    <input type="time" class="form-control" id="horaFim" name="horaFim" 
                                           value="${patrulha.horaFim}">
                                </div>
                                
                                <div class="col-md-6">
                                    <label for="tipo" class="form-label required-field">Tipo</label>
<select name="tipo" class="form-select" required>
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
                        
                        <div class="col-md-12 form-section">
                            <h5 class="form-section-title">
                                <i class="fas fa-users me-2"></i>Membros da Patrulha
                            </h5>
                            <div class="membros-selecao">
                                <c:forEach items="${todosUsuarios}" var="usuario">
                                    <c:if test="${usuario.id_usuario != patrulha.responsavelId}">
                                        <div class="membro-item form-check">
                                            <input class="form-check-input" type="checkbox" 
                                                   id="membro-${usuario.id_usuario}" 
                                                   name="membros" value="${usuario.id_usuario}"
                                                   ${patrulha.contemMembro(usuario.id_usuario) ? 'checked' : ''}>
                                            <label class="form-check-label" for="membro-${usuario.id_usuario}">
                                                ${usuario.nome} (${usuario.cargo})
                                            </label>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </div>
                        
                        <div class="col-md-12 form-section">
                            <h5 class="form-section-title">
                                <i class="fas fa-clipboard-list me-2"></i>Outras Informações
                            </h5>
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label for="status" class="form-label required-field">Status</label>
                                    <select class="form-select" id="status" name="status" required>
                                        <option value="Planejada" ${patrulha.status == 'Planejada' ? 'selected' : ''}>Planejada</option>
                                        <option value="Em Andamento" ${patrulha.status == 'Em Andamento' ? 'selected' : ''}>Em Andamento</option>
                                        <option value="Concluída" ${patrulha.status == 'Concluída' ? 'selected' : ''}>Concluída</option>
                                        <option value="Cancelada" ${patrulha.status == 'Cancelada' ? 'selected' : ''}>Cancelada</option>
                                    </select>
                                </div>
                                
                                <div class="col-md-12">
                                    <label for="observacoes" class="form-label">Observações</label>
                                    <textarea class="form-control" id="observacoes" name="observacoes" 
                                              rows="3">${patrulha.observacoes}</textarea>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-md-12">
                            <div class="d-flex justify-content-between">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save me-2"></i>
                                    ${tipoForm == 'nova' ? 'Cadastrar' : 'Atualizar'}
                                </button>
                                <a href="${pageContext.request.contextPath}/patrulhas" class="btn btn-outline-secondary">
                                    <i class="fas fa-times me-2"></i>Cancelar
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>


    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
