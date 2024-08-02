package org.mindera.fur.code.dto.formsDTO;

import lombok.Data;

@Data
public class FormFieldDTO {

    private Long id;
    private String question;
    private String type;
    private String answer;

    public FormFieldDTO(String s, String text, String s1) {
        this.question = s;
        this.type = text;
        this.answer = s1;
    }
    //  private String label;
    //  private String placeholder;
}
