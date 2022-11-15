package bpd.util;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * Temporizador definido por el tiempo de dis
 * 
 * 
 * <h1>Definición</h1> Genera un <code>eventoEstado( true )</code> por un tiempo
 * de duración de pulso. Al final del pulso, genera otro evento
 * <code>eventoEstado( false )</code>. Opcionalmente se puede retrasar el primer
 * evento mediante un <i>tiempo de retraso de disparo</i> <br/>
 * <h2>Creación</h2>
 * <li>Pulso( int _duracionPulso )</li>
 * <li>Pulso( int _duracionPulso, int _retardoDisparo )</li>
 * <h2>Manejo</h2>
 * <li>dispara(). Desencadena el disparo del pulso</li>
 * <li>eventoEstado( boolean _estaEncendido ). Función que se genera cada vez
 * que el pulso cambia de estado</li>
 * <h1>Ejemplo</h1>
 * 
 * <pre>
 * Pulso pulso = new Pulso( 100, 5000 ).observadoPor( 
 * 	{
 * 		public void eventoEstado( boolean _estaEncendido )
 * 			{
 * 			System.out.println( ( _estaEncendido ? &quot;encendido- &quot; : &quot;apagado-   &quot; ) + new Date().getTime() );
 * 			}
 * 	};
 * pulso.dispara();
 * </pre>
 * 
 * Con cada cambio de estado, el pulso genera un <code>eventoEstado</code>.
 * Cuando se dispara, al pasar el tiempo de retraso del disparo, o
 * inmediatamente si no se especificó retraso, se genera
 * <code>eventoEstado( true )</code>, y al finalizar la duración del pulso, un
 * <code>eventoEstado( false )</code>
 */
public class Pulso
{
	/**
	 * Tiempo en milisegundos que se espera desde que se dispara el pulso, hasta
	 * que el pulso se enciende
	 */
	private int							_RetrasoDisparo			= 0;

	/**
	 * Tiempo en milisegundos que el pulso permanece encendido
	 */
	private int							_DuracionPulso			= 0;

	/**
	 * Temporizador que controla el retraso del disparo
	 */
	private Timer						_TemporizadorRetraso	= new Timer();

	/**
	 * Temporizador que controla la duración del pulso
	 */
	private Timer						_TemporizadorPulso		= new Timer();

	/**
	 * Cantidad de pulsos adicionales que se disparan automáticamente, tras el
	 * primer pulso
	 */
	private int							_NumeroRepeticiones		= 0;

	/**
	 * Variable de trabajo que cuenta las repeticiones
	 */
	private int							_NumeroRepeticion		= 0;
	private boolean						_EstaHabilitado			= true;

	private Set< Consumer< Boolean > >	_Observadores			= new HashSet<>();

	/**
	 * Creación de un pulso con una <code>_duracionPulso</code>, y sin retraso
	 * cuando se dispare
	 * 
	 * @param _duracionPulso
	 *            Tiempo en milisegundos
	 */
	public Pulso( int _duracionPulso )
		{
		super();
		_DuracionPulso = _duracionPulso;
		}

	/**
	 * Creación de un pulso con una <code>_duracionPulso</code>, y un
	 * <code>_retrasoDisparo</code>
	 * 
	 * @param _duracionPulso
	 *            Duración en milisegundos
	 * @param _retrasoDisparo
	 *            Retraso en milisegundos
	 */
	public Pulso( int _duracionPulso, int _retrasoDisparo )
		{
		this( _duracionPulso );
		_RetrasoDisparo = _retrasoDisparo;
		}

	/**
	 * Desencadena el funcionamiento del pulso. Si se dispara antes de la
	 * terminación de un disparo anterior, éste no se llega a completar. El
	 * tiempo se inicia de nuevo sin ejecutar las tareas programadas para el
	 * pulso.
	 * 
	 * @return
	 * 
	 */
	public Pulso dispara()
		{
		_TemporizadorPulso.cancel();
		_TemporizadorRetraso.cancel();
		_NumeroRepeticion = _NumeroRepeticiones;
		_EstaHabilitado = true;
		disparaRepeticion();
		return this;
		}

	public Pulso duracionPulso( int _milisegundos )
		{
		_DuracionPulso = _milisegundos;
		return this;
		}

	/**
	 * Tiempo en milisegundos que tiene que pasar antes de activar el pulso tras
	 * haber enviado el disparo.
	 * 
	 * @param _milisegundos
	 * @return
	 */
	public Pulso retrasoDisparo( int _milisegundos )
		{
		_RetrasoDisparo = _milisegundos;
		return this;
		}

	/**
	 * 
	 * @param _numeroRepeticiones
	 * @return
	 */
	public Pulso repeticiones( int _numeroRepeticiones )
		{
		return numeroRepeticiones( _numeroRepeticiones );
		}

	public Pulso numeroRepeticiones( int _numeroRepeticiones )
		{
		_NumeroRepeticiones = _numeroRepeticiones;
		return this;
		}

	public Pulso inhibe()
		{
		_EstaHabilitado = false;
		return this;
		}

	public Pulso cierra()
		{
		inhibe();
		_TemporizadorPulso.cancel();
		_TemporizadorRetraso.cancel();
		return this;
		}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
		{
		return "Pulso." + ( _EstaHabilitado ? "" : "in" ) + "habilitado( " + _RetrasoDisparo + ":" + _DuracionPulso + " )[ " + _NumeroRepeticion + ":" + _NumeroRepeticiones + " ]";
		}

	private void disparaPulso()
		{
		cambiaEstado( true );
		_TemporizadorPulso = new Timer();
		_TemporizadorPulso.schedule( new TimerTask()
			{
				@Override
				public void run()
					{
					finDelPulso();
					_TemporizadorPulso.cancel();
					}
			}, _DuracionPulso );
		}

	public Pulso observadoPor( Consumer< Boolean > _observadores )
		{
		_Observadores.add( _observadores );
		return this;
		}

	private void cambiaEstado( boolean _estaEncendido )
		{
		if( _EstaHabilitado )
			_Observadores.forEach( c -> c.accept( _estaEncendido ) );
		}

	private void finDelPulso()
		{
		cambiaEstado( false );
		if( _NumeroRepeticiones > 0 )
			{
			if( _NumeroRepeticion > 0 )
				{
				_NumeroRepeticion--;
				disparaRepeticion();
				}
			}
		}

	private void disparaRepeticion()
		{
		_TemporizadorRetraso = new Timer();
		_TemporizadorRetraso.schedule( new TimerTask()
			{
				@Override
				public void run()
					{
					disparaPulso();
					_TemporizadorRetraso.cancel();
					}
			}, _RetrasoDisparo );
		}

	/**
	 * @param args
	 */
	public static void main( String[] args )
		{
		new Pulso( 1000 )//

							.numeroRepeticiones( 3 )
							.observadoPor( c -> System.out.println( LocalDateTime.now().toString() + ": " + c ) )
							.dispara();

		new Pulso( 5000, 1000 )//

								.numeroRepeticiones( 0 )
								.observadoPor( c -> System.out.println( LocalDateTime.now().toString() + ": " + c ) )
								.dispara();

		}

}