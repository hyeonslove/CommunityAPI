package service;

import domain.dto.AccountDTO;
import domain.dto.BoardEmotionDTO;
import domain.dto.BoardDTO;
import domain.dto.BoardSummaryDTO;
import enums.ErrorMessage;
import exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import repository.BoardMapper;
import response.BaseResponse;
import service.interfaces.IAuthService;
import service.interfaces.IBoardService;

import java.util.List;

@Service
public class BoardService implements IBoardService {
    /**
     * TODO MEMO: 페이지네이션 리팩토링 필요
     */
    @Autowired
    private AccountService accountService;

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private IAuthService authService;

    @Override
    public BaseResponse createBoard(BoardDTO board) throws Exception {
        AccountDTO servAccountDTO = authService.authUser();

        boardMapper.createBoard(servAccountDTO.getUid(), board);

        return new BaseResponse("게시글 등록에 성공했습니다.", HttpStatus.OK);
    }

    @Override
    public List<BoardSummaryDTO> getSummaryBoardList(int page, int range) throws Exception {
        page = (page - 1) * range;
        return boardMapper.getSummaryBoardList(page, range);
    }

    @Override
    public List<BoardSummaryDTO> searchSummaryBoardTitleBody(String title, String body, int page, int range) throws Exception {
        if (title.length() < 2 && body.length() < 2)
            throw new BaseException(ErrorMessage.SEARCH_WORD_LENGTH);

        if (title.length() < 2)
            title = null;
        if (body.length() < 2)
            body = null;

        page = (page - 1) * range;
        return boardMapper.searchSummaryBoardTitleBody(title, body, page, range);
    }

    @Override
    public List<BoardSummaryDTO> searchSummaryBoardNickName(String nickname, int page, int range) throws Exception {
        if (nickname.length() < 2)
            throw new BaseException(ErrorMessage.SEARCH_WORD_LENGTH);

        page = (page - 1) * range;
        return boardMapper.searchSummaryBoardNickName(nickname, page, range);
    }

    @Override
    public List<BoardSummaryDTO> searchSummaryBoardCommentNickName(String nickname, int page, int range) throws Exception {
        if (nickname.length() < 2)
            throw new BaseException(ErrorMessage.SEARCH_WORD_LENGTH);

        page = (page - 1) * range;
        return boardMapper.searchSummaryBoardCommentNickName(nickname, page, range);
    }

    @Override
    public BoardDTO getBoardInfo(Long board_uid) throws Exception {
        AccountDTO servAccountDTO = authService.authUser();

//        long user_uid = Long.parseLong(data.get("uid").toString());
        Long board_onwer = boardMapper.getAccountUid(board_uid);

        if (board_onwer == null)
            throw new BaseException(ErrorMessage.NOT_EXIST_BOARD);

        return boardMapper.getBoardInfo(board_uid);
    }

    @Override
    public BaseResponse updateBoard(BoardDTO board) throws Exception {
        AccountDTO servAccountDTO = authService.authUser();


        long user_uid = servAccountDTO.getUid();
        Long board_onwer = boardMapper.getAccountUid(board.getUid());

        if (board_onwer == null)
            throw new BaseException(ErrorMessage.NOT_EXIST_BOARD);


        if (board_onwer != user_uid)
            throw new BaseException(ErrorMessage.NOT_PERMISSION_EXCEPTION);

        boardMapper.updateBoard(board);

        return new BaseResponse("게시글 수정에 성공했습니다.", HttpStatus.OK);
    }

    @Override
    public BaseResponse deleteBoard(Long board_uid) throws Exception {
        AccountDTO servAccountDTO = authService.authUser();

        long user_uid = servAccountDTO.getUid();
        Long board_onwer = boardMapper.getAccountUid(board_uid);

        if (board_onwer == null)
            throw new BaseException(ErrorMessage.NOT_EXIST_BOARD);


        if (board_onwer != user_uid)
            throw new BaseException(ErrorMessage.NOT_PERMISSION_EXCEPTION);

        boardMapper.deleteBoard(board_uid);

        return new BaseResponse("게시글 삭제에 성공했습니다.", HttpStatus.OK);

    }

    @Override
    public BaseResponse createBoardEmotion(Long board_uid, Integer status) throws Exception {
        /**
         * TODO: 중복 예외 처리 필요
         * TODO: 공감 상태 enum 확인 필요
         */
        AccountDTO servAccountDTO = authService.authUser();
        if (!boardMapper.isBoard(board_uid))
            throw new BaseException(ErrorMessage.NOT_EXIST_BOARD);

        long user_uid = servAccountDTO.getUid();
        boardMapper.createBoardEmotion(board_uid, user_uid, status);

        return new BaseResponse("공감에 성공하였습니다.", HttpStatus.OK);
    }

    @Override
    public BaseResponse deleteBoardEmotion(Long board_uid) throws Exception {
        /**
         * TODO: 공감이 없는 것에 대한 삭제 확인
         */
        AccountDTO servAccountDTO = authService.authUser();

        if (!boardMapper.isBoard(board_uid))
            throw new BaseException(ErrorMessage.NOT_EXIST_BOARD);

        long user_uid = servAccountDTO.getUid();

        boardMapper.deleteBoardEmotion(board_uid, user_uid);

        return new BaseResponse("공감에 취소에 성공하였습니다.", HttpStatus.OK);

    }

    @Override
    public List<BoardEmotionDTO> getBoardEmotion(Long board_uid) throws Exception {
        AccountDTO servAccountDTO = authService.authUser();

        if (!boardMapper.isBoard(board_uid))
            throw new BaseException(ErrorMessage.NOT_EXIST_BOARD);

        return boardMapper.getBoardEmotion(board_uid);
    }
}
