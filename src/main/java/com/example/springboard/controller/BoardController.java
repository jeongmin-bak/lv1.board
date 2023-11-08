package com.example.springboard.controller;

import com.example.springboard.dto.BoardRequestDto;
import com.example.springboard.dto.BoardResponseDto;
import com.example.springboard.entity.Board;
import com.example.springboard.service.BoardService;
import lombok.val;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        BoardService boardService = new BoardService(jdbcTemplate);
        return boardService.createBoard(requestDto);
    }

    // 목록보기
    @GetMapping("/list")
    public List<BoardResponseDto> getLists(){
        BoardService boardService = new BoardService(jdbcTemplate);
        return boardService.getLists();

    }

    //게시물 수정하기
    @PutMapping("/list/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto){
        BoardService boardService = new BoardService(jdbcTemplate);
        return boardService.updateBoard(id, requestDto);
    }

    @DeleteMapping("/detail/{id}")
    public BoardResponseDto deleteMemo(@PathVariable Long id, @RequestBody BoardRequestDto requestDto){
        BoardService boardService = new BoardService(jdbcTemplate);
        return boardService.deleteBoard(id, requestDto.getPassword());
    }





}
