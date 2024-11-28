package com.lec.spring.repository;

import com.lec.spring.domain.Attachment;

import java.util.List;
import java.util.Map;
/*TODO
*  Repository
*  데이터베이스와의 상호작용을 당담하는 계층이다 crud작업을 수행하는메서드를 정의한다.
*  Spring Data JPA 와 같은 기술을 사용하여 데이터 베이스와의 연결을 간편하게 처리 할 수 있다.
*  repository는 도메인 객체를 데이터베이스에 저장하거나 조회하는 등의 작업을 수ㅐㅇ하며 비즈니스 로직과 데이터 접근 로직을 분리하는데
*  도음을 준다. */

public interface AttachmentRepository {
    /**
     * 특정 글(postId) 에 첨부파일(들) 추가 INSERT
     * 글 insert, update 시 사용됨.
     * @param list :  Map<String, Object> 들의 List
     *                      ↓        ↓
     *                   <"sourcename",원본파일명>
     *                   <"filename", 저장된파일명>
     * @param postId : 첨부될 글
     * @return : DML 수행 결과값
     */
    int insert(List<Map<String, Object>> list, Long postId);
    /*todo
    *  특정 글에 첨부파일을 추가하는 메소드이당. Map<String(원본 파일명), Object(저장된 파일명) >
    *  long postId -> 첨부파일이 추가될 글의 id이다.
    *  ** map **
    *   map을 사용하면 다양한 형태의 데이터를 유연하게 저장 할 수 있다 . 위에 코드의 경우 첨부파일의 정보를 담기 위해 원본 파일명과 저장된파일 명 이라는 두개의 키를 사용한다.
    *  list 형태로 여러개의 map을 리스트에 담아 전달함으로 써 여러 첨부파일으 ㅣ정보를 한 번에 처리할 수 있다.
    * */

    // 첨부파일 한개 저장 INSERT
    int save(Attachment file);
    /*todo
    *  단일 첨부파일을 데이터베이스에 저장하는 메서드입니다.
    *  file 저장할 첨부파일 객체입니다*/

    // 특정 글(postId) 의 첨부파일들 SELECT
    List<Attachment> findByPost(Long postId);
    /*todo
     *  단일 첨부파일을 데이터베이스에 저장하는 메서드입니다.
     *  file 저장할 첨부파일 객체입니다*/

    // 특정 첨부파일(id) 한개 SELECT
    Attachment findById(Long id);

    // 선택된 첨부파일들 SELECT
    // 글 수정 에서 사용
    List<Attachment> findByIds(Long [] ids);

    // 선택된 첨부파일들 DELETE
    // 글 수정 에서 사용
    int deleteByIds(Long [] ids);

    // 특정 첨부 파일(file)을 DB에서 DELETE
    int delete(Attachment file);
}










