package com.pfe.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JsonProcessingService {
    public static final String VALUE_KEY = "value";
    private static final Logger log = LoggerFactory.getLogger(JsonProcessingService.class);
    private final ObjectMapper objectMapper;

    public JsonProcessingService() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Converts a given name, type, and value into a JSON object node.
     *
     * @param name  the variable name
     * @param type  the variable type (e.g., "string", "object")
     * @param value the variable value
     * @return an ObjectNode representation of the input
     */
    public ObjectNode createJsonNode(String name, String type, Object value) {
        log.debug("Creating JSON node for name: {}, type: {}, value: {}", name, type, value);
        ObjectNode node = objectMapper.createObjectNode();
        node.put("name", name);
        node.put("type", type);

        // Determine how to handle the value based on its type
        if (isPrimitiveType(value)) {
            log.debug("Value is of primitive type: {}", value);
            setPrimitiveValue(node, value);
        } else if ("object".equalsIgnoreCase(type) || "arraylist".equalsIgnoreCase(type)) {
            log.debug("Value is of complex type: {}", value);
            handleComplexType(node, value);
        } else {
            log.debug("Value is of unknown type; converting to string: {}", value);
            node.put(VALUE_KEY, safeToString(value));
        }

        log.debug("Created JSON node: {}", node);
        return node;
    }

    /**
     * Checks if the given value is a primitive type or its wrapper.
     */
    private boolean isPrimitiveType(Object value) {
        return value instanceof String ||
            value instanceof Integer ||
            value instanceof Boolean ||
            value instanceof Double ||
            value instanceof Long;
    }

    /**
     * Adds a primitive value to the JSON node.
     *
     * @param node  the ObjectNode to populate
     * @param value the primitive value
     */
    private void setPrimitiveValue(ObjectNode node, Object value) {
        log.debug("Setting primitive value: {}", value);
        if (value instanceof String s) {
            node.put(VALUE_KEY, s);
        } else if (value instanceof Integer i) {
            node.put(VALUE_KEY, i);
        } else if (value instanceof Boolean b) {
            node.put(VALUE_KEY, b);
        } else if (value instanceof Double d) {
            node.put(VALUE_KEY, d);
        } else if (value instanceof Long l) {
            node.put(VALUE_KEY, l);
        }
    }

    /**
     * Handles complex types like objects, lists, and maps.
     *
     * @param node  the ObjectNode to populate
     * @param value the complex value to handle
     */
    private void handleComplexType(ObjectNode node, Object value) {
        log.debug("Handling complex type: {}", value);
        try {
            JsonNode jsonNode = convertToJsonNode(value);
            if (jsonNode != null) {
                log.debug("Converted complex type to JsonNode: {}", jsonNode);
                node.set(VALUE_KEY, jsonNode);
            } else {
                log.warn("Complex type conversion returned null, storing as string: {}", value);
                node.put(VALUE_KEY, safeToString(value));
            }
        } catch (Exception e) {
            log.error("Error while handling complex type: {} : {}", value, e.getLocalizedMessage());
            node.put(VALUE_KEY, safeToString(value));
        }
    }

    /**
     * Safely converts an object to its string representation, catching exceptions if any.
     *
     * @param value the object to convert
     * @return the string representation or "null" if an exception occurs
     */
    private String safeToString(Object value) {
        try {
            return value != null ? value.toString() : null;
        } catch (Exception e) {
            log.error("Error converting value to string: {} : {}", value, e.getLocalizedMessage());
            return "";
        }
    }

    /**
     * Converts supported types (List, Map, or other objects) to a JsonNode.
     *
     * @param value the value to convert
     * @return a JsonNode representation of the value, or null if unsupported
     */
    private JsonNode convertToJsonNode(Object value) {
        try {
            if (value instanceof List<?> || value instanceof Map<?, ?>) {
                log.debug("Converting value to JsonNode: {}", value);
                return objectMapper.valueToTree(value);
            }
        } catch (Exception e) {
            log.error("Error converting value to JsonNode: {}: {}", value, e.getLocalizedMessage());
        }
        return null;
    }
}
