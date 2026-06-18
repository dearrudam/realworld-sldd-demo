package dev.realworld.users.control;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

record JsonClaims(Map<String, String> values) {

    static JsonClaims parse(String json) {
        var body = json.strip().replaceFirst("^\\{", "").replaceFirst("}$", "");
        if (body.isBlank()) {
            return new JsonClaims(Map.of());
        }
        return new JsonClaims(Arrays.stream(body.split(","))
                .map(part -> part.split(":", 2))
                .collect(Collectors.toMap(parts -> clean(parts[0]), parts -> clean(parts[1]))));
    }

    String value(String name) {
        var value = values.get(name);
        if (value == null) {
            throw new InvalidTokenException();
        }
        return value;
    }

    static String clean(String value) {
        return value.strip().replaceFirst("^\"", "").replaceFirst("\"$", "");
    }
}
