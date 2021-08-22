package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Team teamA = new Team();
            teamA.setName("TeamA");
            Team teamB = new Team();
            teamB.setName("TeamB");
            em.persist(teamA);
            em.persist(teamB);


            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setAge(10);
            member1.setTeam(teamA);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setAge(12);
            member2.setTeam(teamA);

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setAge(13);
            member3.setTeam(teamB);

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

//            em.flush();
//            em.clear();
            //flush 자동 호출
            int i = em.createQuery("update Member m set m.age = m.age + 1").executeUpdate();
            System.out.println("i = " + i);
            System.out.println("member1 = " + member1);//이 때 값이 변하지 않음
            Member findMember1 = em.find(Member.class, member1.getId()); // 10 출력
            System.out.println("findMember.getAge() = " + findMember1.getAge()); // 10 출럭
            //clear를 하여 db에서 값을 가져올 경우 값이 변한다.
            em.clear();
            Member findMember2 = em.find(Member.class, member1.getId());
            System.out.println("findMember2 = " + findMember2);// 11출력
            List<Member> select_m_from_member_m = em.createQuery("select m from Member m", Member.class).getResultList();
            System.out.println("select_m_from_member_m = " + select_m_from_member_m);
//            List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class).setParameter("username", "member1").getResultList();
//            System.out.println("resultList = " + resultList);
//            String jpql = "select m from Member m where m.team  = :team ";
//            List<Member> team = em.createQuery(jpql, Member.class).setParameter("team", teamA).getResultList();
//            for (Member member : team) {
//                System.out.println("member = " + member);
//            }

//            String jpql = "select m from Member m where m.id = :memberId";
//            List resultList = em.createQuery(jpql)
//                    .setParameter("memberId",member1.getId())
//                    .getResultList();
//            for (Object o : resultList) {
//                System.out.println("o = " + o);
//            }

//            String query = " select  t from Team t join fetch t.members";
//            List<Team> resultList = em.createQuery(query, Team.class).getResultList();
//            System.out.println("resultList.size() = " + resultList.size());
//            for (Team team : resultList) {
//                System.out.println("team.getName() = " + team.getName());
//                System.out.println("team.getMembers().size() = " + team.getMembers().size());
//                for (Member member : team.getMembers()) {
//                    System.out.println("member = " + member);
//
//                }
//            }
//
//


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
