package squad.board.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageMapper {
    void save(Long boardId, String fileName);

    // 이미지 파일이름 조회
    String findImageFileName(Long boardId);

}
