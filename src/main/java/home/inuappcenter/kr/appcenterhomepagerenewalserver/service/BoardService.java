package home.inuappcenter.kr.appcenterhomepagerenewalserver.service;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.Board;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.Image;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.IntroBoard;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.PhotoBoard;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.PhotoBoardRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.IntroBoardRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.PhotoBoardResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.IntroBoardResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.BoardRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.ImageRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.IntroBoardRepository;
import javax.servlet.http.HttpServletRequest;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.PhotoBoardRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.utils.BoardUtils;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.exception.customExceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository<Board> boardRepository;

    private final IntroBoardRepository introBoardRepository;
    private final PhotoBoardRepository photoBoardRepository;

    private final ImageRepository imageRepository;
    private final HttpServletRequest request;

    @Transactional
    // (앱) 게시글 조회하기
    public IntroBoardResponseDto<List<String>> getIntroBoard(Long id) {
        IntroBoard foundBoard = introBoardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        List<Image> ImageList = foundBoard.getImages();

        return new IntroBoardResponseDto<>(
                foundBoard.getId(),
                foundBoard.getTitle(),
                foundBoard.getSubTitle(),
                foundBoard.getAndroidStoreLink(),
                foundBoard.getIOSStoreLink(),
                foundBoard.getBody(),
                BoardUtils.returnImageURL(request, ImageList)
        );
    }

    @Transactional
    // (앱) 게시글 저장하기
    public IntroBoardResponseDto<List<Long>> saveIntroBoard(IntroBoardRequestDto introBoardRequestDto) {
        // IntroBoardRequestDTO 내용이 담긴 IntroBoard 타입의 인스턴스를 생성
        IntroBoard introBoard = new IntroBoard(introBoardRequestDto);
        // introBoard를 저장
        boardRepository.save(introBoard);

        // imageRequestDto에 포함된 List<MultiPartFile>을 List<Image>로 변환 / 매핑을 위해 introBoard도 포함하여 함께 저장
        List<Image> imageList = new <IntroBoard>Image().toImageListWithMapping(introBoardRequestDto.getMultipartFiles(), introBoard);
        List<Image> savedImage = imageRepository.saveAll(imageList);

        return new IntroBoardResponseDto<>(
                introBoard.getId(),
                introBoard.getTitle(),
                introBoard.getSubTitle(),
                introBoard.getAndroidStoreLink(),
                introBoard.getIOSStoreLink(),
                introBoard.getBody(),
                BoardUtils.returnImageId(savedImage)
        );
    }

    @Transactional
    // (앱) 게시글 수정
    public IntroBoardResponseDto<List<Long>> updateIntroBoard(IntroBoardRequestDto introBoardRequestDto, Long board_id) {
        IntroBoard foundBoard = introBoardRepository.findById(board_id).orElseThrow(()-> new CustomNotFoundException("The requested ID was not found."));
        foundBoard.updateIntroBoard(introBoardRequestDto);
        List<Image> foundImg = imageRepository.findByIntroBoard(foundBoard);

        // 찾은 이미지에 내용 대입
        for(Image image: foundImg) {
            log.info(image.getOriginalFileName());
            for(MultipartFile multipartFile: introBoardRequestDto.getMultipartFiles()) {
                image.setImage(multipartFile);
            }
        }

        IntroBoard introBoard = introBoardRepository.save(foundBoard);
        List<Image> savedImage = imageRepository.saveAll(foundImg);

        return new IntroBoardResponseDto<>(
                        introBoard.getId(),
                        introBoard.getTitle(),
                        introBoard.getSubTitle(),
                        introBoard.getAndroidStoreLink(),
                        introBoard.getIOSStoreLink(),
                        introBoard.getBody(),
                        BoardUtils.returnImageId(savedImage)
                        );
    }

    @Transactional
    // (앱) 게시글 삭제
    public String deleteIntroBoard(Long id) {
        IntroBoard foundBoard = introBoardRepository.findById(id).orElseThrow();

        List<Image> ImageList = foundBoard.getImages();

        // 등록된 이미지 먼저 삭제
        for(Image image: ImageList) {
            imageRepository.deleteById(image.getId());
        }
        // 게시글 삭제
        introBoardRepository.deleteById(id);

        return "id: " + id + " has been successfully deleted.";
    }

    @Transactional
    // (앱) 모든 게시글 조회하기
    public List<IntroBoardResponseDto<String>> findAllIntroBoard() {
        List<IntroBoard> boardList = introBoardRepository.findAll();

        List<Image> thumbnailList = imageRepository.findAllByIsThumbnailTrue();

        return BoardUtils.returnIntroBoardResponseDtoList(boardList, thumbnailList, request);
    }

    @Transactional
    // (사진) 게시글 조회
    public PhotoBoardResponseDto<List<String>> getPhotoBoard(Long id) {
        PhotoBoard foundBoard = photoBoardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        List<Image> ImageList = foundBoard.getImages();
        return new PhotoBoardResponseDto<>(
                foundBoard.getId(),
                foundBoard.getBody(),
                BoardUtils.returnImageURL(request, ImageList)
        );
    }

    @Transactional
    // (사진) 게시글 저장
    public PhotoBoardResponseDto<List<Long>> savePhotoBoard(PhotoBoardRequestDto photoBoardRequestDto) {
        PhotoBoard photoBoard = new PhotoBoard();
        // imageRequestDto를 List<Image> 타입으로 변환 / 게시판 정보도 함께 포함해서 저장시킴
        List<Image> imageList = new <PhotoBoard>Image().toImageListWithMapping(photoBoardRequestDto.getMultipartFiles(), photoBoard);
        // introBoardRequestDto를 introBoard 타입으로 변환
        photoBoard.setPhotoBoard(photoBoardRequestDto);
        // introBoard를 저장
        boardRepository.save(photoBoard);
        // 첫번째 이미지는 isThumbnail을 true로 변경
        imageList.get(0).isThumbnail();
        List<Image> savedImage = imageRepository.saveAll(imageList);
        List<Long> imageIds = new ArrayList<>();
        for(Image image: savedImage) {
            imageIds.add(image.getId());
        }
        return new PhotoBoardResponseDto<>(
                photoBoard.getId(),
                photoBoard.getBody(),
                imageIds
        );
    }

    @Transactional
    // (사진) 게시글 삭제
    public String deletePhotoBoard(Long id) {
        PhotoBoard foundBoard = photoBoardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));

        List<Image> ImageList = foundBoard.getImages();

        // 등록된 이미지 먼저 삭제
        for(Image image: ImageList) {
            imageRepository.deleteById(image.getId());
        }
        // 게시글 삭제
        boardRepository.deleteById(id);

        return "id: " + id + " has been successfully deleted.";
    }

    @Transactional
    // (사진) 모든 게시글 조회하기
    public List<PhotoBoardResponseDto<String>> findAllPhotoBoard() {
        List<PhotoBoard> boardList = photoBoardRepository.findAll();

        List<Image> thumbnailList = imageRepository.findAllByIsThumbnailTrue();

        return BoardUtils.returnPhotoBoardResponseDtoList(boardList, thumbnailList, request);
    }
}
