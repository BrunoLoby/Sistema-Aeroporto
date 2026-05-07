package mvc.model.Entities;

import java.time.LocalDate;

/**
 * @author Bruno Lobianco
 */
public class Passageiro {
    private String nome;
    private Ticket ticket;
    private long id;
    private String nascimento;
    private LocalDate dt_criacao;
    private LocalDate dt_modificacao;
    private String login;
    private String senha;
    private String documento;
    private String foto;
    
    
    
    //GETTERS E SETTERS

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    
    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public long getID() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public String getNome()
    {
        return nome;
    }
    
    public void setNome(String nome)
    {
        this.nome = nome;
    }

  
    public String pegarNascimento()
    {
        return nascimento;
    }

    public void setNascimento(String nascimento)
    {
        this.nascimento = nascimento;
    }

      public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
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
                  " ID:" + id 
                + " |Nome:" + nome 
                + " |Documento:" + documento 
                + " |Login:" + login 
                + " |Senha:" + senha 
                + " |Nascimeto:" + nascimento 
                + " |Data Criacao:" + dt_criacao 
                + " |Data Modificacao:" + dt_modificacao;     
                
    }
    
    

}
