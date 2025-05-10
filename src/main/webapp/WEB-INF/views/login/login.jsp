<%-- 
    Document   : login
    Created on : 3 de mai de 2025, 12:19:40
    Author     : JR5
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login - Sistema</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .login-container {
            max-width: 400px;
            margin: 0 auto;
            margin-top: 5rem;
        }
        .password-container {
            position: relative;
        }
        .toggle-password {
            position: absolute;
            right: 10px;
            top: 50%;
            transform: translateY(-50%);
            cursor: pointer;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="login-container">
            <div class="card shadow">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0 text-center">Acesso ao Sistema</h4>
                </div>
                <div class="card-body">
                    <c:if test="${not empty param.timeout}">
                        <div class="alert alert-warning">Sua sessão expirou por inatividade. Faça login novamente.</div>
                    </c:if>
                    
                    <c:if test="${not empty erro}">
                        <div class="alert alert-danger">${erro}</div>
                    </c:if>
                    
                    <form id="loginForm" action="${pageContext.request.contextPath}/login" method="post">
                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                        
                        <div class="mb-3">
                            <label for="identificacao" class="form-label">Usuário ou Email</label>
                            <input type="text" class="form-control" id="identificacao" name="identificacao" 
                                   required autofocus autocomplete="username">
                        </div>
                        
                        <div class="mb-3 password-container">
                            <label for="senha" class="form-label">Senha</label>
                            <input type="password" class="form-control" id="senha" name="senha" 
                                   required autocomplete="current-password">
                            <span class="toggle-password" onclick="togglePassword()">
                                <i class="bi bi-eye"></i>
                            </span>
                        </div>
                        
                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary">Entrar</button>
                        </div>
                    </form>
                    
                    <div class="mt-3 text-center">
                        <a href="${pageContext.request.contextPath}/recuperar-senha">Esqueci minha senha</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        function togglePassword() {
            const passwordField = document.getElementById('senha');
            const icon = document.querySelector('.toggle-password i');
            
            if (passwordField.type === 'password') {
                passwordField.type = 'text';
                icon.classList.remove('bi-eye');
                icon.classList.add('bi-eye-slash');
            } else {
                passwordField.type = 'password';
                icon.classList.remove('bi-eye-slash');
                icon.classList.add('bi-eye');
            }
        }
        
        // Tratamento para evitar reenvio do formulário
        document.getElementById('loginForm').addEventListener('submit', function() {
            const submitBtn = this.querySelector('button[type="submit"]');
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Autenticando...';
        });
    </script>
</body>
</html>
