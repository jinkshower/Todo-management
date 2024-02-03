## 기능 요구사항

 - [x] 회원가입을 할 수 있다
   - [x] username의 유효성을 검증한다
   - [x] password의 유효성을 검증한다
   - [x] 중복되는 username은 허용하지 않는다
 - [x] 로그인을 할 수 있다
   - [x] 로그인 성공 시 JWT 토큰을 발행한다
 - [x] 할 일 카드를 작성할 수 있다 @Login
   - [x] 제목, 내용을 저장한다
- [x] 전체 할일 목록을 조회할 수 있다
- [x] 특정 할 일 카드를 조회할 수 있다
- [x] 할 일 카드를 수정할 수 있다 @Login
  - [x] 제목, 내용을 수정할 수 있다
- [x] 할 일 완료처리를 할 수 있다 @Login
- [ ] 할 일 카드에 댓글을 남길 수 있다 @Login
- [ ] 댓글을 수정할 수 있다 @Login
- [ ] 댓글을 삭제할 수 있다 @Login
- [ ] 예외 발생시 400 상태코드와 메시지를 반환한다