package majik.rereskillable.common.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import majik.rereskillable.common.capabilities.SkillModel;
import majik.rereskillable.common.commands.skills.Skill;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.command.EnumArgument;

/**
 * Command to get the skill level of a player.
 */
public class GetCommand {

    /**
     * Registers the get command with the dispatcher.
     *
     * @return The argument builder for the get command.
     */
    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("get")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("skill", EnumArgument.enumArgument(Skill.class))
                                .executes(GetCommand::execute)));
    }

    /**
     * Executes the get command.
     *
     * @param context The command context.
     * @return The skill level of the player.
     * @throws CommandSyntaxException If there is an error in the command syntax.
     */
    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        Skill skill = context.getArgument("skill", Skill.class);
        int level = SkillModel.get(player).getSkillLevel(skill);

        context.getSource().sendSuccess(() -> Component.translatable(skill.displayName).append(" " + level), true);

        return level;
    }
}
