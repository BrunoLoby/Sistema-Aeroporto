package mvc.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import mvc.model.DAO.PassageiroDAO;
import mvc.model.Entities.Passageiro;

public class Relatorios {

    Relatorios relatorios;

    public Relatorios() {
       
    }
    
  
    private static final String PASTA_IMAGENS = "fotos_passageiros/";
    private static final String PASTA_PDFS = "reports/";


    public void gerarPDF(String nomeArquivo) throws DocumentException, IOException {

        // GARANTINDO QUE A REPORTS EXISTA
        File pasta = new File(PASTA_PDFS);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        String caminhoPDF = PASTA_PDFS + nomeArquivo;

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(caminhoPDF));
        document.open();

        Paragraph titulo = new Paragraph("RELATÓRIO DE PASSAGEIROS\n\n");
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        titulo.getFont().setSize(18);
        document.add(titulo);

        
        
        // Carrega passageiros do banco
        PassageiroDAO dao = new PassageiroDAO();
        List<Passageiro> passageiros = dao.listarPassageirosBD();

        for (Passageiro p : passageiros) {
            
            Paragraph bloco = new Paragraph();
            bloco.setAlignment(Paragraph.ALIGN_CENTER);
            
            
            bloco.add(new Paragraph("ID: " + p.getID()));
            bloco.add(new Paragraph("Nome: " + p.getNome()));
            bloco.add(new Paragraph("Documento: " + p.getDocumento()));
            
            document.add(bloco);
            
            // Se tiver uma imagem associada
            if (p.getFoto() != null && !p.getFoto().isEmpty()) {
                try {
                    String caminhoImagem = PASTA_IMAGENS + p.getFoto();
                    Image img = Image.getInstance(caminhoImagem);

                    img.scaleToFit(180, 180); // tamanho bonito
                    img.setAlignment(Image.ALIGN_CENTER);
                    document.add(img);

                } catch (Exception e) {
                    System.out.println("Erro ao carregar a foto de: " + p.getNome());
                }}

            bloco.add(new Paragraph(" "));
        }

        document.close();
        System.out.println("PDF gerado com sucesso: " + caminhoPDF);
    }
    
    
}
