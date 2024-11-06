package net.k3nder.scripting.objects;

import net.k3nder.scripting.ScriptSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Arrays;

public class ScriptItem extends Item {

    private final net.k3nder.scripting.ScriptSource.ScriptCompiled program;

    public ScriptItem(Settings settings, net.k3nder.scripting.ScriptSource.ScriptCompiled source) {
        super(settings);
        this.program = source;
        this.program.runSource();
    }


    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        try {
            var s = program.callMethod("use", Arrays.asList(world, user, hand));
            return (ActionResult) s;
        } catch (Throwable e) {
            return super.use(world, user, hand);
        }
    }
}
