package net.k3nder.scripting;

import net.minecraft.client.MinecraftClient;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;

public class ScriptPack {
    private final ArrayList<ScriptSource> sources;
    private final ScriptingLang engine;

    private ScriptPack(ArrayList<ScriptSource> sources, ScriptingLang engine) {
        this.sources = sources;
        this.engine = engine;
    }
    public void add(ScriptSource source) {
        this.sources.add(source);
    }
    public void execute() {
        for (ScriptSource source : sources) {
            var prog = new ScriptSource.ScriptCompiled(source, engine.buildEngine());
            prog.run();
        }
    }
    public static ScriptPack from(Path path, ScriptingLang lang) throws IOException {
        var sources = new ArrayList<ScriptSource>();

        System.out.println("LOADING SCRIPTS ON PATH: " + path);

        try (Stream<Path> paths = Files.walk(path)) {
            for (Iterator<Path> it = paths.iterator(); it.hasNext(); ) {
                var paath = it.next();
                if (!Files.isDirectory(paath)) {
                    System.out.println("Reading " + paath.getFileName());
                    if (paath.getFileName().toString().endsWith(lang.getExtension())) {
                        System.out.println("IS " + lang.getExtension());
                        sources.add(ScriptSource.from(paath.toFile(), lang));
                    }
                }
            }
        }
        return new ScriptPack(sources, lang);
    }
}
