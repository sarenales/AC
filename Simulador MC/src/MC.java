import java.util.*;
public class MC {

	private final int tamCache = 8;//8 bloques
	private int[][] cache = new int[8][5];
	private int bytePal;//4 u 8 B
	private int palBq;//32 o 64 B
	private int bqConj;//1(directa), 2,4,8 (totalmente asiciativa)
	private int numConj;//en base a la cantidad de bloques que entren en cada conjunto sabiendo que la cache es de 8 bqs
	private int reemplazo;//(0)FIFO o (1)LRU
	private int direccion;
	private int operacion;//(0)LOAD o (1)Store
	private boolean acierto;

	public boolean isAcierto() {
		return acierto;
	}

	public void setAcierto(boolean acierto) {
		this.acierto = acierto;
	}

	public int getDireccion() {
		return direccion;
	}

	public void setDireccion(int direccion) {
		this.direccion = direccion;
	}

	public int getOperacion() {
		return operacion;
	}

	public void setOperacion(int operacion) {
		this.operacion = operacion;
	}

	public int getBytePal() {
		return bytePal;
	}

	public void setBytePal(int bytePal) {
		this.bytePal = bytePal;
	}

	public int getPalBq() {
		return palBq;
	}

	public void setPalBq(int palBq) {
		this.palBq = palBq;
	}

	public int getBqConj() {
		return bqConj;
	}

	public void setBqConj(int bqConj) {
		this.bqConj = bqConj;
	}

	public int getNumConj() {
		return numConj;
	}

	public void setNumConj(int numConj) {
		this.numConj = numConj;
	}

	public int getReemplazo() {
		return reemplazo;
	}

	public void setReemplazo(int remplazo) {
		this.reemplazo = remplazo;
	}

	public void pedirTeclado() {
		Scanner s = new Scanner(System.in);
		int aux;
		boolean introducido = false;
		while(!introducido) {
			System.out.println("Dame el tamaño de la palabra (4 o 8 Bytes): ");
			aux = s.nextInt();
			if(aux == 4 || aux == 8) {
				setBytePal(aux);
				introducido = true;
			}else {
				System.out.println("ERROR! DATO INTRODUCIDO ERRONEO!");
				System.out.println(" ");
			}
		}
		introducido = false;
		while(!introducido) {
			System.out.println("Dame el tamaño del bloque (32 o 64 Bytes): ");
			aux = s.nextInt();
			if(aux == 32 || aux == 64) {
				setPalBq(aux/getBytePal());
				introducido = true;
			}else {
				System.out.println("ERROR! DATO INTRODUCIDO ERRONEO!");
				System.out.println(" ");
			}
		}
		introducido = false;
		while(!introducido) {
			System.out.println("Dame el tamaño de conjunto (1, 2, 4, 8 Bloques): ");
			aux = s.nextInt();
			if(aux == 1 || aux == 2 || aux == 4 || aux == 8) {
				setBqConj(aux);
				introducido = true;
			}else {
				System.out.println("ERROR! DATO INTRODUCIDO ERRONEO!");
				System.out.println(" ");
			}
		}
		introducido = false;

		//numero de conjuntos en base al tamaño de conjunto introducido
		if(getBqConj() == 1) {//si entra un bq en un conjunto y en la MC entran 8 bloques la MC tendrá 8 conjuntos
			setNumConj(8);
		}else if(getBqConj() == 2) {
			setNumConj(4);
		}else if(getBqConj() == 4) {
			setNumConj(2);
		}else if(getBqConj() == 8) {
			setNumConj(1);
		}

		while(!introducido) {
			System.out.println("Dime cual es la politica de reemplazo (FIFO(0) o LRU(1)): ");
			s.nextLine();
			aux = s.nextInt();
			if(aux == 0 || aux == 1) {
				setReemplazo(aux);
				introducido = true;
			}else {
				System.out.println("ERROR! DATO INTRODUCIDO ERRONEO!");
				System.out.println(" ");
			}
		}

		System.out.println("----------------Datos----------------");
		System.out.println("Tamaño Memoria Cache: 8 Bloques");
		System.out.println("Palabra: "+getBytePal()+" Bytes");
		System.out.println("Bloque: "+getPalBq()+" Palabras = "+getPalBq()*getBytePal()+" Bytes");
		System.out.println("Conjunto: "+getBqConj()+" Bloques = "+getPalBq()*getBytePal()*getBqConj()+" Bytes");
		System.out.println("Número de conjuntos: "+getNumConj());
		if(getReemplazo() == 0) {
			System.out.println("Politica de reemplazo: FIFO");
		}else {
			System.out.println("Politica de reemplazo: LRU");
		}	
	}

