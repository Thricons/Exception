package cn.exception.module.impl.movement;

import cn.exception.event.Event;
import cn.exception.event.EventTarget;
import cn.exception.event.events.EventUpdate;
import cn.exception.module.Module;
import cn.exception.module.value.Mode;
import cn.exception.module.value.Numbers;
import cn.exception.module.value.Option;
import cn.exception.utils.MoveUtil;
import cn.exception.utils.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author MiLiBlue
 **/
public class Speed extends Module {
    public static Mode mode = new Mode("Mode", new String[]{"NCP", "Vanilla", "Mineland", "BlocksMC"}, "NCP");
    public Numbers timer = new Numbers<>("Timer", 1.6f, 1f,2f,0.1f);
    private boolean bool;
    public Option workOnDamange = new Option("OnlyWorkOnDamage", true);
    private int offGroundTicks;
    private double moveSpeed;
    private boolean wasSlow = false;
    public Speed(){
        super("Speed", Category.Movement);
        addValues(mode, timer, workOnDamange);
    }


    @EventTarget
    public void onEvent(EventUpdate eventUpdate){
        setSuffix(mode.getValue());
        Wrapper.getTimer().timerSpeed = timer.floatValue();
        if(eventUpdate.getType() == Event.Type.POST && mode.isCurrentMode("NCP")){
            if (mc.thePlayer.ticksExisted % 20 <= 9) {
                Wrapper.getTimer().timerSpeed = 1.05f;
            } else {
                Wrapper.getTimer().timerSpeed = 0.98f;
            }

            if (MoveUtil.isMoving()) {
                if (mc.thePlayer.onGround) {
                    wasSlow = false;
                    mc.thePlayer.jump();
                    MoveUtil.strafe(MoveUtil.getSpeed() * 1.01f);
                    if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        MoveUtil.strafe(MoveUtil.getSpeed() * (1.0f + 0.1f * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1)));
                    }
                }
                MoveUtil.strafe(MoveUtil.getSpeed() * 1.0035f);
                if (MoveUtil.getSpeed() < 0.277) {
                    wasSlow = true;
                }
                if (wasSlow) {
                    MoveUtil.strafe(0.277f);
                }


            } else {
                mc.thePlayer.motionX = 0.0;
                mc.thePlayer.motionZ = 0.0;
                wasSlow = true;
            }
        }
        if(eventUpdate.getType() == Event.Type.PRE && mode.isCurrentMode("Mineland")){
            final double xDist = mc.thePlayer.lastTickPosX - mc.thePlayer.posX;
            final double zDist = mc.thePlayer.lastTickPosZ - mc.thePlayer.posZ;
            final double lastDist = Math.sqrt(xDist * xDist + zDist * zDist);

            if (MoveUtil.isMoving()) {

                // I cannot believe we have six full time developers and not a single one made this speed work in liquid
                // maybe because any of those developers dont give a shit
                if (mc.thePlayer.isInWater()) {
                    MoveUtil.strafe((float) (MoveUtil.getBaseMovementSpeed() * 0.3));
                    return;
                } else if (mc.thePlayer.isInLava()) {
                    MoveUtil.strafe((float) (MoveUtil.getBaseMovementSpeed() * 0.25));
                    return;
                }

                double baseMoveSpeed = MoveUtil.getBaseMovementSpeed();
                //strafe check dont remove
//                if(!Anxious.instance.moduleManager.getModuleByClass(Scaffold.class).isEnable())
////                    eventUpdate.setYaw(getMovementDirection(mc.thePlayer.moveForward, mc.thePlayer.moveStrafing,
////                            mc.thePlayer.rotationYaw));

                if (!mc.thePlayer.onGround && mc.thePlayer.fallDistance < 0.24) {
                    mc.thePlayer.motionY = this.lowHopYModification(mc.thePlayer.motionY, round(
                            mc.thePlayer.posY - (int) mc.thePlayer.posY, 3, 0.001)) + Math.random() / 100000f;
                }

                if (mc.thePlayer.onGround) {
                    boolean doInitialLowHop =
                            !mc.thePlayer.isPotionActive(Potion.jump) && !mc.thePlayer.isCollidedHorizontally;

                    mc.thePlayer.motionY = doInitialLowHop ? 0.4f : 0.42f;
                    MoveUtil.strafe((float) (baseMoveSpeed * 1.6));

                    bool = true;
                } else if (bool) {
                    bool = false;

                    final double bunny = (0.84 + Math.random() / 50f) * (lastDist - baseMoveSpeed);

                    MoveUtil.strafe((float) (lastDist - bunny));
                }
            }

            if (mc.thePlayer.isCollidedHorizontally || mc.thePlayer.fallDistance > 0.8) {
                MoveUtil.strafe((float) (MoveUtil.getSpeed() * 0.9));
            }

            if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.motionX *= 1.04 - Math.random() / 100;
                    mc.thePlayer.motionZ *= 1.04 - Math.random() / 100;
                } else {
                    mc.thePlayer.motionX *= 1.005 - Math.random() / 100;
                    mc.thePlayer.motionZ *= 1.005 - Math.random() / 100;
                }
            }

            if (!isBlockUnder()) {
                MoveUtil.strafe((float) (MoveUtil.getSpeed() * 0.92));
            }

            double motionX2 = mc.thePlayer.motionX;
            double motionZ2 = mc.thePlayer.motionZ;

            MoveUtil.strafe();

            if (!mc.thePlayer.onGround && offGroundTicks != 1) {
                mc.thePlayer.motionX = (mc.thePlayer.motionX * 3 + motionX2) / 4;
                mc.thePlayer.motionZ = (mc.thePlayer.motionZ * 3 + motionZ2) / 4;
            }

            mc.thePlayer.motionX *= 0.99;
            mc.thePlayer.motionZ *= 0.99;
        }
        if(eventUpdate.getType() == Event.Type.POST && mode.isCurrentMode("Vanilla")){
            if(MoveUtil.isMoving()){
                if(mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionX *= 1.01D;
                    mc.thePlayer.motionZ *= 1.01D;
                }

                mc.thePlayer.motionY -= 0.00099999D;
                moveSpeed = (MoveUtil.getBaseMovementSpeed() * 3);
                MoveUtil.strafe((float) moveSpeed);
            }else{
                mc.thePlayer.motionX = 0D;
                mc.thePlayer.motionZ = 0D;
            }
        }
        if(mode.isCurrentMode("BlocksMC") && eventUpdate.getType() == Event.Type.POST){
            if(MoveUtil.isMoving()){
                if(mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionX *= 1.01D;
                    mc.thePlayer.motionZ *= 1.01D;
                }

                mc.thePlayer.motionY -= 0.00099999D;
                moveSpeed = MoveUtil.getSpeed() * 1.001;
                MoveUtil.strafe(moveSpeed);
            }else{
                mc.thePlayer.motionX = 0D;
                mc.thePlayer.motionZ = 0D;
            }
        }
    }

    private double getBaseSpeed() {
        final EntityPlayerSP player = mc.thePlayer;
        double base = 0.2895;
        final PotionEffect moveSpeed = player.getActivePotionEffect(Potion.moveSpeed);
        final PotionEffect moveSlowness = player.getActivePotionEffect(Potion.moveSlowdown);
        if (moveSpeed != null) {
            base *= 1.0 + 0.19 * (moveSpeed.getAmplifier() + 1);
        }

        if (moveSlowness != null) {
            base *= 1.0 - 0.13 * (moveSlowness.getAmplifier() + 1);
        }

        if (player.isInWater()) {
            base *= 0.5203619984250619;
            final int depthStriderLevel = EnchantmentHelper.getDepthStriderModifier(mc.thePlayer);

            if (depthStriderLevel > 0) {
                double[] DEPTH_STRIDER_VALUES = new double[]{1.0, 1.4304347400741908, 1.7347825295420374, 1.9217391028296074};
                base *= DEPTH_STRIDER_VALUES[depthStriderLevel];
            }

        } else if (player.isInLava()) {
            base *= 0.5203619984250619;
        }
        return base;
    }

    public float getMovementDirection(float forward, float strafing, float yaw) {
        if (forward == 0.0F && strafing == 0.0F) {
            return yaw;
        }
        boolean reversed = (forward < 0.0F);
        float strafingYaw = 90.0F * ((forward > 0.0F) ? 0.5F : (reversed ? -0.5F : 1.0F));
        if (reversed) {
            yaw += 180.0F;
        }
        if (strafing > 0.0F) {
            yaw -= strafingYaw;
        } else if (strafing < 0.0F) {
            yaw += strafingYaw;
        }
        return yaw;
    }

    private boolean isBlockUnder() {
        if (mc.thePlayer.posY < 0) {
            return false;
        }
        for (int off = 0; off < (int) mc.thePlayer.posY + 2; off += 2) {
            AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0, -off, 0);
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static final double[] lowhopy = {
            round(0.4, 3, 0.001),
            round(0.71, 3, 0.001),
            round(0.75, 3, 0.001),
            round(0.55, 3, 0.001),
            round(0.41, 3, 0.001),
    };

    public static double round(final double value, final int scale, final double inc) {
        final double halfOfInc = inc / 2.0;
        final double floored = Math.floor(value / inc) * inc;

        if (value >= floored + halfOfInc) {
            return new BigDecimal(Math.ceil(value / inc) * inc)
                    .setScale(scale, RoundingMode.HALF_UP)
                    .doubleValue();
        } else {
            return new BigDecimal(floored)
                    .setScale(scale, RoundingMode.HALF_UP)
                    .doubleValue();
        }
    }

    private double lowHopYModification(final double baseMotionY, final double yDistFromGround) {
        if (yDistFromGround == lowhopy[0]) {
            return 0.31;
        } else if (yDistFromGround == lowhopy[1]) {
            return 0.04;
        } else if (yDistFromGround == lowhopy[2]) {
            return -0.2;
        } else if (yDistFromGround == lowhopy[3]) {
            return -0.14;
        } else if (yDistFromGround == lowhopy[4]) {
            return -0.2;
        }
        return baseMotionY;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Wrapper.getTimer().timerSpeed = 1F;
        mc.thePlayer.jumpMovementFactor = 0.02f;
        super.onDisable();
    }
}
