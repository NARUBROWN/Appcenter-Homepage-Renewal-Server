//package server.inuappcenter.kr.service.board;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import server.inuappcenter.kr.data.domain.board.Board;
//import server.inuappcenter.kr.data.domain.board.FaqBoard;
//import server.inuappcenter.kr.data.dto.request.FaqBoardRequestDto;
//import server.inuappcenter.kr.data.dto.response.FaqBoardResponseDto;
//import server.inuappcenter.kr.data.repository.FaqRepository;
//import server.inuappcenter.kr.service.boardService.BoardService;
//import server.inuappcenter.kr.service.boardService.FaqBoardService;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//public class FaqServiceTest {
//    @Mock
//    private FaqRepository faqRepository;
//    @Mock
//    private BoardService boardService;
//    @InjectMocks
//    private FaqBoardService faqBoardService;
//
//    private final Long givenId = 1L;
//    private final List<FaqBoard> expectedFaqBoardList = new ArrayList<>();
//    private final List<FaqBoardResponseDto> expectedFaqBoardResponseDtoList = new ArrayList<>();
//    private final Board expectedBoard = new FaqBoard(new FaqBoardRequestDto(
//            "서버", "질문입니다.", "답변입니다."
//    ));
//    private final FaqBoard expectedFaqBoard = new FaqBoard(new FaqBoardRequestDto(
//            "서버", "질문입니다.", "답변입니다."
//    ));
//    private final FaqBoardResponseDto expectedResult = FaqBoardResponseDto.entityToDto(expectedBoard);
//
//    @DisplayName("FAQ 게시글 가져오기 테스트")
//    @Test
//    public void getFaqBoardListTest() {
//        // given
//        for (int i = 1; i <= 10; i++) {
//            expectedFaqBoardList.add(expectedFaqBoard);
//        }
//        for (int i = 0; i < 10; i++) {
//            expectedFaqBoardResponseDtoList.add(FaqBoardResponseDto.entityToDto(expectedFaqBoardList.get(i)));
//        }
//        given(faqRepository.findAll()).willReturn(expectedFaqBoardList);
//        // when
//        List<FaqBoardResponseDto> result = faqBoardService.getFaqBoardList();
//        // then
//        for(int i = 0; i < 9; i++) {
//            assertEquals(expectedFaqBoardResponseDtoList.get(i).getId(), result.get(i).getId());
//        }
//    }
//
//}
