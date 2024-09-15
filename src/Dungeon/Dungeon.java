package Dungeon;

import Prop.Prop;
import Elixir.Elixir;
import Personagem.*;
import Armadilha.*;
import Utils.SoundPlayer;
import java.util.Stack;

public class Dungeon {
    private final Prop[][] tabuleiro;
    private final Stack<Prop> removidos;
    private final SoundPlayer soundEffect;
    private final Heroi player;
    private final Chefao chefao;

    public Dungeon(Heroi player, int armadilhasFixas, int armadilhasAleatorias, int capangas, int elixires){
        this.tabuleiro = new Prop[5][10];
        this.removidos = new Stack<>();
        this.player = player;
        this.soundEffect = new SoundPlayer();

        posicionaHeroi();

        for(int i = 0; i < elixires; i++){
            new Elixir(10, this);
        }

        for(int i = 0; i < armadilhasFixas; i++){
            new PerdaFixa(this, 5);
        }

        for(int i = 0; i < armadilhasAleatorias; i++){
            new PerdaAleatoria(this);
        }

        for(int i = 0; i < capangas; i++){
            new Capanga("Capanga"+i, this);
        }

        this.chefao = new Chefao(
                "Marilton",
                this
        );
        posicionaChefao(this.chefao);
    }

    public Heroi getPlayer(){
        return this.player;
    }

    public Prop getConteudoPosicao(int x, int y){
        return this.tabuleiro[x][y];
    }

    public void posicionaHeroi(){
        int x = this.player.getPosicao()[0];
        int y = this.player.getPosicao()[1];

        if(this.verificaPosicaoValida(x, y)) {
            this.tabuleiro[x][y] = this.player;
        }
        else{
            this.player.posiciona(this);
            this.posicionaHeroi();
        }
    }

    public void posicionaArmadilhaFixa(PerdaFixa armadilha){
        int x = armadilha.getPosicao()[0];
        int y = armadilha.getPosicao()[1];

        this.tabuleiro[x][y] = armadilha;
    }

    public void posicionaArmadilhaAleatoria(PerdaAleatoria armadilha){
        int x = armadilha.getPosicao()[0];
        int y = armadilha.getPosicao()[1];

        this.tabuleiro[x][y] = armadilha;
    }

    public void posicionaElixir(Elixir elixir){
        int x = elixir.getPosicao()[0];
        int y = elixir.getPosicao()[1];

        this.tabuleiro[x][y] = elixir;
    }

    public void posicionaCapanga(Capanga capanga){
        this.tabuleiro[capanga.getPosicao()[0]][capanga.getPosicao()[1]] = capanga;
    }

    public void posicionaChefao(Chefao chefao){
        this.tabuleiro[chefao.getPosicao()[0]][chefao.getPosicao()[1]] = chefao;
    }

    public boolean verificaPosicaoVazia(int x, int y){
        return this.tabuleiro[x][y] == null;
    }

    public boolean verificaPosicaoValida(int x, int y){
        if(x < 5 && y < 10 && x >= 0 && y >= 0){
            return this.verificaPosicaoVazia(x, y);
        }
        
        return false;
    }

    public boolean verificaCapangas(int x){
        for(Prop i: tabuleiro[x]){
            if(i instanceof Capanga) return true;
        }
        return false;
    }

    public boolean verificaBatalha(int x, int y){
        return this.getConteudoPosicao(x, y) instanceof Monstro;
    }

    public void verificaArmadilha(int x, int y){
        if(this.getConteudoPosicao(x, y) instanceof Armadilha armadilha){
            soundEffect.playSound("/resources/sound/armadilha.wav",false);
            armadilha.calcularDano(this.player);
            this.removerProp(this.getConteudoPosicao(x, y));
        }
    }

    public void verificaElixir(int x, int y){
        if(this.getConteudoPosicao(x, y) instanceof Elixir){
            soundEffect.playSound("/resources/sound/elixir-found.wav",false);
            this.player.pegarElixir((Elixir) this.getConteudoPosicao(x, y));
            this.removerProp(this.getConteudoPosicao(x, y));
        }
    }

    public int verificaArmadilhas(int x){
        int total = 0;
        for(Prop i: tabuleiro[x]){
            if(i instanceof Armadilha){
                total++;
            }
        }
        return total;
    }

    public void mostrarTabuleiro(boolean debug){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 10; j++) {
                Prop posicao = this.getConteudoPosicao(i, j);
                if (!debug) {
                    System.out.print(posicao instanceof Heroi || posicao instanceof Chefao ? tabuleiro[i][j].getClass() + "\t" : "*\t");
                }
                else{
                    System.out.print(tabuleiro[i][j] != null ? tabuleiro[i][j].getClass() + "\t" : "null\t");
                }
            }
            System.out.println();
        }
    }

    public void removerProp(Prop prop){
        if(prop != null){
            this.removidos.push(prop);
            this.tabuleiro[prop.getPosicao()[0]][prop.getPosicao()[1]] = null;
        }
    }

    public void removerHeroi(){
        int x = this.player.getPosicao()[0];
        int y = this.player.getPosicao()[1];
        this.tabuleiro[x][y] = null;
    }

    public void reiniciarTabuleiro(){
        System.out.println(this.removidos.toString());
        Prop aux;
        this.removerHeroi();
        this.player.setPosicao(this.player.getPosicaoInicial()[0], this.player.getPosicaoInicial()[1]);

        while(!this.removidos.empty()){
            aux = this.removidos.pop();
            if(aux instanceof Monstro){
                ((Monstro) aux).setHp(((Monstro) aux).getMaxHp());
            }
            this.tabuleiro[aux.getPosicao()[0]][aux.getPosicao()[1]] = aux;
        }

        this.posicionaChefao(this.chefao);
        this.tabuleiro[this.player.getPosicaoInicial()[0]][this.player.getPosicaoInicial()[1]] = player;
        System.out.println("Ini X:"+this.player.getPosicaoInicial()[0]+", Ini Y:"+this.player.getPosicaoInicial()[1]+"\nX: "+this.player.getPosicao()[0]+", Y: "+this.player.getPosicao()[1]);
    }
}