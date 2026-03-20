package com.qjprojects.AA_History.Utility;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Converter
public class JsonMapConverter implements AttributeConverter<Map<String, Object>, String> {
    private static final ObjectMapper mapper = new ObjectMapper();


    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try{

            return attribute == null? null : mapper.writeValueAsString(attribute);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
       try{

           return dbData == null? null : mapper.readValue(dbData, new TypeReference<Map<String, Object>>() {});

       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }
}
