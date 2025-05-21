<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Veículos Roubados</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .foto-thumbnail {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border-radius: 5px;
        }
        .actions-column {
            white-space: nowrap;
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h1 class="mb-4">Veículos Roubados</h1>
        
        <c:if test="${not empty sucesso}">
            <div class="alert alert-success">${sucesso}</div>
        </c:if>
        <c:if test="${not empty erro}">
            <div class="alert alert-danger">${erro}</div>
        </c:if>
        
        <div class="d-flex justify-content-between mb-3">
            <a href="veiculos?action=novo" class="btn btn-primary">
                <i class="bi bi-plus-circle"></i> Novo Veículo
            </a>
            
            <form class="d-flex" action="veiculos" method="GET">
                <input type="hidden" name="action" value="buscar">
                <input type="text" name="matricula" class="form-control me-2" placeholder="Buscar por matrícula">
                <button type="submit" class="btn btn-outline-secondary">
                    <i class="bi bi-search"></i> Buscar
                </button>
            </form>
        </div>
        
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>Foto</th>
                        <th>Matrícula</th>
                        <th>Marca</th>
                        <th>Modelo</th>
                        <th>Cor</th>
                        <th>Ano</th>
                        <th>Data Registro</th>
                        <th class="actions-column">Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${veiculos}" var="veiculo">
                        <tr>
                           <td>
    <c:if test="${not empty veiculo.fotoVeiculo}">
        <img src="${pageContext.request.contextPath}/${veiculo.fotoVeiculo}" class="foto-thumbnail" alt="Foto do veículo">
    </c:if>
</td>
                            <td>${veiculo.matricula}</td>
                            <td>${veiculo.marca}</td>
                            <td>${veiculo.modelo}</td>
                            <td>${veiculo.cor}</td>
                            <td>${veiculo.ano}</td>
                            <td>${veiculo.dataRegistro}</td>
                            <td class="actions-column">
                                <a href="veiculos?action=detalhes&id=${veiculo.idVeiculo}" class="btn btn-sm btn-info" title="Detalhes">
                                    <i class="bi bi-eye"></i>
                                </a>
                                <a href="veiculos?action=editar&id=${veiculo.idVeiculo}" class="btn btn-sm btn-warning" title="Editar">
                                    <i class="bi bi-pencil"></i>
                                </a>
                                <form action="veiculos" method="POST" style="display: inline;">
                                    <input type="hidden" name="action" value="deletar">
                                    <input type="hidden" name="id" value="${veiculo.idVeiculo}">
                                    <button type="submit" class="btn btn-sm btn-danger" title="Remover" onclick="return confirm('Tem certeza que deseja remover este veículo?')">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
</body>
</html>
