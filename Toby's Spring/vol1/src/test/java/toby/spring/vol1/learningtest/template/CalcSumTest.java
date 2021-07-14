package toby.spring.vol1.learningtest.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CalcSumTest {
    Calculator calculator;
    String numFilepath;

    @BeforeEach
    public void setUp() {
        this.calculator = new Calculator();
        this.numFilepath = "F:\\SpringStudy\\Toby's Spring\\vol1\\src\\test\\resources\\numbers.txt";

    }

    @Test
    public void sumOfNumbers() throws IOException {
        int sum = calculator.calcSum(numFilepath);
        assertThat(sum).isEqualTo(10);
    }

    @Test
    public void 곱하기테스트() throws IOException {
        int ret = calculator.calcMultiply(numFilepath);
        assertThat(ret).isEqualTo(24);
    }


}
