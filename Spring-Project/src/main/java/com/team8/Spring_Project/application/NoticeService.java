package com.team8.Spring_Project.application;

import com.team8.Spring_Project.application.dto.NoticeDto;
import com.team8.Spring_Project.domain.Notice;
import com.team8.Spring_Project.infrastructure.persistence.NoticeRepository;
import com.team8.Spring_Project.infrastructure.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository; // 이거는 좀 생각해봐야함.

    @Autowired
    public NoticeService(NoticeRepository noticeRepository, UserRepository userRepository) {
        this.noticeRepository = noticeRepository;
        this.userRepository = userRepository;
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
        Notice notice = noticeDto.toEntity(userRepository);
        Notice createNotice = noticeRepository.save(notice);
        //return NoticeDto.fromEntity(createNotice);
    }



}
