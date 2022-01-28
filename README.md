# ViciousCore
Vicious Core is a powerful mod designed to eliminate the tediousness of many processes in modding Minecraft while keeping everything as server efficient as possible. 
This includes: item and entity rendering and animation, mob spawn detection and handling.
Soon to come:
Structure generation and modification.
Tile Entities and Multiblocks.

For rendering and animation, see [ViciousCoreClient](https://github.com/Vicious-MCModding/ViciousCoreClient). which is dependent on CodeChickenLib for more information.

## Fabric Branch

WIP Port of the 1.12.2 version of ViciousCore

## Common Features:
- Mobspawn detection
- Tile Entity Code overwriting
- Field Code overwriting
- Easy Config Creation
## Client Features:
- Rendering and animation
- Entity rendering overwriting.
## WIP Features:
- Structure Generation
- Tile Entities and MultiBlocks
- GUI Systems.

# Using as a Gradle Dependency
1. Put this in your build script
```gradle
repositories {
    maven {
        name = "ViciousCore"
        url = uri("https://maven.pkg.github.com/Vicious-MCModding/ViciousCore")
        credentials {
            username = project.gpr_name
            password = project.gpr_key
        }
    }
}
dependencies {
    compile "com.vicious:viciouscore:VERSION"
}
```
2. If you don't have a GPR Key you need to make one. To do so, go here: [https://github.com/settings/tokens](https://github.com/settings/tokens)
* Click generate new token
* Click the read:packages checkbox
* Scroll down and generate that boyo. Copy the key, and in gradle.properties write
```
gpr_user=YOUR GITHUB USERNAME
gpr_key=THE KEY YOU JUST MADE.
```
* You should be good now BUT be warned. This key grants anyone with it special privileges (the ones you gave it in that checkbox section). Make sure that this key is kept private, you'll want your git system to ignore the gradle.properties file in this case. You could also just use the System Environment variables as well.

Doing this will both provide you ALL dependencies for core and any of the dependencies for the dependencies (Wow its like magic).
