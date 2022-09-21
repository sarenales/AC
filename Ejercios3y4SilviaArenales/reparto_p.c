/*
    reparto.c (SERIE)
    Analizar el reparto de iteraciones
    COMPLETAR LA VERSION PARALELA MAS EFICIENTE
    Hilos: 2, 4, 8, 16, 32, 64

    NOTA:  al compilar, incluir la opcion -lm al final
***************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>
#include <math.h>
#include <omp.h>

#define MAYOR 200
#define N1 1000
#define N2 1000000

int    A[N1], B[N1], C[N1];
float  D[N2];


int calcular (int n)
{
  usleep (500*n); // simula la carga de trabajo: n microsegundos
  return (1);
}


void main ()
{
  int  i, k = 0, tid = -1;
  int  total = 0;

  struct timespec  t0, t1;
  double  tex;


  // valores iniciales, no paralelizar
  for (i=0; i<N1; i++) {
    A[i] = 1;
    if ((rand()%100 < 5) && (i>N1/2) && (i<N1*3/4)) {
      A[i] = MAYOR;
      k++;
    }
  }
  for (i=0; i<N2; i++)  D[i] = 1.0 + rand () % 10;


  // ejecucion del primer bucle
  // ==============================

  clock_gettime (CLOCK_REALTIME, &t0);

#pragma omp parallel for private(i) shared(D)

  for (i=0; i<N2; i++){
    D[i] = (exp (D[i]) - exp (1/D[i])) / sin (D[i]);
}
  clock_gettime (CLOCK_REALTIME, &t1);
  tex = (t1.tv_sec - t0.tv_sec) + (t1.tv_nsec - t0.tv_nsec) / (double)1e9;

  printf ("\n D[100] = %1.3f\n --  Tex_1 (pararelo) = %1.3f ms\n", D[100], tex*1000);


  // ejecucion del segundo bucle
  // ==============================

  clock_gettime (CLOCK_REALTIME, &t0);

#pragma omp parallel for private(i,tid) shared(B,C) schedule(runtime)
tid=omp_get_thread_num();
  for (i=0; i<N1; i++) {
   // tid=omp_get_thread_num();
    B[i] = calcular (A[i]);
    C[i] = tid; 
  }
  clock_gettime (CLOCK_REALTIME, &t1);
  tex = (t1.tv_sec - t0.tv_sec) + (t1.tv_nsec - t0.tv_nsec) / (double)1e9;

  printf ("\n %d iteraciones largas\n", k);
  for (i=0; i<N1; i++) if (A[i] == MAYOR) printf (" %d > hilo %2d\n", i, C[i]);
  printf ("\n");
  for (i=0; i<N1; i++) total += B[i];
  printf (" Total = %d\n -- Tex_2 (paralelo) = %1.3f ms\n\n", total, tex*1000);
}

