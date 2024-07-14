package majik.rereskillable.client;

import majik.rereskillable.common.capabilities.SkillCapability;
import majik.rereskillable.common.capabilities.SkillModel;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientUtils {

    public static SkillModel getClientSkillModel() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            return player.getCapability(SkillCapability.INSTANCE).orElseThrow(() ->
                    new IllegalArgumentException("Player does not have a Skill Model!"));
        }
        throw new IllegalStateException("Minecraft client player is null!");
    }
}
