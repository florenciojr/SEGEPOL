<%-- 
    Document   : detalhesCidadao
    Created on : 4 de mai de 2025, 00:49:15
    Author     : JR5
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Ficha Cadastral - ${cidadao.nome}</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f0f2f5;
            color: #333;
        }
        
        .police-header {
            background-color: #2c3e50;
            color: white;
            padding: 15px 0;
            text-align: center;
            border-bottom: 3px solid #e74c3c;
            margin-bottom: 20px;
        }
        
        .police-header h1 {
            margin: 0;
            font-size: 24px;
            letter-spacing: 1px;
        }
        
        .police-header .subtitle {
            font-size: 14px;
            opacity: 0.8;
            margin-top: 5px;
        }
        
        .badge-id {
            background-color: #e74c3c;
            color: white;
            padding: 3px 8px;
            border-radius: 3px;
            font-size: 12px;
            font-weight: bold;
        }
        
        .police-card {
            max-width: 800px;
            margin: 0 auto 30px;
            background: white;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        
        .police-card-header {
            background-color: #34495e;
            color: white;
            padding: 12px 20px;
            border-bottom: 1px solid #2c3e50;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .police-card-body {
            padding: 20px;
        }
        
        .section-title {
            color: #2c3e50;
            border-bottom: 2px solid #e74c3c;
            padding-bottom: 5px;
            margin: 25px 0 15px;
            font-size: 18px;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        
        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
        }
        
        .info-item {
            margin-bottom: 15px;
        }
        
        .info-label {
            font-weight: bold;
            color: #7f8c8d;
            font-size: 13px;
            text-transform: uppercase;
            margin-bottom: 3px;
            letter-spacing: 0.5px;
        }
        
        .info-value {
            color: #2c3e50;
            font-size: 15px;
            padding: 8px;
            background-color: #f8f9fa;
            border-left: 3px solid #e74c3c;
            border-radius: 0 3px 3px 0;
        }
        
        .photo-container {
            width: 150px;
            height: 180px;
            border: 1px solid #ddd;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
            background-color: #f8f9fa;
            margin: 0 auto;
        }
        
        .photo-container img {
            max-width: 100%;
            max-height: 100%;
            object-fit: cover;
        }
        
        .no-photo {
            color: #7f8c8d;
            text-align: center;
            padding: 10px;
            font-size: 12px;
        }
        
        .document-info {
            display: flex;
            gap: 20px;
            margin-bottom: 15px;
            flex-wrap: wrap;
        }
        
        .document-item {
            flex: 1;
            min-width: 200px;
        }
        
        .action-buttons {
            display: flex;
            justify-content: flex-end;
            gap: 10px;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }
        
        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            text-decoration: none;
            font-size: 14px;
            font-weight: bold;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            transition: all 0.3s;
        }
        
        .btn-edit {
            background-color: #3498db;
            color: white;
        }
        
        .btn-edit:hover {
            background-color: #2980b9;
        }
        
        .btn-back {
            background-color: #95a5a6;
            color: white;
        }
        
        .btn-back:hover {
            background-color: #7f8c8d;
        }
        
        .not-found {
            text-align: center;
            padding: 40px;
            color: #e74c3c;
            font-size: 18px;
        }
        
        .signature-line {
            border-top: 1px solid #2c3e50;
            width: 200px;
            margin-top: 40px;
            padding-top: 5px;
            text-align: center;
            font-size: 12px;
            color: #7f8c8d;
        }
        
        @media print {
            .action-buttons {
                display: none;
            }
            
            body {
                background-color: white;
                padding: 0;
            }
            
            .police-card {
                box-shadow: none;
                border: none;
            }
        }
    </style>
    <!-- Adicionando Font Awesome para ícones -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="police-header">
        <h1>DEPARTAMENTO DE IDENTIFICAÇÃO CIVIL</h1>
        <div class="subtitle">Sistema Integrado de Cadastro de Cidadãos</div>
    </div>
    
    <div class="police-card">
        <div class="police-card-header">
            <h2>FICHA CADASTRAL</h2>
            <span class="badge-id">ID: ${cidadao.idCidadao}</span>
        </div>
        
        <div class="police-card-body">
            <c:if test="${cidadao != null}">
                <div style="display: flex; gap: 30px; margin-bottom: 20px;">
                    <div class="photo-container">
                        <c:choose>
                            <c:when test="${not empty cidadao.caminhoImagem}">
                                <img src="${pageContext.request.contextPath}/cidadao?action=visualizarImagem&id=${cidadao.idCidadao}" 
                                     alt="Foto do cidadão">
                            </c:when>
                            <c:otherwise>
                                <div class="no-photo">
                                    <i class="fas fa-user" style="font-size: 40px; margin-bottom: 10px; display: block;"></i>
                                    SEM FOTO
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    
                    <div style="flex: 1;">
                        <h3 style="margin: 0 0 10px; color: #2c3e50; border-bottom: 1px solid #eee; padding-bottom: 10px;">
                            ${cidadao.nome}
                        </h3>
                        
                        <div class="document-info">
                            <div class="document-item">
                                <div class="info-label">Tipo de Documento</div>
                                <div class="info-value">${cidadao.tipoDocumento}</div>
                            </div>
                            <div class="document-item">
                                <div class="info-label">Nº Documento</div>
                                <div class="info-value" style="font-weight: bold;">${cidadao.documentoIdentificacao}</div>
                            </div>
                            <div class="document-item">
                                <div class="info-label">Naturalidade</div>
                                <div class="info-value">${cidadao.naturalidade}</div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <h4 class="section-title">Dados Pessoais</h4>
                <div class="info-grid">
                    <div class="info-item">
                        <div class="info-label">Data de Nascimento</div>
                        <div class="info-value">${cidadao.dataNascimento}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Gênero</div>
                        <div class="info-value">${cidadao.genero}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Telefone</div>
                        <div class="info-value">${cidadao.telefone}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">E-mail</div>
                        <div class="info-value">${cidadao.email}</div>
                    </div>
                </div>
                
                <h4 class="section-title">Endereço Residencial</h4>
                <div class="info-grid">
                    <div class="info-item">
                        <div class="info-label">Província</div>
                        <div class="info-value">${cidadao.provincia}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Cidade</div>
                        <div class="info-value">${cidadao.cidade}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Bairro</div>
                        <div class="info-value">${cidadao.bairro}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Rua</div>
                        <div class="info-value">${cidadao.rua}</div>
                    </div>
                </div>
                
                <div style="margin-top: 40px; text-align: center;">
                    <div class="signature-line">Assinatura do Responsável</div>
                </div>
                
                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/cidadao?action=editar&id=${cidadao.idCidadao}" class="btn btn-edit">Editar Cadastro</a>
                    <a href="${pageContext.request.contextPath}/cidadao" class="btn btn-back">Voltar para Lista</a>
                </div>
            </c:if>
            
            <c:if test="${cidadao == null}">
                <div class="not-found">
                    <h3>REGISTRO NÃO ENCONTRADO</h3>
                    <p>O cidadão solicitado não está cadastrado em nosso sistema</p>
                    <a href="${pageContext.request.contextPath}/cidadao" class="btn btn-back">Voltar para Lista</a>
                </div>
            </c:if>
        </div>
    </div>
    
    <div style="text-align: center; margin-top: 20px; color: #7f8c8d; font-size: 12px;">
        Sistema de Cadastro de Cidadãos | Departamento de Identificação Civil
    </div>
</body>
</html>
