package azzy.fabric.lookingglass.util.json;

import azzy.fabric.incubus_core.json.JsonUtils;
import azzy.fabric.lookingglass.vo.UnstableAltarRecipeVO;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.*;
import java.util.function.BiConsumer;

import static azzy.fabric.lookingglass.LookingGlassCommon.FFLog;

public class LookingGlassJsonManager {
    public static final String WOOD_SPIKE = "wood_spike";
    public static final String IRON_SPIKE = "iron_spike";
    public static final String DIAMOND_SPIKE = "diamond_spike";
    public static final String NETHERITE_SPIKE = "netherite_spike";

    private static final Set<EntityType<?>> GOLDEN_LASSO_ENTITIES_WHITELIST = new HashSet<>();
    private static final Set<EntityType<?>> CURSED_LASSO_ENTITIES_WHITELIST = new HashSet<>();
    private static final Set<EntityType<?>> GOLDEN_LASSO_ENTITIES_BLACKLIST = new HashSet<>();
    private static final Set<EntityType<?>> CURSED_LASSO_ENTITIES_BLACKLIST = new HashSet<>();
    private static final Map<String, Integer> SPIKE_DAMAGE_LIST = new HashMap<>();
    public static final Map<String, UnstableAltarRecipeVO> UNSTABLE_ALTAR_RECIPE_MAP = new HashMap<>();

    public static void load(ResourceManager manager) {
        GOLDEN_LASSO_ENTITIES_WHITELIST.clear();
        CURSED_LASSO_ENTITIES_WHITELIST.clear();
        GOLDEN_LASSO_ENTITIES_BLACKLIST.clear();
        CURSED_LASSO_ENTITIES_BLACKLIST.clear();
        SPIKE_DAMAGE_LIST.clear();
        UNSTABLE_ALTAR_RECIPE_MAP.clear();

        manager.findResources("lookingglass/item_config", path -> path.endsWith(".json")).forEach(identifier -> loadItemConfigs(manager, identifier));
    }

    public static boolean canLassoCapture(EntityType<?> entityType, boolean cursed) {
        if (cursed) {
            return CURSED_LASSO_ENTITIES_WHITELIST.contains(entityType) || (CURSED_LASSO_ENTITIES_WHITELIST.isEmpty() && !CURSED_LASSO_ENTITIES_BLACKLIST.contains(entityType));
        }
        return GOLDEN_LASSO_ENTITIES_WHITELIST.contains(entityType) || (GOLDEN_LASSO_ENTITIES_WHITELIST.isEmpty() && !GOLDEN_LASSO_ENTITIES_BLACKLIST.contains(entityType));
    }

    public static int getDamageForSpike(int spikeType) {
        switch (spikeType) {
            case 1:
                return SPIKE_DAMAGE_LIST.get(WOOD_SPIKE);
            case 2:
                return SPIKE_DAMAGE_LIST.get(IRON_SPIKE);
            case 3:
                return SPIKE_DAMAGE_LIST.get(DIAMOND_SPIKE);
            case 4:
                return SPIKE_DAMAGE_LIST.get(NETHERITE_SPIKE);
            default:
                return 0;
        }
    }

    public static void loadItemConfigs(ResourceManager manager, Identifier identifier) {
        ItemConfigType configType = null;
        try (InputStream stream = manager.getResource(identifier).getInputStream()) {
            JsonObject configJson = JsonUtils.fromInputStream(stream);

            configType = ItemConfigType.valueOf(configJson.get("type").getAsString());
            configType.processor.accept(configJson, configType);
        } catch (Exception e) {
            FFLog.error("Error found while loading item config json " + identifier.toString() + "of type " + (configType != null ? configType.toString() : "WARNING: INVALID CONFIG TYPE"), e);
        }
    }

    private static void clearItemConfigs(ItemConfigType type) {
        switch (type) {
            case GOLDEN_LASSO:
                GOLDEN_LASSO_ENTITIES_BLACKLIST.clear();
                GOLDEN_LASSO_ENTITIES_WHITELIST.clear();
                break;
            case CURSED_LASSO:
                CURSED_LASSO_ENTITIES_BLACKLIST.clear();
                CURSED_LASSO_ENTITIES_WHITELIST.clear();
                break;
            case SPIKE_DAMAGE:
                SPIKE_DAMAGE_LIST.clear();
                break;
            case UNSTABLE_ALTAR_RECIPES:
                UNSTABLE_ALTAR_RECIPE_MAP.clear();
        }
    }

