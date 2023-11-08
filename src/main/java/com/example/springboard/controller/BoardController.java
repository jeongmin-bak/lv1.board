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
    public Long updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto){
        Board board = findById(id);
        if(board != null) {
            // memo 내용 수정
            // 비밀번호 유효성 검사
            if (isValidPassword(id, requestDto.getPassword())){
                String sql = "UPDATE board SET title = ?, username = ?, contents = ? WHERE id = ?";
                jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getUsername(), requestDto.getContents(), id);
                return id;
            }else{
                throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
            }

        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/detail/{id}")
    public Long deleteMemo(@PathVariable Long id){
        BoardService boardService = new BoardService(jdbcTemplate);
        return boardService.deleteBoard(id);
    }

    private Board findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM board WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Board board = new Board();
                board.setTitle(resultSet.getString("title"));
                board.setUsername(resultSet.getString("username"));
                board.setContents(resultSet.getString("contents"));
                board.setContents(resultSet.getString("date"));
                return board;
            } else {
                return null;
            }
        }, id);
    }

    private boolean isValidPassword(Long id, String password){
        // DB 조회
        String sql = "SELECT * FROM board WHERE id = ? and password = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                return true;
            } else {
                return false;
            }
        }, id, password);
    }


}
