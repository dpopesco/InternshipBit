package qaa.be.petfinderapi.enums;

public enum Gender {
    MALE(0),
    FEMALE(1);

    Gender(int genderType) {
        this.genderType = genderType;
    }

    public int getGenderType() {
        return genderType;
    }

    private final int genderType;
}