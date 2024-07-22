package majik.rereskillable.common.network;

import com.google.gson.Gson;
import majik.rereskillable.Configuration;
import majik.rereskillable.Rereskillable;
import majik.rereskillable.common.commands.skills.Requirement;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class SyncSkillConfigPacket {
    private final Map<String, Requirement[]> skillLocks;
    private final Map<String, Requirement[]> craftSkillLocks;
    private final Map<String, Requirement[]> attackSkillLocks;

    public SyncSkillConfigPacket(Map<String, Requirement[]> skillLocks, Map<String, Requirement[]> craftSkillLocks, Map<String, Requirement[]> attackSkillLocks) {
        this.skillLocks = skillLocks;
        this.craftSkillLocks = craftSkillLocks;
        this.attackSkillLocks = attackSkillLocks;
    }

    public SyncSkillConfigPacket(FriendlyByteBuf buf) {
        String skillLocksJson = buf.readUtf();
        String craftSkillLocksJson = buf.readUtf();
        String attackSkillLocksJson = buf.readUtf();
        Type type = Configuration.getSkillLocksType();
        this.skillLocks = new Gson().fromJson(skillLocksJson, type);
        this.craftSkillLocks = new Gson().fromJson(craftSkillLocksJson, type);
        this.attackSkillLocks = new Gson().fromJson(attackSkillLocksJson, type);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(new Gson().toJson(skillLocks));
        buf.writeUtf(new Gson().toJson(craftSkillLocks));
        buf.writeUtf(new Gson().toJson(attackSkillLocks));
    }

    public static void handle(SyncSkillConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Configuration.setSkillLocks(msg.skillLocks);
            Configuration.setCraftSkillLocks(msg.craftSkillLocks);
            Configuration.setAttackSkillLocks(msg.attackSkillLocks);
        });
        ctx.get().setPacketHandled(true);
    }

    public static void sendToAllClients() {
        Map<String, Requirement[]> skillLocks = Configuration.getSkillLocks();
        Map<String, Requirement[]> craftSkillLocks = Configuration.getCraftSkillLocks();
        Map<String, Requirement[]> attackSkillLocks = Configuration.getAttackSkillLocks();
        SyncSkillConfigPacket packet = new SyncSkillConfigPacket(skillLocks, craftSkillLocks, attackSkillLocks);
        Rereskillable.NETWORK.send(PacketDistributor.ALL.noArg(), packet);
    }
}
