package majik.rereskillable.client;

import majik.rereskillable.Configuration;
import majik.rereskillable.common.capabilities.SkillModel;
import majik.rereskillable.common.commands.skills.Requirement;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class Tooltip
{
    @SubscribeEvent
    public void onTooltipDisplay(ItemTooltipEvent event)
    {
        if (Minecraft.getInstance().player != null)
        {
            ItemStack stack = event.getItemStack();
            ResourceLocation itemRegistryName = Registry.ITEM.getKey(stack.getItem());
            Requirement[] requirements = Configuration.getRequirements(itemRegistryName);
//            Requirement[] requirements = Configuration.getRequirements(event.getItemStack().getItem().getRegistryName());
    
            if (requirements != null)
            {
                List<Component> tooltips = event.getToolTip();
                tooltips.add(Component.literal(""));
                tooltips.add(Component.translatable("tooltip.requirements").append(":").withStyle(ChatFormatting.GRAY));
        
                for (Requirement requirement : requirements)
                {
                    ChatFormatting colour = SkillModel.get().getSkillLevel(requirement.skill) >= requirement.level ? ChatFormatting.GREEN : ChatFormatting.RED;
                    tooltips.add(Component.translatable(requirement.skill.displayName).append(" " + requirement.level).withStyle(colour));
                }
            }
        }
    }
}