<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${modo == 'inserir' ? 'Cadastrar' : 'Editar'} Veículo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
    <style>
        .foto-preview {
            max-width: 300px;
            max-height: 300px;
            margin-top: 10px;
            border-radius: 5px;
            display: none;
        }
        .upload-area {
            border: 2px dashed #ccc;
            padding: 20px;
            text-align: center;
            margin-bottom: 20px;
            cursor: pointer;
            transition: all 0.3s;
        }
        .upload-area:hover {
            border-color: #0d6efd;
            background-color: rgba(13, 110, 253, 0.05);
        }
        .required-field::after {
            content: " *";
            color: red;
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h1>${modo == 'inserir' ? 'Cadastrar Novo' : 'Editar'} Veículo</h1>
        
        <c:if test="${not empty erro}">
            <div class="alert alert-danger">${erro}</div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/veiculos" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="action" value="${modo == 'editar' ? 'atualizar' : 'inserir'}">
            
            <c:if test="${modo == 'editar'}">
                <input type="hidden" name="id" value="${veiculo.idVeiculo}">
                <input type="hidden" name="fotoExistente" value="${veiculo.fotoVeiculo}">
            </c:if>
            
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="marca" class="form-label required-field">Marca</label>
                    <input type="text" class="form-control" id="marca" name="marca" 
                           value="${veiculo.marca}" required>
                </div>
                <div class="col-md-6">
                    <label for="modelo" class="form-label required-field">Modelo</label>
                    <input type="text" class="form-control" id="modelo" name="modelo" 
                           value="${veiculo.modelo}" required>
                </div>
            </div>
            
            <div class="row mb-3">
                <div class="col-md-4">
                    <label for="cor" class="form-label required-field">Cor</label>
                    <input type="text" class="form-control" id="cor" name="cor" 
                           value="${veiculo.cor}" required>
                </div>
                <div class="col-md-4">
                    <label for="matricula" class="form-label required-field">Matrícula</label>
                    <input type="text" class="form-control" id="matricula" name="matricula" 
                           value="${veiculo.matricula}" required>
                </div>
                <div class="col-md-4">
                    <label for="ano" class="form-label">Ano</label>
                    <input type="number" class="form-control" id="ano" name="ano" 
                           value="${veiculo.ano}" min="1900" max="${java.time.Year.now().value}">
                </div>
            </div>
            
            <div class="mb-3">
                <label for="idQueixa" class="form-label">Queixa Associada</label>
                <select class="form-select" id="idQueixa" name="idQueixa">
                    <option value="">-- Selecione uma queixa --</option>
                    <c:forEach items="${queixas}" var="queixa">
                        <option value="${queixa.idQueixa}" ${veiculo.idQueixa == queixa.idQueixa ? 'selected' : ''}>
                            #${queixa.idQueixa} - ${queixa.titulo} (${queixa.status})
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="mb-4">
                <label class="form-label">Foto do Veículo</label>
                <div class="upload-area" onclick="document.getElementById('fotoInput').click()" 
                     id="uploadArea">
                    <div id="uploadText">
                        <i class="bi bi-cloud-arrow-up" style="font-size: 2rem;"></i>
                        <p class="mt-2">Clique ou arraste uma foto aqui</p>
                        <small class="text-muted">Formatos: JPG, PNG, GIF (Máx. 5MB)</small>
                    </div>
                    <input type="file" id="fotoInput" name="fotoVeiculo" 
                           accept="image/jpeg, image/png, image/gif" style="display: none;" 
                           onchange="previewFoto(this)">
                </div>
                
                <div class="d-flex align-items-center">
                    <div id="fotoPreviewContainer">
                        <c:if test="${not empty veiculo.fotoVeiculo}">
                            <img src="${pageContext.request.contextPath}/${veiculo.fotoVeiculo}" 
                                 class="foto-preview" id="fotoExistente">
                        </c:if>
                        <img src="" class="foto-preview" id="novaFotoPreview">
                    </div>
                    <c:if test="${not empty veiculo.fotoVeiculo}">
                        <button type="button" class="btn btn-sm btn-danger ms-3" onclick="removerFoto()">
                            <i class="bi bi-trash"></i> Remover Foto
                        </button>
                    </c:if>
                </div>
                <input type="hidden" id="fotoRemovida" name="fotoRemovida" value="false">
            </div>
            
            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                <a href="${pageContext.request.contextPath}/veiculos" class="btn btn-secondary me-md-2">
                    <i class="bi bi-x-circle"></i> Cancelar
                </a>
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-save"></i> ${modo == 'inserir' ? 'Cadastrar' : 'Atualizar'}
                </button>
            </div>
        </form>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Mostrar foto existente ao carregar a página
        document.addEventListener('DOMContentLoaded', function() {
            const fotoExistente = document.getElementById('fotoExistente');
            if (fotoExistente && fotoExistente.src) {
                fotoExistente.style.display = 'block';
            }
            
            // Configurar arrastar e soltar
            const uploadArea = document.getElementById('uploadArea');
            
            uploadArea.addEventListener('dragover', (e) => {
                e.preventDefault();
                uploadArea.style.borderColor = '#0d6efd';
                uploadArea.style.backgroundColor = 'rgba(13, 110, 253, 0.1)';
            });
            
            uploadArea.addEventListener('dragleave', () => {
                uploadArea.style.borderColor = '#ccc';
                uploadArea.style.backgroundColor = '';
            });
            
            uploadArea.addEventListener('drop', (e) => {
                e.preventDefault();
                uploadArea.style.borderColor = '#ccc';
                uploadArea.style.backgroundColor = '';
                
                if (e.dataTransfer.files.length) {
                    const fileInput = document.getElementById('fotoInput');
                    fileInput.files = e.dataTransfer.files;
                    previewFoto(fileInput);
                }
            });
        });
        
        function previewFoto(input) {
            const file = input.files[0];
            const preview = document.getElementById('novaFotoPreview');
            const uploadText = document.getElementById('uploadText');
            
            if (file) {
                // Verificar tamanho do arquivo (máx. 5MB)
                if (file.size > 5 * 1024 * 1024) {
                    alert('O arquivo é muito grande. O tamanho máximo permitido é 5MB.');
                    input.value = '';
                    return;
                }
                
                // Verificar tipo de arquivo
                const validTypes = ['image/jpeg', 'image/png', 'image/gif'];
                if (!validTypes.includes(file.type)) {
                    alert('Formato de arquivo inválido. Use apenas JPG, PNG ou GIF.');
                    input.value = '';
                    return;
                }
                
                const reader = new FileReader();
                reader.onload = function(e) {
                    preview.src = e.target.result;
                    preview.style.display = 'block';
                    
                    // Esconder foto existente se estiver editando
                    const fotoExistente = document.getElementById('fotoExistente');
                    if (fotoExistente) {
                        fotoExistente.style.display = 'none';
                    }
                    
                    // Atualizar texto da área de upload
                    const tamanhoMB = (file.size / (1024 * 1024)).toFixed(2);
                    uploadText.innerHTML = `
                        <i class="bi bi-check-circle" style="font-size: 2rem; color: green;"></i>
                        <p class="mt-2">${file.name}</p>
                        <small class="text-muted">${tamanhoMB} MB</small>
                    `;
                };
                reader.readAsDataURL(file);
            }
        }
        
        function removerFoto() {
            const fotoExistente = document.getElementById('fotoExistente');
            const fotoInput = document.getElementById('fotoInput');
            const uploadText = document.getElementById('uploadText');
            const fotoRemovida = document.getElementById('fotoRemovida');
            
            if (fotoExistente) {
                fotoExistente.style.display = 'none';
            }
            
            if (fotoInput) {
                fotoInput.value = '';
            }
            
            // Resetar texto da área de upload
            uploadText.innerHTML = `
                <i class="bi bi-cloud-arrow-up" style="font-size: 2rem;"></i>
                <p class="mt-2">Clique ou arraste uma foto aqui</p>
                <small class="text-muted">Formatos: JPG, PNG, GIF (Máx. 5MB)</small>
            `;
            
            // Indicar que a foto foi removida
            fotoRemovida.value = 'true';
        }
    </script>
</body>
</html>
