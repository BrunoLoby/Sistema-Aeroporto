package mvc.model.Entities;

import java.time.LocalDate;

/**
 *
 * @author Bruno Lobianco
 */

public class Bagagem {
    private long id;
    private int ticket;
    private Passageiro passageiro;
    private String documento;
    private LocalDate dt_criacao;
    private LocalDate dt_modificacao;

 

    //GETTES E SETTERS  
    
    public Passageiro getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(Passageiro passageiro) {
        this.passageiro = passageiro;
    }
    
    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return
                "-----------------------------------------------------------------------\n"
                + "ID                : " + id + "\n"
                + "Documento         : " + documento + "\n"
                + "Passageiro        : " + passageiro.getNome() + "\n"
                + "Data Criacao      : " + dt_criacao + "\n"
                + "Data Modificacao  : " + dt_modificacao + "\n";
    }
    
    
}
