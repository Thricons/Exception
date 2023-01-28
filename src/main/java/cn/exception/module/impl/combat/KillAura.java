package cn.exception.module.impl.combat;

import cn.exception.Exception;
import cn.exception.event.Event;
import cn.exception.event.EventTarget;
import cn.exception.event.events.EventUpdate;
import cn.exception.module.Module;
import cn.exception.module.impl.player.Teams;
import cn.exception.module.value.Mode;
import cn.exception.module.value.Numbers;
import cn.exception.module.value.Option;
import cn.exception.utils.MathUtil;
import cn.exception.utils.MoveUtil;
import cn.exception.utils.RotationUtil;
import cn.exception.utils.TimerUtil;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * @author MiLiBlue
 **/
public class KillAura extends Module {
    private final TimerUtil timer;
    public static EntityLivingBase curTarget;
    public static EntityLivingBase blockTarget;
    public Mode hand;
    public static ArrayList<EntityLivingBase> targetList;
    private int index;

    private final TimerUtil blockPreventFlagTimer;

    public static EntityLivingBase vip;
    public static float[] facing;

    public static float[] facing12;

    public static final float lastYaw;
    public static final float lastPitch;
    private final Numbers<Double> MaxCPS;
    private final Numbers<Double> MinCPS;
    public static final Option playerValue;
    public static final Option animalValue;
    public static final Option monsterValue;
    public static final Option neutralValue;
    public static final Option preferValue;
    public static final Option deathValue;
    public static final Option invisibleValue;
    public static final Option throughWallValue;
    public static final Option preventFlagValue;
    public static final Option autoBlockValue;
    public static final Option raycateValue;
    public static final Option ncpRotationsProperty;
    public static final Option attackKey;
    public static final Option blockHit;
    public static final Option suffix;
    public static final Option randomValue;
    public final Numbers<Number> switchDelayValue;
    public final Numbers<Number> rangeValue;
    public final Numbers<Number> swingrangeValue;
    public final Numbers<Number> aimValue;
    public final Numbers<Number> blockRangeValue;
    public final Numbers<Number> wallRangeValue;
    public final Numbers<Number> fovValue;
    public final Numbers<Number> angleValue;
    private final Numbers<Double> crack;
    public float fallDis;
    public boolean fall;
    //  private boolean CRIT;
    public double y;
    private final Mode Priority;
    private final Mode mode;
    public static boolean blocking;
    public Option HeadRotation;
    public static float sYaw;
    public static float sPitch;
    public static float aacB;
    int[] randoms;
    public boolean ready;
    private final TimerUtil switchTimer;
    private final Numbers<Double> PreFerCps;
    public static Mode block = new Mode("BlockMode", new String[]{"Default", "Fake"}, "Default");
    static Minecraft mc = Minecraft.getMinecraft();

    static {
        lastYaw = 0.0f;
        lastPitch = 0.0f;
        playerValue = new Option("Player", true);
        animalValue = new Option("Animal", false);
        monsterValue = new Option("Monster", false);
        neutralValue = new Option("Neutral", false);
        preferValue = new Option("Prefer", false);
        deathValue = new Option("Death", true);
        invisibleValue = new Option("Invisible", false);
        throughWallValue = new Option("ThroughWall", false);
        preventFlagValue = new Option("Flag", true);
        autoBlockValue = new Option("AutoBlock", false);
        raycateValue = new Option("Raycast", false);
        ncpRotationsProperty = new Option("NCPRots", true);
        attackKey = new Option("On attack key", false);
        blockHit = new Option("Digging check", false);
        suffix = new Option("suffix", true);
        randomValue = new Option("Random", false);
    }

