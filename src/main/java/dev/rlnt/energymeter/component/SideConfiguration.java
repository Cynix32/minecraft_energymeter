package dev.rlnt.energymeter.component;

import dev.rlnt.energymeter.meter.MeterBlock;
import dev.rlnt.energymeter.util.TypeEnums.BLOCK_SIDE;
import dev.rlnt.energymeter.util.TypeEnums.IO_SETTING;
import java.util.EnumMap;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.INBTSerializable;

public class SideConfiguration implements INBTSerializable<CompoundNBT> {

    private static final int MAX_OUTPUTS = 4;
    private final EnumMap<Direction, IO_SETTING> config = new EnumMap<>(Direction.class);
    private final Direction facing;
    private final Direction bottom;

    public SideConfiguration(BlockState state) {
        facing = state.getValue(MeterBlock.FACING);
        bottom = state.getValue(MeterBlock.BOTTOM);
        for (Direction direction : Direction.values()) {
            config.put(direction, IO_SETTING.OFF);
        }
    }

    /**
     * Gets an IO setting by a specified direction.
     * This automatically takes the facing direction into account.
     *
     * @param direction the direction to get the IO setting from
     * @return the IO setting
     */
    public IO_SETTING get(Direction direction) {
        return config.get(direction);
    }

    /**
     * Gets an IO setting by a specified block side.
     * This automatically takes the facing direction into account.
     *
     * @param side the block side to get the IO setting from
     * @return the IO setting
     */
    public IO_SETTING get(BLOCK_SIDE side) {
        return config.get(getDirectionFromSide(side));
    }

    /**
     * Sets the specified block side to the specified IO setting.
     *
     * @param side    the side on which the setting should be changed
     * @param setting the setting which should be set
     */
    public void set(BLOCK_SIDE side, IO_SETTING setting) {
        config.put(getDirectionFromSide(side), setting);
    }

    /**
     * Checks if the side configuration has an input set somewhere.
     *
     * @return true if there is an input, false otherwise
     */
    public boolean hasInput() {
        return config.containsValue(IO_SETTING.IN);
    }

    /**
     * Checks if the side configuration has an output set somewhere.
     *
     * @return true if there is an output, false otherwise
     */
    public boolean hasOutput() {
        return config.containsValue(IO_SETTING.OUT);
    }

    /**
     * Checks if the side configuration already has the maximum amount of outputs.
     *
     * @return true if there are max outputs, false otherwise
     */
    public boolean hasMaxOutputs() {
        return config.values().stream().filter(setting -> setting == IO_SETTING.OUT).count() == MAX_OUTPUTS;
    }

    /**
     * Gets the direction from the given block side depending on the facing of the block.
     *
     * @param side the block side to get the direction from
     * @return the direction
     */
    public Direction getDirectionFromSide(BLOCK_SIDE side) {
        return facing != bottom ? verticalConversion(side) : horizontalConversion(side);
    }

    /**
     * Converts the given block side to a direction if the facing direction
     * is up or down.
     *
     * @param side the block side to convert
     * @return the direction
     */
    private Direction verticalConversion(BLOCK_SIDE side) {
        if (side == BLOCK_SIDE.TOP) {
            return bottom.getOpposite();
        } else if (side == BLOCK_SIDE.BOTTOM) {
            return bottom;
        } else if (side == BLOCK_SIDE.LEFT) {
            return facing == Direction.UP ? bottom.getClockWise() : bottom.getCounterClockWise();
        } else if (side == BLOCK_SIDE.RIGHT) {
            return facing == Direction.UP ? bottom.getCounterClockWise() : bottom.getClockWise();
        } else if (side == BLOCK_SIDE.BACK) {
            return facing.getOpposite();
        } else {
            return facing;
        }
    }

    /**
     * Converts the given block side to a direction if the facing direction
     * is any of the 4 compass directions.
     *
     * @param side the block side to convert
     * @return the direction
     */
    private Direction horizontalConversion(BLOCK_SIDE side) {
        if (side == BLOCK_SIDE.TOP) {
            return Direction.UP;
        } else if (side == BLOCK_SIDE.BOTTOM) {
            return Direction.DOWN;
        } else if (side == BLOCK_SIDE.LEFT) {
            return facing.getClockWise();
        } else if (side == BLOCK_SIDE.RIGHT) {
            return facing.getCounterClockWise();
        } else if (side == BLOCK_SIDE.BACK) {
            return facing.getOpposite();
        } else {
            return facing;
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        for (Direction direction : Direction.values()) {
            nbt.putInt(direction.toString(), config.get(direction).ordinal());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        for (Direction direction : Direction.values()) {
            config.put(direction, IO_SETTING.values()[nbt.getInt(direction.toString())]);
        }
    }
}
