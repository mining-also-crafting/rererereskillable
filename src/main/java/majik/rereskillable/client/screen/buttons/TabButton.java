package majik.rereskillable.client.screen.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import majik.rereskillable.client.screen.SkillScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class TabButton extends AbstractWidget {
    private final boolean selected;
    private final TabType type;

    public TabButton(int x, int y, TabType type, boolean selected) {
        super(x, y, 31, 28, Component.literal("Skill"));
        this.type = type;
        this.selected = selected;
    }

    // Render
    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        active = !(minecraft.screen instanceof InventoryScreen) || !((InventoryScreen) minecraft.screen).getRecipeBookComponent().isVisible();

        if (active) {
            RenderSystem.setShaderTexture(0, SkillScreen.RESOURCES);

            guiGraphics.blit(SkillScreen.RESOURCES, getX(), getY(), selected ? 31 : 0, 166, width, height);
            guiGraphics.blit(SkillScreen.RESOURCES, getX() + (selected ? 8 : 10), getY() + 6, 240, 128 + type.iconIndex * 16, 16, 16);
        }
    }

    // Press
    public void onPress() {
        Minecraft minecraft = Minecraft.getInstance();

        switch (type) {
            case INVENTORY:
                assert minecraft.player != null;
                minecraft.setScreen(new InventoryScreen(minecraft.player));
                break;

            case SKILLS:
                minecraft.setScreen(new SkillScreen());
                break;
        }
    }

    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }

    public enum TabType {
        INVENTORY(0),
        SKILLS(1);

        public final int iconIndex;

        TabType(int index) {
            iconIndex = index;
        }
    }
}
