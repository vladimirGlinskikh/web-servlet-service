package kz.zhelezyaka.service;

import kz.zhelezyaka.dto.PurchaseDTO;

import java.util.List;

public interface PurchaseService {
    List<PurchaseDTO> getAllPurchases();
}
