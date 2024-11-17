package net.jackylang2012.tutortialmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;

public class StrangeBlueIceBlock extends Block {

    public StrangeBlueIceBlock(Properties properties) {
        super(properties.noCollission().noOcclusion());  // 禁用碰撞体和透明性
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide) {
            // 安排一个在 15 秒后触发的时钟周期 (300 个时钟周期)
            level.scheduleTick(pos, this, 300);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);

        // 播放冰块碎裂声音
        level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);

        // 移除方块
        level.removeBlock(pos, false);
    }

//    @Override
    public net.minecraft.world.phys.shapes.VoxelShape getCollisionShape(BlockState state, net.minecraft.world.level.LevelReader world, net.minecraft.core.BlockPos pos, CollisionContext context) {
        System.out.println("getCollisionShape called for " + this);  // 调试信息
        return Shapes.empty();  // 返回一个空的形状表示没有碰撞体
    }
}
