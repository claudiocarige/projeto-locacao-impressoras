package br.com.copyimagem.mspersistence.core.domain.enums;

public enum PrinterType {

    LASER_BLACK_AND_WHITE_EASY( 0.04, "Black_Laser" ),
    LASER_BLACK_AND_WHITE_MEDIUM( 0.05, "Black_Laser" ),
    LASER_BLACK_AND_WHITE_HARD( 0.08, "Black_Laser" ),
    LASER_BLACK_AND_WHITE_EXTREME( 0.10, "Black_Laser" ),
    LASER_COLOR_EASY( 0.15, "Color_Laser" ),
    LASER_COLOR_MEDIUM( 0.25, "Color_Laser" ),
    LASER_COLOR_HARD( 0.50, "Color_Laser" ),
    LASER_COLOR_EXTREME( 0.65, "Color_Laser" ),
    INKJET_COLOR_EASY( 0.15, "Color_Jet" ),
    INKJET_COLOR_MEDIUM( 0.20, "Color_Jet" ),
    INKJET_COLOR_HARD( 0.25, "Color_Jet" );

    private final double rate;

    private final String type;

    PrinterType( double rate, String type ) {

        this.rate = rate;
        this.type = type;
    }

    public double getRate() {

        return rate;
    }

    public String getType() {

        return type;
    }
}
