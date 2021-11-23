package by.mogonov.foodtracker.products;


import by.mogonov.foodtracker.security.UserHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;


@Service
public class DefaultProductService implements ProductService {

    private final ProductRepo productRepo;
    private final UserHolder userHolder;

    public DefaultProductService(ProductRepo productRepo, UserHolder userHolder) {
        this.productRepo = productRepo;
        this.userHolder = userHolder;
    }


    public void save(ProductDTO productDto) {
        Product product = new Product();
        product.setUserCreator(userHolder.getUser());
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setProtein(productDto.getProtein());
        product.setFats(productDto.getFats());
        product.setCarbonates(productDto.getCarbonates());
        product.setMeasure(productDto.getMeasure());
        product.setCalories(product.getCalories());
        productRepo.save(product);
    }

    public Page<Product> getAll(Pageable pageable) {
        return productRepo.findAll(pageable);
    }

    public Page<Product> getAll(String name, Pageable pageable) {
        return productRepo.findProductsByNameContains(name, pageable);
    }

    public Product get(Long id) {
        return productRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Product not found")
        );
    }

    public void update(ProductDTO productDto, Long id) {
        Product productToUpdate = get(id);

        if (productDto.getUpdateDate().isEqual(productToUpdate.getUpdateDate())) {
            productToUpdate.setName(productDto.getName());
            productToUpdate.setBrand(productDto.getBrand());
            productToUpdate.setProtein(productDto.getProtein());
            productToUpdate.setFats(productDto.getFats());
            productToUpdate.setCarbonates((productDto.getCarbonates()));
            productToUpdate.setMeasure(productDto.getMeasure());
            productToUpdate.setCalories(productDto.getCalories());
            productRepo.saveAndFlush(productToUpdate);
        } else {
            throw new OptimisticLockException("Product has already been changed");
        }
    }

    public void delete(ProductDTO productDto, Long id) throws EmptyResultDataAccessException, OptimisticLockException {
        Product dataBaseProduct = get(id);
        if (productDto.getUpdateDate().isEqual(dataBaseProduct.getUpdateDate())) {
            productRepo.deleteById(id);
        } else {
            throw new OptimisticLockException("Product has already been changed");
        }
    }

}
