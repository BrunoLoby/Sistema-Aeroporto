package mvc.model.Entities;

import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author Bruno Lobianco
 */
public class Aeroporto {

    private String nome;
    private String cidade;
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

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
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
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.nome);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Aeroporto other = (Aeroporto) obj;
        return Objects.equals(this.nome, other.nome);
    }

    @Override
    public String toString() {
        return "-----------------------------------------------------------------------\n"
                + "ID                : " + id + "\n"
                + "Nome do Aeroporto : " + nome + "\n"
                + "Abreviacao        : " + abreviacao + "\n"
                + "Cidade            : " + cidade + "\n"
                + "Data Criacao      : " + dt_criacao + "\n"
                + "Data Modificacao  : " + dt_modificacao + "\n";

    }

}
