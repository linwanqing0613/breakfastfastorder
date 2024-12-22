package com.example.breakfastorder.controller;

import com.example.breakfastorder.dto.MenuItemDTO;
import com.example.breakfastorder.dto.ResponseDTO;
import com.example.breakfastorder.entity.MenuItem;
import com.example.breakfastorder.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Void>> createMenuItem(@Valid @RequestBody MenuItemDTO menuItemDTO){
        try {
            menuItemService.createMenuItem(menuItemDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ResponseDTO.create("Menu item created successfully", null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }
    @GetMapping
    public ResponseEntity<ResponseDTO<List<MenuItem>>> getAllMenuItems() {
        try {
            List<MenuItem> menuItemList= menuItemService.getAllMenuItems();
            return ResponseEntity.ok(
                    ResponseDTO.success("Fetched all menu items successfully", menuItemList)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<MenuItem>> getMenuItemById(@PathVariable Long id) {
        try {
            MenuItem menuItem= menuItemService.getMenuItemById(id);
            return ResponseEntity.ok(
                    ResponseDTO.success("Menu item fetched successfully", menuItem)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }
    @PutMapping("/update")
    public ResponseEntity<ResponseDTO<MenuItem>> updateMenuItem(@Valid @RequestBody MenuItemDTO menuItemDTO) {
        try {
            MenuItem updatedMenuItem = menuItemService.updateMenuItem(menuItemDTO);
            return ResponseEntity.ok(
                    ResponseDTO.success("Menu item updated successfully", updatedMenuItem )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteMenuItem(@PathVariable Long id) {
        try {
            menuItemService.deleteMenuItem(id);
            return ResponseEntity.ok(
                    ResponseDTO.success("Menu item deleted successfully", null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }
}
