package kz.zhelezyaka.service;

import kz.zhelezyaka.dao.PurchaseDAO;
import kz.zhelezyaka.dao.UserDAO;
import kz.zhelezyaka.dto.PurchaseDTO;
import kz.zhelezyaka.dto.UserDTO;
import kz.zhelezyaka.dto.mapper.Mapper;
import kz.zhelezyaka.dto.mapper.PurchaseMapper;
import kz.zhelezyaka.dto.mapper.UserMapper;
import kz.zhelezyaka.entity.Purchase;
import kz.zhelezyaka.entity.User;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseDAO purchaseDAO;
    private Mapper<Purchase, PurchaseDTO> mapper;

    public PurchaseServiceImpl() {
        this.purchaseDAO = new PurchaseDAO();
        this.mapper = new PurchaseMapper();
    }

    @Override
    public List<PurchaseDTO> getAllPurchases() {
        return purchaseDAO.getAllPurchases()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public PurchaseDTO getPurchaseById(int id) {
        return mapper.toDto(purchaseDAO.getPurchaseById(id));
    }
}
