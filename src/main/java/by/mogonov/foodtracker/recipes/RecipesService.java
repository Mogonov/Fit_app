package by.mogonov.foodtracker.recipes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipesService {

    void save( RecipeDTO recipeDto);
    Page<Recipe> getAll (Pageable pageable);
    Page<Recipe> getAll (String name,Pageable pageable);
    Recipe get(Long id);
    void update(RecipeDTO recipeDto, Long id);
    void delete (RecipeDTO recipeDto, Long id);

}
