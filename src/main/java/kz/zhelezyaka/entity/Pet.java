package kz.zhelezyaka.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {
    private int id;
    private String name;
}
