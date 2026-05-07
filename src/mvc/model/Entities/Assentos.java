package mvc.model.Entities;

import java.time.LocalDate;

/**
 *
 * @author Bruno Lobianco/João Pedro Khoury
 */
public class Assentos {

    private String voo;
    private long id;
    private int numero;
    private String cod_assento;
    private String passageiro;
    private LocalDate dt_modificacao;
    private LocalDate dt_criacao;

    //GETTERS E SETTERS
    public int getNumero() {
        return numero;
    }

    public String getVoo() {
        return voo;
    }

    public void setVoo(String voo) {
        this.voo = voo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCod_assento() {
        return cod_assento;
    }

    public void setCod_assento(String cod_assento) {
        this.cod_assento = cod_assento;
    }

    

    public String getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(String passageiro) {
        this.passageiro = passageiro;
    }

    public LocalDate getDt_modificacao() {
        return dt_modificacao;
    }

    public void setDt_modificacao(LocalDate dt_modificacao) {
        this.dt_modificacao = dt_modificacao;
    }

    public LocalDate getDt_criacao() {
        return dt_criacao;
    }

    public void setDt_criacao(LocalDate dt_criacao) {
        this.dt_criacao = dt_criacao;
    }

    public String toString() {
        return " " + numero;
    }

}
