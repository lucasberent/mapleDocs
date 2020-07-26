package com.mapledocs.api.dto.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.everit.json.schema.ValidationException;

import java.util.List;

@Data
@AllArgsConstructor
public class SchemaValidationExceptionDTO {
    private List<ValidationException> validationExceptionList;
}
