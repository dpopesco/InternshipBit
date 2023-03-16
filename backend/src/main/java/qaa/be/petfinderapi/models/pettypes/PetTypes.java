package qaa.be.petfinderapi.models.pettypes;

import lombok.Getter;
import lombok.Setter;
import qaa.be.petfinderapi.models.pettypes.PetType;

import java.util.ArrayList;

@Getter
@Setter
public class PetTypes {

    ArrayList<PetType> petTypesList;
}
