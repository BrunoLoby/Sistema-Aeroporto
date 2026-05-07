package mvc.model.Entities;

import java.time.LocalDate;


/**
 *
 * @author Bruno Lobianco
 */

public class Ticket {
    private long id;
    private long cod;
    private double valor;
    private Voo voo;
    private Assentos assentos;
    private Passageiro passageiro;
    private LocalDate dt_criacao;
    private LocalDate dt_modificacao;
   
    
    //GETTERS E SETTERS

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCod() {
        return cod;
    }

    public void setCod(long cod) {
        this.cod = cod;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

   

    public Voo getVoo() {
        return voo;
    }

    public void setVoo(Voo voo) {
        this.voo = voo;
    }

    public Passageiro getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(Passageiro passageiro) {
        this.passageiro = passageiro;
    }

    public Assentos getAssentos() {
        return assentos;
    }

    public void setAssentos(Assentos assentos) {
        this.assentos = assentos;
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
    
    
    
}
