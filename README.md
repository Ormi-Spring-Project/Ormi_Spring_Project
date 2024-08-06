# Ormi_Spring_Project


# Yama! 유용한 앱 공유 커뮤니티 서비스

## 1. 목표와 기능

### 1.1 목표
- 각자가 사용해본 앱 중 유용한 것을 공유하기 위한 커뮤니티 서비스
- 다양한 분야의 사람들에게 도움을 주는 커뮤니티 서비스

### 1.2 기능
- 사용해본 앱의 링크를 포함한 설명 등으로 사용해본 앱의 후기 작성
- 앱을 사용해본 후 해당 글에 평점과 함께 댓글 작성
- 카테고리 별로 나누어 앱의 후기를 작성 가능하고, 카테고리 별로 조회가 가능

### 1.3 역할 분담

- 팀장 : 이현준
- FE : 이현준, 이동현, 최의현
- BE : 이현준, 이동현, 최의현
- 디자인 : 이현준, 이동현, 최의현


## 2. Git Convention // 일단 보류 or 노션 캡쳐본 사용

- `feat: {something}` : 새로운 기능을 추가했을 때
- `fix: {something}` : 기능 중 버그를 수정했을 때
- `design: {something}` : 디자인 일부를 변경했을 때
- `refactor: {something}` : 코드를 재정비 하였을 때
- `chore: {something}` : 빌드 관련 업무를 수정하거나 패키지 매니저를 수정했을 때
- `docs: {something}` : README와 같은 문서를 변경했을 때
- `test: {something}` : 테스트 코드를 추가했을 때


## 3. 개발 환경 및 배포 URL
### 3.1 개발 환경 아래 내용 or 동현님이랑 통일

- IDE: IntelliJ IDEA 
- 언어: Java 17
- 빌드 도구: Gradle 8.x
- Web Framework : Spring Boot 3.3.x
- 서비스 배포 환경 : Amazon Lightsail
    
### 3.2 배포 URL X
- https://www.studyin.co.kr/
- 테스트용 계정
  ```
  id : test@test.test
  pw : test11!!
  ```

### 3.3 URL 구조

| URL                                                 | 요청 방식  | 설명                              | 결과 구현 여부 |
|-----------------------------------------------------|--------|---------------------------------|----------|
| /v1/main                                            | GET    | 메인 페이지 요청                       | 200      |
| /v1/login                                           | GET    | 로그인 페이지 요청                      | 200      |
| /v1/signup                                          | GET    | 회원가입 페이지 요청                     | 200      |
| /v1/signup                                          | POST   | 회원가입 요청                         | 302      |
| /v1/user/{id}                                       | GET    | 해당 id를 가진 User의 마이페이지 요청        | 200      |
| /v1/user/{id}                                       | PUT    | 해당 id를 가진 User의 마이페이지의 정보 수정 요청 | 302      |
| /v1/user/{id}                                       | DELETE | 해당 id를 가진 User의 정보 삭제 요청        | 302      |
| /v1/admin                                           | GET    | 관리자가 관리자 페이지 요청                 | 200      |
| /v1/admin/{user_id}                                 | PUT    | 관리자가 해당 id를 가진 User의 권한 변경 요청   | 302      |
| /v1/posts                                           | GET    | User가 게시글 리스트 페이지 요청            | 200      |
| /v1/posts                                           | POST   | User가 새로운 게시글 작성 요청             | 302      |
| /v1/posts/write                                     | GET    | User가 게시글 작성 페이지 요청             | 200      |
| /v1/posts/notice/{id}/edit                          | GET    | Admin이 공지사항 수정 페이지 요청           | 200      |
| /v1/posts/notice/{id}/edit                          | PUT    | Admin이 공지사항 수정 요청               | 302      |
| /v1/posts/post/{id}/edit                            | GET    | User가 게시글 수정 페이지 요청             | 200      |
| /v1/posts/post/{id}/edit                            | PUT    | User가 게시글 수정 요청                 | 302      |
| /v1/posts/notice/{id}                               | GET    | User가 공지사항 페이지 요청               | 200      |
| /v1/posts/notice/{id}                               | DELETE | Admin이 공지사항 삭제 요청               | 302      |
| /v1/posts/post/{id}                                 | GET    | User가 post_id에 해당하는 게시글 상세보기 요청 | 200      |
| /v1/posts/post/{id}                                 | DELETE | User가 post_id에 해당하는 게시글 삭제 요청   | 302      |
| /v1/posts?categoryId={categoryId}&keyword={keyword} | GET    | 게시글 키워드 검색                      | 200      |
| /v1/posts/article-items?categoryId={categoryId}     | GET    | 카테고리 ID에 따른 게시물 목록 요청           | 200      |
| /v1/posts/{postId}/comments                         | GET    | 게시글에 작성된 댓글 조회 요청               | 200      |
| /v1/posts/{postId}/comments                         | POST   | 게시글의 댓글 작성 요청                   | 201      |
| /v1/posts/{postId}/comments/{commentId}             | PUT    | 게시글의 댓글 수정 요청                   | 204      |
| /v1/posts/{postId}/comments                         | DELETE | 게시글의 댓글 삭제 요청                   | 204      |
| /v1/posts/{postId}/average-rating                   | GET    | 게시글의 댓글 총 평점 조회 요청              | 200      |


