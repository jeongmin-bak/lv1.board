package com.example.springboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
public class BoardRequestDto {
    private Long id;
    private String title;
    private String username;
    private String password;
    private String contents;
    private Date date;

}