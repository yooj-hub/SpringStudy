package hellojpa;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        LocalDate now = LocalDate.now();
        LocalDateTime localDateTime= LocalDateTime.now();

        Member member = new Member("은승원",27,RoleType.USER,now,localDateTime,"은승원입니다.",0);
        em.persist(member);

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
//            Member findMember = em.find(Member.class, 1L);
//            System.out.println("findMember.getId() = " + findMember.getId());
//            System.out.println("findMember.getName() = " + findMember.getName());
//            em.remove(findMember);
            List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
//            tx.commit();
            for (Member member1 : members) {
                System.out.println("member.getName() = " + member1.getUsername());
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();


    }

}
