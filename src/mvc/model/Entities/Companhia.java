package mvc.model.Entities;

import java.time.LocalDate;

/**
 *
 * @author Bruno Lobianco
 */

public class Companhia {
    private String nome;
    private String abreviacao;
    private long id;
    private LocalDate dt_criacao;
    private LocalDate dt_modificacao;


    
    
    //GETTERS E SETTERS
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAbreviacao() {
        return abreviacao;
    }

    public void setAbreviacao(String abreviacao) {
        this.abreviacao = abreviacao;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        return "-----------------------------------------------------------------------\n"
                + "ID                : " + id + "\n"
                + "Nome              : " + nome + "\n"
                + "Abreviacao        : " + abreviacao + "\n"
                + "Data Criacao      : " + dt_criacao + "\n"
                + "Data Modificacao  : " + dt_modificacao + "\n";
                
    }
    
    
    


}
