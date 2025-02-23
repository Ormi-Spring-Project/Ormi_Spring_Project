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
[9. 프로젝트 회고](#9-프로젝트-회고)

## 팀원 및 역할
|<img width=50% alt="이현준" src="https://github.com/user-attachments/assets/bf0432d6-03af-4d6d-b629-83850fc082ee">|<img width=100% alt="이동현" src="https://github.com/user-attachments/assets/2dbb9e5b-b5d6-4204-92a4-3e46d5326390">|<img width=100% alt="최의현" src="https://github.com/user-attachments/assets/d0a87556-344c-4db1-a3f6-865f8290d9a3">
|:---:|:---:|:---:|
|이현준|이동현|최의현|
|팀장|팀원|팀원|
|기능 명세서 작성<br>ERD 작성<br>UI 설계<br>User, Admin 기능 구현<br>프로젝트 배포(AWS Lightsail)|UI 설계<br>Comment 기능 구현<br>평점 기능 구현|ERD작성<br>UI 설계<br>Convention 작성<br>Post 기능 구현<br>Spring Security 적용|

## 1. 프로젝트 개요

### 1.1 프로젝트 명

- YAMA ( Your App! My App! )

### 1.2 프로젝트 설명
- 다양한 분야의 앱을 소개함으로써 여러 분야의 사람들과 공유하는 서비스입니다. 사용해본 앱을 카테고리 별로 나누어 링크를 포함한 설명 등으로 후기 작성이 가능하며 다른 사람의 소개 글에 평점과 함께 댓글을 남김으로써 앱에 대한 평가가 가능한 커뮤니티 서비스입니다.

### 1.3 프로젝트 기간
- '2024. 07. 22' ~ '2024. 08. 06'


## 2. Git Convention

<img width=60% alt="스크린샷 2024-08-06 오전 10 21 54" src="https://github.com/user-attachments/assets/0841f1a1-fe53-4312-b954-dd86e64941ed">


## 3. 개발 환경 및 배포 URL
### 3.1 개발 환경

- IDE: IntelliJ IDEA 
- 서비스 배포 : Amazon Lightsail

### 3.2 기술 스택

#### Front-End
<img src="https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white">&nbsp;<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white">&nbsp;<img src="https://img.shields.io/badge/css3-1572B6?style=for-the-badge&logo=css3&logoColor=white">&nbsp;<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white">&nbsp;

#### Back-End
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">&nbsp;<img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">&nbsp;

#### DB
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">

#### Deploy
<img src="https://img.shields.io/badge/light sail-232F3E?style=for-the-badge&logo=amazonwebservices&logoColor=white">&nbsp;




### 3.3 배포 URL
- (AWS 프리티어 종료로 인한 배포 중단)
- ~~http://54.180.244.44:8080/v1/main~~
- ~~테스트용 계정~~
  ```
  id : test@test.com
  pw : 1111
  ```
### 3.4 배포 아키텍처
![image](https://github.com/user-attachments/assets/96405f04-e674-44cf-a327-0ff7206f3769)

### 3.5 URL 구조

👱User

| URL                                                 | 요청 방식  | 설명 | Not Login | USER | ADMIN | BAN |                               
|-----------------------------------------------------|--------|---------------------------------|:----------:|:----------:|:----------:|:----------:|
| /v1/main                                            | GET    | 메인 페이지                       |✅|✅|✅|✅
| /v1/login                                           | GET    | 로그인 페이지                     |✅||||
| /v1/signup                                          | GET    | 회원가입 페이지                     |✅||||
| /v1/signup                                          | POST   | 회원가입                         |✅|||| 
| /v1/user/{id}                                       | GET    | 마이페이지        ||✅|||
| /v1/user/{id}                                       | PUT    | 마이페이지 정보 수정 ||✅|||
| /v1/user/{id}                                       | DELETE | 회원탈퇴        ||✅|||
| /v1/admin                                           | GET    | 관리자 페이지    |||✅||
| /v1/admin/{user_id}                                 | PUT    | User의 권한 변경   |||✅||

📝Post

| URL                                                 | 요청 방식  | 설명 | Not Login | USER | ADMIN | BAN |
|-----------------------------------------------------|--------|---------------------------------|:----------:|:----------:|:----------:|:----------:|
| /v1/posts                                           | GET    | 게시글 리스트 페이지     ||✅|✅|✅
| /v1/posts                                           | POST   | 게시글 작성      ||✅|||
| /v1/posts/write                                     | GET    | 게시글 작성 페이지     ||✅|||
| /v1/posts/notice/{id}/edit                          | GET    | 공지사항 수정 페이지    |||✅|| 
| /v1/posts/notice/{id}/edit                          | PUT    | 공지사항 수정        |||✅||
| /v1/posts/post/{id}/edit                            | GET    | 게시글 수정 페이지     ||✅|✅||
| /v1/posts/post/{id}/edit                            | PUT    | 게시글 수정       ||✅|✅||
| /v1/posts/notice/{id}                               | GET    | 공지사항 페이지     ||✅|✅|✅
| /v1/posts/notice/{id}                               | DELETE | 공지사항 삭제       |||✅| 
| /v1/posts/post/{id}                                 | GET    | 게시글 상세보기 ||✅|✅||
| /v1/posts/post/{id}                                 | DELETE | 게시글 삭제   ||✅|✅||
| /v1/posts?categoryId={categoryId}&keyword={keyword} | GET    | 게시글 키워드 검색    ||✅|✅||
| /v1/posts/article-items?categoryId={categoryId}     | GET    | (메인 페이지) 카테고리 ID에 따른 게시글 리스트           ||✅|✅|✅|

🔖Comment

| URL                                                 | 요청 방식  | 설명 | Not Login | USER | ADMIN | BAN |
|-----------------------------------------------------|--------|---------------------------------|:----------:|:----------:|:----------:|:----------:|
| /v1/posts/{postId}/comments                         | GET    | 댓글 조회         ||✅|✅||
| /v1/posts/{postId}/comments                         | POST   | 댓글 작성         ||✅|✅|| 
| /v1/posts/{postId}/comments/{commentId}             | PUT    | 댓글 수정      ||✅|✅||
| /v1/posts/{postId}/comments                         | DELETE | 댓글 삭제      ||✅|✅||
| /v1/posts/{postId}/average-rating                   | GET    | 게시글 총 평점 조회      ||✅|✅||




## 4. 프로젝트 구조와 개발 일정

### 4.1 개발 일정
<img src="https://github.com/user-attachments/assets/9e3166cc-19ae-41ac-a422-3ae828b70259" width=95%>



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

### 화면 흐름도

<img width="70%" alt="화면 흐름도" src="https://github.com/user-attachments/assets/41ccc691-ccca-4042-ac4c-6843a2ed10ab">

### 요구 사항 및 기능 명세

<img width="70%" alt="요구사항   기능 명세서" src="https://github.com/user-attachments/assets/d9a089f4-df47-414d-8e30-30511659a171">

## 6. 와이어프레임 / UI

### 6.1 와이어프레임

<img width=70% src="https://github.com/user-attachments/assets/22b085e8-f57c-4e3c-bd75-416081fc74f9">

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
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/main.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/cb701758-1717-46cc-ae5e-a10aa81a0c84" width="250" height="350" alt="main">
                </a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/login.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/b5b88f3c-5aa3-4b9d-956d-2c2800d30b61" width="250" height="350" alt="signin">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/signup.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/627c940c-61a7-4bcb-bb85-80aa53f64f6e" width="250" height="350" alt="signup">
</a>
            </td>
        </tr>
        <tr>
            <td>정보수정</td>
            <td>관리자 페이지</td>
            <td>게시글 리스트</td>
        </tr>
        <tr>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/modify.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/3d72c248-8e2f-4c83-a3b6-cdfb156624bc" width="250" height="350" alt="userInfoEdit">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/adminpage.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/af2a96a9-cb7f-4e51-97f1-26373cd81087" width="250" height="350" alt="adminPage">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/postList.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/7d1cbb21-0bb1-40bf-a6c4-2b8c42b47061" width="250" height="350" alt="postList">
</a>
            </td>
        </tr>
        <tr>
            <td>게시글 상세보기</td>
            <td>게시글 수정 / 삭제</td>
            <td>글쓰기</td>
        </tr>
        <tr>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/postDetail.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/8c7e3abf-0e35-41d5-bf73-f791411a05ef" width="250" height="350" alt="postDetail">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/postModify.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/40a38f3b-a941-42be-ba33-f831532e9105" width="250" height="350" alt="postEdit">
</a>
            </td>
            <td>
                <a href="https://github.com/Ormi-Spring-Project/YAMA/blob/develop/Spring-Project/src/main/resources/static/images/postWrite.png" target="_blank">
                <img src="https://github.com/user-attachments/assets/f1e0452c-0945-4f61-ba0b-1179fc531269" width="250" height="350" alt="postWrite">
</a>
            </td>
        </tr>
    </tbody>
</table>


## 7. 데이터베이스 모델링(ERD)

<img src="https://github.com/user-attachments/assets/06f8b0ad-c13a-46a2-85c7-b32ae5207471" width=90%>

## 8. 주요 기능
### 8.1 게시글 작성

<img src="https://github.com/user-attachments/assets/c82f300c-2cfb-4afb-86af-2ce656e88c2a" width=60%>

- 작성자가 사용했던 앱에 대한 후기를 작성하는 기능입니다.
- 앱 분야별 카테고리를 설정할 수 있습니다.
- 앱 대표 이미지를 설정할 수 있습니다.
- URL을 통한 직관적인 연결 기능을 제공합니다.

### 8.2 게시글 수정

<img src="https://github.com/user-attachments/assets/35a49741-ac8a-4e67-a102-ca3bd38bdc80" width=60%>

- 기존에 작성한 게시글을 수정하는 기능입니다.
- 작성 시 설정하였던 아이콘, 제목, 카테고리, URL 및 본문을 수정할 수 있습니다.

### 8.3 게시글 검색

<img src="https://github.com/user-attachments/assets/e0c14b87-a0c0-4ff4-8b33-c5fd48a46538" width=60%>

- 게시글을 검색하는 기능입니다.
- 본인이 원하는 카테고리를 설정 후 제목 또는 작성자 닉네임의 일부분을 이용하여 검색할 수 있습니다.

### 8.4 댓글 및 별점 작성

<img src="https://github.com/user-attachments/assets/3ee1cfdf-2e39-40fd-8973-e65b1db31aa3" width=60%>

- 댓글 및 별점을 작성하는 기능입니다.
- 댓글 작성 시 별점 부여가 가능합니다.
- 댓글마다 부여된 별점은 평균 별점으로 게시글 본문 아래에 집계됩니다.
- 댓글 수정 및 삭제 시에도 즉각적으로 수정된 별점이 반영됩니다.

### 8.5 관리자 기능

<img src="https://github.com/user-attachments/assets/b8273fed-cc86-4709-83f3-a7fef59346ae" width=60%>

- 관리자 페이지로 유저별 권한을 설정하는 기능입니다.
- 회원가입 시 게시글, 댓글 조회 및 작성이 가능한 일반 USER로 권한이 설정됩니다.
- BAN으로 권한을 설정 시에는 공지사항만 조회할 수 있는 기능을 제공합니다.


## 9. 프로젝트 회고

- 최의현
```
- 팀 프로젝트를 진행하며 모두가 스프링을 사용한 백엔드 개발 경험이 없을 뿐더러 2명의 팀원 이탈과 함께 많은 변수를 겪었습니다. 그러나 팀 프로젝트 과정에서 팀의 자원을 파악 후 적절한 분배가 이루어진다면 어떻게든 결과물을 얻어낼 수 있다는 것을 알게 되었습니다. 그 외에도 Spring Security와 같은 프레임워크에도 도전해보며 개발적으로도, 협업적으로도 많이 배울 수 있는 프로젝트였습니다.

- 통합된 DTO인 BoardDTO를 인터페이스 혹은 추상클래스로 구현하여 DTO간 변환 없이 구현하고, 확장가능한 구현이 가능했을 것이라는 아쉬움이 생겼습니다. 좀 더 객체지향적으로 개발하는 방법을 구상 후 프로젝트를 시작하는 연습이 필요하다고 느꼈습니다.

- Thymeleaf를 사용하여 구현한 덕분에 초기 페이지 로딩이 빨라 개발에 이점을 가질 수 있었고 HTML을 그대로 유지하며 동적 기능을 추가하여 편리함을 느낄 수 있었습니다. 이 외에도 JavaScript를 사용하여 기능을 추가하며 통합 방식에 대해 고민해 볼 수 있었습니다.
```

- 이현준
```
- 실제로 프로젝트를 진행하면서, 제가 보유하고 있는 기술에 대해 정확히 이해하고 있지 않은 부분이 어떤 부분인지 확실하게 알 수 있었던 기회였습니다.

- 해당 프로젝트에서 팀장을 맡게 되었는데, 팀장 경험이 거의 없었던 저에게는 큰 도전이었습니다. 팀을 잘 이끌어가고 싶은 욕심이 많았으나, 개발 프로젝트 경험이 많이 없어서 일정 조율, 기능 구현 등 여러 부분에서 난관을 겪어, 팀원들의 역량을 100%까지 끌어내지 못했던게 아쉬웠던 것 같습니다. 하지만, 프로젝트를 진행하면서 팀장으로서 가져야 할 태도와 해야할 일들이 어떤 것인지 깨달았습니다.