package services;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Converter {
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public <T> T obtenerDatos(String json, Class<T> clase) {
        try{
            return objectMapper.readValue(json, clase);
        }catch (JsonProcessingException ex){
            throw new RuntimeException(ex);
        }
    }
}
