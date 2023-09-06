package squad.board.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import squad.board.domain.board.Board;
import squad.board.domain.comment.Comment;
import squad.board.exception.board.BoardException;
import squad.board.exception.board.BoardStatus;
import squad.board.exception.comment.CommentException;
import squad.board.repository.BoardMapper;
import squad.board.repository.CommentMapper;

import static squad.board.exception.comment.CommentStatus.INVALID_DELETE_UPDATE;

@Aspect
@Component
@RequiredArgsConstructor
public class ValidOriginWriterAop {

    private final CommentMapper commentMapper;
    private final BoardMapper boardMapper;

    @Around(value = "@annotation(squad.board.aop.CommentWriterAuth) && args(commentId,memberId,..)", argNames = "joinPoint,commentId,memberId")
    public Object commentWriterAuth(ProceedingJoinPoint joinPoint, Long commentId, Long memberId) throws Throwable {
        Comment comment = commentMapper.findByCommentId(commentId);
        if (!comment.getMemberId().equals(memberId)) {
            throw new CommentException(INVALID_DELETE_UPDATE);
        }
        return joinPoint.proceed();
    }

    @Around(value = "@annotation(squad.board.aop.BoardWriterAuth) && args(boardId,memberId,..)", argNames = "joinPoint,boardId,memberId")
    public Object boardWriterAuth(ProceedingJoinPoint joinPoint, Long boardId, Long memberId) throws Throwable {
        Board board = boardMapper.findById(boardId);
        if (!board.getMemberId().equals(memberId)) {
            throw new BoardException(BoardStatus.INVALID_DELETE_UPDATE);
        }
        return joinPoint.proceed();
    }
}
