<%-- 
    Document   : login
    Created on : 3 de mai de 2025, 12:19:40
    Author     : JR5
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Acesso ao Sistema</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .login-container {
            max-width: 400px;
            margin: 100px auto;
            padding: 20px;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
        }
        .logo {
            text-align: center;
            margin-bottom: 20px;
        }
        .logo img {
            max-width: 150px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="login-container">
            <div class="logo">
                <img src="${pageContext.request.contextPath}/assets/img/logo.png" alt="Logo do Sistema">
            </div>
            
            <c:if test="${not empty erro}">
                <div class="alert alert-danger">${erro}</div>
            </c:if>
            
            <c:if test="${not empty sucesso}">
                <div class="alert alert-success">${sucesso}</div>
            </c:if>
            
            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="mb-3">
                    <label for="identificacao" class="form-label">Número de Identificação ou Email</label>
                    <input type="text" class="form-control" id="identificacao" name="identificacao" required>
                </div>
                <div class="mb-3">
                    <label for="senha" class="form-label">Senha</label>
                    <input type="password" class="form-control" id="senha" name="senha" required>
                </div>
                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-primary">Entrar</button>
                </div>
                <div class="mt-3 text-center">
                    <a href="${pageContext.request.contextPath}/recuperar-senha">Esqueci minha senha</a>
                </div>
            </form>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>