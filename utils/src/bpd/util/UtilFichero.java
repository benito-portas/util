package bpd.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.xml.bind.DatatypeConverter;

public class UtilFichero
{

	private UtilFichero()
		{
		}

	public static boolean esPcap( File _f ) throws IOException
		{
		return esPcapBe( _f ) || esPcapLe( _f ) || esPcapNanoBe( _f ) || esPcapNanoLe( _f ) || esPcapng( _f );
		}

	public static boolean esPdf( File _f ) throws IOException
		{
		return esDeCodigo( "%PDF-".getBytes(), _f );
		}

	public static boolean esZip( File _f )
		{
		return _f.getName().toLowerCase().endsWith( ".zip" );
		}

	public static boolean esRar( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "52 61 72 21 1A 07 00" ), _f ) || esDeCodigo( Datos.deTexto( "52 61 72 21 1A 07 01 00" ), _f );
		}

	public static boolean esJar( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "50 4B 03 04 14 00 08 00 08 00" ), _f );
		}

	public static boolean esPng( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "89 50 4E 47 0D 0A 1A 0A" ), _f );
		}

	public static boolean esClaseJava( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "CA FE BA BE" ), _f );
		}

	public static boolean esUtf8( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "EF BB BF" ), _f );
		}

	public static boolean esUtf16Le( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "FF FE" ), _f );
		}

	public static boolean esUtf16Be( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "FE FF" ), _f );
		}

	public static boolean esUtf32Le( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "FF FE 00 00" ), _f );
		}

	public static boolean esUtf32Be( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "00 00 FE FF" ), _f );
		}

	public static boolean esWav( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "00 00 FE FF" ), _f );
		}

	public static boolean es7z( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "37 7A BC AF 27 1C" ), _f );
		}

	public static boolean esXls( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "D0 CF 11 E0 A1 B1 1A E1" ), _f );
		}

	public static boolean esXlsx( File _f )
		{
		return _f.getName().toLowerCase().endsWith( ".xlsx" );
		}

	public static boolean esDoc( File _f )
		{
		return _f.getName().toLowerCase().endsWith( ".doc" );
		}

	public static boolean esDocx( File _f )
		{
		return _f.getName().toLowerCase().endsWith( ".docx" );
		}

	public static boolean esGz( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "1F 8B" ), _f );
		}

	public static boolean esMp4( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "00 00 00 20 66 74 79 70 69 73 6F 6D" ), _f );
		}

	public static boolean esPcapLe( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "D4 C3 B2 A1" ), _f );
		}

	public static boolean esPcapBe( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "A1 B2 C3 D4" ), _f );
		}

	public static boolean esPcapNanoLe( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "4D 3C B2 A1" ), _f );
		}

	public static boolean esPcapNanoBe( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "4D 3C B2 A1" ), _f );
		}

	public static boolean esPcapng( File _f ) throws IOException
		{
		byte[] inicio = inicioDe( _f, 16 );
		byte[] codigoPcapng = Datos.porcion( inicio, 8, 8 + 4 );
		boolean codigoPcapEo = Arrays.equals( codigoPcapng, Datos.deTexto( "4D 3C 2B 1A" ) ) || Arrays.equals( codigoPcapng, Datos.deTexto( "D4 C3 2B 1A" ) );

		return esDeCodigo( Datos.deTexto( "0A 0D 0D 0A" ), _f ) && codigoPcapEo;
		}

	public static boolean esIco( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "00 00 01 00" ), _f );
		}

	public static boolean esJpeg( File _f ) throws IOException
		{
		byte[] inicio = inicioDe( _f, 4 );
		boolean es = Arrays.equals( inicio, Datos.deTexto( "FF D8 FF DB" ) ) || Arrays.equals( inicio, Datos.deTexto( "FF D8 FF EE" ) ) || Arrays.equals( inicio, Datos.deTexto( "FF D8 FF E1" ) );
		if( !es )
			{
			inicio = inicioDe( _f, 12 );
			es = Arrays.equals( inicio, Datos.deTexto( "FF D8 FF E0 00 10 4A 46 49 46 00 01" ) );
			}

		return es;
		}

	public static boolean esJpg( File _f ) throws IOException
		{
		return esJpeg( _f );
		}

	public static boolean esGif( File _f ) throws IOException
		{
		return esDeCodigo( Datos.deTexto( "47494638" ), _f );
		}

	/**
	 * @param _codigo
	 * @param _f
	 * @return
	 * @throws IOException
	 */
	public static boolean esDeCodigo( byte[] _codigo, File _f ) throws IOException
		{
		return Arrays.equals( _codigo, inicioDe( _f, _codigo.length ) );
		}

	/**
	 * Comprueba si el <code>_fichero</code> representa una imagen.
	 * 
	 * @param _fichero
	 * @return <code>true</code>, si es imagen, <code>false</code>, si no es
	 *         imagen, o el <code>_fichero</code> es <code>null</code> o no
	 *         existe.
	 * 
	 * @throws IOException
	 */
	public static boolean esImagen( File _fichero ) throws IOException
		{
		return imagenDe( _fichero ) != null;
		}

	/**
	 * @param _fichero
	 * @return
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage imagenDe( File _fichero ) throws IOException
		{
		if( !_fichero.exists() )
			return null;

		return ImageIO.read( _fichero );
		}

	/**
	 * Obtiene el tipo de imagen que representa el <code>_fichero</code>.
	 * 
	 * @param _fichero
	 * @return el tipo de imagen (PNG, JPEG...), o <code>null</code>, si el
	 *         fichero no existe, o un texto vac�o, si no se puede obtener el
	 *         tipo de imagen (porque no es una imagen, o por cualquier otro
	 *         motivo?).
	 * 
	 * @throws IOException
	 */
	public static String tipoImagen( File _fichero ) throws IOException
		{
		if( _fichero == null || !_fichero.exists() )
			return null;

		InputStream is = new FileInputStream( _fichero );

		ImageInputStream iis = ImageIO.createImageInputStream( is );
		Iterator< ImageReader > iter = ImageIO.getImageReaders( iis );
		if( !iter.hasNext() )
			return "";

		ImageReader reader = iter.next();
		iis.close();
		is.close();

		return reader.getFormatName();
		}

	/**
	 * Calcula el c�digo <code>MD5</code> del contenido del
	 * <code>_fichero</code>. El resultado es un texto con la representación en
	 * hexadecimal del c�digo objetinod
	 * 
	 * @param _fichero
	 * @return
	 * @throws IOException
	 */
	public static String md5( File _fichero ) throws IOException
		{
		try
			{
			MessageDigest md = MessageDigest.getInstance( "MD5" );

			md.update( Files.readAllBytes( _fichero.toPath() ) );
			byte[] digest = md.digest();

			return DatatypeConverter.printHexBinary( digest ).toUpperCase();
			}
		catch( NoSuchAlgorithmException _ex )
			{
			// Nunca se va a dar
			_ex.printStackTrace();
			}

		return "";
		}

	/**
	 * Obtiene una ristra de octetos de la longitud <code>_cantidad </code>
	 * cargada con los octetos de las primeras posiciones del fichero
	 * <code>_f</code>.
	 * <p/>
	 * 
	 * El resultado será una ristra vacía si el fichero no existe, o el tamap�o
	 * del fichero es inferior a la <code>_cantidad</code> pedida.
	 * 
	 * @param _f
	 *            El fichero
	 * @param _cantidad
	 *            Cantidad de octetos que se deben leer
	 * @return Ristra de octetos de longitud <code>_cantidad</code>, y los datos
	 *         cargados de las primeras posiciones del fichero <code>_f</code>.
	 * 
	 * @throws IOException
	 */
	private static byte[] inicioDe( File _f, int _cantidad ) throws IOException
		{
		if( !_f.exists() )
			return new byte[] {};

		byte[] datos = new byte[ _cantidad ];

		if( _f.exists() )
			try( FileInputStream fin = new FileInputStream( _f ) )
				{
				int x = fin.read( datos );
				if( x < _cantidad )
					return new byte[] {};
				}
			catch( FileNotFoundException _ex )
				{
				// Nunca se va a dar
				_ex.printStackTrace();
				}

		return datos;
		}

}
