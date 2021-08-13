package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.Transactional;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            //영속
            Member member1 = new Member(200L, "A");
            Member member2 = new Member(210L, "B");

            em.persist(member1);
            em.persist(member2);
            

            System.out.println("=======================");
            tx.commit();

//            //같은 유저에 대하여 select 쿼리 1번
//            Member findMember1 = em.find(Member.class, 100L);
//            Member findMember2 = em.find(Member.class, 100L);
//            //동일성을 보장해준다 ( 같은 transaction 에서 사용해야한다.
//            System.out.println("result = " + (findMember2 == findMember1));
//            tx.commit();// commit 시 insert sql을 보냄


//            //비영속 상태
//            Member member = new Member();
//            member.setName("HelloJPA");
//            member.setId(100L);
//
//            //영속
//            System.out.println("=== BEFORE ===");
//            em.persist(member);
//            System.out.println("=== AFTER ===");
//            //select 쿼리가 나가지않고, 조회됨
//            Member findMember = em.find(Member.class, 100L);
//
//            System.out.println("findMember.getId() = " + findMember.getId());
//            System.out.println("findMember.getName() = " + findMember.getName());


            /**
             * JPQL 모든 멤버 조회
             *
             * 애플리케이션이 필요한 데이터만 db에서 불러오려면 결국 검색 조건이 포함된 sql 이 필요함
             */
            /*tx.begin();
            List<Member> result = em.createQuery("Select m from Member as m"
                    , Member.class)
                    .getResultList();
            for (Member member : result) {
                System.out.println("member.getName() = " + member.getName());
            }
            tx.commit();*/

//            tx.begin();
//            Member findMember = em.find(Member.class, 1L);
//            findMember.setName("JPA");
//            tx.commit();
            /**
             * db 에서 찾기 // em.remove 로 삭제 가능
             */
            /*tx.begin();
            Member findMember = em.find(Member.class, 1L);
            System.out.println("findMember.getId() = " + findMember.getId());
            System.out.println("findMember.getName() = " + findMember.getName());
            tx.commit();*/
            /**
             * db 에 아이디 저장
             */
//            tx.begin();
//            Member member = new Member();
//            member.setId(1L);
//            member.setName("HelloA");
//            em.persist(member);
//            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();

        }

    }
}
