package mmt.app.passenger;

import mmt.app.exceptions.DuplicatePassengerNameException;
import mmt.core.TicketOffice;
import mmt.core.exceptions.NonUniquePassengerNameException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;

/**
 * §3.3.3. Register passenger.
 */
public class DoRegisterPassenger extends Command<TicketOffice> {

    private Input<String> _name;

    /**
     * @param receiver
     */
    public DoRegisterPassenger(TicketOffice receiver) {
        super(Label.REGISTER_PASSENGER, receiver);
        _name = _form.addStringInput(Message.requestPassengerName());
    }

    /**
     * @see pt.tecnico.po.ui.Command#execute()
     */
    @Override
    public final void execute() throws DialogException {
        _form.parse();
        try {
            _receiver.newPassenger(_name.value());
        } catch (NonUniquePassengerNameException nupne) {
            throw new DuplicatePassengerNameException(nupne.getName());
        }
    }
}
