/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author JR5
 */



import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Modelo que representa a tabela patrulhas do banco de dados
 * com gestão completa de membros na coluna dedicada
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

    public Patrulha() {
        this.membrosIds = new ArrayList<>();
        this.tipo = "Ronda";
        this.status = "Planejada";
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = validarCampoTexto(nome, "Nome", 50);
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

    public void setData(LocalDate data) {
        this.data = Objects.requireNonNull(data, "Data não pode ser nula");
        if (data.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data não pode ser no passado");
        }
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = Objects.requireNonNull(horaInicio, "Hora de início não pode ser nula");
        validarHoraInicio();
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
        if (tipo != null && !tipo.matches("^(Ronda|Operação|Especial)$")) {
            throw new IllegalArgumentException("Tipo deve ser Ronda, Operação ou Especial");
        }
        this.tipo = tipo != null ? tipo : "Ronda";
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes != null ? observacoes.trim() : null;
        if (this.observacoes != null && this.observacoes.length() > 65535) {
            throw new IllegalArgumentException("Observações excedem o tamanho máximo permitido");
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status != null && !status.matches("^(Planejada|Em Andamento|Concluída|Cancelada)$")) {
            throw new IllegalArgumentException("Status inválido");
        }
        this.status = status != null ? status : "Planejada";
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

    // Métodos específicos para gestão de membros

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

    public String getMembrosAsString() {
        if (membrosIds.isEmpty()) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (Integer id : membrosIds) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(id);
        }
        return sb.toString();
    }

    public void setMembrosFromString(String membrosStr) {
        this.membrosIds = new ArrayList<>();
        
        if (membrosStr == null || membrosStr.trim().isEmpty()) {
            return;
        }
        
        String[] ids = membrosStr.split(",");
        for (String idStr : ids) {
            try {
                int id = Integer.parseInt(idStr.trim());
                if (id > 0 && !this.membrosIds.contains(id)) {
                    this.membrosIds.add(id);
                }
            } catch (NumberFormatException e) {
                System.err.println("ID inválido ignorado: " + idStr);
            }
        }
    }

    public boolean isMembro(int idUsuario) {
        return this.membrosIds.contains(idUsuario) || this.responsavelId == idUsuario;
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

    private void validarHoraInicio() {
        if (this.data != null && this.data.equals(LocalDate.now()) && 
            horaInicio.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("Hora de início não pode ser no passado para a data atual");
        }
    }

    // Métodos de negócio

    public boolean estaAtiva() {
        return "Em Andamento".equals(this.status) && 
               this.data != null && 
               this.horaInicio != null;
    }

    public boolean podeSerIniciada() {
        return "Planejada".equals(this.status) && 
               this.data != null && 
               this.horaInicio != null &&
               !this.membrosIds.isEmpty();
    }

    public boolean podeSerFinalizada() {
        return "Em Andamento".equals(this.status);
    }

    // Representações do objeto

    public String toResumo() {
        return String.format("Patrulha %d: %s (%s) - %s %s", 
                idPatrulha, nome, tipo, data, horaInicio);
    }

    public String toDetalhes() {
        return String.format(
            "Patrulha %d\nNome: %s\nResponsável: %d\nData: %s\nHora Início: %s\n" +
            "Hora Fim: %s\nTipo: %s\nStatus: %s\nMembros: %s\nObservações: %s",
            idPatrulha, nome, responsavelId, data, horaInicio, 
            horaFim != null ? horaFim : "N/A", 
            tipo, status, 
            String.join(", ", getMembrosAsString()),
            observacoes != null ? observacoes : "Nenhuma"
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
                ", observacoes='" + observacoes + '\'' +
                ", status='" + status + '\'' +
                ", criadoEm=" + criadoEm +
                ", atualizadoEm=" + atualizadoEm +
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
}