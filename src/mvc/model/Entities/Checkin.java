package mvc.model.Entities;

import java.time.LocalDate;

/**
 *
 * @author Bruno Lobianco
 */

public class Checkin {

    private int id;
    private static long serial;
    Ticket ticket;
    private String documento;
    private LocalDate dt_criacao;
    private LocalDate dt_modificacao;

    //CONSTRUTOR
    

    //GETTERS E SETTERS 

    public long getId() {
        return id;
    }

    public static long getSerial() {
        return serial;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public String getDocumento() {
        return documento;
    }

    public LocalDate getDt_criacao() {
        return dt_criacao;
    }

    public LocalDate getDt_modificacao() {
        return dt_modificacao;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static void setSerial(long serial) {
        Checkin.serial = serial;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public void setDt_criacao(LocalDate dt_criacao) {
        this.dt_criacao = dt_criacao;
    }

    public void setDt_modificacao(LocalDate dt_modificacao) {
        this.dt_modificacao = dt_modificacao;
    }
    

}
