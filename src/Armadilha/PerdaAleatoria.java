package Armadilha;

import Personagem.Heroi;
import Dungeon.Dungeon;

public class PerdaAleatoria extends Armadilha{
    public PerdaAleatoria(Dungeon tabuleiro) {
        super(0);
        posiciona(tabuleiro);
    }

    @Override
    public void calcularDano(Heroi alvo) {
        System.out.println(alvo.getNome() + " encontrou uma armadilha!");
        this.setDano((int) (Math.random() * 10));
        alvo.sofrerDano(this.getDano());
    }

    @Override
    public void posiciona(Dungeon tabuleiro){
        int x = (int)(Math.random()*5);
        int y = (int)(Math.random()*10);

        if(!tabuleiro.verificaPosicaoValida(x, y)){
            posiciona(tabuleiro);
        }
        else{
            this.setPosicao(x, y);
            tabuleiro.posicionaArmadilhaAleatoria(this);
        }
    }
}
