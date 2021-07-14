package mmt.app.service;

import mmt.app.exceptions.NoSuchServiceException;
import mmt.core.TicketOffice;
import mmt.core.exceptions.NoSuchServiceIdException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;

/**
 * 3.2.2 Show service by number.
 */
public class DoShowServiceByNumber extends Command<TicketOffice> {
    Input<Integer> _number;

    /**
     * @param receiver
     */
    public DoShowServiceByNumber(TicketOffice receiver) {
        super(Label.SHOW_SERVICE_BY_NUMBER, receiver);
        _number = _form.addIntegerInput(Message.requestServiceId());
    }

    /**
     * @see pt.tecnico.po.ui.Command#execute()
     */
    @Override
    public final void execute() throws DialogException {
        _form.parse();
        try {
            _display.addLine(_receiver.getServiceById(_number.value()).toString());
            _display.display();
        } catch (NoSuchServiceIdException nsid) {
            throw new NoSuchServiceException(nsid.getId());
        }
    }
}
