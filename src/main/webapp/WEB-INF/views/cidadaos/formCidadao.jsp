<%-- 
    Document   : formCidadao
    Created on : 4 de mai de 2025, 00:48:27
    Author     : JR5
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${cidadao == null ? 'Novo Cidadão' : 'Editar Cidadão'}</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .form-container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            border-bottom: 1px solid #eee;
            padding-bottom: 10px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input[type="text"],
        input[type="date"],
        input[type="tel"],
        input[type="email"],
        select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .row {
            display: flex;
            gap: 15px;
            margin-bottom: 15px;
        }
        .col {
            flex: 1;
        }
        .form-actions {
            margin-top: 20px;
            text-align: right;
        }
        .btn {
            padding: 8px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            font-size: 14px;
        }
        .btn-primary {
            background-color: #4CAF50;
            color: white;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        .required:after {
            content: " *";
            color: red;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <h1>${cidadao == null ? 'Cadastrar Novo Cidadão' : 'Editar Cidadão'}</h1>
        
        <form action="${pageContext.request.contextPath}/cidadao" method="POST">
            <input type="hidden" name="action" value="${cidadao == null ? 'salvar' : 'atualizar'}">
            
            <c:if test="${cidadao != null}">
                <input type="hidden" name="id" value="${cidadao.idCidadao}">
            </c:if>
            
            <div class="row">
                <div class="col">
                    <div class="form-group">
                        <label for="nome" class="required">Nome Completo</label>
                        <input type="text" id="nome" name="nome" value="${cidadao.nome}" required>
                    </div>
                </div>
                <div class="col">
                    <div class="form-group">
                        <label for="genero" class="required">Gênero</label>
                        <select id="genero" name="genero" required>
                            <option value="">Selecione...</option>
                            <option value="Masculino" ${cidadao.genero == 'Masculino' ? 'selected' : ''}>Masculino</option>
                            <option value="Feminino" ${cidadao.genero == 'Feminino' ? 'selected' : ''}>Feminino</option>
                            <option value="Outro" ${cidadao.genero == 'Outro' ? 'selected' : ''}>Outro</option>
                        </select>
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col">
                    <div class="form-group">
                        <label for="dataNascimento" class="required">Data de Nascimento</label>
                        <input type="date" id="dataNascimento" name="dataNascimento" value="${cidadao.dataNascimento}" required>
                    </div>
                </div>
                <div class="col">
                    <div class="form-group">
                        <label for="documentoIdentificacao" class="required">Documento de Identificação</label>
                        <input type="text" id="documentoIdentificacao" name="documentoIdentificacao" value="${cidadao.documentoIdentificacao}" required>
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col">
                    <div class="form-group">
                        <label for="telefone" class="required">Telefone</label>
                        <input type="tel" id="telefone" name="telefone" value="${cidadao.telefone}" required>
                    </div>
                </div>
                <div class="col">
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" value="${cidadao.email}">
                    </div>
                </div>
            </div>
            
            <div class="form-group">
                <label for="naturalidade">Naturalidade</label>
                <input type="text" id="naturalidade" name="naturalidade" value="${cidadao.naturalidade}">
            </div>
            
            <h3>Endereço</h3>
            <div class="row">
                <div class="col">
                    <div class="form-group">
                        <label for="provincia">Província</label>
                        <input type="text" id="provincia" name="provincia" value="${cidadao.provincia}">
                    </div>
                </div>
                <div class="col">
                    <div class="form-group">
                        <label for="cidade">Cidade</label>
                        <input type="text" id="cidade" name="cidade" value="${cidadao.cidade}">
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col">
                    <div class="form-group">
                        <label for="bairro">Bairro</label>
                        <input type="text" id="bairro" name="bairro" value="${cidadao.bairro}">
                    </div>
                </div>
                <div class="col">
                    <div class="form-group">
                        <label for="rua">Rua</label>
                        <input type="text" id="rua" name="rua" value="${cidadao.rua}">
                    </div>
                </div>
            </div>
            
            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/cidadao" class="btn btn-secondary">Cancelar</a>
                <button type="submit" class="btn btn-primary">${cidadao == null ? 'Cadastrar' : 'Atualizar'}</button>
            </div>
        </form>
    </div>
    
    <script>
        // Máscara para telefone
        document.getElementById('telefone').addEventListener('input', function(e) {
            var x = e.target.value.replace(/\D/g, '').match(/(\d{0,2})(\d{0,5})(\d{0,4})/);
            e.target.value = !x[2] ? x[1] : '(' + x[1] + ') ' + x[2] + (x[3] ? '-' + x[3] : '');
        });
    </script>
</body>
</html>