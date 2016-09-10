The Official Ore Package Manager for Sponge
===========================================

The Ore Sponge plugin is in a pre-beta status. Please do not use in a production environment. I am not responsible for any damage you incur on your server. Thanks!

# Command Reference

##  `ore install`

Installs a plugin by it's unique ID and all of it's dependencies (by default). Note that a newly installed plugin will not be loaded until the next time the server boots.

Example:

`ore install ore-test`

## `ore download`

Downloads but does not install a plugin to a configured directory.

Example:

`ore download ore-test`

## `ore uninstall`

Uninstalls a plugin by it's unique ID. Note that currently loaded plugins will remain loaded until the server is restarted.

Example:

`ore uninstall ore-test`

## `ore update`

Updates a plugin to a different version. By default this uses the "recommended version" the project has set on their page on Ore but can be altered to target any version, including downgrades.

Examples:

`ore update ore-test`

`ore update ore-test 1.0.0`

## `ore search`

Displays a list of plugins from Ore based on a search query.
 
Examples:

`ore search ore-test`

`ore search windy`

## `ore whois`

Lists projects by the specified Ore user.

Example:

`ore whois windy`

## `ore show`

Displays information about an Ore project.

Example:

`ore show ore-test`

Example output:

```
Name: Ore Test Plugin
ID: ore-test
Author: windy
Category: Admin Tools
Installed: 1.0.0
Recommended: 1.0.0
Loaded: Yes
Location: /Users/walker/Dev/sponge/minecraft/forge/mods/Ore Test Plugin-1.0.0.jar
```

## `ore describe`

Displays the description of an Ore project.

Example:

`ore describe ore-test`

## `ore reload`

Reloads the configuration file.

## `ore version`

Displays version information about the plugin.

