package com.github.nickrogg;

public class AttackResults 
{
    public boolean hit;
    public boolean crit;
    public int hits;
    public int crits;
    public int[] rolls;
    public int hpModifier;
    public int critModifier;
    public Condition condition;
    public Combatant target;
    public boolean resisted;

    public AttackResults(Combatant target, boolean hit, int hpModifier, int hits, int[] rolls)
    {
        this.target = target;
        this.hit = hit;
        this.hpModifier = hpModifier;
        this.hits = hits;
        this.rolls = rolls;
    }
}
