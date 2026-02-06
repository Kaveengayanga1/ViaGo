package com.viago.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest implements Serializable {
    private String to;
    private String subject;
    private String templateName;
    private Map<String, Object> variables;
}
