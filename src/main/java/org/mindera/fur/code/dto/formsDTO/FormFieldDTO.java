package org.mindera.fur.code.dto.formsDTO;

import lombok.Data;

@Data
public class FormFieldDTO {

    private Long id;
    private String question;
    private String type;
    private String answer;
    //  private String label;
    //  private String placeholder;
}
