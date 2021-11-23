package by.mogonov.foodtracker.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository <Product,Long>{
    Page <Product> findProductsByNameContains(String name, Pageable pageable);
}
