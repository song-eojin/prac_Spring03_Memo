package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service //@Component (IoC 컨테이너에 Bean 객체로 등록) 포함
/*비지니스 로직 담당*/
public class MemoService {

    //MemoRepository memoRepository = new MemoRepository(jdbcTemplate); 중복 제거
    private final MemoRepository memoRepository;

    //@Autowired : 생성자 1개까지는 생략가능 단, Bean 일 경우만 가능
    //생성자 주입이 좋은 이유 : 객체의 불변성을 지켜준다
    public MemoService(MemoRepository memoRepository) {
        //수동으로 IoC Container 에 접근해서 Bean 이름으로 가져오는 방법 01
        //MemoRepository memoRepository = (MemoRepository) context.getBean("memoRepository");

        //수동으로 IoC Container 에 접근해서 Bean 클래스 형식으로 가져오는 방법 02
        //MemoRepository memoRepository = context.getBean(MemoRepository.class);
        
        //자동으로 가져오기
        this.memoRepository=memoRepository;
    }


    public MemoResponseDto createMemo(MemoRequestDto requestDto) {

        //RequestDto -> Entity 로 변경
        //데이터로 저장하기 위해서
        Memo memo = new Memo(requestDto);


        //DB 저장
        Memo saveMemo = memoRepository.save(memo);


        //Entity -> ResponseDto 로 변경
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);
        return memoResponseDto;
    }


    public List<MemoResponseDto> getMemos() {
        //DB 조회
        return memoRepository.findAll();

    }

    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        //DB 변경

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
