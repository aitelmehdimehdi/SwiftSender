package com.PFE.Assembler;

import com.PFE.Service.ClientService;
import com.PFE.Entity.ClientEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClientAssembler implements RepresentationModelAssembler<ClientEntity,EntityModel<ClientEntity>> {
    @Override
        public EntityModel<ClientEntity> toModel(ClientEntity entity) {
            return EntityModel.of(entity,
                    linkTo(methodOn(ClientService.class).findById(entity.getEmail()))
                            .withRel(entity.getEmail()),
                    linkTo(methodOn(ClientService.class).getAll())
                            .withSelfRel()
                    );
        }
}
