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
    

	@RequestMapping(value = "/setcenter", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE )
	public @ResponseBody String setCenter( @RequestParam(value = "lat", required = true) int lat , @RequestParam(value = "lon", required = true) int lon ) {
		try {
			federateService.setCenter( lat, lon );
		} catch ( Exception e ) {
			return "NOT_STARTED";
		}
		return "ok";
	}
    
	@RequestMapping(value = "/setradius", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE )
	public @ResponseBody String setRadiusInMiles( @RequestParam(value = "radius", required = true) int radius ) {
		try {
			federateService.setRadiusInMiles( radius );
		} catch ( Exception e ) {
			return "NOT_STARTED";
		}
		return "ok";
	}
    
	@RequestMapping(value = "/settestmode", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE )
	public @ResponseBody String setTestMode( @RequestParam(value = "testmode", required = true) boolean testMode ) {
		try {
			federateService.setTestMode( testMode );
		} catch ( Exception e ) {
			return "NOT_STARTED";
		}
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
			SistramVessel aircraft = federateService.sendToRTI( identificador, lat, lon, alt, head, pitch, roll, veloc );
			return aircraft;
		} catch ( Exception e ) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}

