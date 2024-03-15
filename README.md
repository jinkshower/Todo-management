[step1 README 보러가기](https://github.com/jinkshower/Todo-management/tree/main)

## 리팩토링 사항

1. 인수테스트에서의 테스트 격리 해결
2. 검색조건 기능 리팩토링
3. N+1 문제 해결
4. 페이징 처리
5. github actions를 통한 테스트 자동화

## 리팩토링 상세

### 인수테스트에서의 테스트 격리 문제 해결

`step1`에서 해결하지 못했던 테스트 격리 해결.
@DirtiesContext를 먼저 적용해보았으나 테스트 속도가 너무 느려지는 단점을 발견.
이후 테스트에 사용된 테이블을 truncate하는 클래스를 구현하여 문제 해결.

[학습기록](https://jinkshower.github.io/sprintboottest_isolation/)

### 검색조건 기능 리팩토링

전략-어댑터 패턴으로 구현하였던 검색조건 기능 개선.
QueryDSL의 동적쿼리를 이용, 검색 조건을 BooleanExpression으로 변환하여 where절에 각각 추가.

### N+1 문제 해결

위의 검색조건 기능 리팩토링을 하면서 지연로딩한 객체가 반복문 내에서 초기화되며 N+1문제가 발생함을 인지.
fetch join으로 문제 해결
[학습기록](https://jinkshower.github.io/querydsl_nplusone/)

### 페이징 처리

일대다 관계의 객체들을 fetch join하며 페이징 처리시 메모리에 '다' 쪽 컬렉션이 모두 적재됨을 발견.
id를 일반조인으로 가져오는 쿼리 + 가져온 id를 in절에 사용하는 fetch join쿼리로 문제를 해결했으나 유지보수가 힘들다 판단.
batch size 적용으로 해결
[학습기록](https://jinkshower.github.io/pagination_fetchjoin/)

### github actions를 통한 테스트 자동화

테스트 자동화에 대한 학습을 위하여 해당 프로젝트에 적용
.github/workflows에 yml파일에 pull request마다 테스트 자동화 설정 추가
[학습기록](https://jinkshower.github.io/githubaction_automated_test/)

## 더 고민해볼 부분

### 동시성 처리

좋아요 생성 api가 멀티쓰레드에서 동시에 호출되었을때 교착상태가 발생.
todo- like의 연관관계를 제거하여 MySQL의 공유잠금-배타적 잠금의 교착상태를 해결.
이 후 synchronized, jpa의 낙관적 락, 비관적 락을 적용, 비교 후 비관적 락을 선택하였습니다.
하지만 db와 락에 대한 이해가 아직 부족한 것 같아 자신있게 해결했다고는 말하기 힘들 것 같습니다.
