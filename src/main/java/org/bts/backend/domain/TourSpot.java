package org.bts.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicUpdate
public class TourSpot {

    @Id
    @Column(name = "content_id")
    private String id; // API에서 받아온 content_id

    @Column(name = "content_type_id", nullable = false)
    private String typeId; // API에서 받아온 content_type_id

    @Column(nullable = false)
    private String title; // 관광지명

    @ColumnDefault("1")
    private Long likeCount;

    @ColumnDefault("1")
    private Long dislikeCount;


    // -- 생성자 메서드 -- //
    private TourSpot(String id, String typeId, String title) {
        this.id = id;
        this.typeId = typeId;
        this.title = title;
    }

    private static TourSpot of(String id, String typeId, String title) {
        return new TourSpot(id, typeId, title);
    }

    // -- setter 메서드 -- //
    public void increaseLikeCount() {
        this.likeCount += 1;
    }

    public void decreaseLikeCount() {
        if (this.likeCount <= 1) {
            this.likeCount = 1L;
            return;
        }
        this.likeCount -= 1;
    }

    public void increaseDislikeCount() {
        this.dislikeCount += 1;
    }

    public void decreaseDislikeCount() {
        if(this.dislikeCount <= 1) {
            this.dislikeCount = 1L;
            return;
        }
        this.dislikeCount -= 1;
    }

    // redis로 좋아요 수 관리할 경우 사용
    public void updateLikeCount(Long newLikeCount) {
        this.likeCount = newLikeCount;
    }
}
