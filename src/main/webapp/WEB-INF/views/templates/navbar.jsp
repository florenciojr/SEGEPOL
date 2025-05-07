<%-- 
    Document   : navbar
    Created on : 7 de mai de 2025, 02:41:47
    Author     : JR5
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm fixed-top" style="background-color: #006633 !important; border-bottom: 3px solid #FFD700;">
    <div class="container-fluid">
        <!-- Logo e Nome do Sistema -->
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">
            <img src="${pageContext.request.contextPath}/assets/img/brasao-mz.png" alt="Brasão de Moçambique" height="40" class="d-inline-block align-top me-2">
            <span class="d-none d-md-inline">
                <span class="fw-bold">SIGEPOL</span>
                <small class="d-block" style="font-size: 0.7rem;">Polícia da República de Moçambique</small>
            </span>
        </a>

        <!-- Botão Mobile -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSIGEPOL">
            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Itens do Menu -->
        <div class="collapse navbar-collapse" id="navbarSIGEPOL">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <!-- Menu Usuários -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                        <i class="fas fa-users-cog me-1"></i> Usuários
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuarios?action=novo">
                            <i class="fas fa-user-plus text-success me-1"></i> Novo Usuário
                        </a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/usuarios?action=list">
                            <i class="fas fa-list text-primary me-1"></i> Listar Usuários
                        </a></li>
                    </ul>
                </li>

                <!-- Menu Ocorrências -->
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/ocorrencias">
                        <i class="fas fa-clipboard-list me-1"></i> Ocorrências
                    </a>
                </li>
            </ul>

            <!-- Área do Usuário -->
            <ul class="navbar-nav">
                <c:if test="${not empty usuarioLogado}">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user-shield me-1"></i> ${usuarioLogado.nome}
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li class="dropdown-header text-center small">
                                PRM - ${usuarioLogado.numero_identificacao}
                            </li>
                            <li><hr class="dropdown-divider"></li>
                            <li>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/usuarios?action=editar&id=${usuarioLogado.id_usuario}">
                                    <i class="fas fa-user-edit me-1"></i> Meu Perfil
                                </a>
                            </li>
                            <li>
                                <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                    <i class="fas fa-sign-out-alt me-1"></i> Sair
                                </a>
                            </li>
                        </ul>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>

<!-- Espaço para compensar a navbar fixa -->
<div style="height: 80px;"></div>