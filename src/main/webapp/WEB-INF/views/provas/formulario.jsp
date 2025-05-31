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
    <style>
        /* Estilos permanecem os mesmos que você forneceu */
    </style>
</head>
<body class="bg-light">
    <div class="container py-5">
        <div class="form-container">
            <h1 class="form-title">
                <i class="bi bi-file-earmark-medical"></i> ${prova.idProva == 0 ? 'Nova Prova' : 'Editar Prova'}
            </h1>
            
            <c:if test="${not empty erro}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${erro}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            
            <form id="provaForm" action="provas?action=${prova.idProva == 0 ? 'salvar' : 'atualizar'}" method="post" enctype="multipart/form-data">
                <input type="hidden" name="id" value="${prova.idProva}">
                
                <!-- Campos do formulário permanecem os mesmos -->
                
                <div class="mb-4">
                    <label class="form-label fw-bold ${prova.idProva == 0 ? 'required-field' : ''}">Arquivos da Prova</label>
                    <label for="arquivos" class="file-input-label">
                        <i class="bi bi-cloud-arrow-up fs-1"></i>
                        <p class="mb-0">Arraste e solte arquivos aqui ou clique para selecionar</p>
                        <small class="text-muted">Formatos aceitos: .jpg, .jpeg, .png, .pdf, .mp4, .mp3</small>
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
                
                <div class="d-flex justify-content-end gap-2">
                    <a href="provas" class="btn btn-secondary px-4">
                        <i class="bi bi-x-circle"></i> Cancelar
                    </a>
                    <button type="submit" class="btn btn-primary px-4">
                        <i class="bi bi-save"></i> Salvar
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Script para manipulação de múltiplos arquivos
        document.getElementById('arquivos').addEventListener('change', function(e) {
            const previewContainer = document.getElementById('previewContainer');
            const files = e.target.files;
            
            // Limpar previews temporários (mantém os existentes)
            const tempPreviews = previewContainer.querySelectorAll('[data-temp]');
            tempPreviews.forEach(preview => preview.remove());
            
            if (files.length > 0) {
                document.getElementById('arquivo-feedback').style.display = 'none';
            }
            
            for (let i = 0; i < files.length; i++) {
                const file = files[i];
                const previewItem = document.createElement('div');
                previewItem.className = 'preview-item';
                previewItem.setAttribute('data-temp', 'true');
                
                if (file.type.match('image.*')) {
                    const reader = new FileReader();
                    reader.onload = function(e) {
                        const img = document.createElement('img');
                        img.src = e.target.result;
                        img.className = 'preview-img';
                        previewItem.appendChild(img);
                        
                        const removeBtn = document.createElement('span');
                        removeBtn.className = 'remove-btn';
                        removeBtn.innerHTML = '×';
                        removeBtn.onclick = function() {
                            previewItem.remove();
                            removeFileFromInput(file.name);
                        };
                        
                        previewItem.appendChild(removeBtn);
                        previewContainer.appendChild(previewItem);
                    };
                    reader.readAsDataURL(file);
                } else {
                    // Tratamento para arquivos não-imagem
                    previewItem.innerHTML = `
                        <div class="preview-img bg-light d-flex align-items-center justify-content-center">
                            <i class="bi bi-file-earmark-text fs-3 text-muted"></i>
                        </div>
                        <span class="remove-btn" onclick="this.parentElement.remove(); removeFileFromInput('${file.name}')">×</span>
                    `;
                    previewContainer.appendChild(previewItem);
                }
            }
        });
        
        function removeFileFromInput(filename) {
            const input = document.getElementById('arquivos');
            const dataTransfer = new DataTransfer();
            
            for (let i = 0; i < input.files.length; i++) {
                if (input.files[i].name !== filename) {
                    dataTransfer.items.add(input.files[i]);
                }
            }
            
            input.files = dataTransfer.files;
        }
        
        function removerArquivoExistente(element) {
            if (confirm('Deseja realmente remover este arquivo?')) {
                const parent = element.parentElement;
                const hiddenInput = parent.querySelector('input[type="hidden"]');
                
                if (hiddenInput) {
                    hiddenInput.name = 'arquivosRemovidos';
                    parent.style.display = 'none';
                }
            }
        }
        
        // Validação do formulário
        document.getElementById('provaForm').addEventListener('submit', function(e) {
            const arquivos = document.getElementById('arquivos').files;
            const arquivosExistentes = document.querySelectorAll('input[name="arquivosExistentes"]:not([style*="display: none"])');
            
            if (arquivos.length === 0 && arquivosExistentes.length === 0) {
                e.preventDefault();
                document.getElementById('arquivo-feedback').style.display = 'block';
                window.scrollTo({
                    top: document.getElementById('arquivos').offsetTop - 100,
                    behavior: 'smooth'
                });
            }
        });
    </script>
</body>
</html>
