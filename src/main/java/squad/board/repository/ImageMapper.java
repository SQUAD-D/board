package squad.board.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import squad.board.dto.board.ImageInfoRequest;

import java.util.List;

@Mapper
public interface ImageMapper {
    void save(@Param("list") List<ImageInfoRequest> imageInfo, Long boardId);

    // 이미지 UUID 로 파일이름 조회
    List<String> findImageUuid(Long boardId);

    void deleteByBoardId(Long boardId);

    void deleteByImageUuid(List<String> uuid);
}

