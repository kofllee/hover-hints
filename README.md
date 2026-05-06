# Hover Hints

Hover Hints is a lightweight utility mod that shows small contextual hints for hidden vanilla Minecraft mechanics.

It gives useful information at the moment you need it: when you are holding a relevant item, looking at a matching block or entity, and intentionally asking for a hint.

![Hover Hints preview](assets/readme/hover_hints_preview.gif)

Hover Hints does not add new gameplay mechanics or change vanilla balance. It only reveals information that already exists in Minecraft, such as composting chances, fuel burn time, growth behavior, mob drops, taming chances, and other small details that are usually hidden from the player.

## How it works

By default, hints are shown while holding `Left Alt`.

For example, if you hold a compostable item and look at a composter, Hover Hints shows the chance that the item will raise the compost level. If you hold fuel and look at a furnace, it shows how long that fuel will burn. If you hold bone meal and look at a plant, it shows what effect bone meal can have there.

The goal is to keep the information small and readable. Hints are usually one line, use vanilla-style tooltip rendering, and only appear when the current item and target make the information useful.

## Examples

### Composting

See the composting chance before using an item.

![Composting example](assets/readme/composting.gif)

### Fuel burn time

Check how long an item will burn when looking at a furnace-like block.

![Fuel example](assets/readme/fuel.gif)

### Bone meal behavior

See growth chances, stage changes, spreading behavior, or other vanilla bone meal effects depending on the targeted block.

![Bone meal example](assets/readme/bone_meal.gif)

### Entity hints

Hover Hints can also show useful information for entities, such as taming chance, breeding state, baby growth time, and possible mob drops.

![Entity hints example](assets/readme/entity_hints.gif)

## Available hints

Hover Hints works fully client-side for mechanics that are already known to the client. Some hints use server-only data, such as loot tables or villager POI ownership, and require Hover Hints to be installed on the server to show accurate results.

| Hint | What it shows | Server required |
|---|---|---|
| Composting | Compost chance for the held item when looking at a composter | No |
| Fuel Burn Time | Burn time for the held item when looking at a furnace, smoker, or blast furnace | No |
| Grindstone XP | Estimated XP from removing enchantments with a grindstone | No |
| Bone Meal | Growth effect, chance, stage increase, spreading, or item drop behavior | No |
| Taming Chance | Taming or trust chance for tameable animals | No |
| Redstone Power | Current redstone power level of the targeted block | No |
| Silk Touch Requirement | Whether the targeted block needs Silk Touch to drop itself | No |
| Animal Feeding | Breeding state, baby growth time, love mode timer, and breeding cooldown | Yes, for exact timers |
| Mob Loot | Possible drops from the targeted mob | Yes |
| Archaeology Loot | Possible loot from suspicious sand or suspicious gravel | Yes |
| Villager POI | Whether a villager workstation, bed, or bell is occupied | Yes |

## Configuration

The config screen is available through ModMenu or with the `/hoverhints config` command.

![Config example](assets/readme/config.png)