<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Detalhes da Queixa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Detalhes da Queixa #${queixa.idQueixa}</h1>
        
        <div class="card">
            <div class="card-header">
                <h3>${queixa.titulo}</h3>
            </div>
            <div class="card-body">
                <dl class="row">
                    <dt class="col-sm-3">Descrição:</dt>
                    <dd class="col-sm-9">${queixa.descricao}</dd>
                    
                    <dt class="col-sm-3">Data do Incidente:</dt>
                    <dd class="col-sm-9">${queixa.dataIncidente}</dd>
                    
                    <dt class="col-sm-3">Data de Registro:</dt>
                    <dd class="col-sm-9">${queixa.dataRegistro}</dd>
                    
                    <dt class="col-sm-3">Local:</dt>
                    <dd class="col-sm-9">${queixa.localIncidente}</dd>
                    
                    <dt class="col-sm-3">Coordenadas:</dt>
                    <dd class="col-sm-9">${queixa.coordenadas}</dd>
                    
                    <dt class="col-sm-3">Status:</dt>
                    <dd class="col-sm-9">${queixa.status}</dd>
                </dl>
                
                <div class="mt-4">
                    <a href="queixas?action=edit&id=${queixa.idQueixa}" class="btn btn-warning">Editar</a>
                    <a href="queixas" class="btn btn-secondary">Voltar</a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
