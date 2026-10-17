package com.github.nickrogg;

public class Combatant 
{
    private String name;
    public int strength;
    public int finesse;
    public int constitution;
    public int intellect;
    public int cunning;
    public int charisma;

    public int maxHp;
    public int hp;
    public int ac;
    public int speed;
    public int currentMovement;
    public int actions;
    public int freeActions;
    public int reactions;
    public int innateMana;
    public int naturalRange;
    public int classDC;

    public Combatant(String name, int hp, int ac) 
    {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.ac = ac;
    }

    public boolean isAlive() 
    {
        return hp > 0;
    }

    public void modifyHp(int amount) 
    {
        // so healing is represented with negative numbers in this function, can't go below 0, can't go above maxHp
        hp = Math.min(Math.max(0, hp - amount), maxHp);
    }

    public int modifyCurrentMovement(int movement)
    {
        currentMovement = Math.max(0, currentMovement + movement);
        return currentMovement;
    }

    public String getName() 
    {
        return name;
    }
}
