package by.mogonov.foodtracker.products;

import by.mogonov.foodtracker.converter.TimeConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OptimisticLockException;


@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final DefaultProductService productService;

    public ProductsController(DefaultProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> list(@RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "size", defaultValue = "2") int size,
                                              @RequestParam(required = false) String name) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        Page<Product> products;
        if (name != null) {
            products = productService.getAll(name, pageable);
        } else {
            products = productService.getAll(pageable);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        try {
            Product product = productService.get(id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ProductDTO productDto) {
        productService.save(productDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}/dt_update/{dt_update}")
    public ResponseEntity<?> update(@RequestBody ProductDTO productDto,
                                    @PathVariable("id") Long id,
                                    @PathVariable("dt_update") Long dtUpdate) {
        try {
            productDto.setUpdateDate(TimeConverter.microsecondsToLocalDateTime(dtUpdate));
            productService.update(productDto, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OptimisticLockException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}/dt_update/{dt_update}")
    public ResponseEntity<?> delete(@RequestBody ProductDTO productDto,
                                    @PathVariable("id") Long id,
                                    @PathVariable("dt_update") Long dtUpdate) {
        try {
            productDto.setUpdateDate(TimeConverter.microsecondsToLocalDateTime(dtUpdate));
            productService.delete(productDto, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
