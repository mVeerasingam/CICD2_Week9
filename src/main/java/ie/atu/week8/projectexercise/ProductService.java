package ie.atu.week8.projectexercise;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ProductService(ProductRepository productRepository, RabbitTemplate rabbitTemplate) {
        this.productRepository = productRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        //publish a message to RabbitMQ when a new product is created
        rabbitTemplate.convertAndSend("productQueue", product);
        System.out.println("Saved product: " + product);
        return savedProduct;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
