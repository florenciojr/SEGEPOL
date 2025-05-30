/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author JR5
 */



import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Modelo que representa uma patrulha com gestão completa de membros
 */
public class Patrulha {
    private int idPatrulha;
    private String nome;
    private int responsavelId;
    private List<Integer> membrosIds;
    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private String tipo;
    private String observacoes;
    private String status;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private String zonaAtuacao;
 

    public Patrulha() {
        this.membrosIds = new ArrayList<>();
        this.tipo = "Ronda";
        this.status = "Planejada";
        this.zonaAtuacao = "";  // Inicializa o novo campo
    }
    
    
    public static class Builder {
    private Patrulha patrulha = new Patrulha();
    
    public Builder comData(LocalDate data) {
        patrulha.setData(data);
        return this;
    }
    // outros métodos...
    
    public Patrulha build() {
        patrulha.validar();
        return patrulha;
    }
}
    

    // Getters e Setters básicos

    public int getIdPatrulha() {
        return idPatrulha;
    }

    public void setIdPatrulha(int idPatrulha) {
        if (idPatrulha <= 0) {
            throw new IllegalArgumentException("ID da patrulha deve ser positivo");
        }
        this.idPatrulha = idPatrulha;
    }
    
        public String getZonaAtuacao() {
        return zonaAtuacao;
    }
    
    public void setZonaAtuacao(String zonaAtuacao) {
        this.zonaAtuacao = zonaAtuacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = validarCampoTexto(nome, "Nome", 100);
    }

    public int getResponsavelId() {
        return responsavelId;
    }

    public void setResponsavelId(int responsavelId) {
        if (responsavelId <= 0) {
            throw new IllegalArgumentException("ID do responsável deve ser positivo");
        }
        this.responsavelId = responsavelId;
        // Garante que o responsável está na lista de membros
        if (!this.membrosIds.contains(responsavelId)) {
            this.membrosIds.add(responsavelId);
        }
    }

    public LocalDate getData() {
        return data;
    }

public void setData(LocalDate data, boolean validarPassado) {
    this.data = Objects.requireNonNull(data, "Data não pode ser nula");
    
    if (validarPassado && data.isBefore(LocalDate.now())) {
        // Ajuste automático do status para patrulhas com data passada
        if ("Planejada".equals(this.status) || "Em Andamento".equals(this.status)) {
            this.status = "Concluída";
            this.adicionarAoHistorico("Status alterado automaticamente para 'Concluída' devido a data passada");
        }
    }
}

// Método original mantido para compatibilidade
public void setData(LocalDate data) {
    setData(data, true); // Validação ativada por padrão
}

// Método para carregamento do banco de dados
public void setDataFromDB(LocalDate data) {
    setData(data, false); // Validação desativada
}


    public LocalTime getHoraInicio() {
        return horaInicio;
    }

// Método setHoraInicio original (mantido para uso normal)
public void setHoraInicio(LocalTime horaInicio) {
    this.horaInicio = Objects.requireNonNull(horaInicio, "Hora de início não pode ser nula");
    validarHoraInicio(true);  // Sempre valida quando chamado normalmente
}

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        if (horaFim != null && this.horaInicio != null && horaFim.isBefore(this.horaInicio)) {
            throw new IllegalArgumentException("Hora de fim não pode ser antes da hora de início");
        }
        this.horaFim = horaFim;
    }

    public String getTipo() {
        return tipo;
    }

public void setTipo(String tipo) {
    // Normaliza o tipo removendo acentos e convertendo para primeira letra maiúscula
    String tipoNormalizado = normalizarTipo(tipo);
    
    List<String> tiposValidos = List.of("Ronda", "Operacao", "Especial", "Preventiva");
    
    if (tipoNormalizado == null || !tiposValidos.contains(tipoNormalizado)) {
        throw new IllegalArgumentException("Tipo deve ser: " + String.join(", ", tiposValidos));
    }
    this.tipo = tipoNormalizado;
}

// Método auxiliar para normalizar o tipo
private String normalizarTipo(String tipo) {
    if (tipo == null) return null;
    
    // Remove acentos e converte para ASCII simples
    tipo = Normalizer.normalize(tipo, Normalizer.Form.NFD)
                    .replaceAll("[^\\p{ASCII}]", "");
    
    // Converte para primeira letra maiúscula
    return tipo.substring(0, 1).toUpperCase() + tipo.substring(1).toLowerCase();
}

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes != null ? observacoes.trim() : null;
        if (this.observacoes != null && this.observacoes.length() > 2000) {
            throw new IllegalArgumentException("Observações excedem o tamanho máximo de 2000 caracteres");
        }
    }

    public String getStatus() {
        return status;
    }

