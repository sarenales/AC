
public class Main {
	
	public static void main(String[] args) {
		int palabra;
		int bloque;
		int conjunto;
		int tag;
		int palabrasBloque;
		int direccion = 0;
		double referencias = 0;
		double aciertos = 0;
		int tasaAciertos = 0;
		int ciclos = 0;
		int ciclosTot = 0;
		MC mc = new MC();
		mc.pedirTeclado();
		mc.cacheNull();
		
		while(direccion != -1) {
			System.out.println(" ");
			mc.pedirDirOp();
			direccion = mc.getDireccion();
			if(direccion>=0) {
			referencias++;
				palabra = (int)(mc.getDireccion()/mc.getBytePal());
			palabrasBloque = mc.getPalBq()/mc.getBytePal();
			bloque = (int)(palabra/mc.getPalBq());
			conjunto = (int)(bloque % mc.getNumConj());
			if (mc.getNumConj()==1) {
			tag = bloque/8;
			}else if (mc.getNumConj()==2) {
				tag = bloque/2;
			}else if (mc.getNumConj()==4) {
				tag = bloque/4;
			}else {
				tag = bloque;

			}
			
			System.out.println("Direccion: "+mc.getDireccion()+" - Palabra: "+palabra+" - Bloque: "+bloque+" (palabras "+palabra+" - "+(palabra+(mc.getPalBq()-1))+")");
			System.out.println("Conjunto: "+conjunto+" - Tag: "+tag);
			mc.busqueda(bloque);
			
			if(mc.isAcierto()) {
				ciclos = 2;
				aciertos++;
				System.out.println("ACIERTO EN LA CACHE");
				if(mc.getReemplazo() == 1) {
					mc.reemLRU(bloque, conjunto);
				}
			}else {
				System.out.println("FALLO EN LA CACHE");
				boolean aux = mc.introducir(conjunto, bloque, tag);
				if (aux) {
					//System.out.println("escrito");
					ciclos = 2 + 21+(mc.getPalBq()-1)+21+(mc.getPalBq()-1);
				}else {
					//System.out.println("no escrito");
					ciclos = 2 + 21+(mc.getPalBq()-1);

				}
			}
			ciclosTot = ciclosTot + ciclos;
			System.out.println(" ");
			System.out.println(" ");
			System.out.println("T_acc: "+ciclos+" ciclos.");
			System.out.println(" ");
			System.out.println(" ");
			mc.imprimirCache();
			
			}
		}
		System.out.println("Referencias: "+(int)referencias+" -- Aciertos: "+(int)aciertos+" -- Tasa de aciertos, h = "+(aciertos/referencias));
		System.out.println("Tiempo total = "+ciclosTot+" ciclos");
	}

}
