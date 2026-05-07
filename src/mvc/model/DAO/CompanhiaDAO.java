package mvc.model.DAO;

//IMPORTS
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import mvc.control.ConnectionFactory;
import mvc.model.Entities.Companhia;
import mvc.model.Entities.Passageiro;


/**
 *
 * @author Bruno Lobianco
 */
public class CompanhiaDAO {
    
    public Companhia[] companhias = new Companhia[5];
    Scanner scanner = new Scanner(System.in);
    private long contCompanhia;
    
    public CompanhiaDAO(){
        
       Companhia c1 = new Companhia();
       Companhia c2 = new Companhia();
       Companhia c3 = new Companhia();
       
       //COMPANHIA C1
       c1.setNome("AZUL");
       c1.setAbreviacao("AD");
       c1.setDt_criacao(LocalDate.now());
       c1.setDt_modificacao(LocalDate.now());
       
       //COMPANHIA C2
       c2.setNome("GOL");
       c2.setAbreviacao("G3");
       c2.setDt_criacao(LocalDate.now());
       c2.setDt_modificacao(LocalDate.now());
       
       //COMPANHIA C3
       c3.setNome("Air France");
       c3.setAbreviacao("AFR");
       c3.setDt_criacao(LocalDate.now());
       c3.setDt_modificacao(LocalDate.now());
       
       //ADICIONANDO DADOS
       this.adicionarDados(c1);
       this.adicionarDados(c2);
       this.adicionarDados(c3);
    }
    
    private int proximaPosicaoLivre() {
        for (int i = 0; i < companhias.length; i++) {
            if (companhias[i] == null) {
                return i;
            }
        }
        return -1;
    }
    
     public boolean adicionarDados(Companhia companhia){
            int i = proximaPosicaoLivre();
            if(i != -1){
                 companhia.setId(++contCompanhia);
                 companhias[i] = companhia;
                return true;
            }
            return false;
     }


    //METODO DE MAPEAMENTO
    private Companhia mapear(ResultSet rs) throws SQLException {
        Companhia c = new Companhia();
        c.setId(rs.getLong("id"));
        c.setNome(rs.getString("nome"));
        c.setAbreviacao(rs.getString("abreviacao"));
        c.setDt_criacao(rs.getDate("dt_criacao").toLocalDate());
        c.setDt_modificacao(rs.getDate("dt_modificacao").toLocalDate());
        return c;
    }
    
     
     //1.ADICIONAR PASSAGEIRO
    public void adicionarCompanhia()
        {
            System.out.println("\n=== ADICIONAR COMPANHIAS ===");

            //CRIANDO OBJETO 
            Companhia novaCompanhia  = new Companhia();

            System.out.println("Nome: ");
            novaCompanhia.setNome(scanner.nextLine());

            System.out.println("Informe abreviacao:  ");
            novaCompanhia.setAbreviacao(scanner.nextLine());


            novaCompanhia.setDt_criacao(LocalDate.now());
            novaCompanhia.setDt_modificacao(LocalDate.now());

            boolean confirmacao = adicionarCompanhiaBD(novaCompanhia);


            if(confirmacao){
                System.out.println("Companhia adicionada com sucesso!");
            }else{
                System.out.println("Erro ao adicionar companhia!!");
            }

        }
    
