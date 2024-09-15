package Personagem;

import Dungeon.Dungeon;

abstract public class Monstro extends Personagem{
    public Monstro(String nome, int ataque, int defensa, int saude) {
        super(nome, ataque, defensa, saude);
    }

    @Override
    abstract public void posiciona(Dungeon tabuleiro);

}
