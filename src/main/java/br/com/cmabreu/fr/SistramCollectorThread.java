package br.com.cmabreu.fr;


import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.cmabreu.services.SistramVesselManager;


public class SistramCollectorThread implements Runnable {
    private boolean running;
	private Logger logger = LoggerFactory.getLogger( SistramCollectorThread.class );
	private SistramVesselManager manager;
	
	public void finish() {
		this.running = false;
	}
	
    public SistramCollectorThread( ) {
    	logger.info("Coletor Iniciado");
    	this.running = true;
    	this.manager = SistramVesselManager.getInstance();
    }  
    
    public void run() {

    	if( !this.running ) {
    		return;
    	}
    	
    	System.out.println("Coletando...");
    	
    	try {
    		String vessels = getVessels();
    		JSONArray obj = new JSONArray( vessels );
    		for( int x=0; x < obj.length(); x++  ) {
    			JSONObject vessel = obj.getJSONObject( x );
    			manager.updateVessel( vessel );
            }         		
    		
    		logger.info( vessels.length() + " aeronaves coletadas.");
    		
    	} catch( Exception se ) {
    		se.printStackTrace();
    		logger.error( se.getMessage() );
    	}
        	
        
        
    }  
    

	private String getVessels() {
		RestTemplate restTemplate = new RestTemplate();
		String responseBody;
		String url = "http://www.sistram.mar.mil.br/apolo/BuscaEmArea.php?long=-40.81010333333333&lat=-22.703833333333332&raio=1000";
		try {
			HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			
			
            ResponseEntity<String> result = restTemplate.exchange( url , HttpMethod.GET, entity, String.class);
			responseBody = result.getBody().toString();
		
			
		} catch (HttpClientErrorException e) {
		    responseBody = e.getResponseBodyAsString();
		}	
		return responseBody;
	}
	

		
  
}
