<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalhes da Prova | Sistema de Provas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        .card-detalhes {
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            border: none;
        }
        .card-header-custom {
            background-color: #4e73df;
            color: white;
            border-radius: 10px 10px 0 0 !important;
        }
        .detail-label {
            font-weight: 600;
            color: #5a5c69;
        }
        .preview-img {
            max-width: 150px;
            max-height: 150px;
            margin: 5px;
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 5px;
        }
    </style>
</head>
<body class="bg-light">
    <div class="container py-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1 class="h3 text-gray-800">
                <i class="bi bi-file-earmark-medical"></i> Detalhes da Prova
            </h1>
            <a href="provas" class="btn btn-secondary">
                <i class="bi bi-arrow-left"></i> Voltar
            </a>
        </div>

        <div class="card card-detalhes mb-4">
            <div class="card-header card-header-custom py-3">
                <h5 class="m-0 font-weight-bold">
                    <i class="bi bi-info-circle"></i> Informações da Prova #${prova.idProva}
                </h5>
            </div>
            <div class="card-body">
                <div class="row mb-4">
                    <div class="col-md-6">
                        <h5 class="text-primary">${prova.tituloQueixa}</h5>
                        <p class="text-muted">${prova.descricao}</p>
                    </div>
                    <div class="col-md-6">
                        <div class="d-flex justify-content-end">
                            <a href="provas?action=editar&id=${prova.idProva}" class="btn btn-warning me-2">
                                <i class="bi bi-pencil"></i> Editar
                            </a>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <dl class="row">
                            <dt class="col-sm-4 detail-label">Tipo:</dt>
                            <dd class="col-sm-8">${prova.tipo}</dd>

                            <dt class="col-sm-4 detail-label">Data Coleta:</dt>
                            <dd class="col-sm-8">${prova.dataColeta}</dd>

                            <dt class="col-sm-4 detail-label">Data Upload:</dt>
                            <dd class="col-sm-8">${prova.dataUpload}</dd>
                        </dl>
                    </div>
                    <div class="col-md-6">
                        <dl class="row">
                            <dt class="col-sm-4 detail-label">Cadastrado por:</dt>
                            <dd class="col-sm-8">${prova.nomeUsuario}</dd>

                            <dt class="col-sm-4 detail-label">Status:</dt>
                            <dd class="col-sm-8">
                                <span class="badge bg-success">Ativo</span>
                            </dd>
                        </dl>
                    </div>
                </div>
            </div>
        </div>

        <div class="card card-detalhes">
            <div class="card-header card-header-custom py-3">
                <h5 class="m-0 font-weight-bold">
                    <i class="bi bi-file-earmark-image"></i> Arquivos da Prova
                </h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty prova.caminhoArquivo}">
                        <div class="d-flex flex-wrap">
                            <c:forEach items="${prova.caminhoArquivo.split(',')}" var="arquivo" varStatus="loop">
                                <div class="me-4 mb-4 text-center">
                                    <img src="${arquivo}" class="preview-img img-thumbnail" alt="Prova ${loop.index + 1}">
                                    <div class="mt-2">
                                        <a href="download?file=${arquivo}" class="btn btn-sm btn-primary">
                                            <i class="bi bi-download"></i> Baixar
                                        </a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-info">
                            Nenhum arquivo associado a esta prova.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
