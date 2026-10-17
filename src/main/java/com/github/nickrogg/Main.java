package com.github.nickrogg;

import java.util.List;
import java.util.Map;


public final class Main
{
    public static EncounterData runEncounter(Player player, Monster monster, Ability playerAbility, Ability monsterAbility, CombatEngine engine, boolean verbose)
    {
        EncounterData data = new EncounterData();

        // prevent a broken or stupidly long encounter from running forever
        int maxTurns = 1000;

        while (player.isAlive() && monster.isAlive() && data.turns < maxTurns)
        {
            data.turns++;

            if (verbose)
            {
                System.out.println("\n=== Turn " + data.turns + " ===");
            }

            // player attacks
            List<AttackResults> playerResults = engine.attack(player, new Combatant[]{monster}, playerAbility);

            for (AttackResults result : playerResults)
            {
                data.recordAttack(result, true);

                if (verbose)
                {
                    printAttack(player, playerAbility, result, engine);
                }
            }

            if (!monster.isAlive())
            {
                break;
            }

            // monster attacks
            List<AttackResults> monsterResults = engine.attack(monster, new Combatant[]{player}, monsterAbility);

            for (AttackResults result : monsterResults)
            {
                data.recordAttack(result, false);

                if (verbose)
                {
                    printAttack(monster, monsterAbility, result, engine);
                }
            }
        }

        if (player.isAlive() && !monster.isAlive())
        {
            data.winner = "Player";
        }
        else if (monster.isAlive() && !player.isAlive())
        {
            data.winner = "Monster";
        }
        else
        {
            data.winner = "Draw";
        }

        return data;
    }

    public static void printAttack(Combatant actor, Ability ability, AttackResults result, CombatEngine engine)
    {
        System.out.println(actor.getName() + " uses " + ability.name + " against " + result.target.getName());

        System.out.println("Original Rolls: " + (engine.originalRolls));
        System.out.println("Rolls After Advantage: " + java.util.Arrays.toString(result.rolls));

        System.out.println("Hits: " + result.hits);
        System.out.println("Target AC: " + result.target.ac);

        if (result.hit)
        {
            if (result.crit)
            {
                System.out.println("CRITICAL HIT! Total damage dealt: " + result.hpModifier + "\n" + "Base damage dealt: " + 
                    (result.hpModifier - result.critModifier) + "\n" + "Critical damage dealt: " + result.critModifier);
            }
            else
            {
                System.out.println("Hit! Damage dealt: " + result.hpModifier);
            }

            System.out.println("");
        }
        else
        {
            System.out.println("Miss!");
        }

        System.out.println(result.target.getName() + " HP remaining: " + result.target.hp);
    }

    public static void printEncounterData(EncounterData data)
    {
        System.out.println("Winner: " + data.winner);
        System.out.println("Turns: " + data.turns);
        System.out.println("Attacks: " + data.attacks);
        System.out.println("Hits: " + data.hits);
        System.out.println("Crits: " + data.crits);
        System.out.println("Misses: " + data.misses);

        System.out.println("Player damage dealt: " + data.playerDamageDealt);

        System.out.println("Monster damage dealt: " + data.monsterDamageDealt);

        System.out.println("Bonus critical damage dealt: " + data.criticalDamageDealt);
    }

    public static void main(String[] args) 
    {
        CombatEngine engine = new CombatEngine();

        Ability sword = new Ability("Iron Sword", 1, 2, Ability.ResistanceType.NONE, 2, 0, Map.of(6, 3));
        Ability claws = new Ability("Claws", 1, 3, Ability.ResistanceType.NONE, 2, 0, Map.of(6, 2));

        // print out one example encounter with every attack printed to the console
        Player player = new Player("Player", 20, 1);
        Monster monster = new Monster("Monster", 20, 1);

        EncounterData encounter = runEncounter(player, monster, sword, claws, engine, true);

        System.out.println("\n=== Encounter Results ===");
        printEncounterData(encounter);

        // batch simulation
        int simulations = 1000;
        int playerWins = 0;
        int monsterWins = 0;
        int draws = 0;

        long totalTurns = 0;
        long totalPlayerDamage = 0;
        long totalMonsterDamage = 0;
        long totalCritDamage = 0;
        long totalNumberOfCrits = 0;

        CombatEngine batchEngine = new CombatEngine(12345);

        for (int i = 0; i < simulations; i++)
        {
            Player batchPlayer = new Player("Player", 20, 1);
            Monster batchMonster = new Monster("Monster", 20, 1);

            EncounterData data = runEncounter(batchPlayer, batchMonster, sword, claws, batchEngine, false);

            if (data.winner.equals("Player"))
            {
                playerWins++;
            }
            else if (data.winner.equals("Monster"))
            {
                monsterWins++;
            }
            else
            {
                draws++;
            }

            totalTurns += data.turns;
            totalPlayerDamage += data.playerDamageDealt;
            totalMonsterDamage += data.monsterDamageDealt;
            totalCritDamage += data.criticalDamageDealt;
            totalNumberOfCrits += data.crits;
        }

        System.out.println("\n=== Batch Simulation ===");
        System.out.println("Encounters: " + simulations);

        System.out.printf("Player win rate: %.2f%%%n", 100.0 * playerWins / simulations);

        System.out.printf("Monster win rate: %.2f%%%n", 100.0 * monsterWins / simulations);

        System.out.println("Draws: " + draws);

        System.out.printf("Average turns: %.2f%n", (double) totalTurns / simulations);

        System.out.printf("Average player damage dealt: %.2f%n", (double) totalPlayerDamage / simulations);

        System.out.printf("Average monster damage dealt: %.2f%n", (double) totalMonsterDamage / simulations);

        System.out.printf("Average crit damage dealt: %.2f%n", (double) totalCritDamage / simulations);

        System.out.printf("Average crit chance: %.2f%n", (double) totalNumberOfCrits / simulations);
    }
}
