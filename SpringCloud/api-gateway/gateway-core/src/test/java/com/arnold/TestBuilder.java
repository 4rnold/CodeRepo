package com.arnold;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Builder()
@Data
public class TestBuilder {

    @Builder.Default
    private String name = "arnold";

    private int age;

    private String address;

    @Tolerate
    public TestBuilder() {
    }

    public static void main(String[] args) {
        TestBuilder build = TestBuilder.builder().age(11).build();
        System.out.println(build.getAge() + " " + build.getName());
    }


}
