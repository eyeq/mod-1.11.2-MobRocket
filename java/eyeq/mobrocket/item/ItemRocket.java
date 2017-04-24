package eyeq.mobrocket.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemRocket extends Item {
    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker) {
        return launchEntity(itemStack, target, attacker);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        return launchEntity(itemStack, target, player);
    }

    protected boolean launchEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker) {
        World world = target.getEntityWorld();
        if(world.isRemote) {
            return true;
        }
        double x = target.posX;
        double y = target.posY;
        double z = target.posZ;
        EntityTNTPrimed tnt = new EntityTNTPrimed(world, x, y, z, attacker);
        tnt.setFuse(getFuse(itemStack));
        EntityFireworkRocket firework = new EntityFireworkRocket(world, x, y, z, itemStack);
        target.startRiding(tnt);
        tnt.startRiding(firework);

        world.spawnEntity(tnt);
        world.spawnEntity(firework);
        if(!(attacker instanceof EntityPlayer) || !((EntityPlayer) attacker).isCreative()) {
            itemStack.shrink(1);
        }
        return true;
    }

    private int getFuse(ItemStack itemStack) {
        int fuse = 15;
        if(itemStack.hasTagCompound()) {
            NBTTagCompound tag = itemStack.getSubCompound("Fireworks");
            if(tag.hasKey("Flight", 99)) {
                fuse += 10 * tag.getByte("Flight");
            }
        }
        return fuse;
    }
}
