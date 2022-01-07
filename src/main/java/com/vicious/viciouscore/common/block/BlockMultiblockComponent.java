package com.vicious.viciouscore.common.block;

import com.vicious.viciouscore.common.tile.INotifier;
import com.vicious.viciouscore.common.tile.TileMultiBlockComponent;
import com.vicious.viciouscore.common.util.reflect.IFieldCloner;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;


/**
 * Block that notifies a parent TileEntity when updated.
 * All mods should do this, it reduces the need to recheck if the multiblock has been damaged.
 */
public class BlockMultiblockComponent extends ViciousBlock implements ITileEntityProvider, IFieldCloner {
    public BlockMultiblockComponent(Material materialIn) {
        super("multi", materialIn);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileMultiBlockComponent();
    }


    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        notifyParent(worldIn, pos);
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    /*@Override
    public void onBlockUpdated(World worldIn, BlockPos pos) {
        //Disabled notify for now to allow more control.
        //notifyParent(worldIn, pos);
        super.onBlockUpdated(worldIn, pos);
    }*/

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
        TileEntity te = world.getTileEntity(pos);
        if(te == null) return;
        notifyParent(te.getWorld(),pos);
    }

    public void notifyParent(World worldin, BlockPos pos){
        TileEntity tile = worldin.getTileEntity(pos);
        if(tile instanceof INotifier){
            ((INotifier)tile).notifyParent();
        }
    }
}
