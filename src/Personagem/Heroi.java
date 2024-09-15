package Personagem;

import java.util.ArrayList;
import java.util.List;
import Dungeon.Dungeon;
import Elixir.Elixir;

public class Heroi extends Personagem{
    private final List<Elixir> inventario;
    private int[] posicaoInicial;
    private boolean usouHabilidadeEspecial;
    private final int[] contaBonus;

    public Heroi(String nome, int ataque, int defesa, int saude) {
        super(
                nome,
                ataque,
                defesa,
                saude
        );
        this.contaBonus = new int[3];
        this.inventario = new ArrayList<>();
        setPosicaoInicial();
    }

    public void setInventario(Elixir elixir) {
        this.inventario.add(elixir);
    }
    public List <Elixir> getInventario() {
        return this.inventario;
    }
    public void setPosicaoInicial(){
        this.posicaoInicial = new int[2];
        this.posicaoInicial[1] = (int) (Math.random() * 10);
        this.setPosicao(0, this.getPosicaoInicial()[1]);
    }
    public int[] getPosicaoInicial(){
        return this.posicaoInicial;
    }
    public boolean isUsouHabilidadeEspecial(){
        return this.usouHabilidadeEspecial;
    }
    public void setUsouHabilidadeEspecial(){
        this.usouHabilidadeEspecial = !this.isUsouHabilidadeEspecial();
    }

    @Override
    public void posiciona(Dungeon tabuleiro){
        this.setPosicao(0, (int) (Math.random() * 10));
    }

    public void usarElixir(){
        if(!this.inventario.isEmpty()){
            System.out.println("Você usou elixir!");
            this.recuperarHP(this.getInventario().get(0).getRecuperacao());
            this.inventario.remove(0);
        }
        else{
            System.out.println("Você não tem elixir!");
        }
    }

    public void pegarElixir(Elixir elixir){
        this.setInventario(elixir);
    }

    public void habilidadeEspecial(){}
    public void habilidadeEspecial(Monstro alvo){}

    public void vencerBatalha(){
        this.usouHabilidadeEspecial = false;

        int atributo = (int) (Math.random() * 3);
        System.out.println("atributo: " + atributo);
        this.contaBonus[atributo] += 1;
        switch (atributo){
            case 0:
                System.out.println("Você recebeu um bonus de saude");
                this.setSaude(this.getSaude() + 5);
                break;
            case 1:
                System.out.println("Você recebeu um bonus de ataque");
                this.setAtaque(this.getAtaque() + 5);
                break;
            case 2:
                System.out.println("Você recebeu um bonus de defesa");
                this.setDefensa(this.getDefensa() + 5);
                break;
        }
    }

    public void resetarAtributos(){
        this.inventario.removeAll(this.inventario);
        for(int i = 0; i < 3; i++){
            switch (i){
                case 0:
                    this.setSaude(this.getSaude() - 5*contaBonus[i]);
                    this.setHp(this.getMaxHp());
                    break;
                case 1:
                    this.setAtaque(this.getAtaque() - 5*contaBonus[i]);
                    break;
                case 2:
                    this.setDefensa(this.getDefensa() - 5*contaBonus[i]);
                    break;
            }
            this.contaBonus[i] = 0;
        }
    }

    public void status(){
        System.out.println("Inventario: " + this.getInventario());
        System.out.println("Ataque: " + this.getAtaque());
        System.out.println("Defesa: " + this.getDefensa());
        System.out.println("Saude: " + this.getSaude());
        System.out.println("HP: " + this.getHp());
    }
}
