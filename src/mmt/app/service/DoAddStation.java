package mmt.app.service;

import mmt.app.exceptions.DuplicatePassengerNameException;
import mmt.app.exceptions.NoSuchServiceException;
import mmt.core.TicketOffice;
import mmt.core.exceptions.NoSuchServiceIdException;
import mmt.core.exceptions.NonUniquePassengerNameException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;

/**
 * 3.2.2 Show service by number.
 */
public class DoAddStation extends Command<TicketOffice> {
    private Input<String> _name;

    /**
     * @param receiver
     */
    public DoAddStation(TicketOffice receiver) {
        super("Adicionar uma estação", receiver);
        _name = _form.addStringInput("Nome da estação: ");
    }

    /**
     * @see Command#execute()
     */
    @Override
    public final void execute() throws DialogException {
        _form.parse();
        try {
            _receiver.addStation(_name.value());
        } catch (NonUniquePassengerNameException e) {
            throw new DuplicatePassengerNameException(_name.value());
        }
    }
}