    /**
     * This method loads the default damage for the spikes from config.  If the config entries aren't present, it defaults to what I think are optimal.
     *
     * @param parent     The parent Json Object
     * @param configType The config type.
     */
    private static void loadSpikeConfigs(JsonObject parent, ItemConfigType configType) {
        // Default damage for wood spike is 1.
        SPIKE_DAMAGE_LIST.put(WOOD_SPIKE, (parent.has(WOOD_SPIKE)) ? parent.get(WOOD_SPIKE).getAsInt() : 1);
        // Default damage for iron spike is 4.
        SPIKE_DAMAGE_LIST.put(IRON_SPIKE, (parent.has(IRON_SPIKE)) ? parent.get(IRON_SPIKE).getAsInt() : 4);
        // Default damage for diamond spike is 7.
        SPIKE_DAMAGE_LIST.put(DIAMOND_SPIKE, (parent.has(DIAMOND_SPIKE)) ? parent.get(DIAMOND_SPIKE).getAsInt() : 7);
        // Default damage for netherite spike is 14.
        SPIKE_DAMAGE_LIST.put(NETHERITE_SPIKE, (parent.has(NETHERITE_SPIKE)) ? parent.get(NETHERITE_SPIKE).getAsInt() : 14);
    }

    private static void loadUnstableAltarRecipes(JsonObject parent, ItemConfigType configType) {
        if (parent.has("recipes")) {
            parent.getAsJsonArray("recipes").forEach(jsonElement -> {
                UnstableAltarRecipeVO unstableAltarRecipeVO = new UnstableAltarRecipeVO();

                JsonObject jsonObject = (JsonObject) jsonElement;
                JsonArray inputs = jsonObject.getAsJsonArray("inputs");
                inputs.forEach(inputElement -> {
                    String itemId = inputElement.getAsString();
                    unstableAltarRecipeVO.inputList = addItemToList(unstableAltarRecipeVO.inputList, itemId, 1);
                });
                JsonArray outputs = jsonObject.getAsJsonArray("outputs");
                outputs.forEach(outputElement -> {
                    JsonObject outputObject = outputElement.getAsJsonObject();
                    String itemId = outputObject.get("item").getAsString();
                    int count = outputObject.get("count").getAsInt();
                    unstableAltarRecipeVO.outputList = addItemToList(unstableAltarRecipeVO.outputList, itemId, count);
                });

                unstableAltarRecipeVO.instability = jsonObject.get("instability").getAsInt();
                UNSTABLE_ALTAR_RECIPE_MAP.put(unstableAltarRecipeVO.generateKey(), unstableAltarRecipeVO);
            });
        }
    }

    public static List<ItemStack> addItemToList(List<ItemStack> inputList, String itemId, int count) {
        if (StringUtils.isBlank(itemId))
            return inputList;
        if (inputList == null)
            return null;

        Item item = Registry.ITEM.get(Identifier.tryParse(itemId));
        ItemStack itemStack = new ItemStack(item);
        itemStack.setCount(count);
        inputList.add(itemStack);

        return inputList;
    }

    private static void loadLassoConfigs(JsonObject parent, ItemConfigType configType) {
        if (parent.has("whitelist")) {
            parent.getAsJsonArray("whitelist").forEach(jsonElement -> {
                EntityType<?> type = Registry.ENTITY_TYPE.get(Identifier.tryParse(jsonElement.getAsString()));
                switch (configType) {
                    case GOLDEN_LASSO:
                        GOLDEN_LASSO_ENTITIES_WHITELIST.add(type);
                        break;
                    case CURSED_LASSO:
                        CURSED_LASSO_ENTITIES_WHITELIST.add(type);
                        break;
                }
            });
        } else {
            parent.getAsJsonArray("blacklist").forEach(jsonElement -> {
                EntityType<?> type = Registry.ENTITY_TYPE.get(Identifier.tryParse(jsonElement.getAsString()));
                switch (configType) {
                    case GOLDEN_LASSO:
                        GOLDEN_LASSO_ENTITIES_BLACKLIST.add(type);
                        break;
                    case CURSED_LASSO:
                        CURSED_LASSO_ENTITIES_BLACKLIST.add(type);
                        break;
                }
            });
        }
    }

    private enum ItemConfigType {
        GOLDEN_LASSO(LookingGlassJsonManager::loadLassoConfigs),
        CURSED_LASSO(LookingGlassJsonManager::loadLassoConfigs),
        SPIKE_DAMAGE(LookingGlassJsonManager::loadSpikeConfigs),
        UNSTABLE_ALTAR_RECIPES(LookingGlassJsonManager::loadUnstableAltarRecipes);

        private final BiConsumer<JsonObject, ItemConfigType> processor;

        ItemConfigType(BiConsumer<JsonObject, ItemConfigType> processor) {
            this.processor = processor;
        }
    }
}
