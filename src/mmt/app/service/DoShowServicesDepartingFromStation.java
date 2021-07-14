package mmt.app.service;

import mmt.app.exceptions.NoSuchStationException;
import mmt.core.Service;
import mmt.core.TicketOffice;
import mmt.core.exceptions.NoSuchStationNameException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;

import java.util.Comparator;

/**
 * 3.2.3 Show services departing from station.
 */
public class DoShowServicesDepartingFromStation extends Command<TicketOffice> {
    Input<String> _nameDepartingStation;

    /**
     * @param receiver
     */
    public DoShowServicesDepartingFromStation(TicketOffice receiver) {
        super(Label.SHOW_SERVICES_DEPARTING_FROM_STATION, receiver);
        _nameDepartingStation = _form.addStringInput(Message.requestStationName());
    }

    /**
     * @see pt.tecnico.po.ui.Command#execute()
     */
    @Override
    public final void execute() throws DialogException {
        _form.parse();
        try {
            _receiver.getServiceStart(_nameDepartingStation.value())
                    .stream()
                    .sorted(Comparator.comparing(service -> service.getFirstStation().getTime(service)))
                    .map(Service::toString)
                    .forEachOrdered(_display::addLine);
        } catch (NoSuchStationNameException nsne) {
            throw new NoSuchStationException(nsne.getName());
        }
        _display.display();
    }
}
