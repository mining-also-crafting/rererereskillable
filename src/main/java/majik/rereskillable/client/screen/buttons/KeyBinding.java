package majik.rereskillable.client.screen.buttons;

import com.mojang.blaze3d.platform.InputConstants;
import majik.rereskillable.client.screen.SkillScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY = "key.category.rereskillable";
    public static final String OPEN_SKILLS_KEY = "key.rereskillable.open_skills";

    public static final KeyMapping SKILLS_KEY = new KeyMapping(OPEN_SKILLS_KEY, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_N, KEY_CATEGORY);

 }