public void setStatus(String status) {
    // Normaliza o status removendo acentos
    String statusNormalizado = normalizarStatus(status);
    
    List<String> statusValidos = List.of("Planejada", "Em Andamento", "Concluida", "Cancelada");
    
    if (statusNormalizado != null && !statusValidos.contains(statusNormalizado)) {
        throw new IllegalArgumentException("Status inválido. Valores permitidos: " + 
            String.join(", ", statusValidos));
    }
    this.status = statusNormalizado != null ? statusNormalizado : "Planejada";
}

// Método auxiliar para normalizar o status
private String normalizarStatus(String status) {
    if (status == null) return null;
    
    // Remove acentos e converte para ASCII simples
    status = Normalizer.normalize(status, Normalizer.Form.NFD)
                     .replaceAll("[^\\p{ASCII}]", "");
    
    return status;
}

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    // Gestão de Membros

    public List<Integer> getMembrosIds() {
        return Collections.unmodifiableList(membrosIds);
    }

    public void setMembrosIds(List<Integer> membrosIds) {
        Objects.requireNonNull(membrosIds, "Lista de membros não pode ser nula");
        this.membrosIds = new ArrayList<>(membrosIds);
        // Garante que o responsável está na lista
        if (this.responsavelId > 0 && !this.membrosIds.contains(this.responsavelId)) {
            this.membrosIds.add(this.responsavelId);
        }
    }

    public void adicionarMembro(int idMembro) {
        if (idMembro <= 0) {
            throw new IllegalArgumentException("ID do membro deve ser positivo");
        }
        if (idMembro == this.responsavelId) {
            throw new IllegalArgumentException("O responsável já é automaticamente membro");
        }
        if (!this.membrosIds.contains(idMembro)) {
            this.membrosIds.add(idMembro);
        }
    }

    public void removerMembro(int idMembro) {
        if (idMembro == this.responsavelId) {
            throw new IllegalStateException("Não pode remover o responsável da patrulha");
        }
        this.membrosIds.remove(Integer.valueOf(idMembro));
    }

    public boolean contemMembro(int idUsuario) {
        return this.membrosIds.contains(idUsuario) || this.responsavelId == idUsuario;
    }

    public String getMembrosAsString() {
        return membrosIds.stream()
                       .map(String::valueOf)
                       .collect(Collectors.joining(","));
    }

    public void setMembrosFromString(String membrosStr) {
        this.membrosIds = new ArrayList<>();
        
        if (membrosStr == null || membrosStr.trim().isEmpty()) {
            return;
        }
        
        Arrays.stream(membrosStr.split(","))
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .mapToInt(Integer::parseInt)
              .filter(id -> id > 0)
              .distinct()
              .forEach(this.membrosIds::add);
    }

    // Métodos auxiliares para visualização

    public String getNomeResponsavel(Map<Integer, Usuario> usuarios) {
        Usuario usuario = usuarios.get(this.responsavelId);
        return usuario != null ? usuario.getNome() : "ID: " + this.responsavelId;
    }

    public String getCargoResponsavel(Map<Integer, Usuario> usuarios) {
        Usuario usuario = usuarios.get(this.responsavelId);
        return usuario != null ? usuario.getCargo() : "Não encontrado";
    }

    public List<String> getNomesMembros(Map<Integer, Usuario> usuarios) {
        return this.membrosIds.stream()
                            .map(id -> {
                                Usuario usuario = usuarios.get(id);
                                return usuario != null ? usuario.getNome() : "ID: " + id;
                            })
                            .collect(Collectors.toList());
    }

    // Validações de negócio
    public void validar() {
    if (this.data == null) {
        throw new IllegalStateException("Data da patrulha não pode ser nula");
    }
    
    if ("Planejada".equals(this.status) && this.data.isBefore(LocalDate.now())) {
        throw new IllegalStateException("Patrulhas planejadas não podem ter data no passado");
    }
    
    if (this.horaInicio != null && this.horaFim != null && this.horaFim.isBefore(this.horaInicio)) {
        throw new IllegalStateException("Hora de fim não pode ser antes da hora de início");
    }
    
    if (this.membrosIds.size() < 2) {
        throw new IllegalStateException("Patrulha deve ter pelo menos 2 membros");
    }
}

    public boolean podeSerIniciada() {
        return "Planejada".equals(this.status) && 
               this.data != null && 
               this.horaInicio != null &&
               this.membrosIds.size() >= 2; // Pelo menos 2 membros (responsável + outro)
    }

    public boolean podeSerFinalizada() {
        return "Em Andamento".equals(this.status) && 
               this.horaInicio != null;
    }

    public boolean podeSerCancelada() {
        return !"Cancelada".equals(this.status) && 
               !"Concluída".equals(this.status);
    }

    // Métodos auxiliares privados

    private String validarCampoTexto(String valor, String nomeCampo, int tamanhoMaximo) {
        Objects.requireNonNull(valor, nomeCampo + " não pode ser nulo");
        valor = valor.trim();
        if (valor.isEmpty()) {
            throw new IllegalArgumentException(nomeCampo + " não pode ser vazio");
        }
        if (valor.length() > tamanhoMaximo) {
            throw new IllegalArgumentException(nomeCampo + " não pode ter mais que " + tamanhoMaximo + " caracteres");
        }
        return valor;
    }
    
    public void avancarStatus() {
    switch (this.status) {
        case "Planejada":
            if (podeSerIniciada()) this.status = "Em Andamento";
            break;
        case "Em Andamento":
            if (podeSerFinalizada()) this.status = "Concluída";
            break;
        default:
            throw new IllegalStateException("Não é possível avançar o status atual");
    }
}
    
    
    private List<String> historicoAlteracoes = new ArrayList<>();

