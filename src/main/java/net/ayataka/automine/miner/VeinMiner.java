package net.ayataka.automine.miner;

import net.ayataka.automine.util.OredictUtil;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class VeinMiner {
    private static final Vec3i FIND_AREA = new Vec3i(1, 1, 1);
    private final Block origin;
    private final World world;
    private final VeinMode veinMode;
    public final List<BlockPos> result;
    public final List<BlockPos> resultLeaf;

    private int digged;
    private final int limit;
    private boolean foundLeaf;

    public VeinMiner(final BlockPos origin, final World world, final int limit, final VeinMode veinMode) {
        this.origin = world.getBlockState(origin).getBlock();
        this.world = world;
        this.limit = limit;
        this.veinMode = veinMode;
        this.result = new ArrayList<>();
        this.resultLeaf = new ArrayList<>();

        this.veinMine(origin);

        // if it's not a valid "tree", Set results empty to prevent processing.
        if (veinMode == VeinMode.TREE && !this.foundLeaf) {
            this.result.clear();
            this.resultLeaf.clear();
        }
    }

    private void veinMine(final BlockPos in) {
        for (BlockPos pos : BlockPos.getAllInBox(in.subtract(FIND_AREA), in.add(FIND_AREA))) {
            Block block = this.world.getBlockState(pos).getBlock();

            // Check tree
            if (this.veinMode == VeinMode.TREE) {
                if (!this.foundLeaf && OredictUtil.isLeaf(block)) {
                    this.foundLeaf = true;
                }
            }

            if (block == this.origin && !this.result.contains(pos)) {
                if (this.veinMode == VeinMode.TREE) {
                    // Find around leafs to decay later
                    for (BlockPos posLeaf : BlockPos.getAllInBox(pos.add(-6, 0, -6), pos.add(6, 7, 6))) {
                        if (!this.resultLeaf.contains(posLeaf) && OredictUtil.isLeaf(this.world.getBlockState(posLeaf).getBlock())) {
                            this.resultLeaf.add(posLeaf);
                        }
                    }

                    // PREVENT DESTROY DOWNER LOGS ( NO. DON'T DO IT. THIS IS FUCKING ANNOYING )
                   /* if (pos.getY() < this.originPos.getY() - 4) {
                        continue;
                    }*/
                }

                this.digged++;
                this.result.add(pos);

                if (this.digged < this.limit) {
                    this.veinMine(pos);
                }
            }
        }
    }
}
