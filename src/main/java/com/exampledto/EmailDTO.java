package com.exampledto;



import lombok.Data;

@Data
public class EmailDTO {
    private String to;
    private String subject;
    private String body;
    private String enquiryId;
    private String firstName;
}
