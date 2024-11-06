package net.k3nder.scripting;

import org.apache.commons.io.FileUtils;

import javax.script.Compilable;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ScriptSource {
    private final String script;
    private final ScriptTag tag;

    public final static HashMap<String, TagAddImports> TAG_IMPORTS = new HashMap<>();

    public ScriptSource(String script, ScriptTag tag) {
        this.script = script;
        this.tag = tag;
    }

    public String getScript() {
        return script;
    }
    public ScriptTag getTag() {
        return tag;
    }
    public static ScriptSource from(File file, ScriptingLang lang) {
        try {
            var string = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            var tag = string.split("\n")[0];
            return new ScriptSource(string, ScriptTag.from(tag, lang.getTagIndicator()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class ScriptCompiled {
        private final CompiledScript compiled;
        private final ScriptTag tags;

        public static final HashMap<String, ScriptTagRun> REGISTERS = new HashMap<>();

        public ScriptCompiled(ScriptSource source, ScriptEngine runtime) {
            var script = source.getScript();
                this.tags = source.getTag();
                if (TAG_IMPORTS.containsKey(tags.name())) {
                    script = TAG_IMPORTS.get(tags.name()).get() + "\n" + script;
                    System.out.println(script);
                }
                this.compiled = runtime.compile(script);

        }
        public ScriptTag getTag() {
            return tags;
        }
        public void run() {
            if (!REGISTERS.containsKey(tags.name())) throw new RuntimeException("Unknown tag: " + tags.name());
            REGISTERS.get(tags.name()).run(this);
        }
        public void runSource() {
            this.compiled.eval();
        }
        public CompiledScript runtime() {
            return this.compiled;
        }
        public<T> T run(String scr, Class<T> clazz) {
                var s = this.compiled.eval();
                return (T) s;

        }
        public void run(String scr) {
            this.compiled.eval(scr);
        }
        public Object callMethod(String name, Object... args) {
            return this.compiled.invokeFunction(name, args);
        }
        public Object callMethod(String name) {
            return this.compiled.invokeFunction(name);
        }
        public void setVar(String name, Object value) {
            this.compiled.put(name, value);
        }
        public Object getVar(String name) {
            return this.compiled.get(name);
        }
    }
}
