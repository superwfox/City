# City 中文说明

[English README](README_EN.md)

## 项目概要
City 是一个面向 `Mohist/Paper 1.20.1` 的城市生存插件，核心思路是将城市玩法拆成两张世界并自动轮换：
- `Template-World`：模板世界（长期保留）
- `City-World`：每日重置世界（玩法主战场）

插件会在每天 `04:00` 自动执行重置流程：先把安全区区块写回模板世界，再重建 `City-World`，用于实现“每日刷新 + 重点区域保留”。

## 主要功能与结构
### 主要功能
1. 世界生命周期管理
- 启动时自动检查并创建模板世界、城市世界。
- 每日 `04:00` 自动重置城市世界。
- 支持 `/city reload` 手动重置并重载数据。

2. 安全区保留
- `/city save` 将当前区块加入安全区。
- `/city cancel` 将当前区块移出安全区。
- 重置前会把安全区区块从 `City-World` 复制回 `Template-World`。

3. 奖励潜影盒系统
- 奖励按潜影盒类型区分（如 `WHITE_SHULKER_BOX`）。
- `/city rewards [page]` 打开奖励编辑 GUI（54 格分页）。
- 玩家在 `City-World` 开启潜影盒时，按配置概率随机填充前 27 格奖励。
- 每位玩家对同一位置的奖励盒仅会生效一次（直到 `/city reload` 清空记录）。

4. 世界传送逻辑
- 从非城市世界经过传送门会被传送到 `City-World` 的随机安全区中心。
- 从城市世界经过传送门会返回主世界出生点。
- `/city check` 会按坐标映射进入模板世界。
- `/city back` 返回上一次 `/city check` 前的位置。

5. 保护与限制
- 非 OP 在 `City-World` 不能破坏安全区区块。
- `denyBlocks` 列表内方块在与模板世界同坐标同类型时不可破坏。
- `City-World` 中安全区内禁止生物生成。
- `City-World` 中右键床会被拦截，避免设置重生点。

### 代码结构
```text
src/main/java/sudark2/Sudark/city
├─ City.java                          # 插件入口，注册命令与监听
├─ Clock.java                         # 每分钟检查一次 04:00 重置
├─ FileManager.java                   # 插件目录与数据加载入口
├─ command/
│  ├─ CityCommand.java                # /city 子命令处理
│  └─ CommandTabCompleter.java        # 命令补全
├─ World/
│  ├─ WorldManager.java               # 世界检查、重置、坐标映射
│  ├─ WorldGenerator.java             # 世界创建与 region/entities 复制
│  ├─ SecureZone.java                 # 安全区区块复制
│  └─ WorldProtectListener.java       # 区块保护、生成限制、床交互限制
├─ Portal/
│  └─ PortalManager.java              # 传送门重定向与模板世界 ActionBar
├─ Rewards/
│  ├─ RewardsManager.java             # 奖励 GUI 与列表输出
│  └─ RewardsListener.java            # 奖励保存、开箱填充、开箱记录
├─ File/
│  ├─ SaveZoneRelatedFles.java        # 安全区二进制读写
│  ├─ RewardsRelatedFiles.java        # 奖励序列化与配置读写
│  └─ DenyRelatedFiles.java           # 禁止破坏方块列表读写
└─ Util/
   ├─ ChunkUtil.java                  # 区块坐标编码/解码
   └─ MethodUtil.java                 # 奖励 yml 遍历工具
```

## 使用方法（含代码示例）
### 1. 构建与安装
```bash
mvn clean package
```
将构建产物放入服务端 `plugins/` 后启动服务端。

### 2. 首次配置流程（推荐）
1. 启动服务端，让插件自动创建 `Template-World`、`City-World` 与 `plugins/City/` 数据目录。
2. 在需要长期保留的城市区块中执行：
```mcfunction
/city save
```
3. 手持目标潜影盒（例如白色潜影盒）后执行：
```mcfunction
/city rewards 0
```
在 GUI 内放入奖励物品，关闭界面自动保存。
4. 如需配置禁止破坏方块：
```mcfunction
/city denyBlock add DIAMOND_BLOCK
```
5. 完成后执行一次热重载：
```mcfunction
/city reload
```

### 3. 命令列表
| 命令 | 说明 |
|---|---|
| `/city save` | 将当前区块加入安全区 |
| `/city cancel` | 将当前区块移出安全区 |
| `/city rewards [page]` | 用“手持潜影盒类型”打开对应奖励池页面 |
| `/city list` | 输出所有奖励池内容 |
| `/city check` | 按坐标映射传送到模板世界 |
| `/city back` | 返回上一次 `/city check` 前的位置 |
| `/city denyBlock add <MATERIAL>` | 添加禁止破坏方块 |
| `/city denyBlock remove <MATERIAL>` | 移除禁止破坏方块 |
| `/city denyBlockList` | 查看禁止破坏方块列表 |
| `/city reload` | 手动重置世界并重载文件，同时清空开箱记录 |

命令方块额外支持：
```mcfunction
/city tp <x> <y> <z>
```
会将命令方块附近 6 格玩家传送到 `City-World` 对应坐标。

### 4. 配置与数据文件
`plugins/City/config.yml` 的结构按奖励类型生成，示例：
```yaml
WHITE_SHULKER_BOX:
  奖励箱概率:
    概率值: 500
    类型: "正整数 [1-1000]"
    作用: "控制奖励箱每个槽位有多大概率刷出物品"
    计算公式: "概率值 / 1000"
```

主要数据文件：
- `plugins/City/saveZone.data`：安全区区块二进制数据（`long` 列表）。
- `plugins/City/denyBlocks.txt`：禁止破坏方块清单（每行一个 `Material`）。
- `plugins/City/rewardsList/*.yml`：每种潜影盒奖励池（Base64 序列化）。

### 5. 权限
- 权限节点：`city.admin`
- 默认：`op`
