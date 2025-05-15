<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Detalhes da Vítima</title>
    <style>
        .details { max-width: 600px; margin: 0 auto; }
        .detail-row { margin-bottom: 10px; }
        .detail-label { font-weight: bold; }
        .actions { margin-top: 20px; }
    </style>
</head>
<body>
    <div class="details">
        <h1>Detalhes da Vítima</h1>
        
        <div class="detail-row">
            <span class="detail-label">ID:</span>
            <span>${vitima.idVitima}</span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Queixa:</span>
            <span>${vitima.idQueixa}</span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Cidadão:</span>
            <span>${vitima.idCidadao}</span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Tipo:</span>
            <span>${vitima.tipoVitima}</span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Descrição:</span>
            <p>${vitima.descricao}</p>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Data Registro:</span>
            <span>${vitima.dataRegistro}</span>
        </div>
        
        <div class="actions">
            <a href="${pageContext.request.contextPath}/vitimas?action=edit&id=${vitima.idVitima}">Editar</a>
            <a href="${pageContext.request.contextPath}/vitimas?action=delete&id=${vitima.idVitima}" 
               onclick="return confirm('Tem certeza que deseja excluir?')">Excluir</a>
            <a href="${pageContext.request.contextPath}/vitimas">Voltar</a>
        </div>
    </div>
</body>
</html>
