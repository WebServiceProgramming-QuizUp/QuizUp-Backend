// src/main/java/com/example/quizUp/domain/Stage

package com.example.quizUp.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stage")
public class Stage {

    @Id
    @Column(name = "stage_id", nullable = false, unique = true)
    private String stageId;

    // world_id??

    @Column(nullable = false)
    private String stage_num;

    @Column(nullable = false)
    private String category;

    @Column(name = "time_limit", nullable = false)
    private int timeLimit = 600; //타입 varchar로 돼있는데 계산할 때 생각하면 int가 나을 것 같아서 일단 이렇게 했어요.



    // @Builder를 사용하여 안전하게 객체 생성
    @Builder
    public Stage(String stageId, String stage_num, String category, int timeLimit) {
        this.stageId = stageId;
        this.stage_num = stage_num;
        this.category = category;
        this.timeLimit = timeLimit;
    }
}
