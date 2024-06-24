package com.PFE.Assembler;

import com.PFE.Entity.ConducteurEntity;
import com.PFE.Service.ConducteurService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ConducteurAssembler implements RepresentationModelAssembler<ConducteurEntity, EntityModel<ConducteurEntity>> {
    @Override
    public EntityModel<ConducteurEntity> toModel(ConducteurEntity entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ConducteurService.class).getByEmail(entity.getEmail())).withSelfRel(),
                linkTo(methodOn(ConducteurService.class).getAll()).withRel("conducteurs"));
    }
}
