package com.example.breakfastorder.service.impl;

import com.example.breakfastorder.dto.MenuItemDTO;
import com.example.breakfastorder.entity.MenuItem;
import com.example.breakfastorder.repository.MenuItemRepository;
import com.example.breakfastorder.service.MenuItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private static final Logger log = LoggerFactory.getLogger(MenuItemServiceImpl.class);
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public void createMenuItem(MenuItemDTO menuItemDTO) {
        if(menuItemRepository.existsById(menuItemDTO.getId())){
            throw new RuntimeException("Menu item with ID " + menuItemDTO.getId() + " already exists.");
        }
        MenuItem menuItem = convertToEntity(menuItemDTO);
        menuItemRepository.save(menuItem);
        log.info("Menu item created successfully: {}", menuItem);
    }

    @Override
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> menuItems = menuItemRepository.findByIsAvailableTrue();
        log.info("Fetched {} menu items.", menuItems.size());
        return menuItems;
    }

    @Override
    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item with ID " + id + " not found."));
    }

    @Override
    public MenuItem updateMenuItem(MenuItemDTO menuItemDTO) {
        MenuItem menuItem = menuItemRepository.findById(menuItemDTO.getId())
                .orElseThrow(() -> new RuntimeException("Menu item with ID " + menuItemDTO.getId() + " not found."));
        updateEntityFromDTO(menuItem, menuItemDTO);
        menuItemRepository.save(menuItem);
        log.info("Menu item updated successfully: {}", menuItem);
        return menuItem;
    }

    @Override
    public void deleteMenuItem(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new RuntimeException("Menu item with ID " + id + " does not exist.");
        }
        menuItemRepository.deleteById(id);
        log.info("Menu item with ID {} deleted successfully.", id);
    }

    /**
     * 將 MenuItemDTO 轉換為 MenuItem 實體物件。
     * 通常用於創建新的 MenuItem 時，根據 DTO 的內容生成對應的實體。
     *
     * @param menuItemDTO 包含輸入數據的 MenuItemDTO 物件。
     * @return 轉換後的 MenuItem 實體物件。
     */
    private MenuItem convertToEntity(MenuItemDTO menuItemDTO) {
        MenuItem menuItem = new MenuItem();
        updateEntityFromDTO(menuItem, menuItemDTO);
        return menuItem;
    }

    /**
     * 根據 MenuItemDTO 的值，更新指定的 MenuItem 實體物件。
     * 僅更新 DTO 中非空或有效的屬性，保留 MenuItem 原有的其他屬性。
     *
     * @param menuItem 要被更新的 MenuItem 實體物件。
     * @param menuItemDTO 包含新數據的 MenuItemDTO 物件。
     */
    private void updateEntityFromDTO(MenuItem menuItem, MenuItemDTO menuItemDTO) {
        if (menuItemDTO.getName() != null && !menuItemDTO.getName().isEmpty()) {
            menuItem.setName(menuItemDTO.getName());
        }
        if (menuItemDTO.getDescription() != null && !menuItemDTO.getDescription().isEmpty()) {
            menuItem.setDescription(menuItemDTO.getDescription());
        }
        if (menuItemDTO.getPrice() != null) {
            menuItem.setPrice(menuItemDTO.getPrice());
        }
        if (menuItemDTO.getAvailable() != null) {
            menuItem.setAvailable(menuItemDTO.getAvailable());
        }
    }
}
