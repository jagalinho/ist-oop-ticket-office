package mmt.core.exceptions;

public class NonUniqueStationAtServiceException extends Exception {
    private static final long serialVersionUID = 1068888801613627561L;
    private String _stationName;
    private int _serviceId;

    public NonUniqueStationAtServiceException(String stationName, int serviceId) {
        _stationName = stationName;
        _serviceId = serviceId;
    }

    public String getStationName() {
        return _stationName;
    }

    public int getServiceId() {
        return _serviceId;
    }
}
