package com.PFE.Assembler;

import com.PFE.Service.PickUpVoitureService;
import com.PFE.Entity.PickUpVoitureEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class PickUpVoitureAssembler implements RepresentationModelAssembler<PickUpVoitureEntity, EntityModel<PickUpVoitureEntity>> {

    @Override
    public EntityModel<PickUpVoitureEntity> toModel(PickUpVoitureEntity entity) {

        return EntityModel.of(entity,
                linkTo(methodOn(PickUpVoitureService.class).findByMatricule(entity.getMatricule())).withRel(entity.getMatricule()),
                linkTo(methodOn(PickUpVoitureService.class).getAll()).withRel("pickupvoitures"));
    }

}

