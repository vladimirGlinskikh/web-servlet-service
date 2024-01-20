package kz.zhelezyaka.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDTO {
    private int id;
    private UserDTO user;
    private PetDTO pet;
}
