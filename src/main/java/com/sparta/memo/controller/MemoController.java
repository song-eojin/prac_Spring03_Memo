package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MemoController {

    //Dummy DB
    //private final Map<Long, Memo> memoList = new HashMap<>();


    //실제 DB 연결 by MySQL
    private final JdbcTemplate jdbcTemplate;

    public MemoController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    //메모 생성하기 기능 (Post : CREATE)
    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
    //데이터가 넘어오는 형태가 Body 부분에 JSON 형태이므로 매개변수의 타입은 @RequestBody 이어야 한다.

        //RequestDto -> Entity 로 변경
        //데이터로 저장하기 위해서
        Memo memo = new Memo(requestDto);


        //Memo 의 MaxId 값을 찾기
        //Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1 ;
        //memo.setId(maxId);

        //기본 키를 반환받기 위한 객체
        KeyHolder keyHolder = new GeneratedKeyHolder();


        //DB 저장
        //memoList.put(memo.getId(), memo); ->
        String sql = "INSERT INTO memo (username, contents) VALUES (?,?)";

        jdbcTemplate.update(con ->
        {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, memo.getUsername());
            preparedStatement.setString(2, memo.getContents());
            return preparedStatement;
        }, keyHolder);


        //DB Insert 후 받아온 기본키를 확인
        Long id = keyHolder.getKey().longValue();
        memo.setId(id);


        //Entity -> ResponseDto 로 변경
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);
        return memoResponseDto;
    }


    //메모 조회하기 기능 (Get : READ)
    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        //Dummy DB
        //List<MemoResponseDto> responseList = memoList.values().stream().map(com.sparta.memo.dto.MemoResponseDto::new).toList();
        //return responseList;


        //실제 DB 조회
        String sql = "SELECT * FROM memo";

        return jdbcTemplate.query(sql, new RowMapper<MemoResponseDto>() {
            @Override
            public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                //SQL 의 결과로 받아온 Memo 의 데이터들을 MemoResponseDto 타입으로 변환해주는 메서드
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String contents = rs.getString("contents");

                return new MemoResponseDto(id, username, contents);
            }
        });
    }


    //메모 변경하기 기능 (Put : UPDATE)
    @PutMapping("/memos/{id}") //UPDATE 하는 API 는 PUT

    //@RequestBody 는 JSON 형식 데이터를 의미
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {


        //해당 메모가 DB에 존재하는지 확인 (반환 타입은 boolean)
        Memo memo = findById(id);

        //if(memoList.containsKey(id)){
        if(memo != null) {
            //해당 메모 가져오기 (memoList 는 현재 DB 역할담당)
            //Memo memo = memoList.get(id);

            //memo 수정
            //memo.update(requestDto);
            String sql = "UPDATE memo SET username = ?, contents = ? WHERE id = ? ";
            jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id);

            //return memo.getId();
            return id;

        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }


    //메모 삭제하기 기능 (Delete : DELETE)
    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {

        //해당 메모가 DB에 존재하는지 확인
        Memo memo = findById(id);

        //if(memoList.containsKey(id)){
        if(memo != null) {

            //해당 메모 삭제하기
            //memoList.remove(id);
            String sql = "DELETE FROM memo WHERE id = ?";
            jdbcTemplate.update(sql, id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }


    private Memo findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM memo WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Memo memo = new Memo();
                memo.setUsername(resultSet.getString("username"));
                memo.setContents(resultSet.getString("contents"));
                return memo;
            } else {
                return null;
            }
        }, id);
    }

}
