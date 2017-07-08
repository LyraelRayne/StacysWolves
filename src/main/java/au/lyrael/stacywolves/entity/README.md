# How to add a new wolf
## Add a texture
1. Create textures for <wolf_texture_id>_wolf.png , <wolf_texture_id>_wolf_angry.png and <wolf_texture_id>_wolf_tame.png . These should be based off the vanilla wolf texture. 
1. Create a folder in src/main/resources/assets/stacywolves/textures/entity/wolf/<wolf_texture_id>
1. Copy your textures into this folder

NOTE: No support right now for custom collars. If custom collars are required we'll need to update IWolfTextureSet and related classes to support this.

## Create a wolf entity
1. Extend EntityWolfBase in the au.lyrael.stacywolves.entity.wolf package.
    _DO_ use the naming convention already in place.
1. Annotate your new wolf class with @WolfMetadata. 
    * _ALWAYS_ set a name, primary colour, secondary colour and probability.
    * _ALWAYS_ put some @WolfSpawns in spawns _IFF_ you want the wolf to spawn in the world.
1. Override the createChild method to create this kind of wolf when breeding.
1. Override getTextureFolderName to return <wolf_texture_id>
1. Make a constructor and have it add a liked item for taming.
1. Implement any special features (AI, spawn conditions, etc.)

## Add localisation
1. Open src/main/resources/assets/stacywolves/lang/en_US.lang
1. Add a name as entity.stacywolves.<entity_id>.name (entity ID was specified in @WolfMetadata annotation)

NOTE: EntitySavannahWolf does just the bare essentials.
As such if you want to copy a template, it's probably best to use that.