# InfinityLib
A shaded library for Slimefun addons which adds a bunch of useful classes and utilities.

[![Release](https://jitpack.io/v/ybw0014/InfinityLib.svg)](https://jitpack.io/#ybw0014/InfinityLib)

# Packages & Features
## Core
<b>AbstractAddon</b>: An implementation of JavaPlugin
which you will need to extend for many of the other features to work.
It provides multiple utility methods and does some basic setup for you.

<b>AddonConfig</b>: is an implementation of YamlConfiguration
which makes comments available in the user's config
and provides utility methods such as getting a value from within a range
and removing unused/old keys from the user's config.

## Common
<b>CoolDowns</b>: A utility object for keeping track of cool downs of players/uuids

<b>PersistentType</b>: Contains some PersistentDataTypes for
ItemStack's, ItemStack Array's, Locations, and String Arrays.
Also provides a constructor for PersistentDataType that uses lambda parameters.

<b>Events</b>: Contains static utility methods for registering listeners, creating handlers, and calling events

<b>Scheduler</b>: Contains static utility methods for running and repeating tasks

## Commands
<b>AddonCommand</b>: allows you to add commands easily with a parent-child structure,
so you could have a command with a sub command which has a sub command.
It also adds some default commands such as an addon info, aliases, and help command.

## Groups
<b>MultiGroup</b>: An implementation of ItemGroup which lets you organize your groups into SubGroups

<b>SubGroup</b>: An ItemGroup that is hidden from the main page, for use in MultiGroup

## Machines
<b>MenuBlock</b>: A SlimefunItem with a menu, providing overridable methods for setting up the menu

<b>TickingMenuBlock</b>: A MenuBlock with slimefun ticker

<b>AbstractMachineBlock</b>: A TickingMenuBlock which implements EnergyNetComponent and provides a process method

<b>MachineBlock</b>: An AbstractMachineBlock which makes it easy to create simple input-output machines

## Future Additions
<b>Translation Utility</b>: Some sort of easy way to create translatable strings for your addon's and infinitylibs's strings

<b>InfinityLib Metrics</b>: Metrics to see which versions or even classes are being used

# How to use

This fork of InfinityLib is built with Gradle. Add it to the `dependencies` section
in your `build.gradle.kts`:

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.ybw0014:InfinityLib:SPECIFY VERSION HERE")
}
```

Then you need to shade and relocate it into your own package so that it doesn't
conflict with other addons' classes. Using the Gradle shadow plugin:

```kotlin
plugins {
    id("com.gradleup.shadow") version "9.6.1"
}

tasks.shadowJar {
    // minimizeJar: exclude unused classes to reduce file size, not required
    minimize()
    // REQUIRED: make sure to replace the package name
    relocate("io.github.mooy1.infinitylib", "YOUR.MAIN.PACKAGE.HERE.infinitylib")
    exclude("META-INF/*")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
```

Then change your main plugin class to extend `AbstractAddon` and implement the constructor.
You will need to use `enable()` and `disable()` instead of `onEnable()` and `onDisable`.
Make sure you don't call `super.onEnable/Disable`.
Your updater and config setup is now handled, make sure to test that it's working though!
