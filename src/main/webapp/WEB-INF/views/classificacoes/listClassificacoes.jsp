<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Histórico de Classificações</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        .badge-vitima { background-color: #28a745; }
        .badge-suspeito { background-color: #dc3545; }
        .badge-detido { background-color: #dc3545; }
        .badge-testemunha { background-color: #6c757d; }
        .badge-informante { background-color: #007bff; }
        .badge-denunciante { background-color: #17a2b8; }
        .badge-comum { background-color: #f8f9fa; color: #212529; }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h1 class="mb-4">Histórico de Classificações</h1>

        <c:if test="${not empty nomeCidadao}">
            <div class="alert alert-info">
                <h4>Histórico para: ${nomeCidadao}</h4>
            </div>
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        
        <div class="mb-3">
            <c:choose>
                <c:when test="${not empty idCidadao}">
                    <a href="classificacoes?action=new&idCidadao=${idCidadao}" class="btn btn-primary">
                        <i class="bi bi-plus-circle"></i> Registrar Nova Classificação
                    </a>
                    <a href="cidadaos?action=view&id=${idCidadao}" class="btn btn-secondary ms-2">
                        <i class="bi bi-arrow-left"></i> Voltar ao Cidadão
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="classificacoes?action=new" class="btn btn-primary">
                        <i class="bi bi-plus-circle"></i> Registrar Nova Classificação
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
        
        <div class="table-responsive">
            <table class="table table-striped table-bordered table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Cidadão</th>
                        <th>Classificação</th>
                        <th>Observações</th>
                        <th>Data/Hora</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="classificacao" items="${listClassificacoes}">
                        <c:set var="classificacaoLower" value="${fn:toLowerCase(classificacao.classificacao)}"/>
                        <tr>
                            <td>${classificacao.idClassificacao}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty classificacao.nomeCidadao}">
                                        <a href="cidadaos?action=view&id=${classificacao.idCidadao}" class="text-decoration-none">
                                            ${classificacao.nomeCidadao} (ID: ${classificacao.idCidadao})
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        ID: ${classificacao.idCidadao}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <span class="badge badge-${classificacaoLower}">
                                    ${classificacao.classificacao}
                                </span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty classificacao.observacoes}">
                                        <c:choose>
                                            <c:when test="${fn:length(classificacao.observacoes) > 50}">
                                                ${fn:substring(classificacao.observacoes, 0, 50)}...
                                                <button class="btn btn-sm btn-link" data-bs-toggle="tooltip" 
                                                    title="${classificacao.observacoes}">
                                                    <i class="bi bi-eye"></i>
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                ${classificacao.observacoes}
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        Nenhuma observação
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <fmt:formatDate value="${classificacao.dataClassificacao}" pattern="dd/MM/yyyy HH:mm" />
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty listClassificacoes}">
                        <tr>
                            <td colspan="5" class="text-center">Nenhum registro de classificação encontrado</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Ativa tooltips
            var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
            var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl);
            });
            
            // Adiciona classe de cor baseada na classificação
            document.querySelectorAll('.badge').forEach(function(badge) {
                const text = badge.textContent.toLowerCase();
                if (text.includes('vítima')) {
                    badge.classList.add('badge-vitima');
                } else if (text.includes('suspeito') || text.includes('detido')) {
                    badge.classList.add('badge-suspeito');
                } else if (text.includes('testemunha')) {
                    badge.classList.add('badge-testemunha');
                } else if (text.includes('informante')) {
                    badge.classList.add('badge-informante');
                } else if (text.includes('denunciante')) {
                    badge.classList.add('badge-denunciante');
                } else {
                    badge.classList.add('badge-comum');
                }
            });
        });
    </script>
</body>
</html>
