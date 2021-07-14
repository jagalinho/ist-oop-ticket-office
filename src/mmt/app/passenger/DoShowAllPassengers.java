package mmt.app.passenger;

import mmt.core.Passenger;
import mmt.core.TicketOffice;
import pt.tecnico.po.ui.Command;

import java.util.Comparator;

/**
 * §3.3.1. Show all passengers.
 */
public class DoShowAllPassengers extends Command<TicketOffice> {

    /**
     * @param receiver
     */
    public DoShowAllPassengers(TicketOffice receiver) {
        super(Label.SHOW_ALL_PASSENGERS, receiver);
    }

    /**
     * @see pt.tecnico.po.ui.Command#execute()
     */
    @Override
    public final void execute() {
        _receiver.getPassengers()
            .stream()
            .sorted(Comparator.comparingInt(Passenger::getId))
            .map(Passenger::toString)
            .forEachOrdered(_display::addLine);
        _display.display();
    }
}
