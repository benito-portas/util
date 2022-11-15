package bpd.util.pruebas;

import java.io.IOException;
import java.util.Arrays;

import bpd.util.Datos;

public class PruebasDatos
{
	public static void main( String[] args ) throws IOException
		{
		String textoPruebas = "es un texto de pruebas";

		pruebaPorcionAPartirDe( 3, textoPruebas, "un texto de pruebas" );
		pruebaPorcionAPartirDe( 15, textoPruebas, "pruebas" );
		pruebaPorcionAPartirDe( -15, textoPruebas, textoPruebas );
		pruebaPorcionAPartirDe( 100, textoPruebas, "" );

		pruebaPorcionHasta( 11, textoPruebas, "es un texto" );
		pruebaPorcionHasta( 111, textoPruebas, textoPruebas );
		pruebaPorcionHasta( -11, textoPruebas, "" );

		pruebaPorcion( 6, 11, textoPruebas, "texto" );

		pruebaCompleta( "12345", 10, 'x', "12345xxxxx" );
		pruebaCompleta( "12345", 1, 'x', "12345" );

		pruebaRevertir( "12345", "54321" );
		}

	private static void pruebaRevertir( String _textoPrueba, String _resultadoEsperado )
		{
		byte[] esperado = _resultadoEsperado.getBytes();
		byte[] resultado = Datos.revertido( _textoPrueba.getBytes() );

		boolean eo = Arrays.equals( resultado, esperado );
		if( eo )
			System.out.println( String.format( "EO \"%s\" revertido da \"%s\"", _textoPrueba, _resultadoEsperado ) );
		else
			System.out.println( String.format( "ERROR \"%s\" revertido da \"%s\" en vez de \"%s\"", _textoPrueba, new String( resultado ), _resultadoEsperado ) );
		}

	private static void pruebaCompleta( String _origen, int _longitud, char _relleno, String _resultadoEsperado ) throws IOException
		{
		byte[] origen = _origen.getBytes();
		byte[] resultado = Datos.completa( origen, _longitud, ( byte )_relleno );

		boolean eo = Arrays.equals( resultado, _resultadoEsperado.getBytes() );
		if( eo )
			System.out.println( String.format( "EO \"%s\" completado con \"%s\" a tamaño %d da \"%s\"", _origen, _relleno, _longitud, _resultadoEsperado ) );
		else
			System.out.println( String.format( "ERROR \"%s\" completado con \"%s\" a tamaño %d da \"%s\" en vez de \"%s\"", _origen, _relleno, _longitud, new String( resultado ), _resultadoEsperado ) );
		}

	private static void pruebaPorcion( int _inicio, int _fin, String _textoPruebas, String _resultadoEsperado )
		{
		byte[] porcion = Datos.porcion( _textoPruebas.getBytes(), _inicio, _fin );
		byte[] esperado = _resultadoEsperado.getBytes();

		boolean eo = Arrays.equals( porcion, esperado );
		if( eo )
			System.out.println( String.format( "EO \"%s\" entre %d y %d da \"%s\"", _textoPruebas, _inicio, _fin, _resultadoEsperado ) );
		else
			System.out.println( String.format( "ERROR \"%s\" entre %d y %d da \"%s\" en vez de \"%s\"", _textoPruebas, _inicio, _fin, new String( porcion ), _resultadoEsperado ) );
		}

	private static void pruebaPorcionHasta( int _inicio, String _textoPruebas, String _resultadoEsperado )
		{
		byte[] porcion = Datos.porcionHasta( _inicio, _textoPruebas.getBytes() );
		byte[] esperado = _resultadoEsperado.getBytes();

		boolean eo = Arrays.equals( porcion, esperado );
		if( eo )
			System.out.println( String.format( "EO \"%s\" hasta %d da \"%s\"", _textoPruebas, _inicio, _resultadoEsperado ) );
		else
			System.out.println( String.format( "ERROR \"%s\" hasta %d da \"%s\" en vez de \"%s\"", _textoPruebas, _inicio, new String( porcion ), _resultadoEsperado ) );
		}

	private static void pruebaPorcionAPartirDe( int _inicio, String _textoPruebas, String _resultadoEsperado )
		{
		byte[] porcion = Datos.porcionAPartirDe( _inicio, _textoPruebas.getBytes() );
		byte[] esperado = _resultadoEsperado.getBytes();

		boolean eo = Arrays.equals( porcion, esperado );
		if( eo )
			System.out.println( String.format( "EO \"%s\" a partir de %d da \"%s\"", _textoPruebas, _inicio, _resultadoEsperado ) );
		else
			System.out.println( String.format( "ERROR \"%s\" a partir de %d da \"%s\" en vez de \"%s\"", _textoPruebas, _inicio, new String( porcion ), _resultadoEsperado ) );
		}
}
