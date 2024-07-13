package majik.rereskillable.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import majik.rereskillable.client.screen.buttons.SkillButton;
import majik.rereskillable.common.commands.skills.Skill;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SkillScreen extends Screen {
    public static final ResourceLocation RESOURCES = new ResourceLocation("rereskillable", "textures/gui/skills.png");

    public SkillScreen() {
        super(Component.translatable("container.skills").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.BLACK));
    }

    @Override
    protected void init() {
        int left = (width - 162) / 2;
        int top = (height - 128) / 2;

        for (int i = 0; i < 8; i++) {
            int x = left + i % 2 * 83;
            int y = top + i / 2 * 36;
            Skill skill = Skill.values()[i];
            int level = getLevel(skill);  // Replace this with actual method to get the player's skill level
            int maxLevel = getMaxLevel(skill);  // Replace this with actual method to get the maximum skill level

            addRenderableWidget(new SkillButton(x, y, skill, level, maxLevel));
        }
    }

    private int getLevel(Skill skill) {
        // Replace this with actual method to get the player's skill level
        return 0;
    }

    private int getMaxLevel(Skill skill) {
        // Replace this with actual method to get the maximum skill level
        return 100;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderTexture(0, RESOURCES);

        int left = (width - 176) / 2;
        int top = (height - 166) / 2;

        renderBackground(guiGraphics);

        guiGraphics.blit(RESOURCES, left, top, 0, 0, 176, 166);
        guiGraphics.drawString(font, title.getVisualOrderText(), width / 2 - font.width(title) / 2, top + 6, 0x3F3F3F, false);

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
