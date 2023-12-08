package kz.zhelezyaka.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase {
    private int id;
    private int userId;
    private int petId;
}
