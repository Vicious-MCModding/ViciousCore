# ViciousCore
 Vicious Core mod for all sorts of good stuff.

# Features:

**modification.MobSpawnModifier
Allows modification of mobs on the first tick after they spawn for the first time.**
To add a modificator execute MobSpawnModifier.entityModificators.put(entity class, consumer)
By doing this, you can modify pretty much anything in the entity.

**Overriding Vanilla item and entity rendering**
VCore Introduces a bunch of ways you can override Minecraft's default rendering system and adds an easy way of modifying rendering while the game is running. To get started, you will need to understand **IRenderOverride**
Whenever a living entity is about to be rendered in the game VCore checks a few things in the entity before doing so.
Here's what you need to know
 - If the entity is holding an Item that implements IRenderOverride, VCore will call IRenderOverride.renderEntity(Renderer, EntityLivingBase).
   - When this happens, by default VCore will then get the Item's OverrideConfigurations and use them for many things.

**What are OverrideConfigurations?**
One of the most painstaking processes in modding this god forsaken game is reloading it about a billion times to render a model or entity in the correct way. You have to get the right rotation, scale, translation, etc...
**The goal of OverrideConfigurations is to make this entire process reloadable during runtime. Using OverrideConfigurations will save you LOTS of time. Here's how.**
First of all, you need to register an OverrideConfiguration for your item. This can be done when IRenderOverride#registerRenderers is called. 
Simply write in your Item Class: 
```
@Override
public void registerRenderers() {
    //Other stuff...
    renderconfig = OverrideConfigurations.create(this);
    //Other stuff...
}
```
**Now on startup or when "ircfg reload" is called, your item's OverrideConfigurations will be automatically generated in the run/resources/vicious/<yourmodid>/<itemid> directory.
**The json file there provides 9 fields by default for modifying your item's rendering. These should be self explanatory so I won't delve deeper about that.
Just know a few things.
An OverrideConfiguration must have active set to true to work, as well as any of its transformations that will be ran.

With that, you should be able to render the item at the correct scale, rotation, and position. For Animations, you will have to manually implement them. More on that later.

**What about entities? Well, OverrideConfigurations has you covered. Note: Currently, OverrideConfigurations only supports overriding rendering when an IRenderOverride Item is held**
**You can add overrides for specific Models by doing the following in your Item class**
```
@Override
public void registerRenderers() {
    //Other stuff...
    renderconfig = OverrideConfigurations.create(this);
    renderconfig.addEntityModelOverrider(modelclass extends ModelBase); //EXAMPLE OF COMPLETE CODE renderconfig.addEntityModelOverrider(ModelBiped.class);
    //Other stuff...
}
```
**In the Example, we are overriding the ModelBiped rendering. This will effect vanilla mobs such as Zombies, Skeletons, Pigmen, ... . Once you do this, the OverrideConfigurations for that specific model type will be generated in run/resources/vicious/<yourmodid>/<itemid>/<ModelClassCanonicalName>** 
Each Model part will have its own OverrideConfiguration json file generated. This works the same as the Item variant, just without scale. If you enable one of the parts to have the render be overriden, the entity will be rendered differently while the item is held.

**Being able to do this in such an easy fashion allows you to quickly created cursed beings such as the one below**

<spider holding gun probably>

