package com.team8.Spring_Project.application;

import com.team8.Spring_Project.application.dto.NoticeDto;
import com.team8.Spring_Project.application.dto.UserService;
import com.team8.Spring_Project.domain.Notice;
import com.team8.Spring_Project.infrastructure.persistence.NoticeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserService userService;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository,
                         UserService userService) {
        this.noticeRepository = noticeRepository;
        this.userService = userService;
    }

    // 공지사항 리스트
    @Transactional(readOnly = true)
    public List<NoticeDTO> getAllNotices() {

        // 여기서 정렬하거나 or DB에서 정렬해서 받아오거나
        return noticeRepository.findAll().stream()
                .sorted(Comparator.comparing(Notice::getUpdatedAt).reversed())
                .limit(5)
                .map(NoticeDTO::fromEntity)
                .collect(Collectors.toList());

    }

    // 공지사항 상세보기
    @Transactional(readOnly = true)
    public NoticeDto getNoticeById(Long id) {

        return noticeRepository.findById(id)
                .map(NoticeDto::fromEntity)
                .orElse(null);

    }

    // 공지사항 생성
    @Transactional
    public NoticeDTO createNotice(NoticeDTO noticeDto) {

        User user = userService.findUserEntity(noticeDto.getUserId());

        Notice notice = noticeDto.toEntity(user);
        noticeRepository.save(notice);

        return NoticeDTO.fromEntity(notice);

    }

    @Transactional
    public void updateNotice(Long id, NoticeDto noticeDto) {

        Notice notice = noticeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("데이터를 찾을 수 없습니다."));
        notice.update(
                noticeDto.getTitle(),
                noticeDto.getContent()
        );

    }

    @Transactional
    public void deleteNotice(Long id) {

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("데이터를 찾을 수 없습니다: " + id));

        noticeRepository.delete(notice);

    }



}
