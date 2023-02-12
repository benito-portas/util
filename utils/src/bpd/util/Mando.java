package bpd.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * <h1>Definición</h1> Interpretación de un texto como secuencia de partes. El
 * espacio en blanco es el separador que define las partes. La primera parte se
 * interpreta como <code>mando</code>, el resto son los <code>par�metros</code>
 * <h2>Accesos</h2>
 * <li>mando</li> Texto que representa el mando. La primera parte del texto
 * <li>parametros</li> El texto original sin la primera parte (todos los
 * par�metros)
 * <li>listaParametros</li> Lista con todos los par�metros, o lista vacía, si el
 * mando no tiene par�metros
 * <li>parametro</li> Acceso a uno de los par�metros, seg�n el �ndice. El primer
 * par�metro tiene el �ndice cero. Si el �ndice consultado no existe, el
 * resultado es un texto vac�o
 * <li>es</li> Comprueba si es un mando en concreto
 * <li>esComentario</li>
 * <li>esMando</li> No es comentario
 * <p>
 * Si el primer car�cter no blanco es un inicio de comentario, el mando se
 * interpreta como comentario. No tiene ni mando ni par�metros.
 * </p>
 */
public class Mando
{
	private String			_ContenidoOriginal	= "";
	private String			_Mando;
	private List< String >	_Parametros			= new ArrayList<  >();
	private String			_LineaParams;
	private String			_DelimMando			= " ";
	private String			_DelimParams		= " ";
	private String			_InicioComentario	= "#";

	public Mando( String _mando, String _delimMando, String _delimParams )
		{
		_DelimMando = _delimMando;
		_DelimParams = _delimParams;
		inicializa( _mando );
		}

	/**
	 * Genera el mando seg�n el texto <code>_mando</code>
	 * 
	 * @param _mando
	 *            El texto
	 */
	public Mando( String _mando )
		{
		inicializa( _mando );
		}

	/**
	 * @deprecated No mezclar el concepto de mando con el de comentario 
	 * @param _mando
	 * @param _inicioComentario
	 */
	public Mando( String _mando, String _inicioComentario )
		{
		inicializa( _mando );
		inicioComentario( _inicioComentario );
		}

	private void inicializa( String _mando )
		{
		if( ( _mando == null ) || ( _mando.trim().length() < 1 ) )
			inicializaMandoVacio();
		else
			inicializaMandoCon( _mando.trim() );
		}

	/**
	 * Acceso al mando
	 * 
	 * @return El mando (la primera parte del texto)
	 */
	public String mando()
		{
		return _Mando;
		}

	public boolean es( String _mando )
		{
		return _mando.equals( _Mando );
		}

	/**
	 * @deprecated No mezclar el concepto de mando con el de comentario 
	 * 
	 * @return
	 */
	public boolean esComentario()
		{
		return ( _Mando.length() < 1 ) || _Mando.startsWith( _InicioComentario );
		}

	public boolean esMando()
		{
		return !esComentario();
		}

	/**
	 * Lista con los par�metros
	 * 
	 * @return La lista
	 */
	public List< String > listaParametros()
		{
		return new ArrayList<  >( _Parametros );
		}

	/**
	 * Acceso al par�metro con el �ndice <code>_nroParam</code>. El primer
	 * par�metro tiene el �ndice cero. Si no existe el �ndice, se obtiene un
	 * texto vac�o
	 * 
	 * @param _nroParam
	 *            El �ndice para acceder al par�metro
	 * @return El par�metro
	 */
	public String parametro( int _nroParam )
		{
		if( ( _nroParam >= 0 ) && ( _nroParam < _Parametros.size() ) )
			return _Parametros.get( _nroParam );

		return "";
		}

	/**
	 * Cantidad de par�metros que componten el mando
	 * 
	 * @return El número de par�metros
	 */
	public int numeroParametros()
		{
		return _Parametros.size();
		}

	/**
	 * Acceso al texto original que contiene s�lo los par�metros tal cual se
	 * introdujo
	 * 
	 * @return Los par�metros
	 */
	public String parametros()
		{
		return _LineaParams;
		}

	public String parametrosAPartirDe( int _nroParam )
		{
		ConcatenadorDeTexto ct = new ConcatenadorDeTexto( " " );
		for( int np = _nroParam; np < numeroParametros(); np++ )
			ct.concatena( parametro( np ) );

		return ct.toString();
		}

	private void inicializaMandoCon( String _mando )
		{
		_ContenidoOriginal = _mando;
		_mando = prepara( _mando );
		StringTokenizer st = new StringTokenizer( _mando, _DelimMando );
		if( st.hasMoreTokens() )
			_Mando = st.nextToken().trim();

		if( _Mando == null || _Mando.length() < 1 )
			{
			_Mando = "";
			_LineaParams = "";
			return;
			}

		_LineaParams = _mando.substring( _Mando.length() ).trim();
		st = new StringTokenizer( _LineaParams, _DelimParams );
		while( st.hasMoreTokens() )
			_Parametros.add( st.nextToken().trim().replace( "%_%", " " ) );

		_LineaParams = _LineaParams.replace( "%_%", " " );
		}

	public String contenidoOriginal()
		{
		return _ContenidoOriginal;
		}

	/**
	 * @param _mando2
	 * @return
	 */
	private String prepara( String _texto )
		{
		char[] letras = _texto.toCharArray();
		StringBuilder sb = new StringBuilder();
		boolean en_texto = false;
		for( char c: letras )
			{
			if( c == '"' )
				{
				en_texto = !en_texto;
				continue;
				}
			if( en_texto && c == ' ' )
				sb.append( "%_%" );
			else
				sb.append( c );
			}
		return sb.toString();
		}

	private void inicializaMandoVacio()
		{
		inicializaSoloMandoCon( "" );
		}

	private void inicializaSoloMandoCon( String _mando )
		{
		_Mando = _mando;
		_LineaParams = "";
		}

	@Override
	public String toString()
		{
		return _Mando + "(" + _LineaParams + ")";
		}

	private void inicioComentario( String _inicio )
		{
		if( ( _inicio == null ) || ( _inicio.length() < 1 ) )
			return;

		_InicioComentario = _inicio;
		}
}
