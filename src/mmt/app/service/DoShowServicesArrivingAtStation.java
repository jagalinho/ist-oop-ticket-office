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
 * 3.2.4 Show services arriving at station.
 */
public class DoShowServicesArrivingAtStation extends Command<TicketOffice> {
    Input<String> _nameArrivingStation;

    /**
     * @param receiver
     */
    public DoShowServicesArrivingAtStation(TicketOffice receiver) {
        super(Label.SHOW_SERVICES_ARRIVING_AT_STATION, receiver);
        _nameArrivingStation = _form.addStringInput(Message.requestStationName());
    }

    /**
     * @see pt.tecnico.po.ui.Command#execute()
     */
    @Override
    public final void execute() throws DialogException {
        _form.parse();
        try {
            _receiver.getServiceEnd(_nameArrivingStation.value())
                    .stream()
                    .sorted(Comparator.comparing(service -> service.getLastStation().getTime(service)))
                    .map(Service::toString)
                    .forEachOrdered(_display::addLine);
        } catch (NoSuchStationNameException nsne) {
            throw new NoSuchStationException(nsne.getName());
        }
        _display.display();
    }
}
