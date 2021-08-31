# OSIV와 성능 최적화

### OSIV = Open Session In View



- JPA Trasaction 시작시 Transaction을 가져온다.



#### OSIV ON

- OSIV 가 켜져있으면 더이상 필요가 없을 떄 까지(Controller 포함) Transcation을 가져오고 영속성 컨텍스트를 가지고 있는다.

##### 장점

- 개발의 편의성이 좋다.
- 코드가 단순화 된다.

##### 단점

- ==> 커넥션을 너무 오래 갖고있게된다.(API요청시 API 대기시간동안 Transcation을 유지한다.)



#### OSIV OFF

- OSIV를 끄면 Transaction을 종료할 때 영속성 컨텍스트를 담고 데이터베이스 커넥션도 반환한다.

##### 장점

- 커넥션 리소스의 낭비가 적어진다.

##### 단점

- 모든 지연로딩이 Transaction안에서 된다.(Controller에서 사용 불가능)
- 끄게되면 프록시를 조회하면 Transcation이 종료된 후에 실행 되므로 에러가 발생한다.
  - Transaction 에서 로딩을 완료하여 반환하거나, fetch조인을 이용할 경우 해결 가능하다.
  - 쿼리용 서비스를 만들어서 해결한다.(Transcation이 커져있는 서비스를 따로 만들어 사용한다.)





##### 실무에서의 OSIV

- OSIV를 끈 상태로 복잡성을 관리하는 좋은 방법이 있다. 바로 Command와 Query를 분리하는 것이다.

- Life cycle이 다른 것을 해결할 수 있다.

- API 트래픽이 클 경우 OSIV를 끄는 경우가 좋다.

- ADMIN 시스템에는 OSIV를 키는게 좋다.

  









