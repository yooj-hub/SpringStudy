package jpql;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member member = new Member();
            member.setUsername("TeamA");
            member.setAge(10);
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);
            member.setTeam(team);
            em.persist(member);
            em.flush();
            em.clear();
            String query =
                    "select " +
                            "case when m.age <= 10 then '학생요금'" +
                            "     when m.age >= 60 then '경로요금'" +
                            "     else '일반요금'" +
                            "end " +
                    "from Member m";
            List<String> resultList = em.createQuery(query, String.class).getResultList();
            System.out.println("resultList.get(0) = " + resultList.get(0));


////            String s = "select m from Member m inner join m.team t";
////            String s = "select m from Member m left join m.team t";
////            String s = "select m from Member m left join m.team t on t.name = 'teamA'";
//            String s = "select m from Member m left Join Team t on m.username=t.name";
//            List<Member> resultList = em.createQuery(s, Member.class).getResultList();
//            System.out.println("resultList.size() = " + resultList.size());
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.out.println("error = " + e);
        } finally {
            em.close();
        }
        emf.close();

    }
}
