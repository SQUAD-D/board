## 프로젝트 아키텍처

![pja.png](mdimg%2Fpja.png)

## 프로젝트 요구사항

- 로그인
    - 회원 Session 방식 로그인 지원
    - Session Clustering 구축 [Redis]

- 게시글
    - 기본적인 게시글 CRUD
    - 원작자/외부자에 대한 수정, 삭제 예외 처리
    - 볼드체, 이텔릭체, 삭선, UL/LI 기능
    - 게시글 페이징 처리
    - 게시글 (제목+내용) , (제목) 검색기능 [MySQL n-gram]
    - 이미지 업로드 기능 [S3]
        - 이미지는 여러 개 업로드 가능
        - 이미지 확장자/크기 예외처리

- 댓글
    - 기본적인 댓글 CRUD
    - 원작자/외부자에 대한 수정, 삭제 예외 처리
    - 대댓글은 1개까지 가능

- 로드밸런싱
    - Nignx를 이용한 RoundRobin 방식의 로드밸런싱

- CI/CD
    - GitHub Action으로 PR, Push 시 EC2에 자동 배포
    - Docker Compose를 이용한 다중 컨테이너 설정

- Monitoring
    - Prometheus를 이용한 Redis 모니터링
    - Scouter를 이용한 Tomcat/JVM 모니터링

## 프로젝트 미리보기

### 주소

https://haechan.store/

- Main

![main.png](mdimg%2Fmain.png)

- Board List

![boardList.png](mdimg%2FboardList.png)

- Board Create

![boardDetail.png](mdimg%2FboardDetail.png)

- Board Detail

![boardComment.png](mdimg%2FboardComment.png)