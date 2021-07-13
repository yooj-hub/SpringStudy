package toby.spring.vol1.learningtest.template;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CalcSumTest {
    @Test
    public void sumOfNumbers() throws IOException {
        Calculator calculator = new Calculator();
        int sum = calculator.calcSum(
                "F:\\SpringStudy\\Toby's Spring\\vol1\\src\\test\\resources\\numbers.txt");
        assertThat(sum).isEqualTo(10);
    }


}
