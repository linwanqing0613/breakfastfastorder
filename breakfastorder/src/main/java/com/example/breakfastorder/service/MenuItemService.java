package com.example.breakfastorder.service;

import com.example.breakfastorder.dto.MenuItemDTO;
import com.example.breakfastorder.entity.MenuItem;

import java.util.List;

public interface MenuItemService {
    public void createMenuItem(MenuItemDTO menuItemDTO);
    public List<MenuItem> getAllMenuItems();
    public MenuItem getMenuItemById(Long id);
    public MenuItem updateMenuItem(MenuItemDTO menuItemDTO);
    public void deleteMenuItem(Long id);
}
