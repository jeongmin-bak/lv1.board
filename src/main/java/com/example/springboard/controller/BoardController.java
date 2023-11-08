package com.example.springboard.controller;

import com.example.springboard.dto.BoardRequestDto;
import com.example.springboard.dto.BoardResponseDto;
import com.example.springboard.entity.Board;
import com.example.springboard.service.BoardService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

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

    //게시물 조회하기
    @GetMapping("list/{id}")
    public BoardResponseDto detailBoard(@PathVariable Long id){
        BoardService boardService = new BoardService(jdbcTemplate);
        return boardService.detailBoard(id);
    }

    @DeleteMapping("/list/{id}")
    public BoardResponseDto deleteMemo(@PathVariable Long id, @RequestBody BoardRequestDto requestDto){
        BoardService boardService = new BoardService(jdbcTemplate);
        return boardService.deleteBoard(id, requestDto.getPassword());
    }





}
