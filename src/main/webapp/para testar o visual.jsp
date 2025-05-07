<%-- 
    Document   : para testar o visual
    Created on : 7 de mai de 2025, 02:03:25
    Author     : JR5
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-MZ">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SIGEPOL - PRM | República de Moçambique</title>
    <!-- Bootstrap 5.3.0 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome 6.4.0 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        /* Cores PRM */
        :root {
            --prm-verde: #006633;
            --prm-amarelo: #FFD700;
            --prm-preto: #000000;
            --prm-vermelho: #CC0000;
        }
        
        /* Navbar */
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
        
        /* Links */
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
        
        /* Dropdown */
        .dropdown-menu-prm {
            border: 1px solid var(--prm-amarelo);
        }
        
        /* Badge */
        .badge-prm {
            background-color: var(--prm-vermelho);
            font-size: 0.6rem;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark navbar-prm fixed-top">
        <div class="container-fluid">
            <a class="navbar-brand navbar-brand-prm" href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/assets/img/logo-prm.png" 
                     alt="PRM" 
                     class="d-inline-block align-top">
                <span class="ms-2 d-none d-md-inline-block">
                    <span class="d-block" style="font-size: 1.1rem;">SIGEPOL</span>
                    <small class="d-block" style="font-size: 0.7rem;">Sistema Integrado de Gestão Policial</small>
                </span>
            </a>
            
            <button class="navbar-toggler" type="button" 
                    data-bs-toggle="collapse" 
                    data-bs-target="#navbarContent">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item dropdown">
                        <a class="nav-link nav-link-prm dropdown-toggle" 
                           href="#" 
                           role="button" 
                           data-bs-toggle="dropdown">
                            <i class="fas fa-clipboard-list me-1"></i> Ocorrências
                        </a>
                        <ul class="dropdown-menu dropdown-menu-prm">
                            <li><a class="dropdown-item" 
                                   href="${pageContext.request.contextPath}/ocorrencias?action=nova">
                                <i class="fas fa-plus-circle text-success me-1"></i> Nova
                            </a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" 
                                   href="${pageContext.request.contextPath}/ocorrencias?action=list">
                                <i class="fas fa-list text-primary me-1"></i> Listar
                            </a></li>
                        </ul>
                    </li>
                    
                    <li class="nav-item">
                        <a class="nav-link nav-link-prm" 
                           href="${pageContext.request.contextPath}/viaturas">
                            <i class="fas fa-car-side me-1"></i> Viaturas
                        </a>
                    </li>
                </ul>
                
                <div class="d-flex">
                    <div class="dropdown">
                        <a class="nav-link nav-link-prm dropdown-toggle" 
                           href="#" 
                           role="button" 
                           data-bs-toggle="dropdown">
                            <i class="fas fa-user-shield me-1"></i>
                            ${usuarioLogado.nome}
                            <span class="badge badge-prm rounded-pill ms-1">2</span>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end dropdown-menu-prm">
                            <li class="dropdown-header text-center small">
                                PRM - ${usuarioLogado.matricula}
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
    
    <main class="container-fluid mt-5 pt-4">
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
                </main>
        
        <footer class="footer bg-dark text-white pt-4 pb-2">
            <div class="container-fluid">
                <style>
                    /* Estilos do Footer */
                    .footer {
                        border-top: 3px solid var(--prm-amarelo);
                        background-color: var(--prm-preto) !important;
                    }
                    
                    .footer-logo {
                        height: 60px;
                        filter: drop-shadow(0 0 2px rgba(255,215,0,0.5));
                    }
                    
                    .footer-link {
                        color: var(--prm-amarelo) !important;
                        text-decoration: none;
                        transition: all 0.3s;
                    }
                    
                    .footer-link:hover {
                        color: white !important;
                        text-decoration: underline;
                    }
                    
                    .emergencia {
                        font-family: 'Arial Black', sans-serif;
                        color: var(--prm-vermelho);
                    }
                </style>
                
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <div class="d-flex align-items-center">
                            <img src="${pageContext.request.contextPath}/assets/img/brasao-mz.png" 
                                 alt="Brasão de Moçambique" 
                                 class="footer-logo me-3">
                            <div>
                                <h5 class="mb-1">Polícia da República de Moçambique</h5>
                                <small class="text-warning">Ministério do Interior</small>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-6 mb-3 text-md-end">
                        <div class="mb-2">
                            <i class="fas fa-phone-alt me-1 text-warning"></i>
                            <span class="emergencia">EMERGÊNCIAS: 119</span>
                        </div>
                        <div>
                            <a href="#" class="footer-link me-3">
                                <i class="fab fa-facebook-f me-1"></i> PRM Oficial
                            </a>
                            <a href="#" class="footer-link">
                                <i class="fas fa-globe me-1"></i> www.prm.gov.mz
                            </a>
                        </div>
                    </div>
                </div>
                
                <div class="row mt-2">
                    <div class="col-12 text-center">
                        <small class="text-muted">
                            &copy; 2023 SIGEPOL - Versão 1.0 | 
                            Desenvolvido pela <span class="text-warning">PRM/DNTIC</span>
                        </small>
                    </div>
                </div>
            </div>
        </footer>

        <!-- Scripts -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Atualiza data/hora moçambicana
            function atualizarDataHora() {
                const options = {
                    day: '2-digit',
                    month: '2-digit',
                    year: 'numeric',
                    hour: '2-digit',
                    minute: '2-digit',
                    timeZone: 'Africa/Maputo'
                };
                const dataAtual = new Date().toLocaleString('pt-MZ', options);
                console.log(`[SIGEPOL] ${dataAtual}`); // Opcional: log no console
            }
            setInterval(atualizarDataHora, 60000);
            atualizarDataHora();
        </script>
    </body>
</html>
