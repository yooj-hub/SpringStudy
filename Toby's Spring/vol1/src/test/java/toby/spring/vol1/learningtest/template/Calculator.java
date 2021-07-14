package toby.spring.vol1.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Calculator {

    public Integer calcMultiply(String filepath) throws IOException{
        LineCallback multiplyCallback= new LineCallback() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value * Integer.valueOf(line);
            }
        };
        return lineReadTmplate(filepath, multiplyCallback, 1);
    }


    public Integer calcMultiply2(String filepath) throws IOException {
        BufferedReaderCallback multiplyCallback =
                new BufferedReaderCallback() {
                    public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                        Integer multiply = 1;
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            multiply *= Integer.valueOf(line);
                        }
                        return multiply;
                    }

                    ;
                };
        return fileReadTemplate(filepath, multiplyCallback);

    }

    public Integer calcSum(String filepath) throws IOException {
        LineCallback sumCallback = new LineCallback() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value + Integer.valueOf(line);
            }
        };

        return lineReadTmplate(filepath, sumCallback, 0);

    }

    public Integer calcSum3(String filepath) throws IOException {
        BufferedReaderCallback sumCallback =
                new BufferedReaderCallback() {
                    //bufferReaderCallback 의 doSomethingWithReader 를 오버라이드
                    public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                        int sum = 0;
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            sum += Integer.parseInt(line);
                        }
                        return sum;

                    }
                };
        return fileReadTemplate(filepath, sumCallback);

    }


    public Integer calcSum2(String filepath) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            int sum = 0;
            String line = null;
            while ((line = br.readLine()) != null) {
                sum += Integer.parseInt(line);
            }
            return sum;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            int ret = callback.doSomethingWithReader(br);
            return ret;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public Integer lineReadTmplate(String filepath, LineCallback callback, int initVal) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            Integer res = initVal;
            String line = null;
            while ((line = br.readLine()) != null) {
                res = callback.doSomethingWithLine(line, res);
            }
            return res;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if(br!=null){
                try{
                    br.close();
                }catch (IOException e){
                    System.out.println(e.getMessage());
                }
            }

        }

    }
}
