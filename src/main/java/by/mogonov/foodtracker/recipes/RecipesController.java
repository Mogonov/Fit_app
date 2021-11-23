package by.mogonov.foodtracker.recipes;

import by.mogonov.foodtracker.converter.TimeConverter;
import by.mogonov.foodtracker.security.UserHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OptimisticLockException;

@RestController
@RequestMapping("/api/recipes")
public class RecipesController {

    private final RecipesService recipeService;
    private final UserHolder userHolder;

    public RecipesController(RecipesService recipeService, UserHolder userHolder) {
        this.recipeService = recipeService;
        this.userHolder = userHolder;
    }

    @GetMapping
    public ResponseEntity<Page<Recipe>> index(@RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "size", defaultValue = "2") int size,
                                              @RequestParam(required = false) String name) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        Page<Recipe> recipes;
        if (name != null) {
            recipes = recipeService.getAll(name, pageable);
        } else {
            recipes = recipeService.getAll(pageable);
        }
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable("id") Long id) {
        try {
            Recipe recipe = recipeService.get(id);
            return new ResponseEntity<>(recipe, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody RecipeDTO recipeDto) {
        recipeService.save(recipeDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}/dt_update/{dt_update}")
    public ResponseEntity<?> update(@RequestBody RecipeDTO recipeDto,
                                    @PathVariable("id") Long id,
                                    @PathVariable("dt_update") Long dtUpdate) {
        try {
            recipeDto.setUpdateDate(TimeConverter.microsecondsToLocalDateTime(dtUpdate));
            recipeService.update(recipeDto, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OptimisticLockException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}/dt_update/{dt_update}")
    public ResponseEntity<?> delete(@RequestBody RecipeDTO recipeDto,
                                    @PathVariable("id") Long id,
                                    @PathVariable("dt_update") Long dtUpdate) {
        try {
            recipeDto.setUpdateDate(TimeConverter.microsecondsToLocalDateTime(dtUpdate));
            recipeService.delete(recipeDto, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
