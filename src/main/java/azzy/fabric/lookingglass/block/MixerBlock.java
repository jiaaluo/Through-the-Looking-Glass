package azzy.fabric.lookingglass.block;

import azzy.fabric.lookingglass.blockentity.AlloyFurnaceEntity;
import azzy.fabric.lookingglass.blockentity.MixerEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MixerBlock extends HorizontalMachineBlock implements BlockEntityProvider {

    public MixerBlock(Settings settings) {
        super(settings, false, 0);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return new MixerEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!player.isSneaking()) {
            if(!world.isClient())
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    //@Environment(EnvType.CLIENT)
    //public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    //    if (state.get(LIT)) {
    //        double d = pos.getX() + 0.5D;
    //        double e = pos.getY();
    //        double f = pos.getZ() + 0.5D;
    //        if (random.nextDouble() < 0.1D) {
    //            world.playSound(d, e, f, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
    //        }
//
    //        Direction direction = state.get(HORIZONTAL_FACING);
    //        Direction.Axis axis = direction.getAxis();
    //        double g = 0.52D;
    //        double h = random.nextDouble() * 0.6D - 0.3D;
    //        double i = axis == Direction.Axis.X ? direction.getOffsetX() * 0.52D : h;
    //        double j = random.nextDouble() * 6.0D / 16.0D;
    //        double k = axis == Direction.Axis.Z ? direction.getOffsetZ() * 0.52D : h;
    //        world.addParticle(ParticleTypes.SMOKE, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
    //        world.addParticle(ParticleTypes.FLAME, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
    //    }
    //}
}
