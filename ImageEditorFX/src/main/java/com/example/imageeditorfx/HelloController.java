package com.example.imageeditorfx;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.plugin.ContrastEnhancer;
import ij.plugin.filter.Convolver;
import ij.plugin.filter.GaussianBlur;
import ij.plugin.filter.RankFilters;
import ij.plugin.filter.ThresholdToSelection;
import ij.process.ImageProcessor;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.ResourceBundle;

public class HelloController implements Initializable {


    public ImageView imageView;
    public MenuItem miSalvar;
    public MenuItem miNegativo;
    public MenuItem miTonsCinza;
    public MenuItem miPretoBranco;
    public MenuItem miEspelharH;
    public MenuItem miEspelharV;
    public MenuItem miSalvarComo;
    public MenuItem miCorroer;
    public MenuItem miBina;
    public MenuItem miDectar;
    public MenuItem miBlur;
    public MenuItem miDilatar;
    private BreakIterator propriedades;
    private String arquivoAberto;
    private boolean alterado;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void evtAbrirImagem(ActionEvent actionEvent) {
        Image image=null;
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("c:"));
        fc.setTitle("Escolha um arquivo de imagem");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos JPG (*.jpg)", "*.jpg"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos PNG (*.png)", "*.png"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos JPEG (*.jpeg)","*.jpeg"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos GIF (*.gif)","*.gif"));
        File arquivo = fc.showOpenDialog(null);
        arquivoAberto = arquivo.getAbsolutePath();

        if(arquivo!=null) {
            image=new Image(arquivo.toURI().toString());
            imageView.setImage(new Image(arquivo.toURI().toString()));
            imageView.setFitHeight(image.getHeight());
            imageView.setFitWidth(image.getWidth());
            miSalvar.setDisable(false);
            miNegativo.setDisable(false);
            miEspelharH.setDisable(false);
            miEspelharV.setDisable(false);
            miSalvarComo.setDisable(false);
            miPretoBranco.setDisable(false);
            miTonsCinza.setDisable(false);
            miBina.setDisable(false);
            miCorroer.setDisable(false);
            miDectar.setDisable(false);
            miBlur.setDisable(false);
            miDilatar.setDisable(false);
            alterado = false;
        }
    }

    public void evtSalvarImagem(ActionEvent actionEvent) {
        salvar();
    }

    public void evtSalvarComo(ActionEvent actionEvent){
        FileChooser fc;
        fc=new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos JPG (*.jpg)", "*.jpg"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos PNG (*.png)", "*.png"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos JPEG (*.jpeg)","*.jpeg"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos GIF (*.gif)","*.gif"));
        File arq=fc.showSaveDialog(null);
        if(arq!=null)
        {
            BufferedImage bimg;
            bimg= SwingFXUtils.fromFXImage(imageView.getImage(), null);
            if(arq.getName().endsWith(".jpg") || arq.getName().endsWith(".jpeg")){
                /* tratamento para salvar uma imagem modificada*/
                BufferedImage copy = new BufferedImage(bimg.getWidth(), bimg.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = copy.createGraphics();
                g2d.setColor(Color.WHITE); // Or what ever fill color you want...
                g2d.fillRect(0, 0, copy.getWidth(), copy.getHeight());
                g2d.drawImage(bimg, 0, 0, null);
                g2d.dispose();
                /* fim do tratamento*/
                try
                {
                    ImageIO.write(copy, "jpg", arq);
                }catch(Exception e)
                {
                    System.out.println(e.getMessage()); }
            }
            else if(arq.getName().endsWith(".png")){
                try
                {
                    ImageIO.write(bimg, "png", arq);
                }catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
            else{
                try
                {
                    ImageIO.write(bimg, "gif", arq);
                }catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }

        }
    }

    private void salvar(){
        Image img = imageView.getImage();
        File arq = new File(arquivoAberto);
        BufferedImage bimg = SwingFXUtils.fromFXImage(imageView.getImage(),null);
        if(arq.getName().endsWith(".jpg") || arq.getName().endsWith(".jpeg")){
            BufferedImage copy = new BufferedImage(bimg.getWidth(), bimg.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = copy.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, copy.getWidth(), copy.getHeight());
            g2d.drawImage(bimg, 0, 0, null);
            g2d.dispose();
            try
            {
                ImageIO.write(copy, "jpg", arq);
            }catch(Exception e)
            {
                System.out.println(e.getMessage()); }
        }
        else if(arq.getName().endsWith(".png")){
            try
            {
                ImageIO.write(bimg, "png", arq);
            }catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        else{
            try
            {
                ImageIO.write(bimg, "gif", arq);
            }catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    public void evtFechar(ActionEvent actionEvent) {
        if(alterado){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Verificação");
            alert.setHeaderText("Você deseja fechar sem salvar?");
            alert.setContentText("Se quiser salvar clique em salvar ou só em fechar para fechar");
            ButtonType salvar = new ButtonType("Salvar");
            ButtonType fechar = new ButtonType("Fechar", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(salvar,fechar);
            ButtonType resultado = alert.showAndWait().orElse(fechar);
            if(resultado == salvar){
                salvar();
                imageView.setImage(null);
            }
            else{
                imageView.setImage(null);
            }
        }
        else {
            imageView.setImage(null);
        }
        alterar(false);
    }
    private void alterar(boolean alter){
        miSalvar.setDisable(!alter);
        miNegativo.setDisable(!alter);
        miEspelharH.setDisable(!alter);
        miEspelharV.setDisable(!alter);
        miSalvarComo.setDisable(!alter);
        miPretoBranco.setDisable(!alter);
        miTonsCinza.setDisable(!alter);
        miBina.setDisable(!alter);
        miCorroer.setDisable(!alter);
        miDectar.setDisable(!alter);
        miBlur.setDisable(!alter);
        miDilatar.setDisable(!alter);
        alterado = alter;
    }

    public void evtTonsCinza(ActionEvent actionEvent) {
        convertGrayScale();
        alterado = true;
    }

    private void convertGrayScale() {
        int media,pixel[]={0,0,0,0};
        Image tmp = imageView.getImage();
        BufferedImage bimg= SwingFXUtils.fromFXImage(tmp,null);
        for (int y = 0; y < tmp.getHeight(); y++) {
            for (int x = 0; x < tmp.getWidth(); x++) {
                bimg.getRaster().getPixel(x,y,pixel);
                media=(int)(pixel[0]*0.299+pixel[1]*0.587+pixel[2]*0.114);
                pixel[0]=pixel[1]=pixel[2]=media;
                bimg.getRaster().setPixel(x,y,pixel);
            }
        }
        tmp = SwingFXUtils.toFXImage(bimg,null);
        imageView.setImage(tmp);
    }

    public void evtPretoBranco(ActionEvent actionEvent) {
        int media,pixel[]={0,0,0,0};
        Image tmp = imageView.getImage();
        BufferedImage bimg= SwingFXUtils.fromFXImage(tmp,null);
        for (int y = 0; y < tmp.getHeight(); y++) {
            for (int x = 0; x < tmp.getWidth(); x++) {
                bimg.getRaster().getPixel(x,y,pixel);
                media=(pixel[0]+pixel[1]+pixel[2])/3;
                if(media>127.5){
                    pixel[0]=pixel[1]=pixel[2]=255;
                    bimg.getRaster().setPixel(x,y,pixel);
                }
                else{
                    pixel[0]=pixel[1]=pixel[2]=0;
                    bimg.getRaster().setPixel(x,y,pixel);
                }
            }
        }
        //devolve a imagem modificada ao ImageView
        tmp = SwingFXUtils.toFXImage(bimg,null);
        imageView.setImage(tmp);
        alterado = true;
    }

    public void evtDetectarBordas(ActionEvent actionEvent) {
        ImagePlus imagePlus=new ImagePlus();
        Image tmp = imageView.getImage();
        BufferedImage bimg= SwingFXUtils.fromFXImage(tmp,null);

        imagePlus.setImage(bimg);
        imagePlus.getProcessor().findEdges();

        tmp = SwingFXUtils.toFXImage(imagePlus.getBufferedImage(),null);
        imageView.setImage(tmp);
        alterado = true;
    }

    public void evtNegativo(ActionEvent actionEvent) {
        Image temp = imageView.getImage();
        BufferedImage bimg = SwingFXUtils.fromFXImage(temp, null);
        for (int y = 0; y < temp.getHeight(); y++) {
            for (int x = 0; x < temp.getWidth(); x++) {
                int cor = bimg.getRGB(x, y);
                int novoVermelho = 255 - ((cor >> 16) & 0xFF);//comparaçao bit a bit
                int novoVerde = 255 - ((cor >> 8) & 0xFF);
                int novoAzul = 255 - (cor & 0xFF);
                int novaCor = (cor & 0xFF000000) | (novoVermelho << 16) | (novoVerde << 8) | novoAzul;
                bimg.setRGB(x, y, novaCor);
            }
        }
        Image imagemNegativa = SwingFXUtils.toFXImage(bimg, null);
        imageView.setImage(imagemNegativa);
        alterado = true;
    }

    public void evtEspelharV(ActionEvent actionEvent) {
        ImagePlus imagePlus = new ImagePlus();
        Image temp = this.imageView.getImage();
        BufferedImage bimg = SwingFXUtils.fromFXImage(temp, (BufferedImage)null);
        imagePlus.setImage(bimg);
        imagePlus.getProcessor().flipVertical();
        temp = SwingFXUtils.toFXImage(imagePlus.getBufferedImage(), (WritableImage) null);
        this.imageView.setImage(temp);
        alterado = true;
    }

    public void evtEspelharH(ActionEvent actionEvent) {
        ImagePlus imagePlus = new ImagePlus();
        Image temp = this.imageView.getImage();
        BufferedImage bimg = SwingFXUtils.fromFXImage(temp, (BufferedImage)null);
        imagePlus.setImage(bimg);
        imagePlus.getProcessor().flipHorizontal();
        temp = SwingFXUtils.toFXImage(imagePlus.getBufferedImage(), (WritableImage) null);
        this.imageView.setImage(temp);
        alterado = true;
    }

    public void evtSobre(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre");
        alert.setHeaderText(null);
        alert.setContentText("Nome: AppMyPhotoshop\n" +
                "Tecla de atalho: Abrir:Ctrl + A \nSalvar: Ctrl + S\nSalvar Como: Shifit + Ctrl + S\nFecahar:Ctrl + F\nTons de Cinza: Ctrl + T\n" +
                "Sobre: Trabalho de Ferro2\n" +
                "Versão SDK: 20\n" +
                "Desenvolvedor: Luis Felipe Pereira");
        alert.showAndWait();
    }

    public void evtBlur(ActionEvent actionEvent) {
        Image tmp = imageView.getImage();
        BufferedImage bimg = SwingFXUtils.fromFXImage(tmp, null);
        ImagePlus imagePlus = new ImagePlus();
        imagePlus.setImage(bimg);
        ImageProcessor ip = imagePlus.getProcessor();
        ip.blurGaussian(2.0);
        tmp = SwingFXUtils.toFXImage(imagePlus.getBufferedImage(), null);
        imageView.setImage(tmp);
        alterado = true;
    }

    public void evtBinarizacao(ActionEvent actionEvent) {
        Image tmp = imageView.getImage();
        BufferedImage bimg = SwingFXUtils.fromFXImage(tmp, null);
        ImagePlus imagePlus = new ImagePlus();
        imagePlus.setImage(bimg);
        ImageProcessor ip = imagePlus.getProcessor();
        ip.autoThreshold();
        tmp = SwingFXUtils.toFXImage(imagePlus.getBufferedImage(), null);
        imageView.setImage(tmp);
        alterado = true;
    }

    public void evtCorroe(ActionEvent actionEvent) {
        ImagePlus imagePlus = new ImagePlus();
        Image tmp = imageView.getImage();
        BufferedImage bimg = SwingFXUtils.fromFXImage(tmp, null);
        imagePlus.setImage(bimg);
        imagePlus.getProcessor().erode();
        tmp = SwingFXUtils.toFXImage(imagePlus.getBufferedImage(), null);
        imageView.setImage(tmp);
        alterado = true;
    }

    public void evtDilatar(ActionEvent actionEvent) {
        Image tmp = imageView.getImage();
        BufferedImage bimg = SwingFXUtils.fromFXImage(tmp, null);
        ImagePlus imagePlus = new ImagePlus();
        imagePlus.setImage(bimg);
        ImageProcessor ip = imagePlus.getProcessor();
        ip.erode();
        tmp = SwingFXUtils.toFXImage(imagePlus.getBufferedImage(), null);
        imageView.setImage(tmp);
    }

    public void evtAtalhos(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Atalhos");
        alert.setHeaderText(null);
        alert.setContentText("Tecla de atalho:\nAbrir: Ctrl + A \nSalvar: Ctrl + S\nSalvar Como: Shifit + Ctrl + S\nFecahar: Ctrl + F\nTons de Cinza: Ctrl + T\n");
        alert.showAndWait();
    }
}