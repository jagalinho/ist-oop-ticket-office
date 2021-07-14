package mmt.app.itineraries;

import mmt.app.exceptions.NoSuchPassengerException;
import mmt.core.Itinerary;
import mmt.core.Passenger;
import mmt.core.TicketOffice;
import mmt.core.exceptions.NoSuchPassengerIdException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Display;
import pt.tecnico.po.ui.Input;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * §3.4.2. Show all itineraries (for a specific passenger).
 */
public class DoShowMostExpensiveItinerary extends Command<TicketOffice> {
    private Input<Integer> _id;

    /**
     * @param receiver
     */
    public DoShowMostExpensiveItinerary(TicketOffice receiver) {
        super("Mostrar itinerário mais caro para um passageiro", receiver);

        _id = _form.addIntegerInput(Message.requestPassengerId());
    }

    /**
     * @see Command#execute()
     */
    @Override
    public final void execute() throws DialogException {
        try {
            _form.parse();
            Passenger p = _receiver.getPassengerById(_id.value());
            Itinerary itinerary = p.getItineraries().stream().max(Comparator.comparing(Itinerary::getCost)).orElse(null);
            if (itinerary == null)
                _display.addLine(Message.noItineraries(_id.value()));
            else
                _display.addLine(itinerary.printItinerary(1));
            _display.display();
        } catch (NoSuchPassengerIdException nspide) {
            throw new NoSuchPassengerException(nspide.getId());
        }
    }

}
