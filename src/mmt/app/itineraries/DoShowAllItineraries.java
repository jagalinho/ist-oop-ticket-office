package mmt.app.itineraries;

import mmt.core.Passenger;
import mmt.core.TicketOffice;
import pt.tecnico.po.ui.Command;

import java.util.Comparator;

/**
 * §3.4.1. Show all itineraries (for all passengers).
 */
public class DoShowAllItineraries extends Command<TicketOffice> {

    /**
     * @param receiver
     */
    public DoShowAllItineraries(TicketOffice receiver) {
        super(Label.SHOW_ALL_ITINERARIES, receiver);
    }

    /**
     * @see pt.tecnico.po.ui.Command#execute()
     */
    @Override
    public final void execute() {
        _receiver.getPassengers()
            .stream()
            .filter(p -> !p.getItineraries().isEmpty())
            .sorted(Comparator.comparingInt(Passenger::getId))
            .forEachOrdered(p -> DoShowPassengerItineraries.displayItineraries(_display, p));
        _display.display();
    }
}
