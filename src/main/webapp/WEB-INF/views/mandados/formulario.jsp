<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${acao == 'create' ? 'Novo' : 'Editar'} Mandado de Prisão</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        :root {
            --primary-color: #2c3e50;
            --secondary-color: #3498db;
            --accent-color: #e74c3c;
        }
        
        body {
            background-color: #f8f9fa;
        }
        
        .form-container {
            max-width: 800px;
            margin: 30px auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1);
            border-top: 5px solid var(--primary-color);
        }
        
        .form-title {
            color: var(--primary-color);
            font-weight: 600;
            margin-bottom: 1.5rem;
            position: relative;
            padding-bottom: 0.5rem;
        }
        
        .form-title:after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 0;
            width: 60px;
            height: 3px;
            background-color: var(--secondary-color);
        }
        
        .required-label:after {
            content: " *";
            color: var(--accent-color);
        }
        
        .btn-primary {
            background-color: var(--primary-color);
            border-color: var(--primary-color);
        }
        
        .btn-primary:hover {
            background-color: #1a252f;
            border-color: #1a252f;
        }
        
        .form-control:focus, .form-select:focus {
            border-color: var(--secondary-color);
            box-shadow: 0 0 0 0.25rem rgba(52, 152, 219, 0.25);
        }
        
        .auto-calculate-btn {
            cursor: pointer;
            color: var(--secondary-color);
            font-size: 0.9rem;
        }
        
        .auto-calculate-btn:hover {
            text-decoration: underline;
        }
        
        .card-suspeito {
            border-left: 3px solid var(--secondary-color);
        }
    </style>
