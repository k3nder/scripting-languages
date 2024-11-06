package net.k3nder.scripting;

import java.util.HashMap;

public class ScriptTag {
    private final HashMap<String, String> tags;
    private final String name;

    private ScriptTag(String name, HashMap<String, String> tags) {
        this.name = name;
        this.tags = tags;
    }
    public static ScriptTag from(String tag, String tag_indicator) {
        if (tag.startsWith(tag_indicator)) {
            tag = tag.substring(tag_indicator.length()).trim();
        }

        var nameAndTags = tag.split("\\|");

        var name = nameAndTags[0];
        System.out.println("TAG:  " + name);

        if (nameAndTags.length == 1) return new ScriptTag(name, new HashMap<>());

        var properties = new HashMap<String, String>();

        for (int i = 1; i < nameAndTags.length; i++) {
            var property = nameAndTags[i];
            var keyValue = property.split("=", 2);

            if (keyValue.length == 1) {
                properties.put(keyValue[0], "true");
                continue;
            }

            properties.put(keyValue[0], keyValue[1]);
        }

        return new ScriptTag(name, properties);
    }

    @Override
    public String toString() {
        return "RILTag{" + "name='" + name + '\'' + ", tags=" + tags + '}';
    }

    public String name() {
        return name;
    }
    public String tag(String key) {
        return tags.get(key);
    }
    public HashMap<String, String> tags() {
        return tags;
    }
    public boolean contains(String key) {
        return tags.containsKey(key);
    }
}
