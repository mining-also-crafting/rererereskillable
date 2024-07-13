package majik.rereskillable.client.screen.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import majik.rereskillable.client.screen.SkillScreen;
import majik.rereskillable.common.capabilities.SkillModel;
import majik.rereskillable.common.commands.skills.Skill;
import majik.rereskillable.Configuration;
import majik.rereskillable.common.network.RequestLevelUp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public class SkillButton extends AbstractWidget {
    private final Skill skill;
    private final int level;
    private final int maxLevel;

    public SkillButton(int x, int y, Skill skill, int level, int maxLevel) {
        super(x, y, 79, 32, Component.literal(""));
        this.skill = skill;
        this.level = level;
        this.maxLevel = maxLevel;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, SkillScreen.RESOURCES);

        int level = SkillModel.get().getSkillLevel(skill);
        int maxLevel = Configuration.getMaxLevel();

        int u = ((int) Math.ceil((double) level * 4 / maxLevel) - 1) * 16 + 176;
        int v = skill.index * 16 + 128;

        guiGraphics.blit(SkillScreen.RESOURCES, getX(), getY(), 176, (level == maxLevel ? 64 : 0) + (isMouseOver(mouseX, mouseY) ? 32 : 0), width, height);
        guiGraphics.blit(SkillScreen.RESOURCES, getX() + 6, getY() + 8, u, v, 16, 16);

        PoseStack poseStack = guiGraphics.pose();
        MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

        Font font = minecraft.font;
        font.drawInBatch(Component.translatable(skill.getDisplayName()), getX() + 25, getY() + 7, 0xFFFFFF, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        font.drawInBatch(Component.literal(level + "/" + maxLevel), getX() + 25, getY() + 18, 0xBEBEBE, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);

        bufferSource.endBatch();

        if (isMouseOver(mouseX, mouseY) && level < maxLevel) {
            int cost = Configuration.getStartCost() + (level - 1) * Configuration.getCostIncrease();
            assert minecraft.player != null;
            int colour = minecraft.player.experienceLevel >= cost ? 0x7EFC20 : 0xFC5454;
            String text = Integer.toString(cost);

            guiGraphics.drawString(font, text, getX() + 73 - font.width(text), getY() + 18, colour);
        }
    }

    public void onPress() {
        RequestLevelUp.send(skill);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }
}
