package com.github.nickrogg;

public class EncounterData 
{
    public int turns;
    public int attacks;
    public int hits;
    public int crits;
    public int misses;

    public int playerDamageDealt;
    public int monsterDamageDealt;
    public int criticalDamageDealt;

    public String winner;

    public EncounterData()
    {
        turns = 0;
        attacks = 0;
        hits = 0;
        crits = 0;
        misses = 0;
        playerDamageDealt = 0;
        monsterDamageDealt = 0;
        criticalDamageDealt = 0;
        winner = "Draw";
    }

    public void recordAttack(AttackResults result, boolean playerAttacking)
    {
        attacks++;
        criticalDamageDealt += result.critModifier;

        if (result.crit)
        {
            hits++;
            crits++;
        }
        else if (result.hit)
        {
            hits++;
        }
        else
        {
            misses++;
        }

        if (playerAttacking)
        {
            playerDamageDealt += result.hpModifier;
        }
        else
        {
            monsterDamageDealt += result.hpModifier;
        }
    }
}
