package kz.zhelezyaka.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User{
    private int id;
    private String name;
}
