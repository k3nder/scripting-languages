package net.k3nder.scripting;

public interface CompiledScript {

    Object eval();
    Object eval(String input);

    Object invokeFunction(String name, Object... args);
    Object invokeMethod(String name);

    void put(String name, Object value);

    Object get(String name);
}
