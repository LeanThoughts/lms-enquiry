package pfs.lms.enquiry.domain;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Product extends AggregateRoot<Product> {

    private String code;

    private String name;

    public Product() {

    }
}
