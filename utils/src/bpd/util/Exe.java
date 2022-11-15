package bpd.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;

public class Exe
{
	private static boolean				_EsWindows	= System.getProperty( "os.name" ).toLowerCase().contains( "windows" );

	private File						_Directorio	= null;
	private HashMap< String, String >	_Entorno	= new HashMap<>();
	private PrintStream					out			= System.out;
	private PrintStream					err			= System.err;

	public int ejecuta( String _mando ) throws IOException
		{
		ProcessBuilder processBuilder = new ProcessBuilder();
		if( _EsWindows )
			processBuilder.command( "cmd.exe", "/c", _mando );
		else
			processBuilder.command( "bash", "-c", _mando );

		if( _Directorio != null )
			processBuilder.directory( _Directorio );
		if( !_Entorno.isEmpty() )
			processBuilder.environment().putAll( _Entorno );

		Process process = processBuilder.start();

		BufferedReader reader = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
		BufferedReader error = new BufferedReader( new InputStreamReader( process.getErrorStream() ) );

		String lineaTexto;
		while( ( lineaTexto = reader.readLine() ) != null )
			out.println( lineaTexto );
		while( ( lineaTexto = error.readLine() ) != null )
			err.println( lineaTexto );

		return process.exitValue();
		}

	public Exe conSalidaPor( PrintStream _ps )
		{
		if( _ps != null )
			out = _ps;

		return this;
		}

	public Exe conErroresPor( PrintStream _ps )
		{
		if( _ps != null )
			err = _ps;

		return this;
		}

	public Exe directorio( File _dir )
		{
		if( _dir == null )
			return this;
		if( _dir.isFile() )
			return this;
		if( !_dir.exists() )
			return this;

		_Directorio = _dir;
		return this;
		}

	public Exe variableEntorno( String _clave, String _valor )
		{
		_Entorno.put( _clave, _valor );
		return this;
		}

	public static void reiniciaOrdenador() throws IOException
		{
		if( _EsWindows )
			new Exe().ejecuta( "shutdown /r" );
		else
			new Exe().ejecuta( "shutdown -r now" );
		}

	public static void apagaOrdenador() throws IOException
		{
		if( _EsWindows )
			new Exe().ejecuta( "shutdown /s" );
		else
			new Exe().ejecuta( "shutdown -h now" );
		}

	public static void espera( int _milis )
		{
		try
			{
			Thread.sleep( _milis );
			}
		catch( InterruptedException _ex )
			{
			Thread.currentThread().interrupt();
			}
		}

	public static void main( String[] args ) throws IOException
		{
		int resultado = new Exe()//
									.directorio( new File( System.getProperty( "user.home" ) ) )
									.variableEntorno( "IFBLOAD", "C:\\" )
									.ejecuta( "perl -h" );

		System.out.println( String.format( "Sale con codigo: %d", resultado ) );
		}
}
