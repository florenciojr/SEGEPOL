<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cadastrar Tipo de Queixa - Gestão Policial</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container-fluid mt-4">
            <div class="row mb-4">
                <div class="col">
                    <h1 class="display-6">
                        <i class="bi bi-clipboard2-plus"></i> Cadastrar Novo Tipo de Queixa
                    </h1>
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}">Início</a></li>
                            <li class="breadcrumb-item"><a href="tiposqueixa">Tipos de Queixa</a></li>
                            <li class="breadcrumb-item active">Novo Cadastro</li>
                        </ol>
                    </nav>
                </div>
            </div>

            <c:if test="${not empty mensagemErro}">
                <div class="alert alert-danger alert-dismissible fade show">
                    ${mensagemErro}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <div class="card">
                <div class="card-body">
                    <form action="tiposqueixa?action=adicionar" method="post" id="formTipoQueixa">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="nome" class="form-label">Nome do Tipo <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="nome" name="nome" 
                                       required maxlength="100" placeholder="Ex: Furto qualificado">
                                <div class="form-text">Máximo 100 caracteres</div>
                            </div>
                            <div class="col-md-6">
                                <label for="gravidade" class="form-label">Gravidade <span class="text-danger">*</span></label>
                                <select class="form-select" id="gravidade" name="gravidade" required>
                                    <option value="">Selecione a gravidade...</option>
                                    <option value="Leve">Leve</option>
                                    <option value="Média">Média</option>
                                    <option value="Grave">Grave</option>
                                    <option value="Muito Grave">Muito Grave</option>
                                </select>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="descricao" class="form-label">Descrição <span class="text-danger">*</span></label>
                            <textarea class="form-control" id="descricao" name="descricao" 
                                      rows="4" required placeholder="Descreva as características deste tipo de queixa"></textarea>
                        </div>

                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                            <button type="submit" class="btn btn-primary me-md-2">
                                <i class="bi bi-save"></i> Cadastrar
                            </button>
                            <a href="tiposqueixa" class="btn btn-outline-secondary">
                                <i class="bi bi-x-circle"></i> Cancelar
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
        
        <script>
            // Validação do formulário
            document.getElementById('formTipoQueixa').addEventListener('submit', function(e) {
                const nome = document.getElementById('nome').value.trim();
                const gravidade = document.getElementById('gravidade').value;
                const descricao = document.getElementById('descricao').value.trim();
                
                if (!nome || !gravidade || !descricao) {
                    e.preventDefault();
                    alert('Por favor, preencha todos os campos obrigatórios!');
                    return false;
                }
                
                if (nome.length > 100) {
                    e.preventDefault();
                    alert('O nome do tipo não pode exceder 100 caracteres!');
                    return false;
                }
                
                return true;
            });
        </script>
    </body>
</html>
