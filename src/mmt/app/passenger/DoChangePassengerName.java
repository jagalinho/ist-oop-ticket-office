package mmt.app.passenger;

import mmt.app.exceptions.DuplicatePassengerNameException;
import mmt.app.exceptions.NoSuchPassengerException;
import mmt.core.TicketOffice;
import mmt.core.exceptions.NoSuchPassengerIdException;
import mmt.core.exceptions.NonUniquePassengerNameException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;

/**
 * §3.3.4. Change passenger name.
 */
public class DoChangePassengerName extends Command<TicketOffice> {
    private Input<Integer> _id;
    private Input<String> _name;

    /**
     * @param receiver
     */
    public DoChangePassengerName(TicketOffice receiver) {
        super(Label.CHANGE_PASSENGER_NAME, receiver);
        _id = _form.addIntegerInput(Message.requestPassengerId());
        _name = _form.addStringInput(Message.requestPassengerName());
    }

    /**
     * @see pt.tecnico.po.ui.Command#execute()
     */
    @Override
    public final void execute() throws DialogException {
        _form.parse();
        try {
            _receiver.changePassengerName(_id.value(), _name.value());
        } catch (NonUniquePassengerNameException nupne) {
            throw new DuplicatePassengerNameException(nupne.getName());
        } catch (NoSuchPassengerIdException nspide) {
            throw new NoSuchPassengerException(nspide.getId());
        }
    }
}
