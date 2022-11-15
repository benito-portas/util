package bpd.util;

import java.util.Arrays;
import java.util.Collection;

public class ConcatenadorDeTexto
{
	private StringBuilder	_TextoConcatenado	= new StringBuilder();
	private String			_Delimitador		= null;
	private boolean			_IncluirVacios		= false;
	private String			_Alternativo		= "";

	/**
	 * El que inserta después del inicial
	 */
	private Insertador		_InsertadorFinal	= _texto -> {
												_TextoConcatenado.append( _Delimitador );
												_TextoConcatenado.append( _texto );
												};

	/**
	 * El que inicia la inserción
	 */
	private Insertador		_InsertadorInicial	= _texto -> {
												_TextoConcatenado.append( _texto );
												_Insertador = _InsertadorFinal;
												};

	/**
	 * El que realmente inserta. La primera inserción la asume el
	 * <code>_InsertadorInicial</code>, el resto de inserciones las hace el
	 * <code>_InsertadorFinal</code>
	 */
	private Insertador		_Insertador;

	/**
	 * Texto que se antepone al contenido, si hay contenido
	 */
	private String			_Prefijo;

	/**
	 * Texto que se añade al conenido, si hay contenido
	 */
	private String			_Sufijo;

	public static ConcatenadorDeTexto sinConcatenador()
		{
		return new ConcatenadorDeTexto( "" );
		}

	/**
	 * Concatenador que separa las partes mediante una coma y un espacio
	 * 
	 * @return
	 */
	public static ConcatenadorDeTexto medianteComa()
		{
		return mediante( "," );
		}

	/**
	 * 
	 * @return
	 */
	public static ConcatenadorDeTexto medianteEspacio()
		{
		return mediante( " " );
		}

	public static ConcatenadorDeTexto mediantePuntoYComa()
		{
		return mediante( ";" );
		}

	public static ConcatenadorDeTexto mediantePunto()
		{
		return mediante( "." );
		}

	public static ConcatenadorDeTexto medianteDosPuntos()
		{
		return mediante( ":" );
		}

	public static ConcatenadorDeTexto medianteIgual()
		{
		return mediante( "=" );
		}

	public static ConcatenadorDeTexto medianteBarra()
		{
		return mediante( "/" );
		}

	public static ConcatenadorDeTexto medianteBarraVertical()
		{
		return mediante( "|" );
		}

	public static ConcatenadorDeTexto medianteArroba()
		{
		return mediante( "@" );
		}

	public static ConcatenadorDeTexto medianteTabulador()
		{
		return mediante( "\t" );
		}

	public static ConcatenadorDeTexto medianteNuevaLinea()
		{
		return mediante( "\n" );
		}

	public static ConcatenadorDeTexto mediante( String _concatenador )
		{
		return new ConcatenadorDeTexto( _concatenador );
		}

	public ConcatenadorDeTexto( String _delimitador )
		{
		this( _delimitador, "", "" );
		}

	/**
	 * @param _delimitador
	 * @param _prefijo
	 *            Texto que se antepone al contenido, si hay contenido
	 * @param _sufijo
	 *            Texto que se añade al contenido, si hay contenido
	 */
	public ConcatenadorDeTexto( String _delimitador, String _prefijo, String _sufijo )
		{
		_Delimitador = _delimitador;
		_Insertador = _InsertadorInicial;
		_Prefijo = _prefijo;
		_Sufijo = _sufijo;
		}

	public ConcatenadorDeTexto incluyendoVacios()
		{
		_IncluirVacios = true;
		return this;
		}

	/**
	 * Añade un espacio al delimitador
	 * 
	 * @return
	 */
	public ConcatenadorDeTexto espacio()
		{
		_Delimitador += " ";
		return this;
		}

	public ConcatenadorDeTexto prefijo( String _prefijo )
		{
		_Prefijo = _prefijo;
		return this;
		}

	public ConcatenadorDeTexto sufijo( String _sufijo )
		{
		_Sufijo = _sufijo;
		return this;
		}

	public ConcatenadorDeTexto enmarcadoEn( String _prefijo, String _sufijo )
		{
		return prefijo( _prefijo ).sufijo( _sufijo );
		}

	public ConcatenadorDeTexto alternatva( String _textoAlternativo )
		{
		if( _textoAlternativo != null )
			_Alternativo = _textoAlternativo;

		return this;
		}

	public boolean estaVacio()
		{
		return _TextoConcatenado.length() < 1;
		}

	public ConcatenadorDeTexto concatena( String _nuevoTexto )
		{
		if( !_IncluirVacios && ( _nuevoTexto == null || _nuevoTexto.isEmpty() ) )
			return this;

		_Insertador.inserta( _nuevoTexto );
		return this;
		}

	/**
	 * Añade la <code>_nota</code>, si se cumple la <code>_condicion</code>
	 * 
	 * @param _condicion
	 *            Si se cumple esta condición, se concatena la
	 *            <code>_nota</code>
	 * @param _nota
	 * 
	 * @return
	 */
	public ConcatenadorDeTexto concatena( boolean _condicion, String _nota )
		{
		if( _condicion )
			return concatena( _nota );

		return this;
		}

	public ConcatenadorDeTexto concatena( Object _nuevoObjeto )
		{
		if( _nuevoObjeto != null )
			concatena( contenidoDeConcatenacion( _nuevoObjeto ) );

		return this;
		}

	public ConcatenadorDeTexto concatena( boolean _condicion, Object _nuevoObjeto )
		{
		if( _condicion )
			return concatena( _nuevoObjeto );

		return this;
		}

	/**
	 * <p>
	 * Definición de cómo debe ser concatenado el <code>_objeto</code>. Lo que
	 * aquí se concatena es la representación en texto del <code>_objeto</code>
	 * </p>
	 * <p>
	 * Posibilidad de redefinir esta función para tener más flexibilidad a la
	 * hora de concatenar algo en particular. Por ejemplo, concatenar sólo una
	 * propiedad del objeto.
	 * </p>
	 * 
	 * @param _objeto
	 * @return
	 */
	protected String contenidoDeConcatenacion( Object _objeto )
		{
		return String.valueOf( _objeto );
		}

	public ConcatenadorDeTexto concatena( Collection< ? > _lista )
		{
		if( _lista != null )
			for( Object o: _lista )
				concatena( o );

		return this;
		}

	public ConcatenadorDeTexto concatena( Object... _objects )
		{
		return concatena( Arrays.asList( _objects ) );
		}

	public ConcatenadorDeTexto con( Collection< ? > _lista )
		{
		return concatena( _lista );
		}

	public ConcatenadorDeTexto concatena( boolean _condicion, Collection< ? > _lista )
		{
		if( _condicion )
			return concatena( _lista );
		return this;
		}

	public String toString()
		{
		String textoConcatenado = _TextoConcatenado.toString();

		if( textoConcatenado.isEmpty() && _Alternativo.isEmpty() )
			return "";

		if( textoConcatenado.isEmpty() )
			textoConcatenado = _Alternativo;

		return _Prefijo + textoConcatenado + _Sufijo;
		}
}

interface Insertador
{
	public void inserta( String _texto );
}
