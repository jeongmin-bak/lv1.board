package com.example.springboard.dto;

import com.example.springboard.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private String date;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.username = board.getUsername();
        this.contents = board.getContents();
        this.date = board.getDate();
    }
}
