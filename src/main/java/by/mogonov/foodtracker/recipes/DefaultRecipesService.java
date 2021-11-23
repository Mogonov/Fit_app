package by.mogonov.foodtracker.recipes;

import by.mogonov.foodtracker.security.UserHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import java.util.List;

@Service
public class DefaultRecipesService implements RecipesService {
    private final RecipesRepo recipesRepo;
    private final IngredientRepo ingredientRepo;
    private final UserHolder userHolder;

    public DefaultRecipesService(RecipesRepo recipesRepo,
                                 IngredientRepo ingredientRepo,
                                 UserHolder userHolder) {
        this.recipesRepo = recipesRepo;
        this.ingredientRepo = ingredientRepo;
        this.userHolder = userHolder;
    }

    @Override
    public void save(RecipeDTO recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setUserCreator(userHolder.getUser());
        List<Ingredient> ingredients = recipeDto.getIngredients();
        for (Ingredient ingredient : ingredients) {
            ingredient.setCreationDate(recipe.getCreationDate());
            ingredient.setUpdateDate(ingredient.getCreationDate());
            ingredientRepo.save(ingredient);
        }
        recipe.setName(recipeDto.getName());
        recipesRepo.save(recipe);
    }

    @Override
    public Page<Recipe> getAll(Pageable pageable) {
        return recipesRepo.findAll(pageable);
    }

    public Page<Recipe> getAll(String name, Pageable pageable) {
        return recipesRepo.findRecipeByNameContains(name, pageable);
    }

    @Override
    public Recipe get(Long id) {
        return recipesRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Recipe not found")
        );
    }

    @Override
    public void update(RecipeDTO recipeDto, Long id) {
        Recipe recipeToUpdate = get(id);
        if (recipeDto.getUpdateDate().isEqual(recipeToUpdate.getUpdateDate())) {
            recipeToUpdate.setName(recipeDto.getName());
            recipeToUpdate.setIngredients(recipeDto.getIngredients());
            recipesRepo.saveAndFlush(recipeToUpdate);
        } else {
            throw new OptimisticLockException("Recipe has already been changed");
        }
    }

    @Override
    public void delete(RecipeDTO recipeDto, Long id) {
        Recipe dataBaseRecipe = get(id);
        if (recipeDto.getUpdateDate().isEqual(dataBaseRecipe.getUpdateDate())) {
            recipesRepo.deleteById(id);
        } else {
            throw new OptimisticLockException("Recipe has already been changed");
        }
    }
}
