# Python OOP Companion

![Build](https://github.com/intenics/class_name_py/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/io.intenics.python.fileNameMismatch.svg)](https://plugins.jetbrains.com/plugin/io.intenics.python.fileNameMismatch)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/io.intenics.python.fileNameMismatch.svg)](https://plugins.jetbrains.com/plugin/io.intenics.python.fileNameMismatch)

<!-- Plugin description -->
Make writing OOP Python feel more like writing typescript!

Each feature can be enabled individually:
- Keep file names in sync with the classes within 
- Ensure all empty methods in ABC (abstract base classes) are marked @abstractmethod 
- Support for "Interface"-based architecture
    - Define your Prefix or Suffix for naming of Interface classes
    - Ensure ABC classes with only @abstractmethods have the Interface Prefix/Suffix
    - Ensure classes with the Interface Prefix/Suffix inherit from ABC
- Exclude paths (e.g. virtualenv, third-party code, scripts) from validation
    - via simple "path contains this string" 
    - via glob patterns
    - via regex patterns

This plugin was developed during an internal hackathon event at [Intenics](https://www.intenics.io).

If you're based in germany and looking for a dev job with AWS, check them out :) 
<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Python OOP
  Companion"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/intenics/pycharm-file-names/releases/latest) and install it manually
  using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Dev Environment Setup

1. Recommended JVM: Amazon Coretto 11
2. Recommended IDE: IntelliJ IDEA Ultimate (latest version)

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation