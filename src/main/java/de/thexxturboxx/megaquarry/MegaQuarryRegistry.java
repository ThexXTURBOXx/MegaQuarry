package de.thexxturboxx.megaquarry;

import java.io.File;

import de.thexxturboxx.megaquarry.blocks.BetterQuarry;
import de.thexxturboxx.megaquarry.blocks.FilteredQuarry;
import de.thexxturboxx.megaquarry.blocks.MQBlockContainer;
import de.thexxturboxx.megaquarry.blocks.Quarry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MegaQuarryRegistry {

	public static NonNullList<Ingredient> getIngredientList(int size, ItemStack... ingredients) {
		NonNullList<Ingredient> ingredientList = NonNullList.withSize(size, Ingredient.EMPTY);
		int i = 0;
		for (ItemStack stack : ingredients) {
			if (stack.isEmpty()) {
				ingredientList.set(i++, Ingredient.EMPTY);
			} else {
				ingredientList.set(i++, Ingredient.fromStacks(stack));
			}
		}
		return ingredientList;
	}

	private static <T extends Block> T registerBlock(T block) {
		ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(block.getRegistryName());
		return registerBlock(block, itemBlock);
	}

	private static <T extends Block> T registerBlock(T block, ItemBlock itemBlock) {
		GameRegistry.register(block);
		GameRegistry.register(itemBlock);
		if (block instanceof MQBlockContainer) {
			((MQBlockContainer) block).registerItemModel(itemBlock);
		}
		return block;
	}

	public static void registerBlocks(FMLPreInitializationEvent e) {
		File configdir = e.getModConfigurationDirectory();
		File configfile = new File(configdir, MegaQuarryMod.MODID + ".cfg");
		if (!configfile.exists())
			configdir.mkdirs();
		Configuration config = new Configuration(configfile);
		config.load();
		boolean hardmode = config.getBoolean("hardmode_recipes", "General", false,
				"Very hard recipes, only for hard men!");
		config.save();
		MegaQuarryAdditions.quarry = registerBlock(new Quarry());
		MegaQuarryAdditions.better_quarry = registerBlock(new BetterQuarry());
		MegaQuarryAdditions.filtered_quarry = registerBlock(new FilteredQuarry());
		if (hardmode) {
			registerShapedRecipe(new ItemStack(MegaQuarryAdditions.quarry), 3, 3, new ItemStack(Blocks.QUARTZ_BLOCK),
					new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(Blocks.QUARTZ_BLOCK),
					new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(Blocks.EMERALD_BLOCK),
					new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(Blocks.QUARTZ_BLOCK),
					new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(Blocks.QUARTZ_BLOCK));
			registerShapelessRecipe(new ItemStack(MegaQuarryAdditions.better_quarry),
					new ItemStack(MegaQuarryAdditions.quarry), new ItemStack(Blocks.DIAMOND_BLOCK),
					new ItemStack(Blocks.EMERALD_BLOCK));
			registerShapelessRecipe(new ItemStack(MegaQuarryAdditions.filtered_quarry),
					new ItemStack(MegaQuarryAdditions.better_quarry), new ItemStack(Blocks.DIAMOND_BLOCK),
					new ItemStack(Blocks.SKULL, 1, 1));
		} else {
			registerShapedRecipe(new ItemStack(MegaQuarryAdditions.quarry), 3, 3, new ItemStack(Items.IRON_INGOT),
					new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(Items.IRON_INGOT),
					new ItemStack(Items.IRON_INGOT), new ItemStack(Items.DIAMOND_SHOVEL),
					new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.REDSTONE),
					new ItemStack(Items.IRON_INGOT));
			registerShapelessRecipe(new ItemStack(MegaQuarryAdditions.better_quarry),
					new ItemStack(MegaQuarryAdditions.quarry), new ItemStack(Items.DIAMOND),
					new ItemStack(Blocks.COBBLESTONE));
			registerShapelessRecipe(new ItemStack(MegaQuarryAdditions.filtered_quarry),
					new ItemStack(MegaQuarryAdditions.better_quarry), new ItemStack(Items.DIAMOND),
					new ItemStack(Items.PAPER));
		}
	}

	public static void registerItems(FMLPreInitializationEvent e) {

	}

	public static void registerShapedRecipe(ItemStack output, int w, int h, ItemStack... ingredients) {
		GameRegistry.register(new ShapedRecipes("", w, h, getIngredientList(w * h, ingredients), output),
				new ResourceLocation(MegaQuarryMod.MODID,
						"recipe_" + output.getItem().getRegistryName().getResourcePath()));
	}

	public static void registerShapelessRecipe(ItemStack output, ItemStack... ingredients) {
		GameRegistry.register(new ShapelessRecipes("", output, getIngredientList(ingredients.length, ingredients)),
				new ResourceLocation(MegaQuarryMod.MODID,
						"recipe_" + output.getItem().getRegistryName().getResourcePath()));
	}

}
