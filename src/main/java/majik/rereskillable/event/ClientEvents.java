package majik.rereskillable.event;

import majik.rereskillable.Rereskillable;
import majik.rereskillable.client.screen.SkillScreen;
import majik.rereskillable.client.screen.buttons.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Rereskillable.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(KeyBinding.SKILLS_KEY);
    }

    @Mod.EventBusSubscriber(modid = Rereskillable.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            Minecraft minecraft = Minecraft.getInstance();
            if (KeyBinding.SKILLS_KEY.consumeClick()) {
                assert minecraft.player != null;
                minecraft.player.sendSystemMessage(Component.literal("Pressed a Key!"));
                minecraft.setScreen(new SkillScreen());
            }
        }
    }
}
