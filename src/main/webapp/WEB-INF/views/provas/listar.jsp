<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Provas | Sistema de Provas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        .table-container {
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            padding: 2rem;
        }
        .page-header {
            border-bottom: 2px solid #4e73df;
            padding-bottom: 1rem;
            margin-bottom: 2rem;
        }
        .status-badge {
            font-size: 0.8rem;
            padding: 0.35rem 0.65rem;
        }
        .action-btn {
            width: 30px;
            height: 30px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            margin: 0 3px;
        }
        .table th {
            background-color: #4e73df;
            color: white;
        }
    </style>
</head>
<body class="bg-light">
    <div class="container py-5">
        <div class="table-container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1 class="h3 text-gray-800">
                    <i class="bi bi-file-earmark-medical"></i> Lista de Provas
                </h1>
                <a href="provas?action=nova" class="btn btn-primary">
                    <i class="bi bi-plus-circle"></i> Nova Prova
                </a>
            </div>
            
            <c:if test="${not empty mensagem}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    ${mensagem}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="mensagem" scope="session"/>
            </c:if>
            
            <c:if test="${not empty erro}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${erro}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="erro" scope="session"/>
            </c:if>
            
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Queixa</th>
                            <th>Tipo</th>
                            <th>Data</th>
                            <th>Usuário</th>
                            <th class="text-center">Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${provas}" var="prova">
                            <tr>
                                <td class="fw-bold">#${prova.idProva}</td>
                                <td>${prova.tituloQueixa}</td>
                                <td>
                                    <span class="badge bg-primary status-badge">${prova.tipo}</span>
                                </td>
                                <td>${prova.dataUpload}</td>
                                <td>${prova.nomeUsuario}</td>
                                <td class="text-center">
                                    <a href="provas?action=detalhes&id=${prova.idProva}" 
                                       class="btn btn-sm btn-info action-btn" title="Detalhes">
                                        <i class="bi bi-eye"></i>
                                    </a>
                                    <a href="provas?action=editar&id=${prova.idProva}" 
                                       class="btn btn-sm btn-warning action-btn" title="Editar">
                                        <i class="bi bi-pencil"></i>
                                    </a>
                                    <a href="provas?action=remover&id=${prova.idProva}" 
                                       class="btn btn-sm btn-danger action-btn" title="Remover"
                                       onclick="return confirm('Tem certeza que deseja remover esta prova?')">
                                        <i class="bi bi-trash"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            
            <nav aria-label="Page navigation" class="mt-4">
                <ul class="pagination justify-content-center">
                    <li class="page-item disabled">
                        <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Anterior</a>
                    </li>
                    <li class="page-item active"><a class="page-link" href="#">1</a></li>
                    <li class="page-item"><a class="page-link" href="#">2</a></li>
                    <li class="page-item"><a class="page-link" href="#">3</a></li>
                    <li class="page-item">
                        <a class="page-link" href="#">Próximo</a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