    public boolean adicionarCompanhiaBD(Companhia elemento){
        
        String sql = "INSERT INTO companhia"
                +"(nome, abreviacao, dt_criacao, dt_modificacao)"
                +"VALUES (?,?,?,?)";
        
        boolean resultado = false;
        
          try (Connection connection = new ConnectionFactory().getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            // SETANDO OS VALORES DO OBJETO
            stmt.setString(1, elemento.getNome());
            stmt.setString(2, elemento.getAbreviacao());
            stmt.setObject(3, elemento.getDt_criacao());
            stmt.setObject(4, elemento.getDt_modificacao());

            stmt.execute();
            resultado = true; 

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultado;
    }
    
    
    
    
    //2.LISTAR COMPANHIAS
    public void listarCompanhias() {
        System.out.println("\n=== LISTA DE COMPANHIAS ===");

        String sql = "SELECT * FROM companhia";

        List<Companhia> c = new ArrayList<>();

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                c.add(mapear(rs));

                System.out.println(c.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERRO AO LISTAR COMPANHIAS");
        }

    }
    
    
    //BUSCA POR ID
    public Companhia buscaCompanhiaPorId(int id){
        
        String sql = "SELECT * FROM companhia WHERE id = ?";
        
        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                Companhia c = new Companhia();
                c.setId(resultado.getInt("id"));
                c.setNome(resultado.getString("nome"));
                c.setAbreviacao(resultado.getString("abreviacao"));
                c.setDt_criacao(resultado.getDate("dt_criacao").toLocalDate());
                c.setDt_modificacao(resultado.getDate("dt_modificacao").toLocalDate());

                return c;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    
    public void alterarCompanhia()
    {
        System.out.println("\n=== ALTERAR COMPANHIAS ===");

        //LISTAGEM DOS PASSAGEIROS
        this.listarCompanhias();
        
        //PEDE ID PARA ALTERACAO
        System.out.println("\nInforme o ID da Companhia: ");
        int id =  scanner.nextInt();
        scanner.nextLine();
        
        Companhia CompanhiaEncontrada = buscaCompanhiaPorId(id);
        
        
        if(CompanhiaEncontrada == null){
            System.out.println("Companhia com ID " + id + " nao encontrado!!");
            return;
        }
        
        //FAZENDO PEDIDO DE ALTERACAO DOS DADOS
        System.out.println("Informe os novos dados");
        
        //NOME
        System.out.println("Novo nome: ");
        String novoNome = scanner.nextLine();
        //TRIM REMOVE ESPACOS EM BRANCO// IS EMPTY VERIFICA SE FICOU VAZIO
        if(!novoNome.trim().isEmpty()){
            CompanhiaEncontrada.setNome(novoNome);
        }
        
        //NASCIMENTO
        System.out.println("Nova Abreviacao: ");
        String novoAbreviacao = scanner.nextLine();
        if(!novoAbreviacao.trim().isEmpty())
        {
            CompanhiaEncontrada.setAbreviacao(novoAbreviacao);
        }
        

        //ALTERANDO DATA MODIFICACAO
        CompanhiaEncontrada.setDt_modificacao(LocalDate.now());
      
        
        //BANCO DE DADOS
        String sql = "UPDATE companhia SET abreviacao = ?, nome = ?, dt_modificacao = ? WHERE id = ?";
        
        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, CompanhiaEncontrada.getAbreviacao());
            stmt.setString(2, CompanhiaEncontrada.getNome());
            stmt.setObject(3, CompanhiaEncontrada.getDt_modificacao());
            stmt.setInt(4, (int) CompanhiaEncontrada.getId());
           

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar companhia!");
            e.printStackTrace();
            return;
        }

        System.out.println("Companhia Alterada com sucesso!");    
    }
        
    
    //4.DELETAR PASSAGEIRO
    public void deletarCompanhia() 
        {
            System.out.println("\n=== DELETAR COMPANHIA ===");

            //LISTAGEM DOS PASSAGEIROS
            this.listarCompanhias();

            //PEDE ID PARA DELETAR
            System.out.println("\nInforme o ID da Companhia que deseja DELETAR: ");
            int id =  scanner.nextInt();
            scanner.nextLine();
            
            String sql = "DELETE FROM companhia WHERE id = ?";

            try (Connection connection = new ConnectionFactory().getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

                stmt.setInt(1, id);

                int linhas = stmt.executeUpdate();

                if (linhas > 0) {
                    System.out.println("COMPANHIA DELETADO COM SUCESSO!!");
                } else {
                    System.out.println("ERRO: COMPANHIA NAO ENCONTRADO!!");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            
            
        }
    
    
}
