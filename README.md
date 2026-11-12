# 禁止火把 (Interdiction Torch)

> 来自《等价交换》的经典道具，Fabric 26.3 移植版。

放置后会在火把周围形成力场：推开敌对生物、偏向飞来的弹射物，照明等级与原版火把一致（14）。
火把本体完全沿用原版的方块行为与粒子逻辑，只把配色换成了紫黑 + 亮黄，并新增了力场。

![贴图预览](docs/preview.png)

## 特性

- **落地 / 贴墙两种形态**，一个物品同时支持，放置方式与原版火把一致
- **光照等级 14**，与原版火把相同
- **力场**：半径 8 格，推开敌对生物，越靠近推力越大；多根火把的推力矢量叠加
- **弹射物偏向**：靠近火把的箭矢等会被拨开
- **自定义紫色火焰粒子**，形状与运动逻辑照搬原版 `FlameParticle`

> 友好生物和玩家不受力场影响。

## 环境要求

| 项目 | 版本 |
| --- | --- |
| Minecraft | 26.3 |
| Fabric Loader | 0.20.1 或更高 |
| Fabric API | 0.165.2+26.3 或更高 |
| Java | 25 |

## 安装

1. 安装 Fabric Loader（26.3）
2. 把 `Fabric API` 和本 mod 的 jar 一起放进 `mods/` 文件夹
3. 启动游戏

## 合成

```
R D R      R = 红石火把
D   D      D = 钻石
G G G      G = 荧石粉
```

产出 1 个禁止火把。中心留空（原版配方此处为贤者之石）。

## 从源码构建

```powershell
# 需要 JDK 25
$env:JAVA_HOME='C:\Path\temurin25-jdk-25.0.5+8'
.\gradlew build
```

产物位于 `build/libs/InterdictionTorch-<version>.jar`。

开发调试：

```powershell
.\gradlew runClient
```

## 项目结构

```
src/main/java/com/ribu/interdictiontorch/
├── InterdictionTorch.java            主入口
├── ModBlocks.java                    方块注册
├── ModBlockEntities.java             方块实体注册
├── ModItems.java                     物品注册
├── ModParticles.java                 粒子类型注册
└── block/
    ├── InterdictionTorchBlock.java           落地火把
    ├── InterdictionWallTorchBlock.java       贴墙火把
    └── InterdictionTorchBlockEntity.java     力场逻辑

src/client/java/com/ribu/interdictiontorch/client/
├── InterdictionTorchClient.java      客户端入口
└── particle/InterdictionFlameParticle.java
```

## 实现说明

26.1 起 Minecraft 官方版本不再混淆，Fabric 也已停止维护 Yarn 映射，因此本项目直接使用
Mojang 官方类名（例如 `TorchBlock`、`Item.Properties`）。

方块与物品的行为尽量与原版火把保持逐项一致：碰撞箱、支撑判定、光照、音效、活塞反应、
战利品表、`wall_post_override` 与 `washed_away_by_fluids` 标签，以及 `animateTick`
中的粒子生成位置与速度。

## 许可

MIT，见 [LICENSE](LICENSE)。
