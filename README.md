![issue resolution time](https://isitmaintained.com/badge/resolution/Derkades/Minigames.svg)
![open issue percentage](https://isitmaintained.com/badge/open/Derkades/Minigames.svg)

# Writing a minigame documentation (OUTDATED)

## Cloning the project
1. Clone the project
2. Open eclipse
3. Right click in package manager or project manager, click import
4. Find Maven > Existing maven project
5. Select the parent directory of the repository (on windows by default `~/Documents/GitHub/`)
6. Select the minigames repo
7. Click next and stuff

## Creating a new minigame
1. Create a new class in xyz.derkades.minigames.games
2. Extend `Game` or `ParkourGame`
3. Insert the required constructor and methods
4. Code your game

When your game has ended call `startNextGame(List<Player> winners)`
You can add listeners to this class, they'll be registered automatically when the game starts and unregistered when it ends.

5. Add your game to the array in xyz.derkades.minigames.games.Game

## Minigame examples
[Creeper attack](https://github.com/Derkades/Minigames/blob/master/src/xyz/derkades/minigames/games/CreeperAttack.java)
[Jungle run](https://github.com/Derkades/Minigames/blob/master/src/xyz/derkades/minigames/games/JungleRun.java)

## Useful methods and variables
`Var.WORLD`

`Var.LOBBY_LOCATION` (Note: you don't need to teleport players back to the lobby when the game ends!)

`Minigames.setCanTakeDamage(Player, true|false)`

`Utils.giveInvisibility(Player)` - Give infinite invisiblity without particles

`Utils.giveInfiniteEffect(Player, PotionEffectType, int amplifier)` - Give infinite effect without particles (amplifier is optional)

`Utils.clearInventory(player)` - Clears the player inventory including armor slots

`Utils.setArmor(Player, helmet, chestplate, leggings, boots)`

`boolean allPlayersWon(List<UUID> winners)` - True all online players are in the winners list

`getAliveCountFromDeadList(List<UUID> dead)` - Get the number of players that are still alive

`List<Player> getWinnersFromPointsHashmap(Map<UUID, Integer> points)`

`List<Player> getPlayerListFromUUIDList(List<UUID>)`

`playSoundForAllPlayers(Sound, float pitch)`

`setGameRule(String gamerule, boolean)` (please set the gamerule back at the end of the game)

`clearPotionEffects(Player player)`
