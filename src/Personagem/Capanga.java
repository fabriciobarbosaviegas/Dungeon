package Personagem;
import Dungeon.Dungeon;

public class Capanga extends Monstro {
    public Capanga(String nome, Dungeon tabuleiro) {
        super(
            nome,
            (int) (Math.random()*(tabuleiro.getPlayer().getAtaque() - 5) + 5),
            (int) (Math.random()*(tabuleiro.getPlayer().getDefensa() - 5) + 5),
            (int) (Math.random()*(tabuleiro.getPlayer().getSaude() - 5) + 5)
        );
        posiciona(tabuleiro);
    }

    @Override
    public void posiciona(Dungeon tabuleiro){
        int x = (int) (Math.random() * 5);
        int y = (int) (Math.random() * 10);

        if(!tabuleiro.verificaPosicaoValida(x, y) || tabuleiro.verificaCapangas(x)){
            posiciona(tabuleiro);
        }
        else{
            this.setPosicao(x, y);
            tabuleiro.posicionaCapanga(this);
        }
    }
}
