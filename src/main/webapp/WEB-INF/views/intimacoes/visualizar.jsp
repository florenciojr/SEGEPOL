<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${not empty pageTitle ? pageTitle : 'Detalhes da Intimação'}</title>
    <style>
        /* Estilos para tela */
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            padding: 25px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: white;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            color: #2c3e50;
            border-bottom: 2px solid #eee;
            padding-bottom: 10px;
            margin-top: 0;
        }
        .detail-row {
            margin-bottom: 15px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eee;
            display: flex;
            flex-wrap: wrap;
        }
        .detail-label {
            font-weight: bold;
            color: #34495e;
            width: 200px;
            flex-shrink: 0;
        }
        .detail-value {
            flex-grow: 1;
            min-width: 200px;
        }
        .actions {
            margin-top: 30px;
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }
        .btn {
            padding: 10px 15px;
            text-decoration: none;
            border-radius: 4px;
            font-weight: bold;
            transition: all 0.3s;
            border: none;
            cursor: pointer;
            font-size: 14px;
        }
        .btn-edit {
            background-color: #3498db;
            color: white;
        }
        .btn-edit:hover {
            background-color: #2980b9;
        }
        .btn-back {
            background-color: #95a5a6;
            color: white;
        }
        .btn-back:hover {
            background-color: #7f8c8d;
        }
        .btn-print {
            background-color: #9b59b6;
            color: white;
        }
        .btn-print:hover {
            background-color: #8e44ad;
        }
        .status {
            font-weight: bold;
            padding: 4px 8px;
            border-radius: 4px;
            display: inline-block;
        }
        .status-pendente {
            background-color: #fdebd0;
            color: #ba4a00;
        }
        .status-compareceu {
            background-color: #d5f5e3;
            color: #1e8449;
        }
        .status-nao-compareceu {
            background-color: #fadbd8;
            color: #c0392b;
        }
        .status-adiado {
            background-color: #eaf2f8;
            color: #2874a6;
        }
        .empty-value {
            color: #7f8c8d;
            font-style: italic;
        }
        .print-only {
            display: none;
        }
        @media (max-width: 600px) {
            .detail-label, .detail-value {
                width: 100%;
            }
            .detail-label {
                margin-bottom: 5px;
            }
        }

        /* Estilos para impressão */
        @media print {
            body {
                background-color: white;
                font-size: 12pt;
                margin: 0;
                padding: 0;
            }
            .container {
                border: none;
                box-shadow: none;
                padding: 1cm;
                max-width: 100%;
            }
            .no-print {
                display: none !important;
            }
            .print-only {
                display: block;
            }
            .actions, .btn {
                display: none !important;
            }
            .detail-row {
                page-break-inside: avoid;
                margin-bottom: 10px;
                padding-bottom: 10px;
            }
            .print-header {
                text-align: center;
                margin-bottom: 20px;
                border-bottom: 2px solid #000;
                padding-bottom: 10px;
            }
            .print-footer {
                text-align: center;
                margin-top: 20px;
                border-top: 1px solid #000;
                padding-top: 10px;
                font-size: 10pt;
            }
            .detail-label {
                width: 180px;
                color: black;
            }
            .detail-value {
                color: black;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Cabeçalho visível apenas na impressão -->
        <div class="print-header print-only">
            <h2>${not empty pageTitle ? pageTitle : 'Detalhes da Intimação'}</h2>
            <p>Emitido em: <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy 'às' HH:mm" /></p>
        </div>
        
        <!-- Título visível apenas na tela -->
        <h1 class="no-print">${not empty pageTitle ? pageTitle : 'Detalhes da Intimação'}</h1>
        
        <!-- Conteúdo principal -->
        <div class="detail-row">
            <span class="detail-label">Número da Intimação:</span>
            <span class="detail-value">${not empty intimacao.idIntimacao ? intimacao.idIntimacao : '<span class="empty-value">Não informado</span>'}</span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Cidadão:</span>
            <span class="detail-value">
                <c:choose>
                    <c:when test="${not empty intimacao.nomeCidadao}">
                        ${intimacao.nomeCidadao} <span class="empty-value">(ID: ${intimacao.idCidadao})</span>
                    </c:when>
                    <c:otherwise>
                        <span class="empty-value">Cidadão ID: ${intimacao.idCidadao}</span>
                    </c:otherwise>
                </c:choose>
            </span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Queixa Relacionada:</span>
            <span class="detail-value">
                <c:choose>
                    <c:when test="${not empty intimacao.tituloQueixa}">
                        ${intimacao.tituloQueixa} <span class="empty-value">(ID: ${intimacao.idQueixa})</span>
                    </c:when>
                    <c:otherwise>
                        <span class="empty-value">Queixa ID: ${intimacao.idQueixa}</span>
                    </c:otherwise>
                </c:choose>
            </span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Motivo:</span>
            <span class="detail-value">${not empty intimacao.motivo ? intimacao.motivo : '<span class="empty-value">Não informado</span>'}</span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Data de Emissão:</span>
            <span class="detail-value">
                <c:choose>
                    <c:when test="${not empty intimacao.dataEmissao}">
                        <fmt:parseDate value="${intimacao.dataEmissao.toString()}" pattern="yyyy-MM-dd" var="parsedEmissao" />
                        <fmt:formatDate value="${parsedEmissao}" pattern="dd/MM/yyyy" />
                    </c:when>
                    <c:otherwise>
                        <span class="empty-value">Não definida</span>
                    </c:otherwise>
                </c:choose>
            </span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Data de Comparecimento:</span>
            <span class="detail-value">
                <c:choose>
                    <c:when test="${not empty intimacao.dataComparecimento}">
                        <fmt:parseDate value="${intimacao.dataComparecimento.toString()}" pattern="yyyy-MM-dd" var="parsedComparecimento" />
                        <fmt:formatDate value="${parsedComparecimento}" pattern="dd/MM/yyyy" />
                    </c:when>
                    <c:otherwise>
                        <span class="empty-value">Não definida</span>
                    </c:otherwise>
                </c:choose>
            </span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Local de Comparecimento:</span>
            <span class="detail-value">${not empty intimacao.localComparecimento ? intimacao.localComparecimento : '<span class="empty-value">Não informado</span>'}</span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Status:</span>
            <span class="detail-value">
                <c:choose>
                    <c:when test="${not empty intimacao.status}">
                        <span class="status status-${fn:replace(fn:toLowerCase(intimacao.status), 'ã', 'a').replace(' ', '-')}">
                            ${intimacao.status}
                        </span>
                    </c:when>
                    <c:otherwise>
                        <span class="empty-value">Indefinido</span>
                    </c:otherwise>
                </c:choose>
            </span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Observações:</span>
            <span class="detail-value">
                <c:choose>
                    <c:when test="${not empty intimacao.observacoes}">
                        ${intimacao.observacoes}
                    </c:when>
                    <c:otherwise>
                        <span class="empty-value">Nenhuma observação registrada</span>
                    </c:otherwise>
                </c:choose>
            </span>
        </div>
        
        <div class="detail-row">
            <span class="detail-label">Registrado por:</span>
            <span class="detail-value">
                <c:choose>
                    <c:when test="${not empty intimacao.nomeUsuario}">
                        ${intimacao.nomeUsuario} <span class="empty-value">(ID: ${intimacao.idUsuario})</span>
                    </c:when>
                    <c:otherwise>
                        <span class="empty-value">Usuário ID: ${intimacao.idUsuario}</span>
                    </c:otherwise>
                </c:choose>
            </span>
        </div>
        
        <!-- Rodapé visível apenas na impressão -->
        <div class="print-footer print-only">
            <p>Sistema de Intimações - ${not empty intimacao.nomeUsuario ? intimacao.nomeUsuario : 'Usuário'} - Página <span class="page-number"></span></p>
        </div>
        
        <!-- Ações visíveis apenas na tela -->
        <div class="actions no-print">
            <a href="intimacoes?action=edit&id=${intimacao.idIntimacao}" class="btn btn-edit">✏️ Editar Intimação</a>
            <a href="intimacoes?action=list" class="btn btn-back">↩️ Voltar para Lista</a>
            <button onclick="window.print()" class="btn btn-print">🖨️ Imprimir Intimação</button>
        </div>
    </div>

    <script>
        // Adiciona numeração de páginas na impressão
        window.onbeforeprint = function() {
            var pages = document.getElementsByClassName('page-number');
            for (var i = 0; i < pages.length; i++) {
                pages[i].textContent = (i + 1);
            }
        };
        
        // Opcional: Imprimir automaticamente ao carregar a página
        // Descomente as linhas abaixo se quiser que imprima automaticamente
        /*
        window.onload = function() {
            setTimeout(function() {
                window.print();
            }, 500); // Pequeno delay para garantir que tudo carregou
        };
        */
    </script>
</body>
</html>
