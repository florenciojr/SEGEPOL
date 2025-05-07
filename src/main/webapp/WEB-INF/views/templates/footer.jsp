<%-- 
    Document   : footer
    Created on : 7 de mai de 2025, 01:55:01
    Author     : JR5
--%>

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
                            &copy; 2025 SIGEPOL - Versão 1.0 | 
                            Desenvolvido por <span class="text-warning">FLORENCIO A. HONWANA</span>
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