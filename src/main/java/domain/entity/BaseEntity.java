package domain.entity;

import lombok.Getter;

import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {
    private LocalDateTime createDate;
    private LocalDateTime editDate;

    public BaseEntity() {
        createDate = LocalDateTime.now();
        editDate = LocalDateTime.now();
    }

    @PreUpdate
    public void updateEditDate() {
        editDate = LocalDateTime.now();
    }
}