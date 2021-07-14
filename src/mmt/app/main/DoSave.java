package mmt.app.main;

import mmt.core.TicketOffice;
import mmt.core.exceptions.MissingFileAssociationException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;

import java.io.IOException;
/**
 * §3.1.1. Save to file under current name (if unnamed, query for name).
 */
public class DoSave extends Command<TicketOffice> {
    Input<String> _file;

    /**
     * @param receiver
     */
    public DoSave(TicketOffice receiver) {
        super(Label.SAVE, receiver);
        _file = _form.addStringInput(Message.newSaveAs());
    }

    /**
     * @see pt.tecnico.po.ui.Command#execute()
     */
    @Override
    public final void execute() {
        if (_receiver.getAssociatedFile() == null) {
            _form.parse();
            _receiver.associateFile(_file.value());
            _form.clear();
        }
        try {
            _receiver.save();
        } catch (MissingFileAssociationException | IOException e) {
            // shouldn't happen in a controlled test setup
            e.printStackTrace();
        }
    }
}
