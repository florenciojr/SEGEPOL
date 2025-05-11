<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${not empty suspeito ? 'Editar' : 'Novo'} Suspeito | Sistema Policial</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome para ícones -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #34495e;
            --accent-color: #e74c3c;
            --light-color: #ecf0f1;
            --dark-color: #2c3e50;
        }
        
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .form-container {
            max-width: 800px;
            margin: 30px auto;
            padding: 30px;
            background-color: white;
            border-radius: 12px;
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.08);
            border-top: 4px solid var(--accent-color);
        }
        
        .form-title {
            color: var(--dark-color);
            border-bottom: 2px solid var(--light-color);
            padding-bottom: 12px;
            margin-bottom: 25px;
            font-weight: 700;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .form-label {
            font-weight: 600;
            color: var(--secondary-color);
            margin-bottom: 8px;
        }
        
        .required-field:after {
            content: " *";
            color: var(--accent-color);
        }
        
        textarea.form-control {
            min-height: 120px;
            resize: vertical;
            padding: 12px;
            border-radius: 8px;
        }
        
        .form-select {
            padding: 10px 12px;
            border-radius: 8px;
        }
        
        .role-badge {
            display: inline-block;
            padding: 4px 10px;
            border-radius: 50px;
            font-size: 0.85rem;
            font-weight: 600;
            margin-right: 8px;
        }
        
        .role-principal {
            background-color: #e74c3c;
            color: white;
        }
        
        .role-cumplice {
            background-color: #e67e22;
            color: white;
        }
        
        .role-acessorio {
            background-color: #7f8c8d;
            color: white;
        }
        
        .role-testemunha {
            background-color: #27ae60;
            color: white;
        }
        
        .btn-cancel {
            background-color: #95a5a6;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 8px;
            font-weight: 600;
        }
        
        .btn-submit {
            background-color: var(--accent-color);
            color: white;
            border: none;
            padding: 10px 25px;
            border-radius: 8px;
            font-weight: 600;
            transition: all 0.3s;
        }
        
        .btn-submit:hover {
            background-color: #c0392b;
            transform: translateY(-2px);
        }
        
        .form-note {
            font-size: 0.85rem;
            color: #7f8c8d;
            margin-top: 5px;
            display: flex;
            align-items: center;
            gap: 5px;
        }
        
        .form-section {
            margin-bottom: 25px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eee;
        }
        
        .form-section:last-child {
            border-bottom: none;
        }
        
        .suspect-icon {
            color: var(--accent-color);
            font-size: 1.2rem;
            margin-right: 8px;
        }
        
        .form-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        
        .back-link {
            color: var(--secondary-color);
            text-decoration: none;
            font-weight: 600;
            display: flex;
            align-items: center;
            gap: 5px;
        }
    </style>
</head>
<body>
    <!-- Inclui o header -->
    <%@include file="/WEB-INF/views/templates/header.jsp" %>

    <div class="container">
        <div class="form-container">
            <div class="form-header">
                <h2 class="form-title">
                    <i class="fas fa-user-secret suspect-icon"></i>
                    ${not empty suspeito ? 'Editar Suspeito' : 'Cadastrar Novo Suspeito'}
                </h2>
                <a href="suspeitos?action=list${not empty idQueixaSelecionado ? 'ByQueixa&idQueixa=' += idQueixaSelecionado : ''}" 
                   class="back-link">
                    <i class="fas fa-arrow-left"></i> Voltar para lista
                </a>
            </div>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show mb-4" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            
            <form action="suspeitos?action=${not empty suspeito ? 'update' : 'insert'}" method="post">
                <input type="hidden" name="id" value="${not empty suspeito ? suspeito.idSuspeito : ''}">
                
                <div class="form-section">
                    <!-- Campo Queixa -->
                    <div class="mb-4">
                        <label for="idQueixa" class="form-label required-field">Queixa Relacionada</label>
                        <select class="form-select" id="idQueixa" name="idQueixa" required>
                            <option value="">Selecione uma queixa...</option>
                            <c:forEach items="${queixas}" var="queixa">
                                <option value="${queixa.id}"
                                    ${(not empty idQueixaSelecionado && idQueixaSelecionado == queixa.id) || 
                                      (not empty suspeito && suspeito.idQueixa == Integer.parseInt(queixa.id)) ? 'selected' : ''}>
                                    Queixa #${queixa.id} - ${queixa.descricao}
                                </option>
                            </c:forEach>
                        </select>
                        <small class="form-note">
                            <i class="fas fa-info-circle"></i> Selecione a queixa a qual este suspeito está vinculado
                        </small>
                    </div>
                    
                   
<!-- Campo Cidadão -->
<div class="mb-4">
    <label for="idCidadao" class="form-label">Identificação do Cidadão</label>
    <select class="form-select" id="idCidadao" name="idCidadao">
        <option value="" ${empty suspeito || suspeito.idCidadao == null ? 'selected' : ''}>
            <i class="fas fa-user-slash me-1"></i> Não identificado
        </option>
        <c:forEach items="${cidadaos}" var="cidadao">
            <option value="${cidadao.id}"
                ${(not empty suspeito && suspeito.idCidadao != null && suspeito.idCidadao == cidadao.id) ? 'selected' : ''}>
                <i class="fas fa-user me-1"></i> ${cidadao.nome} (CPF: ${cidadao.cpf})
            </option>
        </c:forEach>
    </select>
    <small class="form-note">
        <i class="fas fa-info-circle"></i> Deixe como "Não identificado" se o suspeito não estiver cadastrado no sistema
    </small>
</div>
                    <!-- Campo Descrição -->
                    <div class="mb-4">
                        <label for="descricao" class="form-label">Descrição do Suspeito</label>
                        <textarea class="form-control" id="descricao" name="descricao"
                            placeholder="Informe características físicas, vestimentas, comportamentos observados e outras informações relevantes...">${not empty suspeito ? suspeito.descricao : ''}</textarea>
                        <small class="form-note">
                            <i class="fas fa-info-circle"></i> Detalhes que ajudem na identificação do suspeito
                        </small>
                    </div>
                </div>
                
                <div class="form-section">
                    <!-- Campo Papel no Incidente -->
                    <div class="mb-4">
                        <label for="papelIncidente" class="form-label required-field">Papel no Incidente</label>
                        <select class="form-select" id="papelIncidente" name="papelIncidente" required>
                            <option value="">Selecione o papel do suspeito...</option>
                            <option value="Principal" ${not empty suspeito && suspeito.papelIncidente == 'Principal' ? 'selected' : ''}>
                                <span class="role-badge role-principal">Principal</span> - Autor principal do incidente
                            </option>
                            <option value="Cúmplice" ${not empty suspeito && suspeito.papelIncidente == 'Cúmplice' ? 'selected' : ''}>
                                <span class="role-badge role-cumplice">Cúmplice</span> - Participou ativamente auxiliando
                            </option>
                            <option value="Acessório" ${not empty suspeito && suspeito.papelIncidente == 'Acessório' ? 'selected' : ''}>
                                <span class="role-badge role-acessorio">Acessório</span> - Envolvido indiretamente
                            </option>
                            <option value="Testemunha" ${not empty suspeito && suspeito.papelIncidente == 'Testemunha' ? 'selected' : ''}>
                                <span class="role-badge role-testemunha">Testemunha</span> - Presenciou o incidente
                            </option>
                        </select>
                        <small class="form-note">
                            <i class="fas fa-info-circle"></i> Classifique o nível de envolvimento do suspeito
                        </small>
                    </div>
                </div>
                
                <div class="d-flex justify-content-between mt-5">
                    <a href="suspeitos?action=list${not empty idQueixaSelecionado ? 'ByQueixa&idQueixa=' += idQueixaSelecionado : ''}" 
                       class="btn btn-cancel">
                        <i class="fas fa-times me-2"></i>Cancelar
                    </a>
                    <button type="submit" class="btn btn-submit">
                        <i class="fas fa-save me-2"></i>${not empty suspeito ? 'Atualizar' : 'Cadastrar'}
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- Inclui o footer -->
    <%@include file="/WEB-INF/views/templates/footer.jsp" %>

    <!-- Bootstrap JS e dependências -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Validação do formulário antes de enviar
        document.querySelector('form').addEventListener('submit', function(e) {
            const queixa = document.getElementById('idQueixa');
            const papel = document.getElementById('papelIncidente');
            let isValid = true;
            
            // Reset de erros
            queixa.classList.remove('is-invalid');
            papel.classList.remove('is-invalid');
            
            // Validação da queixa
            if (!queixa.value) {
                queixa.classList.add('is-invalid');
                isValid = false;
            }
            
            // Validação do papel
            if (!papel.value) {
                papel.classList.add('is-invalid');
                isValid = false;
            }
            
            if (!isValid) {
                e.preventDefault();
                alert('Por favor, preencha todos os campos obrigatórios marcados com *');
            }
        });
    </script>
</body>
</html>
