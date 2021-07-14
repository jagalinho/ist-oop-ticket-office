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
public class DoShowPassengerItineraries extends Command<TicketOffice> {
    private Input<Integer> _id;

    /**
     * @param receiver
     */
    public DoShowPassengerItineraries(TicketOffice receiver) {
        super(Label.SHOW_PASSENGER_ITINERARIES, receiver);

        _id = _form.addIntegerInput(Message.requestPassengerId());
    }

    static void displayItineraries(Display display, Passenger passenger) {
        List<Itinerary> itineraries = passenger.getItineraries();
        int sizeItineraries = itineraries.size();
        if (sizeItineraries == 0) {
            display.addLine(Message.noItineraries(passenger.getId()));
            return;
        }
        itineraries = itineraries.stream()
            .sorted(Comparator.comparing(Itinerary::getDay))
            .collect(Collectors.toList());
        display.addLine(String.format(Locale.US, "== Passageiro %d: %s ==\n\n", passenger.getId(), passenger.getName()));
        for (int i = 0; i < sizeItineraries; i++)
            display.addLine(itineraries.get(i).printItinerary(i + 1));
    }

    /**
     * @see pt.tecnico.po.ui.Command#execute()
     */
    @Override
    public final void execute() throws DialogException {
        try {
            _form.parse();
            displayItineraries(_display, _receiver.getPassengerById(_id.value()));
            _display.display();
        } catch (NoSuchPassengerIdException nspide) {
            throw new NoSuchPassengerException(nspide.getId());
        }
    }

}
