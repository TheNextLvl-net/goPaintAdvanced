![](https://github.com/TheNextLvl-net/goPaintAdvanced/blob/master/images/gopaint-banner.png?raw=true)

---

**goPaint** is a plugin designed to simplify painting inside Minecraft.
goPaintAdvanced is a fork of the [original goPaint plugin](https://github.com/Arcaniax-Development/goPaint_1.14), featuring numerous improvements and bug fixes.
Our goal is to stay up-to-date, leveraging modern technologies rather than relying on outdated or obsolete code.

The primary focus of goPaintAdvanced is to drastically improve performance by leveraging a multithreaded architecture,
ensuring smoother and faster brush operations, even in demanding environments.

## Major Changes

- **Fully asynchrone** - No lags whatsoever
- **100%** customizable messages
- Better WorldEdit integration
- Survival Support
- New brushes
- Brigadier commands
- Improved configuration file
- Improved and faster algorithms
- No legacy code or dependencies
- Hangar and Modrinth auto deployment
- New CI Development builds

## Overview

goPaintAdvanced's default brush is the Feather.

    Left-click with the Feather to open the GUI, where you can customize, import, and export brushes.
    Right-click with any brush to use it in the direction you're facing.

### Importing and Exporting Brushes

Exporting a brush allows you to save and store multiple brushes, similar to how WorldEdit handles brushes.
This means you can easily switch between brushes, giving you the flexibility to work with as many brushes as you need simultaneously.

Importing a brush into the GUI lets you customize that specific brush without having to re-enter all the settings, streamlining the process for quick adjustments.

    Export a brush: Click an item on the toggle in the GUI.
    Import a brush: Click the item on the toggle again to import it.

## Advanced Features

For more advanced users, goPaintAdvanced offers command access to control nearly all brush settings and features.

    /gopaint size <size> - change the size of your brush
    /gopaint menu - opens the brush menu
    /gopaint brush <brush> - change your brush
    /gopaint wand - gives you the brush item
    /gopaint export - export your brush to your current item
    /gopaint import - import your current brush item to the gui
    /gopaint toggle - enable or disable your brush
    /gopaint reload - reload the config

## Permissions

    gopaint.use - grants access to using all painting related commands and tools as well as the interface
    gopaint.admin - the admin permission to reload the plugin
    gopaint.world.bypass - allows users to bypass world restrictions

## Links

* [Download - Modrinth](https://modrinth.com/plugin/gopaintadvanced)
* [Download - Hangar](https://hangar.papermc.io/TheNextLvl/goPaintAdvanced)
* [Discord](https://thenextlvl.net/discord)
* [Issues](https://github.com/TheNextLvl-net/goPaintAdvanced/issues)

# Building

Gradle is the recommended way to build the project. Use `./gradlew shadowJar` in the main project directory to build the
project.

## Suggestions

Suggestions are welcome! We have a separate issue form for suggestions, that can be
found [here](https://github.com/TheNextLvl-net/goPaintAdvanced/issues).
