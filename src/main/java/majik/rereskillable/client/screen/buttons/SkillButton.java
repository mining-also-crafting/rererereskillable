package majik.rereskillable.client.screen.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import majik.rereskillable.Configuration;
import majik.rereskillable.client.screen.SkillScreen;
import majik.rereskillable.common.capabilities.SkillModel;
import majik.rereskillable.common.network.RequestLevelUp;
import majik.rereskillable.common.commands.skills.Skill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;



public class SkillButton extends AbstractButton
{
    private final Skill skill;
    
    public SkillButton(int x, int y, Skill skill)
    {
        super(x, y, 79, 32, Component.literal(""));
        
        this.skill = skill;
    }
    
    // Render
    
    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks)
    {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, SkillScreen.RESOURCES);
    
        int level = SkillModel.get().getSkillLevel(skill);
        int maxLevel = Configuration.getMaxLevel();
        
        int u = ((int) Math.ceil((double) level * 4 / maxLevel) - 1) * 16 + 176;
        int v = skill.index * 16 + 128;
        
        blit(stack, x, y, 176, (level == maxLevel ? 64 : 0) + (isMouseOver(mouseX, mouseY) ? 32 : 0), width, height);
        blit(stack, x + 6, y + 8, u, v, 16, 16);
        
        minecraft.font.draw(stack, Component.translatable(skill.displayName), x + 25, y + 7, 0xFFFFFF);
        minecraft.font.draw(stack, level + "/" + maxLevel, x + 25, y + 18, 0xBEBEBE);
        
        if (isMouseOver(mouseX, mouseY) && level < maxLevel)
        {
            int cost = Configuration.getStartCost() + (level - 1) * Configuration.getCostIncrease();
            int colour = minecraft.player.experienceLevel >= cost ? 0x7EFC20 : 0xFC5454;
            String text = Integer.toString(cost);
            
            minecraft.font.drawShadow(stack, text, x + 73 - minecraft.font.width(text), y + 18, colour);
        }
    }
    
    // Press
    
    @Override
    public void onPress()
    {
        RequestLevelUp.send(skill);
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {

    }
}