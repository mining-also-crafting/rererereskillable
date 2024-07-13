package majik.rereskillable.event;

import majik.rereskillable.client.KeyBindings;
import majik.rereskillable.client.screen.SkillScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "rereskillable", value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.SKILLS_KEY);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (KeyBindings.SKILLS_KEY.consumeClick()) {
            Minecraft.getInstance().setScreen(new SkillScreen());
        }
    }
}
