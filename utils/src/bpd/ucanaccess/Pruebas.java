package bpd.ucanaccess;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import bpd.util.Texto;

public class Pruebas
{
	public static void vigilaFichero( String _fich ) throws IOException
		{
		String nomFich = Texto.terminadoEn( ".accdb", _fich );
		File fich = new File( nomFich );
		System.out.println( "Vigilando " + nomFich );

		WatchService watcher = FileSystems.getDefault().newWatchService();
		Path dir = Paths.get( fich.getParent() );
		dir.register( watcher, StandardWatchEventKinds.ENTRY_MODIFY );
		boolean valid = true;
		try
			{
			while( valid )
				{
				WatchKey key = watcher.take();
				String url = "jdbc:ucanaccess://" + nomFich + ";showSchema=true;openExclusive=false;ignoreCase=true";
				System.out.println( url );

				for( WatchEvent< ? > event: key.pollEvents() )
					{
					System.out.println( event.kind() + ": " + event.context() );
					leeEstaciones( url );
					System.out.println();

					valid = key.reset();
					}
				}
			}
		catch( InterruptedException _ex )
			{
			Thread.currentThread().interrupt();
			}
		catch( SQLException _ex )
			{
			_ex.printStackTrace();
			}

		System.out.println( "FIN Vigilando " + nomFich );
		}

	private static void leeEstaciones( String url ) throws SQLException
		{
		try( Connection conn = DriverManager.getConnection( url ) )
			{
			try( Statement stm = conn.createStatement(); )
				{
				ResultSet rs = stm.executeQuery( "select * from Estaciones" );
				while( rs.next() )
					{
					String cod = rs.getString( "Código" );
					String nom = rs.getString( "Nombre" );
					String lin = String.format( "Estación %s, %s", cod, nom );
					System.out.println( lin );
					}
				}
			}
		}

	public static void main( String[] args ) throws IOException
		{
		pruebas();
		vigilaFichero( "c:/Users/usuario/workspace/Amurrio/actpas.accdb" );
		}

	private static void pruebas()
		{
		String carpeta = "c:/Users/usuario/workspace/Amurrio";
		String bd = "actpas";
		String url = "jdbc:ucanaccess://" + carpeta + "/" + bd + ".accdb;showSchema=true;openExclusive=true;ignoreCase=true";

		try( Connection conn = DriverManager.getConnection( url ) )
			{
			System.out.println( "Catálogo" );
			ResultSet catalogs = conn.getMetaData().getCatalogs();
			while( catalogs.next() )
				System.out.println( "  " + catalogs.getString( "TABLE_CAT" ) );

			System.out.println( "Esquema" );
			ResultSet schemas = conn.getMetaData().getSchemas();
			while( schemas.next() )
				System.out.println( "  " + schemas.getString( "TABLE_SCHEM" ) );

			System.out.println( "Tablas" );
			ResultSet tables = conn.getMetaData().getTables( null, null, null, null );
			while( tables.next() )
				{
				String esquema = tables.getString( "TABLE_SCHEM" );
				String tabla = tables.getString( "TABLE_NAME" );
				System.out.println( String.format( "  %s:%s", esquema, tabla ) );
				}

			System.out.println( "Campos clave de Estaciones" );
			ResultSet claves = conn.getMetaData().getPrimaryKeys( null, null, "Estaciones" );
			while( claves.next() )
				{
				String esquema = claves.getString( "TABLE_SCHEM" );
				String tabla = claves.getString( "TABLE_NAME" );
				String col = claves.getString( "COLUMN_NAME" );
				String pk = claves.getString( "PK_NAME" );
				String sec = claves.getString( "KEY_SEQ" );

				System.out.println( String.format( "  %s:%s %s=%s.%s", esquema, tabla, pk, col, sec ) );
				System.out.println();
				}

			System.out.println( "Columnas" );
			ResultSet cols = conn.getMetaData().getColumns( null, null, null, null );
			while( cols.next() )
				{
				String esquema = cols.getString( "TABLE_SCHEM" );
				String tabla = cols.getString( "TABLE_NAME" );
				String col = cols.getString( "COLUMN_NAME" );
				String tipo = cols.getString( "TYPE_NAME" );
				String longitud = cols.getString( "COLUMN_SIZE" );
				String decimales = cols.getString( "DECIMAL_DIGITS" );
				String norequerido = cols.getString( "IS_NULLABLE" );
				String auto = cols.getString( "IS_AUTOINCREMENT" );

				System.out.println( String.format( "  %s:%s.%s[tipo: %s, nro-digitos:(%s,%s), null:%s, autoinc:%s]", esquema, tabla, col, tipo, longitud, decimales, norequerido, auto ) );
				}

			System.out.println( "fin." );
			}
		catch( SQLException _ex )
			{
			_ex.printStackTrace();
			}
		}
}
