package bpd.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bpd.util.observadores.ObservadorEstimulos;

public class Reaccion
{
	private Set< ObservadorEstimulos >	_Observadores	= new HashSet<>();

	private Pattern						_PatronEstimulo;

	/**
	 * <code>_estimulo</code> es una expresión <code>REGEX</code> que define el
	 * estímulo al que hay que reaccionar
	 * 
	 * @param _estimulo
	 */
	public Reaccion( String _estimulo )
		{
		_PatronEstimulo = Pattern.compile( _estimulo );
		}

	/**
	 * Comprueba si el <code>_texto</code> es mi estímulo, y reacciona
	 * 
	 * @param _texto
	 */
	public void estimulo( String _texto )
		{
		if( !esMiEstimulo( _texto ) )
			return;

		_Observadores.forEach( e -> e.reaccion( _texto ) );
		}

	public Reaccion observadoPor( ObservadorEstimulos _observador )
		{
		_Observadores.add( _observador );
		return this;
		}

	public static void main( String[] args )
		{
		Reaccion reaccion = new Reaccion( "^A" ).observadoPor( System.out::println );

		reaccion.estimulo( "A ver si funciona" );
		reaccion.estimulo( "No va a funcionar" );
		}

	private boolean esMiEstimulo( String _texto )
		{
		Matcher m = _PatronEstimulo.matcher( _texto );
		return m.find();
		}
}
