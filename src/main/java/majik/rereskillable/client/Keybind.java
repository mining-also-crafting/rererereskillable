package majik.rereskillable.client;

import majik.rereskillable.Rereskillable;
import majik.rereskillable.client.screen.SkillScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
@Mod.EventBusSubscriber(modid = Rereskillable.MOD_ID, value = Dist.CLIENT)
public class Keybind
{
    public static final String RESKILLABLE_CATEGORY = "key.reskillable.category";

public static final KeyMapping openKey = new KeyMapping("key.skills", KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, RESKILLABLE_CATEGORY);
    @SubscribeEvent
    public static void keybind(RegisterKeyMappingsEvent event)
    {
        event.register(openKey);
    }

    @SubscribeEvent
    public void onKeyInput(net.minecraftforge.client.event.InputEvent.Key event)
    {
        Minecraft minecraft = Minecraft.getInstance();

        if (openKey.consumeClick())
        {
            minecraft.setScreen(new SkillScreen());
        }
    }
    @SubscribeEvent
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("skill_page", new Overlay());
    }
    @Mod.EventBusSubscriber(modid = Rereskillable.MOD_ID , value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(openKey);
        }
    }
}

