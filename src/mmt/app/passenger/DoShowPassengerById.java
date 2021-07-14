package mmt.app.passenger;

import mmt.app.exceptions.NoSuchPassengerException;
import mmt.core.Passenger;
import mmt.core.TicketOffice;
import mmt.core.exceptions.NoSuchPassengerIdException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;

/**
 * §3.3.2. Show specific passenger.
 */
public class DoShowPassengerById extends Command<TicketOffice> {
    Input<Integer> _id;

    /**
     * @param receiver
     */
    public DoShowPassengerById(TicketOffice receiver) {
        super(Label.SHOW_PASSENGER_BY_ID, receiver);

        _id = _form.addIntegerInput(Message.requestPassengerId());
    }

    /**
     * @see pt.tecnico.po.ui.Command#execute()
     */
    @Override
    public final void execute() throws DialogException {
        try {
            _form.parse();
            Passenger p = _receiver.getPassengerById(_id.value());
            if (p.getItineraries().size() > 3)
                _display.addLine(p.toString());
            _display.display();
        } catch (NoSuchPassengerIdException nspide) {
            throw new NoSuchPassengerException(nspide.getId());
        }
    }
}
