package br.com.cmabreu;

public class TestaOndas {
	private static int MAX_BUFFER = 151;
	private static double freq = 10;
	private static double waveSpeed = 100.0;
	private static double amp = 1.8;
	private static double damping = 0.1;
	private static double[] viby = new double[MAX_BUFFER];
	private static double[] sum1 = new double[MAX_BUFFER];
	private static double[] sum2 = new double[MAX_BUFFER];
	private static double[] kn = new double[21];
	
	private static void calculate( double TT ){
		double angle = 2.0 * Math.PI * freq * TT;       
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		
	    for ( int i = 0; i < MAX_BUFFER; i++ ) {       
	       	viby[i] = ( (sum1[i] + amp * (1.0-i/150.0)) * cos + sum2[i] * sin); 
	    }
	}
	
	
	private static void calculateSum1Sum2(){
		double k = 2.0 * Math.PI * freq / waveSpeed; 
		double a = 2.0 * amp / Math.PI;
		for (int i = 0; i < MAX_BUFFER; i++){
			double z = Math.PI * i / 150.0;
		    sum1[i] = 0.0;
	        sum2[i] = 0.0;
            for ( int j = 1; j < 21; j++ ){
            	double cn = kn[j] * kn[j] -k * k;
            	double bn = Math.sin( j * z ) / j / ( cn * cn + k * k * damping * damping ); 	
            	 sum1[i] += bn * ( cn - damping * damping );
            	 sum2[i] += bn * kn[j] * kn[j];            		
            }
	        sum1[i] = a * k * k * sum1[i];
	        sum2[i] = a * k * sum2[i] * damping;
	    }
	}	
	
	public static void main(String[] args) {
		calculateSum1Sum2();
		
		double TT = 0.0;
		for( int x = 0; x < MAX_BUFFER; x++ ) {
			calculate( TT );
			System.out.println( viby[0] );
			TT = TT + 0.001;
		}
		
		

	}

}
