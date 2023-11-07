package com.example.springboard.controller;

import com.example.springboard.dto.BoardRequestDto;
import com.example.springboard.dto.BoardResponseDto;
import com.example.springboard.entity.Board;
import lombok.val;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;


@RestController
@RequestMapping("/board")
public class BoardController {
    private final JdbcTemplate jdbcTemplate;

    public BoardController(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/create")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto){
        // requestdto -> entity 로
        Board board = new Board(requestDto);

        //DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // board 날짜
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM");
        board.setDate(dateTime.format(formatter));

        String sql = "INSERT INTO board (title, username, password, contents, date) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, board.getTitle());
            preparedStatement.setString(2, board.getUsername());
            preparedStatement.setString(3, board.getPassword());
            preparedStatement.setString(4, board.getContents());
            preparedStatement.setString(5, board.getDate());
            return preparedStatement;
            },
            keyHolder);

        Long id = keyHolder.getKey().longValue();
        board.setId(id);

        // Entity -> ResponseDto
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);

        return boardResponseDto;
    }

    // 목록보기
    @GetMapping("/list")
    public List<BoardResponseDto> getLists(){
        return null;
    }

    //게시물 수정하기
    @PutMapping("/list/{id}")
    public Long updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto){
        return null;
    }

    @DeleteMapping("/detail/{id}")
    public Long deleteMemo(@PathVariable Long id){
        return null;
    }


}