</head>
<body>
    <div class="container py-4">
        <div class="form-container">
            <h2 class="form-title text-center">
                <i class="fas fa-file-signature me-2"></i>
                ${acao == 'create' ? 'Novo Mandado de Prisão' : 'Editar Mandado'}
            </h2>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show d-flex align-items-center">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    <div>${error}</div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            
            <form id="formMandado" action="${pageContext.request.contextPath}/mandados?action=${acao}" method="post" novalidate>
                <c:if test="${acao == 'update'}">
                    <input type="hidden" name="id" value="${mandado.idMandado}">
                </c:if>
                
                <div class="row g-3">
                    <!-- Número do Mandado -->
                    <div class="col-md-6">
                        <label for="numeroMandado" class="form-label required-label">Número do Mandado</label>
                        <input type="text" class="form-control ${acao == 'update' ? 'bg-light' : ''}" 
                               id="numeroMandado" name="numeroMandado" 
                               value="${mandado.numeroMandado}" 
                               ${acao == 'update' ? 'readonly' : 'required'}
                               pattern="[A-Za-z0-9\-]+">
                        <small class="form-text text-muted">Formato: ABC-1234</small>
                    </div>
                    
                    <!-- Tipo de Mandado -->
                    <div class="col-md-6">
                        <label for="tipo" class="form-label required-label">Tipo de Mandado</label>
                        <select class="form-select" id="tipo" name="tipo" required>
                            <option value="">Selecione...</option>
                            <option value="Prisão" ${mandado.tipo == 'Prisão' ? 'selected' : ''}>Prisão</option>
                            <option value="Busca e Apreensão" ${mandado.tipo == 'Busca e Apreensão' ? 'selected' : ''}>Busca e Apreensão</option>
                            <option value="Comparência" ${mandado.tipo == 'Comparência' ? 'selected' : ''}>Comparência</option>
                        </select>
                    </div>
                    
                    <!-- Data de Emissão -->
                    <div class="col-md-6">
                        <label for="dataEmissao" class="form-label required-label">Data de Emissão</label>
                        <input type="date" class="form-control" id="dataEmissao" name="dataEmissao" 
                               value="<fmt:formatDate value='${mandado.dataEmissao}' pattern='yyyy-MM-dd'/>" 
                               required>
                    </div>
                    
                    <!-- Data de Validade (com cálculo automático) -->
                    <div class="col-md-6">
                        <label for="dataValidade" class="form-label">Data de Validade</label>
                        <div class="input-group">
                            <input type="date" class="form-control" id="dataValidade" name="dataValidade" 
                                   value="<fmt:formatDate value='${mandado.dataValidade}' pattern='yyyy-MM-dd'/>">
                            <button class="btn btn-outline-secondary" type="button" id="calcular30Dias">
                                <i class="fas fa-calculator"></i> 30 dias
                            </button>
                        </div>
                    </div>
                    
                    <!-- Status -->
                    <div class="col-md-6">
                        <label for="status" class="form-label required-label">Status</label>
                        <select class="form-select" id="status" name="status" required>
                            <option value="">Selecione...</option>
                            <c:forEach items="${statusMandado}" var="status">
                                <option value="${status}" ${status == mandado.status ? 'selected' : ''}>${status}</option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <!-- Suspeito -->
                    <div class="col-12">
                        <label for="idSuspeito" class="form-label required-label">Suspeito</label>
                        <select class="form-select" id="idSuspeito" name="idSuspeito" required>
                            <option value="">Selecione um suspeito...</option>
                            <c:forEach items="${suspeitos}" var="suspeito">
                                <option value="${suspeito.idSuspeito}" 
                                    ${suspeito.idSuspeito == mandado.idSuspeito ? 'selected' : ''}>
                                    ${suspeito.cidadaoNome} (ID: ${suspeito.idSuspeito})
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    
                    <!-- Descrição -->
                    <div class="col-12">
                        <label for="descricao" class="form-label">Descrição</label>
                        <textarea class="form-control" id="descricao" name="descricao" rows="4" 
                                  maxlength="500" placeholder="Detalhes sobre o mandado...">${mandado.descricao}</textarea>
                        <small class="form-text text-muted">Máximo 500 caracteres</small>
                    </div>
                </div>
                
                <hr class="my-4">
                
                <div class="d-flex justify-content-between">
                    <a href="${pageContext.request.contextPath}/mandados?action=list" class="btn btn-outline-secondary">
                        <i class="fas fa-arrow-left me-1"></i> Voltar
                    </a>
                    
                    <div>
                        <button type="button" class="btn btn-outline-danger me-2" id="btnLimpar">
                            <i class="fas fa-eraser me-1"></i> Limpar
                        </button>
                        
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save me-1"></i> ${acao == 'create' ? 'Cadastrar' : 'Atualizar'}
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    
    <!-- Custom Script -->
    <script>
        $(document).ready(function() {
            // Cálculo automático da data de validade quando a data de emissão é alterada
            $('#dataEmissao').change(function() {
                calcularDataValidade(30);
            });
            
            // Cálculo de 30 dias para comparecimento (botão manual)
            $('#calcular30Dias').click(function() {
                calcularDataValidade(30);
            });
            
            // Função para calcular a data de validade
            function calcularDataValidade(dias) {
                const dataEmissao = $('#dataEmissao').val();
                
                if (!dataEmissao) {
                    alert('Informe a data de emissão primeiro!');
                    $('#dataEmissao').focus();
                    return;
                }
                
                const date = new Date(dataEmissao);
                date.setDate(date.getDate() + dias);
                
                // Formata a data para o padrão yyyy-MM-dd
                const formattedDate = date.toISOString().split('T')[0];
                $('#dataValidade').val(formattedDate);
                
                // Mostra feedback visual
                const feedback = $('<div class="alert alert-success alert-dismissible fade show mt-2">');
                feedback.html('<i class="fas fa-check-circle me-2"></i>Data calculada: ' + 
                    formatarDataBR(formattedDate) + ' (' + dias + ' dias após emissão)');
                feedback.append('<button type="button" class="btn-close" data-bs-dismiss="alert"></button>');
                
                $('#dataValidade').after(feedback);
                
                // Remove o feedback após 5 segundos
                setTimeout(() => {
                    feedback.alert('close');
                }, 5000);
            }
            
            // Formatar data para exibição BR
            function formatarDataBR(dataISO) {
                const [ano, mes, dia] = dataISO.split('-');
                return `${dia}/${mes}/${ano}`;
            }
            
            // Carrega informações do suspeito quando selecionado
            $('#idSuspeito').change(function() {
                const suspeitoId = $(this).val();
                if (suspeitoId) {
                    const suspeitoSelecionado = $(this).find('option:selected');
                    const nomeSuspeito = suspeitoSelecionado.text().split('(ID:')[0].trim();
                    const idSuspeito = suspeitoSelecionado.val();
                    
                    $('#infoSuspeito').html(`
                        <p class="mb-1"><strong>Nome:</strong> ${nomeSuspeito}</p>
                        <p class="mb-1"><strong>ID:</strong> ${idSuspeito}</p>
                        <p class="mb-0"><strong>Status:</strong> <span class="badge bg-primary">Ativo</span></p>
                    `);
                } else {
                    $('#infoSuspeito').html('<p class="text-muted mb-0">Selecione um suspeito para visualizar as informações</p>');
                }
            });
            
            // Validação do formulário
            $('#formMandado').on('submit', function(e) {
                let isValid = true;
                
                $('[required]').each(function() {
                    if (!$(this).val()) {
                        $(this).addClass('is-invalid');
                        isValid = false;
                    } else {
                        $(this).removeClass('is-invalid');
                    }
                });
                
                if (!isValid) {
                    e.preventDefault();
                    
                    // Mostra alerta bonito
                    const alertHtml = `
                        <div class="alert alert-danger alert-dismissible fade show d-flex align-items-center mb-4">
                            <i class="fas fa-exclamation-triangle me-2"></i>
                            <div>Preencha todos os campos obrigatórios!</div>
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    `;
                    
                    // Remove alertas existentes
                    $('.alert-dismissible').alert('close');
                    
                    // Adiciona o novo alerta
                    $('.form-title').after(alertHtml);
                    
                    // Rola para o topo
                    $('html, body').animate({ scrollTop: 0 }, 'fast');
                }
            });
            
            // Botão limpar
            $('#btnLimpar').on('click', function() {
                $('#formMandado')[0].reset();
                $('.is-invalid').removeClass('is-invalid');
                $('#infoSuspeito').html('<p class="text-muted mb-0">Selecione um suspeito para visualizar as informações</p>');
            });
            
            // Validação da data de validade
            $('#dataValidade').change(function() {
                const emissao = new Date($('#dataEmissao').val());
                const validade = new Date($(this).val());
                
                if (validade < emissao) {
                    // Mostra toast de erro
                    const toastHtml = `
                        <div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11">
                            <div class="toast show" role="alert" aria-live="assertive" aria-atomic="true">
                                <div class="toast-header bg-danger text-white">
                                    <strong class="me-auto"><i class="fas fa-exclamation-circle me-1"></i> Erro</strong>
                                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast" aria-label="Close"></button>
                                </div>
                                <div class="toast-body">
                                    Data de validade não pode ser anterior à data de emissão!
                                </div>
                            </div>
                        </div>
                    `;
                    
                    $('body').append(toastHtml);
                    
                    // Remove o toast após 5 segundos
                    setTimeout(() => {
                        $('.toast').toast('dispose');
                    }, 5000);
                    
                    $(this).val('');
                }
            });
            
            // Máscara para o número do mandado
            $('#numeroMandado').on('input', function() {
                $(this).val($(this).val().toUpperCase());
            });
        });
    </script>
</body>
</html>