    public KillAura(){
        super("KillAura", Category.Combat);
        timer = new TimerUtil();
        hand = new Mode("Rotation", new String[]{"Slient", "Hypixel", "NorotSet", "Vlamy", "Zenith", "Exhibition", "AAC"}, "Hypixel");
        targetList = new ArrayList<>();
        blockPreventFlagTimer = new TimerUtil();
        MaxCPS = new Numbers<>("Max", 17.0, 1.0, 20.0, 1.0);
        MinCPS = new Numbers<>("Min", 6.0, 1.0, 20.0, 1.0);
        switchDelayValue = new Numbers<>("SwitchDelay", 15.0, 0.5, 20.0, 0.5);
        rangeValue = new Numbers<>("Range", 4.2, 1.0, 8.0, 0.05);
        swingrangeValue = new Numbers<>("SwingRange", 4.2, 1.0, 10.0, 0.05);
        aimValue = new Numbers<>("Aim", 1.6, 0.0, 1.6, 0.1);
        blockRangeValue = new Numbers<>("BlockRange", 5.0, 1.0, 15.0, 0.05);
        wallRangeValue = new Numbers<>("WallRange", 4.2, 1.0, 5.0, 0.05);
        fovValue = new Numbers<>("Fov", 180.0, 10.0, 180.0, 10.0);
        angleValue = new Numbers<>("AngleStep", 180.0, 0.0, 180.0, 1.0);
        crack = new Numbers<>("CrackSize", 1.0, 0.0, 10.0, 1.0);
        Priority = new Mode("Priority", new String[]{"Distance", "Health", "Fov"}, "Distance");
        mode = new Mode("Mode", new String[]{"Single", "Switch", "Multi"}, "Switch");
        HeadRotation = new Option("HeadRotation", false);
        randoms = new int[]{0, 1, 0};
        switchTimer = new TimerUtil();
        PreFerCps = new Numbers<>("PreferCps", 17.0, 1.0, 100.0, 1.0);
        addValues(hand, mode, block, Priority, aimValue, MaxCPS, MinCPS, PreFerCps, fovValue, angleValue, swingrangeValue, switchDelayValue, wallRangeValue, blockRangeValue, rangeValue, crack, playerValue, animalValue, monsterValue, neutralValue, preferValue, deathValue, invisibleValue, preventFlagValue, throughWallValue, autoBlockValue, HeadRotation, attackKey, blockHit, raycateValue, ncpRotationsProperty, randomValue);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        targetList.clear();
        curTarget = null;
        blockTarget = null;
        if (autoBlockValue.getValue() && blocking) {
            unblock(true);
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        targetList.clear();
        curTarget = null;
        blockTarget = null;

        if (mc.thePlayer != null) {

            sYaw = mc.thePlayer.rotationYaw;

            sPitch = mc.thePlayer.rotationPitch;
        }

        if(mc.thePlayer != null) {
            blocking = mc.thePlayer.isBlocking();
        }
        blockPreventFlagTimer.reset();
        aacB = 0.0f;
        timer.reset();
        index = 0;
        switchTimer.reset();
    }

    protected static final Vec3 getVectorForRotation(float pitch, float yaw)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }

    private static boolean isLookingAtEntity(final float yaw, final float pitch, final Entity entity, final double range, final boolean rayTrace) {
        final EntityPlayerSP player = mc.thePlayer;
        final Vec3 src = mc.thePlayer.getPositionEyes(1.0f);
        final Vec3 rotationVec = getVectorForRotation(pitch, yaw);
        final Vec3 dest = src.addVector(rotationVec.xCoord * range, rotationVec.yCoord * range, rotationVec.zCoord * range);
        final MovingObjectPosition obj = mc.theWorld.rayTraceBlocks(src, dest, false, false, true);
        if (obj == null) {
            return false;
        }
        if (obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            if (rayTrace) {
                return false;
            }
            if (player.getDistanceToEntity(entity) > 3.0) {
                return false;
            }
        }
        return entity.getEntityBoundingBox().expand(0.10000000149011612, 0.10000000149011612, 0.10000000149011612).calculateIntercept(src, dest) != null;
    }

    private static boolean hasSword(final EntityPlayer playerIn) {
        return playerIn.inventory.getCurrentItem() != null && playerIn.inventory.getCurrentItem().getItem() instanceof ItemSword;
    }

    public static long randomClickDelay(final int minCPS, final int maxCPS) {
        return (long) (Math.random() * (1000 / minCPS - 1000 / maxCPS + 1) + 1000 / maxCPS);
    }

    private boolean shouldAttack() {
        if (curTarget instanceof EntityWither) {
            return timer.hasReached(0 / (int) (Object) PreFerCps.getValue());
        }
        return timer.hasReached(randomClickDelay((int) (Object) MinCPS.getValue().intValue(), (int) (Object) MaxCPS.getValue().intValue()));
    }

