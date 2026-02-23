# City English README

[中文说明](README.md)

## Project Overview
City is a city-survival plugin for `Mohist/Paper 1.20.1`. It uses a dual-world lifecycle:
- `Template-World`: persistent template world
- `City-World`: gameplay world rebuilt every day

At `04:00` every day, the plugin runs a reset pipeline: copy protected chunks back to the template world, then rebuild `City-World`.

## Main Features And Structure
### Main Features
1. World lifecycle
- Auto-check and create required worlds at startup.
- Auto reset `City-World` every day at `04:00`.
- `/city reload` supports manual reset and data reload.

2. Safe-zone persistence
- `/city save` adds the current chunk to safe zones.
- `/city cancel` removes the current chunk from safe zones.
- Safe-zone chunks are copied from `City-World` to `Template-World` before reset.

3. Shulker-box reward system
- Rewards are separated by shulker box type (for example `WHITE_SHULKER_BOX`).
- `/city rewards [page]` opens the 54-slot paged editor.
- Opening shulker boxes in `City-World` fills the first 27 slots by configured probability.
- Each player triggers rewards once per chest location until `/city reload` clears records.

4. Teleport flow
- Entering a portal from non-city worlds sends entities to a random safe chunk center in `City-World`.
- Entering a portal from `City-World` sends entities to main-world spawn.
- `/city check` maps current coordinates into template world.
- `/city back` returns to the location saved by `/city check`.

5. Protection and limits
- Non-OP players cannot break blocks in safe chunks of `City-World`.
- Blocks listed in `denyBlocks` are protected when template and city block types match at the same coordinates.
- Creature spawns are blocked inside safe chunks.
- Bed interaction in `City-World` is blocked.

### Code Structure
```text
src/main/java/sudark2/Sudark/city
├─ City.java
├─ Clock.java
├─ FileManager.java
├─ command/
│  ├─ CityCommand.java
│  └─ CommandTabCompleter.java
├─ World/
│  ├─ WorldManager.java
│  ├─ WorldGenerator.java
│  ├─ SecureZone.java
│  └─ WorldProtectListener.java
├─ Portal/
│  └─ PortalManager.java
├─ Rewards/
│  ├─ RewardsManager.java
│  └─ RewardsListener.java
├─ File/
│  ├─ SaveZoneRelatedFles.java
│  ├─ RewardsRelatedFiles.java
│  └─ DenyRelatedFiles.java
└─ Util/
   ├─ ChunkUtil.java
   └─ MethodUtil.java
```

## Usage With Examples
### 1. Build and install
```bash
mvn clean package
```
Put the built jar into `plugins/` and start the server.

### 2. Recommended first setup
1. Start the server once so the plugin creates `Template-World`, `City-World`, and `plugins/City/`.
2. In chunks you want to preserve:
```mcfunction
/city save
```
3. Hold the target shulker box and edit rewards:
```mcfunction
/city rewards 0
```
4. Add deny-break blocks if needed:
```mcfunction
/city denyBlock add DIAMOND_BLOCK
```
5. Reload data once:
```mcfunction
/city reload
```

### 3. Commands
| Command | Description |
|---|---|
| `/city save` | Add current chunk to safe zones |
| `/city cancel` | Remove current chunk from safe zones |
| `/city rewards [page]` | Open rewards page for the shulker type in hand |
| `/city list` | Print all reward pool entries |
| `/city check` | Teleport to mapped coordinates in template world |
| `/city back` | Return to position saved by `/city check` |
| `/city denyBlock add <MATERIAL>` | Add a protected block type |
| `/city denyBlock remove <MATERIAL>` | Remove a protected block type |
| `/city denyBlockList` | Show protected block list |
| `/city reload` | Reset world, reload files, clear opened-chest records |

Extra command-block usage:
```mcfunction
/city tp <x> <y> <z>
```
Teleports nearby players (radius 6) to those coordinates in `City-World`.

### 4. Config and data files
`plugins/City/config.yml` is generated per reward type. Example:
```yaml
WHITE_SHULKER_BOX:
  奖励箱概率:
    概率值: 500
    类型: "正整数 [1-1000]"
    作用: "控制奖励箱每个槽位有多大概率刷出物品"
    计算公式: "概率值 / 1000"
```

Main data files:
- `plugins/City/saveZone.data`: safe-zone chunk keys (`long` list).
- `plugins/City/denyBlocks.txt`: deny-break materials (one `Material` per line).
- `plugins/City/rewardsList/*.yml`: reward pools per shulker type (Base64 serialized).

### 5. Permission
- Node: `city.admin`
- Default: `op`
