# Useful Links

Video tutorial: https://www.youtube.com/watch?v=3rfnp1FcVlI&t=11s
UltimateAdvancementAPI GitHub: https://github.com/frengor/UltimateAdvancementAPI
Spigot: https: https://www.spigotmc.org/resources/95585/
Discord: https://discord.gg/KRS7dSU6tR
FAQ: https://github.com/frengor/UltimateAdvancementAPI/wiki/FAQ
UltimateAdvancementGenerator: https://escanortargaryen.dev/UltimateAdvancementGenerator


# Before starting

This tool was created with the aim of facilitating the creation of advancements, totally avoiding coding the graphic part of making advancements.

It is still in beta. Please report bugs [on GitHub](https://github.com/frengor/UltimateAdvancementAPI/issues).

With this tool you can:
* Create advancement tabs using a graphical tool
* Generate the classes for your advancements


# Configure the Java project

* Create a new project on you IDE and make a new package.
* Make your own main class of the plugin.
* Set the right package.

The downloaded ZIP file contains:
* The `advs` folder. Inside there are:
	* Sub-package of every `AdvancementTab` with all the classes of the generated advancements.
	* AdvancementTabNamespaces class that contains a namespace of every `AdvancementTab`.
* A file called `initTabs.java`. This isn't a class, it contains:
	* UltimateAdvancementAPI intance.
	* `AdvancementTab` instances.
	* `#initializeTabs()` method that initialize every tab.

Miss the event that will show the `AdvancementTab`s.

* Open your project folder and go to the right package and drag in it the `advs` folder.
* In your main class copy and paste the content of `initTabs.java`.
* Check that everything is ok and generate your plugin!


Confused? Check out our support discord server (Linked Above) or the video tutorial (Linked Above).
