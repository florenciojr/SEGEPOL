<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Registro de Ocorrência - #${queixa.idQueixa}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        @media print {
            body { padding: 0; background: white; }
            .no-print { display: none !important; }
            .card { border: none; box-shadow: none; }
            .print-header { display: block !important; }
        }
        
        body {
            background-color: #f5f5f5;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .print-header {
            display: none;
            text-align: center;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 3px double #333;
        }
        
        .header-logo {
            height: 80px;
            margin-bottom: 10px;
        }
        
        .protocolo-badge {
            font-size: 1.1rem;
            letter-spacing: 1px;
        }
        
        .card-ocorrencia {
            border-left: 4px solid #2c3e50;
            border-radius: 0;
        }
        
        .card-header-ocorrencia {
            background-color: #2c3e50;
            color: white;
            font-weight: 600;
            padding: 15px 20px;
        }
        
        .info-label {
            font-weight: 600;
            color: #2c3e50;
            min-width: 200px;
        }
        
        .info-value {
            padding: 8px 0;
            border-bottom: 1px dotted #ddd;
        }
        
        .status-badge {
            font-weight: 600;
            padding: 5px 10px;
            border-radius: 3px;
        }
        
        .status-Registrada { background-color: #95a5a6; color: white; }
        .status-Em-Investigação { background-color: #e67e22; color: white; }
        .status-Atribuída { background-color: #3498db; color: white; }
        .status-Resolvida { background-color: #2ecc71; color: white; }
        .status-Cancelada { background-color: #e74c3c; color: white; }
        .status-Arquivada { background-color: #7f8c8d; color: white; }
        
        .assinatura-area {
            margin-top: 50px;
            padding-top: 20px;
            border-top: 1px dashed #333;
            text-align: center;
        }
        
        .action-buttons {
            gap: 10px;
            margin-top: 20px;
        }
        
        .btn-police {
            border-radius: 2px;
            font-weight: 500;
            letter-spacing: 0.5px;
        }
        
        .btn-police-primary {
            background-color: #2c3e50;
            border-color: #2c3e50;
            color: white;
        }
        
        .descricao-box {
            background-color: #f8f9fa;
            border-left: 3px solid #2c3e50;
            padding: 15px;
            margin: 15px 0;
        }
    </style>
</head>
<body>
    <div class="container py-4">
        <!-- Cabeçalho para impressão -->
        <div class="print-header">
            <h3>SECRETARIA DE SEGURANÇA PÚBLICA</h3>
            <h4>REGISTRO DIGITAL DE OCORRÊNCIA</h4>
            <p>Data da impressão: <fmt:formatDate value="${now}" pattern="dd/MM/yyyy HH:mm" /></p>
        </div>

        <div class="d-flex justify-content-between align-items-center mb-4 no-print">
            <div>
                <h2 class="h4 mb-0 text-uppercase" style="color: #2c3e50;">
                    <i class="bi bi-clipboard2-data"></i> Detalhes da Ocorrência
                </h2>
            </div>
            <div>
                <span class="badge protocolo-badge bg-dark">
                    Protocolo: #${queixa.idQueixa}
                </span>
            </div>
        </div>

        <c:if test="${empty queixa}">
            <div class="alert alert-danger">Ocorrência não encontrada</div>
            <a href="queixas" class="btn btn-police btn-police-primary no-print">
                <i class="bi bi-arrow-left"></i> Voltar para a lista
            </a>
        </c:if>

        <c:if test="${not empty queixa}">
            <div class="card card-ocorrencia mb-4">
                <div class="card-header card-header-ocorrencia">
                    <h3 class="h5 mb-0">${queixa.titulo}</h3>
                </div>
                
                <div class="card-body">
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <dl class="row">
                                <dt class="col-sm-5 info-label">Status:</dt>
                                <dd class="col-sm-7 info-value">
                                    <span class="status-badge status-${queixa.status.replace(' ', '-')}">
                                        ${queixa.status}
                                    </span>
                                </dd>
                                
<dt class="col-sm-5 info-label">Data do Incidente:</dt>
<dd class="col-sm-7 info-value">
    <fmt:formatDate value="${queixa.dataIncidenteAsDate}" pattern="dd/MM/yyyy" />
</dd>

<dt class="col-sm-5 info-label">Data do Registro:</dt>
<dd class="col-sm-7 info-value">
    <fmt:formatDate value="${queixa.dataRegistroAsDate}" pattern="dd/MM/yyyy HH:mm" />
</dd>
                                <dt class="col-sm-5 info-label">Local do Incidente:</dt>
                                <dd class="col-sm-7 info-value">${queixa.localIncidente}</dd>
                            </dl>
                        </div>
                        
                        <div class="col-md-6">
                            <dl class="row">
                                <c:if test="${not empty queixa.coordenadas}">
                                    <dt class="col-sm-5 info-label">Coordenadas:</dt>
                                    <dd class="col-sm-7 info-value">${queixa.coordenadas}</dd>
                                </c:if>
                                
                                <dt class="col-sm-5 info-label">Cidadão:</dt>
                                <dd class="col-sm-7 info-value">
                                    ${not empty queixa.nomeCidadao ? queixa.nomeCidadao : 'Não informado'}
                                </dd>
                                
                                <dt class="col-sm-5 info-label">Tipo de Ocorrência:</dt>
                                <dd class="col-sm-7 info-value">
                                    ${not empty queixa.nomeTipoQueixa ? queixa.nomeTipoQueixa : 'Não especificado'}
                                </dd>
                                
                                <dt class="col-sm-5 info-label">Responsável:</dt>
                                <dd class="col-sm-7 info-value">
                                    ${not empty queixa.nomeUsuario ? queixa.nomeUsuario : 'Não atribuído'}
                                </dd>
                            </dl>
                        </div>
                    </div>
                    
                    <div class="descricao-box">
                        <h5 class="mb-3" style="color: #2c3e50;">
                            <i class="bi bi-journal-text"></i> Descrição da Ocorrência
                        </h5>
                        <p>${queixa.descricao}</p>
                    </div>
                    
                    <!-- Área de assinatura para impressão -->
                    <div class="assinatura-area no-screen">
                        <div style="height: 80px;"></div>
                        <p>_________________________________________</p>
                        <p>Assinatura do Responsável</p>
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-between action-buttons no-print">
                <div>
                    <button onclick="window.print()" class="btn btn-police btn-outline-dark">
                        <i class="bi bi-printer"></i> Imprimir
                    </button>
                </div>
                <div>
                    <c:if test="${queixa.status != 'Cancelada' && queixa.status != 'Resolvida'}">
                        <a href="queixas?action=resolve&id=${queixa.idQueixa}" class="btn btn-police btn-success me-2">
                            <i class="bi bi-check-circle"></i> Resolver
                        </a>
                    </c:if>
                    <c:if test="${queixa.status != 'Cancelada'}">
                        <a href="queixas?action=cancel&id=${queixa.idQueixa}" class="btn btn-police btn-danger me-2">
                            <i class="bi bi-x-circle"></i> Cancelar
                        </a>
                    </c:if>
                    <a href="queixas?action=edit&id=${queixa.idQueixa}" class="btn btn-police btn-warning">
                        <i class="bi bi-pencil"></i> Editar
                    </a>
                </div>
            </div>
        </c:if>
    </div>

    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Adiciona a data atual para o cabeçalho de impressão
        document.addEventListener('DOMContentLoaded', function() {
            const now = new Date();
            const nowElement = document.createElement('span');
            nowElement.id = 'current-date';
            nowElement.textContent = now.toLocaleString();
            document.querySelector('.print-header p').appendChild(nowElement);
        });
    </script>
</body>
</html>
