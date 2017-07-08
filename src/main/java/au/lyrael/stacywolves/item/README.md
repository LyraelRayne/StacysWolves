# Items

## Creating a new bone/wolf food
### Add to the code
1. Open au.lyrael.stacywolves.item.ItemWolfFood
1. Add createSubItem("<food_id>"); to buildItemsList
1. Add registerRecipe("<food_id>", new ItemStack(<recipe_item>)); to registerRecipes()

### Add localisation
1. Open src/main/resources/assets/stacywolves/lang/en_US.lang
1. Add a name as item.stacywolves.wolf_food_<food_id>.name
1. Add a description as item.stacywolves.wolf_food_<food_id>.tooltip . Make sure it's cute and/or funny!

