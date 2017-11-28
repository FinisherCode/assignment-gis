package starter.controller;

import model.BusStopsHolder;
import model.GenericPositionAndNameObject;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import resolovers.DataGatherer;

import java.util.List;


/**
 * Handles Rest Api calls. Each method is explained by its name.
 */
@RestController
public class DataController {

    @RequestMapping("/status")
    public String getStatus(){
        return "ok";
    }

    @RequestMapping("/get_vets")
    public List<GenericPositionAndNameObject> getVets(@RequestParam("x") double x, @RequestParam ("y") double y,
                                                      @RequestParam(name = "distance", required = false) Integer distance) {
        return new DataGatherer().getVets(x, y, distance);
    }

    @RequestMapping ("/get_bus_stops")
    public BusStopsHolder getBusStops(@RequestParam("my_x") double myX, @RequestParam ("my_y") double myY,
                                      @RequestParam(name = "dst_x") double destinationX, @RequestParam(name = "dst_y") double destinationY){
        return new DataGatherer().getBusStops(myX, myY, destinationX, destinationY);
    }

    @RequestMapping ("/get_park_lots")
    public List<GenericPositionAndNameObject> getParkLots(@RequestParam("x") double x, @RequestParam ("y") double y,
                                                          @RequestParam(name = "my_x") double myX, @RequestParam(name = "my_y") double myY){
        return new DataGatherer().getParkLots(x, y, myX, myY);
    }

    @RequestMapping ("/get_pet_stores")
    public List<GenericPositionAndNameObject> getPetStores(@RequestParam("x") double x, @RequestParam ("y") double y,
                                                           @RequestParam(name = "distance", required = false) Integer distance){
        return new DataGatherer().getPetStores(x, y, distance);
    }

    @RequestMapping ("/get_dog_parks")
    public List<GenericPositionAndNameObject> getDogParks(@RequestParam("x") double x, @RequestParam ("y") double y){
        return new DataGatherer().getDogParks(x, y);
    }

}
