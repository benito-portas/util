/**
 * 
 */
package bpd.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 
 * <ol>
 * <li>6-5-2022, error</li>Inserción en una posición en concreto
 * (filtro/exclusión/transformación)
 * </ol>
 * 
 *
 */
public class ReemplazadorFiltro
{
	private Map< String, String >	_Transformaciones					= new LinkedHashMap<>();
	private List< String >			_Filtros							= new ArrayList<>();
	private List< String >			_TextosExcluir						= new ArrayList<>();

	private boolean					_TransformacionesEstanHabilitadas	= true;
	private boolean					_FiltrosEstanHabilitados			= true;
	private boolean					_ExclusionesEstanHabilitadas		= true;

	/**
	 * Borra todas las definiciones de textos a excluir
	 * 
	 * @return
	 * 
	 * @see #rearma() para borrar todos los reemplazos, exclusiones y filtros
	 * @see #borraFiltros() para borrar todos los filtros
	 * @see #borraFiltro(String) par borrar un filtro
	 * @see #borraExclusion(String) para borrar una exclusión
	 * @see #borraReemplazo(String) para borrar un reemplazo
	 * @see #borraReemplazos() para borrar todos los reemplazos
	 * 
	 */
	public ReemplazadorFiltro borraExclusiones()
		{
		_TextosExcluir.clear();
		return this;
		}

	/**
	 * @see #rearma() para borrar todos los reemplazos, exclusiones y filtros
	 * @see #borraFiltros() para borrar todos los filtros
	 * @see #borraFiltro(String) par borrar un filtro
	 * @see #borraExclusiones() para borrar todas las exclusiones
	 * @see #borraReemplazo(String) para borrar un reemplazo
	 * @see #borraReemplazos() para borrar todos los reemplazos
	 * 
	 * @param _textoExclusion
	 * @return
	 */
	public ReemplazadorFiltro borraExclusion( String _textoExclusion )
		{
		_TextosExcluir.remove( _textoExclusion );
		return this;
		}

	/**
	 * Borra todas las definiciones de reemplazos, exclusiones y filtros
	 * 
	 * @return
	 * 
	 * @see #borraFiltro(String)
	 * @see #borraFiltros()
	 * @see #borraExclusion(String)
	 * @see #borraExclusiones()
	 * @see #borraReemplazo(String)
	 * @see #borraReemplazos()
	 */
	public ReemplazadorFiltro rearma()
		{
		borraFiltros();
		borraExclusiones();
		borraTransformaciones();
		return this;
		}

	/**
	 * Añade <code>_texto</code> al final de la lista de textos a excluir.
	 * 
	 * @param _texto
	 *            El texto a excluir
	 * @return
	 */
	public ReemplazadorFiltro excluye( String _texto )
		{
		return excluye( _texto, _TextosExcluir.size() );
		}

	public ReemplazadorFiltro excluye( String _texto, int _posicion )
		{
		if( _ExclusionesEstanHabilitadas )
			insertaEnLista( _texto, _TextosExcluir, _posicion );
		return this;
		}

	public ReemplazadorFiltro habilitaExclusiones( boolean _habilitar )
		{
		_ExclusionesEstanHabilitadas = _habilitar;
		return this;
		}

	/**
	 * Añande al final de la lista de transformaciones.
	 * 
	 * 
	 * @param _textoOriginal
	 *            Expresión REGEX que define el texto a transformar
	 * @param _transformacion
	 *            Texto que substituye el texto original
	 * @return
	 */
	public ReemplazadorFiltro transforma( String _textoOriginal, String _transformacion )
		{
		return transforma( _textoOriginal, _transformacion, _Transformaciones.size() );
		}

