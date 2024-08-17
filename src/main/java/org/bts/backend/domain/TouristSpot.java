package org.bts.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicUpdate
public class TouristSpot {

    @Id
    @Column(name = "content_id")
    private String id; // API에서 받아온 content_id

    @Column(name = "content_type_id", nullable = false)
    private String typeId; // API에서 받아온 content_type_id

    @ColumnDefault("0")
    private Long likeCount;

    // -- 생성자 메서드 -- //
    private TouristSpot(String id, String typeId) {
        this.id = id;
        this.typeId = typeId;
    }

    private static TouristSpot of(String id, String typeId) {
        return new TouristSpot(id, typeId);
    }

    // -- setter 메서드 -- //
    public void increaseLikeCount() {
        this.likeCount += 1;
    }

    public void decreaseLikeCount() {
        if (this.likeCount <= 0) {
            this.likeCount = 0L;
            return;
        }
        this.likeCount -= 1;
    }

    // redis로 좋아요 수 관리할 경우 사용
    public void updateLikeCount(Long newLikeCount) {
        this.likeCount = newLikeCount;
    }
}
