package kz.zhelezyaka.dto.mapper;

import kz.zhelezyaka.dto.PurchaseDTO;
import kz.zhelezyaka.entity.Purchase;

public class PurchaseMapper implements Mapper<Purchase, PurchaseDTO> {

    @Override
    public Purchase toEntity(PurchaseDTO dto) {
        Purchase purchase = new Purchase();
        purchase.setId(dto.getId());
        return purchase;
    }

    @Override
    public PurchaseDTO toDto(Purchase purchase) {
        return PurchaseDTO.builder()
                .id(purchase.getId())
                .build();
    }
}