	/**
	 * 
	 * Añade una nueva transformación y posiciónala en la <code>_posicion</code>
	 * de la lista de transformaciones. No se añade, si la
	 * <code>_posicion</code> está fuera de rango.
	 * <p/>
	 * 
	 * Si el <code>_textoOriginal</code> ya se encuentra en la lista, se
	 * modifica ésta en la posición que se encuentre, sin tener en cuenta la
	 * <code>_posicion</code> que se esté pasando.
	 * <p/>
	 * 
	 * @param _textoOriginal
	 * @param _transformacion
	 * @param _posicion
	 * @return
	 */
	public ReemplazadorFiltro transforma( String _textoOriginal, String _transformacion, int _posicion )
		{
		if( !_TransformacionesEstanHabilitadas )
			return this;

		if( _posicion < 0 || _posicion > _Transformaciones.size() )
			return this;

		/*
		 * Comprobación de la expresión REGEX
		 */
		Pattern.compile( _textoOriginal );

		/*
		 * El primer elemento se mete directamente
		 */
		if( _Transformaciones.isEmpty()  )
			{
			_Transformaciones.put( _textoOriginal, _transformacion );
			return this;
			}

		if( _Transformaciones.containsKey( _textoOriginal ) )
			{
			_Transformaciones.remove( _textoOriginal );
			transforma( _textoOriginal, _transformacion, _posicion );
			}

		LinkedHashMap< String, String > nuevaLista = new LinkedHashMap<>();
		int pos = 0;
		for( Entry< String, String > e: _Transformaciones.entrySet() )
			{
			if( pos == _posicion )
				nuevaLista.put( _textoOriginal, _transformacion );

			nuevaLista.put( e.getKey(), e.getValue() );
			pos++;
			}
		if( !nuevaLista.containsKey( _textoOriginal ))
			nuevaLista.put( _textoOriginal, _transformacion );

		_Transformaciones = nuevaLista;
		
		return this;
		}

	public ReemplazadorFiltro habilitaTransformacion( boolean _habilitar )
		{
		_TransformacionesEstanHabilitadas = _habilitar;
		return this;
		}

	/**
	 * Texto que resulta de aplicar las transformaciones previamente definidas
	 * 
	 * @param _texto
	 * @return
	 */
	private String transformado( String _texto )
		{
		/*
		 * Se transforma si las transformaciones están habilitadas
		 */
		if( _TransformacionesEstanHabilitadas )
			{
			Set< Entry< String, String > > trans = transformaciones().entrySet();
			for( Entry< String, String > e: trans )
				{
				Pattern p = Pattern.compile( e.getKey() );
				Matcher m = p.matcher( _texto );
				if( m.find() )
					_texto = m.replaceAll( e.getValue() );
				}
			}

		return _texto;
		}

	public Map< String, String > transformaciones()
		{
		return _Transformaciones;
		}

	public ReemplazadorFiltro borraTransformacion( String _textoOriginal )
		{
		_Transformaciones.remove( _textoOriginal );
		return this;
		}

	public ReemplazadorFiltro borraTransformaciones()
		{
		_Transformaciones.clear();
		return this;
		}

	/**
	 * Lista de los textos a excluir, en el orden en el que se han definido
	 * 
	 * @return
	 */
	public List< String > exclusiones()
		{
		return _TextosExcluir;
		}

	/**
	 * Borra todos los filtros definidos. A partir de ahora, no se filtra ningún
	 * texto
	 * 
	 * @return
	 * 
	 * @see #rearma()
	 * @see #borraFiltro(String)
	 * @see #borraExclusion(String)
	 * @see #borraExclusiones()
	 * @see #borraReemplazo(String)
	 * @see #borraReemplazos()
	 * 
	 */
	public ReemplazadorFiltro borraFiltros()
		{
		_Filtros.clear();
		return this;
		}

	/**
	 * Borra <code>_texto</code> de la lista de los filtros. A partir de ahora,
	 * no se verán las líneas que contengan <code>_texto</code>, si hay otros
	 * filtros definidos.
	 * <p>
	 * Si era el único filtro que quedaba, al no tener filtro, se visualizan
	 * todas las líneas de texto
	 * </p>
	 * 
	 * @param _texto
	 *            El texto a eliminar de la lista de filtros
	 * @return
	 * 
	 * @see #rearma() para borrar todos los reemplazos, exclusiones y filtros
	 * @see #borraFiltros() para borrar todos los filtros
	 * @see #borraExclusion(String) para borrar una exclusión
	 * @see #borraExclusiones() para borrar todas las exclusiones
	 * @see #borraReemplazo(String) para borrar un reemplazo
	 * @see #borraReemplazos() para borrar todos los reemplazos
	 * 
	 */
	public ReemplazadorFiltro borraFiltro( String _texto )
		{
		_Filtros.remove( _texto );
		return this;
		}

