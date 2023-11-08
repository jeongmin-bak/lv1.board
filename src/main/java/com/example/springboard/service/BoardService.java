package com.example.springboard.service;

import com.example.springboard.dto.BoardRequestDto;
import com.example.springboard.dto.BoardResponseDto;
import com.example.springboard.entity.Board;
import com.example.springboard.repository.BoardRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    private final JdbcTemplate jdbcTemplate;

    public BoardService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        Board board = new Board(requestDto);

        // DB 저장
        BoardRepository boardRepository = new BoardRepository(jdbcTemplate);
        Board saveBoard = boardRepository.save(board);

        // Entity -> ResponseDto
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);

        return boardResponseDto;
    }

    public List<BoardResponseDto> getLists() {
        BoardRepository boardRepository = new BoardRepository(jdbcTemplate);
        return boardRepository.findAll();
    }

    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto) {
        BoardRepository boardRepository = new BoardRepository(jdbcTemplate);
        Board board = boardRepository.findById(id);
        if(board != null) {
            // memo 내용 수정, 비밀번호 유효성 검사
            if (boardRepository.isValidPassword(id, requestDto.getPassword())){
                boardRepository.update(id, requestDto);
                return new BoardResponseDto(board);
            }else{
                throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
            }

        } else {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
        }
    }

    public BoardResponseDto detailBoard(Long id) {
        BoardRepository boardRepository = new BoardRepository(jdbcTemplate);
        Board board = boardRepository.findById(id);
        if(board != null){
            return new BoardResponseDto(board);
        }else{
            throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
        }
    }

    public BoardResponseDto deleteBoard(Long id, String password){
        BoardRepository boardRepository = new BoardRepository(jdbcTemplate);

        Board board = boardRepository.findById(id);
        if(board != null) {
            // 게시물 삭제
            if (boardRepository.isValidPassword(id, password)){
                boardRepository.delete(id);
                return new BoardResponseDto(board);
            }else{
                throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
        }
    }



}
