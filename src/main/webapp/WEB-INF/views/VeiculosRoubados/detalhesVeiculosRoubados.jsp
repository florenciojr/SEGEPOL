<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Detalhes do Veículo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .foto-detalhes {
            max-width: 100%;
            max-height: 300px;
            border-radius: 5px;
        }
        .detalhes-card {
            max-width: 800px;
            margin: 0 auto;
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <div class="detalhes-card card">
            <div class="card-header">
                <h2 class="mb-0">Detalhes do Veículo</h2>
            </div>
            <div class="card-body">
                <div class="row">
<div class="col-md-4">
    <c:if test="${not empty veiculo.fotoVeiculo}">
        <img src="${pageContext.request.contextPath}/${veiculo.fotoVeiculo}" class="foto-detalhes mb-3">
    </c:if>
</div>
                    <div class="col-md-8">
                        <dl class="row">
                            <dt class="col-sm-3">Matrícula:</dt>
                            <dd class="col-sm-9">${veiculo.matricula}</dd>
                            
                            <dt class="col-sm-3">Marca/Modelo:</dt>
                            <dd class="col-sm-9">${veiculo.marca} ${veiculo.modelo}</dd>
                            
                            <dt class="col-sm-3">Cor:</dt>
                            <dd class="col-sm-9">${veiculo.cor}</dd>
                            
                            <dt class="col-sm-3">Ano:</dt>
                            <dd class="col-sm-9">${veiculo.ano}</dd>
                            
                            <dt class="col-sm-3">ID Queixa:</dt>
                            <dd class="col-sm-9">${veiculo.idQueixa}</dd>
                            
                            <dt class="col-sm-3">Data Registro:</dt>
                            <dd class="col-sm-9">${veiculo.dataRegistro}</dd>
                        </dl>
                    </div>
                </div>
                
                <div class="mt-4">
                    <a href="veiculos?action=editar&id=${veiculo.idVeiculo}" class="btn btn-warning">
                        <i class="bi bi-pencil"></i> Editar
                    </a>
                    <a href="veiculos" class="btn btn-secondary ms-2">
                        <i class="bi bi-arrow-left"></i> Voltar
                    </a>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
</body>
</html>
