package com.PFE.Assembler;

import com.PFE.Service.ServiceService;
import com.PFE.Entity.ServiceEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ServiceAssembler implements RepresentationModelAssembler<ServiceEntity,EntityModel<ServiceEntity>> {

    @Override
    public EntityModel<ServiceEntity> toModel(ServiceEntity entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ServiceService.class).findById(entity.getNum_service())).withRel(entity.getNum_service().toString()),
                linkTo(methodOn(ServiceService.class).getAll()).withRel("services"));
    }
}
