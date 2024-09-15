package Jogo;

import Interface.TelaGameOver;
import Interface.TelaJogo;
import Personagem.*;
import Dungeon.Dungeon;
import Interface.TelaInicial;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class Jogo {
    private Heroi player;
    private Dungeon tabuleiro;
    private Clip bgMusic;
    private int dicasRestantes;
    private boolean debug;
    private boolean gameOver;

    public Jogo() {
        this.setDicasRestantes(3);
        this.debug = false;
        SwingUtilities.invokeLater(() -> new TelaInicial(this).setVisible(true));
    }

    public void setPlayer(Heroi player){
        this.player = player;
    }
    public Heroi getPlayer() {
        return player;
    }
    public Dungeon getTabuleiro() {
        return tabuleiro;
    }
    public void setTabuleiro(Dungeon tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    public int getDicasRestantes() {
        return dicasRestantes;
    }
    public void setDicasRestantes(int dicasRestantes) {
        this.dicasRestantes = dicasRestantes;
    }
    public void setDebug() {
        this.debug = !this.debug;
    }
    public boolean isDebug() {
        return debug;
    }
    public Clip getBgMusic(){
        return bgMusic;
    }
    public void setBgMusic(Clip bgMusic){
        this.bgMusic = bgMusic;
    }
    public void setGameOver() {
        this.gameOver = !this.isGameOver();
    }
    public boolean isGameOver() {
        return gameOver;
    }

    public void iniciar(){
        this.tabuleiro = new Dungeon(this.player, 3, 3, 3, 3);
    }

    public int usarDica(int x){
        this.setDicasRestantes(this.getDicasRestantes()-1);
        try {
            return this.tabuleiro.verificaArmadilhas(x);
        }
        catch (Exception e){
            this.setDicasRestantes(this.getDicasRestantes()+1);
            return 0;
        }
    }

    public void gameOver(JFrame parent){
        if(this.player.getHp() <= 0){
            this.setGameOver();
            TelaGameOver tela = new TelaGameOver(parent, false, this);
            this.getBgMusic().stop();
            tela.setVisible(true);
            parent.dispose();
        }
    }

    public void reiniciar(){
        this.setDicasRestantes(3);
        this.player.resetarAtributos();
        this.tabuleiro.reiniciarTabuleiro();
        this.tabuleiro.mostrarTabuleiro(true);
        new TelaJogo(this); // Reinicia o jogo com nova instância
    }
}