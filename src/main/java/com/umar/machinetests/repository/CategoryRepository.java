package com.umar.machinetests.repository;

import com.umar.machinetests.dto.CategoryDto;
import com.umar.machinetests.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String name);
}