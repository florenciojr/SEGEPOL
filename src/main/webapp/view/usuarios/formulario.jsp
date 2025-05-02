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
        .current-photo {
            max-width: 100px;
            max-height: 100px;
            display: block;
            margin-top: 10px;
            border-radius: 4px;
        }
    </style>
</head>
<body class="bg-light">
    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white">
                        <h1 class="h4 mb-0">
                            <i class="fas ${usuario == null ? 'fa-user-plus' : 'fa-user-edit'} me-2"></i>
                            ${usuario == null ? 'Cadastrar Novo Usuário' : 'Editar Usuário'}
                        </h1>
                    </div>
                    
                    <div class="card-body">
                        <!-- Mensagens de feedback -->
                        <c:if test="${not empty erro}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-circle me-2"></i>
                                ${erro}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        
                        <c:if test="${not empty sucesso}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="fas fa-check-circle me-2"></i>
                                ${sucesso}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                      <form action="${pageContext.request.contextPath}/usuarios" method="POST" enctype="multipart/form-data">     <input type="hidden" name="action" value="${usuario == null ? 'inserir' : 'atualizar'}">
                            <c:if test="${usuario != null}">
                                <input type="hidden" name="id" value="${usuario.id_usuario}">
                            </c:if>
                            
                            <!-- Seção: Dados Pessoais -->
                            <div class="form-section">
                                <h2><i class="fas fa-id-card me-2"></i>Dados Pessoais</h2>
                                
                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label for="nome" class="form-label required-field">Nome Completo</label>
                                        <input type="text" class="form-control" id="nome" name="nome" 
                                               value="${usuario.nome}" required
                                               placeholder="Digite o nome completo">
                                        <div class="invalid-feedback">Por favor, informe o nome completo.</div>
                                    </div>
                                    
                                    <div class="col-md-6">
                                        <label for="email" class="form-label required-field">Email</label>
                                        <div class="input-group">
                                            <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                                            <input type="email" class="form-control" id="email" name="email" 
                                                   value="${usuario.email}" required
                                                   placeholder="exemplo@dominio.com">
                                        </div>
                                        <div class="invalid-feedback">Por favor, informe um email válido.</div>
                                    </div>
                                    
                                    <div class="col-md-6">
                                        <label for="telefone" class="form-label">Telefone</label>
                                        <div class="input-group">
                                            <span class="input-group-text"><i class="fas fa-phone"></i></span>
                                            <input type="tel" class="form-control" id="telefone" name="telefone" 
                                                   value="${usuario.contacto}"
                                                   placeholder="(00) 00000-0000">
                                        </div>
                                    </div>
                                    
                                    <div class="col-md-6">
                                        <label for="numero_identificacao" class="form-label required-field">Número de Identificação</label>
                                        <input type="text" class="form-control" id="numero_identificacao" 
                                               name="numero_identificacao" value="${usuario.numero_identificacao}" required
                                               placeholder="Número único de identificação">
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
                                                   ${usuario == null ? 'required' : ''}
                                                   placeholder="${usuario == null ? 'Digite uma senha forte' : 'Deixe em branco para manter a atual'}">
                                            <button class="btn btn-outline-secondary toggle-password" type="button">
                                                <i class="fas fa-eye"></i>
                                            </button>
                                        </div>
                                        <div class="form-text">Mínimo de 8 caracteres, com letras e números</div>
                                        <div class="invalid-feedback">Por favor, informe uma senha válida.</div>
                                    </div>
                                    
                                    <div class="col-md-6">
                                        <label for="confirmar_senha" class="form-label ${usuario == null ? 'required-field' : ''}">Confirmar Senha</label>
                                        <div class="input-group">
                                            <span class="input-group-text"><i class="fas fa-key"></i></span>
                                            <input type="password" class="form-control" id="confirmar_senha" 
                                                   ${usuario == null ? 'required' : ''}
                                                   placeholder="Confirme a senha">
                                            <button class="btn btn-outline-secondary toggle-password" type="button">
                                                <i class="fas fa-eye"></i>
                                            </button>
                                        </div>
                                        <div class="invalid-feedback">As senhas não coincidem.</div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Seção: Informações do Cargo -->
                            <div class="form-section">
                                <h2><i class="fas fa-briefcase me-2"></i>Informações do Cargo</h2>
                                
                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label for="cargo" class="form-label required-field">Cargo</label>
                                        <select class="form-select" id="cargo" name="cargo" required>
                                            <option value="" disabled selected>Selecione um cargo</option>
                                            <option value="Comandante" ${usuario.cargo eq 'Comandante' ? 'selected' : ''}>Comandante</option>
                                            <option value="Investigador" ${usuario.cargo eq 'Investigador' ? 'selected' : ''}>Investigador</option>
                                            <option value="Agente de Patrulha" ${usuario.cargo eq 'Agente de Patrulha' ? 'selected' : ''}>Agente de Patrulha</option>
                                            <option value="Agente Tático" ${usuario.cargo eq 'Agente Tático' ? 'selected' : ''}>Agente Tático</option>
                                            <option value="Perito Criminal" ${usuario.cargo eq 'Perito Criminal' ? 'selected' : ''}>Perito Criminal</option>
                                            <option value="Administrador" ${usuario.cargo eq 'Administrador' ? 'selected' : ''}>Administrador</option>
                                        </select>
                                        <div class="invalid-feedback">Por favor, selecione um cargo.</div>
                                    </div>
                                    
                                    <div class="col-md-6">
                                        <label for="perfil" class="form-label required-field">Perfil</label>
                                        <select class="form-select" id="perfil" name="perfil" required>
                                            <option value="" disabled selected>Selecione um perfil</option>
                                            <option value="Operacional" ${usuario.perfil eq 'Operacional' ? 'selected' : ''}>Operacional</option>
                                            <option value="Tático" ${usuario.perfil eq 'Tático' ? 'selected' : ''}>Tático</option>
                                            <option value="Investigativo" ${usuario.perfil eq 'Investigativo' ? 'selected' : ''}>Investigativo</option>
                                            <option value="Administrativo" ${usuario.perfil eq 'Administrativo' ? 'selected' : ''}>Administrativo</option>
                                            <option value="Comando" ${usuario.perfil eq 'Comando' ? 'selected' : ''}>Comando</option>
                                        </select>
                                        <div class="invalid-feedback">Por favor, selecione um perfil.</div>
                                    </div>
                                    
                                    <div class="col-md-6">
                                        <label for="status" class="form-label required-field">Status</label>
                                        <select class="form-select" id="status" name="status" required>
                                            <option value="Ativo" ${usuario.status eq 'Ativo' ? 'selected' : ''}>Ativo</option>
                                            <option value="Inativo" ${usuario.status eq 'Inativo' ? 'selected' : ''}>Inativo</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Seção: Foto de Perfil -->
                            <div class="form-section">
                                <h2><i class="fas fa-camera me-2"></i>Foto de Perfil</h2>
                                
                                <div class="mb-3">
                                    <label for="foto_perfil" class="form-label">Upload de Foto</label>
                                    <input class="form-control" type="file" id="foto_perfil" name="foto_perfil" accept="image/*">
                                    <div class="form-text">Formatos aceitos: JPG, PNG, GIF (Máx. 2MB)</div>
                                </div>
                                
                                <c:if test="${not empty usuario.foto_perfil}">
                                    <div class="current-photo-container">
                                        <p class="mb-2">Foto atual:</p>
                                        <img src="${pageContext.request.contextPath}/uploads/usuarios/${usuario.foto_perfil}" 
                                             alt="Foto do perfil" class="current-photo img-thumbnail">
                                        <div class="form-check mt-2">
                                            <input class="form-check-input" type="checkbox" id="remover_foto" name="remover_foto">
                                            <label class="form-check-label" for="remover_foto">Remover foto atual</label>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                            
                            <!-- Botões de ação -->
                            <div class="d-flex justify-content-between mt-4">
                                <a href="usuarios" class="btn btn-outline-secondary">
                                    <i class="fas fa-arrow-left me-2"></i>Voltar
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
    
    <!-- Validação do formulário -->
    <script>
        // Exemplo de validação do formulário
        (function() {
            'use strict';
            
            // Selecionar todos os formulários que precisam de validação
            const forms = document.querySelectorAll('form');
            
            // Loop sobre eles e prevenir envio
            Array.from(forms).forEach(form => {
                form.addEventListener('submit', event => {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    
                    form.classList.add('was-validated');
                    
                    // Validação personalizada para confirmar senha
                    const senha = document.getElementById('senha');
                    const confirmarSenha = document.getElementById('confirmar_senha');
                    
                    if (senha && confirmarSenha && senha.value !== confirmarSenha.value) {
                        confirmarSenha.setCustomValidity('As senhas não coincidem');
                        event.preventDefault();
                        event.stopPropagation();
                    } else if (confirmarSenha) {
                        confirmarSenha.setCustomValidity('');
                    }
                }, false);
            });
            
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
        })();
    </script>
</body>
</html>