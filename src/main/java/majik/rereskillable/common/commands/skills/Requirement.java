package majik.rereskillable.common.commands.skills;

public class Requirement {
    public final Skill skill;
    public final int level;

    /**
     * Constructs a new Requirement with the specified skill and level.
     *
     * @param skill The skill required.
     * @param level The level required for the skill.
     */
    public Requirement(Skill skill, int level) {
        this.skill = skill;
        this.level = level;
    }

    /**
     * Returns a string representation of the Requirement.
     * Useful for debugging purposes.
     *
     * @return A string representing the Requirement.
     */
    @Override
    public String toString() {
        return "Requirement{" +
                "skill=" + skill +
                ", level=" + level +
                '}';
    }
}
