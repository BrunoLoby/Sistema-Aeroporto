package mvc.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;


import mvc.control.ConnectionFactory;
import mvc.model.Entities.Assentos;

public class AssentosDAO {

   
    // 1. Gerar assentos para o voo
    public void gerarAssentosParaVoo(int idVoo, int capacidade) {
        String sql = """
            INSERT INTO assentos (cod_assento, id_voo, ocupado, nome_passageiro, dt_criacao, dt_modificacao)
            VALUES (?, ?, 0, NULL, ?, ?)
        """;

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            LocalDate agora = LocalDate.now();

            for (int i = 1; i <= capacidade; i++) {
                stmt.setString(1, "A" + i);
                stmt.setInt(2, idVoo);
                stmt.setDate(3, java.sql.Date.valueOf(agora));
                stmt.setDate(4, java.sql.Date.valueOf(agora));
                stmt.executeUpdate();
            }

            System.out.println("Assentos criados para o voo ID " + idVoo);

        } catch (Exception e) {
            System.out.println("Erro ao gerar assentos: " + e.getMessage());
        }
    }


    // 2. Verifica se já tem assentos gerados
    public boolean temAssentosGerados(int idVoo) {
        String sql = "SELECT COUNT(*) AS qtd FROM assentos WHERE id_voo = ?";

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVoo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("qtd") > 0;
            }

        } catch (Exception e) {
            System.out.println("Erro ao verificar assentos: " + e.getMessage());
        }

        return false;
    }

    // 3. Listar assentos disponíveis
    public void listarAssentosDisponiveis(int idVoo) {
        String sql = "SELECT cod_assento FROM assentos "
                + "WHERE id_voo = ? AND ocupado = 0 "
                + "ORDER BY CAST(SUBSTRING(cod_assento, 2) AS UNSIGNED)";

        try (Connection conn = new ConnectionFactory().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVoo);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\nAssentos disponiveis:");

            StringBuilder linha = new StringBuilder();

            while (rs.next()) {
                String cod = rs.getString("cod_assento");
                linha.append("[").append(cod).append("] ");
            }

            System.out.println(linha.toString());

        } catch (Exception e) {
            System.out.println("Erro ao listar assentos: " + e.getMessage());
            e.printStackTrace();
        }
}



    // =====================================================================
    // 4. Voo está lotado?
    // =====================================================================
    public boolean vooLotado(int idVoo) {
        String sql = "SELECT COUNT(*) AS qtd FROM assentos WHERE id_voo = ? AND ocupado = 0";

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVoo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("qtd") == 0;
            }

        } catch (Exception e) {
            System.out.println("Erro ao verificar lotacao: " + e.getMessage());
        }

        return false;
    }


    // =====================================================================
    // 5. Ocupar assento
    // =====================================================================
    public boolean ocuparAssento(int idVoo, String codAssento, String nomePassageiro) {
        String sql = """
            UPDATE assentos SET 
                ocupado = 1, 
                nome_passageiro = ?,
                dt_modificacao = ?
            WHERE id_voo = ? 
              AND cod_assento = ? 
              AND ocupado = 0
        """;

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomePassageiro);
            stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setInt(3, idVoo);
            stmt.setString(4, codAssento);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Erro ao ocupar assento: " + e.getMessage());
        }

        return false;
    }


   
    // 6. Recuperar objeto Assento completo
    public Assentos pegarAssentoEscolhido(int idVoo, String nomePassageiro) {
        String sql = """
            SELECT * FROM assentos
            WHERE id_voo = ? AND nome_passageiro = ?
        """;

        try (Connection conn = new ConnectionFactory().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVoo);
            stmt.setString(2, nomePassageiro);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Assentos a = new Assentos();
                a.setId(rs.getInt("id"));
                a.setCod_assento(rs.getString("cod_assento"));
                a.setDt_criacao(rs.getDate("dt_criacao").toLocalDate());
                a.setDt_modificacao(rs.getDate("dt_modificacao").toLocalDate());
                return a;
            }

        } catch (Exception e) {
            System.out.println("Erro ao recuperar assento: " + e.getMessage());
        }

        return null;
    }
}
