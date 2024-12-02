package com.umar.machinetests.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umar.machinetests.dto.CategoryDto;
import com.umar.machinetests.dto.ProductDto;
import com.umar.machinetests.exception.ResourceNotFoundException;
import com.umar.machinetests.model.Category;
import com.umar.machinetests.model.Product;
import com.umar.machinetests.repository.CategoryRepository;
import com.umar.machinetests.repository.ProductRepository;
import com.umar.machinetests.service.CategoryMethod;
import com.umar.machinetests.service.ProductMethod;

import jakarta.transaction.Transactional;

@Service
public class ProductService implements ProductMethod {

	// SLF4J Logger
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryMethod categoryService;

	@Autowired
	private ModelMapper modelMapper;

	@Cacheable(cacheNames = "Product_Cache")
	@Transactional
	public ProductDto insert(ProductDto crudDto) {
		logger.info("Attempting to insert Product with name: {}", crudDto.getName());

		Product product = modelMapper.map(crudDto, Product.class);

		Category category = this.categoryRepository.findByName(crudDto.getCategory().getName()).orElseThrow(() -> {
			logger.error("Category '{}' not found for Product '{}'", crudDto.getCategory().getName(),
					crudDto.getName());
			return new ResourceNotFoundException("Category not found: " + crudDto.getCategory().getName());
		});

		product.setCategory(category);

		Product savedProduct = productRepository.save(product);

		logger.info("Product with name '{}' inserted successfully.", crudDto.getName());
		return modelMapper.map(savedProduct, ProductDto.class);
	}

	@CachePut(cacheNames = "Product_Cache", key = "#productDto.id")
	public ProductDto update(ProductDto productDto, Long id) {
		logger.info("Attempting to update Product with id: {}", id);

		Product product = this.productRepository.findById(id).orElseThrow(() -> {
			logger.error("Product not found with id: {}", id);
			return new ResourceNotFoundException("Product not found with id: " + id);
		});

		if (productDto.getName() != null) {
			product.setName(productDto.getName());
			logger.info("Product name updated to '{}'", productDto.getName());
		}
		if (productDto.getDescription() != null) {
			product.setDescription(productDto.getDescription());
			logger.info("Product description updated.");
		}
		if (productDto.getPrice() != 0) {
			product.setPrice(productDto.getPrice());
			logger.info("Product price updated to '{}'", productDto.getPrice());
		}

		Product savedProduct = this.productRepository.save(product);

		logger.info("Product with id '{}' updated successfully.", id);
		return this.modelMapper.map(savedProduct, ProductDto.class);
	}

	@CacheEvict(cacheNames = "Product_Cache", key = "#id")
	public void delete(Long id) {
		logger.info("Attempting to delete Product with id: {}", id);

		Product product = this.productRepository.findById(id).orElseThrow(() -> {
			logger.error("Product not found with id: {}", id);
			return new ResourceNotFoundException("Product not found with id: " + id);
		});

		this.productRepository.deleteById(id);

		logger.info("Product with id '{}' deleted successfully.", id);
	}

	@Cacheable(cacheNames = "Product_Cache", key = "#id")
	public ProductDto get(Long id) {
		logger.info("Attempting to fetch Product with id: {}", id);

		Product product = this.productRepository.findById(id).orElseThrow(() -> {
			logger.error("Product not found with id: {}", id);
			return new ResourceNotFoundException("Product not found with id: " + id);
		});

		logger.info("Product with id '{}' fetched successfully.", id);
		return this.modelMapper.map(product, ProductDto.class);
	}

	@Cacheable(cacheNames = "Product_Cache", key = "'page:' + #page + ':pagesize:' + #pagesize")
	public List<ProductDto> get(Integer page, Integer pagesize) {
		logger.info("Fetching Products with page: {} and page size: {}", page, pagesize);

		Pageable pageable = PageRequest.of(page, pagesize);

		Page<Product> findAll = this.productRepository.findAll(pageable);
		List<Product> content = findAll.getContent();
		List<ProductDto> cruddto = content.stream().map((find) -> this.modelMapper.map(find, ProductDto.class))
				.collect(Collectors.toList());

		logger.info("Fetched {} Products.", cruddto.size());
		return cruddto;
	}
}