	/**
	 * Añade <code>_texto</code> al final de la lista de filtros. No se añade si
	 * el texto es vacío.
	 * 
	 * @param _texto
	 *            El texto a incluir en los filtros
	 * @return
	 */
	public ReemplazadorFiltro filtra( String _texto )
		{
		return filtra( _texto, _Filtros.size() );
		}

	/**
	 * Inserta el <code>_texto</code> en la <code>_posicion</code> de la lista.
	 * No se inserta si los filtros no están habilitados.
	 * 
	 * @return
	 * 
	 */
	public ReemplazadorFiltro filtra( String _texto, int _posicion )
		{
		if( _FiltrosEstanHabilitados )
			insertaEnLista( _texto, _Filtros, _posicion );
		return this;
		}

	public void habilitaFiltro( boolean _habilitar )
		{
		_FiltrosEstanHabilitados = _habilitar;
		}

	/**
	 * @see #reemplazos()
	 * @see #exclusiones()
	 * 
	 * @return Los filtros definidos. Colección de textos
	 */
	public Collection< String > filtros()
		{
		return _Filtros;
		}

	public ReemplazadorFiltro bajaTransformacion( String _clave )
		{
		_Transformaciones = bajaUnaPosicion( _Transformaciones, _clave );
		return this;
		}

	public ReemplazadorFiltro subeTransformacion( String _clave )
		{
		_Transformaciones = subeUnaPosicion( _Transformaciones, _clave );
		return this;
		}

	private boolean esTextoFiltrar( String _texto )
		{
		/*
		 * Sólo se aplica el filtro si los filtros están habilitados
		 */
		if( !_FiltrosEstanHabilitados )
			return true;

		/*
		 * No hay filtros, no se filtra
		 */
		if( _Filtros.isEmpty() )
			return true;

		Predicate< ? super String > esFiltro = clave -> Pattern.compile( clave ).matcher( _texto ).find();
		return _Filtros.stream().anyMatch( esFiltro );
		}

	/**
	 * @param _textoOriginal
	 *            El texto a transformar
	 * 
	 * @return El texto resultante de aplicar las transformaciones, las
	 *         exclusiones y los filtros definidos
	 */
	public String aplicaA( String _textoOriginal )
		{
		String textoTransformado = transformado( _textoOriginal );

		if( esTextoParaExcluir( textoTransformado ) )
			return "";

		if( !esTextoFiltrar( textoTransformado ) )
			return "";

		return textoTransformado;
		}

	/**
	 * @param _texto
	 * @return
	 */
	private boolean esTextoParaExcluir( String _texto )
		{
		/*
		 * No se excluye si las transformaciones no están habilitadas
		 */
		if( !_ExclusionesEstanHabilitadas )
			return false;

		Predicate< ? super String > esExcluir = clave -> Pattern.compile( clave ).matcher( _texto ).find();
		return _TextosExcluir.stream().anyMatch( esExcluir );
		}

	private static Map< String, String > subeUnaPosicion( Map< String, String > _lista, final String _clave )
		{
		ArrayList< String > claves = new ArrayList<>( _lista.keySet() );
		int act = claves.indexOf( _clave );
		String clave = claves.remove( act );
		claves.add( act - 1, clave );

		LinkedHashMap< String, String > res = new LinkedHashMap<>();
		claves.forEach( c -> res.put( c, _lista.get( c ) ) );
		return res;
		}

	private static Map< String, String > bajaUnaPosicion( Map< String, String > _lista, final String _clave )
		{
		ArrayList< String > claves = new ArrayList<>( _lista.keySet() );
		int act = claves.indexOf( _clave );
		String clave = claves.remove( act );
		claves.add( act + 1, clave );

		LinkedHashMap< String, String > res = new LinkedHashMap<>();
		claves.forEach( c -> res.put( c, _lista.get( c ) ) );
		return res;
		}

