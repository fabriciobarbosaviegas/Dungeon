package Elixir;

import Dungeon.Dungeon;
import Prop.Prop;

public class Elixir extends Prop{
    private int recuperacao;

    public Elixir(int recuperacao, Dungeon tabuleiro){
        this.setRecuperacao(recuperacao);
        this.posiciona(tabuleiro);
    }

    public void setRecuperacao(int recuperacao){
        this.recuperacao = recuperacao;
    }

    public int getRecuperacao(){
        return this.recuperacao;
    }

    public void posiciona(Dungeon tabuleiro){
        int x = (int)(Math.random()*5);
        int y = (int)(Math.random()*10);

        if(!tabuleiro.verificaPosicaoValida(x, y)){
            posiciona(tabuleiro);
        }
        else{
            this.setPosicao(x, y);
            tabuleiro.posicionaElixir(this);
        }
    }
}
