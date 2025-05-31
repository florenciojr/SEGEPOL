<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${prova.idProva == 0 ? 'Nova Prova' : 'Editar Prova'} | Sistema de Provas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css">
    <style>
        :root {
            --primary-color: #4e73df;
            --secondary-color: #f8f9fc;
            --accent-color: #2e59d9;
            --dark-color: #5a5c69;
        }
        
        body {
            background-color: #f8f9fa;
            font-family: 'Nunito', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
        }
        
        .form-container {
            background-color: white;
            border-radius: 15px;
            box-shadow: 0 0.15rem 1.75rem 0 rgba(58, 59, 69, 0.15);
            padding: 2.5rem;
            margin-top: 2rem;
            margin-bottom: 2rem;
            animation: fadeIn 0.5s ease-in-out;
        }
        
        .form-title {
            color: var(--primary-color);
            border-bottom: 3px solid var(--primary-color);
            padding-bottom: 0.75rem;
            margin-bottom: 2rem;
            font-weight: 600;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .form-label {
            font-weight: 600;
            color: var(--dark-color);
            margin-bottom: 0.5rem;
        }
        
        .form-control, .form-select {
            border-radius: 0.35rem;
            padding: 0.75rem 1rem;
            border: 1px solid #d1d3e2;
            transition: all 0.3s;
        }
        
        .form-control:focus, .form-select:focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 0.25rem rgba(78, 115, 223, 0.25);
        }
        
        .preview-container {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            margin-top: 15px;
        }
        
        .preview-item {
            position: relative;
            width: 150px;
            transition: all 0.3s;
        }
        
        .preview-item:hover {
            transform: translateY(-5px);
        }
        
        .preview-img {
            width: 100%;
            height: 120px;
            object-fit: cover;
            border-radius: 8px;
            border: 1px solid #ddd;
            box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
        }
        
        .remove-btn {
            position: absolute;
            top: -8px;
            right: -8px;
            background: #dc3545;
            color: white;
            border-radius: 50%;
            width: 25px;
            height: 25px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
            cursor: pointer;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
            transition: all 0.2s;
        }
        
        .remove-btn:hover {
            background: #bb2d3b;
            transform: scale(1.1);
        }
        
        .file-input-label {
            display: block;
            padding: 2rem;
            border: 2px dashed #d1d3e2;
            border-radius: 10px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s;
            background-color: var(--secondary-color);
        }
        
        .file-input-label:hover {
            border-color: var(--primary-color);
            background-color: #f0f7ff;
            transform: translateY(-2px);
        }
        
        .file-input-label i {
            color: var(--primary-color);
            margin-bottom: 10px;
        }
        
        .btn-primary {
            background-color: var(--primary-color);
            border-color: var(--primary-color);
            padding: 0.5rem 1.5rem;
            font-weight: 600;
            transition: all 0.3s;
        }
        
        .btn-primary:hover {
            background-color: var(--accent-color);
            border-color: var(--accent-color);
            transform: translateY(-2px);
        }
        
        .btn-secondary {
            padding: 0.5rem 1.5rem;
            font-weight: 600;
            transition: all 0.3s;
        }
        
        .btn-secondary:hover {
            transform: translateY(-2px);
        }
        
        .required-field::after {
            content: " *";
            color: #dc3545;
            font-weight: bold;
        }
        
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }
        
        /* Responsividade */
        @media (max-width: 768px) {
            .form-container {
                padding: 1.5rem;
            }
            
            .preview-item {
                width: 120px;
            }
        }
    </style>
