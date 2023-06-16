package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service //@Component (IoC 컨테이너에 Bean 객체로 등록) 포함
/*비지니스 로직 담당*/
public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }


    public MemoResponseDto createMemo(MemoRequestDto requestDto) {

        //RequestDto -> Entity 로 변경
        Memo memo = new Memo(requestDto);


        //DB 저장
        Memo saveMemo = memoRepository.save(memo);


        //Entity -> ResponseDto 로 변경
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);
        return memoResponseDto;
    }


    public List<MemoResponseDto> getMemos() {
        //DB 조회
        return memoRepository.findAllByOrderByModifiedAtDesc().stream().map(MemoResponseDto::new).toList();
    }

    @Transactional
    //부모 메서드
    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        //DB 변경

        //해당 메모가 DB에 존재하는지 확인 (반환 타입은 boolean)
        //Optional 처리
        Memo memo = findMemo(id);

        //if(memoList.containsKey(id)){
        memo.update(requestDto);

        //return memo.getId();
        return id;
    }

    public Long deleteMemo(Long id) {

        //해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);

        //해당 메모 삭제하기
        memoRepository.delete(memo);

        return id;

    }

    private Memo findMemo(Long id) {
        return memoRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다.")
        );
    }

}
