package cn.exception.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;

/**
 * Code by MiLiBlue, At 2022/12/27
 **/
public class MoveUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static float getSpeed() {
        return (float) getSpeed(mc.thePlayer.motionX, mc.thePlayer.motionZ);
    }

    public static double getSpeed(double motionX, double motionZ) {
        return Math.sqrt(motionX * motionX + motionZ * motionZ);
    }

    public static double getBaseSpeed() {
        final EntityPlayerSP player = mc.thePlayer;
        double base = 0.2895;
        final PotionEffect moveSpeed = player.getActivePotionEffect(Potion.moveSpeed);
        final PotionEffect moveSlowness = player.getActivePotionEffect(Potion.moveSlowdown);
        if (moveSpeed != null)
            base *= 1.0 + 0.19 * (moveSpeed.getAmplifier() + 1);

        if (moveSlowness != null)
            base *= 1.0 - 0.13 * (moveSlowness.getAmplifier() + 1);

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

    public static void strafe() {
        strafe(getSpeed());
    }
    public static float[] getRotations(EntityLivingBase entity) {
        EntityLivingBase entityLivingBase = entity;
        double diffX = entityLivingBase.posX - mc.thePlayer.posX;
        double diffZ = entityLivingBase.posZ - mc.thePlayer.posZ;
        double diffY = entityLivingBase.posY + (double) entity.getEyeHeight() - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
        double X = diffX;
        double Z = diffZ;
        double dist = MathHelper.sqrt_double((double) (X * X + Z * Z));
        float yaw = (float) (Math.atan2((double) diffZ, (double) diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (-(Math.atan2((double) diffY, (double) dist) * 180.0 / 3.141592653589793));
        return new float[]{yaw, pitch};
    }
    public static void strafe(final double speed) {
        if(!isMoving())
            return;

        final double yaw = getDirection();
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }

    public static double getDirection() {
        return getDirectionRotation(mc.thePlayer.rotationYaw, mc.thePlayer.moveStrafing, mc.thePlayer.moveForward);
    }
    public static double getDirectionRotation(float yaw, float pStrafe, float pForward) {
        float rotationYaw = yaw;

        if(pForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if(pForward < 0F)
            forward = -0.5F;
        else if(pForward > 0F)
            forward = 0.5F;

        if(pStrafe > 0F)
            rotationYaw -= 90F * forward;

        if(pStrafe < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    public static float getRawDirectionRotation(float yaw, float pStrafe, float pForward) {
        float rotationYaw = yaw;

        if(pForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if(pForward < 0F)
            forward = -0.5F;
        else if(pForward > 0F)
            forward = 0.5F;

        if(pStrafe > 0F)
            rotationYaw -= 90F * forward;

        if(pStrafe < 0F)
            rotationYaw += 90F * forward;

        return rotationYaw;
    }
    public static double getBaseMovementSpeed() {
        double base = 0.2873D;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            base += 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return base;
    }

    public static boolean isMoving() {
        return mc.thePlayer.movementInput.moveForward != 0.0f || mc.thePlayer.movementInput.moveStrafe != 0.0f;
    }

    public static int getSpeedEffect() {
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }

    public static double getJumpEffect() {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            return mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }

    public static boolean isOnGround(double height) {
        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty();
    }

    public static boolean isPressingMoving() {
        return mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown();
    }
}
