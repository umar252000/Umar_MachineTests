package com.umar.machinetests.service;

import java.util.List;

import com.umar.machinetests.dto.CategoryDto;
import com.umar.machinetests.dto.ProductDto;

public interface ProductMethod {

	ProductDto insert(ProductDto crudBean);
	ProductDto update(ProductDto crudDto,Long id); 
	void delete(Long id);
	ProductDto get(Long id);
	List<ProductDto> get(Integer page,Integer pagesize);
	
	
}
