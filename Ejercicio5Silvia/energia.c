/*
    energia.c    Version serie

    CREA LA VERSION PARALELA
    para ejecutar:   energia f1 f2 numero

    NOTA:  al compilar, incluir la opcion -lm al final
*************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <math.h>

void calcular_energias (int *partic, double *energia, int n) 
{
  int  i, j;

  for (i=0; i<n; i++)
  for (j=0; j<n; j++)
    if (i != j) energia[i] += partic[i] * partic[j] / pow ((i-j), 2);
}



void main (int argc, char *argv[])
{
  int     i, n;
  int     *partic;
  double  *energia;
  FILE    *f1, *f2;

  double  tex;
  struct timespec t0, t1, t2, t3;

  if (argc != 4) {
    printf ("\n  ERROR:  prog f1 f2 numero \n\n");
    exit (-1);
  }

  // asignar memoria a los vectores partic y energia
  n       = atoi (argv[3]);
  partic  = malloc (n * sizeof (int));
  energia = calloc (n, sizeof (double));  // inicializado con 0


  // medir el tiempo de ejecucion del programa

  clock_gettime (CLOCK_REALTIME, &t0);

  // 1 - leer los datos de las particulas del fichero 
  //   NO EJECUTAR EN PARALELO
  // ====================================================

  f1 = fopen (argv[1], "r");
  if (f1 == NULL) {
    printf ("Error al abrir el fichero %s\n", argv[1]);
    fclose (f1);
    exit (-1);
  }

  for (i=0; i<n; i++)
    fscanf (f1, "%d", &partic[i]);

  fclose (f1);

  clock_gettime (CLOCK_REALTIME, &t1);
  // 2 - calcular las energias 
  // ====================================================
 
  calcular_energias (partic, energia, n);

  clock_gettime (CLOCK_REALTIME, &t2);


  // 3 - escribir los resultados en el fichero de salida
  //   NO EJECUTAR EN PARALELO
  // ====================================================

  f2 = fopen (argv[2], "w");
  if (f2 == NULL) {
    printf ("Error al abrir el fichero %s\n", argv[1]);
    fclose (f2);
    exit (-1);
  }

  for (i=0; i<n; i++)
    fprintf (f2, "%1.4f \n", energia[i]);

  fclose (f2);

  clock_gettime (CLOCK_REALTIME, &t3);


// printf ("\n  Ejeucion paralela, %d hilos", omp_get_max_threads ());	// en el programa paralelo

  printf ("\n  Ejecucion serie");
  printf ("\n  =================\n\n");
  tex = (t1.tv_sec - t0.tv_sec) + (t1.tv_nsec - t0.tv_nsec) / (double)1e9;
  printf ("   T lectura: %8.3f ms\n", tex*1000);
  tex = (t2.tv_sec - t1.tv_sec) + (t2.tv_nsec - t1.tv_nsec) / (double)1e9;
  printf ("   T energia:  %8.3f ms\n", tex*1000);
  tex = (t3.tv_sec - t2.tv_sec) + (t3.tv_nsec - t2.tv_nsec) / (double)1e9;
  printf ("   T escritura:   %8.3f ms\n", tex*1000);
  tex = (t3.tv_sec - t0.tv_sec) + (t3.tv_nsec - t0.tv_nsec) / (double)1e9;
  printf ("   ----------------------\n");
  printf ("   T total:     %8.3f ms\n\n", tex*1000);

  printf ("  energia[0] = %1.4f  energia[%d] = %1.4f  energia[%d] = %1.4f\n\n", energia [0],
				 n/2, energia [n/2], n-1, energia [n-1]);
}

