package com.team8.Spring_Project.application;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team8.Spring_Project.application.dto.NoticeDTO;
import com.team8.Spring_Project.application.dto.PostDTO;
import com.team8.Spring_Project.domain.*;
import com.team8.Spring_Project.infrastructure.persistence.NoticeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserService userService;
    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository,
                         UserService userService, JPAQueryFactory jpaQueryFactory) {
        this.noticeRepository = noticeRepository;
        this.userService = userService;
        this.jpaQueryFactory = jpaQueryFactory;
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

    // 일반 게시글 검색
    @Transactional(readOnly = true)
    public List<NoticeDTO> searchPostByKeyword(String keyword) {
        QNotice notice = QNotice.notice;
        QUser user = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            builder.or(notice.title.containsIgnoreCase(keyword))
                    .or(notice.user.nickname.containsIgnoreCase(keyword));
        }

        List<Notice> searchedNotices = jpaQueryFactory.selectFrom(notice)
                .leftJoin(notice.user, user)
                .where(builder)
                .fetch();

        return searchedNotices.stream()
                .map(NoticeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 공지사항 상세보기
    @Transactional(readOnly = true)
    public NoticeDTO getNoticeById(Long id) {

        return noticeRepository.findById(id)
                .map(NoticeDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("데이터를 찾을 수 없습니다."));

    }

    // 공지사항 생성
    @Transactional
    public NoticeDTO createNotice(NoticeDTO noticeDto) {

        User user = userService.findUserEntity(noticeDto.getUserId());

        Notice notice = noticeDto.toEntity(user);
        noticeRepository.save(notice);

        return NoticeDTO.fromEntity(notice);

    }

    // 공지사항 수정
    @Transactional
    public NoticeDTO updateNotice(Long id, NoticeDTO noticeDto) {

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("데이터를 찾을 수 없습니다."));
        notice.update(
                noticeDto.getTitle(),
                noticeDto.getContent(),
                new Timestamp(System.currentTimeMillis())
        );

        return NoticeDTO.fromEntity(notice);

    }

    // 공지사항 삭제
    @Transactional
    public void deleteNotice(Long id) {

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("데이터를 찾을 수 없습니다: " + id));

        noticeRepository.delete(notice);

    }



}
