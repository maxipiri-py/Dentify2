package com.dentify.paciente.Assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import com.dentify.paciente.Model.PacienteModel;
import com.dentify.paciente.Controller.PacienteController;


@Component
public class PacienteModelAssembler  implements RepresentationModelAssembler<PacienteModel, EntityModel<PacienteModel>>{
    @Override
    public EntityModel<PacienteModel> toModel(PacienteModel entity){
        return EntityModel.of(entity,
            linkTo(methodOn(controller: PacienteController.class).buscarPaciente(entity.getId())).withSelfRel(),
            linkTo(methodOn(controller: PacienteController.class).buscarTodos()).withSelfRel()
        );
    }

}
