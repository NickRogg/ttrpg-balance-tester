package com.github.nickrogg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CombatEngine 
{
    private final Random random;
    public List<Integer> originalRolls;

    public CombatEngine()
    {
        random = new Random();
    }

    public CombatEngine(long seed)
    {
        random = new Random(seed);
    }

    public int[] diceRoller(Map<Integer, Integer> dicePool, int advantage, int disadvantage)
    {
        List<int[]> rolls = new ArrayList<>();

        // roll each die type the determined number of times, then store {current roll, die maximum}
        for (Map.Entry<Integer, Integer> entry : dicePool.entrySet())
        {
            int dieMax = entry.getKey();
            int quantity = entry.getValue();

            for (int i = 0; i < quantity; i++)
            {
                rolls.add(new int[] {random.nextInt(dieMax) + 1, dieMax});
            }
        }

        originalRolls = new ArrayList<>();

        for (int[] roll : rolls)
        {
            originalRolls.add(roll[0]);
        }

        int netAdvantage = advantage - disadvantage;

        if (netAdvantage > 0)
        {
            // advantage: increase highest rolls first
            while (netAdvantage > 0)
            {
                rolls.sort((a, b) -> Integer.compare(b[0], a[0]));

                boolean changed = false;

                for (int[] roll : rolls)
                {
                    if (netAdvantage == 0) 
                    {
                        break;
                    }

                    if (roll[0] < roll[1])
                    {
                        roll[0]++;
                        netAdvantage--;
                        changed = true;
                        break; // re-sort
                    }
                }

                if (!changed)
                {
                    break;
                }
            }
        }
        else if (netAdvantage < 0)
        {
            int remainingDisadvantage = -netAdvantage;

            while (remainingDisadvantage > 0)
            {
                // target dice at their maximum, otherwise target highest rolls
                rolls.sort((a, b) -> Integer.compare(b[0], a[0]));

                int[] target = null;

                for (int[] roll : rolls)
                {
                    if (roll[0] == roll[1])
                    {
                        target = roll;
                        break;
                    }
                }

                if (target == null)
                {
                    for (int[] roll : rolls)
                    {
                        if (roll[0] > 1)
                        {
                            target = roll;
                            break;
                        }
                    }
                }

                if (target == null)
                {
                    break;
                }

                target[0]--;
                remainingDisadvantage--;
            }
        }

        // extract results and sort descending
        rolls.sort((a, b) -> Integer.compare(b[0], a[0]));

        return rolls.stream().mapToInt(roll -> roll[0]).toArray();
    }

    public List<AttackResults> attack(Combatant actor, Combatant[] targets, Ability ability)
    {
        List<AttackResults> results = new ArrayList<>();

        for (Combatant target : targets) 
        {
            if (!actor.isAlive()) 
            {
                break;
            }

            if (!target.isAlive())
            {
                continue;
            }

            int[] rolls = diceRoller(ability.dicePool, ability.advantage, ability.disadvantage);

            int hits = 0;
            int crits = 0;
            int critAmount = 0;
            int index = 0;

            for (Map.Entry<Integer, Integer> entry : ability.dicePool.entrySet()) 
            {
                int dieMax = entry.getKey();
                int quantity = entry.getValue();

                for (int i = 0; i < quantity; i++) 
                {
                    if (rolls[index] == dieMax) 
                    {
                        hits++;
                        crits++;
                        critAmount += dieMax;
                    }

                    index++;
                }
            }

            int hpModifer = 0;
            int critModifier = 0;
            boolean hit = false;

            if (hits >= target.ac) 
            {
                hpModifer = ability.power * hits;
                if (hits >= target.ac + 2)
                {
                    critModifier = critAmount;
                    hpModifer += critAmount;
                }

                hit = true;
            }

            if (hit) 
            {
                target.modifyHp(hpModifer);
            }

            AttackResults result = new AttackResults(target, hit, hpModifer, hits, rolls);

            result.critModifier = critModifier;
            result.crit = critModifier > 0;
            result.crits = crits;

            results.add(result);
        }

        return results;
    }
}