</head>
<body class="bg-light">
    <div class="container py-4">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <div class="form-container animate__animated animate__fadeIn">
                    <h1 class="form-title">
                        <i class="bi bi-file-earmark-medical"></i> ${prova.idProva == 0 ? 'Nova Prova' : 'Editar Prova'}
                    </h1>
                    
                    <c:if test="${not empty erro}">
                        <div class="alert alert-danger alert-dismissible fade show animate__animated animate__shakeX" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i> ${erro}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    
                    <form id="provaForm" action="provas?action=${prova.idProva == 0 ? 'salvar' : 'atualizar'}" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="id" value="${prova.idProva}">
                        
                        <div class="row mb-4">
                            <div class="col-md-6 mb-3">
                                <label for="idQueixa" class="form-label fw-bold required-field">Queixa Relacionada</label>
                                <select class="form-select" id="idQueixa" name="idQueixa" required>
                                    <option value="">Selecione uma queixa...</option>
                                    <c:forEach items="${queixas}" var="queixa">
                                        <option value="${queixa['id']}" ${prova.idQueixa == queixa['id'] ? 'selected' : ''}>
                                            #${queixa['id']} - ${queixa['titulo']}
                                        </option>
                                    </c:forEach>
                                </select>
                                <div class="invalid-feedback">Por favor, selecione uma queixa.</div>
                            </div>
                            
                            <div class="col-md-6 mb-3">
                                <label for="tipo" class="form-label fw-bold required-field">Tipo de Prova</label>
                                <select class="form-select" id="tipo" name="tipo" required>
                                    <option value="">Selecione o tipo...</option>
                                    <option value="Imagem" ${prova.tipo == 'Imagem' ? 'selected' : ''}>Imagem</option>
                                    <option value="Documento" ${prova.tipo == 'Documento' ? 'selected' : ''}>Documento</option>
                                    <option value="Vídeo" ${prova.tipo == 'Vídeo' ? 'selected' : ''}>Vídeo</option>
                                    <option value="Áudio" ${prova.tipo == 'Áudio' ? 'selected' : ''}>Áudio</option>
                                </select>
                                <div class="invalid-feedback">Por favor, selecione um tipo.</div>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="descricao" class="form-label fw-bold required-field">Descrição</label>
                            <textarea class="form-control" id="descricao" name="descricao" rows="4" required>${prova.descricao}</textarea>
                            <div class="invalid-feedback">Por favor, insira uma descrição.</div>
                        </div>
                        
                        <div class="row mb-4">
                            <div class="col-md-6 mb-3">
                                <label for="dataColeta" class="form-label fw-bold">Data de Coleta</label>
                                <input type="date" class="form-control" id="dataColeta" name="dataColeta" 
                                       value="${prova.dataColeta != null ? prova.dataColeta.toString().substring(0, 10) : ''}">
                            </div>
                            
                            <div class="col-md-6 mb-3">
                                <label for="idUsuario" class="form-label fw-bold required-field">Usuário Responsável</label>
                                <select class="form-select" id="idUsuario" name="idUsuario" required>
                                    <option value="">Selecione um usuário...</option>
                                    <c:forEach items="${usuarios}" var="usuario">
                                        <option value="${usuario['id']}" ${prova.idUsuario == usuario['id'] ? 'selected' : ''}>
                                            #${usuario['id']} - ${usuario['nome']}
                                        </option>
                                    </c:forEach>
                                </select>
                                <div class="invalid-feedback">Por favor, selecione um usuário.</div>
                            </div>
                        </div>
                        
                        <div class="mb-4">
                            <label class="form-label fw-bold ${prova.idProva == 0 ? 'required-field' : ''}">Arquivos da Prova</label>
                            <label for="arquivos" class="file-input-label mb-3">
                                <i class="bi bi-cloud-arrow-up fs-1"></i>
                                <p class="mb-2 fw-bold">Arraste e solte arquivos aqui ou clique para selecionar</p>
                                <small class="text-muted">Formatos aceitos: .jpg, .jpeg, .png, .pdf, .mp4, .mp3 (Tamanho máximo: 10MB)</small>
                            </label>
                            <input type="file" class="form-control d-none" id="arquivos" name="arquivos" multiple 
                                   accept="image/*,.pdf,.mp4,.mp3" ${prova.idProva == 0 ? 'required' : ''}>
                            
                            <div class="preview-container" id="previewContainer">
                                <c:if test="${not empty prova.caminhoArquivo}">
                                    <c:forEach items="${prova.caminhoArquivo.split(',')}" var="arquivo" varStatus="loop">
                                        <div class="preview-item">
                                            <img src="${arquivo}" class="preview-img" alt="Arquivo ${loop.index + 1}">
                                            <input type="hidden" name="arquivosExistentes" value="${arquivo}">
                                            <span class="remove-btn" onclick="removerArquivoExistente(this)">×</span>
                                        </div>
                                    </c:forEach>
                                </c:if>
                            </div>
                            <div class="invalid-feedback" id="arquivo-feedback">Pelo menos um arquivo é obrigatório.</div>
                        </div>
                        
                        <div class="d-flex justify-content-end gap-3 mt-4">
                            <a href="provas" class="btn btn-secondary px-4 py-2">
                                <i class="bi bi-x-circle me-2"></i> Cancelar
                            </a>
                            <button type="submit" class="btn btn-primary px-4 py-2">
                                <i class="bi bi-save me-2"></i> Salvar
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Preview de múltiplos arquivos
        document.getElementById('arquivos').addEventListener('change', function(e) {
            const previewContainer = document.getElementById('previewContainer');
            const files = e.target.files;
            
            // Limpar previews de novos arquivos (mantém os existentes)
            const newPreviews = previewContainer.querySelectorAll(':not([data-existing])');
            newPreviews.forEach(preview => preview.remove());
            
            if (files.length > 0) {
                document.getElementById('arquivo-feedback').style.display = 'none';
            }
            
            for (let i = 0; i < files.length; i++) {
                const file = files[i];
                
                const previewItem = document.createElement('div');
                previewItem.className = 'preview-item';
                
                // Verificar tipo de arquivo
                if (file.type.match('image.*')) {
                    const reader = new FileReader();
                    reader.onload = function(e) {
                        const img = document.createElement('img');
                        img.src = e.target.result;
                        img.className = 'preview-img';
                        previewItem.appendChild(img);
                    };
                    reader.readAsDataURL(file);
                } else {
                    const iconDiv = document.createElement('div');
                    iconDiv.className = 'preview-img bg-light d-flex align-items-center justify-content-center';
                    
                    const icon = document.createElement('i');
                    icon.className = 'bi bi-file-earmark-text fs-3 text-muted';
                    
                    const fileType = document.createElement('small');
                    fileType.className = 'position-absolute bottom-0 start-0 end-0 bg-dark text-white text-center';
                    fileType.style.borderBottomLeftRadius = '7px';
                    fileType.style.borderBottomRightRadius = '7px';
                    fileType.textContent = file.name.split('.').pop().toUpperCase();
                    
                    iconDiv.appendChild(icon);
                    previewItem.appendChild(iconDiv);
                    previewItem.appendChild(fileType);
                }
                
                const removeBtn = document.createElement('span');
                removeBtn.className = 'remove-btn';
                removeBtn.innerHTML = '×';
                removeBtn.onclick = function() {
                    previewItem.remove();
                    // Atualizar o input file para remover o arquivo
                    const dataTransfer = new DataTransfer();
                    const filesInput = document.getElementById('arquivos').files;
                    
                    for (let j = 0; j < filesInput.length; j++) {
                        if (j !== i) {
                            dataTransfer.items.add(filesInput[j]);
                        }
                    }
                    
                    document.getElementById('arquivos').files = dataTransfer.files;
                };
                
                previewItem.appendChild(removeBtn);
                previewContainer.appendChild(previewItem);
            }
        });
        
        // Remover arquivo existente
        function removerArquivoExistente(element) {
            if (confirm('Deseja realmente remover este arquivo?')) {
                const parent = element.parentElement;
                const hiddenInput = parent.querySelector('input[type="hidden"]');
                
                if (hiddenInput) {
                    hiddenInput.name = 'arquivosRemovidos'; // Muda o nome para identificar no servidor
                    parent.style.display = 'none'; // Oculta em vez de remover
                } else {
                    parent.remove();
                }
            }
        }
        
        // Validação do formulário
        (function() {
            'use strict';
            
            const form = document.getElementById('provaForm');
            
            form.addEventListener('submit', function(event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                
                // Validação customizada para arquivos
                const arquivos = document.getElementById('arquivos').files;
                const arquivosExistentes = document.querySelectorAll('input[name="arquivosExistentes"]:not([style*="display: none"])');
                
                if (arquivos.length === 0 && arquivosExistentes.length === 0) {
                    event.preventDefault();
                    document.getElementById('arquivo-feedback').style.display = 'block';
                    window.scrollTo({
                        top: document.getElementById('arquivos').offsetTop - 100,
                        behavior: 'smooth'
                    });
                }
                
                form.classList.add('was-validated');
            }, false);
        })();
        
        // Animar elementos ao rolar a página
        document.addEventListener('DOMContentLoaded', function() {
            const animateElements = [
                '.form-title',
                '.row.mb-4',
                '.mb-3',
                '.mb-4',
                '.d-flex.justify-content-end'
            ];
            
            animateElements.forEach((selector, index) => {
                const element = document.querySelector(selector);
                if (element) {
                    setTimeout(() => {
                        element.classList.add('animate__animated', 'animate__fadeInUp');
                        element.style.animationDelay = `${index * 0.1}s`;
                    }, 100);
                }
            });
        });
    </script>
</body>
</html>
