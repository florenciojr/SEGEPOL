<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Erro</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .error-container { padding: 20px; background: #f8d7da; border: 1px solid #f5c6cb; border-radius: 5px; }
        .error-message { color: #721c24; }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>Ocorreu um erro</h1>
        <p class="error-message">${error}</p>
        <p><a href="${pageContext.request.contextPath}/cidadao">Voltar para a página inicial</a></p>
    </div>
</body>
</html>
