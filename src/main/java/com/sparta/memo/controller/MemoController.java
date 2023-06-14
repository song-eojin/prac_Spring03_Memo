package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MemoController {

    //현재 우리의 DB 역할
    private final Map<Long, Memo> memoList = new HashMap<>();

    //메모 생성하기 기능 (Post)
    @PostMapping("memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) { //데이터가 넘어오는 형태가 Body 부분에 JSON 형태이므로 매개변수의 타입은 @RequestBody 이어야 한다.

        //RequestDto -> Entity 로 변경
        //데이터로 저장하기 위해서
        Memo memo = new Memo(requestDto);

        //Memo 의 MaxId 값을 찾기
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1 ;
        memo.setId(maxId);

        //DB 저장
        memoList.put(memo.getId(), memo);

        //Entity -> ResponseDto 로 변경
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }


    //메모 조회하기 기능 (Get)
    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        List<MemoResponseDto> responseList = memoList.values().stream().map(com.sparta.memo.dto.MemoResponseDto::new).toList();

        return responseList;
    }


    //메모 변경하기 기능 (Put)
    @PutMapping("/memos/{id}") //UPDATE 하는 API 는 PUT

    //@RequestBody 는 JSON 형식 데이터를 의미
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {


        //해당 메모가 DB에 존재하는지 확인 (반환 타입은 boolean)
        if(memoList.containsKey(id)){
            //해당 메모 가져오기 (memoList 는 현재 DB 역할담당)
            Memo memo = memoList.get(id);

            //memo 수정
            memo.update(requestDto);
            return memo.getId();

        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    //메모 삭제하기 기능 (Delete)
    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        //해당 메모가 DB에 존재하는지 확인
        if(memoList.containsKey(id)){
            //해당 메모 삭제하기
            memoList.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

}
