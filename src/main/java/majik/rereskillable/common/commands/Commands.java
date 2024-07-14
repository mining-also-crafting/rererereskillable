package majik.rereskillable.common.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Class responsible for registering commands for the mod.
 */
public class Commands {

    /**
     * Event handler for registering commands.
     *
     * @param event The event that triggers the command registration.
     */
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("skills")
                        .requires(source -> source.hasPermission(2))
                        .then(SetCommand.register())
                        .then(GetCommand.register())
        );
    }
}
