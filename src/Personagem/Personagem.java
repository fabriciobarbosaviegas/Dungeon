package Personagem;

import Dungeon.Dungeon;
import Prop.Prop;
import Utils.SoundPlayer;

abstract public class Personagem extends Prop{
    private String nome;
    private int ataque;
    private int defensa;
    private int saude;
    private int hp;
    protected SoundPlayer soundPlayer;

    public Personagem(String nome, int ataque, int defensa, int saude) {
        this.setNome(nome);
        this.setAtaque(ataque);
        this.setDefensa(defensa);
        this.setSaude(saude);
        this.setHp(this.getMaxHp());
        this.soundPlayer = new SoundPlayer();
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int getAtaque() {
        return ataque;
    }
    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }
    public int getDefensa() {
        return defensa;
    }
    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }
    public int getSaude() {
        return saude;
    }
    public void setSaude(int saude) {
        this.saude = saude;
    }
    public int getHp() {
        return hp;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    public int getMaxHp(){
        return this.getSaude() + 10;
    }

    @Override
    abstract public void posiciona(Dungeon tabuleiro);

    public void sofrerDano(int dano){
        System.out.println(this.nome + " sofreu " + dano + " de dano");
        this.hp -= dano;
    }

    public void recuperarHP(int recuperacao){
        this.soundPlayer.playSound("/resources/sound/cura.wav", false);
        this.setHp(Math.min(this.getHp() + recuperacao, this.getMaxHp()));
    }

    public void ataque(Personagem alvo, double modificador) {
        int valorAtaque = this.ataque + (int) (Math.random() * 10) + (int) (getAtaque()*(modificador/100));
        int valorDefesa = alvo.defende();

        int dano = valorAtaque - valorDefesa;
        if (dano > 0) {
            alvo.sofrerDano(dano);
        } else {
            this.sofrerDano(-dano);
        }
    }

    public int defende() {
        return this.defensa + (int) (Math.random() * 10);
    }
}