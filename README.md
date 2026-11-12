# Interdiction Torch Standalone (禁止火把)

[English](README.md) | [简体中文](README.zh-CN.md)

A Fabric port of the Interdiction Torch from Equivalent Exchange. Creates a force field that pushes hostile mobs away and deflects projectiles.

![Texture preview](docs/preview.png)

## Features

- Two forms, floor and wall mounted, placed by a single item
- Light level 14
- Force field with a 12 block radius; push strength decreases with distance and stacks across torches
- Projectiles approaching the torch are reflected
- Custom violet flame particle; spawn position, motion and scaling follow vanilla `FlameParticle`

The force field affects hostile mobs only. Players and passive mobs are unaffected.

## Requirements

| Component | Version |
| --- | --- |
| Minecraft | 26.3 |
| Fabric Loader | 0.20.1 or later |
| Fabric API | 0.165.2+26.3 or later |
| Java | 25 |

## Installation

1. Install Fabric Loader for 26.3
2. Place Fabric API and this mod's jar into `mods/`
3. Start the game

## Recipe

```
 A       A = Amethyst Cluster
 S       S = Stick
 S
```

Yields 1 Interdiction Torch. The cluster occupies the top slot, the two sticks are stacked vertically below.

## Building from source

```powershell
# Requires JDK 25
$env:JAVA_HOME='C:\Path\temurin25-jdk-25.0.5+8'
.\gradlew build
```

The artifact is written to `build/libs/InterdictionTorch-<version>.jar`.

Development client:

```powershell
.\gradlew runClient
```

## Project layout

```
src/main/java/com/ribu/interdictiontorch/
├── InterdictionTorch.java            Mod entrypoint
├── ModBlocks.java                    Block registration
├── ModBlockEntities.java             Block entity type registration
├── ModItems.java                     Item registration
├── ModParticles.java                 Particle type registration
└── block/
    ├── InterdictionTorchBlock.java           Floor torch
    ├── InterdictionWallTorchBlock.java       Wall torch
    └── InterdictionTorchBlockEntity.java     Force field logic

src/client/java/com/ribu/interdictiontorch/client/
├── InterdictionTorchClient.java      Client entrypoint
└── particle/InterdictionFlameParticle.java
```

## Implementation notes

Since Minecraft 26.1 the official distribution is unobfuscated, and Fabric has stopped maintaining
Yarn mappings, so this project uses Mojang class names such as `TorchBlock` and `Item.Properties`.

The following block and item behaviour matches the vanilla torch: collision shape, support checks,
light level, sound, piston and fluid reactions, loot tables, the `wall_post_override` and
`washed_away_by_fluids` tags, and the particle spawn position and velocity in `animateTick`.

## Built-in resource pack

The mod ships an optional 128x texture pack as a built-in pack. It is disabled by default and can be enabled
from the resource pack screen; it adds diffuse, normal and LabPBR specular maps.

## License

MIT, see [LICENSE](LICENSE).




