package squad.board.dto.board;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
public class BoardUpdateRequest {
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
    private List<ImageInfoRequest> imageInfoList;

    public BoardUpdateRequest(String title, String content, List<ImageInfoRequest> imageInfo) {
        this.title = title;
        this.content = content.replace(".com/tmp/", ".com/original/");
        this.imageInfoList = imageInfo;
    }

    public List<String> checkDeletedImage(List<String> originalUuid) {
        List<String> requestUuid = extractImageUuid();
        return originalUuid.stream()
                .filter(o -> isNoneMatchWithRegex(requestUuid, o))
                .toList();
    }

    private boolean isNoneMatchWithRegex(List<String> requestUuid, String o) {
        return requestUuid.stream().noneMatch(Predicate.isEqual(o));
    }

    private List<String> extractImageUuid() {
        List<String> uuid = new ArrayList<>();
        String regex = "(?<=\\boriginal\\/)(.*?)(?=\\\")";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            uuid.add(matcher.group());
        }
        return uuid;
    }
}
