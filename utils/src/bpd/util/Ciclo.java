package bpd.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Ciclo
{
	private Set< Runnable >					_Observadores	= new HashSet<>();

	private static ScheduledExecutorService	executor		= Executors.newScheduledThreadPool( 1 );
	private int								_TotalMilis		= 0;
	private int								_Contador		= 0;
	protected boolean						_EstaParado;

	public Ciclo( int _tiempoMilis )
		{
		_EstaParado = true;
		_TotalMilis = _tiempoMilis;
		executor.scheduleAtFixedRate( contador(), 0, 1, TimeUnit.MILLISECONDS );
		}

	public static String aTexto( Duration _duracion )
		{
		long s = _duracion.getSeconds();
		long d = s / ( 60 * 60 * 24 );
		s = s % ( 60 * 60 * 24 );
		long h = s / ( 60 * 60 );
		s = s % ( 60 * 60 );
		long m = s / 60;
		s = s % 60;

		int milis = _duracion.getNano() / 1000000;
		String segundos = String.format( "%d.%03d", s, milis );

		ConcatenadorDeTexto //
		ct = ConcatenadorDeTexto//
								.medianteEspacio()
								.concatena( d > 0, d + "d" )
								.concatena( h > 0, h + "h" )
								.concatena( m > 0, m + "m" )
								.concatena( s > 0 || milis > 0, segundos + "s" );

		return ct.toString();
		}

	public Ciclo observadoPor( Runnable _observador )
		{
		_Observadores.add( _observador );
		return this;
		}

	/**
	 * Pon contador a cero, y habilita el contador. Independientemente del
	 * estado en el que se encuentre la cuenta
	 * 
	 * @return
	 */
	public Ciclo arranca()
		{
		rearma();
		_EstaParado = false;
		return this;
		}

	/**
	 * Parada del contador hasta que se reanude la cuenta, o se rearme desde
	 * cero
	 * 
	 * @return
	 */
	public Ciclo para()
		{
		_EstaParado = true;
		return this;
		}

	/**
	 * Reanuda el contador
	 * 
	 * @return
	 */
	public Ciclo sigue()
		{
		_EstaParado = false;
		return this;
		}

	/**
	 * Empieza la cuenta desde el principio
	 * 
	 * @return
	 */
	public Ciclo rearma()
		{
		_Contador = _TotalMilis;
		return this;
		}

	/**
	 * Cambia el tiempo del ciclo, que se hará efectivo a partir del siguiente
	 * ciclo
	 * 
	 * @param _milisegundos
	 * @return
	 */
	public Ciclo nuevoTiempo( int _milisegundos )
		{
		_TotalMilis = _milisegundos;
		return this;
		}

	private Runnable contador()
		{
		return () -> //
		{
		cuenta();
		if( contadorHaDesbordado() )
			{
			_Observadores.forEach( Runnable::run );
			rearma();
			}
		};
		}

	private boolean contadorHaDesbordado()
		{
		return _Contador < 1;
		}

	private void cuenta()
		{
		if( _EstaParado )
			return;

		if( _Contador > 0 )
			_Contador--;
		}

	public static void main( String[] args )
		{
		Duration dur = Duration.ofMillis( 123456789 );
		System.out.println( aTexto( dur ) );

		new Ciclo( 1000 )//
							.observadoPor( () -> System.out.println( String.format( "Ciclo %s", LocalDateTime.now().toString() ) ) )
							.arranca();
		}

}
