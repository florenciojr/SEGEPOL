<%-- 
    Document   : header
    Created on : 7 de mai de 2025, 01:54:36
    Author     : JR5
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-MZ">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SIGEPOL - PRM | República de Moçambique</title>
    
    <!-- CSS Externo -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        :root {
            --prm-verde: #006633;
            --prm-amarelo: #FFD700;
            --prm-preto: #000000;
            --prm-vermelho: #CC0000;
        }
        
        /* Estilos da Navbar */
        .navbar-prm {
            background: linear-gradient(90deg, var(--prm-verde) 0%, var(--prm-preto) 100%);
            border-bottom: 3px solid var(--prm-amarelo);
            box-shadow: 0 2px 10px rgba(0,0,0,0.3);
        }
        
        .navbar-brand-prm {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            font-weight: 700;
        }
        
        .navbar-brand-prm img {
            height: 50px;
            transition: transform 0.3s;
        }
        
        .navbar-brand-prm:hover img {
            transform: scale(1.05);
        }
        
        .nav-link-prm {
            color: white !important;
            font-weight: 500;
            padding: 8px 15px;
            margin: 0 5px;
            border-radius: 4px;
            transition: all 0.3s;
        }
        
        .nav-link-prm:hover {
            background-color: rgba(255, 215, 0, 0.2);
            color: var(--prm-amarelo) !important;
            transform: translateY(-2px);
        }
        
        .dropdown-menu-prm {
            border: 1px solid var(--prm-amarelo);
        }
        
        .badge-prm {
            background-color: var(--prm-vermelho);
            font-size: 0.6rem;
        }
    </style>
</head>
<body>
    <!-- Barra de Navegação Principal -->
    <nav class="navbar navbar-expand-lg navbar-dark navbar-prm fixed-top">
        <div class="container-fluid">
            <!-- Logo e Brand -->
            <a class="navbar-brand navbar-brand-prm" href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/assets/img/logo-prm.png" 
                     alt="PRM" 
                     class="d-inline-block align-top">
                <span class="ms-2 d-none d-md-inline-block">
                    <span class="d-block" style="font-size: 1.1rem;">SIGEPOL</span>
                    <small class="d-block" style="font-size: 0.7rem;">Sistema Integrado de Gestão Policial</small>
                </span>
            </a>
            
            <!-- Botão Mobile -->
            <button class="navbar-toggler" type="button" 
                    data-bs-toggle="collapse" 
                    data-bs-target="#navbarContent">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <!-- Conteúdo da Navbar -->
            <div class="collapse navbar-collapse" id="navbarContent">
                <!-- Menu Principal -->
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <!-- Dropdown Ocorrências -->
                    <li class="nav-item dropdown">
                        <a class="nav-link nav-link-prm dropdown-toggle" 
                           href="#" 
                           role="button" 
                           data-bs-toggle="dropdown">
                            <i class="fas fa-clipboard-list me-1"></i> Ocorrências
                        </a>
                        <ul class="dropdown-menu dropdown-menu-prm">
                            <li>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/ocorrencias?action=nova">
                                    <i class="fas fa-plus-circle text-success me-1"></i> Nova
                                </a>
                            </li>
                            <li><hr class="dropdown-divider"></li>
                            <li>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/ocorrencias?action=list">
                                    <i class="fas fa-list text-primary me-1"></i> Listar
                                </a>
                            </li>
                        </ul>
                    </li>


  <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                        <i class="fas fa-users-cog me-1"></i> Usuários
                    </a>

<ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuarios?action=novo">
                            <i class="fas fa-user-plus text-success me-1"></i> Auto de noticia = agente vai reportar o acontecimento
                        </a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuarios?action=list">
                            <i class="fas fa-list text-primary me-1"></i> Auto de denuncia = o cidadao lesado vai fazer a denuncia
                        </a></li>
                    </ul>



                    
                    <!-- Item Viaturas -->
                    <li class="nav-item">
                        <a class="nav-link nav-link-prm" 
                           href="${pageContext.request.contextPath}/viaturas">
                            <i class="fas fa-car-side me-1"></i> Viaturas
                        </a>
                    </li>
                </ul>
                
                <!-- Menu do Usuário -->
                <div class="d-flex">
                    <div class="dropdown">
                        <a class="nav-link nav-link-prm dropdown-toggle" 
                           href="#" 
                           role="button" 
                           data-bs-toggle="dropdown">
                            <i class="fas fa-user-shield me-1"></i>
                            <c:out value="${usuarioLogado.nome}" default="Usuário"/>
                            <span class="badge badge-prm rounded-pill ms-1">2</span>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end dropdown-menu-prm">
                            <li class="dropdown-header text-center small">
                                PRM - <c:out value="${usuarioLogado.nome}" default="Usuário"/>
                            </li>
                            <li><hr class="dropdown-divider"></li>
                            <li>
                                <a class="dropdown-item" href="#">
                                    <i class="fas fa-cog me-1"></i> Configurações
                                </a>
                            </li>
                            <li>
                                <a class="dropdown-item text-danger" 
                                   href="${pageContext.request.contextPath}/logout">
                                    <i class="fas fa-sign-out-alt me-1"></i> Sair
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </nav>
    
    <!-- Conteúdo Principal -->
    <main class="container-fluid mt-5 pt-4">
