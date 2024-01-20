package kz.zhelezyaka.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase {
    private int id;
    private User user;
    private Pet pet;
}