	private void insertaEnLista( String _texto, List< String > _lista, int _posicion )
		{
		if( _texto.isEmpty() )
			return;

		Pattern.compile( _texto );

		if( _lista.isEmpty() )
			_posicion = -1;

		int pos = Math.max( _posicion, 0 );
		pos = Math.min( pos, _lista.size() );

		int ind = _lista.indexOf( _texto );
		if( ind < 0 )
			{
			_lista.add( pos, _texto );
			return;
			}

		if( ind == pos )
			return;

		_lista.add( pos, _texto );

		if( ind > pos )//subir
			ind++;

		_lista.remove( ind );
		}

	public static void main( String[] args )
		{
		ReemplazadorFiltro rf = new ReemplazadorFiltro();
		boolean eo = false;

		System.out.println( "--filtros" );

		rf.filtra( "uno" );
		eo = rf.filtros().stream().collect( Collectors.joining( "," ) ).equals( "uno" );
		System.out.println( eo );
		rf.filtra( "dos" );
		eo = rf.filtros().stream().collect( Collectors.joining( "," ) ).equals( "uno,dos" );
		System.out.println( eo );
		rf.filtra( "cero", 0 );
		eo = rf.filtros().stream().collect( Collectors.joining( "," ) ).equals( "cero,uno,dos" );
		System.out.println( eo );
		rf.filtra( "uno", 0 );
		eo = rf.filtros().stream().collect( Collectors.joining( "," ) ).equals( "uno,cero,dos" );
		System.out.println( eo );
		rf.filtra( "dos", 1 );
		eo = rf.filtros().stream().collect( Collectors.joining( "," ) ).equals( "uno,dos,cero" );
		System.out.println( eo );
		rf.filtra( "xxx", 20 ); // por encima del límite
		eo = rf.filtros().stream().collect( Collectors.joining( "," ) ).equals( "uno,dos,cero,xxx" );
		System.out.println( eo );
		rf.filtra( "xxx", -20 ); // por debajo del límite
		eo = rf.filtros().stream().collect( Collectors.joining( "," ) ).equals( "xxx,uno,dos,cero" );
		System.out.println( eo );

		eo = rf//
				.rearma()
				.filtra( "uno" )
				.filtra( "uno", 1 )
				.filtros()
				.stream()
				.collect( Collectors.joining( "," ) )
				.equals( "uno" );
		System.out.println( eo );

		eo = rf//
				.rearma()
				.filtra( "uno" )
				.filtra( "dos" )
				.filtra( "dos", 2 )
				.filtros()
				.stream()
				.collect( Collectors.joining( "," ) )
				.equals( "uno,dos" );
		System.out.println( eo );

		// bajar una posición
		eo = rf//
				.rearma()
				.filtra( "uno" )
				.filtra( "uno", 1 )
				.filtros()
				.stream()
				.collect( Collectors.joining( "," ) )
				.equals( "uno" );
		System.out.println( eo );

		// bajar una posición
		eo = rf//
				.rearma()
				.filtra( "uno" )
				.filtra( "dos" )
				.filtra( "uno", 2 )
				.filtros()
				.stream()
				.collect( Collectors.joining( "," ) )
				.equals( "dos,uno" );
		System.out.println( eo );

		// bajar una posición
		eo = rf//
				.rearma()
				.filtra( "uno" )
				.filtra( "dos" )
				.filtra( "tres" )
				.filtra( "cuatro" )
				.filtra( "dos", 3 )
				.filtros()
				.stream()
				.collect( Collectors.joining( "," ) )
				.equals( "uno,tres,dos,cuatro" );
		System.out.println( eo );

		System.out.println( "--exclusiones" );

		rf.excluye( "uno" );
		eo = rf.exclusiones().stream().collect( Collectors.joining( "," ) ).equals( "uno" );
		System.out.println( eo );
		rf.excluye( "dos" );
		eo = rf.exclusiones().stream().collect( Collectors.joining( "," ) ).equals( "uno,dos" );
		System.out.println( eo );
		rf.excluye( "cero", 0 );
		eo = rf.exclusiones().stream().collect( Collectors.joining( "," ) ).equals( "cero,uno,dos" );
		System.out.println( eo );
		rf.excluye( "uno", 0 );
		eo = rf.exclusiones().stream().collect( Collectors.joining( "," ) ).equals( "uno,cero,dos" );
		System.out.println( eo );
		rf.excluye( "dos", 1 );
		eo = rf.exclusiones().stream().collect( Collectors.joining( "," ) ).equals( "uno,dos,cero" );
		System.out.println( eo );
		rf.excluye( "xxx", 20 ); // por encima del límite
		eo = rf.exclusiones().stream().collect( Collectors.joining( "," ) ).equals( "uno,dos,cero,xxx" );
		System.out.println( eo );
		rf.excluye( "xxx", -20 ); // por debajo del límite
		eo = rf.exclusiones().stream().collect( Collectors.joining( "," ) ).equals( "xxx,uno,dos,cero" );
		System.out.println( eo );

		System.out.println( "--transformaciones" );

		rf.transforma( "uno", "UNO" );
		eo = rf.transformaciones().keySet().stream().collect( Collectors.joining( "," ) ).equals( "uno" );
		System.out.println( eo );
		rf.transforma( "dos", "DOS" );
		eo = rf.transformaciones().keySet().stream().collect( Collectors.joining( "," ) ).equals( "uno,dos" );
		System.out.println( eo );
		rf.transforma( "uno-y-medio", "UNO-Y-MEDIO", 1 );
		eo = rf.transformaciones().keySet().stream().collect( Collectors.joining( "," ) ).equals( "uno,uno-y-medio,dos" );
		System.out.println( eo );
		rf.transforma( "dos", "DOS", 1 );
		eo = rf.transformaciones().keySet().stream().collect( Collectors.joining( "," ) ).equals( "uno,dos,uno-y-medio" );
		System.out.println( eo );
		rf.transforma( "uno", "UNO", 2 );
		eo = rf.transformaciones().keySet().stream().collect( Collectors.joining( "," ) ).equals( "dos,uno-y-medio,uno" );
		System.out.println( eo );
		rf.transforma( "uno", "UNO", 2 ); // en la misma posición
		eo = rf.transformaciones().keySet().stream().collect( Collectors.joining( "," ) ).equals( "dos,uno-y-medio,uno" );
		System.out.println( eo );
		rf.transforma( "xxx", "XXX", 20 ); // por encima del límite
		eo = rf.transformaciones().keySet().stream().collect( Collectors.joining( "," ) ).equals( "dos,uno-y-medio,uno" );
		System.out.println( eo );
		rf.transforma( "xxx", "XXX", -20 ); // por debajo del límite
		eo = rf.transformaciones().keySet().stream().collect( Collectors.joining( "," ) ).equals( "dos,uno-y-medio,uno" );
		System.out.println( eo );

		System.out.println( "--rearme" );
		rf.rearma();
		eo = rf.filtros().isEmpty();
		System.out.println( eo );
		eo = rf.exclusiones().isEmpty();
		System.out.println( eo );
		eo = rf.transformaciones().isEmpty();
		System.out.println( eo );

		eo = rf//
				.transforma( "uno", "UNO" )
				.transforma( "dos", "DOS" )
				.transforma( "tres", "TRES" )
				.transforma( "cero", "CERO", 0 )
				.transformaciones()
				.keySet()
				.stream()
				.collect( Collectors.joining( "," ) )
				.equals( "cero,uno,dos,tres" );
		System.out.println( eo );

		// subir una posición
		System.out.println( "--subir una posición" );
		eo = rf	.rearma()//
				.transforma( "uno", "UNO" )
				.transforma( "dos", "DOS" )
				.transforma( "tres", "TRES" )
				.transforma( "tres", "TRES", 1 )
				.transformaciones()
				.keySet()
				.stream()
				.collect( Collectors.joining( "," ) )
				.equals( "uno,tres,dos" );
		System.out.println( eo );

		// bajar una posición
		System.out.println( "--bajar una posición" );
		eo = rf	.rearma()//
				.transforma( "uno", "UNO" )
				.transforma( "dos", "DOS" )
				.transforma( "tres", "TRES" )
				.transforma( "dos", "DOS", 3 )
				.transformaciones()
				.keySet()
				.stream()
				.collect( Collectors.joining( "," ) )
				.equals( "uno,tres,dos" );
		System.out.println( eo );

		System.out.println( "fin" );
		}
}
