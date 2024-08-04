package br.com.copyimagem.ms_help_desk.core.domain.enums;


public enum TicketPriority {

    URGENT (4, "Urgent"),
    HIGH (8, "High"),
    MEDIUM  (15, "Medium"),
    LOW (24, "Low");

    private int hoursToAttend;
    private String description;

    TicketPriority(int hoursToAttend, String description) {
        this.hoursToAttend = hoursToAttend;
        this.description = description;
    }

    public int getHoursToAttend() {
        return hoursToAttend;
    }

    public String getDescription() {
        return description;
    }
}
