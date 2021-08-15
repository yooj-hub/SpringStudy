package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            Team team = new Team();
            team.setName("TeamA");
            Team team1 = new Team();
            team1.setName("TeamB");
            em.persist(team);
            em.persist(team1);


            Member member = new Member();
            member.setUsername("member1");
            member.changeTeam(team);
            em.persist(member);

            //db 에서 가져온걸 쓸려면 해야함
//            em.flush();
//            em.clear();

            //위에 두 줄을 안할 경우 find 가 나중에 나감
            Member find = em.find(Member.class, member.getId());
            Team findTeam = find.getTeam();
//            find.setTeam(team1);
            System.out.println("findTeam.getName() = " + findTeam.getName());
            List<Member> findMembers = findTeam.getMembers();
            for (Member findMember : findMembers) {
                System.out.println("findMember = " + findMember.getUsername());
            }


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();

        }
    }
}

