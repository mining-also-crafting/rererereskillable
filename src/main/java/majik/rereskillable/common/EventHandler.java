package majik.rereskillable.common;

import majik.rereskillable.Configuration;
import majik.rereskillable.common.capabilities.SkillModel;
import majik.rereskillable.common.capabilities.SkillProvider;
import majik.rereskillable.common.network.SyncToClient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler
{
    // Left Click Block
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event)
    {
        Player player = event.getEntity();
        ItemStack item = event.getItemStack();
        Block block = event.getLevel().getBlockState(event.getPos()).getBlock();
        SkillModel model = SkillModel.get(player);
        
        if (!player.isCreative() && (!model.canUseItem(player, item) || !model.canUseBlock(player, block)))
        {
            event.setCanceled(true);
        }
    }
    
    // Right Click Block
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        Player player = event.getEntity();
        ItemStack item = event.getItemStack();
        Block block = event.getLevel().getBlockState(event.getPos()).getBlock();
        SkillModel model = SkillModel.get(player);
        
        if (!player.isCreative() && (!model.canUseItem(player, item) || !model.canUseBlock(player, block)))
        {
            event.setCanceled(true);
        }
    }
    
    // Right Click Item
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event)
    {
        Player player = event.getEntity();
        ItemStack item = event.getItemStack();
        
        if (!player.isCreative() && !SkillModel.get(player).canUseItem(player, item))
        {
            event.setCanceled(true);
        }
    }
    
    // Right Click Entity
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClickEntity(PlayerInteractEvent.EntityInteract event)
    {
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        ItemStack item = event.getItemStack();
        
        if (!player.isCreative())
        {
            if (!SkillModel.get(player).canUseEntity(player, entity) || !SkillModel.get(player).canUseItem(player, item))
            {
                event.setCanceled(true);
            }
        }
    }
    
    // Attack Entity
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAttackEntity(AttackEntityEvent event)
    {
        Player player = event.getEntity();
        
        if (player != null)
        {
            ItemStack item = player.getMainHandItem();
    
            if (!player.isCreative() && !SkillModel.get(player).canUseItem(player, item))
            {
                event.setCanceled(true);
            }

            if (!player.isCreative() && !SkillModel.get(player).canAttackEntity(player, event.getTarget()))
            {
                event.setCanceled(true);
            }
        }
    }
    
    // Change Equipment
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChangeEquipment(LivingEquipmentChangeEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Player player = (Player) event.getEntity();
            
            if (!player.isCreative() && event.getSlot().getType() == EquipmentSlot.Type.ARMOR)
            {
                ItemStack item = event.getTo();
                
                if (!SkillModel.get(player).canUseItem(player, item))
                {
                    player.drop(item.copy(), false);
                    item.setCount(0);
                }
            }
        }
    }
    
    // Entity Drops
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityDrops(LivingDropsEvent event)
    {
        if (Configuration.getDisableWool() && event.getEntity() instanceof Sheep) {
            event.getDrops().removeIf(item -> item.getItem().is(ItemTags.WOOL));
        }
    }
    
    // Player Death
    
    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            if (Configuration.getDeathReset()){
                SkillModel.get((Player) event.getEntity()).skillLevels = new int[]{1, 1, 1, 1, 1, 1, 1, 1};
            }
            lastDiedPlayerSkills = SkillModel.get((Player) event.getEntity());
        }
    }
    
    // Capabilities
    
    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof Player)
        {
            SkillModel skillModel = new SkillModel();
            SkillProvider provider = new SkillProvider(skillModel);
            
            event.addCapability(new ResourceLocation("rereskillable", "cap_skills"), provider);
        }
    }

    private SkillModel lastDiedPlayerSkills = null;
    
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event)
    {
        SkillModel.get(event.getEntity()).skillLevels = lastDiedPlayerSkills.skillLevels; // SkillModel.get(event.getOriginal()).skillLevels;
    }
    
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e)
    {
        SyncToClient.send(e.getEntity());
    }
    
    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent e)
    {
        SyncToClient.send(e.getEntity());
    }
    
    @SubscribeEvent
    public void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent e)
    {
        if (e.getEntity().level.isClientSide()) return;
        SyncToClient.send(e.getEntity());
    }
}