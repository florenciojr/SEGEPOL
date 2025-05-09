<%-- 
    Document   : formUsuario
    Created on : 2 de mai. de 2025, 14:30:00
    Author     : JR5
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${empty usuario ? 'Cadastrar' : 'Editar'} Usuário | SIGEPOL</title>
       <%@include file="/WEB-INF/views/templates/header.jsp" %>
    <style>
        :root {
            --police-primary: #0d47a1;
            --police-dark: #002171;
            --police-light: #e3f2fd;
            --police-accent: #ffab00;
            --police-danger: #dc3545;
        }
        
        .form-container {
            max-width: 800px;
            margin: 2rem auto;
        }
        
        .card-form {
            border: none;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }
        
        .card-header-form {
            background: linear-gradient(135deg, var(--police-dark), var(--police-primary));
            color: white;
            padding: 1.25rem 1.5rem;
            border-bottom: 3px solid var(--police-accent);
        }
        
        .photo-container {
            width: 150px;
            height: 150px;
            margin: 0 auto 1.5rem;
            position: relative;
            border-radius: 50%;
            background-color: var(--police-light);
        }
        
        .photo-preview {
            width: 100%;
            height: 100%;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid white;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }
        
        .photo-upload {
            position: absolute;
            bottom: 10px;
            right: 10px;
            background-color: var(--police-accent);
            color: white;
            width: 36px;
            height: 36px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
        }
        
        .form-section {
            background-color: white;
            border-radius: 6px;
            padding: 1.5rem;
            margin-bottom: 1.5rem;
            border-left: 4px solid var(--police-primary);
        }
        
        .section-title {
            color: var(--police-primary);
            font-weight: 600;
            margin-bottom: 1.25rem;
            padding-bottom: 0.5rem;
            border-bottom: 1px solid #eee;
        }
        
        .required-label::after {
            content: " *";
            color: var(--police-danger);
        }
        
        .form-control:focus, .form-select:focus {
            border-color: var(--police-primary);
            box-shadow: 0 0 0 0.25rem rgba(13, 71, 161, 0.25);
        }
        
        .btn-police {
            background-color: var(--police-primary);
            color: white;
            border: none;
            padding: 0.5rem 1.5rem;
            border-radius: 4px;
            font-weight: 500;
        }
        
        .btn-police:hover {
            background-color: var(--police-dark);
            color: white;
        }
        
        .btn-outline-police {
            border: 1px solid var(--police-primary);
            color: var(--police-primary);
            background-color: transparent;
        }
        
        .btn-outline-police:hover {
            background-color: var(--police-light);
        }
        
        .password-toggle {
            cursor: pointer;
            transition: all 0.2s;
        }
        
        .password-toggle:hover {
            color: var(--police-primary);
        }
    </style>
