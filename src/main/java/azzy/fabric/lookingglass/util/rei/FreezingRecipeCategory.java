package azzy.fabric.lookingglass.util.rei;

import azzy.fabric.lookingglass.block.LookingGlassBlocks;
import azzy.fabric.lookingglass.item.LookingGlassItems;
import azzy.fabric.lookingglass.recipe.AlloyingRecipe;
import azzy.fabric.lookingglass.recipe.FreezingRecipe;
import azzy.fabric.lookingglass.recipe.LookingGlassRecipes;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FreezingRecipeCategory extends AbstractLookingGlassRecipeCategory<FreezingRecipe> {

    public FreezingRecipeCategory() {
        super(LookingGlassRecipes.FREEZING_RECIPE, EntryStack.create(LookingGlassItems.FREEZER_UPGRADE_ITEM));
    }

    @Override
    public @NotNull List<Widget> setupDisplay(LookingGlassRecipeDisplay<FreezingRecipe> recipeDisplay, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point start = new Point(bounds.getCenterX() - 41, bounds.y + 10);
        widgets.add(Widgets.createCategoryBase(bounds));
        widgets.add(Widgets.createResultSlotBackground(new Point(start.x + 61, start.y + 9)));
        widgets.add(Widgets.createSlot(new Point(start.x + 1, start.y  + 10)).entries(getInput(recipeDisplay, 0)).markInput());
        widgets.add(Widgets.createArrow(new Point(start.x + 24, start.y + 8)).animationDurationTicks(800));
        widgets.add(Widgets.createSlot(new Point(start.x + 61, start.y + 9)).entries(getOutput(recipeDisplay, 0)).disableBackground().markOutput());

        //widgets.add(ReiPlugin.createProgressBar(bounds.x + 46 + 21, bounds.y + 30, recipeDisplay.getTime() * 50, GuiBuilder.ProgressDirection.RIGHT));
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 49;
    }
}
