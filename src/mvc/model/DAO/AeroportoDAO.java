package mvc.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import mvc.control.ConnectionFactory;
import mvc.model.Entities.Aeroporto;
import mvc.model.Entities.Companhia;

/**
 * @author Bruno Lobianco/ João Pedro Khoury
 */
public class AeroportoDAO {

    public Aeroporto[] aeroportos = new Aeroporto[15];
    Scanner scanner;
    private long contAeroporto;
    
    public AeroportoDAO() {
        scanner = new Scanner(System.in);

        /*//AEROPORTO A1
        Aeroporto a1 = new Aeroporto();
        a1.setNome("Aeroporto Estadual Dr. Leite Lopes");
        a1.setCidade("Ribeirao Preto - SP");
        a1.setAbreviacao("RAO");
        a1.setDt_criacao(LocalDate.now());
        a1.setDt_modificacao(LocalDate.now());

        //AEROPORTO A2
        Aeroporto a2 = new Aeroporto();
        a2.setNome("Aeroporto Mario de Almeida Franco");
        a2.setCidade("Uberaba - MG");
        a2.setAbreviacao("UBA");
        a2.setDt_criacao(LocalDate.now());
        a2.setDt_modificacao(LocalDate.now());

        Aeroporto a3 = new Aeroporto();
        a3.setNome("Aeroporto Internacional de Los Angeles");
        a3.setCidade("Los Angeles - CA");
        a3.setAbreviacao("LAX");
        a3.setDt_criacao(LocalDate.now());
        a3.setDt_modificacao(LocalDate.now());

        //ADCIONANDO AEROPORTOS
        this.adicionarDados(a1);
        this.adicionarDados(a2);
        this.adicionarDados(a3);
        */
    }

    
    //METODO ESSENCIAIS PARA FUNCIONALIDADES EM BANCO DE DADOS
    
    //METODO DE MAPEAMENTO (PEGA TODO CONTEUDO DA LINHA DO BANCO DE DADOS)
    private Aeroporto mapear(ResultSet rs) throws SQLException {
        Aeroporto a = new Aeroporto();
        a.setId(rs.getLong("id"));
        a.setNome(rs.getString("nome"));
        a.setCidade(rs.getString("cidade"));
        a.setAbreviacao(rs.getString("abreviacao"));
        a.setDt_criacao(rs.getDate("dt_criacao").toLocalDate());
        a.setDt_modificacao(rs.getDate("dt_modificacao").toLocalDate());
        return a;
    }

    
    
