package majik.rereskillable.common.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import majik.rereskillable.Configuration;
import majik.rereskillable.common.capabilities.SkillModel;
import majik.rereskillable.common.network.SyncToClient;
import majik.rereskillable.common.commands.skills.Skill;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.command.EnumArgument;

/**
 * Command to set the skill level of a player.
 */
public class SetCommand {

    /**
     * Registers the set command with the dispatcher.
     *
     * @return The argument builder for the set command.
     */
    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("set")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("skill", EnumArgument.enumArgument(Skill.class))
                                .then(Commands.argument("level", IntegerArgumentType.integer(1, Configuration.getMaxLevel()))
                                        .executes(SetCommand::execute))));
    }

    /**
     * Executes the set command.
     *
     * @param context The command context.
     * @return A success code.
     * @throws CommandSyntaxException If there is an error in the command syntax.
     */
    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        Skill skill = context.getArgument("skill", Skill.class);
        int level = IntegerArgumentType.getInteger(context, "level");

        SkillModel.get(player).setSkillLevel(skill, level);
        SyncToClient.send(player);

        context.getSource().sendSuccess(() -> Component.translatable(skill.displayName).append(" set to " + level), true);

        return 1;
    }
}
