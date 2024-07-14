package majik.rereskillable.common.capabilities;

import majik.rereskillable.common.network.NotifyWarning;
import majik.rereskillable.common.commands.skills.Requirement;
import majik.rereskillable.common.commands.skills.RequirementType;
import majik.rereskillable.common.commands.skills.Skill;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public class SkillModel implements INBTSerializable<CompoundTag> {
    public int[] skillLevels = new int[]{1, 1, 1, 1, 1, 1, 1, 1};
    public int[] skillExperience = new int[]{0, 0, 0, 0, 0, 0, 0, 0};

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

    // Add Experience to Skill
    public void addExperience(Skill skill, int experience) {
        skillExperience[skill.index] += experience;
        checkForLevelUp(skill);
    }

    // Check for Level Up
    private void checkForLevelUp(Skill skill) {
        int level = skillLevels[skill.index];
        int xp = skillExperience[skill.index];
        while (xp >= experienceToNextLevel(level)) {
            xp -= experienceToNextLevel(level);
            level++;
        }
        skillExperience[skill.index] = xp;
        skillLevels[skill.index] = level;
    }

    // Experience required to level up
    private int experienceToNextLevel(int level) {
        return level * 100; // Example: 100 XP per level
    }

    // Can Player Use Item
    public boolean canUseItem(Player player, ItemStack item) {
        return canUse(player, item.getItem().asItem().builtInRegistryHolder().key().location());
    }

    // Can Player Use Block
    public boolean canUseBlock(Player player, Block block) {
        return !canUse(player, block.builtInRegistryHolder().key().location());
    }

    // Can Player Use Entity
    public boolean canUseEntity(Player player, Entity entity) {
        return canUse(player, entity.getType().builtInRegistryHolder().key().location());
    }

    // Can Player Use
    private boolean canUse(Player player, ResourceLocation resource) {
        return checkRequirements(player, resource, RequirementType.USE);
    }

    private boolean checkRequirements(Player player, ResourceLocation resource, RequirementType type) {
        Requirement[] requirements = type.getRequirements(resource);
        if (requirements != null) {
            for (Requirement requirement : requirements) {
                if (getSkillLevel(requirement.skill) < requirement.level) {
                    if (player instanceof ServerPlayer) {
                        NotifyWarning.send(player, resource, type);
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
                new IllegalArgumentException("Player " + player.getName().getContents() + " does not have a Skill Model!")
        );
    }

    // Get Local Player Skills
    public static SkillModel get() {
        assert Minecraft.getInstance().player != null;
        return Minecraft.getInstance().player.getCapability(SkillCapability.INSTANCE).orElseThrow(() ->
                new IllegalArgumentException("Player does not have a Skill Model!")
        );
    }

    // Serialize and Deserialize
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putIntArray("skillLevels", skillLevels);
        compound.putIntArray("skillExperience", skillExperience);
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        skillLevels = nbt.getIntArray("skillLevels");
        skillExperience = nbt.getIntArray("skillExperience");
    }

    public boolean canCraftItem(Player player, ItemStack stack) {
        ResourceLocation resource = stack.getItem().builtInRegistryHolder().key().location();
        return checkRequirements(player, resource, RequirementType.CRAFT);
    }

    public boolean canAttackEntity(Player player, Entity target) {
        ResourceLocation resource = target.getType().builtInRegistryHolder().key().location();
        return checkRequirements(player, resource, RequirementType.ATTACK);
    }
}
