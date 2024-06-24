package com.PFE.Assembler;

import com.PFE.Entity.TransportEnCommunEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class TransitAssembler implements RepresentationModelAssembler<TransportEnCommunEntity, EntityModel<TransportEnCommunEntity>>{
    @Override
    public EntityModel<TransportEnCommunEntity> toModel(TransportEnCommunEntity entity) {
        return null;
    }
}
