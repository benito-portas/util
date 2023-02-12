package bpd.util;

import java.util.HashSet;
import java.util.Set;

import bpd.util.observadores.ObservadorCambioContador;

/**
 * <p>
 * Cuenta y descuenta uno a uno o en una cantidad de unidades. En cada cuenta se
 * comunica al usuario del objeto, que el valor del contador ha cambiado.
 * </p>
 * <p>
 * El contador empieza a contar con el valor cero. Con cada <code>cuenta</code>,
 * el valor se incrementa en una <i>unidad de conteo</i>. Con cada
 * <code>descuenta</code>, el valor se decrementa en una <i>unidad de
 * conteo</i>. El valor inicial de la <i>unidad de conteo</i> es uno, y se puede
 * cambiar con <code>pasos</code>.
 * </p>
 * 
 * <p>
 * La cuenta se puede reiniciar desde cero a trav�s del método
 * <code>rearma</code>. Al rearmar se lanza el evento de "valor ha cambiado"
 * </p>
 * 
 */
public class Contador
{
	private Set< ObservadorCambioContador >	_Observadores	= new HashSet<>();

	private int								_Pasos			= 1;
	private int								_Valor			= 0;

	public void valorHaCambiado( int _nuevoValor )
		{
		//
		}

	public Contador observadoPor( ObservadorCambioContador _observador )
		{
		_Observadores.add( _observador );
		return this;
		}

	/**
	 * Pon el valor a cero
	 */
	public void rearma()
		{
		valor( 0 );
		}

	/**
	 * Cantidad de veces que se incrementa el <code>valor</code>
	 * 
	 * @param _pasos
	 */
	public void pasos( int _pasos )
		{
		_Pasos = _pasos;
		}

	/**
	 * Incrementa el <code>valor</code> en una unidad
	 */
	public void cuenta()
		{
		valor( _Valor - _Pasos );
		}

	/**
	 * 
	 * Incrementa el <code>valor</code> en una unidad, si se cumple la
	 * <code>_condicion</code>
	 * 
	 * @param _condicion
	 */
	public void cuenta( boolean _condicion )
		{
		if( _condicion )
			cuenta();
		}

	/**
	 * Decrementa el <code>valor</code> en una unidad
	 */
	public void descuenta()
		{
		valor( _Valor - _Pasos );
		}

	/**
	 * 
	 * Decrementa el <code>valor</code> en una unidad, si se cumple la
	 * <code>_condicion</code>
	 * 
	 * @param _condicion
	 */
	public void descuenta( boolean _condicion )
		{
		if( _condicion )
			descuenta();
		}

	private void valor( int _nuevoValor )
		{
		if( _Valor == _nuevoValor )
			return;

		_Valor = _nuevoValor;
		valorHaCambiado( _Valor );
		_Observadores.forEach( e -> e.contadorHaCambiado( _nuevoValor ) );
		}
}
