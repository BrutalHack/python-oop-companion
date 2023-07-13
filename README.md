# class_name.py

![Build](https://github.com/intenics/class_name_py/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/io.intenics.python.fileNameMismatch.svg)](https://plugins.jetbrains.com/plugin/io.intenics.python.fileNameMismatch)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/io.intenics.python.fileNameMismatch.svg)](https://plugins.jetbrains.com/plugin/io.intenics.python.fileNameMismatch)

<!-- Plugin description -->
Keep python file names synced to their class names!

Contains inspection to find wrongly named files and quickfix to rename them and their usages.

This plugin aims to make working with object-oriented Python code easier.

The inspection ignores all files within "venv" or ".venv" directories.

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "class_name.py"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/intenics/pycharm-file-names/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation