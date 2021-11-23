package by.mogonov.foodtracker.recipes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipesRepo extends JpaRepository<Recipe, Long> {
    Page<Recipe> findRecipeByNameContains(String name, Pageable pageable);
}
