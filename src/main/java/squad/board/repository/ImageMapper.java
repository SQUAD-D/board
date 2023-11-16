package squad.board.repository;

import org.apache.ibatis.annotations.Mapper;
import squad.board.dto.board.ImageInfoRequest;

import java.util.List;

@Mapper
public interface ImageMapper {
    void save(ImageInfoRequest request, Long boardId);

    // 이미지UUID로 파일이름 조회
    List<String> findImageUUID(Long boardId);
}

