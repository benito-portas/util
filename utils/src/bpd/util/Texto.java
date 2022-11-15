package bpd.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Splitter;

public class Texto
{
	private final static String[] NUMEROS = { "cero", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve", "diez" };

	private Texto()
		{
		}

	/**
	 * Genera una palabra partiendo del <code>_texto</code>, uniendo las
	 * palabras del <code>_texto</code> en may�scula.
	 * <p/>
	 * 
	 * Ejemplo de la transformaci�n:
	 * 
	 * <pre>
	 * nombre del cliente -> NombreDelCliente
	 * </pre>
	 * 
	 * @param _texto
	 * @return
	 */
	public static String generaClaveCon( String _texto )
		{
		return Stream//
						.of( _texto.split( "\\s+" ) )
						.map( Texto::enMayuscula )
						.collect( Collectors.joining() );
		}

	/**
	 * Genera una palabra partiendo del <code>_texto</code>, uniendo las
	 * palabras del <code>_texto</code> en may�scula. De este proceso se
	 * excluyen las palabras definidas en <code>_excluyendo</code>.
	 * 
	 * Ejemplo de la transformaci�n:
	 * 
	 * <pre>
	 * nombre del cliente -> NombreCliente
	 * </pre>
	 * 
	 * @param _texto
	 * @param _excluyendo
	 * @return
	 */
	public static String generaClaveCon( String _texto, String... _excluyendo )
		{
		List< String > aExcluir = Arrays.asList( _excluyendo );
		return Stream//
						.of( _texto.split( "\\s+" ) )
						.filter( e -> !aExcluir.contains( e ) )
						.map( Texto::enMayuscula )
						.collect( Collectors.joining() );
		}

	/**
	 * 
	 * Comprueba si el texto <code>_opcion</code> se encuentra dentro de las
	 * <code>_opcioones</code>. <code>_opciones</code> es un texto compuesto por
	 * la concatenaci�n de varios textos (las opciones) mediante un punto.
	 * 
	 * <p>
	 * Ejemplo
	 * </p>
	 * 
	 * <pre>
	 * Texto.contieneOpcion( "C.I.A", "I" ) -> true
	 * Texto.contieneOpcion( "C.I.A", "X" ) -> false
	 * Texto.contieneOpcion( "C.I.A", "" ) -> false
	 * </pre>
	 * 
	 * @param _opciones
	 *            Texto con las opciones
	 * @param _opcion
	 *            Valor que se quiere consultar
	 * 
	 * @see #contieneOpcion(String, String, String)
	 * 
	 * @return
	 */
	public static boolean contieneOpcion( String _opciones, String _opcion )
		{
		return contieneOpcion( _opciones, _opcion, "\\." );
		}

	/**
	 * Comprueba si el texto <code>_opcion</code> se encuentra dentro de las
	 * <code>_opcioones</code>. <code>_opciones</code> es un texto compuesto por
	 * la concatenaci�n de varios textos (las opciones) mediante un
	 * <code>_delimitadorOpciones</code> (por ejemplo un punto).
	 * 
	 * <p>
	 * Ejemplo
	 * </p>
	 * 
	 * <pre>
	 * Texto.contieneOpcion( "C.I.A", "I", "\\." ) -> true
	 * </pre>
	 * 
	 * @param _opciones
	 *            Texto con las opciones
	 * @param _opcion
	 *            Valor que se quiere consultar
	 * @param _delimitadorOpciones
	 *            Expresi�n REGEX que define el delimitador en el texto de las
	 *            opciones
	 * 
	 * @see #contieneOpcion(String, String)
	 * 
	 * @return
	 */
	public static boolean contieneOpcion( String _opciones, String _opcion, String _delimitadorOpciones )
		{
		return Pattern//
						.compile( _delimitadorOpciones )
						.splitAsStream( _opciones )
						.anyMatch( e -> e.equals( _opcion ) );
		}

	/**
	 * Genera un n�mero de versi�n autom�ticamente partiendo de una
	 * <code>_version</code> anterior.
	 * 
	 * <p>
	 * La siguiente versi�n se obtiene incrementando el n�mero final de la
	 * expresi�n en <code>_version</code>. Si la expresi�n no termina en n�mero,
	 * se mantiene la versi�n original
	 * </p>
	 * 
	 * Ejemplos:
	 * <ul>
	 * <li>1.2.9</li>Resulta en 1.2.10
	 * <li>Versi�n_1</li>Resulta en Versi�n_2
	 * <li>3</li>Resulta en 4
	 * <li>Prototipo</li>Resulta en Prototipo (no se cambia, no termina en
	 * n�mero)
	 * <li>Actual.2.3</li>Resulta en Actual.2.4
	 * </ul>
	 * 
	 * @param _version
	 * @return
	 */
	public static String siguienteVersionA( String _version )
		{
		if( _version == null || _version.isEmpty() )
			return "1";

		Matcher m = Pattern.compile( "(?<prefijo>.+\\D)?(?<version>\\d+)$" ).matcher( _version );
		if( m.find() )
			{
			String prefijo = m.group( "prefijo" );
			String version = m.group( "version" );

			if( prefijo == null )
				prefijo = "";

			int siguiente = Integer.parseInt( version ) + 1;

			_version = prefijo + siguiente;
			}

		return _version;
		}

	public static String rellena( String _texto, int _longitudTotal, char _relleno )
		{
		String plantilla = "%1$-" + _longitudTotal + "s";
		return String.format( plantilla, _texto ).replace( ' ', _relleno );
		}

	public static String rellenaIzquierda( String _texto, int _longitudTotal, char _relleno )
		{
		String plantilla = "%1$" + _longitudTotal + "s";
		return String.format( plantilla, _texto ).replace( ' ', _relleno );
		}

	/**
	 * Asegura que el <code>_texto</code> termina en <code>_terminacion</code>.
	 * Si no lo hace, se le a�ade la terminaci�n al texto.
	 * <p/>
	 * 
	 * Tiene en cuenta que la terminaci�n puede venir en may�sculas y
	 * min�sculas.
	 * 
	 * @param _terminacion
	 * @param _texto
	 * @return
	 */
	public static String terminadoEn( String _terminacion, String _texto )
		{
		if( _texto.toLowerCase().endsWith( _terminacion.toLowerCase() ) )
			return _texto;

		return _texto + _terminacion;
		}

	/**
	 * Asegura que el <code>_texto</code> empieza por <code>_inicio</code>. Si
	 * no lo hace, se le antepone el inicio al texto
	 * <p/>
	 * 
	 * Tiene en cuenta que la terminaci�n puede venir en may�sculas y
	 * min�sculas.
	 * 
	 * @param _inicio
	 * @param _texto
	 * @return
	 */
	public static String empezandoPor( String _inicio, String _texto )
		{
		if( _texto.toLowerCase().startsWith( _inicio.toLowerCase() ) )
			return _texto;

		return _inicio + _texto;
		}

	/**
	 * Cambia todos los caracteres, no permitidos en un nombre de fichero, por
	 * el car�cter "<code>_</code>".
	 * 
	 * @param _nomFich
	 * @return
	 */
	public static String paraNombreFichero( String _nomFich )
		{
		return _nomFich.replaceAll( "[*:<>?\\/|@#+]", "_" );
		}

	/**
	 * Es num�rico si JAVA entiende la <code>_expresi�n</code> como n�mero
	 * decimal
	 * 
	 * @param _expresion
	 * @return
	 */
	public static boolean esNumerico( String _expresion )
		{
		try
			{
			Double.parseDouble( _expresion );
			}
		catch( NumberFormatException | NullPointerException _ex )
			{
			return false;
			}

		return true;
		}

	/**
	 * <code>_texto</code> es n�mero decimal cuando consta de un punto precedio
	 * o seguido de d�gitos, con posible signo en la primera posici�n
	 * <p/>
	 * 
	 * Ejemplos:
	 * 
	 * <pre>
	 * 1.2
	 * .2
	 * 1.
	 * +.2
	 * -1.2
	 * </pre>
	 * 
	 * @param _texto
	 * @return
	 */
	public static boolean esNumeroDecimal( String _texto )
		{
		if( _texto == null )
			return false;

		Pattern p = Pattern.compile( "^[+-]?(\\d*\\.\\d+)|([+-]?\\d+\\.)$" );
		return p.matcher( _texto ).find();
		}

	/**
	 * <p>
	 * Convierte <code>_texto</code> cambiando los caracteres con acentos en los
	 * mismos sin acento, conservando las may�sculas y min�sculas. Igualmente se
	 * convierte la e�e en ene y la cedilla en la letre ce
	 * </p>
	 * 
	 * @param _texto
	 * @return
	 */
	public static String sinAcentos( String _texto )
		{
		if( _texto == null )
			return null;
		if( _texto.isEmpty() )
			return "";

		_texto = _texto.replace( "�", "a" ).replace( "�", "e" ).replace( "�", "i" ).replace( "�", "o" ).replace( "�", "u" ).replace( "�", "A" ).replace( "�", "E" ).replace( "�", "I" ).replace( "�", "O" ).replace( "�", "U" );
		_texto = _texto.replace( "�", "n" ).replace( "�", "N" );
		_texto = _texto.replace( "�", "c" ).replace( "�", "C" );

		return _texto;
		}

	/**
	 * Pon en may�scula la primera letra del <code>_texto</code>
	 * 
	 * @param _texto
	 *            El texto original con la primera letra en may�lscula
	 * @return
	 */
	public static String enMayuscula( String _texto )
		{
		if( _texto == null )
			return null;
		if( _texto.isEmpty() )
			return "";

		return Character.toUpperCase( _texto.charAt( 0 ) ) + _texto.substring( 1 );
		}

	public static String enMinuscula( String _texto )
		{
		if( _texto == null )
			return null;
		if( _texto.isEmpty() )
			return "";

		return Character.toLowerCase( _texto.charAt( 0 ) ) + _texto.substring( 1 );
		}

	/**
	 * <p>
	 * Comprueba si todas las letras de <code>_texto</code> est�n en may�scula,
	 * incluyendo el gui�n bajo "_"
	 * </p>
	 * 
	 * @param _texto
	 * @return <code>false</code>, si hay al menos una letra en min�scula
	 */
	public static boolean esTodoMayusculas( String _texto )
		{
		Pattern p = Pattern.compile( "^[A-Z�����������������_]+$" );
		return p.matcher( _texto ).find();
		}

	/**
	 * Pone en min�scula la primera letra del <code>_texto</code>
	 * 
	 * @param _texto
	 *            El texto original con la primera letra en min�scula
	 * @return
	 */
	public static String minuscula( String _texto )
		{
		if( _texto == null )
			return null;
		if( _texto.isEmpty() )
			return _texto;

		return Character.toLowerCase( _texto.charAt( 0 ) ) + _texto.substring( 1 );
		}

	public static int cantidadPalabrasEn( String _texto )
		{
		Matcher m = Pattern.compile( "\\b" ).matcher( _texto );
		int nro = 0;
		while( m.find() )
			nro++;

		return nro;
		}

	public static String primeraPalabraEn( String _texto )
		{
		Matcher m = Pattern.compile( "^\\s*(\\w+)" ).matcher( _texto );
		if( m.find() )
			return m.group( 1 );

		return "";
		}

	public static List< String > palabrasEn( String _texto )
		{
		return Stream.of( _texto.split( "[^\\w������������]+" ) ).filter( palabra -> !palabra.isEmpty() ).collect( Collectors.toList() );
		}

	public static void main( String[] args )
		{
		System.out.println( Texto.cantidadPalabrasEn( ".Aqu� / hay   cuatro palabras  " ) );
		System.out.println( Texto.cantidadPalabrasEn( "." ) );

		String _texto = ".Aqu� / hay   cuatro palabras  ";
		Matcher m = Pattern.compile( "\\b" ).matcher( _texto );
		int nro = 0;
		while( m.find() )
			nro++;

		System.out.println( nro / 2 );

		String[] palabras = _texto.split( "[^\\w������������]+" );
		Stream.of( palabras ).forEach( System.out::println );

		System.out.println( palabrasEn( _texto ) );
		}

	/**
	 * Interpreta el <code>_texto</code> como una secuencia de campos separados
	 * por <code>_separadorCampos</code>. El campo en s� se compone de dos
	 * partes. El nombre es la parte anterior al <code>_separadorValor</code>, y
	 * el valor es la parte que sigue al <code>_separadorValor</code>,
	 * eliminando los espacios al inicio y al final
	 * 
	 * @param _lineaTexto
	 *            L�nea de texto con los campos
	 * @param _separadorCampos
	 *            Texto que delimita los campos
	 * @param _separadorValor
	 *            Texto que delimita la clave del valor
	 * 
	 * @return La lista de campos
	 */
	public static Map< String, String > campos( CharSequence _lineaTexto, String _separadorCampos, String _separadorValor )
		{
		List< String > registro = Splitter//
											.on( _separadorCampos )
											.omitEmptyStrings()
											.trimResults()
											.splitToList( _lineaTexto );

		Map< String, String > res = new HashMap<>();
		registro.stream().forEach( //
				e -> //
				{
				Pattern p = Pattern.compile( "\\s*(?<nombre>[^" + _separadorValor + "]+)" + _separadorValor + "\\s*(?<valor>.+)" );
				Matcher m = p.matcher( e );
				if( m.find() )
					{
					String nombre = m.group( "nombre" );
					String valor = m.group( "valor" );

					res.put( nombre, valor );
					}
				} );

		return res;
		}

	/**
	 * Convierte el <code>_texto</code> en una lista de campos representado en
	 * un objeto <code>Map</code> en el que la clave es el nombre del campo y el
	 * valor el valor del campo
	 * 
	 * <p>
	 * El nombre del campo es la palabra que termina en dos puntos (:) y
	 * contin�a con el valor. El valor no va separado de los dos puntos.
	 * </p>
	 * Ejemplo:
	 * 
	 * Definici�n de un registro con los campos id, importe y fecha.
	 * 
	 * <pre>
	 * id:25 importe:244.35 fecha:25-3-2000
	 * </pre>
	 * 
	 * Error, el id no termina en dos puntos, el importe empieza por un espacio
	 * 
	 * <pre>
	 * id :25 importe: 244.35 fecha:25-3-2000
	 * </pre>
	 * 
	 * @param _lineaTexto
	 *            Texto con los campos
	 * 
	 * @return
	 */
	public static Map< String, String > camposDeLineaTexto( String _lineaTexto )
		{
		/*
		 * El nombre del campo es un texto que termina en dos puntos, y tras los
		 * dos puntos no hay un espacio
		 */
		Pattern plantillaNombres = Pattern.compile( "(\\b\\w+):" );
		Matcher m = plantillaNombres.matcher( _lineaTexto );

		List< String > nombres = new ArrayList<>();
		while( m.find() )
			nombres.add( m.group( 1 ) );

		String expr = nombres//
								.stream()
								.map( e -> e + ":(?<" + e + ">.+)?" )
								.collect( Collectors.joining( "\\s+" ) );

		Map< String, String > campos = new HashMap<>();
		Matcher mm = Pattern.compile( expr ).matcher( _lineaTexto );
		if( mm.find() )
			nombres//
					.stream()
					.filter( e -> mm.group( e ) != null )
					.forEach( e -> campos.put( e, mm.group( e ).trim().replace( "_dp_", ":" ) ) );

		return campos;
		}


	public static String camposALineaTexto( Map< String, String > _campos )
		{
		return _campos//
						.entrySet()
						.stream()
						.map( e -> e.getKey() + ":" + e.getValue() )
						.collect( Collectors.joining( " " ) );

		}

	/*
	 * -------------------------------------------------------------------------
	 * FUNCIONALIDADES EXPRESAS PARA LIENZO
	 * -------------------------------------------------------------------------
	 * 
	 */

	public static String conInicioEnMayuscula( String _palabra )
		{
		return enMayuscula( _palabra );
		}

	public static String procesaComillas( String _textoOriginal )
		{
		return _textoOriginal.replace( "'", "\\\\'" );
		}

	public static String plural( String _palabra, int _cantidad )
		{
		return _cantidad == 1 ? _palabra : enPlural( _palabra );
		}

	/**
	 * Nombra el n�mero en texto hasta el 10. A partir del 11 se da el propio
	 * n�mero
	 * 
	 * @param _cantidad
	 * @return
	 */
	public static String enTexto( int _cantidad )
		{
		if( _cantidad < NUMEROS.length )
			return NUMEROS[ _cantidad ];

		return "" + _cantidad;
		}

	/**
	 * Averigua el plural de la <code>_palabra</code>. Si �sta se una frase,
	 * s�lo se pluraliza la primera.
	 *
	 * @param _palabra
	 *            La palabra o frase
	 * @return Texto con el plurar de la palabra
	 */
	public static String enPlural( String _palabra )
		{
		int i = _palabra.indexOf( ' ' );

		//
		// Si es una frase, se pluraliza s�lo la primera
		// palabra
		//
		if( i > 0 )
			return enPlural( _palabra.substring( 0, i ) ) + enPluralAdjetivo( _palabra.substring( i ) );

		String ultimaLetra = _palabra.substring( _palabra.length() - 1 ).toUpperCase();

		if( "AEIOU".indexOf( ultimaLetra ) >= 0 )
			return _palabra + "s";

		if( "Z".indexOf( ultimaLetra ) >= 0 )
			return "ces";

		if( _palabra.endsWith( "�n" ) )
			return _palabra.substring( 0, _palabra.lastIndexOf( "�n" ) ) + "anes";

		if( _palabra.endsWith( "�n" ) )
			return _palabra.substring( 0, _palabra.lastIndexOf( "�n" ) ) + "enes";

		if( _palabra.endsWith( "�n" ) )
			return _palabra.substring( 0, _palabra.lastIndexOf( "�n" ) ) + "ines";

		if( _palabra.endsWith( "�n" ) )
			return _palabra.substring( 0, _palabra.lastIndexOf( "�n" ) ) + "ones";

		if( _palabra.endsWith( "�n" ) )
			return _palabra.substring( 0, _palabra.lastIndexOf( "�n" ) ) + "unes";

		if( _palabra.endsWith( "�s" ) )
			return _palabra.substring( 0, _palabra.lastIndexOf( "�s" ) ) + "anes";

		if( _palabra.endsWith( "�s" ) )
			return _palabra.substring( 0, _palabra.lastIndexOf( "�s" ) ) + "enes";

		if( _palabra.endsWith( "�s" ) )
			return _palabra.substring( 0, _palabra.lastIndexOf( "�s" ) ) + "ines";

		if( _palabra.endsWith( "�s" ) )
			return _palabra.substring( 0, _palabra.lastIndexOf( "�s" ) ) + "ones";

		if( _palabra.endsWith( "�s" ) )
			return _palabra.substring( 0, _palabra.lastIndexOf( "�s" ) ) + "unes";

		return _palabra + "es";
		}

	private static String enPluralAdjetivo( String _palabra )
		{
		if( _palabra == null )
			return null;

		int i = _palabra.indexOf( ' ' );

		if( i > 0 )
			{
			String primeraPalabra = _palabra.substring( 0, i );

			if( esAdjetivo( primeraPalabra ) )
				return enPlural( primeraPalabra ) + _palabra.substring( i );
			else
				return _palabra;
			}

		if( esAdjetivo( _palabra ) )
			return enPlural( _palabra );

		return _palabra;
		}

	public static boolean esAdjetivo( String _palabra )
		{
		String palabra = _palabra.toLowerCase();

		if( palabra.endsWith( "ido" ) )
			return true;

		if( palabra.endsWith( "ida" ) )
			return true;

		if( palabra.endsWith( "ado" ) )
			return true;

		if( palabra.endsWith( "ada" ) )
			return true;

		if( esColor( palabra ) )
			return true;

		return false;
		}

	private static String _Colores = "amarillo.amarilla.azul.blanco.blanca.naranja.negro.negra.rojo.roja.rosa.verde.violeta";

	public static boolean esColor( String _palabra )
		{
		return _Colores.indexOf( _palabra ) >= 0;
		}
}
