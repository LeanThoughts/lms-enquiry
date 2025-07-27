package pfs.lms.enquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfs.lms.enquiry.domain.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Product findByCode(String code);
}
