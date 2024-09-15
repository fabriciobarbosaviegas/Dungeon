package Personagem;
import Dungeon.Dungeon;

public class Chefao extends Monstro {
    public Chefao(String nome, Dungeon tabuleiro) {
        super(
                nome,
                (int) (tabuleiro.getPlayer().getAtaque() * 1.5),
                (int) (tabuleiro.getPlayer().getDefensa() * 1.5),
                (int) (tabuleiro.getPlayer().getSaude() * 1.5)
        );
        posiciona(tabuleiro);
    }

    @Override
    public void posiciona(Dungeon tabuleiro){
        int x = 4;
        int y = (int) (Math.random() * 10);
        if(!tabuleiro.verificaPosicaoValida(x, y)){
            posiciona(tabuleiro);
        }
        else{
            this.setPosicao(x, y);
            tabuleiro.posicionaChefao(this);
        }
    }
}
