package com.github.nickrogg;

public class Player extends Combatant
{
    public int lvl;
    public String race;
    public String characterClass;
    public String archetype;

    public Player(String name, int hp, int ac)
    {
        // super means to use the parent's version of something, in this case, the constructor
        super(name, hp, ac);
    }
}
