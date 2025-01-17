package majik.rereskillable.client.screen.buttons;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public final class KeyBinding {
    public static final String KEY_CATEGORY = "key.category.rereskillable";
    public static final String OPEN_SKILLS_KEY = "key.rereskillable.open_skills";

    public static final KeyMapping SKILLS_KEY = new KeyMapping
            (OPEN_SKILLS_KEY, KeyConflictContext.IN_GAME,
                    InputConstants.getKey(InputConstants.KEY_G, -1),
                    KEY_CATEGORY
            );
}

