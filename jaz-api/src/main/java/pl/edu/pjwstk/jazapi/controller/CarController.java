package pl.edu.pjwstk.jazapi.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jazapi.dto.CarDTO;
import pl.edu.pjwstk.jazapi.model.Car;
import pl.edu.pjwstk.jazapi.service.CarService;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/cars")
public class CarController extends CrudController<Car, CarDTO> {

    private final CarService service;

    public CarController(CarService service) {
        super(service);
        this.service = service;
    }

    @Override
    public Function<Car, CarDTO> transformToDTO() {
        return CarDTO::new;
    }

    @Override
    public Function<Collection<EntityModel<CarDTO>>, CollectionModel<EntityModel<CarDTO>>> addLinksForCollection() {
        return cars -> CollectionModel.of(cars,
                linkTo(methodOn(CarController.class).getAll()).withRel("cars"));
    }

    @Override
    public Function<CarDTO, EntityModel<CarDTO>> addLinksForItem() {
        return car -> EntityModel.of(car,
                linkTo(methodOn(CarController.class).getById(car.getId())).withSelfRel(),
                linkTo(methodOn(CarController.class).getByManufacturer(car.getManufacturer())).withRel("manufacturer"));
    }
// toAdd: getter with cars manufacturer manu, stream that creates list of mappings with names of
// manufacturers, create payload that adds those links to list created before

    @GetMapping("/manufacturer/{manufacturer}")
    public ResponseEntity<CollectionModel<EntityModel<CarDTO>>> getByManufacturer(@PathVariable("manufacturer") String manufacturer) {
        try {
            var cars = service.getByManufacturer(manufacturer);
            var carsWithLinks = cars.stream()
                    .map(obj -> transformToDTO().apply(obj))
                    .map(obj -> addLinksForItem().apply(obj))
                    .collect(Collectors.toList());

            var payload = addLinksForCollection().apply(carsWithLinks);
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //
}
