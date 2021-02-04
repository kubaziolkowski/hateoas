package pl.edu.pjwstk.jazapi.dto;

import org.springframework.hateoas.server.core.Relation;
import pl.edu.pjwstk.jazapi.model.AddOn;
import pl.edu.pjwstk.jazapi.service.Identifiable;

import java.math.BigDecimal;

//adding relational connection between single item and it's list where it is being stored
@Relation(itemRelation = "addon", collectionRelation = "addons")
public class AddOnDTO implements Identifiable {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long car;

    public AddOnDTO(AddOn entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.car = entity.getCar().getId();
    }

    public AddOnDTO(Long id, String name, BigDecimal price, Long car) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.car = car;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getCar() {
        return car;
    }
}
