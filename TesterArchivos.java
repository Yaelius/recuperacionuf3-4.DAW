import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.yael.hotchpotch.bean.CSVProduction;

/**
 * 
 */
public class TesterArchivos {

//	static String  path = "./test";
	/**
	 * 
	 */
	static final char scapeChar = '.';

	public static void main(String[] args) {
		String path = "./";

		try {
			mostrarPrimeraLineaFicherosDeUnDirectorio(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// mostrarPrimeraLineaFicherosDeUnDirectorio
	
	/**
	 * 
	 * @param path ruta has encontar el acñvldfkv
	 * @throws FileNotFoundException
	 */
	public static void mostrarPrimeraLineaFicherosDeUnDirectorio(String path) throws FileNotFoundException {
		Set<String> lista;
		File file = new File(path);

		lista = listFilesUsingJavaIO(path);

		// Files iteration
		for (String fileName : lista) {
			if (fileName.endsWith("2.csv")) {
				System.out.println("Voy a procesar: " + fileName);
				file = new File(fileName);
				Scanner scanner = new Scanner(file);
				String linea;
				scanner.nextLine(); // ignore first line with headers
				
				CSVProduction csvProduction = new CSVProduction();
				int indiceFila = 1;
				
				
				// Data processing iteration
				while    (scanner.hasNext()) {
//					System.out.println("Aqui se encuentra el error "+indiceFila);
					linea = fixCommaIssue(scanner.nextLine());
					String[] lineaDiv = linea.split(",");

					for (int i = 0; i < lineaDiv.length; i++) {

//						System.out.println("dato:" + lineaDiv[i]);
						
						Double doublePossitionX = Double.parseDouble(lineaDiv[3]);
						Double doublePossitionY = Double.parseDouble(lineaDiv[4]);
						Double rotation = Double.parseDouble(lineaDiv[5]);
						
						switch (i) {
						case 0:
							// TODO parametrizar procesp para quitar las comillas?
							csvProduction.setReference(lineaDiv[i].replaceAll("\"", ""));
							break;
						case 1:
							// TODO parametrizar procesp para quitar las comillas?
							csvProduction.setValue(lineaDiv[i].replaceAll("\"", ""));
							break;
						case 2:
							// TODO parametrizar procesp para quitar las comillas?
							csvProduction.setPackageId(lineaDiv[i].replaceAll("\"", ""));
							break;
						case 3:
							csvProduction.setPossitionX(doublePossitionX);
							break;
						case 4:
							csvProduction.setPossitionY(doublePossitionY);
							break;
						case 5:
							csvProduction.setRotation(rotation);
							break;
						case 6:
							csvProduction.setSide(lineaDiv[i].replaceAll("\"", ""));
							break;
						default:
							break;
						}

					}

					System.out.println("Objeto("+indiceFila+"): " + csvProduction);
					indiceFila++;
					
					// INSERTAR DATO (FILA) EN BBDD
					// insertar reference deshacer punto por coma
					// insertar value 

				}

//				System.out.println("["+fileName+"] --------------------");

//				CSVProduction csvProduction = new CSVProduction("",null,null,null);

			}
		}

	}

	public static Set<String> listFilesUsingJavaIO(String dir) {
		return Stream.of(new File(dir).listFiles()).filter(file -> !file.isDirectory()).map(File::getName)
				.collect(Collectors.toSet());
	}
	
	private static String fixCommaIssue(String linea) {
		String lineaOut="";
		boolean comillaCerrada = true;
		for (char c : linea.toCharArray()) {
			if (c == '"') {
				comillaCerrada = !comillaCerrada;
			}
			if (c == ',' && !comillaCerrada) {
				lineaOut = lineaOut + scapeChar;
			} else {
				lineaOut = lineaOut + c;
			}
		}
		return lineaOut;
	}

}
