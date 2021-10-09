package org.playuniverse.minecraft.core.lithos.custom.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public class CraftStructureProcessor extends StructureProcessor {

    @Override
    public StructureBlockInfo processBlock(LevelReader reader, BlockPos var2, BlockPos var3, StructureBlockInfo var4, StructureBlockInfo var5,
        StructurePlaceSettings var6) {
        return null;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return null;
    }

}