	public boolean estaEscrito(int bloque) {
		boolean encontrado = false;;
		for(int i = 0;i <= 7;i++) {
			if(cache[i][4] == bloque && cache[i][0]==1) {
				if(cache[i][1] == 1) {
					encontrado =  true;
				}
			}
		}
		return encontrado;
	}
	
	public void pedirDirOp() {
		Scanner s = new Scanner(System.in);
		boolean introducido = false;
		int op;
		System.out.println("Introduce la direccion: ");
		setDireccion(s.nextInt());
		if(getDireccion() >=0) {
		while(!introducido) {
			System.out.println("Introduce la operacion: (0)Load - (1)Store ");
			op = s.nextInt();
			if(op == 0 || op == 1) {
				setOperacion(op);
				introducido = true;
			}else {
				System.out.println("ERROR! DATO INTRODUCIDO ERRONEO!");
			}
		}
		}
	}

	public void imprimirCache() {
		//cache[6][0]=1;
		//cache[6][4]=35;
		int cont = 0;
		System.out.println("ocup    mod    tag    rem    ||    bloque");
		System.out.println("-----------------------------------");
		for(int i = 0;i<8;i++) {
			for(int j = 0;j < 5;j++) {
				if(j <= 3) {
					System.out.print("   "+cache[i][j]+"   ");
				}else{
					if(cache[i][0] == 0) {
						System.out.print("||  ---");
					}else {
						System.out.print("||  b"+cache[i][j]);
					}

				}

			}
			System.out.println();
			if(numConj == 8) {
				System.out.println("-----------------------------------");
			}else if((cont == 1 || cont == 3 || cont == 5) && (numConj == 4)) {
				System.out.println("-----------------------------------");
			}else if(numConj == 2 && cont == 3) {
				System.out.println("-----------------------------------");
			}
			cont++;
		}
		System.out.println("-----------------------------------");
	}

	public void cacheNull() {
		for(int i = 0;i<8;i++) {
			for(int j = 0;j < 5;j++) {
				cache[i][j] = 0;
			}
		}
	}

	public void busqueda(int bloque) {
		//cache[6][4]=35;
		setAcierto(false);
		for(int i = 0;i <= 7;i++) {
			if(cache[i][4] == bloque && cache[i][0]==1) {
				setAcierto(true);
				if(getOperacion() == 1) {//Store
					cache[i][1] = 1;
				}
			}
		}
		/**
		else if(numConj == 2) {
			if(conjunto == 0) {
				for(int i = 0;i <= 3;i++) {
					if(cache[i][5] == bloque) {
						setAcierto(true);
					}
				}
			}else {
				for(int i = 4;i <= 7;i++) {
					if(cache[i][5] == bloque) {
						setAcierto(true);
					}
				}
			}
		}else if(numConj == 4) {

		}
		 */
	}
	
