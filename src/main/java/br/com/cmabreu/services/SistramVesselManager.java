package br.com.cmabreu.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.cmabreu.models.SistramVessel;
import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.RTIambassador;

public class SistramVesselManager {
	private RTIambassador rtiAmb;
	
	private InteractionClassHandle interactionHandle;   

	// caches of handle types - set once we join a federation
	private AttributeHandleSet attributes;
	private ObjectClassHandle entityHandle;
	private AttributeHandle entityTypeHandle;
	private AttributeHandle spatialHandle;
	private AttributeHandle forceIdentifierHandle;
	private AttributeHandle markingHandle;	
	private AttributeHandle isConcealedHandle;
	private AttributeHandle entityIdentifierHandle;
	private AttributeHandle damageStateHandle;
	private static SistramVesselManager instance;
	private List<SistramVessel> navios;
	
	private SistramVessel exists( String id ) {
		for( SistramVessel ac : this.navios ) {
			if( ac.getIdentificador().equals(id) ) return ac;
		}
		return null;
	}
	
	private Logger logger = LoggerFactory.getLogger( SistramVesselManager.class );
	
	public static SistramVesselManager getInstance() {
		return instance;
	}
	
	public static void startInstance( RTIambassador rtiAmb ) throws Exception {
		instance = new SistramVesselManager( rtiAmb );
	}
	
	private SistramVesselManager( RTIambassador rtiAmb ) throws Exception {
		this.navios = new ArrayList<SistramVessel>();
		logger.info("Gerenciador SISTRAM ativo");
		this.rtiAmb = rtiAmb;
		this.publish();
	}
	
	private void publish() throws Exception {
		// get all the handle information for the attributes
		logger.info("Objetos publicados.");
		this.entityHandle = this.rtiAmb.getObjectClassHandle("HLAobjectRoot.BaseEntity.PhysicalEntity.Platform.SurfaceVessel");
		this.entityTypeHandle = this.rtiAmb.getAttributeHandle(entityHandle, "EntityType");
		this.spatialHandle = this.rtiAmb.getAttributeHandle(entityHandle, "Spatial");
		this.forceIdentifierHandle = this.rtiAmb.getAttributeHandle(entityHandle, "ForceIdentifier");
		this.markingHandle = this.rtiAmb.getAttributeHandle(entityHandle, "Marking");
		this.isConcealedHandle = this.rtiAmb.getAttributeHandle(entityHandle, "IsConcealed");
		this.entityIdentifierHandle = this.rtiAmb.getAttributeHandle(entityHandle, "EntityIdentifier");
		this.damageStateHandle = this.rtiAmb.getAttributeHandle(entityHandle, "DamageState");
		
		// package the information into a handle set
		attributes = this.rtiAmb.getAttributeHandleSetFactory().create();
		attributes.add(entityTypeHandle);
		attributes.add(spatialHandle);
		attributes.add(forceIdentifierHandle);
		attributes.add(markingHandle);
		attributes.add(isConcealedHandle);
		attributes.add(entityIdentifierHandle);
		attributes.add(damageStateHandle);
        this.rtiAmb.publishObjectClassAttributes( this.entityHandle, attributes );   
        
        this.interactionHandle = this.rtiAmb.getInteractionClassHandle("Acknowledge");
        this.rtiAmb.publishInteractionClass(interactionHandle);
	}

	/* GETTERS e SETTERS */
	
	public RTIambassador getRtiAmb() {
		return rtiAmb;
	}

	public AttributeHandle getEntityIdentifierHandle() {
		return entityIdentifierHandle;
	}

	public InteractionClassHandle getInteractionHandle() {
		return interactionHandle;
	}

	public ObjectClassHandle getEntityHandle() {
		return entityHandle;
	}

	public AttributeHandle getEntityTypeHandle() {
		return entityTypeHandle;
	}

	public AttributeHandle getSpatialHandle() {
		return spatialHandle;
	}

	public AttributeHandle getForceIdentifierHandle() {
		return forceIdentifierHandle;
	}

	public AttributeHandle getMarkingHandle() {
		return markingHandle;
	}

	public AttributeHandle getIsConcealedHandle() {
		return isConcealedHandle;
	}

	public AttributeHandle getDamageStateHandle() {
		return damageStateHandle;
	}

	public SistramVessel spawn( String identificador ) throws Exception {
		return null;
	}
	
	public void update( List<SistramVessel> aircrafts ) throws Exception {
		for( SistramVessel ac : aircrafts  ) {
			ac.sendSpatialVariant();
		}
	}

	public SistramVessel update(String identificador, float lat, float lon, float alt, float head, float pitch, float roll, float veloc) throws Exception {
		
    	// Verifica se já tenho esta aeronave
    	SistramVessel ac = this.exists( identificador );
    	if( ac == null ) {
    		// se não tiver eu crio na minha lista
    		ac = new SistramVessel( this, identificador );
    		this.navios.add( ac );
    	} else {
    		//
    	}

		// Preenche os atributos da aeronave com os dados do FlightRadar24
		// O numero do voo identifica unicamente uma aeronave
		// Envia as atualizacoes para a RTI
		ac.setAltitude( alt );
		ac.setLongitude( lon );
		ac.setLatitude( lat );
		ac.setVelocityX( veloc );
		ac.setOrientationPhi( head );
		
		// Manda a atualizacao para a RTI
		ac.sendSpatialVariant();		
		
		return ac;
	}
	
	public void updateVessel( JSONObject vesselJsonData ) throws Exception {
		/*
		  {
		    "id_origem_dados_pontos": "1590814415",
		    "lon": "-38.9857383333333",
		    "lat": "-23.3274733333333",
		    "rumo": "0",
		    "veloc": "0",
		    "irin": "",
		    "mmsi": "993116063",
		    "dh": "2020-04-17 01:40:49-03",
		    "imo": "0",
		    "fonte": "AIS-S",
		    "id_origem_dados": "29",
		    "nome_navio": ""
		  },    	
		*/
		String identificador = vesselJsonData.getString("mmsi");
		Float lat = Float.valueOf( vesselJsonData.getString("lat") );
		Float lon = Float.valueOf( vesselJsonData.getString("lon") );
		Float heading = Float.valueOf( vesselJsonData.getString("rumo") );
		Float veloc = Float.valueOf( vesselJsonData.getString("veloc") );
		String nome = vesselJsonData.getString("nome_navio");
    	this.update(identificador + "." + nome, lat, lon, 0, heading, 0, 0, veloc);
	}
	
}
