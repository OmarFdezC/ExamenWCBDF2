package com.examen.cofc.Services;

import com.examen.cofc.Entities.InventoryEntity;
import com.examen.cofc.Repositories.InventoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    
    @Autowired
    private InventoryRepository InventoryRepository;

    public List<InventoryEntity> getAllInventories() {
        return InventoryRepository.findAll();
    }

    public InventoryEntity getInventoryById(Long id) {
        return InventoryRepository.findById(id).orElse(null);
    }

    public InventoryEntity createInventory(InventoryEntity inventory) {
        return InventoryRepository.save(inventory);
    }

    public InventoryEntity updateInventory(InventoryEntity inventory) {
        if(InventoryRepository.existsById(inventory.getInventory_id())){
            return InventoryRepository.save(inventory);
        }
        return null;
    }

    public void deleteInventoryById(Long id) {
        if(InventoryRepository.existsById(id)){
            InventoryRepository.deleteById(id);
        } 
    }
}
