<%-- 
    Document   : listarCidadaos
    Created on : 4 de mai de 2025, 00:47:42
    Author     : JR5
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista de Cidadãos | PRM</title>
    <%@include file="/WEB-INF/views/templates/header.jsp" %>
<c:out value="[DEBUG: ${cidadao.classificacao}]"/>
    <style>
        :root {
            --prm-verde: #006633;
            --prm-amarelo: #FFD700;
            --prm-preto: #000000;
            --prm-vermelho: #CC0000;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
            padding: 20px;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            border-top: 5px solid var(--prm-vermelho);
        }
        
        .header-title {
            color: var(--prm-verde);
            border-bottom: 3px solid var(--prm-amarelo);
            padding-bottom: 10px;
            margin-bottom: 30px;
            font-weight: 700;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        /* Barra de ações */
        .action-bar {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
            flex-wrap: wrap;
            gap: 15px;
        }
        
        .btn-prm {
            background-color: var(--prm-verde);
            color: white;
            padding: 10px 20px;
            border-radius: 4px;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            font-weight: 600;
            transition: all 0.3s;
        }
        
        .btn-prm:hover {
            background-color: #005a2b;
            transform: translateY(-2px);
        }
        
        /* Barra de pesquisa */
        .search-box {
            display: flex;
            gap: 10px;
            align-items: center;
        }
        
        .search-box input {
            padding: 8px 12px;
            border: 1px solid #ced4da;
            border-radius: 4px;
            min-width: 250px;
            transition: all 0.3s;
        }
        
        .search-box input:focus {
            border-color: var(--prm-verde);
            box-shadow: 0 0 0 2px rgba(0, 102, 51, 0.2);
        }
        
        /* Filtros */
        .filter-container {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
            flex-wrap: wrap;
        }
        
        .filter-btn {
            padding: 6px 12px;
            border-radius: 4px;
            background-color: white;
            border: 1px solid #dee2e6;
            cursor: pointer;
            transition: all 0.3s;
            display: flex;
            align-items: center;
            gap: 5px;
        }
        
        .filter-btn:hover, .filter-btn.active {
            background-color: var(--prm-verde);
            color: white;
            border-color: var(--prm-verde);
        }
        
        /* Tabela */
        .data-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            font-size: 0.95rem;
        }
        
        .data-table th {
            background-color: var(--prm-verde);
            color: white;
            padding: 12px 15px;
            text-align: left;
            font-weight: 600;
            position: sticky;
            top: 0;
        }
        
        .data-table td {
            padding: 12px 15px;
            border-bottom: 1px solid #dee2e6;
            vertical-align: middle;
        }
        
        .data-table tr:hover {
            background-color: #f8f9fa;
        }
        
    
    /* Estilo moderno para as badges de classificação */
    .classification-badge {
        display: inline-block;
        padding: 5px 12px;
        border-radius: 50px;
        font-size: 0.85rem;
        font-weight: 600;
        text-align: center;
        min-width: 110px;
        transition: all 0.3s;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        border: 1px solid transparent;
        text-transform: uppercase;
        font-size: 0.75rem;
        letter-spacing: 0.5px;
    }

    /* Cores modernas com gradiente e bordas */
    .classification-comum {
        background: linear-gradient(135deg, #6c757d 0%, #5a6268 100%);
        color: white;
        border-color: #545b62;
    }
    
    .classification-vitima {
        background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
        color: white;
        border-color: #bd2130;
        animation: pulse 1.5s infinite;
        text-shadow: 0 1px 1px rgba(0,0,0,0.2);
    }
    
    .classification-detido {
        background: linear-gradient(135deg, #343a40 0%, #23272b 100%);
        color: white;
        border-left: 3px solid #dc3545;
        font-weight: 700;
        text-shadow: 0 1px 1px rgba(0,0,0,0.3);
    }
    
    .classification-suspeito {
        background: linear-gradient(135deg, #fd7e14 0%, #e36209 100%);
        color: white;
        border-color: #d85609;
        text-shadow: 0 1px 1px rgba(0,0,0,0.2);
    }
    
    .classification-testemunha {
        background: linear-gradient(135deg, #17a2b8 0%, #138496 100%);
        color: white;
        border-color: #117a8b;
    }
    
    .classification-informante {
        background: linear-gradient(135deg, #28a745 0%, #218838 100%);
        color: white;
        border-color: #1e7e34;
    }
    
    .classification-denunciante {
        background: linear-gradient(135deg, #6f42c1 0%, #5a3d8e 100%);
        color: white;
        border-color: #563d7c;
    }

    /* Efeito hover moderno */
    .classification-badge:hover {
        transform: translateY(-1px);
        box-shadow: 0 4px 8px rgba(0,0,0,0.15);
        opacity: 0.95;
    }

    /* Animação pulsante para alertas */
    @keyframes pulse {
        0% { transform: scale(1); box-shadow: 0 0 0 0 rgba(220, 53, 69, 0.7); }
        70% { transform: scale(1.02); box-shadow: 0 0 0 6px rgba(220, 53, 69, 0); }
        100% { transform: scale(1); box-shadow: 0 0 0 0 rgba(220, 53, 69, 0); }
    }

    /* Tooltip moderno */
    .classification-badge[title]:hover:after {
        content: attr(title);
        position: absolute;
        background: rgba(0,0,0,0.8);
        color: #fff;
        padding: 5px 10px;
        border-radius: 4px;
        font-size: 0.75rem;
        white-space: nowrap;
        z-index: 100;
        margin-top: 5px;
    }

        
        /* Botões de ação */
        .action-buttons {
            display: flex;
            gap: 8px;
        }
        
        .action-btn {
            padding: 6px 10px;
            border-radius: 4px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 32px;
            height: 32px;
            transition: all 0.3s;
        }
        
        .btn-edit {
            background-color: var(--prm-amarelo);
            color: var(--prm-preto);
        }
        
        .btn-edit:hover {
            background-color: #e6b800;
        }
        
        .btn-delete {
            background-color: var(--prm-vermelho);
            color: white;
        }
        
        .btn-delete:hover {
            background-color: #b30000;
        }
        
        .btn-details {
            background-color: #17a2b8;
            color: white;
        }
        
        .btn-details:hover {
            background-color: #138496;
        }
        
        /* Mensagem vazia */
        .empty-message {
            text-align: center;
            padding: 30px;
            color: #6c757d;
            font-style: italic;
        }
        
        /* Responsividade */
        @media (max-width: 768px) {
            .action-bar {
                flex-direction: column;
            }
            
            .search-box {
                width: 100%;
            }
            
            .search-box input {
                width: 100%;
                min-width: auto;
            }
            
            .filter-container {
                overflow-x: auto;
                padding-bottom: 10px;
            }
            
            .data-table {
                display: block;
                overflow-x: auto;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="header-title">
            <i class="fas fa-users"></i> Lista de Cidadãos
        </h1>
        
        <!-- Barra de ações -->
        <div class="action-bar">
            <a href="${pageContext.request.contextPath}/cidadao?action=novo" class="btn-prm">
                <i class="fas fa-plus-circle"></i> Novo Cidadão
            </a>
            
            <div class="search-box">
                <form action="${pageContext.request.contextPath}/cidadao" method="GET">
                    <input type="hidden" name="action" value="buscar">
                    <input type="text" name="documento" placeholder="Buscar por documento" 
                           value="${not empty param.documento ? param.documento : ''}">
                    <button type="submit" class="btn-prm">
                        <i class="fas fa-search"></i>
                    </button>
                    <c:if test="${not empty param.documento}">
                        <a href="${pageContext.request.contextPath}/cidadao" class="btn-prm" style="background-color: #6c757d;">
                            <i class="fas fa-times"></i> Limpar
                        </a>
                    </c:if>
                </form>
            </div>
        </div>
        
        <!-- Filtros por classificação -->
        <div class="filter-container">
            <a href="${pageContext.request.contextPath}/cidadao" 
               class="filter-btn ${empty param.classificacao ? 'active' : ''}">
                <i class="fas fa-list"></i> Todos
            </a>
            <c:forEach items="${classificacoes}" var="classificacao">
                <a href="${pageContext.request.contextPath}/cidadao?action=buscarPorClassificacao&classificacao=${classificacao}" 
                   class="filter-btn ${param.classificacao == classificacao ? 'active' : ''}">
                    <i class="fas fa-tag"></i> ${classificacao}
                </a>
            </c:forEach>
        </div>
        
        <!-- Tabela de cidadãos -->
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Documento</th>
                    <th>Classificação</th>
                    <th>Telefone</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty cidadaos}">
                        <c:forEach var="cidadao" items="${cidadaos}">
                            <tr>
                                <td>${cidadao.idCidadao}</td>
                                <td>${cidadao.nome}</td>
                                <td>${cidadao.documentoIdentificacao}</td>
                               <td>
    <c:choose>
        <c:when test="${cidadao.classificacao == 'Cidadão Comum'}">
            <span class="classification-badge classification-comum" title="Cidadão sem registros relevantes">
                <i class="fas fa-user mr-1"></i> COMUM
            </span>
        </c:when>
        <c:when test="${cidadao.classificacao == 'Vítima'}">
            <span class="classification-badge classification-vitima" title="Vítima - Prioridade máxima">
                <i class="fas fa-first-aid mr-1"></i> VÍTIMA
            </span>
        </c:when>
        <c:when test="${cidadao.classificacao == 'Suspeito'}">
            <span class="classification-badge classification-suspeito" title="Suspeito - Em investigação">
                <i class="fas fa-search mr-1"></i> SUSPEITO
            </span>
        </c:when>
        <c:when test="${cidadao.classificacao == 'Testemunha'}">
            <span class="classification-badge classification-testemunha" title="Testemunha - Contato relevante">
                <i class="fas fa-eye mr-1"></i> TESTEMUNHA
            </span>
        </c:when>
        <c:when test="${cidadao.classificacao == 'Informante'}">
            <span class="classification-badge classification-informante" title="Informante - Fonte de informações">
                <i class="fas fa-info-circle mr-1"></i> INFORMANTE
            </span>
        </c:when>
        <c:when test="${cidadao.classificacao == 'Denunciante'}">
            <span class="classification-badge classification-denunciante" title="Denunciante - Fez denúncia formal">
                <i class="fas fa-bullhorn mr-1"></i> DENUNCIANTE
            </span>
        </c:when>
        <c:when test="${cidadao.classificacao == 'Detido'}">
            <span class="classification-badge classification-detido" title="Detido - Sob custódia">
                <i class="fas fa-lock mr-1"></i> DETIDO
            </span>
        </c:when>
        <c:otherwise>
            <span class="classification-badge classification-comum">
                <i class="fas fa-question mr-1"></i> ${cidadao.classificacao}
            </span>
        </c:otherwise>
    </c:choose>
</td>
                                <td>${cidadao.telefone}</td>
                                <td>
                                    <div class="action-buttons">
                                        <a href="${pageContext.request.contextPath}/cidadao?action=editar&id=${cidadao.idCidadao}" 
                                           class="action-btn btn-edit" title="Editar">
                                            <i class="fas fa-edit"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/cidadao?action=deletar&id=${cidadao.idCidadao}" 
                                           class="action-btn btn-delete" title="Excluir"
                                           onclick="return confirm('Tem certeza que deseja excluir este cidadão?')">
                                            <i class="fas fa-trash-alt"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/cidadao?action=buscar&documento=${cidadao.documentoIdentificacao}" 
                                           class="action-btn btn-details" title="Detalhes">
                                            <i class="fas fa-info-circle"></i>
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="6" class="empty-message">
                                <i class="fas fa-info-circle fa-2x"></i>
                                <p style="margin-top: 10px;">
                                    <c:choose>
                                        <c:when test="${not empty param.documento}">
                                            Nenhum cidadão encontrado com o documento "${param.documento}"
                                        </c:when>
                                        <c:when test="${not empty param.classificacao}">
                                            Nenhum cidadão encontrado com a classificação "${param.classificacao}"
                                        </c:when>
                                        <c:otherwise>
                                            Nenhum cidadão cadastrado no sistema
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
    
    <%@include file="/WEB-INF/views/templates/footer.jsp" %>
    
    <script>
        // Confirmação antes de excluir
        document.querySelectorAll('.btn-delete').forEach(btn => {
            btn.addEventListener('click', function(e) {
                if (!confirm('Tem certeza que deseja excluir este cidadão?')) {
                    e.preventDefault();
                }
            });
        });
        
        // Foco no campo de busca
        document.addEventListener('DOMContentLoaded', function() {
            const searchInput = document.querySelector('input[name="documento"]');
            if (searchInput) searchInput.focus();
        });
        
        // Tooltips dinâmicos
        tippy('.classification-badge', {
            content(reference) {
                return reference.getAttribute('title');
            },
        });
    </script>
</body>
</html>
