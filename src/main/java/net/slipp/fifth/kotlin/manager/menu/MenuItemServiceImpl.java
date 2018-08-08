package net.slipp.fifth.kotlin.manager.menu;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuItemServiceImpl implements MenuItemService {

	@Autowired
	private MenuItemRepository repository;
	
	@Override
	@Transactional
	public MenuItem save(MenuItem entity) {
		return repository.save(entity);
	}

	@Override
	public MenuItem findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public List<MenuItem> findAll() {
		return repository.findAll();
	}


}
