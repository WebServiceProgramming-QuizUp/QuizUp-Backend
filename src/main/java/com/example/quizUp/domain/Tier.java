package com.example.quizUp.domain;

import lombok.Getter;

@Getter
public enum Tier {
    T1("초등학생", 0, 100),
    T2("중학생", 100, 300),
    T3("고등학생", 300, 600),
    T4("대학생", 600, 1000),
    T5("대학원생", 1000, 2000),
    T6("교수", 2000, Integer.MAX_VALUE);

    private final String name;
    private final int minStars;
    private final int nextMinStars;

    Tier(String name, int minStars, int nextMinStars) {
        this.name = name;
        this.minStars = minStars;
        this.nextMinStars = nextMinStars;
    }
}
