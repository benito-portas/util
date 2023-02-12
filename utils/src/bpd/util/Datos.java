package bpd.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Datos
{
	private Datos()
		{
		}

	/**
	 * Genera una estructura del tipo <code>byte[]</code> partiendo de una
	 * expresión de texto
	 * <p>
	 * <ul>
	 * <li>El texto puede contener separadores</li>
	 * <li>Separador se entiende lo que no es un d�gito hexadecimal (0-9, a-f,
	 * A-F)
	 * <li>Los separadores se eliminan antes de la conversión</li>
	 * <li>El número de cifras en el texto tiene que ser par</li>
	 * <li>El texto se procesa de izquierda a derecha. El resultado se plasma
	 * tambión de izquierda a derecha</li>
	 * 
	 * </p
	 * 
	 * <p>
	 * Ejemplo
	 * 
	 * <pre>
	 * "01.3a.1B" se convierte en:
	 * {1,58,27}
	 * 
	 * igual que: "013a1B"
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @see #deTexto(String)
	 * @param _hex
	 *            Texto con d�gitos hexadecimales, y signos de separación entre
	 *            ellos
	 * @return
	 */
	public static byte[] aDatos( String _hex )
		{
		// Elimina separaciones entre los números
		_hex = _hex.replaceAll( "[^\\da-fA-F]", "" );

		/*
		 * Hago la longitud par para que pueda contar m�s adelante de dos en dos
		 * sin salirme de la lista de números
		 */
		int len = _hex.length();
		if( len / 2 * 2 != len )
			{
			_hex = "0" + _hex;
			len += 1;
			}

		byte[] data = new byte[ len / 2 ];
		for( int i = 0; i < len; i += 2 )
			data[ i / 2 ] = ( byte )( ( Character.digit( _hex.charAt( i ), 16 ) << 4 ) + Character.digit( _hex.charAt( i + 1 ), 16 ) );

		return data;
		}

	/**
	 * @see #aDatos(String)
	 * @param _hex
	 * @return
	 */
	public static byte[] deTexto( String _hex )
		{
		return aDatos( _hex );
		}

	/**
	 * El orden de grabación es de menor significancia a mayor (BIG ENDIAN)
	 * 
	 * @param _valor
	 * @param _cantidadOctetos
	 * @return
	 */
	public static byte[] aDatos( long _valor, int _cantidadOctetos )
		{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		for( int i = 0; i < _cantidadOctetos; i++ )
			os.write( ( int )( _valor >> 8 * i ) & 0xFF );

		return os.toByteArray();
		}

	/**
	 * Resulta en la lista de los octetos en <code>_datos</code> con el orden
	 * revertido
	 * <p>
	 * Ejemplo
	 * </p>
	 * 
	 * <pre>
	 * Util.revertido( {1,2,3,4,5} )
	 * resulta en: {5,4,3,2,1}
	 * </pre>
	 * 
	 * @param _datos
	 * @return
	 */
	public static byte[] revertido( byte[] _datos )
		{
		int longitudDatos = _datos.length;

		byte[] revertido = new byte[ longitudDatos ];
		int pos = longitudDatos - 1;
		while( pos >= 0 )
			{
			revertido[ pos ] = _datos[ longitudDatos - 1 - pos ];
			pos--;
			}

		return revertido;
		}

	/**
	 * Ampl�a <code>_datos</code> a la <code>_longitud</code> con el
	 * <code>_valorRelleno</code> a�adido a la derecha
	 * 
	 * 
	 * @param _datos
	 * @param _longitud
	 * @param valorRelleno
	 * @return
	 * 
	 * @throws IOException
	 */
	public static byte[] completa( byte[] _datos, int _longitud, byte _valorRelleno ) throws IOException
		{
		if( _datos.length >= _longitud )
			return _datos;

		byte[] apendice = new byte[ _longitud - _datos.length ];
		Arrays.fill( apendice, _valorRelleno );

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write( _datos );
		baos.write( apendice );

		return baos.toByteArray();
		}

	public static String aTexto( byte[] _datos )
		{
		return representacionOctetos( _datos );
		}

	/**
	 * Convierte el número <code>_numero</code> en texto con ceros de relleno a
	 * la izquierda para completar el <code>_cantidadDigitos</code>
	 * 
	 * @param _IdEvento
	 * @param i
	 * @return
	 */
	public static String aTexto( int _numero, int _cantidadDigitos )
		{
		String numero = "" + _numero;
		if( numero.length() >= _cantidadDigitos )
			return numero;

		StringBuilder sb = new StringBuilder();
		while( sb.length() < _cantidadDigitos - numero.length() )
			sb.append( "0" );

		return sb.toString() + numero;
		}

	private static String TABLA_HEX = "0123456789abcdef";

	/**
	 * Representa la secuencia <code>_datos/code> con c�digos hexadecimales.
	 * S�lo intervienen los datos a partir de <code>_posInicial</code>
	 *
	 * @param _datos
	 *            Datos a representar
	 * @param _posInicial
	 *            Posición en la secuencia a partir de la cual se representa
	 * @param _longitudAConvertir
	 *            Cantidad de posiciones a representar
	 * @return El texto
	 */
	public static String hex( byte[] _datos, int _posInicial, int _longitudAConvertir )
		{
		StringBuilder sb = new StringBuilder();
		byte x;

		for( int num = _posInicial; num < ( _posInicial + _longitudAConvertir ); num++ )
			{
			x = ( byte )( _datos[ num ] >> 4 );
			sb.append( hex( x ) );
			x = _datos[ num ];
			sb.append( hex( x ) );
			}

		return sb.toString();
		}

	/**
	 * Convierte a cifra hex (0..9,a..f) el <code>_dato</code>, valiendo s�lo
	 * los cuatro bit de menor peso
	 * 
	 * @param _dato
	 *            El dato
	 * @return La letra hex
	 */
	public static String hex( byte _dato )
		{
		int posicion = _dato & 0x0f;
		if( posicion < 1 || posicion >= TABLA_HEX.length() )
			return "";

		return "" + TABLA_HEX.charAt( posicion );

		}

	/**
	 * Toma s�lo los datos a partir de la <code>_posicionInicial</code>
	 * 
	 * @param _posicionInicial
	 * @param _datos
	 * 
	 * @return
	 */
	public static byte[] porcionAPartirDe( int _posicionInicial, byte[] _datos )
		{
		if( _posicionInicial < 1 )
			return _datos;

		final int cantidad = _datos.length - _posicionInicial;
		if( cantidad < 1 )
			return new byte[] {};

		byte[] resultado = new byte[ cantidad ];
		System.arraycopy( _datos, _posicionInicial, resultado, 0, cantidad );

		return resultado;
		}

	public static byte[] porcionHasta( int _posFinal, byte[] _datos )
		{
		return porcion( _datos, 0, _posFinal );
		}

	/**
	 * Porción de <code>_datos</code> entre las posiciones
	 * <code>_posInicial</code> y <code>_posFinal</code> (�sta excluida)
	 * 
	 * <p>
	 * Si la posición inicial es inferior a cero, se toma la posición cero. Si
	 * la posición final es superior al límite superior de <code>_datos</code>,
	 * se toma la posición final de <code>_datos</code>.
	 * </p>
	 * 
	 * <p>
	 * Si la cantidad de datos resultante es inferior a uno, el resultado es una
	 * lista de datos vacía. Si la cantidad resultante es superior a la cantidad
	 * de datos en <code>_datos</code>, el resultado es <code>_datos</code>.
	 * </p>
	 * 
	 * @param _datos
	 * @param _posInicial
	 * @param _posFinal
	 * @return La porción indicada, una lista vacía, o todos los datos
	 * 
	 */
	public static byte[] porcion( byte[] _datos, int _posInicial, int _posFinal )
		{
		if( _posFinal >= _datos.length )
			return porcionAPartirDe( _posInicial, _datos );

		if( _posInicial < 1 )
			_posInicial = 0;

		int cantidad = _posFinal - _posInicial;
		if( cantidad < 1 )
			return new byte[] {};

		byte[] resultado = new byte[ cantidad ];
		System.arraycopy( _datos, _posInicial, resultado, 0, resultado.length );

		return resultado;
		}

	public static int octeto( int _inicio, byte[] _datos )
		{
		byte octeto = _datos[ _inicio ];
		return ( octeto < 0 ) ? ( octeto + 256 ) : ( int )octeto;
		}

	public static int octetoDoble( int _inicio, byte[] _datos )
		{
		return ( octeto( _inicio, _datos ) << 8 ) | octeto( _inicio + 1, _datos );
		}

	public static int octetoDobleAlReves( int _inicio, byte[] _datos )
		{
		return ( octeto( _inicio + 1, _datos ) << 8 ) | octeto( _inicio, _datos );
		}

	public static long octetoCuadruple( int _inicio, byte[] _datos )
		{
		return ( ( long )octeto( _inicio, _datos ) << 24 ) | ( ( long )octeto( _inicio + 1, _datos ) << 16 ) | ( ( long )octeto( _inicio + 2, _datos ) << 8 ) | octeto( _inicio + 3, _datos );
		}

	public static long octetoCuadrupleAlReves( int _inicio, byte[] _datos )
		{
		return ( ( long )octeto( _inicio + 3, _datos ) << 24 ) | ( ( long )octeto( _inicio + 2, _datos ) << 16 ) | ( ( long )octeto( _inicio + 1, _datos ) << 8 ) | octeto( _inicio, _datos );
		}

	public static String representacionOcteto( byte _octeto )
		{
		String nro = Integer.toHexString( _octeto & 0xff );

		if( ( _octeto & 0xff ) < 16 )
			return "0" + nro;

		return nro;
		}

	public static String representacionBinaria( byte[] _octetos )
		{
		return representacionOctetos( _octetos, 0, _octetos.length, "." );
		}

	public static String representacionBinaria( byte[] _octetos, int _inicio, int _longitud, String _separador )
		{
		if( _inicio < 0 )
			return "";

		if( _longitud < 0 )
			return "";

		StringBuilder sb = new StringBuilder();
		int fin = _inicio + _longitud;

		if( fin > _octetos.length )
			fin = _octetos.length;

		int nro = _inicio;
		if( nro < fin )
			sb.append( representacionBinaria( _octetos[ nro ] ) );
		nro++;
		while( nro < fin )
			{
			sb.append( _separador );
			sb.append( representacionBinaria( _octetos[ nro ] ) );
			nro++;
			}
		return sb.toString();
		}

	private static String representacionBinaria( byte _octeto )
		{
		StringBuilder sb = new StringBuilder();
		int mascara = 128;
		while( mascara > 0 )
			{
			if( ( mascara & _octeto ) != 0 )
				sb.append( "1" );
			else
				sb.append( "0" );
			mascara /= 2;
			}
		return sb.toString();
		}

	public static String representacionOctetos( byte[] _octetos )
		{
		if( _octetos == null )
			return "";

		return representacionOctetos( _octetos, 0, _octetos.length, "." );
		}

	public static String representacionOctetos( byte[] _octetos, int _inicio, int _longitud, String _separador )
		{
		if( _inicio < 0 )
			return "";

		if( _longitud < 0 )
			return "";

		StringBuilder sb = new StringBuilder();
		int fin = _inicio + _longitud;

		if( fin > _octetos.length )
			fin = _octetos.length;

		int nro = _inicio;

		if( nro < fin )
			{
			sb.append( representacionOcteto( _octetos[ nro ] ) );
			nro++;
			}

		while( nro < fin )
			{
			sb.append( _separador );
			sb.append( representacionOcteto( _octetos[ nro ] ) );
			nro++;
			}

		return sb.toString();
		}

	/**
	 * Rellena el <code>_texto</code> con <code>_c</code> por la parte
	 * izquierda, para alcanzar una longitud total de
	 * <code>_longitudTotal</code>
	 * <p>
	 * No se rellena, si la longitud de <code>_texto</code> ya es
	 * <code>_longitudTotal</code>, o superior
	 * </p>
	 * 
	 * @param _texto
	 *            El texto a rellenar
	 * @param _longitudTotal
	 *            Longitud total del texto con el relleno
	 * @param _c
	 *            Car�cter a utilizar como relleno
	 * @return El texto ampliado por la izquierda
	 */
	public static String completa( String _texto, int _longitudTotal, char _c )
		{
		int longitudActual = _texto.length();
		int longirudRelleno = _longitudTotal - longitudActual;
		if( longirudRelleno < 1 )
			return _texto;

		char[] relleno = new char[ longirudRelleno ];
		Arrays.fill( relleno, _c );
		return new String( relleno ) + _texto;
		}

	/**
	 * Lee de <code>_is</code> un bloque de una longitud de
	 * <code>_cantidad</code> de bytes. Lanza una excepción si no se ha podido
	 * leer esta <code>_cantidad</code>.
	 * 
	 * 
	 * @param _is
	 * @param _cantidad
	 * @return Los datos le�dos, o nada, si se ha llegado al fin del fichero
	 * 
	 * @throws IOException
	 *             Si la cantidad de bytes le�dos no es la misma
	 *             <code>_cantidad</code>
	 */
	public static byte[] leeDe( InputStream _is, int _cantidad ) throws IOException
		{
		byte[] datos = new byte[ _cantidad ];
		int leido = _is.read( datos );
		if( leido == -1 )
			return new byte[] {};

		if( leido != _cantidad )
			throw new IOException( String.format( "Registro corrupto: longitud=%d, pero le�do=%d", _cantidad, leido ) );

		return datos;
		}
}
