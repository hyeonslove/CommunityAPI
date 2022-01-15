package repository;

import domain.entity.BoardEmotionEntity;
import domain.entity.BoardEntity;
import domain.dto.BoardDTO;
import domain.entity.BoardSummaryEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardMapper {
    void createBoard(@Param(value = "uid") Long uid, @Param(value = "board") BoardDTO board);

    List<BoardSummaryEntity> searchSummaryBoardTitle(@Param(value = "word") String word, @Param(value = "page") int page, @Param(value = "range") int range);

    List<BoardSummaryEntity> getSummaryBoardList(@Param(value = "page") int page, @Param(value = "range") int range);

    BoardEntity getBoardInfo(@Param(value = "board_uid") Long board_uid);

    void updateBoard(@Param(value = "board") BoardDTO board);

    void deleteBoard(@Param(value = "board_uid") Long board_uid);

    Long getAccountUid(@Param(value = "board_uid") Long board_uid);

    Long getBoardListCnt();

    void createBoardEmotion(@Param(value = "board_uid") Long board_uid, @Param(value = "account_uid") Long account_uid, @Param(value = "status") Integer status);

    void deleteBoardEmotion(@Param(value = "board_uid") Long board_uid, @Param(value = "account_uid") Long account_uid);

    List<BoardEmotionEntity> getBoardEmotion(@Param(value = "board_uid") Long board_uid);

    Boolean isBoard(@Param(value = "board_uid") Long board_uid);
}
