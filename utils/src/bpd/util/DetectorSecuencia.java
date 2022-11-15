package bpd.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DetectorSecuencia
{
	private Set< Runnable >	_Observadores		= new HashSet<>();

	private List< Object >	_Secuencia			= new ArrayList<>();
	private int				_SiguienteAProbar	= 0;

	public DetectorSecuencia()
		{
		_Secuencia = new ArrayList<>();
		}

	public DetectorSecuencia( List< Object > _secuencia )
		{
		_Secuencia = _secuencia;
		}

	public DetectorSecuencia con( Object _elemSecuencia )
		{
		_Secuencia.add( _elemSecuencia );
		return this;
		}

	/**
	 * Comprueba si con el <code>_objeto</code> se forma la secuencia esperada.
	 * En tal caso, lanza <code>secuenciaDetectada()</code>
	 * 
	 * @param _objeto
	 *            Objeto a añadir a la secuencia ya recibida
	 */
	public void detecta( Object _objeto )
		{
		if( _Secuencia.get( _SiguienteAProbar ).equals( _objeto ) )
			{
			_SiguienteAProbar++;
			if( _SiguienteAProbar == _Secuencia.size() )
				{
				rearma();
				secuenciaDetectada();
				_Observadores.forEach( Runnable::run );
				}

			return;
			}

		rearma();
		if( _Secuencia.get( _SiguienteAProbar ).equals( _objeto ) )
			{
			_SiguienteAProbar++;

			if( _SiguienteAProbar == _Secuencia.size() )
				{
				rearma();
				secuenciaDetectada();
				_Observadores.forEach( Runnable::run );
				}

			}
		}

	public void rearma()
		{
		_SiguienteAProbar = 0;
		}

	public DetectorSecuencia observadoPor( Runnable _observador )
		{
		_Observadores.add( _observador );
		return this;
		}

	public void secuenciaDetectada()
		{
		//
		}

	public static void main( String[] _params )
		{
		DetectorSecuencia x = new DetectorSecuencia()
														//
														.con( "h" )
														.con( "o" )
														.con( "l" )
														.con( "a" )
														.observadoPor( () -> System.out.println( "Secuencia detectada" ) );

		x.detecta( "a" );
		x.detecta( "a" );
		x.detecta( "a" );
		x.detecta( "h" );
		x.detecta( "o" );
		x.detecta( "h" );
		x.detecta( "o" );
		x.detecta( "l" );
		x.detecta( "a" );
		x.detecta( "a" );
		x.detecta( "a" );
		x.detecta( "a" );
		x.detecta( "a" );
		x.detecta( "a" );
		x.detecta( "a" );
		}
}
