package majik.rereskillable.common.capabilities;

import majik.rereskillable.common.commands.skills.Requirement;
import majik.rereskillable.common.commands.skills.RequirementType;
import majik.rereskillable.common.commands.skills.Skill;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.client.Minecraft;

public class SkillModel implements INBTSerializable<CompoundTag> {
    public int[] skillLevels = new int[]{1, 1, 1, 1, 1, 1, 1, 1};

    // Get Level for Skill
    public int getSkillLevel(Skill skill) {
        return skillLevels[skill.index];
    }

    // Set Level for Skill
    public void setSkillLevel(Skill skill, int level) {
        skillLevels[skill.index] = level;
    }

    // Increase Level for Skill
    public void increaseSkillLevel(Skill skill) {
        skillLevels[skill.index]++;
    }

    // Can Player Use Item
    public boolean canUseItem(Player player, ItemStack item) {
        return canUse(player, ForgeRegistries.ITEMS.getKey(item.getItem()));
    }

    // Can Player Use Block
    public boolean canUseBlock(Player player, Block block) {
        return canUse(player, ForgeRegistries.BLOCKS.getKey(block));
    }

    // Can Player Use Entity
    public boolean canUseEntity(Player player, Entity entity) {
        return canUse(player, ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()));
    }

    // Can Player Use
    private boolean canUse(Player player, ResourceLocation resource) {
        if (resource == null) {
            return true; // Allow usage if the resource is not found
        }
        return checkRequirements(player, resource, RequirementType.USE);
    }

    private boolean checkRequirements(Player player, ResourceLocation resource, RequirementType type) {
        Requirement[] requirements = type.getRequirements(resource);
        if (requirements != null) {
            for (Requirement requirement : requirements) {
                if (getSkillLevel(requirement.skill) < requirement.level) {
                    if (player instanceof ServerPlayer) {
                        player.sendSystemMessage(Component.literal("Not skilled enough to use this item!"));
                    }
                    return false;
                }
            }
        }
        return true;
    }

    // Get Player Skills
    public static SkillModel get(Player player) {
        return player.getCapability(SkillCapability.INSTANCE).orElseThrow(() ->
                new IllegalArgumentException("Player " + player.getName().getString() + " does not have a Skill Model!")
        );
    }

    // Get Local Player Skills
    public static SkillModel get() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            return player.getCapability(SkillCapability.INSTANCE).orElseThrow(() ->
                    new IllegalArgumentException("Player does not have a Skill Model!")
            );
        }
        throw new IllegalArgumentException("Minecraft instance does not have a player!");
    }

    // Serialize and Deserialize
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("mining", skillLevels[0]);
        compound.putInt("gathering", skillLevels[1]);
        compound.putInt("attack", skillLevels[2]);
        compound.putInt("defense", skillLevels[3]);
        compound.putInt("building", skillLevels[4]);
        compound.putInt("farming", skillLevels[5]);
        compound.putInt("agility", skillLevels[6]);
        compound.putInt("magic", skillLevels[7]);

        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        skillLevels[0] = nbt.getInt("mining");
        skillLevels[1] = nbt.getInt("gathering");
        skillLevels[2] = nbt.getInt("attack");
        skillLevels[3] = nbt.getInt("defense");
        skillLevels[4] = nbt.getInt("building");
        skillLevels[5] = nbt.getInt("farming");
        skillLevels[6] = nbt.getInt("agility");
        skillLevels[7] = nbt.getInt("magic");
    }

    public boolean canCraftItem(Player player, ItemStack stack) {
        ResourceLocation resource = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (resource == null) {
            return true; // Allow crafting if the resource is not found
        }
        return checkRequirements(player, resource, RequirementType.CRAFT);
    }

    public boolean canAttackEntity(Player player, Entity target) {
        ResourceLocation resource = ForgeRegistries.ENTITY_TYPES.getKey(target.getType());
        if (resource == null) {
            return true; // Allow attacking if the resource is not found
        }
        return checkRequirements(player, resource, RequirementType.ATTACK);
    }
}
