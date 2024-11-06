package net.k3nder.scripting.objects;

import net.k3nder.scripting.ScriptSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;

public class ScriptBlock extends Block {
    private final ScriptSource.ScriptCompiled program;

    public ScriptBlock(Settings settings, ScriptSource.ScriptCompiled source) {
        super(settings);
        this.program = source;
        this.program.runSource();
    }
}
