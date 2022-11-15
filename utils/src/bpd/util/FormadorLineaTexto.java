package bpd.util;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Colecciona datos uno a uno hasta alcanzar el fin de línea. Añade la línea al
 * bote de líneas para ser leída.<br/>
 * Se define un tiempo máximo tras el cual se asume que la línea está formada.
 * De ser así el caso, el siguiente fin de línea que se reciba no se tendría en
 * cuenta
 * 
 */
public class FormadorLineaTexto

{
	/**
	 * Lista de todos los renglones que han sido formados, pero todavía no
	 * leídos por el cliente
	 */
	private Queue< String >	_LineasFormadasPendientesDeLeer;
	/**
	 * Datos recibidos que todavía no alcanzan a formar una línea
	 */
	private StringBuilder	_LineaEnFormacion;
	private String			_IdentificadorFinLinea	= "\r\n";

	/**
	 * Vigilante de formación. Con este temporizador se decide que el renglón se
	 * da por formado si en un tiempo no se ha recibido dato alguno, aunque no
	 * se haya recibido la marca de fin de línea
	 */
	private Ciclo			_TemporizadorFinLin;

	/**
	 * Longitud máxima que se admite para una línea. A partir de esta longitud
	 * se supone que algo no funciona (que no se está empleando el código de
	 * caracteres adecuado, y no se detectan los fines de línea). Al alcanzar
	 * esta longitud se pasa a la lista de líneas formadas
	 */
	private int				_MaximaLongitud;

	/**
	 * Genera un formador de línea con los valores predeterminados para el fin
	 * de línea (ASCII CRLF), y un temporizador fin de línea de tres segundos
	 */
	public FormadorLineaTexto()
		{
		this( "\r\n", 200, 256 );
		}

	/**
	 * Genera un formador de línea con los valores para el fin de línea
	 * <code>_id_finLinea</code>, y un temporizador fin de línea de
	 * <code>_tiempoEsperaFinLinea_ms</code> milisegundos
	 * 
	 * @param _id_finLinea
	 *            El identificador de fin de línea
	 * @param _tiempoEsperaFinLinea_ms
	 *            El tiempo de fin de línea. Al cabo de este tiempo, aunque no
	 *            se haya recibido el fin de línea, se trataría lo recibido como
	 *            una línea completa. Como si sï¿½ se recibiera el fin de línea
	 * @param _maximaLongitud
	 */
	public FormadorLineaTexto( String _id_finLinea, int _tiempoEsperaFinLinea_ms, int _maximaLongitud )
		{
		_TemporizadorFinLin = new Ciclo( _tiempoEsperaFinLinea_ms ).observadoPor( this::pasaLineaAPendientesDeLeer );

		_IdentificadorFinLinea = _id_finLinea;
		_LineaEnFormacion = new StringBuilder();
		_LineasFormadasPendientesDeLeer = new LinkedList<>();
		tiempoFinLinea( _tiempoEsperaFinLinea_ms );

		_MaximaLongitud = _maximaLongitud;
		}

	public boolean estaVacio()
		{
		return _LineaEnFormacion.length() < 1;
		}

	/**
	 * Recibe el dato e incluye en el renglón en formación. Si el dato es la
	 * marca de fin de línea, se da el renglón por formado y se pasa a la lista
	 * de pendientes de leer por el usuario
	 * 
	 * @param _dato
	 */
	public void elabora( byte _dato )
		{
		_TemporizadorFinLin.arranca();
		incluyeEnRenglonEnFormacion( _dato );
		if( haAlcanzadoFinLinea() )
			pasaLineaAPendientesDeLeer();
		}

	public FormadorLineaTexto elabora( byte[] _datos )
		{
		for( byte d: _datos )
			elabora( d );

		return this;
		}

	/**
	 * Comprueba si hay textos pendientes de ser leídos por el usuario
	 * 
	 * @return <code>true</code>, si hay textos
	 */
	public boolean hayTextos()
		{
		return !_LineasFormadasPendientesDeLeer.isEmpty();
		}

