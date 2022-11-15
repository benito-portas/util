package bpd.util.pruebas;

import bpd.util.ContadorSegundos;

public class PruebasContadorSegundos
{
	public static void main( String[] args )
		{
		prueba( 0 * 60 * 60 + 0 * 60 + 1, "un segundo" );
		prueba( 0 * 60 * 60 + 0 * 60 + 3, "3 segundos" );
		prueba( 0 * 60 * 60 + 0 * 60 + 59, "59 segundos" );
		prueba( 0 * 60 * 60 + 1 * 60 + 0, "un minuto" );
		prueba( 0 * 60 * 60 + 3 * 60 + 0, "3 minutos" );
		prueba( 0 * 60 * 60 + 3 * 60 + 5, "3 minutos y 5 segundos" );
		prueba( 1 * 60 * 60 + 0 * 60 + 0, "una hora" );
		prueba( 1 * 60 * 60 + 7 * 60 + 0, "una hora y 7 minutos" );
		prueba( 1 * 60 * 60 + 6 * 60 + 25, "una hora, 6 minutos y 25 segundos" );
		prueba( 1 * 60 * 60 + 0 * 60 + 25, "una hora y 25 segundos" );
		prueba( 3 * 60 * 60 + 0 * 60 + 0, "3 horas" );
		}

	public static void prueba( int _nroSegundos, String _resultadoEsperado )
		{
		String resultadoReal = ContadorSegundos.aTexto( _nroSegundos );
		boolean eo = resultadoReal.equals( _resultadoEsperado );
		if( eo )
			System.out.println( String.format( "EO con %d ==> %s", _nroSegundos, _resultadoEsperado ) );
		else
			System.out.println( String.format( "ERROR con %d ==> esperado (%s), real (%s)", _nroSegundos, _resultadoEsperado, resultadoReal ) );
		}

}
