package rpo.demo.todo.backend.util;

import rpo.demo.todo.backend.exception.JsonParsingException;

public interface JsonUtil {
    String objectToJson(Object object) throws JsonParsingException;
}
