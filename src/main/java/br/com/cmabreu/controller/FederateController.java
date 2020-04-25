package br.com.cmabreu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.cmabreu.models.SistramVessel;
import br.com.cmabreu.services.FederateService;

@RestController
public class FederateController {
	
    @Autowired
    private FederateService federateService;	
	

    // In original code the Federation was destroyed after the main loop.
    // Now we must destroy it by calling this endpoint
    @RequestMapping(value = "/quit", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE )
	public @ResponseBody String quit() {
    	federateService.quit();
    	return "ok";
	}
	

    @RequestMapping(value = "/start", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE )
	public @ResponseBody String startCollector() {
    	federateService.startCollector();
    	return "ok";
	}
    
    
	@RequestMapping(value = "/deleteobjectinstance", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE )
	public @ResponseBody String deleteObjectInstance( @RequestParam(value = "handle", required = true) Integer objectInstanceHandle ) {
		try {
			federateService.deleteObjectInstance( objectInstanceHandle );
		} catch ( Exception e ) {
			//
		}
		return "ok";
	}
	
	
	@RequestMapping(value = "/spawn", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE )
	public @ResponseBody SistramVessel spawn( @RequestParam(value = "identificador", required = true) String identificador ) {
		try {
			SistramVessel vessel = federateService.spawn( identificador );
			return vessel;
		} catch ( Exception e ) {
			e.printStackTrace();
			return null;
		}
	}


	@RequestMapping(value = "/update", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE )
	public @ResponseBody SistramVessel update( @RequestParam(value = "identificador", required = true) String identificador,
			@RequestParam(value = "lat", required = true) float lat,
			@RequestParam(value = "lon", required = true) float lon, 
			@RequestParam(value = "alt", required = true) float alt,
			@RequestParam(value = "head", required = true) float head, 
			@RequestParam(value = "pitch", required = true) float pitch,
			@RequestParam(value = "roll", required = true) float roll,
			@RequestParam(value = "veloc", required = true) float veloc) {
		try {
			SistramVessel aircraft = federateService.update( identificador, lat, lon, alt, head, pitch, roll, veloc );
			return aircraft;
		} catch ( Exception e ) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}