	public FormadorLineaTexto rearma()
		{
		_LineaEnFormacion.delete( 0, _LineaEnFormacion.length() );
		_LineasFormadasPendientesDeLeer.clear();
		_TemporizadorFinLin.rearma();

		return this;
		}

	/**
	 * Cambia el identificador de fin de línea al
	 * <code>_nuevoIdentificador</code>,
	 * 
	 * @param _nuevoIdentificador
	 * @return
	 */
	public FormadorLineaTexto identificadorFinLinea( String _nuevoIdentificador )
		{
		if( _nuevoIdentificador == null )
			return this;

		_IdentificadorFinLinea = _nuevoIdentificador;
		return this;
		}

	/**
	 * Secuencia que determina cuándo se alcaza el fin de la línea de texto
	 * 
	 * @return El texto que define el fin de línea
	 */
	public String identificadorFinLinea()
		{
		return _IdentificadorFinLinea;
		}

	/**
	 * Cambia el tiempo de espera a <code>_milisegundos</code>. Cualquier tiempo
	 * anteriormente establecido
	 * 
	 * @param _milisegundos
	 * @return
	 */
	public FormadorLineaTexto tiempoFinLinea( int _milisegundos )
		{
		_TemporizadorFinLin.nuevoTiempo( _milisegundos );
		return this;
		}

	public FormadorLineaTexto maximaLongitud( int _numeroCaracteres )
		{
		if( _numeroCaracteres > 0 )
			_MaximaLongitud = _numeroCaracteres;

		return this;
		}

	/**
	 * Lee el siguiente renglón de la lista de pendientes de leer. Tras la
	 * lectura, el renglón desaparece de la lista de pendientes
	 * 
	 * @return El siguiente renglón de la lista de pendientes, o
	 *         <code>null</code>, si la lista está vacía
	 */
	public String texto()
		{
		return _LineasFormadasPendientesDeLeer.poll();
		}

	private void pasaLineaAPendientesDeLeer()
		{
		_TemporizadorFinLin.para();

		/*
		 * Protección contra exceso de consumo de memoria por acumulación de
		 * líneas si nadie las lee (aleatoriamente, límite = 1000)
		 */
		if( _LineasFormadasPendientesDeLeer.size() > 1000 )
			texto();

		_LineasFormadasPendientesDeLeer.add( _LineaEnFormacion.toString() );
		_LineaEnFormacion.delete( 0, _LineaEnFormacion.length() );
		}

	private void incluyeEnRenglonEnFormacion( byte _dato )
		{
		_LineaEnFormacion.append( new String( new byte[] { _dato } ) );
		}

	/**
	 * Ha alcanzado el fin de línea cuando se detecta la secuencia de fin de
	 * línea al final de la línea en formación, o cuando la línea en formación
	 * supera el límite máximo de caracteres.
	 * 
	 * @return
	 */
	private boolean haAlcanzadoFinLinea()
		{
		return _LineaEnFormacion.toString().endsWith( _IdentificadorFinLinea ) || _LineaEnFormacion.length() >= _MaximaLongitud;
		}

	public static void main( String[] args )
		{
		FormadorLineaTexto flt = new FormadorLineaTexto();
		flt.elabora( ( byte )0x32 );
		flt.elabora( ( byte )0x32 );
		flt.elabora( ( byte )0x33 );
		while( !flt.hayTextos() )
			Thread.yield();

		System.out.println( "texto >> " + flt.texto() );

		flt.tiempoFinLinea( 60000 );
		flt.elabora( ( byte )0x33 );
		flt.elabora( ( byte )0x34 );
		flt.elabora( ( byte )0x0d );
		flt.elabora( ( byte )0x0a );
		while( true )
			{
			Thread.yield();
			if( flt.hayTextos() )
				break;
			}
		System.out.println( flt.texto() );

		while( !flt.hayTextos() )
			Thread.yield();

		System.out.println( "Fin." );
		}
}
