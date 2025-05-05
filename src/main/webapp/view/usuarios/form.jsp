<%-- 
    Document   : formulario
    Created on : 2 de mai de 2025, 19:14:55
    Author     : JR5
--%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    
    <!-- DEBUG: Verificar se as listas estão sendo passadas -->
<div style="display: none;">
    <p>Cargos: ${not empty cargos ? 'OK' : 'VAZIO'}</p>
    <p>Perfis: ${not empty perfis ? 'OK' : 'VAZIO'}</p>
    <p>Status: ${not empty statusOptions ? 'OK' : 'VAZIO'}</p>
</div>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${usuario == null ? 'Cadastrar' : 'Editar'} Usuário | SEGEPOL</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        .form-section {
            background-color: #f8f9fa;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
        }
        .form-section h2 {
            color: #0d6efd;
            font-size: 1.2rem;
            margin-bottom: 15px;
            padding-bottom: 5px;
            border-bottom: 1px solid #dee2e6;
        }
        .required-field::after {
            content: " *";
            color: #dc3545;
        }
        .photo-preview {
            max-width: 200px;
            max-height: 200px;
            display: block;
            margin-top: 10px;
            border-radius: 4px;
        }
        .current-photo {
            max-width: 150px;
            max-height: 150px;
            border: 1px solid #dee2e6;
            border-radius: 4px;
            padding: 3px;
        }
    </style>
</head>
<body class="bg-light">
    <div class="container py-4">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white">
                        <h1 class="h5 mb-0">
                            <i class="fas ${usuario == null ? 'fa-user-plus' : 'fa-user-edit'} me-2"></i>
                            ${usuario == null ? 'Cadastrar Novo Usuário' : 'Editar Usuário'}
                        </h1>
                    </div>
                    
                    <div class="card-body">
                        <!-- Mensagens de feedback -->
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-circle me-2"></i>
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        
   <form action="${pageContext.request.contextPath}/usuarios" method="post" enctype="multipart/form-data">
    <input type="hidden" name="action" value="${usuario == null ? 'inserir' : 'atualizar'}">
              
                            <!-- Seção: Dados Pessoais -->
                            <div class="form-section">
                                <h2><i class="fas fa-id-card me-2"></i>Dados Pessoais</h2>
                                
                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label for="nome" class="form-label required-field">Nome Completo</label>
                                        <input type="text" class="form-control" id="nome" name="nome" 
                                               value="${usuario.nome}" required>
                                        <div class="invalid-feedback">Por favor, informe o nome completo.</div>
                                    </div>
                                    
                                    <div class="col-md-6">
                                        <label for="email" class="form-label required-field">Email</label>
                                        <div class="input-group">
                                            <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                                            <input type="email" class="form-control" id="email" name="email" 
                                                   value="${usuario.email}" required>
                                        </div>
                                        <div class="invalid-feedback">Por favor, informe um email válido.</div>
                                    </div>
                                    
                                    <div class="col-md-6">
                                        <label for="telefone" class="form-label">Telefone</label>
                                        <div class="input-group">
                                            <span class="input-group-text"><i class="fas fa-phone"></i></span>
                                            <input type="text" class="form-control" id="telefone" name="telefone" 
                                                   value="${usuario.telefone}">
                                        </div>
                                    </div>
                                    
                                    <div class="col-md-6">
                                        <label for="numero_identificacao" class="form-label required-field">Número de Identificação</label>
                                        <input type="text" class="form-control" id="numero_identificacao" 
                                               name="numero_identificacao" value="${usuario.numero_identificacao}" required>
                                        <div class="invalid-feedback">Por favor, informe o número de identificação.</div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Seção: Dados de Acesso -->
                            <div class="form-section">
                                <h2><i class="fas fa-lock me-2"></i>Dados de Acesso</h2>
                                
                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label for="senha" class="form-label ${usuario == null ? 'required-field' : ''}">Senha</label>
                                        <div class="input-group">
                                            <span class="input-group-text"><i class="fas fa-key"></i></span>
                                            <input type="password" class="form-control" id="senha" name="senha" 
                                                   ${usuario == null ? 'required' : ''}>
                                            <button class="btn btn-outline-secondary toggle-password" type="button">
                                                <i class="fas fa-eye"></i>
                                            </button>
                                        </div>
                                        <c:if test="${usuario != null}">
                                            <small class="text-muted">Deixe em branco para manter a senha atual</small>
                                        </c:if>
                                        <div class="invalid-feedback">Por favor, informe uma senha válida.</div>
                                    </div>
                                    
                                    <div class="col-md-6">
                                        <label for="confirmar_senha" class="form-label ${usuario == null ? 'required-field' : ''}">Confirmar Senha</label>
                                        <div class="input-group">
                                            <span class="input-group-text"><i class="fas fa-key"></i></span>
                                            <input type="password" class="form-control" id="confirmar_senha" 
                                                   ${usuario == null ? 'required' : ''}>
                                            <button class="btn btn-outline-secondary toggle-password" type="button">
                                                <i class="fas fa-eye"></i>
                                            </button>
                                        </div>
                                        <div class="invalid-feedback">As senhas não coincidem.</div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Seção: Informações do Cargo -->
                         <!-- Seção de Cargos - Modificada -->
    <div class="mb-3">
        <label class="form-label required-field">Cargo</label>
        <select class="form-select" name="cargo" required>
            <option value="">Selecione um cargo</option>
            <c:choose>
                <c:when test="${not empty cargos}">
                    <c:forEach items="${cargos}" var="cargo">
                        <option value="${cargo}" ${usuario.cargo eq cargo ? 'selected' : ''}>${cargo}</option>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <!-- Fallback caso cargos não estejam disponíveis -->
                    <option value="Comandante" ${usuario.cargo eq 'Comandante' ? 'selected' : ''}>Comandante</option>
                    <option value="Chefe das operações" ${usuario.cargo eq 'Chefe das operações' ? 'selected' : ''}>Chefe das operações</option>
                    <option value="Chefe da ética" ${usuario.cargo eq 'Chefe da ética' ? 'selected' : ''}>Chefe da ética</option>
                    <!-- Adicione outros cargos conforme necessário -->
                </c:otherwise>
            </c:choose>
        </select>
    </div>

    <!-- Seção de Perfis - Modificada -->
    <div class="mb-3">
        <label class="form-label required-field">Perfil</label>
        <select class="form-select" name="perfil" required>
            <option value="">Selecione um perfil</option>
            <c:choose>
                <c:when test="${not empty perfis}">
                    <c:forEach items="${perfis}" var="perfil">
                        <option value="${perfil}" ${usuario.perfil eq perfil ? 'selected' : ''}>${perfil}</option>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <!-- Fallback caso perfis não estejam disponíveis -->
                    <option value="Comando" ${usuario.perfil eq 'Comando' ? 'selected' : ''}>Comando</option>
                    <option value="Operacional" ${usuario.perfil eq 'Operacional' ? 'selected' : ''}>Operacional</option>
                    <option value="Administrativo" ${usuario.perfil eq 'Administrativo' ? 'selected' : ''}>Administrativo</option>
                    <!-- Adicione outros perfis conforme necessário -->
                </c:otherwise>
            </c:choose>
                    
                    
                    <!-- Adicione isso dentro do formulário quando for edição -->
