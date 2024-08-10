# Yama! 유용한 앱 공유 커뮤니티 서비스
## 목차
[1. 프로젝트 개요](#1-프로젝트-개요)  
[2. Git Convention](#2-git-convention)  
[3. 개발 환경 및 배포 URL](#3-개발-환경-및-배포-url)  
[4. 프로젝트 구조와 개발 일정](#4-프로젝트-구조와-개발-일정)  
[5. 요구사항과 기능 명세](#5-요구사항과-기능-명세)  
[6. 와이어프레임 / UI](#6-와이어프레임--ui)  
[7. 데이터베이스 모델링(ERD)](#7-데이터베이스-모델링erd)  
[8. 주요 기능](#8-주요-기능)  
[9. 개발 이슈](#9-개발-이슈)  
[10. 프로젝트를 진행하며 느낀점](#10-프로젝트를-진행하며-느낀점)  

## 1. 프로젝트 개요

### 1.1 프로젝트 명

- YAMA ( Your App! My App! )

### 1.2 프로젝트 설명
- 다양한 분야의 앱을 소개함으로써 여러 분야의 사람들과 공유하는 서비스입니다. 사용해본 앱을 카테고리 별로 나누어 링크를 포함한 설명 등으로 후기 작성이 가능하며 다른 사람의 소개 글에 평점과 함께 댓글을 남김으로써 앱에 대한 평가가 가능한 커뮤니티 서비스입니다.

### 1.3 프로젝트 기간
- '2024. 07. 22' ~ '2024. 08. 06'

### 1.4 역할 분담

- 팀장 : 이현준
- FE : 이현준, 이동현, 최의현
- BE : 이현준, 이동현, 최의현
- 디자인 : 이현준, 이동현, 최의현


## 2. Git Convention

<img width=60% alt="스크린샷 2024-08-06 오전 10 21 54" src="https://github.com/user-attachments/assets/0841f1a1-fe53-4312-b954-dd86e64941ed">


## 3. 개발 환경 및 배포 URL
### 3.1 개발 환경

- IDE: IntelliJ IDEA 
- 서비스 배포 : Amazon Lightsail

### 3.2 기술 스택

<img width=60% alt="스크린샷 2024-08-06 오전 10 21 54" src="https://github.com/user-attachments/assets/b1ded36c-7f42-4af3-900b-2f551bfae594">

- Thymeleaf: 3.3.1
- Spring Boot: 3.3.1
- Spring Security: 6.3.1
- Spring Data JPA: 3.1.0
- MySQL: 8.3.0

### 3.3 배포 URL
- http://3.36.65.194:8080/v1/main
- 테스트용 계정
  ```
  id : test@test.com
  pw : 1111
  ```

### 3.4 URL 구조

👱User

| URL                                                 | 요청 방식  | 설명                              
|-----------------------------------------------------|--------|---------------------------------|
| /v1/main                                            | GET    | 메인 페이지 요청                       | 
| /v1/login                                           | GET    | 로그인 페이지 요청                      | 
| /v1/signup                                          | GET    | 회원가입 페이지 요청                     | 
| /v1/signup                                          | POST   | 회원가입 요청                         | 
| /v1/user/{id}                                       | GET    | 해당 id를 가진 User의 마이페이지 요청        | 
| /v1/user/{id}                                       | PUT    | 해당 id를 가진 User의 마이페이지의 정보 수정 요청 | 
| /v1/user/{id}                                       | DELETE | 해당 id를 가진 User의 정보 삭제 요청        | 
| /v1/admin                                           | GET    | 관리자가 관리자 페이지 요청                 | 
| /v1/admin/{user_id}                                 | PUT    | 관리자가 해당 id를 가진 User의 권한 변경 요청   | 

📝Post

| URL                                                 | 요청 방식  | 설명                              
|-----------------------------------------------------|--------|---------------------------------|
| /v1/posts                                           | GET    | User가 게시글 리스트 페이지 요청            | 
| /v1/posts                                           | POST   | User가 새로운 게시글 작성 요청             | 
| /v1/posts/write                                     | GET    | User가 게시글 작성 페이지 요청             | 
| /v1/posts/notice/{id}/edit                          | GET    | Admin이 공지사항 수정 페이지 요청           | 
| /v1/posts/notice/{id}/edit                          | PUT    | Admin이 공지사항 수정 요청               | 
| /v1/posts/post/{id}/edit                            | GET    | User가 게시글 수정 페이지 요청             |
| /v1/posts/post/{id}/edit                            | PUT    | User가 게시글 수정 요청                 | 
| /v1/posts/notice/{id}                               | GET    | User가 공지사항 페이지 요청               | 
| /v1/posts/notice/{id}                               | DELETE | Admin이 공지사항 삭제 요청               | 
| /v1/posts/post/{id}                                 | GET    | User가 post_id에 해당하는 게시글 상세보기 요청 | 
| /v1/posts/post/{id}                                 | DELETE | User가 post_id에 해당하는 게시글 삭제 요청   |
| /v1/posts?categoryId={categoryId}&keyword={keyword} | GET    | 게시글 키워드 검색                      | 
| /v1/posts/article-items?categoryId={categoryId}     | GET    | 카테고리 ID에 따른 게시물 목록 요청           | 

🔖Comment

| URL                                                 | 요청 방식  | 설명                              
|-----------------------------------------------------|--------|---------------------------------|
| /v1/posts/{postId}/comments                         | GET    | 게시글에 작성된 댓글 조회 요청               | 
| /v1/posts/{postId}/comments                         | POST   | 게시글의 댓글 작성 요청                   | 
| /v1/posts/{postId}/comments/{commentId}             | PUT    | 게시글의 댓글 수정 요청                   | 
| /v1/posts/{postId}/comments                         | DELETE | 게시글의 댓글 삭제 요청                   | 
| /v1/posts/{postId}/average-rating                   | GET    | 게시글의 댓글 총 평점 조회 요청              |




## 4. 프로젝트 구조와 개발 일정

### 4.1 개발 일정
<img src="https://github.com/user-attachments/assets/9e3166cc-19ae-41ac-a422-3ae828b70259" width=90%>

```mermaid
gantt
    title YAMA Project
    dateFormat  YY-MM-DD
    todayMarker off
    section 팀 빌딩 및 기획
        팀 빌딩           :2024-07-17, 1d
        프로젝트 주제 선정 :2024-07-18, 1d
        프로젝트 초기 설정 :2024-07-22, 1d
    section 디자인
        UI 설계 :2024-07-23, 1d
    section FE
        메인 페이지      :2024-07-24, 1d
        회원 가입 페이지 :2024-07-24, 1d
        로그인 페이지    :2024-07-24, 1d
        마이 페이지 :2024-07-24, 1d
        관리자 페이지  :2024-07-24, 1d
        게시글 작성 페이지    :2024-07-24, 1d
        게시글 수정 페이지    :2024-07-24, 1d
        게시글 상세 보기 페이지    :2024-07-24, 1d
        카테고리별 앱 소개 페이지    :2024-07-24, 1d
    section BE
        메인    :2024-08-01, 1d
        회원가입:2024-07-26, 1d
        로그인:2024-07-26, 1d
        로그아웃:2024-07-26, 1d
        회원 탈퇴:2024-07-26, 2d
        회원 정보 수정:2024-07-26, 2d
        회원 권한 변경:2024-07-26, 2d
        게시글 등록    :2024-07-26, 1d
        게시글 조회    :2024-07-27, 1d
        게시글 수정    :2024-07-27, 2d
        게시글 삭제    :2024-07-26, 1d
        게시글 검색    :2024-08-02, 1d
        댓글 등록:2024-07-26, 1d
        댓글 조회:2024-07-27, 2d
        댓글 수정:2024-07-31, 1d
        댓글 삭제:2024-07-29, 1d
        평점 부여:2024-08-02, 1d
    section Fix & Deploy
        기타 버그 수정 :2024-08-05, 2d
        배포          :2024-08-06, 1d


```


### 4.2 프로젝트 구조

도메인 주도 설계 원칙을 따르는 아키텍처 패턴으로 구현하였습니다.

📦Spring_Project   
┣ 📂application    
┃ ┣ 📂dto    
┃ ┃   ┣ 📜BoardDTO  
┃ ┃   ┣ 📜CategoryDTO    
┃ ┃   ┣ 📜CommentDTO   
┃ ┃   ┣ 📜NoticeDTO    
┃ ┃   ┣ 📜PostDTO    
┃ ┃   ┗ 📜UserDTO    
┃ ┣ 📜BoardService    
┃ ┣ 📜CategoryService   
┃ ┣ 📜CommentService   
┃ ┣ 📜NoticeService    
┃ ┣ 📜PostService    
┃ ┗ 📜UserService       
┣ 📂config   
┃ ┣ 📜PasswordEncoderConfig    
┃ ┣ 📜SecurityConfig   
┃ ┗ 📜UserStatusCheckFilter    
┣ 📂domain   
┃ ┣ 📜Authority    
┃ ┣ 📜Category   
┃ ┣ 📜Comment    
┃ ┣ 📜Notice   
┃ ┣ 📜Post   
┃ ┗ 📜User   
┣ 📂infrastructure   
┃ ┣ 📂config   
┃ ┃ ┣ 📜QueryDslConfig   
┃ ┃ ┣ 📜Category   
┃ ┃ ┣ 📜Comment    
┃ ┗ 📂persistence    
┃   ┣ 📜CategoryRepository   
┃   ┣ 📜CommentRepository   
┃   ┣ 📜NoticeRepository   
┃   ┣ 📜PostRepository   
┃   ┗ 📜UserRepository   
┗ 📂presentation   
  ┣ 📜BoardController    
  ┣ 📜CommentController    
  ┗ 📜UserController   

## 5. 요구사항과 기능 명세

### 요구 사항


```mermaid
mindmap
  root((프로젝트 요구사항))
    1. 1단계 준비 및 환경 구축
      GitHub 및 Notion 사용
      개발용 develop 브랜치 생성
      Pull Request로 코드 통합
      MySQL 사용
      API 문서화
      ERD 작성
    2. 2단계 기본 게시판 기능
      글 목록 보기
      글 상세 보기
      글 수정하기
      글 삭제하기
      글쓰기
      Thymeleaf로 UI 구현
    3. 3단계 기능 심화
      회원 관리
        로그인
        
        회원 가입
        회원 탈퇴
      권한 관리
        일반
        관리자
        정지
      공지사항 기능
        관리자만 작성/수정/삭제
        모든 유저가 조회 가능
        최근 5개 표시
    4. 4단계 서비스 배포
      AWS 배포
        EC2 또는 Lightsail
      외부 접근 가능한 IP/도메인
```


### 기능 명세


```mermaid
mindmap
  root((YAMA))
    1. 사용자
      1.1 로그인
      1.2 회원 가입
      1.3 회원탈퇴
      1.4 마이 페이지
    2. 게시글
      2.1 글 쓰기
      2.2 글 목록 보기
      2.3 글 상세 보기
      2.4 글 수정하기
      2.5 글 삭제하기
    3. 댓글
      3.1 댓글 쓰기
      3.2 댓글 보기
      3.3 댓글 수정
      3.4 댓글 삭제
    4. 관리자
      4.1 관리자 페이지
        카테고리 관리
        사용자 권한 관리
    5. 평점(후기)
      5.1 평점 등록
    6. 검색
      6.1 App 검색
    7. 카테고리
      7.1 App 카테고리
```

## 6. 와이어프레임 / UI

### 6.1 와이어프레임

<img width=60% src="https://github.com/user-attachments/assets/22b085e8-f57c-4e3c-bd75-416081fc74f9">

### 6.2 화면 설계


<table>
    <tbody>
        <tr>
            <td>메인</td>
            <td>로그인</td>
            <td>회원가입</td>
        </tr>
        <tr>
            <td>
                <img src="https://github.com/user-attachments/assets/cb701758-1717-46cc-ae5e-a10aa81a0c84" width="250" height="350" alt="main">
            </td>
            <td>
                <img src="https://github.com/user-attachments/assets/b5b88f3c-5aa3-4b9d-956d-2c2800d30b61" width="250" height="350" alt="signin">
            </td>
            <td>
                <img src="https://github.com/user-attachments/assets/627c940c-61a7-4bcb-bb85-80aa53f64f6e" width="250" height="350" alt="signup">
            </td>
        </tr>
        <tr>
            <td>정보수정</td>
            <td>관리자 페이지</td>
            <td>게시글 리스트</td>
        </tr>
        <tr>
            <td>
                <img src="https://github.com/user-attachments/assets/3d72c248-8e2f-4c83-a3b6-cdfb156624bc" width="250" height="350" alt="userInfoEdit">
            </td>
            <td>
                <img src="https://github.com/user-attachments/assets/ecd0cf00-b9d2-4c74-b494-2c2a50c97e1f" width="250" height="350" alt="adminPage">
            </td>
            <td>
                <img src="https://github.com/user-attachments/assets/7d1cbb21-0bb1-40bf-a6c4-2b8c42b47061" width="250" height="350" alt="postList">
            </td>
        </tr>
        <tr>
            <td>게시글 상세보기</td>
            <td>게시글 수정 / 삭제</td>
            <td>글쓰기</td>
        </tr>
        <tr>
            <td>
                <img src="https://github.com/user-attachments/assets/8c7e3abf-0e35-41d5-bf73-f791411a05ef" width="250" height="350" alt="postDetail">
            </td>
            <td>
                <img src="https://github.com/user-attachments/assets/40a38f3b-a941-42be-ba33-f831532e9105" width="250" height="350" alt="postEdit">
            </td>
            <td>
                <img src="https://github.com/user-attachments/assets/f1e0452c-0945-4f61-ba0b-1179fc531269" width="250" height="350" alt="postWrite">
            </td>
        </tr>
    </tbody>
</table>


## 7. 데이터베이스 모델링(ERD)

<img src="https://github.com/user-attachments/assets/96ff1277-7e92-4195-9dff-e1d5e132b187" width=60%>


## 8. 프로젝트를 진행하며 느낀점

- Spring Security를 시도해보자라는 생각으로 적용시켜보며 의도치 않게 접근 권한을 설정하게 되는 것과 같은 문제를 겪었다. 이를 해결하며 적절한 권한 설정이 중요하다는 것을 깨달았고, 커스터마이징을 하며 Spring Security의 유연함을 체험해 볼 수 있었다. 백엔드 구현에 있어 중요한 부분인만큼 방대한 학습량과 지식이 필요하다는 것도 알 수 있었다.

- 통합된 DTO인 BoardDTO를 인터페이스 혹은 추상클래스로 구현하여 DTO간 변환 없이 구현하고, 확장가능한 구현이 가능했을 것이라는 아쉬움이 생겼다. 좀 더 객체지향적으로 개발하는 방법을 구상 후 프로젝트를 시작하는 연습이 필요하다고 느꼈다.

- Thymeleaf를 사용하여 구현한 덕분에 초기 페이지 로딩이 빨라 개발에 이점을 가질 수 있었고 HTML을 그대로 유지하며 동적 기능을 추가하여 편리함을 느낄 수 있었다. 이 외에도 JavaScript를 사용하여 기능을 추가하며 통합 방식에 대해 고민해 볼 수 있었다.
