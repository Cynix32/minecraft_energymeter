package dev.rlnt.energymeter.meter;

import dev.rlnt.energymeter.core.Setup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

public class MeterContainer extends Container {

    private final MeterTile tile;

    public MeterContainer(MeterTile tile, int windowID) {
        super(Setup.Containers.METER_CONTAINER.get(), windowID);
        this.tile = tile;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        if (tile.getLevel() == null) return false;
        return Container.stillValid(
            IWorldPosCallable.create(tile.getLevel(), tile.getBlockPos()),
            player,
            tile.getBlockState().getBlock()
        );
    }

    /**
     * Gets the parent {@link TileEntity} of the {@link Container}.
     *
     * @return the parent
     */
    public MeterTile getTile() {
        return tile;
    }
}
