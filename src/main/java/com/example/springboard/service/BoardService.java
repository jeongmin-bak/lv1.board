package com.example.springboard.service;

import com.example.springboard.entity.Board;
import com.example.springboard.repository.BoardRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    private final JdbcTemplate jdbcTemplate;

    public BoardService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long deleteBoard(Long id){
        BoardRepository boardRepository = new BoardRepository(jdbcTemplate);

        Board board = boardRepository.findById(id);
        if(board != null) {
            // 게시물 삭제
            boardRepository.delete(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }
}
