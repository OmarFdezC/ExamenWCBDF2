package com.examen.cofc.Controllers;

import com.examen.cofc.Entities.InventoryEntity;
import com.examen.cofc.Services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventories")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    //GET de todos
    @GetMapping
    public ResponseEntity<List<InventoryEntity>> getAllInventories(){
        return ResponseEntity.ok(inventoryService.getAllInventories());
    }

    //GET
    @GetMapping("/{id}")
    public ResponseEntity<InventoryEntity> getInventoryById(@PathVariable Long id){
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    //POST
    @PostMapping
    public ResponseEntity<InventoryEntity> createInventory(@RequestBody InventoryEntity inventory) {
        try {
         return ResponseEntity.ok(inventoryService.createInventory(inventory));
        } catch (ObjectOptimisticLockingFailureException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Conflicto debido a concurrencia
        }
}

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<InventoryEntity> updateInventory(@RequestBody InventoryEntity inventory, @PathVariable Long id){
        inventory.setInventory_id(id);
        return ResponseEntity.ok(inventoryService.updateInventory(inventory));
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<InventoryEntity> deleteInventory(@PathVariable Long id){
        inventoryService.deleteInventoryById(id);
        return ResponseEntity.noContent().build();
    }
}