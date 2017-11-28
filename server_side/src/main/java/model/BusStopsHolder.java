package model;

/**
 * Holder for closest Bus stop to my position and closest bus stop to target's position
 */
public class BusStopsHolder {

    private GenericPositionAndNameObject starBusStop;
    private GenericPositionAndNameObject endBusStop;

    public BusStopsHolder() {

    }

    public GenericPositionAndNameObject getStarBusStop() {
        return starBusStop;
    }

    public void setStarBusStop(GenericPositionAndNameObject starBusStop) {
        this.starBusStop = starBusStop;
    }

    public GenericPositionAndNameObject getEndBusStop() {
        return endBusStop;
    }

    public void setEndBusStop(GenericPositionAndNameObject endBusStop) {
        this.endBusStop = endBusStop;
    }
}
