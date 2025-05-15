<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Erro no Banco de Dados</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <div class="alert alert-danger">
            <h2>Erro no Banco de Dados</h2>
            <p>Ocorreu um erro ao acessar o banco de dados.</p>
            <c:if test="${not empty errorDetail}">
                <p><small>Detalhes técnicos: ${errorDetail}</small></p>
            </c:if>
        </div>
        <a href="denunciantes" class="btn btn-primary">Voltar</a>
    </div>
</body>
</html>
