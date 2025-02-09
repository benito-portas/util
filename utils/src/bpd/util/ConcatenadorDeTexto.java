package bpd.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConcatenadorDeTexto
{
	private String			_Delimitador			= null;
	private boolean			_IncluirVacios			= false;
	private String			_Alternativo			= "";
	private boolean			_EsReverso;

	private List< String >	_Contenidos				= new ArrayList<>();

	/**
	 * Texto que se antepone al contenido, si hay contenido
	 */
	private String			_Prefijo;

	/**
	 * Texto que se añade al contenido, si hay contenido
	 */
	private String			_Sufijo;
	private String			_Secuencia				= "";
	private String			_PlantillaNumeracion	= "";

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

	/**
	 * Realizar la concatenación en orden inverso al de introducción.
	 * <p/>
	 * 
	 * @return
	 */
	public ConcatenadorDeTexto inverso()
		{
		_EsReverso = true;
		return this;
		}

	public boolean estaVacio()
		{
		return _Contenidos.isEmpty();
		}

	public ConcatenadorDeTexto numeracion( String _plantilla )
		{
		_PlantillaNumeracion = _plantilla;
		return this;
		}

	public ConcatenadorDeTexto concatena( String _nuevoTexto )
		{
		if( !_IncluirVacios && ( _nuevoTexto == null || _nuevoTexto.isEmpty() ) )
			return this;

		if( _PlantillaNumeracion.contains( "%d" ) )
			_nuevoTexto = String.format( _PlantillaNumeracion + "%s", _Contenidos.size() + 1, _nuevoTexto );

		_Contenidos.add( _nuevoTexto );
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
	 * Posibilidad de redefinir esta función para tener m�s flexibilidad a la
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

	public ConcatenadorDeTexto concatena( boolean _condicion, Collection< ? > _lista )
		{
		if( _condicion )
			return concatena( _lista );
		return this;
		}

	public ConcatenadorDeTexto secuenciando( String _plantilla )
		{
		String.format( _plantilla, 0, "prueba" );
		_Secuencia = _plantilla;
		return this;
		}

	@Override
	public String toString()
		{
		return aTexto();
		}

	public String aTexto()
		{
		if( _Contenidos.isEmpty() )
			return _Alternativo;

		List< String > contenidos = new ArrayList<>( _Contenidos );
		if( _EsReverso )
			Collections.reverse( contenidos );

		String expresion = esSecuenciando() && contenidos.size() > 1//
				? IntStream//
							.range( 0, contenidos.size() )
							.mapToObj( //
									n -> {
									String plantillaExpresion = _Secuencia + "%s";
									return String.format( plantillaExpresion, n + 1, contenidos.get( n ) );
									} )
							.collect( Collectors.joining( _Delimitador ) )
				: contenidos.stream().collect( Collectors.joining( _Delimitador ) );

		return _Prefijo + expresion + _Sufijo;
		}

	private boolean esSecuenciando()
		{
		return !_Secuencia.isEmpty();
		}
}
