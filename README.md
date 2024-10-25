This project is the biggest Spigot/Paper plugin for Wisecraft.xyz. A Minecraft server network. It uses Maven as its buildtool and is mostly based on Spigot's API and a handful of other plugins

The project is divided into modules that each has a main area of function. Examples are:
* PVPToggle handles all pvp related features
* Advancements handles all Advancements, including data gathering from other plugins for the advancements to function.

The modulation system as a whole enables faster development in the fact you don't have to restart the server to reload and can use code hotswap. Reloading a module simulates actual reloading. It also gives us the option to disable/enable modules/features in production servers depending on what the server needs or in case major bugs are found. 
