package azzy.fabric.lookingglass.item;

import azzy.fabric.incubus_core.datagen.ModelJsonGen;
import azzy.fabric.lookingglass.block.EclipseRoseBlock;
import azzy.fabric.lookingglass.block.LookingGlassBlocks;
import azzy.fabric.lookingglass.blockentity.LookingGlassMachine;
import azzy.fabric.lookingglass.gui.PoweredFurnaceGuiDescription;
import azzy.fabric.lookingglass.recipe.LookingGlassRecipes;
import azzy.fabric.lookingglass.util.machine.ModifierProvider;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketSlots;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import static azzy.fabric.lookingglass.LookingGlassCommon.*;

@SuppressWarnings("unused")
public class LookingGlassItems {

    //FoodComponents
    private static final FoodComponent BAD_NOMS = new FoodComponent.Builder().alwaysEdible().hunger(2).statusEffect(new StatusEffectInstance(StatusEffects.POISON, 100), 1F).build();
    private static final FoodComponent STEELHEAD = new FoodComponent.Builder().meat().snack().hunger(3).saturationModifier(1F).build();
    private static final FoodComponent SHIMMERFIN = new FoodComponent.Builder().meat().hunger(8).saturationModifier(2F).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 600, 3), 1F).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 600, 1), 1F).statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 1200, 0), 1F).build();

    private static FabricItemSettings defaultSettings() {
        return new FabricItemSettings().group(LOOKINGGLASS_ITEMS);
    }

    public static void init() {
        TrinketSlots.addSlot(SlotGroups.OFFHAND, Slots.RING, new Identifier("trinkets", "textures/item/empty_trinket_slot_ring.png"));
    }

    private static FabricItemSettings genericSettings(Rarity rarity) {
        return new FabricItemSettings().group(LOOKINGGLASS_ITEMS).rarity(rarity);
    }

    //Tools
    public static final Item DATA_SHARD = registerItem("data_shard", new DataShardItem(defaultSettings().maxCount(16)));
    public static final Item ENERGY_PROBE = registerItem("energy_probe", new EnergyProbeItem(defaultSettings().maxCount(1)));
    public static final Item BASE_RING = registerGeneratedItem("ring", new Item(defaultSettings().rarity(Rarity.UNCOMMON).maxCount(8)));
    public static final Item SIMPLE_ANGEL_RING = registerItem("simple_angel_ring", new SimpleAngelRingItem(new FabricItemSettings().group(LOOKINGGLASS_ITEMS).rarity(FINIS_RARITY).maxCount(1)));
    public static final Item ADVANCED_ANGEL_RING = registerItem("advanced_angel_ring", new AdvancedAngelRingItem(new FabricItemSettings().group(LOOKINGGLASS_ITEMS).rarity(LUPREVAN_RARITY).fireproof().maxCount(1)));
    // I should consider fixing the potential bug where user changes the stack size for lasso, has more than one in a stack and uses on an entity.  It makes ALL the lassos get the entity.
    // So if the user subsequently spreads out the lassos and uses them one at a time, they effectively duplicate the mob.  I've sidestepped the issue for now by forcing stack size to 1.
    public static final Item GOLDEN_LASSO = registerItem("golden_lasso", new LassoItem(defaultSettings().maxCount(1), false));
    public static final Item CURSED_LASSO = registerItem("cursed_lasso", new LassoItem(defaultSettings().maxCount(1), true));
    public static final Item SACRED_SHOVEL = registerItem("sacred_shovel", new SacredShovelItem(ToolMaterials.GOLD, defaultSettings().maxCount(1)));

    //Upgrades
    public static final Item ROSE_CHIPSET = registerGeneratedItem("rose_chipset", new Item(defaultSettings()));
    public static final Item BASIC_SPEED_UPGRADE = registerGeneratedItem("basic_speed_upgrade", new GenericUpgradeItem(defaultSettings(), 1.2, 0, 0.95, ModifierProvider.AdditivityType.ADD, ModifierProvider.AdditivityType.ADD, LookingGlassMachine.MachineTier.BASIC));
    public static final Item BASIC_ENERGY_STORAGE_UPGRADE = registerGeneratedItem("basic_energy_storage_upgrade", new GenericUpgradeItem(defaultSettings(), 1, 0.5, 1, ModifierProvider.AdditivityType.ADD, ModifierProvider.AdditivityType.ADD, LookingGlassMachine.MachineTier.BASIC));
    public static final Item BASIC_ENERGY_USAGE_UPGRADE = registerGeneratedItem("basic_energy_usage_upgrade", new GenericUpgradeItem(defaultSettings(), 0.9, 0, 1.05, ModifierProvider.AdditivityType.ADD, ModifierProvider.AdditivityType.ADD, LookingGlassMachine.MachineTier.BASIC));
    public static final Item FREEZER_UPGRADE_ITEM = registerGeneratedItem("freezer_upgrade", new RecipeConvertingUpgradeItem<>(defaultSettings(), LookingGlassRecipes.FREEZING_RECIPE, PoweredFurnaceGuiDescription.class, 2.0, 0, 4.0, ModifierProvider.AdditivityType.ADD, ModifierProvider.AdditivityType.ADD));
    public static final Item BLAST_UPGRADE_ITEM = registerGeneratedItem("blast_upgrade", new RecipeConvertingUpgradeItem<>(defaultSettings(), RecipeType.BLASTING, PoweredFurnaceGuiDescription.class, 2.0, 0, 0.5, ModifierProvider.AdditivityType.ADD_MULT, ModifierProvider.AdditivityType.ADD));

    //Foods
    public static final Item CORN_COB = registerGeneratedItem("corn_cob", new Item(defaultSettings().food(new FoodComponent.Builder().hunger(2).saturationModifier(0F).build())));
    public static final Item ROASTED_CORN_COB = registerGeneratedItem("roasted_corn_cob", new Item(defaultSettings().food(new FoodComponent.Builder().hunger(6).saturationModifier(1.5F).build())));
    public static final Item LEGENDARY_CORN_COB = registerGeneratedItem("legendary_corn_cob", new Item(genericSettings(Rarity.EPIC).fireproof().food(new FoodComponent.Builder().hunger(200).saturationModifier(4F).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 18000, 19), 1F).statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 18000, 3), 1F).build())));

    //Materials
    public static final Item ENRICHED_CHARCOAL = registerGeneratedItem("enriched_charcoal", new Item(defaultSettings()));
    public static final Item IRON_DUST = registerGeneratedItem("iron_dust", new Item(defaultSettings()));
    public static final Item GOLD_DUST = registerGeneratedItem("gold_dust", new Item(defaultSettings()));
    public static final Item ANCIENT_DEBRIS_DUST = registerGeneratedItem("ancient_debris_dust", new Item(defaultSettings()));
    public static final Item NETHERITE_DUST = registerGeneratedItem("netherite_dust", new Item(defaultSettings()));
    public static final Item STEEL_BLEND = registerGeneratedItem("steel_blend", new Item(defaultSettings()));
    public static final Item FISH_FEED = registerItem("fish_feed", new Item(defaultSettings().food(BAD_NOMS)));
    public static final Item DWARVEN_CLAY = registerGeneratedItem("dwarven_clay", new Item(defaultSettings()));
    public static final Item SILICON_INGOT = registerGeneratedItem("red_silicon_ingot", new Item(defaultSettings()));
    public static final Item SILICON_INDUCTION_COIL = registerGeneratedItem("silicon_induction_coil", new Item(defaultSettings()));
    public static final Item ROSE_GOLD_INGOT = registerGeneratedItem("rose_gold_ingot", new Item(defaultSettings()));
    public static final Item ROSE_GOLD_NUGGET = registerGeneratedItem("rose_gold_nugget", new Item(defaultSettings()));
    public static final Item PINK_GEL = registerGeneratedItem("pink_gel", new Item(genericSettings(Rarity.UNCOMMON)));
    public static final Item CELESTIAL_AMALGAM = registerGeneratedItem("celestial_amalgam", new Item(genericSettings(FINIS_RARITY)));
    public static final Item FINIS_INGOT = registerGeneratedItem("finis_ingot", new Item(genericSettings(FINIS_RARITY)));
    public static final Item FINIS_NUGGET = registerGeneratedItem("finis_nugget", new Item(genericSettings(FINIS_RARITY)));
    public static final Item ELDENMETAL_INGOT = registerItem("eldenmetal_tear", new Item(genericSettings(ELDENMETAL_RARITY)));
    public static final Item ELDENMETAL_NUGGET = registerItem("eldenmetal_drop", new Item(genericSettings(ELDENMETAL_RARITY)));
    public static final Item LUPREVAN_GLASS = registerGeneratedItem("luprevan_glass", new Item(genericSettings(LUPREVAN_RARITY).fireproof()));
    public static final Item LUPREVAN_GLASS_SHARD = registerGeneratedItem("luprevan_glass_shard", new Item(genericSettings(LUPREVAN_RARITY).fireproof()));
    //public static final Item ELDENMETAL_GEMSTONE = registerItem("eldenmetal_gem", new Item(eldenmetalSettings()));

    //Weapons
    public static final Item STEELHEAD_TROUT = registerItem("steelhead_trout", new FishWeaponItem(false, 8, defaultSettings().rarity(Rarity.UNCOMMON).fireproof().food(STEELHEAD)));
    public static final Item PRISMATIC_SHIMMERFIN = registerItem("shimmerfin", new FishWeaponItem(true, 199, defaultSettings().rarity(WORLDFORGE_RARITY).fireproof().food(SHIMMERFIN)));
    public static final Item MARKSMAN_REVOLVER = registerItem("marksman_revolver", new RevolverItem(defaultSettings().rarity(WORLDFORGE_RARITY).fireproof()));
    public static final Item COIN = registerItem("coin", new CoinItem(defaultSettings().fireproof()));

    //Registry shenanigans
    public static final Item ANGEL_BLOCK = registerItem("angel_block", new AngelBlockItem(LookingGlassBlocks.ANGEL_BLOCK, new FabricItemSettings().group(LOOKINGGLASS_BLOCKS).fireproof()));
    public static final Item CURSED_EARTH_BLOCK = registerItem("cursed_earth", new CursedEarthBlockItem(LookingGlassBlocks.CURSED_EARTH_BLOCK, new FabricItemSettings().group(LOOKINGGLASS_BLOCKS)));
    public static final Item WOODEN_SPIKE_BLOCK = registerItem("wooden_spike", new WoodenSpikeBlockItem(LookingGlassBlocks.WOODEN_SPIKE_BLOCK, new FabricItemSettings().group(LOOKINGGLASS_BLOCKS)));
    public static final Item IRON_SPIKE_BLOCK = registerItem("iron_spike", new IronSpikeBlockItem(LookingGlassBlocks.IRON_SPIKE_BLOCK, new FabricItemSettings().group(LOOKINGGLASS_BLOCKS)));
    public static final Item DIAMOND_SPIKE_BLOCK = registerItem("diamond_spike", new DiamondSpikeBlockItem(LookingGlassBlocks.DIAMOND_SPIKE_BLOCK, new FabricItemSettings().group(LOOKINGGLASS_BLOCKS)));
    public static final Item NETHERITE_SPIKE_BLOCK = registerItem("netherite_spike", new NetheriteSpikeBlockItem(LookingGlassBlocks.NETHERITE_SPIKE_BLOCK, new FabricItemSettings().group(LOOKINGGLASS_BLOCKS)));
    public static final Item SLOW_VECTOR_PLATE_BLOCK = registerItem("slow_vector_plate", new VectorPlateBlockItem(LookingGlassBlocks.SLOW_VECTOR_PLATE_BLOCK, new FabricItemSettings().group(LOOKINGGLASS_BLOCKS)));
    public static final Item NORMAL_VECTOR_PLATE_BLOCK = registerItem("normal_vector_plate", new VectorPlateBlockItem(LookingGlassBlocks.NORMAL_VECTOR_PLATE_BLOCK, new FabricItemSettings().group(LOOKINGGLASS_BLOCKS)));
    public static final Item FAST_VECTOR_PLATE_BLOCK = registerItem("fast_vector_plate", new VectorPlateBlockItem(LookingGlassBlocks.FAST_VECTOR_PLATE_BLOCK, new FabricItemSettings().group(LOOKINGGLASS_BLOCKS)));
    public static final Item UNSTABLE_ENCHANTER_BLOCK = registerItem("unstable_altar", new UnstableAltarBlockItem(LookingGlassBlocks.UNSTABLE_ALTAR_BLOCK, new FabricItemSettings().group(LOOKINGGLASS_BLOCKS)));
    public static final Item DISPLAY_PEDESTAL_BLOCK = registerItem("display_pedestal", new DisplayPedestalBlockItem(LookingGlassBlocks.DISPLAY_PEDESTAL_BLOCK, new FabricItemSettings().group(LOOKINGGLASS_BLOCKS)));
    public static final Item ECLIPSE_ROSE_BLOCK = registerItem("eclipse_rose", new EclipseRoseBlockItem(LookingGlassBlocks.ECLIPSE_ROSE, new FabricItemSettings().group(LOOKINGGLASS_BLOCKS)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(MODID, name), item);
    }

    private static Item registerGeneratedItem(String name, Item item) {
        Identifier id = new Identifier(MODID, name);
        if (REGEN_ITEMS)
            ModelJsonGen.genItemJson(METADATA, id);
        return Registry.register(Registry.ITEM, id, item);
    }
}
