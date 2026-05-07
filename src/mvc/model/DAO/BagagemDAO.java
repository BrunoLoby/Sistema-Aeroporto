package mvc.model.DAO;

import mvc.model.Entities.Bagagem;
import mvc.model.Entities.Ticket;
import mvc.model.Entities.Passageiro;
import java.time.LocalDate;
import java.util.Scanner;

public class BagagemDAO {
    private Bagagem[] bagagens = new Bagagem[100];
    private Scanner scanner = new Scanner(System.in);

    public boolean despacharBagagem(Passageiro passageiro) {
        System.out.println("\n=== DESPACHO DE BAGAGEM ===");
        if (passageiro == null || passageiro.getNome()== null) {
            System.out.println("Ticket inválido.");
            return false;
        }

      
        Bagagem b = new Bagagem();
        b.getId();
        b.setDocumento(passageiro.getDocumento());
        b.setDt_criacao(LocalDate.now());
        b.setDt_modificacao(LocalDate.now());
        b.setPassageiro(passageiro);

        int pos = proximaPosicaoLivre();
        if (pos == -1) {
            System.out.println("Erro: limite de bagagens atingido.");
            return false;
        }

        bagagens[pos] = b;
        System.out.println("Bagagem despachada com sucesso!!");
        return true;
    }

    //METODOS
    
    private int proximaPosicaoLivre() {
        for (int i = 0; i < bagagens.length; i++) {
            if (bagagens[i] == null) return i;
        }
        return -1;
    }

    public void listarBagagens() {
        System.out.println("\n=== LISTA DE BAGAGENS ===");
        for (Bagagem b : bagagens) {
            if (b != null)
                System.out.println(b);
        }
    }
}

