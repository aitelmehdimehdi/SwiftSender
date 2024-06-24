package com.PFE.Assembler;

import com.PFE.Service.CamionService;
import com.PFE.Entity.CamionEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CamionAssembler implements RepresentationModelAssembler<CamionEntity, EntityModel<CamionEntity>> {

    @Override
    public EntityModel<CamionEntity> toModel(CamionEntity entity) {

        return EntityModel.of(entity,
                linkTo(methodOn(CamionService.class).findByMatricule(entity.getMatricule())).withRel(entity.getMatricule()),
                linkTo(methodOn(CamionService.class).getAll()).withRel("camions"));
    }
}
