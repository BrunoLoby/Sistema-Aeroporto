
package mvc.model.Entities;


import java.time.LocalDate;

/**
 *
 * @author Bruno Lobianco
 */

public class Voo {
    
    private int id_companhia;
    private long id = 1;
    private int cod_assento;
    private String origem;
    private String destino;
    private String duracao;
    private Companhia companhia;
    private String estado;
    private int Capacidade;
    private LocalDate dt_criacao;
    private LocalDate dt_modificacao;
    
    
    //GETTERS E SETTERS

    public int getId_companhia() {
        return id_companhia;
    }

    public void setId_companhia(int id_companhia) {
        this.id_companhia = id_companhia;
    }
    
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    

    public int getCod_assento() {
        return cod_assento;
    }

    public void setCod_assento(int cod_assento) {
        this.cod_assento = cod_assento;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public Companhia getCompanhia() {
        return companhia;
    }

    public void setCompanhia(Companhia companhia) {
        this.companhia = companhia;
    }

    

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCapacidade() {
        return Capacidade;
    }

    public void setCapacidade(int Capacidade) {
        this.Capacidade = Capacidade;
    }

    public LocalDate getDt_criacao() {
        return dt_criacao;
    }

    public void setDt_criacao(LocalDate dt_criacao) {
        this.dt_criacao = dt_criacao;
    }

    public LocalDate getDt_modificacao() {
        return dt_modificacao;
    }

    public void setDt_modificacao(LocalDate dt_modificacao) {
        this.dt_modificacao = dt_modificacao;
    }
    
    
    
    
      @Override
    public String toString() {
        return 
                  "ID               :" + id + "\n"
                + "Origem           :" + origem + "\n"
                + "Destino          :" + destino + "\n"
                + "Duracao          :" + duracao + "\n"
                + "Companhia aerea  :" + companhia.getNome() + "\n"
                + "Estado           :" + estado + "\n"
                + "Capacidade       :" + Capacidade + "\n"
                + "Data criacao     :" + dt_criacao + "\n"
                + "Data modificacao :" + dt_modificacao + "\n";
    }
    
    
    
    
}
