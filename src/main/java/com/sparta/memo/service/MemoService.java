package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/*비지니스 로직 담당*/
public class MemoService {

    private final JdbcTemplate jdbcTemplate;

    public MemoService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {

        //RequestDto -> Entity 로 변경
        //데이터로 저장하기 위해서
        Memo memo = new Memo(requestDto);


        //DB 저장
        MemoRepository memoRepository = new MemoRepository(jdbcTemplate);
        Memo saveMemo = memoRepository.save(memo);


        //Entity -> ResponseDto 로 변경
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);
        return memoResponseDto;
    }


    public List<MemoResponseDto> getMemos() {
        //DB 조회
        MemoRepository memoRepository = new MemoRepository(jdbcTemplate);
        return memoRepository.findAll();

    }

    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        //DB 변경
        MemoRepository memoRepository = new MemoRepository(jdbcTemplate);

        //해당 메모가 DB에 존재하는지 확인 (반환 타입은 boolean)
        Memo memo = memoRepository.findById(id);

        //if(memoList.containsKey(id)){
        if(memo != null) {
            //해당 메모 가져오기 (memoList 는 현재 DB 역할담당)
            //Memo memo = memoList.get(id);

            //memo 수정
            //memo.update(requestDto);
            memoRepository.update(id, requestDto);

            //return memo.getId();
            return id;

        }
        else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    public Long deleteMemo(Long id) {
        MemoRepository memoRepository = new MemoRepository(jdbcTemplate);

        //해당 메모가 DB에 존재하는지 확인
        Memo memo = memoRepository.findById(id);

        //if(memoList.containsKey(id)){
        if(memo != null) {

            //해당 메모 삭제하기
            //memoList.remove(id);
            memoRepository.delete(id);

            return id;
        }
        else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }


}
