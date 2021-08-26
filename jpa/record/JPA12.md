# 단순 Api 

###### 스프링의 경우 RestController annotation을 통하여 객체를 반환할 경우 손쉽게 Json타입으로 전달이 가능하다.



## 회원 등록 API



1. 직접 엔티티를 받아서 등록하는 경우



```java
@PostMapping("/api/v1/members")
public CreateMemberResponse saveMember1(@RequestBody @Validated Member member){
    Long id = memberService.join(member);
    return new CreateMemberResponse(id);

}
```

- 문제점
  - API 스펙 또는 엔티티가 변경 될 경우 에러가 발생할 수 있다.
  - 엔티티를 통한 별도의 valdation이 불가능하다.





2. 별도의 DTO를 사용한 경우

```java
@PostMapping("/api/v2/members")
public CreateMemberResponse saveMember2(@RequestBody @Validated CreateMemberRequest request){
    Member member = new Member();
    member.setName(request.getName());
    Long id = memberService.join(member);
    return new CreateMemberResponse(id);

}

@Data
static class CreateMemberRequest{
    private String name;
}
```

- 장점
  - API 스펙을 해당 API용 DTO를 통하여 확인이 가능하다.
  - 엔티티에 변경 사항이 있을 경우 별도의 DTO를 이용하여 손쉽게 처리 가능하다.
  - 엔티티와 API 스펙을 명확히 구분할 수 있다.
  - 엔티티를 통한 별도의 validation이 가능하다.









## 회원 변경 API



- put은 멱등하여 여러번 호출돼도 되므로 put을 이용하여 만든다.
- 변경 결과를 엔티티를 반환하지 않고, 엔티티의 값을 따로 조회하여 반환하는 식으로 한다(query 와 command의 분리)







## 회원 조회 API



1. List<엔티티>로 변경하는 경우

```java
@GetMapping("/api/v1/members")
public List<Member> membersV1(){
    return memberService.findMembers();
}
```

- Java.util의 list로 반환시 json으로 값을 뿌려준다.
- 엔티티를 사용할 경우 엔티티 내의 모든 정보가 뿌려진다.
- 모든 정보를 뿌리지 않으려면 해당 엔티티의 원하는 값에 @JsonIgnore annotation을 달아주면 뿌려지지 않는다.
- 단, 조회는 한 곳에서 만 조회하는 것이 아니기 떄문에, 유연한 변경이 힘들어 진다.
- Json 스펙상 정보를 전달하기 힘들어진다.(그냥 배열로 전달 될 경우 그 배열 외에 정보를 전달 할 수 없다.)





2. MemberDto와 다른 객체를 통하여 감싸서 변환하는 경우

```java
@GetMapping("/api/v2/members")
public Result membersV2(){
    List<Member> findMembers = memberService.findMembers();
    List<MemberDto> collect = findMembers.stream().map(m -> new MemberDto(m.getName())).collect(Collectors.toList()); // 각각의 정보를 MemberDto로 감쌈
    return new Result(collect.size(),collect); // Result의 data로 감쌈
}

@Data
@AllArgsConstructor
static  class Result<T>{
    private T data;
}

@Data
@AllArgsConstructor
static class MemberDto{
  	private int count;
    private String name;
}
```



```json
{
  	"count": 3,
    "data": [ // Result로 감싼 정보
        {// MemberDto로 감싼 정보
            "name": "new_hello"
        },
        {
            "name": "은승원"
        },
        {
            "name": "member1"
        }
    ]
}
```



장점

- Api 스펙과 엔티티가 분리되어 자유로움
- 감싸서 반환하여 Json스펙상 여러 가지 정보를 반환할 수 있음





==> 무조건 entity를 노출하면 안된다.



> ​	인프런 김영한님의 JPA 강의 2편