package by.mogonov.foodtracker.products;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    void save(ProductDTO productDto);
    Page<Product> getAll (Pageable pageable);
    Product get(Long id);
    void update(ProductDTO productDto, Long id);
    void delete (ProductDTO productDto, Long id);
}
