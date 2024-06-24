package com.PFE.Assembler;


import com.PFE.Service.RemorqueService;
import com.PFE.Entity.RemorqueEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

    @Component
    public class RemorqueAssembler implements RepresentationModelAssembler<RemorqueEntity, EntityModel<RemorqueEntity>> {

        @Override
        public EntityModel<RemorqueEntity> toModel(RemorqueEntity entity) {

            return EntityModel.of(entity,
                    linkTo(methodOn(RemorqueService.class).findByMatricule(entity.getMatricule())).withRel(entity.getMatricule()),
                    linkTo(methodOn(RemorqueService.class).getAll()).withRel("remorques"));
        }
    }