</head>
<body>
    <%@include file="../templates/navbar.jsp" %>
    
    <main class="container form-container">
        <div class="card card-form">
            <div class="card-header card-header-form">
                <h2 class="h5 mb-0">
                    <i class="fas ${empty usuario ? 'fa-user-plus' : 'fa-user-edit'} me-2"></i>
                    ${empty usuario ? 'CADASTRAR NOVO USUÁRIO' : 'EDITAR USUÁRIO'}
                </h2>
            </div>
            
            <div class="card-body">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show mb-4">
                        <i class="fas fa-exclamation-circle me-2"></i>${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/usuarios" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="${empty usuario ? 'inserir' : 'atualizar'}">
                    <c:if test="${not empty usuario}">
                        <input type="hidden" name="id" value="${usuario.id_usuario}">
                    </c:if>

                    <!-- Seção Foto -->
                    <div class="text-center mb-4">
                        <div class="photo-container">
                            <img id="photoPreview" 
                                 src="${not empty usuario.foto_perfil ? 
                                       pageContext.request.contextPath.concat('/').concat(usuario.foto_perfil) : 
                                       'https://via.placeholder.com/150?text=Sem+Foto'}" 
                                 class="photo-preview">
                            <label for="foto_perfil" class="photo-upload">
                                <i class="fas fa-camera"></i>
                                <input type="file" id="foto_perfil" name="foto_perfil" accept="image/*" class="d-none">
                            </label>
                        </div>
                    </div>

                    <!-- Seção Dados Pessoais -->
                    <div class="form-section">
                        <h3 class="section-title">
                            <i class="fas fa-id-card me-2"></i>DADOS PESSOAIS
                        </h3>
                        
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label for="nome" class="form-label required-label">Nome Completo</label>
                                <input type="text" class="form-control" id="nome" name="nome" 
                                       value="${usuario.nome}" required>
                            </div>
                            
                            <div class="col-md-6">
                                <label for="numero_identificacao" class="form-label required-label">Nº Identificação</label>
                                <input type="text" class="form-control" id="numero_identificacao" name="numero_identificacao" 
                                       value="${usuario.numero_identificacao}" required>
                            </div>
                            
                            <div class="col-md-6">
                                <label for="email" class="form-label required-label">Email</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                                    <input type="email" class="form-control" id="email" name="email" 
                                           value="${usuario.email}" required>
                                </div>
                            </div>
                            
                            <div class="col-md-6">
                                <label for="telefone" class="form-label">Telefone</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-phone"></i></span>
                                    <input type="tel" class="form-control" id="telefone" name="telefone" 
                                           value="${usuario.telefone}">
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Seção Dados de Acesso -->
                    <div class="form-section">
                        <h3 class="section-title">
                            <i class="fas fa-lock me-2"></i>DADOS DE ACESSO
                        </h3>
                        
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label for="senha" class="form-label ${empty usuario ? 'required-label' : ''}">
                                    ${empty usuario ? 'Senha' : 'Nova Senha'}
                                </label>
                                <div class="input-group">
                                    <input type="password" class="form-control" id="senha" 
                                           name="senha" ${empty usuario ? 'required' : ''}>
                                    <span class="input-group-text password-toggle" id="toggleSenha">
                                        <i class="fas fa-eye"></i>
                                    </span>
                                </div>
                                <c:if test="${not empty usuario}">
                                    <small class="text-muted">Deixe em branco para manter a senha atual</small>
                                </c:if>
                            </div>
                            
                            <div class="col-md-6">
                                <label for="confirmar_senha" class="form-label ${empty usuario ? 'required-label' : ''}">
                                    Confirmar Senha
                                </label>
                                <div class="input-group">
                                    <input type="password" class="form-control" id="confirmar_senha" 
                                           ${empty usuario ? 'required' : ''}>
                                    <span class="input-group-text password-toggle" id="toggleConfirmar">
                                        <i class="fas fa-eye"></i>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Seção Informações Institucionais -->
                    <div class="form-section">
                        <h3 class="section-title">
                            <i class="fas fa-briefcase me-2"></i>INFORMAÇÕES INSTITUCIONAIS
                        </h3>
                        
                        <div class="row g-3">
                            <div class="col-md-4">
                                <label for="cargo" class="form-label required-label">Cargo</label>
                                <select class="form-select" id="cargo" name="cargo" required>
                                    <option value="">Selecione...</option>
                                    <c:forEach items="${cargos}" var="cargo">
                                        <option value="${cargo}" ${usuario.cargo eq cargo.descricao ? 'selected' : ''}>
                                            ${cargo.descricao}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <div class="col-md-4">
                                <label for="perfil" class="form-label required-label">Perfil</label>
                                <select class="form-select" id="perfil" name="perfil" required>
                                    <option value="">Selecione...</option>
                                    <c:forEach items="${perfis}" var="perfil">
                                        <option value="${perfil}" ${usuario.perfil eq perfil.descricao ? 'selected' : ''}>
                                            ${perfil.descricao}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <div class="col-md-4">
                                <label for="status" class="form-label required-label">Status</label>
                                <select class="form-select" id="status" name="status" required>
                                    <c:forEach items="${statusOptions}" var="status">
                                        <option value="${status.descricao}" ${usuario.status eq status.descricao ? 'selected' : ''}>
                                            ${status.descricao}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <!-- Botões -->
                    <div class="d-flex justify-content-between mt-4">
                        <a href="${pageContext.request.contextPath}/usuarios" 
                           class="btn btn-outline-police">
                            <i class="fas fa-arrow-left me-2"></i> Voltar
                        </a>
                        <button type="submit" class="btn btn-police">
                            <i class="fas fa-save me-2"></i> Salvar
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </main>

     <%@include file="/WEB-INF/views/templates/footer.jsp" %>

    <!-- Scripts -->
    <script>
        // Preview da foto
        document.getElementById('foto_perfil').addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(event) {
                    document.getElementById('photoPreview').src = event.target.result;
                };
                reader.readAsDataURL(file);
            }
        });

        // Mostrar/esconder senha
        document.querySelectorAll('.password-toggle').forEach(btn => {
            btn.addEventListener('click', function() {
                const inputId = this.id === 'toggleSenha' ? 'senha' : 'confirmar_senha';
                const input = document.getElementById(inputId);
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

        // Validação de senha
        document.querySelector('form').addEventListener('submit', function(e) {
            const senha = document.getElementById('senha').value;
            const confirmar = document.getElementById('confirmar_senha').value;
            
            if (senha && senha !== confirmar) {
                e.preventDefault();
                alert('As senhas não coincidem!');
                document.getElementById('senha').focus();
            }
        });
    </script>
</body>
</html>