	public void reemLRU(int bloque, int conjunto) {
		boolean encontrado = false;
		if(numConj == 8) {
			cache[bloque % 8][3]=0;
		}else if(numConj == 2) {
			if(conjunto == 0) {
				for(int i = 0;i <= 3 && !encontrado;i++) {
					if(cache[i][4] == bloque) {
						int aux = cache[i][3];
						cache[i][3]=3;
						encontrado = true;
						for(int j =0; j<=3;j++) {
							if(i!=j) {
								if(cache[j][3]!=0 && cache[j][3]>aux) {
									cache[j][3] =cache[j][3]-1;
								}
							}
						}
					}
				}
			}else{
				for(int i = 4;i <= 7 && !encontrado;i++) {
					if(cache[i][4] == bloque) {
						int aux = cache[i][3];
						cache[i][3]=3;
						encontrado = true;
						for(int j = 4; j<=7;j++) {
							if(i!=j) {
								if(cache[j][3]!=0 && cache[j][3]>aux) {
									cache[j][3] =cache[j][3]-1;
								}
							}
						}
					}
				}
			}
		}else if (numConj == 4) {
			if(conjunto == 0) {
				for(int i = 0;i <= 1 && !encontrado;i++) {
					if(cache[i][4] == bloque) {
						cache[i][3]=1;
						encontrado = true;
						for(int j = 0; j<=1;j++) {
							if(i!=j) {
								if(cache[j][3]!=0) {
									cache[j][3] =cache[j][3]-1;
								}
							}
						}
					}
				}
			}else if(conjunto == 1) {
				for(int i = 2;i <= 3 && !encontrado;i++) {
					if(cache[i][4] == bloque) {
						cache[i][3]=1;
						encontrado = true;
						for(int j = 2; j<=3;j++) {
							if(i!=j) {
								if(cache[j][3]!=0) {
									cache[j][3] =cache[j][3]-1;
								}
							}
						}
					}
				}
			}else if(conjunto == 2) {
				for(int i = 4;i <= 5 && !encontrado;i++) {
					if(cache[i][4] == bloque) {
						cache[i][3]=1;
						encontrado = true;
						for(int j = 4; j<=5;j++) {
							if(i!=j) {
								if(cache[j][3]!=0) {
									cache[j][3] =cache[j][3]-1;
								}
							}
						}
					}
				}
			}else {
				for(int i = 6;i <= 7 && !encontrado;i++) {
					if(cache[i][4] == bloque) {
						cache[i][3]=1;
						encontrado = true;
						
						for(int j = 6; j<=7;j++) {
							if(i!=j) {
								if(cache[j][3]!=0) {
									cache[j][3] =cache[j][3]-1;
								}
							}
						}
						
					}
				}
			}
		
		}else {
			for(int i = 0;i <= 7 && !encontrado;i++) {
				if(cache[i][4] == bloque) {
					int aux =cache[i][3];
					cache[i][3]=7;
					encontrado = true;
					for(int j = 0; j<=7;j++) {
						if(i!=j) {
							if(cache[j][3]!=0 && cache[j][3]>aux) {
								cache[j][3] =cache[j][3]-1;
							}
						}
					}
				}
			}
		}
	}
	
