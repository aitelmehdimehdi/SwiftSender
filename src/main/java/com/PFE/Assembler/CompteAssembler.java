package com.PFE.Assembler;

import com.PFE.Service.CompteService;
import com.PFE.Entity.CompteEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CompteAssembler implements RepresentationModelAssembler<CompteEntity, EntityModel<CompteEntity>> {

    @Override
    public EntityModel<CompteEntity> toModel(CompteEntity entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(CompteService.class).findById(entity.getId()))
                        .withRel(entity.getId().toString()),
                linkTo(methodOn(CompteService.class).getAll()).withRel("comptes")
        );
    }
}
