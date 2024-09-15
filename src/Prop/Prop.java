package Prop;

import Dungeon.Dungeon;

abstract public class Prop {
    int[] posicao;

    public Prop(){
        posicao = new int[2];
    }
    public void setPosicao(int x, int y){
        this.posicao[0] = x;
        this.posicao[1] = y;
    }
    public int[] getPosicao(){
        return this.posicao;
    }
    abstract public void posiciona(Dungeon tabuleiro);
}
