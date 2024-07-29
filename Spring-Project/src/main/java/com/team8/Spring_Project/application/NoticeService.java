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
    private final UserService userService; // 이거는 좀 생각해봐야함.

    @Autowired
    public NoticeService(NoticeRepository noticeRepository, UserService userService) {
        this.noticeRepository = noticeRepository;
        this.userService = userService;
    }

    // NoticeList 조회
    @Transactional(readOnly = true)
    public List<NoticeDto> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(NoticeDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 공지사항 상세보기
    @Transactional(readOnly = true)
    public NoticeDto getNoticeById(Long id) {
        return noticeRepository.findById(id)
                .map(NoticeDto::fromEntity)
                .orElse(null);
    }

    @Transactional
    public void createNotice(NoticeDto noticeDto) {
        Notice notice = noticeDto.toEntity(userService);
        noticeRepository.save(notice);
        //return NoticeDto.fromEntity(createNotice);
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
