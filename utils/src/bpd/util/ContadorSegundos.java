package bpd.util;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import bpd.util.observadores.ObservadorContadorSegundos;

/**
 * Contador que se incrementa automáticamente cada segundo a partir de ejecutar
 * <code>arrancaContador</code>. A cada segundo comunica al usuario la cantidad
 * de segundos pasados a través del método {@link #segundo(int)} que el usuario
 * tiene que implementar
 * 
 * @see #arrancaContador()
 * @see #paraContador()
 * @see #toString()
 * @see #formatea(long)
 * @see #segundosTranscurridos()
 *
 */
public class ContadorSegundos
{
	private Set< ObservadorContadorSegundos >	_Observadores	= new HashSet<>();

	private Timer								_Reloj;
	private int									_Segundos		= 0;

	public ContadorSegundos observadoPor( ObservadorContadorSegundos _observador )
		{
		_Observadores.add( _observador );
		return this;
		}

	/**
	 * Valor actual los segundos que han pasado desde que se arrancó el contador
	 * 
	 * @return
	 */
	public int segundosTranscurridos()
		{
		return _Segundos;
		}

	/**
	 * Representación en texto del contador, usando el formato de reloj, en
	 * donde las horas, minutos y segundos se separan por dos puntos
	 * 
	 * @see #formatea(long)
	 */
	@Override
	public String toString()
		{
		return formatea( _Segundos );
		}

	/**
	 * Pon el contador en marcha. A partir de aquí, cada segundo se notificará a
	 * los observadores de este contador el número de sgundos contados
	 * 
	 * @see #paraContador()
	 */
	public void arrancaContador()
		{
		_Segundos = 0;
		_Reloj = new Timer();
		_Reloj.scheduleAtFixedRate( cuentaSegundos(), 0, 1000L );
		}

	/**
	 * Para la cuenta de segundos y prepara el sistema para otro
	 * {@link #arrancaContador()}.
	 * 
	 * <p>
	 * El contador parado mantiene los segundos contados hasta ahora
	 * </p>
	 * 
	 * @see #arrancaContador()
	 */
	public void paraContador()
		{
		if( _Reloj != null )
			_Reloj.cancel();
		
		_Reloj = new Timer();
		}

	/**
	 * Utilidad para convertir un valor numérico (<code>_segundos</code>) en
	 * formato reloj, en donde las horas, minutos y segundos se separan mediante
	 * dos puntos
	 * 
	 * @param _segundos
	 * @return
	 */
	public static String formatea( long _segundos )
		{
		return reloj( _segundos );
		}

	public static String reloj( long _segundos )
		{
		long second = ( _segundos ) % 60;
		long minute = ( _segundos / ( 60 ) ) % 60;
		long hour = ( _segundos / ( 60 * 60 ) ) % 24;

		return String.format( "%02d:%02d:%02d", hour, minute, second );
		}

	/**
	 * Convierte en texto el valor de <code>_segundos</code> en formato de
	 * horas, minutos y segundos
	 * 
	 * <p>
	 * Ejemplo
	 * </p>
	 * 
	 * <pre>
	 * 3985 se convierte en: una hora, 6 minutos y 25 segundos
	 * </pre>
	 * 
	 * @param _segundos
	 * @return
	 */
	public static String aTexto( long _segundos )
		{
		long segundos = ( _segundos ) % 60;
		long minutos = ( _segundos / ( 60 ) ) % 60;
		long horas = ( _segundos / ( 60 * 60 ) ) % 24;

		return ConcatenadorDeTexto	.medianteEspacio()//
									.concatena( horas == 1, "una hora" )
									.concatena( horas > 1, horas + " horas" )
									.concatena( horas > 0 && minutos > 0 && segundos == 0, "y" )
									.concatena( horas > 0 && minutos > 0 && segundos > 0, "," )
									.concatena( minutos == 1, "un minuto" )
									.concatena( minutos > 1, minutos + " minutos" )
									.concatena( ( horas > 0 || minutos > 0 ) && segundos > 0, "y" )
									.concatena( segundos == 1, "un segundo" )
									.concatena( segundos > 1, segundos + " segundos" )
									.toString()
									.replace( " ,", "," );
		}

	private TimerTask cuentaSegundos()
		{
		return new TimerTask()
			{
				@Override
				public void run()
					{
					_Segundos++;
					_Observadores.forEach( e -> e.segundo( _Segundos ) );
					}
			};
		}
}
