# City – English Documentation

A daily-reset city survival core plugin specifically written and thoroughly tested for **Mohist 1.20.1**  
(This version has only been fully tested on Mohist 1.20.1 for three consecutive days under real load. Compatibility with other versions is not guaranteed.)

## Feature Details

### 1. Daily Automatic Reset
- Every day at **04:00 real time**, the plugin automatically deletes and rebuilds **City-World** with hot-reload — no server restart required.
- Before resetting, all protected safe-zone chunks are automatically written back to the template world to preserve buildings.
- Players online during the reset will be temporarily kicked and can rejoin immediately once the process finishes.

### 2. Safe-Zone System (Chunk Protection)
- Stand in the desired chunk and run **/city save** → the entire 16×16 chunk is marked as a safe zone.
- Run **/city cancel** in a protected chunk to remove protection.
- On every reset, all safe-zone chunks are copied back to the template world and will persist after the next rebuild.
- Coordinates are stored in `saveZone.txt` (one line per chunk: `chunkX,chunkZ`).

### 3. Reward Chest System
- Admins point at any block and run **/city add** → the block becomes a registered reward chest.
- **/city remove** while looking at a registered chest deletes it.
- Chests are automatically placed when their chunk loads.
- Each player may open every reward chest only once (records persist until manually cleared with **/city reload**).
- Contents are randomly filled from the global reward pool according to the probability set in config.yml (27 slots).
- All chest locations are saved in `chestLocs.txt`.

### 4. Global Reward Pool GUI
- **/city rewards** [page] opens a 54-slot editor (paginated).
- Drag items in or modify existing ones; changes are saved automatically when the inventory is closed.
- Unlimited items are supported — additional pages are created automatically.
- The pool is serialized as Base64 in `rewards.txt`.

### 5. Entity Limit in City Dimension
- The value `城市维度.刷怪上限` in config.yml caps the total number of living entities in City-World (natural spawns + spawner mobs).
- Any new entity spawn exceeding the limit is cancelled to prevent lag from entity buildup.

### 6. Dimension Travel Logic
- Entering from the overworld via Nether portal → teleports to the center-top of a random safe-zone chunk (or 0,0 if none exist).
- Leaving City-World → returns to the exact coordinates used before entering (fallback to bed spawn or world spawn).
- **/city check** manually enters the city dimension using template coordinate mapping.
- **/city back** instantly returns to the last pre-entry location.

### 7. Additional Commands
- **/city allchest** → lists every registered reward chest with clickable teleport links.
- **/city reload** → manually triggers a full world reset, reloads all config/files, and clears all “already opened” chest records.

## Configuration (plugins/City/config.yml)
```yaml
奖励箱概率.概率值: 500     # 1-1000 (higher = higher chance per slot; 500 ≈ 50%)
城市维度.刷怪上限: 500     # Maximum entities in City-World (recommended 300-800)
```

## Data Files
**In the plugin folder:**
- `saveZone.txt` → safe-zone chunk coordinates
- `chestLocs.txt` → reward chest positions (format: chunkX,chunkZ,relativeX,Y,Z)
- `rewards.txt` → Base64-encoded reward pool

**In the server root:**
- `Template-World` → permanent void-flat template (do not delete)
- `City-World` → the active city dimension (rebuilt daily)

## Permissions
All `/city` subcommands are OP-only by default (controlled via plugin.yml).

## Tested Environment
- Mohist 1.20.1 (latest official build `mohist-1.20.1-6eb4f67`)
- Clean environment, 72 consecutive resets over 3 days, zero crashes or data loss
- Peak concurrent players: 60

For version adaptation or new features, please contact the author **@SudarkX**.

Thank you for using City!
