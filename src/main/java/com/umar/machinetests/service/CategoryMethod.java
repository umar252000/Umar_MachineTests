package com.umar.machinetests.service;

import java.util.List;

import com.umar.machinetests.dto.CategoryDto;
import com.umar.machinetests.dto.ProductDto;
import com.umar.machinetests.exception.GlobalExceptionHandler;

public interface CategoryMethod {

	CategoryDto insert(CategoryDto crudBean)throws Exception;
	CategoryDto update(CategoryDto crudDto,Long id);
	void delete(Long id);
	CategoryDto get(Long id);
	List<CategoryDto> get(Integer page,Integer pagesize);
	
}
