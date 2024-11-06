package net.k3nder.scripting;

public interface ScriptingLang {
    public String getExtension();
    public String getTagIndicator();
    public ScriptEngine buildEngine();
}
