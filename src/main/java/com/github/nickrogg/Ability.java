package com.github.nickrogg;

import java.util.Map;

public class Ability
{
    public enum ResistanceType 
    {
        NONE,
        STRENGTH,
        FINESSE,
        CONSTITUTION,
        INTELLECT,
        CUNNING,
        CHARISMA
    }

    public enum DamageType 
    {
        BLUDGEONING,
        PIERCING,
        SLASHING,
        ARCANE,
        FORCE,
        FIRE,
        LIGHTNING,
        TRUE,
        POISON
    }

    public String name;
    public int range;
    public int power;
    public int takeFlatDamage;
    public ResistanceType resistanceType;
    public DamageType damageType;
    public int DC;
    public int limit;
    public int actionType;
    public int actionAmount;
    public int advantage;
    public int disadvantage;
    public Map<Integer, Integer> dicePool;

    public Ability(String name, int range, int power, ResistanceType resistanceType, int advantage, int DC, Map<Integer, Integer> dicePool) 
    {
        this.name = name;
        this.range = range;
        this.power = power;
        this.resistanceType = resistanceType;
        this.advantage = advantage;
        this.DC = DC;
        this.dicePool = dicePool;
        this.damageType = DamageType.SLASHING;
    }
}