	public boolean introducir(int conjunto, int bloque, int tag) {
		boolean encontrado = false;
		boolean escrito = false;
		if(numConj == 8) {
			if (cache[bloque % 8][1] == 1) {
				escrito=true;
			}
			cache[bloque % 8][0]=1;
			cache[bloque % 8][1]=this.operacion;
			cache[bloque % 8][2]=tag;
			cache[bloque % 8][3]=0;
			cache[bloque % 8][4]=bloque;			
		}else if(numConj == 2) {
			if(conjunto == 0) {
				for(int i = 0;i <= 3 && !encontrado;i++) {
					if(cache[i][3] == 0) {
						if (cache[i][1] == 1 ) {
							escrito=true;
						}
						cache[i][0]=1;
						cache[i][1]=this.operacion;
						cache[i][2]=tag;
						cache[i][3]=3;
						cache[i][4]=bloque;
						encontrado = true;
						for(int j =0; j<=3;j++) {
							if(i!=j) {
								if(cache[j][3]!=0) {
									cache[j][3] =cache[j][3]-1;
								}
							}
						}
					}
				}
			}else{
				for(int i = 4;i <= 7 && !encontrado;i++) {
					if(cache[i][3] == 0) {
						if (cache[i][1] == 1 ) {
							escrito=true;
						}
						cache[i][0]=1;
						cache[i][1]=this.operacion;
						cache[i][2]=tag;
						cache[i][3]=3;
						cache[i][4]=bloque;
						encontrado = true;
						for(int j = 4; j<=7;j++) {
							if(i!=j) {
								if(cache[j][3]!=0) {
									cache[j][3] =cache[j][3]-1;
								}
							}
						}
					}
				}
			}
		}else if (numConj == 4) {
			if(conjunto == 0) {
				for(int i = 0;i <= 1 && !encontrado;i++) {
					if(cache[i][3] == 0) {
						if (cache[i][1] == 1 ) {
							escrito=true;
						}
						cache[i][0]=1;
						cache[i][1]=this.operacion;
						cache[i][2]=tag;
						cache[i][3]=1;
						cache[i][4]=bloque;
						encontrado = true;
						for(int j = 0; j<=1;j++) {
							if(i!=j) {
								if(cache[j][3]!=0) {
									cache[j][3] =cache[j][3]-1;
								}
							}
						}
					}
				}
			}else if(conjunto == 1) {
				for(int i = 2;i <= 3 && !encontrado;i++) {
					if(cache[i][3] == 0) {
						if (cache[i][1] == 1 ) {
							escrito=true;
						}
						cache[i][0]=1;
						cache[i][1]=this.operacion;
						cache[i][2]=tag;
						cache[i][3]=1;
						cache[i][4]=bloque;
						encontrado = true;
						for(int j = 2; j<=3;j++) {
							if(i!=j) {
								if(cache[j][3]!=0) {
									cache[j][3] =cache[j][3]-1;
								}
							}
						}
					}
				}
			}else if(conjunto == 2) {
				for(int i = 4;i <= 5 && !encontrado;i++) {
					if(cache[i][3] == 0) {
						if (cache[i][1] == 1 ) {
							escrito=true;
						}
						cache[i][0]=1;
						cache[i][1]=this.operacion;
						cache[i][2]=tag;
						cache[i][3]=1;
						cache[i][4]=bloque;
						encontrado = true;
						for(int j = 4; j<=5;j++) {
							if(i!=j) {
								if(cache[j][3]!=0) {
									cache[j][3] =cache[j][3]-1;
								}
							}
						}
					}
				}
			}else {
				for(int i = 6;i <= 7 && !encontrado;i++) {
					if(cache[i][3] == 0) {
						if (cache[i][1] == 1 ) {
							escrito=true;
						}
						cache[i][0]=1;
						cache[i][1]=this.operacion;
						cache[i][2]=tag;
						cache[i][3]=1;
						cache[i][4]=bloque;
						encontrado = true;
						
						for(int j = 6; j<=7;j++) {
							if(i!=j) {
								if(cache[j][3]!=0) {
									cache[j][3] =cache[j][3]-1;
								}
							}
						}
						
					}
				}
			}
		
		}else {
			for(int i = 0;i <= 7 && !encontrado;i++) {
				if(cache[i][3] == 0) {
					if (cache[i][1] == 1 ) {
						escrito=true;
					}
					cache[i][0]=1;
					cache[i][1]=this.operacion;
					cache[i][2]=tag;
					cache[i][3]=7;
					cache[i][4]=bloque;
					encontrado = true;
					for(int j = 0; j<=7;j++) {
						if(i!=j) {
							if(cache[j][3]!=0) {
								cache[j][3] =cache[j][3]-1;
							}
						}
					}
				}
			}
		}
		return escrito;
	}

}
