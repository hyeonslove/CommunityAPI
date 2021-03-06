package service.interfaces;

import domain.dto.BoardEmotionDTO;
import domain.dto.BoardDTO;
import domain.param.BoardSearchModel;
import response.BaseResponse;

import java.util.List;

public interface IBoardService {
    BaseResponse createBoard(BoardDTO board) throws Exception;

    List<BoardDTO> getSummaryBoardList(BoardSearchModel board) throws Exception;

    BoardDTO getBoard(Long board_uid) throws Exception;

    BaseResponse updateBoard(BoardDTO board) throws Exception;

    BaseResponse deleteBoard(Long uid) throws Exception;

    BaseResponse createBoardEmotion(Long board_uid, Integer status) throws Exception;

    BaseResponse deleteBoardEmotion(Long board_uid) throws Exception;

    List<BoardEmotionDTO> getBoardEmotion(Long board_uid) throws Exception;
}
