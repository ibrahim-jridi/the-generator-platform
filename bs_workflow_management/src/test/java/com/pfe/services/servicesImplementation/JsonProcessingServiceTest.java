package com.pfe.services.servicesImplementation;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pfe.services.JsonProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonProcessingServiceTest {

    private JsonProcessingService jsonProcessingService;

    @BeforeEach
    void setUp() {
        jsonProcessingService = new JsonProcessingService();
    }

    // Tests for setPrimitiveValue with valid primitive types
    @Test
    void testSetPrimitiveValue_withValidPrimitiveValues() {
        ObjectNode node = jsonProcessingService.createJsonNode("key", "string", "value");
        assertEquals("value", node.get("value").asText());

        node = jsonProcessingService.createJsonNode("key", "integer", 123);
        assertEquals(123, node.get("value").asInt());

        node = jsonProcessingService.createJsonNode("key", "boolean", true);
        assertTrue(node.get("value").asBoolean());

        node = jsonProcessingService.createJsonNode("key", "double", 45.67);
        assertEquals(45.67, node.get("value").asDouble());

        node = jsonProcessingService.createJsonNode("key", "long", 123456789L);
        assertEquals(123456789L, node.get("value").asLong());

        node = jsonProcessingService.createJsonNode("key", "long", 'A');
        assertEquals("A", node.get("value").asText());
    }

    // Tests for setPrimitiveValue with invalid primitive types
    @Test
    void testSetPrimitiveValue_withInvalidPrimitiveValues() {
        ObjectNode node = jsonProcessingService.createJsonNode("key", "integer", "invalidInteger");
        assertEquals("invalidInteger", node.get("value").asText());

        node = jsonProcessingService.createJsonNode("key", "string", true);
        assertEquals("true", node.get("value").asText());

        node = jsonProcessingService.createJsonNode("key", "double", "invalidDouble");
        assertEquals("invalidDouble", node.get("value").asText());

        node = jsonProcessingService.createJsonNode("key", "long", "invalidLong");
        assertEquals("invalidLong", node.get("value").asText());
    }

    // Tests for setPrimitiveValue with null value
    @Test
    void testSetPrimitiveValue_withNullValue() {
        ObjectNode node = jsonProcessingService.createJsonNode("key", "string", null);
        assertTrue(node.has("value"));
        assertTrue(node.get("value").isEmpty());
    }

    // Tests for setPrimitiveValue with unsupported types
    @Test
    void testSetPrimitiveValue_withUnsupportedType() {
        ObjectNode node = jsonProcessingService.createJsonNode("key", "float", 12.34f);
        assertEquals("12.34", node.get("value").asText());

        node = jsonProcessingService.createJsonNode("key", "object", new Object());
        assertNotNull(node.get("value"));
        assertEquals("java.lang.Object@", node.get("value").asText().substring(0, 17));
    }

    // Tests for handleComplexType with supported types (List and Map)
    @Test
    void testHandleComplexType_withList() {
        List<Integer> list = List.of(1, 2, 3);
        ObjectNode node = jsonProcessingService.createJsonNode("key", "arraylist", list);
        assertTrue(node.get("value").isArray());
        assertEquals(3, node.get("value").size());
    }

    @Test
    void testHandleComplexType_withMap() {
        Map<String, String> map = Map.of("key1", "value1", "key2", "value2");
        ObjectNode node = jsonProcessingService.createJsonNode("key", "object", map);
        assertTrue(node.get("value").isObject());
        assertEquals("value1", node.get("value").get("key1").asText());
    }

    // Tests for handleComplexType with unsupported types and exceptions
    @Test
    void testHandleComplexType_withUnsupportedType() {
        Object unsupportedObject = new Object();
        ObjectNode node = jsonProcessingService.createJsonNode("key", "object", unsupportedObject);
        assertTrue(node.has("value"));
        assertEquals(unsupportedObject.toString(), node.get("value").asText());
    }

    @Test
    void testHandleComplexType_withException() {
        Object object = new Object() {
            @Override
            public String toString() {
                throw new RuntimeException("Test exception");
            }
        };
        ObjectNode node = jsonProcessingService.createJsonNode("key", "object", object);
        assertTrue(node.has("value"));
        assertTrue( node.get("value").asText().isEmpty());
    }

    // Tests for safeToString
    @Test
    void testSafeToString_withValidValue() {
        String result = jsonProcessingService.createJsonNode("key", "object", "test").get("value").asText();
        assertEquals("test", result);
    }

    @Test
    void testSafeToString_withNullValue() {
        ObjectNode node = jsonProcessingService.createJsonNode("key", "object", null);
        assertTrue(node.has("value"));
        assertTrue(node.get("value").isNull());
    }

    // Tests for createJsonNode with primitive types
    @Test
    void testCreateJsonNode_withPrimitiveTypes() {
        ObjectNode node = jsonProcessingService.createJsonNode("name", "string", "value");
        assertEquals("value", node.get("value").asText());

        node = jsonProcessingService.createJsonNode("name", "integer", 42);
        assertEquals(42, node.get("value").asInt());
    }

    // Tests for createJsonNode with complex types
    @Test
    void testCreateJsonNode_withComplexTypes() {
        ObjectNode node = jsonProcessingService.createJsonNode("name", "object", Map.of("key", "value"));
        assertTrue(node.get("value").isObject());
        assertEquals("value", node.get("value").get("key").asText());

        node = jsonProcessingService.createJsonNode("name", "arraylist", List.of(1, 2, 3));
        assertTrue(node.get("value").isArray());
        assertEquals(3, node.get("value").size());
    }

    // Tests for createJsonNode with unsupported types and exceptions
    @Test
    void testCreateJsonNode_withUnsupportedType() {
        ObjectNode node = jsonProcessingService.createJsonNode("name", "object", new Object());
        assertNotNull(node.get("value"));
        assertNotNull(node.get("value").asText());
    }

    @Test
    void testCreateJsonNode_handleComplexType_withException() {
        Object value = new Object() {
            @Override
            public String toString() {
                throw new RuntimeException("Test exception in toString");
            }
        };

        ObjectNode node = jsonProcessingService.createJsonNode("testName", "object", value);
        assertTrue(node.has("value"));
        assertTrue(node.get("value").asText().isEmpty());
    }

    // Tests for createJsonNode with non-null value
    @Test
    void testCreateJsonNode_withNonNullValue() {
        ObjectNode node = jsonProcessingService.createJsonNode("name", "custom", "testValue");
        assertEquals("testValue", node.get("value").asText());
    }
}