public void adicionarAoHistorico(String alteracao) {
    this.historicoAlteracoes.add(LocalDateTime.now() + " - " + alteracao);
}

 // Método modificado para validar hora de início
private void validarHoraInicio(boolean validarPassado) {
    if (validarPassado && this.data != null && this.data.equals(LocalDate.now())) {
        if (this.horaInicio.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("Hora de início não pode ser no passado para a data atual");
        }
    }
}

public void validarPatrulha(Patrulha patrulha) {
    if (patrulha.getStatus().equals("Planejada") || patrulha.getStatus().equals("Em Andamento")) {
        if (patrulha.getData().isBefore(LocalDate.now())) {
            // Atualizar status automaticamente ou lançar exceção
            patrulha.setStatus("Concluída");
            // Ou: throw new IllegalStateException("Patrulha com data passada deve ser concluída");
        }
    }}



// Novo método para uso no DAO (sem validação de passado)
public void setHoraInicioFromDB(LocalTime horaInicio) {
    this.horaInicio = Objects.requireNonNull(horaInicio, "Hora de início não pode ser nula");
    // Não chama validarHoraInicio() - usado apenas para carregar dados existentes
}

    // Representações do objeto

    public String toResumo() {
        return String.format("Patrulha %d: %s (%s) - %s %s", 
                idPatrulha, nome, tipo, data, horaInicio);
    }

    public Map<String, Object> toMap() {
        return Map.of(
            "id", idPatrulha,
            "nome", nome,
            "responsavelId", responsavelId,
            "membrosIds", new ArrayList<>(membrosIds), // Cópia defensiva
            "data", data,
            "horaInicio", horaInicio,
            "horaFim", horaFim,
            "tipo", tipo,
            "status", status,
            "observacoes", observacoes != null ? observacoes : ""
        );
    }

    @Override
    public String toString() {
        return "Patrulha{" +
                "idPatrulha=" + idPatrulha +
                ", nome='" + nome + '\'' +
                ", responsavelId=" + responsavelId +
                ", membrosIds=" + membrosIds +
                ", data=" + data +
                ", horaInicio=" + horaInicio +
                ", horaFim=" + horaFim +
                ", tipo='" + tipo + '\'' +
                ", status='" + status + '\'' +
                ", criadoEm=" + criadoEm +
                ", atualizadoEm=" + atualizadoEm +
                ", zonaAtuacao='" + zonaAtuacao + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patrulha patrulha = (Patrulha) o;
        return idPatrulha == patrulha.idPatrulha;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPatrulha);
    }

    public String toDetalhes() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
