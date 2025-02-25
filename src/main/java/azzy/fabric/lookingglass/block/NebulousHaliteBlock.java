package azzy.fabric.lookingglass.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Random;

public class NebulousHaliteBlock extends PillarBlock {

    public NebulousHaliteBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if(!world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos, Direction.UP) && random.nextInt(3) == 0)
            world.addParticle(ParticleTypes.END_ROD, true, pos.getX() + random.nextDouble(), pos.getY() - 0.05, pos.getZ() + random.nextDouble(), 0, 0, 0);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {

        if(!world.isClient()) {
            if(!(entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative())) {
                Random random = world.getRandom();
                if(random.nextInt(51) == 0) {
                    if(random.nextBoolean()) {
                        ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 10 + random.nextInt(20), 39, false, false, false));
                    }
                    else
                        entity.requestTeleport(entity.getX() + (random.nextInt(33) - 16), entity.getY() + random.nextInt(9),entity.getZ() + (random.nextInt(33) - 16));
                    world.playSoundFromEntity(null, entity, SoundEvents.ENTITY_ENDER_EYE_DEATH, SoundCategory.PLAYERS, 2.0F, 1.5F);
                    ((ServerWorld) world).spawnParticles(ParticleTypes.END_ROD, entity.getX() + (entity.getWidth() / 2.0), entity.getY() + (entity.getHeight() / 2.0), entity.getZ() + (entity.getWidth() / 2.0), 10 + world.getRandom().nextInt(10), 0, 0, 0, 0.08);
                }
            }
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if(!world.isClient() && !player.isCreative()) {
            Random random = world.getRandom();
            int enchantability = Math.max(player.getStackInHand(Hand.MAIN_HAND).getItem().getEnchantability(), 0);
            if(enchantability < 20 && random.nextInt(enchantability + 1) == 0) {
                if(random.nextBoolean()) {
                    player.applyStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 10 + random.nextInt(20), 39, false, false, false));
                }
                else
                    player.requestTeleport(player.getX() + (random.nextInt(33) - 16), player.getY() + random.nextInt(9),player.getZ() + (random.nextInt(33) - 16));
                world.playSoundFromEntity(null, player, SoundEvents.ENTITY_ENDER_EYE_DEATH, SoundCategory.PLAYERS, 2.0F, 1.5F);
                ((ServerWorld) world).spawnParticles(ParticleTypes.END_ROD, player.getX() + (player.getWidth() / 2.0), player.getY() + (player.getHeight() / 2.0), player.getZ() + (player.getWidth() / 2.0), 10 + world.getRandom().nextInt(10), 0, 0, 0, 0.08);
            }
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0F;
    }
}
