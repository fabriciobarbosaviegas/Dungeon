package Armadilha;
import Personagem.Heroi;
import Dungeon.Dungeon;

public class PerdaFixa extends Armadilha{
    public PerdaFixa(Dungeon tabuleiro, int dano) {
        super(dano);
        posiciona(tabuleiro);
    }

    @Override
    public void calcularDano(Heroi alvo){
        System.out.println(alvo.getNome() + " encontrou uma armadilha!");
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
            tabuleiro.posicionaArmadilhaFixa(this);
        }
    }
}
