<%-- 
    Document   : test page
    Created on : 27 de abr de 2025, 21:16:50
    Author     : JR5
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Página de Teste</title>
    <style>
        .user-data {
            background-color: #f5f5f5;
            padding: 20px;
            border-radius: 5px;
            margin: 20px 0;
        }
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            padding: 8px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
    </style>
</head>
<body>
    <h1>Teste de Dados do Usuário</h1>
    
    <div class="user-data">
        <h2>Dados da Sessão:</h2>
        <table>
            <tr>
                <th>Atributo</th>
                <th>Valor</th>
            </tr>
            <tr>
                <td>ID</td>
                <td>${usuarioLogado.id_usuario}</td>
            </tr>
            <tr>
                <td>Nome</td>
                <td>${usuarioLogado.nome}</td>
            </tr>
            <tr>
                <td>Email</td>
                <td>${usuarioLogado.email}</td>
            </tr>
            <tr>
                <td>Perfil</td>
                <td>${usuarioLogado.perfil}</td>
            </tr>
            <tr>
                <td>Status</td>
                <td>${usuarioLogado.status}</td>
            </tr>
            
            <tr>
                <td>cargo</td>
                <td>${usuarioLogado.estado}</td>
            </tr>
        </table>
    </div>

    <h3>Debug Completo:</h3>
    <pre>${usuarioLogado}</pre>
    
    <a href="logout">Sair</a>
</body>
</html>