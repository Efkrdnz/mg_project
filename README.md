# Minefinity Gauntlet

A NeoForge 1.21.1 mod implementing Marvel's Infinity Stones system in Minecraft with extensive abilities and custom dimensions.

## Overview

This mod adds all six Infinity Stones to Minecraft, each providing multiple unique abilities. The stones can be combined in a gauntlet to unlock additional powers. The mod prioritizes spectacle and functionality over game balance.

## Stone Abilities

### Space Stone
Handles teleportation mechanics, portal creation, and spatial manipulation. Includes point-to-point teleportation, dimensional travel, and distance-based effects.

### Power Stone
Provides destructive capabilities through energy projection, explosive force, and area damage. Features laser attacks, energy blasts, and large-scale destruction abilities.

### Reality Stone
Enables matter manipulation and dimensional banishment. Can transform blocks, alter terrain, and send entities to custom dimensions outside normal reality.

### Time Stone
Controls temporal flow with rewind mechanics, time freezing, and acceleration. Implements both quick memory-based reversals and full disk-based world restoration.

### Soul Stone
Manages life force, resurrection, and entity soul manipulation. Handles player revival, soul extraction, and life-related mechanics.

### Mind Stone
Controls entity behavior, AI manipulation, and mental influence. Affects mob actions, player control, and entity awareness.

### Combination Abilities
Requires multiple stones equipped simultaneously. Unlocks enhanced versions of base abilities and unique multi-stone powers.

## Custom Dimensions

### Unreal Void
Hub dimension featuring mathematically-generated cosmic structures. Contains 60 different visual patterns including parametric equations, spiral formations, fractals, and impossible geometry. Serves as a central access point for other dimensional spaces.

### Banishment Dimensions
Multiple themed dimensions accessible through Reality Stone abilities:
- Infinite corridor systems with puzzle mechanics
- Fractured mirror realms
- Recursive loop spaces
- Additional themed environments

## Technical Implementation

### Architecture
- Built on NeoForge 1.21.1
- Package structure: `net.mcreator.minefinitygauntlet`
- Developed using MCreator's procedure system with custom Java integration
- Server-side logic with client-side rendering separation

### Data Systems
- NBT-based persistence for entity data and world states
- Global map variables for temporary state tracking
- Entity data preservation including trades, equipment, and custom attributes
- Multiplayer synchronization through server-side entity NBT

### Rendering
- Custom OpenGL shaders for visual effects
- Vertex buffer systems for complex geometry
- Post-processing effects including gravitational lensing
- Level-of-detail rendering for performance optimization

### Performance
- Gradual processing for large-scale effects (distributed across multiple ticks)
- Entity processing limits to prevent frame drops
- Face culling and render distance optimization
- Example: World Sunder ability processes 400 blocks per tick over 3 seconds, supporting 20,000+ blocks

## Installation

Requirements:
- Minecraft 1.21.1
- NeoForge for 1.21.1

Installation:
1. Install NeoForge 1.21.1
2. Place mod .jar file in mods folder
3. Launch game

## Development

Primary development done through MCreator 2024.3.42716 with custom Java procedures. The mod uses MCreator's visual procedure framework while integrating direct code for advanced functionality.

## Current State

Active development with 25+ implemented abilities across all stones. Recent work focuses on dimensional mechanics and visual effect refinement. Time loop system complete with both snapshot and full backup capabilities.

## License

All Rights Reserved © 2025 Efkrdnz

You may freely use this mod for gameplay, servers, and content creation with the following terms:

**Allowed:**
- Use on multiplayer servers and in modpacks
- Create videos, streams, and other content (monetization permitted)
- Modify for personal use

**Not Allowed:**
- Redistribute the mod or modified versions
- Use code or assets in other projects
- Implement pay-to-win server features using this mod
- Reupload to other hosting platforms

Servers must keep all mod features accessible to all players equally.

## Credits

Developed by Efkrdnz

Based on Marvel's Infinity Stones concept.
