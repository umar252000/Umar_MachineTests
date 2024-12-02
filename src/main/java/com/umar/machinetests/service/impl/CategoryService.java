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
import com.umar.machinetests.exception.ResourceNotFoundException;
import com.umar.machinetests.model.Category;
import com.umar.machinetests.repository.CategoryRepository;
import com.umar.machinetests.service.CategoryMethod;

@Service
public class CategoryService implements CategoryMethod {

    // SLF4J Logger
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository crudRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Cacheable(cacheNames = "Category_Cache")
    public CategoryDto insert(CategoryDto crudDto) throws Exception {
        logger.info("Attempting to insert Category with name: {}", crudDto.getName());

        Optional<Category> findByName = crudRepo.findByName(crudDto.getName());
        if (findByName.isPresent()) {
            logger.warn("Category name '{}' already exists. Insertion failed.", crudDto.getName());
            throw new Exception("Category name already exists");
        }

        Category map = this.modelMapper.map(crudDto, Category.class);
        Category save = this.crudRepo.save(map);

        logger.info("Category with name '{}' inserted successfully.", crudDto.getName());
        return this.modelMapper.map(save, CategoryDto.class);
    }

    @CachePut(cacheNames = "Category_Cache", key = "#categoryDto.id")
    public CategoryDto update(CategoryDto categoryDto, Long id) {
        logger.info("Attempting to update Category with id: {}", id);

        Category category = this.crudRepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category not found with id: {}", id);
                    return new ResourceNotFoundException("Product not found with id: " + id);
                });

        if (categoryDto.getName() != null) {
            category.setName(categoryDto.getName());
            logger.info("Category name updated to '{}'", categoryDto.getName());
        }

        Category save = this.crudRepo.save(category);
        logger.info("Category with id '{}' updated successfully.", id);

        return this.modelMapper.map(save, CategoryDto.class);
    }

    @CacheEvict(cacheNames = "Category_Cache", key = "#id")
    public void delete(Long id) {
        logger.info("Attempting to delete Category with id: {}", id);

        Category crudBean = this.crudRepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category not found with id: {}", id);
                    return new ResourceNotFoundException("Product not found with id: " + id);
                });

        this.crudRepo.deleteById(id);
        logger.info("Category with id '{}' deleted successfully.", id);
    }

    @Cacheable(cacheNames = "Category_Cache", key = "#id")
    public CategoryDto get(Long id) {
        logger.info("Attempting to fetch Category with id: {}", id);

        Category crudBean = this.crudRepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category not found with id: {}", id);
                    return new ResourceNotFoundException("Product not found with id: " + id);
                });

        logger.info("Category with id '{}' fetched successfully.", id);
        return this.modelMapper.map(crudBean, CategoryDto.class);
    }

    @Cacheable(cacheNames = "Category_Cache", key = "'page:' + #page + ':pagesize:' + #pagesize")
    public List<CategoryDto> get(Integer page, Integer pagesize) {
        logger.info("Fetching Categories with page: {} and page size: {}", page, pagesize);

        Pageable pageable = PageRequest.of(page, pagesize);

        Page<Category> findAll = this.crudRepo.findAll(pageable);
        List<Category> content = findAll.getContent();
        List<CategoryDto> cruddto = content.stream()
                .map((find) -> this.modelMapper.map(find, CategoryDto.class))
                .collect(Collectors.toList());

        logger.info("Fetched {} Categories.", cruddto.size());
        return cruddto;
    }
}
