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
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            border-top: 5px solid var(--prm-vermelho);
            flex: 1;
        }
        
        .header-title {
            color: var(--prm-verde);
            border-bottom: 3px solid var(--prm-amarelo);
            padding-bottom: 10px;
            margin-bottom: 30px;
            text-transform: uppercase;
            font-weight: 700;
        }
        
        .action-buttons {
            margin: 20px 0;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 15px;
        }
        
        .btn-prm {
            background-color: var(--prm-verde);
            color: white;
            font-weight: 600;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }
        
        .search-container {
            display: flex;
            gap: 10px;
            align-items: center;
        }
        
        .search-form {
            display: flex;
            gap: 10px;
            align-items: center;
        }
        
        .search-form input {
            padding: 8px 12px;
            border: 1px solid #ced4da;
            border-radius: 4px;
            min-width: 250px;
        }
        
        .search-form button {
            padding: 8px 15px;
            background-color: var(--prm-verde);
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: 5px;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        
        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #dee2e6;
        }
        
        th {
            background-color: var(--prm-verde);
            color: white;
            font-weight: 600;
        }
        
        .actions-cell {
            display: flex;
            gap: 8px;
        }
        
        .btn {
            padding: 6px 12px;
            border-radius: 4px;
            font-size: 0.85rem;
            display: inline-flex;
            align-items: center;
            gap: 5px;
            text-decoration: none;
        }
        
        .btn-sec {
            background-color: var(--prm-amarelo);
            color: var(--prm-preto);
        }
        
        .btn-danger {
            background-color: var(--prm-vermelho);
            color: white;
        }
        
        .btn-info {
            background-color: #5bc0de;
            color: white;
        }
        
        .empty-table-message {
            text-align: center;
            padding: 20px;
            color: #6c757d;
            font-style: italic;
        }
        
        @media (max-width: 768px) {
            .action-buttons {
                flex-direction: column;
                align-items: flex-start;
            }
            
            .search-form {
                width: 100%;
            }
            
            .search-form input {
                min-width: 0;
                width: 100%;
            }
            
            table {
                display: block;
                overflow-x: auto;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="header-title">
            <i class="fas fa-users me-2"></i>Lista de Cidadãos
        </h1>
        
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/cidadao?action=novo" class="btn-prm">
                <i class="fas fa-plus-circle"></i> Novo Cidadão
            </a>
            
            <div class="search-container">
                <form class="search-form" action="${pageContext.request.contextPath}/cidadao" method="GET">
                    <input type="hidden" name="action" value="buscar">
                    <input type="text" name="documento" placeholder="Buscar por número de documento" 
                           value="${not empty param.documento ? param.documento : ''}">
                    <button type="submit">
                        <i class="fas fa-search"></i> Buscar
                    </button>
                    <c:if test="${not empty param.documento}">
                        <a href="${pageContext.request.contextPath}/cidadao" class="btn btn-sec">
                            <i class="fas fa-times"></i> Limpar
                        </a>
                    </c:if>
                </form>
            </div>
        </div>
        
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Documento</th>
                    <th>Telefone</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="cidadao" items="${cidadaos}">
                    <tr>
                        <td>${cidadao.idCidadao}</td>
                        <td>${cidadao.nome}</td>
                        <td>${cidadao.documentoIdentificacao}</td>
                        <td>${cidadao.telefone}</td>
                        <td class="actions-cell">
                            <a href="${pageContext.request.contextPath}/cidadao?action=editar&id=${cidadao.idCidadao}" 
                               class="btn btn-sec" title="Editar">
                                <i class="fas fa-edit"></i>
                            </a>
                            <a href="${pageContext.request.contextPath}/cidadao?action=deletar&id=${cidadao.idCidadao}" 
                               class="btn btn-danger" title="Excluir"
                               onclick="return confirm('Tem certeza que deseja excluir este cidadão?')">
                                <i class="fas fa-trash-alt"></i>
                            </a>
                            <a href="${pageContext.request.contextPath}/cidadao?action=buscar&documento=${cidadao.documentoIdentificacao}" 
                               class="btn btn-info" title="Detalhes">
                                <i class="fas fa-info-circle"></i>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                
                <c:if test="${empty cidadaos}">
                    <tr>
                        <td colspan="5" class="empty-table-message">
                            <i class="fas fa-info-circle fa-lg"></i><br>
                            <c:choose>
                                <c:when test="${not empty param.documento}">
                                    Nenhum cidadão encontrado com o documento informado
                                </c:when>
                                <c:otherwise>
                                    Nenhum cidadão cadastrado
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
    
    <%@include file="/WEB-INF/views/templates/footer.jsp" %>
    
    <script>
        // Confirmação antes de excluir
        document.querySelectorAll('.btn-danger').forEach(btn => {
            btn.addEventListener('click', function(e) {
                if (!confirm('Tem certeza que deseja excluir este cidadão?')) {
                    e.preventDefault();
                }
            });
        });
        
        // Foco no campo de busca ao carregar a página
        document.addEventListener('DOMContentLoaded', function() {
            const searchInput = document.querySelector('input[name="documento"]');
            if (searchInput) {
                searchInput.focus();
            }
        });
    </script>
</body>
</html>
