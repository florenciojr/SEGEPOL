<%-- 
    Document   : formCidadao
    Created on : 4 de mai de 2025, 00:48:27
    Author     : JR5
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%
    // Definindo listas de opções que serão usadas nos selects
    List<String> generos = Arrays.asList("Masculino", "Feminino", "Outro");
    List<String> tiposDocumento = Arrays.asList("BI", "Passaporte", "Carta de Condução", "Outro");
    List<String> classificacoes = Arrays.asList("Comum", "Vitima", "Suspeito", "Testemunha", "Informante", "Denunciante", "Detido");
    
    request.setAttribute("generos", generos);
    request.setAttribute("tiposDocumento", tiposDocumento);
    request.setAttribute("classificacoes", classificacoes);
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${cidadao == null ? 'Novo Cidadão' : 'Editar Cidadão'} | PRM</title>
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
        }
        
        .form-container {
            max-width: 1000px;
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
            text-transform: uppercase;
            font-weight: 700;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
        }
        
        .section-title {
            color: var(--prm-verde);
            border-bottom: 2px solid var(--prm-amarelo);
            padding-bottom: 8px;
            margin: 30px 0 20px;
            font-size: 1.2rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        
        .form-label {
            font-weight: 600;
            color: #495057;
            margin-bottom: 5px;
        }
        
        .required-label:after {
            content: " *";
            color: var(--prm-vermelho);
        }
        
        .btn-prm {
            background-color: var(--prm-verde);
            color: white;
            font-weight: 600;
            letter-spacing: 0.5px;
            padding: 10px 25px;
            border: none;
            border-radius: 4px;
            transition: all 0.3s;
        }
        
        .btn-prm:hover {
            background-color: #005a2b;
            color: white;
            transform: translateY(-2px);
        }
        
        .photo-container {
            border: 2px dashed #ced4da;
            border-radius: 8px;
            height: 200px;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            overflow: hidden;
            background-color: #f8f9fa;
            transition: all 0.3s;
        }
        
        .photo-container:hover {
            border-color: var(--prm-verde);
            box-shadow: 0 0 10px rgba(0, 102, 51, 0.2);
        }
        
        .photo-preview {
            max-width: 100%;
            max-height: 100%;
            display: none;
            object-fit: cover;
        }
        
        .form-control:focus, .form-select:focus {
            border-color: var(--prm-verde);
            box-shadow: 0 0 0 0.25rem rgba(0, 102, 51, 0.25);
        }
        
        .is-invalid {
            border-color: #dc3545 !important;
        }

        .invalid-feedback {
            color: #dc3545;
            font-size: 0.875em;
        }

        /* Estilos para as classificações */
        .classification-comum {
            background-color: #6c757d;
            color: white;
        }

        .classification-vitima {
            background-color: #dc3545;
            color: white;
        }

        .classification-suspeito {
            background-color: #fd7e14;
            color: black;
        }

        .classification-testemunha {
            background-color: #17a2b8;
            color: white;
        }

        .classification-informante {
            background-color: #28a745;
            color: white;
        }

        .classification-denunciante {
            background-color: #6610f2;
            color: white;
        }

        .classification-detido {
            background-color: #212529;
            color: white;
        }

        /* Badge de status */
        .status-badge {
            font-size: 0.8rem;
            font-weight: 700;
            letter-spacing: 0.5px;
        }

        /* Responsividade */
        @media (max-width: 768px) {
            .form-container {
                padding: 20px;
            }
            
            .header-title {
                font-size: 1.5rem;
                flex-direction: column;
                text-align: center;
            }
            
            .photo-container {
                height: 150px;
                margin-bottom: 15px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h1 class="header-title">
                <i class="fas fa-user-edit"></i>
                ${cidadao == null ? 'Cadastrar Novo Cidadão' : 'Editar Cadastro'}
                <span class="status-badge ms-2 badge rounded-pill ${cidadao == null ? 'bg-danger' : 'bg-warning text-dark'}">
                    ${cidadao == null ? 'NOVO' : 'EDIÇÃO'}
                </span>
            </h1>
            
            <form action="${pageContext.request.contextPath}/cidadao" method="POST" enctype="multipart/form-data" onsubmit="return validarFormulario()">
                <input type="hidden" name="action" value="${cidadao == null ? 'salvar' : 'atualizar'}">
                
                <c:if test="${cidadao != null}">
                    <input type="hidden" name="id" value="${cidadao.idCidadao}">
                </c:if>
                
                <!-- Seção: Dados Pessoais -->
                <h3 class="section-title">
                    <i class="fas fa-user"></i>Dados Pessoais
                </h3>
                
                <div class="row mb-4">
                    <!-- Foto -->
                    <div class="col-md-3">
                        <div class="mb-3">
                            <label class="form-label">Foto</label>
                            <div class="photo-container" id="photoContainer">
                                <i class="fas fa-camera fa-3x text-secondary" id="photoIcon"></i>
                                <c:if test="${cidadao != null && cidadao.caminhoImagem != null && !cidadao.caminhoImagem.isEmpty()}">
                                    <img src="${pageContext.request.contextPath}/cidadao?action=visualizarImagem&id=${cidadao.idCidadao}" 
                                         class="photo-preview" id="photoPreview">
                                </c:if>
                            </div>
                            <input type="file" id="imagem" name="imagem" accept="image/*" class="d-none">
                            <small class="text-muted">Formatos: JPG, PNG (Max. 2MB)</small>
                        </div>
                    </div>
                    
                    <!-- Dados Básicos -->
                    <div class="col-md-9">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="nome" class="form-label required-label">Nome Completo</label>
                                <input type="text" class="form-control" id="nome" name="nome" 
                                       value="${cidadao.nome}" required>
                                <div class="invalid-feedback">Este campo é obrigatório</div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="genero" class="form-label required-label">Gênero</label>
                                <select class="form-select" id="genero" name="genero" required>
                                    <option value="">Selecione...</option>
                                    <c:forEach items="${generos}" var="genero">
                                        <option value="${genero}" ${cidadao.genero == genero ? 'selected' : ''}>
                                            ${genero}
                                        </option>
                                    </c:forEach>
                                </select>
                                <div class="invalid-feedback">Este campo é obrigatório</div>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="dataNascimento" class="form-label required-label">Data de Nascimento</label>
                                <input type="date" class="form-control" id="dataNascimento" name="dataNascimento" 
                                       value="${cidadao.dataNascimento}" required>
                                <div class="invalid-feedback">Este campo é obrigatório</div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="tipoDocumento" class="form-label required-label">Tipo de Documento</label>
                                <select class="form-select" id="tipoDocumento" name="tipoDocumento" required>
                                    <option value="">Selecione...</option>
                                    <c:forEach items="${tiposDocumento}" var="tipo">
                                        <option value="${tipo}" ${cidadao.tipoDocumento == tipo ? 'selected' : ''}>
                                            ${tipo}
                                        </option>
                                    </c:forEach>
                                </select>
                                <div class="invalid-feedback">Este campo é obrigatório</div>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="documentoIdentificacao" class="form-label required-label">Nº Documento</label>
                                <input type="text" class="form-control" id="documentoIdentificacao" 
                                       name="documentoIdentificacao" value="${cidadao.documentoIdentificacao}" 
                                       placeholder="Número do documento" required>
                                <div class="invalid-feedback">Este campo é obrigatório</div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="naturalidade" class="form-label">Naturalidade</label>
                                <input type="text" class="form-control" id="naturalidade" name="naturalidade" 
                                       value="${cidadao.naturalidade}" placeholder="Cidade/Província de nascimento">
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Seção: Classificação -->
                <h3 class="section-title">
                    <i class="fas fa-tag"></i>Classificação
                </h3>
                
                <div class="row mb-4">
                    <div class="col-md-6 mb-3">
                        <label for="classificacao" class="form-label required-label">Classificação</label>
                        <select class="form-select" id="classificacao" name="classificacao" required>
                            <option value="">Selecione uma classificação...</option>
                            <c:forEach items="${classificacoes}" var="classificacao">
                                <option value="${classificacao}" 
                                    ${cidadao.classificacao == classificacao ? 'selected' : ''}
                                    class="${'classification-' += classificacao.toLowerCase()}">
                                    ${classificacao}
                                </option>
                            </c:forEach>
                        </select>
                        <div class="invalid-feedback">Este campo é obrigatório</div>
                    </div>
                </div>
<!-- Seção: Características Físicas -->
<h3 class="section-title">
    <i class="fas fa-id-card"></i> Características Físicas
</h3>

<div class="row mb-4">
    <div class="col-12">
        <label for="caracteristicasFisicas" class="form-label">Características Físicas</label>
<textarea class="form-control" id="caracteristicasFisicas" name="caracteristicasFisicas" 
          rows="4">${cidadao.caracteristicasFisicas}</textarea>
        <small class="text-muted">
            Ex: Estatura, cor dos olhos, cicatrizes, tatuagens, marcas distintivas, etc.
        </small>
    </div>
</div>
                
                <!-- Seção: Contato -->
                <h3 class="section-title">
                    <i class="fas fa-phone-alt"></i>Contato
                </h3>
                
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="telefone" class="form-label required-label">Telefone</label>
                        <input type="tel" class="form-control" id="telefone" name="telefone" 
                               value="${cidadao.telefone}" placeholder="(XX) XXXXX-XXXX" required>
                        <div class="invalid-feedback">Este campo é obrigatório</div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="email" class="form-label">E-mail</label>
                        <input type="email" class="form-control" id="email" name="email" 
                               value="${cidadao.email}" placeholder="exemplo@dominio.com">
                    </div>
                </div>
                
                <!-- Seção: Endereço -->
                <h3 class="section-title">
                    <i class="fas fa-map-marker-alt"></i>Endereço Residencial
                </h3>
                
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="provincia" class="form-label">Província</label>
                        <input type="text" class="form-control" id="provincia" name="provincia" 
                               value="${cidadao.provincia}">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="cidade" class="form-label">Cidade</label>
                        <input type="text" class="form-control" id="cidade" name="cidade" 
                               value="${cidadao.cidade}">
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="bairro" class="form-label">Bairro</label>
                        <input type="text" class="form-control" id="bairro" name="bairro" 
                               value="${cidadao.bairro}">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="rua" class="form-label">Rua e Número</label>
                        <input type="text" class="form-control" id="rua" name="rua" 
                               value="${cidadao.rua}" placeholder="Ex: Av. 25 de Setembro, nº 1234">
                    </div>
                </div>
                
                <!-- Botões de Ação -->
                <div class="d-flex justify-content-between mt-5 pt-4 border-top">
                    <a href="${pageContext.request.contextPath}/cidadao" class="btn btn-secondary">
                        <i class="fas fa-arrow-left me-2"></i>Voltar
                    </a>
                    <button type="submit" class="btn btn-prm">
                        <i class="fas fa-save me-2"></i>${cidadao == null ? 'Cadastrar' : 'Atualizar'}
                    </button>
                </div>
            </form>
        </div>
    </div>

    <%@include file="/WEB-INF/views/templates/footer.jsp" %>
    
    <script>
        // Máscara para telefone
        document.getElementById('telefone').addEventListener('input', function(e) {
            var x = e.target.value.replace(/\D/g, '').match(/(\d{0,2})(\d{0,5})(\d{0,4})/);
            e.target.value = !x[2] ? x[1] : '(' + x[1] + ') ' + x[2] + (x[3] ? '-' + x[3] : '');
        });
        
        // Upload de foto
        const photoContainer = document.getElementById('photoContainer');
        const photoInput = document.getElementById('imagem');
        const photoPreview = document.getElementById('photoPreview');
        const photoIcon = document.getElementById('photoIcon');
        
        photoContainer.addEventListener('click', () => photoInput.click());
        
        photoInput.addEventListener('change', (e) => {
            const file = e.target.files[0];
            if (file) {
                // Verificar tamanho do arquivo (max 2MB)
                if (file.size > 2 * 1024 * 1024) {
                    alert('O tamanho máximo da imagem é 2MB');
                    return;
                }
                
                const reader = new FileReader();
                reader.onload = (event) => {
                    if (!photoPreview) {
                        // Se não existir preview, cria um
                        const newPreview = document.createElement('img');
                        newPreview.id = 'photoPreview';
                        newPreview.className = 'photo-preview';
                        newPreview.src = event.target.result;
                        newPreview.style.display = 'block';
                        photoContainer.appendChild(newPreview);
                    } else {
                        photoPreview.src = event.target.result;
                        photoPreview.style.display = 'block';
                    }
                    photoIcon.style.display = 'none';
                };
                reader.readAsDataURL(file);
            }
        });
        
        // Verificar se documento já existe
        document.getElementById('documentoIdentificacao').addEventListener('blur', function() {
            const documento = this.value;
            if (documento.length > 0) {
                fetch('${pageContext.request.contextPath}/cidadao?action=verificarDocumento&documento=' + documento)
                    .then(response => response.json())
                    .then(data => {
                        if (data.existe && ${cidadao == null ? 'true' : 'data.id != ' + cidadao.idCidadao}) {
                            alert('Este documento já está cadastrado no sistema!');
                            document.getElementById('documentoIdentificacao').focus();
                        }
                    });
            }
        });

        // Validação do formulário
        function validarFormulario() {
            let isValid = true;
            const requiredFields = document.querySelectorAll('[required]');
            
            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    isValid = false;
                    field.classList.add('is-invalid');
                } else {
                    field.classList.remove('is-invalid');
                }
            });
            
            // Validação específica para classificação
            const classificacao = document.getElementById('classificacao');
            if (classificacao.value === "") {
                classificacao.classList.add('is-invalid');
                isValid = false;
            } else {
                classificacao.classList.remove('is-invalid');
            }
            
            if (!isValid) {
                alert('Por favor, preencha todos os campos obrigatórios!');
                return false;
            }
            
            return true;
        }
        
        // Foco no primeiro campo ao carregar a página
        document.addEventListener('DOMContentLoaded', function() {
            document.getElementById('nome').focus();
            
            // Se já existir uma foto, mostra o preview
            if (photoPreview && photoPreview.src) {
                photoPreview.style.display = 'block';
                photoIcon.style.display = 'none';
            }
        });
    </script>
</body>
</html>
