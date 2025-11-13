package net.lopymine.patpat.utils;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.Cat;

import java.util.Random;
import java.util.UUID;

public class TameUtils {
    private static final boolean showParticles = true;
    private static final Random random = new Random();

    private static boolean chance(double chance) {
        return random.nextDouble(100) < chance;
    }

    public static void runByChance(Entity entity, ServerPlayer player, ServerLevel serverWorld) {

        if (entity instanceof Wolf wolf) {
            if (!wolf.isTame() && chance(wolf.isAngry() ? 0.25 : 1)) {
                wolf.tame(player);
                wolf.setOwnerUUID(player.getUUID());
                wolf.stopBeingAngry(); // Because sounds are still appearing
                showParticles(wolf, serverWorld);
            }

            UUID PlayerTamed = wolf.getOwnerUUID();
            if (chance(4) && PlayerTamed == player.getUUID()) {
                final float wolfHp = wolf.getHealth();
                float maxHealth = wolf.getMaxHealth();
                float futureHp = wolfHp + 1f;
                if (maxHealth < futureHp) {futureHp = maxHealth; }
                wolf.setHealth(futureHp);
                showParticles(wolf, serverWorld);
            }
        }


        if (entity instanceof Cat cat) {
            // It seems to me that in real life cats will be much less likely to become attached to a person if they are simply petted, but this is purely my opinion!

            if (!cat.isTame() && chance(1.5)) {
                cat.tame(player);
                cat.setOwnerUUID(player.getUUID());
                showParticles(cat, serverWorld);
            }
        }
    }



    private static void showParticles(Entity entity, ServerLevel serverWorld) {
        showParticles(entity, serverWorld, 5);
    }
    private static void showParticles(Entity entity, ServerLevel serverWorld, int amount) {
        if (!showParticles) { return; }

        serverWorld.sendParticles(
                ParticleTypes.HEART,
                entity.getX(),
                entity.getY() + 1,
                entity.getZ(),
                amount,
                0.5,
                0.5,
                0.5,
                1.0
        );
    }
}

