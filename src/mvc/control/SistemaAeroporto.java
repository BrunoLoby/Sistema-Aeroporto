package mvc.control;

//FAZENDO IMPORTS1
import java.util.Scanner;
import java.time.LocalDateTime;
import mvc.model.Entities.Aeroporto;

import mvc.view.Menus;
import mvc.model.DAO.AeroportoDAO;
import mvc.model.Entities.Aeroporto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * Sistema de Aeroporto desenvolvido na disciplina de P.O.O no curso de Análise
 * e Desenvolvimento de Sistemas;
 *
 * Data inicio: 10/11/2025; Data termino: 04/12/2025 ;
 *
 * @author Bruno Lobianco dos Santos ;
 * @author João Pedro Khoury;
 *
 * Acessos para ADM: 
 * 1
 * ADM BRUNO LOGIN: ADM SENHA: 1001
 *
 * ADM JOÃO LOGIN: ADM2  SENHA: 2002
 *
 *
 * @version Fase 2;
 *
 */
public class SistemaAeroporto {

    Menus menus = new Menus();

    public SistemaAeroporto() {
        this.menus = new Menus();

        menus.menuInicial(null);

    }

    public static void main(String[] args) {

        Connection conexao = new ConnectionFactory().getConnection();
        System.out.println("Conexao ao banco realizada com sucesso!");

        new SistemaAeroporto();
    }

}
