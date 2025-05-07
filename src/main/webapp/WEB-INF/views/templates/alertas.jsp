<%-- 
    Document   : alertas
    Created on : 7 de mai de 2025, 02:59:41
    Author     : JR5
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty param.sucesso}">
    <div class="alert alert-success alert-dismissible fade show">
        <i class="fas fa-check-circle me-2"></i>${param.sucesso}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>
<c:if test="${not empty param.erro}">
    <div class="alert alert-danger alert-dismissible fade show">
        <i class="fas fa-exclamation-circle me-2"></i>${param.erro}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>