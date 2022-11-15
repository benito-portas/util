package bpd.util;

import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlantillaRegex
{
	private StringBuilder	_Patron;
	private Pattern			_Compilado;

	public PlantillaRegex()
		{
		_Patron = new StringBuilder();
		_Compilado = null;
		}

	private static final Pattern _PatronCoordenadasGoogle = //
			new PlantillaRegex() //
									.inicio()

									// coordenada_y
									.grupo( "y", new PlantillaRegex().signoNumerico().opcional().digito().maximo( 2 ).punto().digito().repetido() )

									// separación
									.coma()
									.espacio()
									.repetidoOpcional()

									// coordenada_x
									.grupo( "x", new PlantillaRegex().signoNumerico().opcional().digito().maximo( 3 ).punto().digito().repetido() )

									.fin()
									.patron();

	public static Pattern plantillaCoordenadasGoogle()
		{
		return _PatronCoordenadasGoogle;
		}

	public static boolean esCoordenadasGoogle( String _texto )
		{
		return _texto != null && plantillaCoordenadasGoogle().matcher( _texto ).find();
		}

	/**
	 * Compila la expresión contenida en <code>_campo</code> y capta posibles
	 * excepciones, con el objeto de encontrar fallos en el momento de definir
	 * la expresión, en vez de cuando se está ejecutando la lectura de ficheros
	 * 
	 * @param texto
	 * 
	 * @param _campo
	 * @return
	 */
	public static String compruebaRegexDe( String texto )
		{
		try
			{
			Pattern.compile( texto );
			return( "" );
			}
		catch( Exception _ex )
			{
			return( _ex.getMessage() );
			}
		}

	/**
	 * Concatena, mediante espacio, los textos de los grupos en secuencia de
	 * número de grupos.
	 * 
	 * @param m
	 * @return
	 */
	public static String gruposEn( Matcher m )
		{
		int nroGrupos = m.groupCount();
		if( nroGrupos < 1 )
			return m.group( 0 );

		ConcatenadorDeTexto ct = new ConcatenadorDeTexto( " " );
		for( int nro = 0; nro < nroGrupos; nro++ )
			ct.concatena( m.group( nro + 1 ) );

		return ct.toString();
		}

	public String gruposEn( String _texto )
		{
		Matcher m = patron().matcher( _texto );
		if( !m.find() )
			return "";

		return gruposEn( m );
		}

	@Override
	public String toString()
		{
		return _Patron.toString();
		}

	public Pattern patron()
		{
		if( _Compilado == null )
			_Compilado = Pattern.compile( _Patron.toString() );
		return _Compilado;
		}

	/**
	 * 
	 * @param _texto
	 * @return <code>true</code>, si la plantilla aplica al <code>_texto</code>
	 */
	public boolean es( String _texto )
		{
		return _texto != null && patron()//
											.matcher( _texto )
											.find();
		}

	/**
	 * Cualquier expresión. Al menos algo
	 * 
	 * @return
	 */
	public PlantillaRegex algo()
		{
		return indiferente().repetido();
		}

	/**
	 * Cualquier carácter, número, signo, etc. es válido
	 * 
	 * @return
	 */
	public PlantillaRegex indiferente()
		{
		return texto( "." );
		}

	/**
	 * Signo más (+) para anteceder a números positivos, y signo menos (-) para
	 * los negaticos
	 * 
	 * @return
	 */
	public PlantillaRegex signoNumerico()
		{
		return texto( "[+-]" );
		}

	public PlantillaRegex punto()
		{
		return texto( "\\." );
		}

	public PlantillaRegex coma()
		{
		return texto( "," );
		}

	public PlantillaRegex puntoYComa()
		{
		return texto( ";" );
		}

	public PlantillaRegex mas()
		{
		return texto( "+" );
		}

	public PlantillaRegex menos()
		{
		return texto( "-" );
		}

	public PlantillaRegex igual()
		{
		return texto( "=" );
		}

	public PlantillaRegex dolar()
		{
		return texto( "\\$" );
		}

	public PlantillaRegex dosPuntos()
		{
		return texto( ":" );
		}

	/**
	 * Barra iclinada hacia adelante (la barra del 7)
	 * 
	 * @return
	 */
	public PlantillaRegex barraInclinada()
		{
		return texto( "\\/" );
		}

	public PlantillaRegex barraHorizontal()
		{
		return texto( "\\|" );
		}

	public PlantillaRegex barraInvertida()
		{
		return texto( "\\\\" );
		}

	/**
	 * El punto
	 * 
	 * @return
	 */
	public PlantillaRegex separadorDecimales()
		{
		return texto( "\\." );
		}

	public PlantillaRegex separadoresDecimales()
		{
		return unoDe( new PlantillaRegex().punto().coma().toString() );
		}

	public PlantillaRegex espacios()
		{
		return texto( "\\s+" );
		}

	/**
	 * Cualquier cosa que sea espacio (<code>\S</code>)
	 * 
	 * @return
	 */
	public PlantillaRegex espacio()
		{
		return texto( "\\s" );
		}

	/**
	 * Cualquier cosa que no sea espacio (<code>\S</code>)
	 * 
	 * @return
	 */
	public PlantillaRegex sinEspacio()
		{
		return texto( "\\S" );
		}

	public PlantillaRegex inicio()
		{
		return texto( "^" );
		}

	public PlantillaRegex fin()
		{
		return texto( "$" );
		}

	public PlantillaRegex hex()
		{
		return unoDe( "a-fA-F0-9" );
		}

	public PlantillaRegex numeroEntero()
		{
		return signoNumerico()//
								.digito()
								.repetido();
		}

	/**
	 * Consta de una letra, o el signo "_" seguido opcionalmente de una lista de
	 * letras y números, teniendo en cuenta los caracteres especiales del
	 * español
	 * 
	 * @return
	 */
	public PlantillaRegex palabra()
		{
		return texto( "[a-zA-ZáéíóúÁÉÍÓÚüÜñÑàèìòù_][a-zA-ZáéíóúÁÉÍÓüÜñÑàèìòù0-9_]*" );
		}

	public PlantillaRegex digito()
		{
		return texto( "\\d" );
		}

	public PlantillaRegex digitos( int _numeroDigitos )
		{
		if( _numeroDigitos < 2 )
			return digito();

		return digito().repetido( _numeroDigitos );
		}

	public PlantillaRegex digitos( int _de, int _a )
		{
		if( _de < 2 )
			return texto( "\\d{," + _a + "}" );

		if( _a < 2 )
			return texto( "\\d{" + _a + ",}" );

		return texto( "\\d" + repetido( _de, _a ) );
		}

	public PlantillaRegex grupo( String _nombre, PlantillaRegex _plantillaRegEx )
		{
		return texto( "(?<" + _nombre + ">" + _plantillaRegEx.toString() + ")" );
		}

	public PlantillaRegex grupo( PlantillaRegex _plantillaRegEx )
		{
		return texto( "(" + _plantillaRegEx.toString() + ")" );
		}

	public PlantillaRegex alternativa()
		{
		return texto( "|" );
		}

	public PlantillaRegex opcional()
		{
		return texto( "?" );
		}

	public PlantillaRegex repetido()
		{
		return texto( "+" );
		}

	public PlantillaRegex unoDe( String _letras )
		{
		return texto( "[" )//
							.texto( _letras )
							.texto( "]" );
		}

	public PlantillaRegex ningunoDe( String _letras )
		{
		return texto( "[^" )//
							.texto( _letras )
							.texto( "]" );
		}

	public PlantillaRegex repetidoOpcional()
		{
		return texto( "*" );
		}

	public PlantillaRegex texto()
		{
		return texto( "\\w" );
		}

	public PlantillaRegex repetido( int _nroVeces )
		{
		return texto( "{" + _nroVeces + "}" );
		}

	public PlantillaRegex minimo( int _nroVeces )
		{
		return texto( "{" + _nroVeces + ",}" );
		}

	public PlantillaRegex maximo( int _nroVeces )
		{
		return repetido( 0, _nroVeces );
		}

	public PlantillaRegex repetido( int _min, int _max )
		{
		texto( "{" + _min + "," + _max + "}" );
		return this;
		}

	/**
	 * Es número cuando el <code>_texto</code> se compone de cifras, con
	 * posibilidad de signo positivo o negativo al inicio, con o sin punto
	 * decimal. El punto decimal puede ser el inicio o el final del número. El
	 * número tiene que contener al menos una cifra
	 * 
	 * @param _texto
	 * @return
	 */
	public static boolean esNumero( String _texto )
		{
		return _texto != null && patronNumero().matcher( _texto ).find();
		}

	public static Pattern patronNumero()
		{
		return new PlantillaRegex()//
									.inicio()
									.signoNumerico()
									.opcional()
									.digito()
									.repetido()
									.separadorDecimales()
									.opcional()
									.digito()
									.repetidoOpcional()
									.fin()

									.alternativa()
									.inicio()
									.separadorDecimales()
									.digito()
									.repetido()
									.fin()
									.patron();
		}

	public static Pattern patronNumeroEntero()
		{
		return new PlantillaRegex()//
									.inicio()
									.signoNumerico()
									.opcional()
									.digito()
									.repetido()
									.fin()
									.patron();
		}

	public static Pattern patronHex()
		{
		return new PlantillaRegex()//
									.inicio()
									.hex()
									.repetido()
									.fin()
									.patron();
		}

	public static Pattern patronHex( int _nroDigitos )
		{
		return new PlantillaRegex()//
									.inicio()
									.hex()
									.repetido( _nroDigitos )
									.fin()
									.patron();
		}

	/**
	 * Es número entero cuando el <code>_texto</code> se compone exclusivamente
	 * de cifras, posiblemente precedidas de signo positivo o negativo
	 * 
	 * @param _texto
	 * @return
	 */
	public static boolean esNumeroEntero( String _texto )
		{
		return _texto != null && patronNumeroEntero().matcher( _texto ).find();
		}

	public static void main( String[] args )
		{
		prueba( "\"38.76520240000001, -0.9541771400000032\" son coordenadas google", esCoordenadasGoogle( "38.76520240000001, -0.9541771400000032" ) );
		pruebaEsNumero();
		pruabaEsNumeroEntero();
		}

	private static void pruabaEsNumeroEntero()
		{
		System.out.println( "----NÚMERO ENTERO" );
		prueba( "Número entero debe contener al menos una cifra: con texto vacío", !esNumeroEntero( "" ) );
		prueba( "Número entero sólo contiene cifras 123", esNumeroEntero( "123" ) );
		prueba( "Número entero no contiene nada que no sea cifra 1.23", !esNumeroEntero( "1.23" ) );
		prueba( "Número entero puede tener signo positivo +123", esNumeroEntero( "+123" ) );
		prueba( "Número entero puede tener signo negativo -123", esNumeroEntero( "-123" ) );
		prueba( "Número entero no debe tener sólo signo +", !esNumeroEntero( "+" ) );
		}

	private static void pruebaEsNumero()
		{
		System.out.println( "----NÚMERO" );
		prueba( "Número debe contener al menos una cifra: con texto vacío", !esNumero( "" ) );
		prueba( "Número entero 123", esNumero( "123" ) );
		prueba( "Número decimal ordinario 1.23", esNumero( "1.23" ) );
		prueba( "Número empieza por punto decimal .123", esNumero( ".123" ) );
		prueba( "Número termina en punto decimal 123.", esNumero( "123." ) );
		prueba( "Número debe contener al menos una cifra: sólo punto", !esNumero( "." ) );
		prueba( "Número no tiene más de un punto decimal 1.2.3", !esNumero( "1.2.3" ) );
		}

	private static void prueba( String _prueba, boolean _correcto )
		{
		@SuppressWarnings( "resource" )
		PrintStream out = _correcto ? System.out : System.err;
		String resultado = _correcto ? "correcto" : "INCORRECTO";

		out.println( String.format( "%s%n\t--> %s", _prueba, resultado ) );
		}

	public PlantillaRegex texto( String _texto )
		{
		_Compilado = null;
		_Patron.append( _texto );
		return this;
		}

}
