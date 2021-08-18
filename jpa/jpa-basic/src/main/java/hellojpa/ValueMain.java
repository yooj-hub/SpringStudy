package hellojpa;

import javax.persistence.criteria.CriteriaBuilder;

public class ValueMain {
    public static void main(String[] args) {
        Integer a = new Integer(10);
        Integer b = a;
        b=120;
        System.out.println("a = " + a);
        System.out.println("b = " + b);


    }
}
