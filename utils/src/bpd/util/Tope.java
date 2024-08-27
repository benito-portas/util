package bpd.util;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Contador que incrementa su valor hasta alcanzar su tope, donde ejecuta la
 * función de los observadores registrados.
 * <p/>
 * 
 * Para registrar un observador se utiliza el método
 * {@link #observadoPor(Runnable)}. El observador es una función.
 *
 */
public class Tope
{
	private Set< Runnable >	_Observadores	= new HashSet<>();

	private int				_Limite			= 0;
	private int				_Contador		= 0;
	private boolean			_FuncionCiclica;

	public Tope( int _limite )
		{
		_Limite = _limite;
		}

	public Tope observadoPor( Runnable _observador )
		{
		_Observadores.add( _observador );
		return this;
		}

	/**
	 * Haz que la alarma se ejecute cíclicamente
	 * 
	 * @return
	 */
	public Tope ciclico()
		{
		_FuncionCiclica = true;
		return this;
		}

	/**
	 * Pon el contador a cero
	 * 
	 * @return
	 */
	public Tope rearma()
		{
		_Contador = 0;
		return this;
		}

	public Tope tope( int _tope )
		{
		_Limite = _tope;
		return this;
		}

	/**
	 * Incrementa si se cumple la <code>_condicion</code>
	 * 
	 * @see #incrementa()
	 * 
	 * @param _condicion
	 * @return
	 */
	public Tope incrementa( boolean _condicion )
		{
		if( _condicion )
			return incrementa();

		return this;
		}

	/**
	 * Incrementa el contador en <code>_cantidad</code> unidades, y si alcanza
	 * el límite, lanza la alarma y rearma el contador
	 * 
	 * @param _cantidad
	 * @return
	 */
	public Tope incrementa( int _cantidad )
		{
		/*
		 * Contador exhausto. No vuelve a contar mientras no se rearme
		 */
		if( !enCuenta() )
			return this;

		avanzaContador( _cantidad );
		if( enCuenta() )
			return this;

		/*
		 * Se ha alcanzado el tope. Notificación a los observadores
		 */
		_Observadores.forEach( Runnable::run );

		/*
		 * Vuelve a ejecutar si es cíclico
		 */
		if( esCiclico() )
			rearma();

		return this;
		}

	/**
	 * Incrementa el contador en una unidad, y si alcanza el límite, lanza la
	 * alarma y rearma el contador
	 * 
	 * @return
	 */
	public Tope incrementa()
		{
		return incrementa( 1 );
		}

	/**
	 * Comprueba si el contador se encuentra en la zona de cuenteo o ha
	 * alcanzado el tope.
	 * 
	 * @return <code>true</code> si todavía no se ha alcanzado el límite, o
	 *         <code>false</code> en caso contrario. Si es cíclico, el resultado
	 *         será siempre <code>true</code>
	 */
	public boolean enCuenta()
		{
		return valor() < _Limite;
		}

	@Override
	public String toString()
		{
		return String.format( "%d/%d", valor(), _Limite );
		}

	/*
	 * Valor actual del contador
	 */
	public int valor()
		{
		return _Contador;
		}

	public static void main( String[] args )
		{
		Tope contador = new Tope( 4 ).observadoPor( () -> System.out.println( "tope" ) );

		System.out.println( "No ciclico" );
		IntStream.range( 0, 20 ).forEach( e -> contador.incrementa() );

		Tope contador2 = new Tope( 4 )//
										.ciclico()
										.observadoPor( () -> System.out.println( "tope" ) );

		System.out.println( "Ciclico" );
		IntStream.range( 0, 20 ).forEach( e -> contador2.incrementa() );
		}

	private boolean esCiclico()
		{
		return _FuncionCiclica;
		}

	private void avanzaContador( int _cantidad )
		{
		_Contador = _Contador + _cantidad;
		}
}
