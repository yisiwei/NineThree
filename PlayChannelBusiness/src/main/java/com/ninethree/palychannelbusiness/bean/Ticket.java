package com.ninethree.palychannelbusiness.bean;

import java.io.Serializable;

public class Ticket implements Serializable {

    private static final long serialVersionUID = -5265570180245282106L;

    private String TicketMacId;
    private String TicketMacGuid;
    private String TicketMacText;

    public String getTicketMacId() {
        return TicketMacId;
    }

    public void setTicketMacId(String ticketMacId) {
        TicketMacId = ticketMacId;
    }

    public String getTicketMacGuid() {
        return TicketMacGuid;
    }

    public void setTicketMacGuid(String ticketMacGuid) {
        TicketMacGuid = ticketMacGuid;
    }

    public String getTicketMacText() {
        return TicketMacText;
    }

    public void setTicketMacText(String ticketMacText) {
        TicketMacText = ticketMacText;
    }

}
