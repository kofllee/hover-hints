Hover Hints is a lightweight utility mod that shows small contextual hints for hidden vanilla Minecraft mechanics.

It gives useful information exactly when you need it, while you are holding a relevant item, looking at a matching block or entity, and intentionally asking for a hint.

![Hover Hints preview](https://cdn.modrinth.com/data/cached_images/2d93e8366e1c5e1298085245cf857c5385bbedbd.gif)

Hover Hints does not add new gameplay mechanics or change vanilla balance. It only reveals information that already exists in Minecraft.

## How it works

By default, hints are shown while holding `Left Alt`.

For example, if you hold a compostable item and look at a composter, Hover Hints shows the chance that the item will raise the compost level. If you hold fuel and look at a furnace, it shows how long that fuel will burn. If you hold bone meal and look at a plant, it shows what effect bone meal can have there.

## Examples

### Composting
![Composting Example](https://cdn.modrinth.com/data/cached_images/b4b9089e0bdd90023f361e5869bef0768028a0ca.png)

### Fuel burn time
![Fuel Example](https://cdn.modrinth.com/data/cached_images/c25f635f5cd8b7d01e0eb21c0ae5d9b534341c53.png)

### Bone meal behavior
![Bone Meal Example](https://cdn.modrinth.com/data/cached_images/120dfcc02317809e33ea1ea98560f212aa32e9d1.png)

### Entity hints
![Entity Hints Example](https://cdn.modrinth.com/data/cached_images/95578fb26f43f636d4d9ce025b2de98d2b6013a5.png)

## Available hints

Hover Hints works fully client-side for mechanics that are already known to the client. Some hints use server-only data, such as loot tables or villager POI ownership, and require Hover Hints to be installed on the server to show accurate results.

| Hint | What it shows | Server installation required |
|---|---|------------------------------|
| Composting | Compost chance for the held item when looking at a composter | No                           |
| Fuel Burn Time | Burn time for the held item when looking at a furnace, smoker, or blast furnace | No                           |
| Grindstone XP | Estimated XP from removing enchantments with a grindstone | No                           |
| Bone Meal | Growth effect, chance, stage increase, spreading, or item drop behavior | No                           |
| Taming Chance | Taming or trust chance for tameable animals | No                           |
| Redstone Power | Current redstone power level of the targeted block | No                           |
| Silk Touch Requirement | Whether the targeted block needs Silk Touch to drop itself | No                           |
| Animal Feeding | Breeding state, baby growth time, love mode timer, and breeding cooldown | Yes, for exact timers        |
| Mob Loot | Possible drops from the targeted mob | Yes                          |
| Archaeology Loot | Possible loot from suspicious sand or suspicious gravel | Yes                          |
| Villager POI | Whether a villager workstation, bed, or bell is occupied | Yes                          |

## Configuration

The config screen is available through ModMenu or the `/hoverhints config` command. Cloth Config is required.

![Config Menu](https://cdn.modrinth.com/data/cached_images/85c3a4b03176bec671c22e3d0d4ca2a8f7ecb0bb.png)