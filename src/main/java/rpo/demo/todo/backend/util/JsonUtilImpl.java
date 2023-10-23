package rpo.demo.todo.backend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import rpo.demo.todo.backend.exception.JsonParsingException;

@Service
public class JsonUtilImpl implements JsonUtil {
    private final ObjectMapper objectMapper;

    public JsonUtilImpl() {
        this.objectMapper = new ObjectMapper();
    }

    public String objectToJson(Object object) throws JsonParsingException {
        try {
            return this.objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonParsingException(e);
        }
    }
}
