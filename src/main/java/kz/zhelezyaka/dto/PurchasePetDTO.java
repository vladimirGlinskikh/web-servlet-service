package kz.zhelezyaka.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchasePetDTO {
    private PurchaseDTO purchaseDTO;
    private List<Integer> petsId;
}