## 4. 프로젝트 구조와 개발 일정

### 4.1 개발 일정

wbs 첨부

### 4.2 프로젝트 구조

우리가 어떤 아키텍처를 선택해서 패키지 구조를 만들었는지? --> 기억해야함

어떠한 패턴을 기반으로 패키지 구조를 생성했습니다.

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

노션 글 그대로 갖다 쓰거나 or 아래 마인드맵 or 플로우차트

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

노션 글 그대로 갖다 쓰거나 or 아래 마인드맵 or 플로우차트

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

## 6. 와이어프레임 / UI / BM

### 6.1 와이어프레임
- 아래 페이지별 상세 설명, 더 큰 이미지로 하나하나씩 설명 필요

<img width=60% src="https://github.com/user-attachments/assets/22b085e8-f57c-4e3c-bd75-416081fc74f9">

### 6.2 화면 설계


<table>
    <tbody>
        <tr>
            <td>메인</td>
            <td>로그인</td>
            <td>회원가입</td>
            <td>정보수정</td>
            <td>관리자 페이지</td>
        </tr>
        <tr>
            <td>
		            <img src="https://github.com/user-attachments/assets/cb701758-1717-46cc-ae5e-a10aa81a0c84" width="160" height="200" alt="main">
            </td>
            <td>
                <img src="https://github.com/user-attachments/assets/b5b88f3c-5aa3-4b9d-956d-2c2800d30b61" width="160" height="200" alt="signin">
            </td>
            <td>
                <img src="https://github.com/user-attachments/assets/627c940c-61a7-4bcb-bb85-80aa53f64f6e" width="160" height="200" alt="signup">
            </td>
            <td>
                <img src="https://github.com/user-attachments/assets/3d72c248-8e2f-4c83-a3b6-cdfb156624bc" width="160" height="200" alt="userInfoEdit">
            </td>
            <td>
	              <img src="https://github.com/user-attachments/assets/ecd0cf00-b9d2-4c74-b494-2c2a50c97e1f" width="160" height="200" alt="adminPage">
            </td>
        </tr>
        <tr>
            <td>게시글 리스트</td>
            <td>게시글 상세보기</td>
            <td>게시글 수정 / 삭제</td>
            <td>글쓰기</td>
        </tr>
        <tr>
            <td>
                <img src="https://github.com/user-attachments/assets/7d1cbb21-0bb1-40bf-a6c4-2b8c42b47061" width="160" height="200" alt="postList">
            </td>
            <td>
                <img src="https://github.com/user-attachments/assets/8c7e3abf-0e35-41d5-bf73-f791411a05ef" width="160" height="200" alt="postDetail">
            </td>
            <td>
	              <img src="https://github.com/user-attachments/assets/40a38f3b-a941-42be-ba33-f831532e9105" width="160" height="200" alt="postEdit">
            </td>
            <td>
                <img src="https://github.com/user-attachments/assets/f1e0452c-0945-4f61-ba0b-1179fc531269" width="160" height="200" alt="postWrite">
            </td>
        </tr>
        <tr>
    </tbody>
</table>

## 7. 데이터베이스 모델링(ERD)

이거 아니면 erdCloud 캡쳐본

```mermaid
erDiagram
    User {
        Long id PK
        VARCHAR(255) email
        VARCHAR(255) nickname
        VARCHAR(255) password
        VARCHAR(255) phoneNumber
        VARCHAR(255) authority
    }
    Category {
        Long id PK
        VARCHAR(255) name
        VARCHAR(255) icon
    }
    Post {
        Long id PK
        VARCHAR(255) title
        VARCHAR(255) content
        VARCHAR(255) application
        TIMESTAMP createdAt
        TIMESTAMP updatedAt
        Long userId FK
        Long categoryId FK
        LONGBLOB picture
    }
    Notice {
        Long id PK
        VARCHAR(255) title
        VARCHAR(255) content
        TIMESTAMP createdAt
        TIMESTAMP updatedAt
        Long userId FK
    }
    Comment {
        Long id PK
        VARCHAR(255) content
        TIMESTAMP createdAt
        TIMESTAMP updatedAt
        Long postId FK
        Long userId FK
    }

    User ||--o{ Post : creates
    User ||--o{ Notice : creates
    User ||--o{ Comment : writes
    Category ||--o{ Post : belongs_to
    Post ||--o{ Comment : has
```

## 10. 에러와 에러 해결

트러블 슈팅 문서화 or 그냥 몇개 적기 or 없애기

## 10. 개발하며 느낀점
- 이현준 :

- 이동현 :

- 최의현 : 