<c:if test="${not empty usuario}">
    <input type="hidden" name="id" value="${usuario.id_usuario}">
</c:if>
        </select>
    </div>

    <!-- Seção de Status - Modificada -->
    <div class="mb-3">
        <label class="form-label required-field">Status</label>
        <select class="form-select" name="status" required>
            <c:choose>
                <c:when test="${not empty statusOptions}">
                    <c:forEach items="${statusOptions}" var="status">
                        <option value="${status}" ${usuario.status eq status ? 'selected' : ''}>${status}</option>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <!-- Fallback caso status não estejam disponíveis -->
                    <option value="Ativo" ${usuario.status eq 'Ativo' ? 'selected' : ''}>Ativo</option>
                    <option value="Inativo" ${usuario.status eq 'Inativo' ? 'selected' : ''}>Inativo</option>
                </c:otherwise>
            </c:choose>
        </select>
    </div>
                            <!-- Botões de ação -->
                            <div class="d-flex justify-content-between mt-4">
                                <a href="usuarios?action=list" class="btn btn-outline-secondary">
                                    <i class="fas fa-arrow-left me-2"></i>Cancelar
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save me-2"></i>Salvar
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Validação e scripts do formulário -->
    <script>
        // Validação do formulário
        (function() {
            'use strict';
            
            // Selecionar o formulário
            const form = document.querySelector('.needs-validation');
            
            // Validar ao submeter
            form.addEventListener('submit', function(event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                
                // Verificar se as senhas coincidem
                const senha = document.getElementById('senha');
                const confirmarSenha = document.getElementById('confirmar_senha');
                
                if (senha.value !== confirmarSenha.value) {
                    confirmarSenha.setCustomValidity('As senhas não coincidem');
                    event.preventDefault();
                    event.stopPropagation();
                } else {
                    confirmarSenha.setCustomValidity('');
                }
                
                form.classList.add('was-validated');
            }, false);
            
            // Toggle para mostrar/esconder senha
            document.querySelectorAll('.toggle-password').forEach(button => {
                button.addEventListener('click', function() {
                    const input = this.parentNode.querySelector('input');
                    const icon = this.querySelector('i');
                    
                    if (input.type === 'password') {
                        input.type = 'text';
                        icon.classList.replace('fa-eye', 'fa-eye-slash');
                    } else {
                        input.type = 'password';
                        icon.classList.replace('fa-eye-slash', 'fa-eye');
                    }
                });
            });
            
            // Máscara para telefone
            const telefoneInput = document.getElementById('telefone');
            if (telefoneInput) {
                telefoneInput.addEventListener('input', function(e) {
                    let value = e.target.value.replace(/\D/g, '');
                    if (value.length > 11) value = value.substring(0, 11);
                    
                    if (value.length > 0) {
                        value = value.replace(/^(\d{2})(\d)/g, '($1) $2');
                        if (value.length > 10) {
                            value = value.replace(/(\d)(\d{4})$/, '$1-$2');
                        } else {
                            value = value.replace(/(\d)(\d{3})$/, '$1-$2');
                        }
                    }
                    
                    e.target.value = value;
                });
            }
            
            // Pré-visualização da foto
            const fotoInput = document.getElementById('foto');
            if (fotoInput) {
                fotoInput.addEventListener('change', function(e) {
                    const file = e.target.files[0];
                    if (file) {
                        const reader = new FileReader();
                        reader.onload = function(event) {
                            let preview = document.querySelector('.photo-preview');
                            if (!preview) {
                                const container = document.querySelector('.current-photo-container') || 
                                                document.querySelector('.form-section:last-child');
                                preview = document.createElement('img');
                                preview.className = 'photo-preview img-thumbnail mt-2';
                                container.appendChild(preview);
          Z                  }
                            preview.src = event.target.result;
                        };
                        reader.readAsDataURL(file);
                    }
                });
            }
        })();
    </script>
</body>
</html>