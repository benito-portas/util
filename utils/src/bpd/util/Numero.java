package bpd.util;

public class Numero
{
	private Numero()
		{
		}

	public static String aTexto( int _numero )
		{
		switch( Math.abs( _numero ) )
			{
			case 0:
				return "cero";
			case 1:
				return "uno";
			case 2:
				return "dos";
			case 3:
				return "tres";
			case 4:
				return "cuatro";
			case 5:
				return "cinco";
			case 6:
				return "seis";
			case 7:
				return "siete";
			case 8:
				return "ocho";
			case 9:
				return "nueve";

			default:
				break;
			}
		return "" + _numero;
		}
}
