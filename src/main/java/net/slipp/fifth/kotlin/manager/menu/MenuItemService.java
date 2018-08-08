package net.slipp.fifth.kotlin.manager.menu;

import java.util.ArrayList;
import java.util.List;

public interface MenuItemService {

	MenuItem findOne(Long id);
	List<MenuItem> findAll();
	MenuItem save(MenuItem entity);
	
}
