<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Erro no Sistema</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        .error-container {
            max-width: 600px;
            margin: 0 auto;
            padding: 2rem;
        }
        
        .error-icon {
            font-size: 5rem;
            color: #dc3545;
        }
    </style>
</head>
<body class="bg-light">
    <div class="d-flex align-items-center justify-content-center vh-100">
        <div class="error-container text-center">
            <div class="error-icon mb-4">
                <i class="fas fa-exclamation-triangle"></i>
            </div>
            <h1 class="display-5 fw-bold text-danger">Ocorreu um erro</h1>
            <div class="alert alert-danger mt-4">
                <i class="fas fa-bug me-2"></i>
                <strong>Detalhes:</strong> ${error}
            </div>
            <div class="mt-4">
                <a href="${pageContext.request.contextPath}/mandados?action=list" class="btn btn-primary btn-lg">
                    <i class="fas fa-home me-2"></i> Voltar para Página Inicial
                </a>
            </div>
            <div class="mt-3 text-muted">
                <small>Se o problema persistir, entre em contato com o administrador do sistema.</small>
            </div>
        </div>
    </div>

    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