    //METODO DE BUSCA POR ID
    public Aeroporto buscaAeroportoPorId(int id){
        
        String sql = "SELECT * FROM aeroporto WHERE id = ?";
        
        try(Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            
            if(resultado.next()){
                Aeroporto a = new Aeroporto();
                a.setId(resultado.getInt("id"));
                a.setCidade(resultado.getString("cidade"));
                a.setAbreviacao(resultado.getString("abreviacao"));
                a.setDt_criacao(resultado.getDate("dt_criacao").toLocalDate());
                a.setDt_modificacao(resultado.getDate("dt_modificacao").toLocalDate());
            
                return a;
            }
            
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
 

    // 1.FUNCAO PARA ADICIONAR AEROPORTO
    public void adicionarAeroporto() {
        System.out.println("\n=== ADICIONAR AEROPORTO ===");

        // Cria o objeto primeiro
        Aeroporto novoAeroporto = new Aeroporto();

        System.out.print("Nome: ");
        novoAeroporto.setNome(scanner.nextLine());

        System.out.print("Cidade: ");
        novoAeroporto.setCidade(scanner.nextLine());

        System.out.print("Sigla: ");
        novoAeroporto.setAbreviacao(scanner.nextLine().toUpperCase());

        novoAeroporto.setDt_criacao(LocalDate.now());
        novoAeroporto.setDt_modificacao(LocalDate.now());

        boolean confirmacao = adicionarAeroportoBD(novoAeroporto);
        
        if(confirmacao){
            System.out.println("Aeroporto adicionado com sucesso!!");
        }else{
            System.out.println("Erro ao adicionar aeroporto!!");
        }
        
    }
    
    public boolean adicionarAeroportoBD(Aeroporto elemento){
        String sql = "INSERT INTO aeroporto"
                +"(nome, cidade, abreviacao, dt_criacao, dt_modificacao)"
                +"VALUES (?,?,?,?,?)";
        
        boolean resultado = false;
        
        try (Connection connection = new ConnectionFactory().getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            // SETANDO OS VALORES DO OBJETO
            stmt.setString(1, elemento.getNome());
            stmt.setString(2, elemento.getCidade());
            stmt.setString(3, elemento.getAbreviacao());
            stmt.setObject(4, elemento.getDt_criacao());
            stmt.setObject(5, elemento.getDt_modificacao());

            stmt.execute();
            resultado = true; 

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return resultado;
    }
    
    

    //2.FUNCAO PARA LISTAR AEROPORTOS
    public void listarAeroportos() {
        System.out.println("\n=== LISTA DE AEROPORTOS ===");
       
        String sql = "SELECT * FROM aeroporto";
        
        List<Aeroporto> a = new ArrayList<>();
        
         try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {

                a.add(mapear(resultado));  
            }
            for(Aeroporto aeroporto : a){
            System.out.println(aeroporto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERRO AO LISTAR COMPANHIAS");
        }
        
    }

    //3.ALTERAR INFORMACOES 
    public void alterarAeroporto() {
        System.out.println("\n=== ALTERAR AEROPORTO ===");

        //FAZENDO LISTAGEM DOS AEROPORTOS 
        this.listarAeroportos();

        //PEDE ID PARA ALTERACAO
        System.out.print("\nDigite o ID do aeroporto que deseja alterar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Aeroporto aeroportoEncontrado = buscaAeroportoPorId(id);
     
        if (aeroportoEncontrado == null) {
            System.out.println("Aeroporto com ID " + id + " nao encontrado!!");
            return;
        }

        //FAZENDO PEDIDO DE ALTERACAO DOS DADOS
        System.out.println("Informe os novos dados");
        System.out.print("Novo nome: ");
        String novoNome = scanner.nextLine();
        if (!novoNome.trim().isEmpty()) {
            aeroportoEncontrado.setNome(novoNome);
        }

        System.out.print("Nova cidade: ");
        String novaCidade = scanner.nextLine();
        if (!novaCidade.trim().isEmpty()) {
            aeroportoEncontrado.setCidade(novaCidade);
        }

        System.out.print("Nova sigla: ");
        String novaSigla = scanner.nextLine();
        if (!novaSigla.trim().isEmpty()) {
            aeroportoEncontrado.setAbreviacao(novaSigla.toUpperCase());
        }

        //ALTERANDO DATA MODIFICACAO
        aeroportoEncontrado.setDt_modificacao(LocalDate.now());

        //BANCO DE DADOS
        String sql = "UPDATE aeroporto SET nome = ?, abreviacao = ?, cidade = ?, dt_modificacao=? WHERE id=?";
        
        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql))
        {
            
            stmt.setString(1, aeroportoEncontrado.getNome());
            stmt.setString(2, aeroportoEncontrado.getAbreviacao());
            stmt.setString(3, aeroportoEncontrado.getCidade());
            stmt.setObject(4, aeroportoEncontrado.getDt_modificacao());
            stmt.setInt(5, (int) aeroportoEncontrado.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar passageiro!");
            e.printStackTrace();
            return;}
        
        
        
        
        System.out.println("Aeroporto alterado com sucesso!!!");
    }

    

    
    //4.FUNCAO PARA DELETAR AEROPORTOS
    public void deletarAeroporto() {
        System.out.println("\n=== DELETAR AEROPORTO ===");

        //FAZENDO LISTAGEM DOS AEROPORTOS 
        this.listarAeroportos();

        //PEDE ID PARA DELETAR
        System.out.print("\nDigite o ID do aeroporto que deseja deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM aeroporto WHERE id = ?";
        
        try (Connection connection = new ConnectionFactory().getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("AEROPORTO DELETADO COM SUCESSO!!");
            } else {
                System.out.println("ERRO: AEROPORTO NAO ENCONTRADO!!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
       

    }

}
