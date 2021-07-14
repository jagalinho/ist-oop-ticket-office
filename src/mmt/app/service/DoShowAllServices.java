package mmt.app.service;

import mmt.core.Service;
import mmt.core.TicketOffice;
import pt.tecnico.po.ui.Command;

import java.util.Comparator;

/**
 * 3.2.1 Show all services.
 */
public class DoShowAllServices extends Command<TicketOffice> {
    /**
     * @param receiver
     */
    public DoShowAllServices(TicketOffice receiver) {
        super(Label.SHOW_ALL_SERVICES, receiver);
    }

    /**
     * @see pt.tecnico.po.ui.Command#execute()
     */
    @Override
    public final void execute() {
        _receiver.getServices()
            .stream()
            .sorted(Comparator.comparingInt(Service::getId))
            .map(Service::toString)
            .forEachOrdered(_display::addLine);

        _display.display();
    }
}