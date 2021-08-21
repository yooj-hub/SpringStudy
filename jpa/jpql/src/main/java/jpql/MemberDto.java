package jpql;

public class MemberDto {
    private String username;
    private Integer age;

    @Override
    public String toString() {
        return "MemberDto{" +
                "username='" + username + '\'' +
                ", age=" + age +
                '}';
    }

    public MemberDto(String username, Integer age) {
        this.username = username;
        this.age = age;
    }
}
