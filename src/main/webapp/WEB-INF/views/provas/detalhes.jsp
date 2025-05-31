<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
        :root {
            --primary-color: #4e73df;
            --secondary-color: #f8f9fc;
            --text-color: #5a5c69;
        }
        
        body {
            background-color: #f8f9fc;
            color: var(--text-color);
        }
        
        .card-detail {
            border: none;
            border-radius: 0.5rem;
            box-shadow: 0 0.15rem 1.75rem 0 rgba(58, 59, 69, 0.15);
            margin-bottom: 1.5rem;
        }
        
        .card-header {
            background-color: var(--primary-color);
            color: white;
            border-radius: 0.5rem 0.5rem 0 0 !important;
            padding: 1rem 1.5rem;
        }
        
        .detail-item {
            padding: 1rem 1.5rem;
            border-bottom: 1px solid #e3e6f0;
        }
        
        .detail-item:last-child {
            border-bottom: none;
        }
        
        .detail-label {
            font-weight: 600;
            color: var(--text-color);
        }
        
        .file-card {
            transition: all 0.3s ease;
            border: 1px solid #e3e6f0;
            border-radius: 0.35rem;
        }
        
        .file-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
        }
        
        .file-preview {
            height: 150px;
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: #f8f9fc;
            border-radius: 0.35rem 0.35rem 0 0;
            overflow: hidden;
        }
        
        .file-preview img {
            max-height: 100%;
            max-width: 100%;
            object-fit: contain;
        }
        
        .file-actions {
            padding: 0.75rem;
            background-color: white;
            border-radius: 0 0 0.35rem 0.35rem;
        }
        
        .badge-status {
            font-size: 0.85rem;
            padding: 0.35em 0.65em;
        }
    </style>
</head>
<body>
    <div class="container py-4">
        <!-- Cabeçalho -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="h4 text-gray-800 mb-0">
                    <i class="bi bi-file-earmark-medical me-2"></i>Detalhes da Prova
                </h2>
                <nav aria-label="breadcrumb" class="mt-2">
                    <ol class="breadcrumb small">
                        <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                        <li class="breadcrumb-item"><a href="provas">Provas</a></li>
                        <li class="breadcrumb-item active" aria-current="page">Detalhes</li>
                    </ol>
                </nav>
            </div>
            <a href="provas" class="btn btn-outline-secondary">
                <i class="bi bi-arrow-left me-1"></i> Voltar
            </a>
        </div>

        <!-- Card principal -->
        <div class="card card-detail mb-4">
            <div class="card-header">
                <div class="d-flex justify-content-between align-items-center">
                    <h5 class="m-0 font-weight-bold">
                        <i class="bi bi-info-circle me-2"></i>Prova #${prova.idProva}
                    </h5>
                    <div>
                        <a href="provas?action=editar&id=${prova.idProva}" class="btn btn-sm btn-light">
                            <i class="bi bi-pencil me-1"></i> Editar
                        </a>
                    </div>
                </div>
            </div>
            <div class="card-body p-0">
                <!-- Informações básicas -->
                <div class="detail-item">
                    <h5 class="text-primary mb-3">${prova.tituloQueixa}</h5>
                    <p class="mb-0">${prova.descricao}</p>
                </div>
                
                <!-- Detalhes em grid -->
                <div class="row g-0">
                    <div class="col-md-6">
                        <div class="detail-item">
                            <dl class="row mb-0">
                                <dt class="col-sm-4 detail-label">Tipo:</dt>
                                <dd class="col-sm-8">${prova.tipo}</dd>

                                <dt class="col-sm-4 detail-label">Data Coleta:</dt>
                                <dd class="col-sm-8">${prova.dataColeta}</dd>

                                <dt class="col-sm-4 detail-label">Data Upload:</dt>
                                <dd class="col-sm-8">${prova.dataUpload}</dd>
                            </dl>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="detail-item">
                            <dl class="row mb-0">
                                <dt class="col-sm-4 detail-label">Cadastrado por:</dt>
                                <dd class="col-sm-8">${prova.nomeUsuario}</dd>

                                <dt class="col-sm-4 detail-label">Status:</dt>
                                <dd class="col-sm-8">
                                    <span class="badge bg-success badge-status">Ativo</span>
                                </dd>
                            </dl>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Card de arquivos -->
        <div class="card card-detail">
            <div class="card-header">
                <h5 class="m-0 font-weight-bold">
                    <i class="bi bi-file-earmark-image me-2"></i> Arquivos da Prova
                </h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty prova.caminhoArquivo}">
                        <div class="row g-3">
                            <c:forEach items="${prova.caminhoArquivo.split(',')}" var="arquivo" varStatus="loop">
                                <div class="col-md-4 col-lg-3">
                                    <div class="file-card h-100">
                                        <div class="file-preview">
                                            <img src="${arquivo}" class="img-fluid" alt="Arquivo ${loop.index + 1}">
                                        </div>
                                        <div class="file-actions text-center">
                                            <a href="download?file=${fn:escapeXml(arquivo)}" 
                                               class="btn btn-sm btn-primary w-100">
                                                <i class="bi bi-download me-1"></i> Baixar
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-info mb-0">
                            <i class="bi bi-info-circle me-2"></i> Nenhum arquivo associado a esta prova.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