    private static int randomNumber(final double min, final double max) {
        final Random random = new Random();
        return (int) (min + random.nextDouble() * (max - min));
    }

    @EventTarget
    public void onUpdate(EventUpdate e){
        if(e.getType() == Event.Type.PRE){
            if (suffix.getValue()) {
                setSuffix("Priority");
            } else {
                final int aps = randomNumber((int) (Object) MaxCPS.getValue(), (int) (Object) MinCPS.getValue());
                getRotations(curTarget);
                if (curTarget != null) {
                    setSuffix(aps + " (100" + "%)");
                } else {
                    setSuffix("0 (0%)");
                }
            }
        }
    }

    private void updateTargets() {
        targetList.clear();
        final List entities = mc.theWorld.loadedEntityList;
        for (int entitiesSize = entities.size(), i = 0; i < entitiesSize; ++i) {
            final Entity entity = (Entity) entities.get(i);
            final EntityLivingBase entityLivingBase;
            if (entity instanceof EntityLivingBase && getEntityValid(entityLivingBase = (EntityLivingBase) entity)) {
                targetList.add(entityLivingBase);
            }
        }
    }

    @EventTarget
    public void onPre(EventUpdate event){
        if(event.getType() == Event.Type.PRE){
            blockTarget = null;
            updateTargets();
            Label_0169:
            {
                if (autoBlockValue.getValue().equals(false)) {
                    if (KillAura.mc.gameSettings.keyBindAttack.isKeyDown() || !attackKey.getValue()) {
                        if (blockHit.getValue()) {

                            if (mc.playerController.getIsHittingBlock()) {
                                return;
                            }
                        }
                        if (!isBlocking()) {
                            break Label_0169;
                        }

                        if (MoveUtil.isMoving()) {
                            KillAura.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        }
                        break Label_0169;
                    }
                    return;
                }
            }

            if (mc.thePlayer.ticksExisted <= 1 && deathValue.getValue()) {
                //  NotificationPublisher.queue("Disabled KillAura", "Disabled aura due to death.", NotificationType.WARNING);
                setEnable(false);
                return;
            }
            blockTarget = null;

            for (final Entity entity : mc.theWorld.getLoadedEntityList()) {
                if (!autoBlockValue.getValue()) {
                    break;
                }
                if (!(entity instanceof EntityLivingBase)) {
                    continue;
                }
                EntityLivingBase livingBase = (EntityLivingBase) entity;
                if (!getEntityValidBlock(livingBase)) {
                    continue;
                }
                blockTarget = livingBase;
            }
            curTarget = null;
            targetList.clear();
            clearcurTargets();

            for (final Entity entity : mc.theWorld.getLoadedEntityList()) {
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase livingBase = (EntityLivingBase) entity;
                    if (!getEntityValid(livingBase)) {
                        continue;
                    }
                    if (vip == livingBase) {
                        targetList.clear();
                        targetList.add(livingBase);
                        break;
                    }
                    targetList.add(livingBase);
                }
            }
            sortList(targetList);
            if (preferValue.getValue()) {
                for (final EntityLivingBase entityLivingBase : targetList) {
                    if (entityLivingBase instanceof EntityWither) {
                        targetList.clear();
                        targetList.add(entityLivingBase);
                        break;
                    }
                }
            }
            if (switchTimer.delay(switchDelayValue.getValue().intValue() * 100L) && targetList.size() > 1) {
                switchTimer.reset();
                ++index;
            }
            if (index >= targetList.size()) {
                index = 0;
            }
            if (!targetList.isEmpty()) {
                curTarget = targetList.get((mode.isCurrentMode("Switch")) ? index : 0);
            }
            if (curTarget != null) {
                Label_0710:
                {
                    if (autoBlockValue.getValue().equals(false)) {
                        if (KillAura.mc.gameSettings.keyBindAttack.isKeyDown() || !attackKey.getValue()) {
                            if (!blockHit.getValue()) {
                                break Label_0710;
                            }

                            if (!mc.playerController.getIsHittingBlock()) {
                                break Label_0710;
                            }
                        }
                        return;
                    }
                }

                if (hand.isCurrentMode("Vlamy")) {
                    final float[] rots = getRotations(curTarget, event);
                    event.setYaw(rots[0]);
                    event.setPitch(rots[1]);
                    //  RendererLivingEntity.SetPitchY(rots[1]);
                }
                if (hand.isCurrentMode("AAC")) {
                    final float[] rots = getRotations1(curTarget);
                    event.setYaw(rots[0]);
                    event.setPitch(rots[1]);
                    // RendererLivingEntity.SetPitchY(rots[1]);
                }
                if (hand.isCurrentMode("Hypixel")) {
                    final float[] rotations1 = ncpRotationsProperty.getValue() ? getRotations2(curTarget) : getRotations(curTarget, event.getYaw(), event.getPitch(), (float) (Object) angleValue.getValue());
                    final float yaw = rotations1[0];
                    final float pitch = rotations1[1];
                    event.setYaw(yaw);
                    event.setPitch(pitch);

                    mc.thePlayer.rotationYawHead = yaw;

                    //  RendererLivingEntity.SetPitchY(mc.thePlayer.rotationPitchHead = pitch);
                }
                if (hand.isCurrentMode("Zenith")) {
                    final float[] rots = customRots(event);
                    aacB /= 2.0f;
                    event.setYaw(rots[0]);
                    event.setPitch(rots[1]);
                    //RendererLivingEntity.SetPitchY(rots[1]);
                }
                if (hand.isCurrentMode("Exhibition")) {
                    final float[] rotations2 = RotationUtil.getRotations(curTarget);
                    // MathHelper.clamp_float(RotationUtil.getYawChangeGiven(curTarget.posX, curTarget.posZ, lastAngles.x) + randomNumber(-5.0, 5.0), -180.0f, 180.0f);
                    facing = RotationUtil.getRotations(curTarget);
                    if (facing.length > 0) {
                        event.setYaw(facing[0] + new Random().nextInt(2) - 1.0f);
                        event.setPitch(facing[1]);
                    }
                    //RendererLivingEntity.SetPitchY(rotations2[1]);
                }
                if (hand.isCurrentMode("Slient")) {
                    final float[] rotations1 = ncpRotationsProperty.getValue() ? getRotations2(curTarget) : getRotations(curTarget, event.getYaw(), event.getPitch(), (float) (Object) angleValue.getValue());
                    final float yaw = rotations1[0];
                    final float pitch = rotations1[1];
                    event.setYaw(yaw);
                    event.setPitch(pitch);

                    mc.thePlayer.rotationYawHead = yaw;
                    // RendererLivingEntity.SetPitchY(KillAura.facing12[1]);
                }
            }
            if (autoBlockValue.getValue()) {
                if (blockTarget != null) {

                    if (hasSword(mc.thePlayer)) {
                        if (!blocking) {
                            blockPreventFlagTimer.reset();
                        } else if (blockPreventFlagTimer.delay(30L) && preventFlagValue.getValue()) {
                            unblock(false);
                            blockPreventFlagTimer.reset();
                        }
                    } else if (blocking) {
                        unblock(true);
                    }
                } else if (blocking) {
                    unblock(true);
                }
            }
        }
    }

    private static float[] getRotations(final Entity entity, final float prevYaw, final float prevPitch, final float aimSpeed) {
        final EntityPlayerSP player = mc.thePlayer;
        final double xDist = entity.posX - player.posX;
        final double zDist = entity.posZ - player.posZ;
        final AxisAlignedBB entityBB = entity.getEntityBoundingBox().expand(0.10000000149011612, 0.10000000149011612, 0.10000000149011612);
        final double playerEyePos = player.posY + player.getEyeHeight();
        final double yDist = (playerEyePos > entityBB.maxY) ? (entityBB.maxY - playerEyePos) : ((playerEyePos < entityBB.minY) ? (entityBB.minY - playerEyePos) : 0.0);
        final double fDist = MathHelper.sqrt_double(xDist * xDist + zDist * zDist);
        final float yaw = interpolateRotation(prevYaw, (float) (StrictMath.atan2(zDist, xDist) * 57.29577951308232) - 90.0f, aimSpeed);
        final float pitch = interpolateRotation(prevPitch, (float) (-(StrictMath.atan2(yDist, fDist) * 57.29577951308232)), aimSpeed);
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationFromPosition(final double x, final double z, final double y) {

        final double xDiff = x - mc.thePlayer.posX;

        final double zDiff = z - mc.thePlayer.posZ;

        final double yDiff = y - mc.thePlayer.posY - 1.2;
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[]{yaw, pitch};
    }

    private static float[] getRotations2(final Entity entity) {
        final EntityPlayerSP player = mc.thePlayer;
        final double xDist = entity.posX - player.posX;
        final double zDist = entity.posZ - player.posZ;
        double yDist = entity.posY - player.posY;
        final double dist = StrictMath.sqrt(xDist * xDist + zDist * zDist);
        final AxisAlignedBB entityBB = entity.getEntityBoundingBox().expand(0.10000000149011612, 0.10000000149011612, 0.10000000149011612);
        final double playerEyePos = player.posY + player.getEyeHeight();
        final boolean close = dist < 2.0 && Math.abs(yDist) < 2.0;
        float pitch;
        if (close && playerEyePos > entityBB.minY) {
            pitch = 60.0f;
        } else {
            yDist = ((playerEyePos > entityBB.maxY) ? (entityBB.maxY - playerEyePos) : ((playerEyePos < entityBB.minY) ? (entityBB.minY - playerEyePos) : 0.0));
            pitch = (float) (-(StrictMath.atan2(yDist, dist) * 57.29577951308232));
        }
        float yaw = (float) (StrictMath.atan2(zDist, xDist) * 57.29577951308232) - 90.0f;
        if (close) {
            final int inc = (dist < 1.0) ? 180 : 90;
            yaw = Math.round(yaw / inc) * inc;
        }
        return new float[]{yaw, pitch};
    }

    public float[] customRots(final EventUpdate em) {
        final double randomYaw = 0.05;
        final double randomPitch = 0.05;
        final float[] rotsN = getCustomRotsChange(sYaw, sPitch, curTarget.posX + randomNumber(5.0, -5.0) * randomYaw, curTarget.posY + randomNumber(5.0, -5.0) * randomPitch, curTarget.posZ + randomNumber(5.0, -5.0) * randomYaw);
        final float targetYaw = rotsN[0];
        float yawFactor = targetYaw * targetYaw / (4.7f * targetYaw);
        if (targetYaw < 5.0f) {
            yawFactor = targetYaw * targetYaw / (3.7f * targetYaw);
        }
        if (Math.abs(yawFactor) > 7.0f) {
            aacB = yawFactor * 7.0f;
            yawFactor = targetYaw * targetYaw / (3.7f * targetYaw);
        } else {
            yawFactor = targetYaw * targetYaw / (6.7f * targetYaw) + aacB;
        }
        em.setYaw(sYaw + yawFactor);
        sYaw += yawFactor;
        final float targetPitch = rotsN[1];
        final float pitchFactor = targetPitch / 3.7f;
        em.setPitch(sPitch + pitchFactor);
        sPitch += pitchFactor;
        return new float[]{sYaw, sPitch};
    }

    public float[] getCustomRotsChange(final float yaw, final float pitch, double x, double y, double z) {
        final double n = x;

        double xDiff = n - mc.thePlayer.posX;
        final double n2 = z;

        double zDiff = n2 - mc.thePlayer.posZ;

        double yDiff;
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        double mult = 1.0 / (dist + 1.0E-4) * 2.0;
        if (mult > 0.2) {
            mult = 0.2;
        }

        final WorldClient theWorld = mc.theWorld;

        final EntityPlayerSP thePlayer = mc.thePlayer;

        if (!theWorld.getEntitiesWithinAABBExcludingEntity(thePlayer, mc.thePlayer.getEntityBoundingBox()).contains(curTarget)) {
            x += 0.3 * randoms[0];
            y -= 0.4 + mult * randoms[1];
            z += 0.3 * randoms[2];
        }
        final double n4 = x;

        xDiff = n4 - mc.thePlayer.posX;
        final double n5 = z;

        zDiff = n5 - mc.thePlayer.posZ;
        final double n6 = y;

        yDiff = n6 - mc.thePlayer.posY;
        final float yawToEntity = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitchToEntity = (float) (-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[]{MathHelper.wrapAngleTo180_float(-(yaw - yawToEntity)), -MathHelper.wrapAngleTo180_float(pitch - pitchToEntity) - 2.5f};
    }

    public float[] getRotations(final EntityLivingBase e, final EventUpdate event) {
        final float[] targetYaw = getRotations(e);
        float yaw;
        final float speed = (float) ThreadLocalRandom.current().nextDouble(2.0, 2.7);
        final float yawDifference = event.getYaw() - targetYaw[0];
        yaw = event.getYaw() - yawDifference / speed;
        float pitch;
        final float pitchDifference = event.getPitch() - targetYaw[1];
        pitch = event.getPitch() - pitchDifference / speed;
        return new float[]{yaw, pitch};
    }

    public static float[] getRotations1(final EntityLivingBase e) {
        final float[] rots = RotationUtil.getRotations(e);
        return new float[]{rots[0] + randomNumber(3.0, -3.0), rots[1] + randomNumber(3.0, -3.0)};
    }

    public float[] getRotationsNeededBlock(final double x, final double y, final double z) {
        final double n = x + 0.5;

        final double diffX = n - mc.thePlayer.posX;
        final double n2 = z + 0.5;

        final double diffZ = n2 - mc.thePlayer.posZ;

        final double diffY = y - mc.thePlayer.posY - 0.2;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        final float[] array = new float[2];
        final int n3 = 0;

        array[n3] = mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - (float) angleValue.getValue().doubleValue());
        final int n4 = 1;

        final float rotationPitch = mc.thePlayer.rotationPitch;

        array[n4] = rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch);
        return array;
    }

    public static float[] getNeededRotations(final Vec3 vec) {

        final double posX = mc.thePlayer.posX;

        final double posY = mc.thePlayer.posY;

        final double y = posY + mc.thePlayer.getEyeHeight();

        final Vec3 eyesPos = new Vec3(posX, y, mc.thePlayer.posZ);
        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch)};
    }

    private static float interpolateRotation(final float prev, final float now, final float maxTurn) {
        float var4 = MathHelper.wrapAngleTo180_float(now - prev);
        if (var4 > maxTurn) {
            var4 = maxTurn;
        }
        if (var4 < -maxTurn) {
            var4 = -maxTurn;
        }
        return prev + var4;
    }

    public float[] getRotations(final Entity entity) {
        if (entity == null) {
            return null;
        }

        final double posX = entity.posX;

        final double diffX = posX - mc.thePlayer.posX;

        final double posZ = entity.posZ;

        final double diffZ = posZ - mc.thePlayer.posZ;
        double diffY;
        if (randomValue.getValue()) {
            final double posY = entity.posY;

            final double posY2 = mc.thePlayer.posY;

            diffY = posY - (posY2 + mc.thePlayer.getEyeHeight()) + MathUtil.randomDouble(0.0, 1.6);
        } else {
            final double posY3 = entity.posY;

            final double posY4 = mc.thePlayer.posY;

            diffY = posY3 - (posY4 + mc.thePlayer.getEyeHeight()) + aimValue.getValue().doubleValue();
        }
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public float[] ExgetHypixelRotationsNeededBlock(final double x2, final double y2, final double z2) {

        final double n = x2 + 0.5;

        final double diffX = n - mc.thePlayer.posX;

        final double n2 = z2 + 0.5;

        final double diffZ = n2 - mc.thePlayer.posZ;


        final double diffY = y2 - mc.thePlayer.posY - 0.2;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        final float[] arrf = new float[2];

        final int n3 = 0;
        arrf[n3] = mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - (float) angleValue.getValue().doubleValue());

        final int n4 = 1;
        final float rotationPitch = mc.thePlayer.rotationPitch;
        arrf[n4] = rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch);
        return arrf;
    }

    public static float[] ExgetRotationFromPosition(final double x, final double z, final double y) {

        final double xDiff = x - mc.thePlayer.posX;

        final double zDiff = z - mc.thePlayer.posZ;

        final double yDiff = y - mc.thePlayer.posY - 1.2;
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[]{yaw, pitch};
    }

    private static void block(final boolean setItemInUseCount) {
        if(block.isCurrentMode("Default")) {
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getItemStack()));
            if (setItemInUseCount) {
               // mc.thePlayer.itemInUseCount = (mc.thePlayer.inventory.getCurrentItem().getMaxItemUseDuration());
                mc.thePlayer.setItemInUse(mc.thePlayer.getItemInUse(), mc.thePlayer.inventory.getCurrentItem().getMaxItemUseDuration());
            }
        }else if(block.isCurrentMode("Fake")){
            //Nothing -> see ItemRenderer
        }
        blocking = true;
    }

    private static void unblock(final boolean setItemInUseCount) {
        if(block.isCurrentMode("Default")) {
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
            if (setItemInUseCount) {
                mc.thePlayer.setItemInUse(mc.thePlayer.getItemInUse(), 0);
            }
        }else if(block.isCurrentMode("Fake")){
            //Nothing -> see ItemRenderer
        }
        blocking = false;
    }

    public void clearcurTargets() {
        curTarget = null;
        targetList.clear();
    }

    private static boolean isBlocking() {
        if (mc.thePlayer.getItemInUseCount() > 0.0F && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            return mc.thePlayer.isBlocking() && !blocking;
        }
        return false;
    }

    public static boolean isMoving() {

        if (!mc.thePlayer.isCollidedHorizontally) {

            if (!mc.thePlayer.isSneaking()) {

                if (mc.thePlayer.moveForward == 0.0f) {

                    return mc.thePlayer.moveStrafing != 0.0f;
                }
                return true;
            }
        }
        return false;
    }

    @EventTarget
    public void onPost(EventUpdate e){
        if(e.getType() == Event.Type.PRE){
            final int crackSize = (int) (Object) crack.getValue().intValue();
            Label_0138:
            {
                if (autoBlockValue.getValue().equals(false)) {
                    if (KillAura.mc.gameSettings.keyBindAttack.isKeyDown() || !attackKey.getValue()) {
                        if (blockHit.getValue()) {

                            if (mc.playerController.getIsHittingBlock()) {
                                return;
                            }
                        }
                        if (!isBlocking()) {
                            break Label_0138;
                        }

                        if (MoveUtil.isMoving()) {
                            final NetHandlerPlayClient netHandler = KillAura.mc.getNetHandler();

                            netHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                        }
                        break Label_0138;
                    }
                    return;
                }
            }

            if (curTarget != null && shouldAttack()) {
                Label_0242:
                {
                    if (autoBlockValue.getValue()) {

                        if (hasSword(mc.thePlayer)) {
                            if (!blocking) {

                                if (!mc.thePlayer.isBlocking()) {
                                    break Label_0242;
                                }
                            }
                            unblock(false);
                        }
                    }
                }
                attack(e);
                timer.reset();
            }
            if (autoBlockValue.getValue() && blockTarget != null) {

                if (hasSword(mc.thePlayer) && !blocking) {
                    block(true);
                }
            }
            for (int i2 = 0; i2 < crackSize; ++i2) {
                //KillAura.mc.effectRenderer.emitParticleAtEntity(curTarget, EnumParticleTypes.CRIT);
                //KillAura.mc.effectRenderer.emitParticleAtEntity(curTarget, EnumParticleTypes.CRIT_MAGIC);
            }
        }
    }

    private void attack(final EventUpdate e) {
        if (autoBlockValue.getValue().equals(false) && !KillAura.mc.gameSettings.keyBindAttack.isKeyDown() && attackKey.getValue()) {
            return;
        }

        if (ncpRotationsProperty.getValue() || isLookingAtEntity(e.getYaw(), e.getPitch(), curTarget, rangeValue.getValue().doubleValue(), raycateValue.getValue())) {
            mc.thePlayer.swingItem();
            mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(curTarget, C02PacketUseEntity.Action.ATTACK));
        }
    }

    private boolean getEntityValid(final EntityLivingBase entity) {

        if (mc.thePlayer.isEntityAlive()) {

            if (!mc.thePlayer.isPlayerSleeping()) {

                if (!mc.thePlayer.isDead) {

                    if (mc.thePlayer.getHealth() > 0.0f) {

                        if (mc.thePlayer.getDistanceToEntity(entity) <= rangeValue.getValue().floatValue() + 0.4 && entity.isEntityAlive() && !entity.isDead && entity.getHealth() > 0.0f && !(entity instanceof EntityArmorStand) && !AntiBot.isBot(entity)) {

                            if (entity != mc.thePlayer) {
                                if (entity instanceof EntityPlayer && isOnTeam(entity)) {

                                    if (Exception.instance.moduleManager.getModuleByClass(Teams.class).isEnable()) {
                                        return false;
                                    }
                                }
//                                if (FriendManager.isFriend(entity.getName())) {
//                                    return false;
//                                }
                                if (entity instanceof EntityPlayer) {
                                    EntityPlayer player = (EntityPlayer) entity;
                                    if (!playerValue.getValue()) {
                                        return false;
                                    }
                                    if (player.isPlayerSleeping()) {
                                        return false;
                                    }
                                    boolean b;
                                    Label_0283:
                                    {
                                        if (throughWallValue.getValue()) {

                                            if (mc.thePlayer.getDistanceToEntity(player) <= wallRangeValue.getValue().floatValue() + 0.5f) {
                                                b = false;
                                                break Label_0283;
                                            }
                                        }
                                        b = true;
                                    }
                                    final boolean wallChecks = b;
                                    if (!mc.thePlayer.canEntityBeSeen(player) && wallChecks) {
                                        return false;
                                    }
                                    if (player.isPotionActive(Potion.invisibility) && !invisibleValue.getValue()) {
                                        return false;
                                    }
                                }
                                if (entity instanceof EntityAnimal) {
                                    return animalValue.getValue();
                                }
                                if (entity instanceof EntityMob) {
                                    return monsterValue.getValue();
                                }
                                return (!(entity instanceof EntityVillager) && !(entity instanceof EntityIronGolem) && !(entity instanceof EntitySnowman)) || neutralValue.getValue();
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean isOnTeam(final EntityLivingBase en) {

        if (!mc.thePlayer.getDisplayName().getUnformattedText().isEmpty() && mc.thePlayer.getDisplayName().getUnformattedText().charAt(0) == '§') {

            if (mc.thePlayer.getDisplayName().getUnformattedText().length() <= 2 || en.getDisplayName().getUnformattedText().length() <= 2) {
                return false;
            }

            return mc.thePlayer.getDisplayName().getUnformattedText().substring(0, 2).equals(en.getDisplayName().getUnformattedText().substring(0, 2));
        }
        return false;
    }

    private boolean getEntityValidBlock(final EntityLivingBase entity) {

        if (mc.thePlayer.isEntityAlive()) {

            if (!mc.thePlayer.isPlayerSleeping()) {

                if (!mc.thePlayer.isDead) {

                    if (mc.thePlayer.getHealth() > 0.0f) {

                        if (mc.thePlayer.getDistanceToEntity(entity) <= blockRangeValue.getValue().floatValue() && entity.isEntityAlive() && !entity.isDead && entity.getHealth() > 0.0f && !(entity instanceof EntityArmorStand) && !AntiBot.isBot(entity)) {

                            if (entity != mc.thePlayer) {
                                if (entity instanceof EntityPlayer && isOnTeam(entity)) {

                                    if (Exception.instance.moduleManager.getModuleByClass(Teams.class).isEnable()) {
                                        return false;
                                    }
                                }
//                                if (FriendManager.isFriend(entity.getName())) {
//                                    return false;
//                                }
                                if (entity instanceof EntityPlayer) {
                                    EntityPlayer player = (EntityPlayer) entity;
                                    if (!playerValue.getValue()) {
                                        return false;
                                    }
                                    if (player.isPlayerSleeping()) {
                                        return false;
                                    }
                                    if (player.isPotionActive(Potion.invisibility) && !invisibleValue.getValue()) {
                                        return false;
                                    }
                                }
                                if (entity instanceof EntityAnimal) {
                                    return animalValue.getValue();
                                }
                                if (entity instanceof EntityMob) {
                                    return monsterValue.getValue();
                                }
                                return (!(entity instanceof EntityVillager) && !(entity instanceof EntityIronGolem) && !(entity instanceof EntitySnowman)) || neutralValue.getValue();
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void sortList(final List<? extends EntityLivingBase> weed) {
        if (Priority.isCurrentMode("Distance")) {
            weed.sort(Comparator.comparingDouble(o -> RotationUtil.getRotations(o)[0]));
        }
        if (Priority.isCurrentMode("Health")) {
            weed.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
        }
        if (Priority.isCurrentMode("Fov")) {
            weed.sort(Comparator.comparingDouble(o -> Math.abs(RotationUtil.getYawChange((float) o.posX, o.posY, o.posZ))));
        }
    }
}
