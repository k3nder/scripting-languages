package net.k3nder.scripting;

@FunctionalInterface
public interface ScriptTagRun {
    void run(ScriptSource.ScriptCompiled source);
}
