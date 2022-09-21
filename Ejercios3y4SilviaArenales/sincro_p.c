/*
   sincro.c (SERIE)
   Media de los elementos de un vector; maximo, minimo y sus posiciones
   CREA LA VERSION PARALELA
******************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <omp.h>

#define N 100000
#define VALMAX 888
float  A[N];


void main ()
{
  int    i, minpos, maxpos, a, b;
  float  min = VALMAX, max = 0.0, suma = 0.0, media;
  
 float maxL=0, minL=0, sumaL=0, minLpos=0, maxLpos=0;
 struct timespec  t0, t1;
  double  tex;
 

  // Valores iniciales - no paralelizar
  for (i=0; i<N; i++) A[i] = (rand () % 670 + rand() % 133) % VALMAX ; 
  printf ("\n Valores iniciales  A[N/2] = %1.2f\n", A[N/2]);

  for (i=0; i<N; i++) {
    suma += A[i];
    if (A[i] > max)  { max = A[i]; maxpos = i; }
    if (A[i] < min)  { min = A[i]; minpos = i; }
  }

  media = suma / N;

  printf("Ejecución seriada...\n");
  printf ("\n Min: %6.2f  pos: %5d", min, minpos);
  printf ("\n Max: %6.2f  pos: %5d", max, maxpos);
  printf ("\n Media: %1.2f\n", media);
  printf ("\n Al finalizar  A[N/2] = %1.2f\n", A[N/2]);
  printf("-----------------------------------------------------------\n");

  media = 0;

  clock_gettime (CLOCK_REALTIME, &t0);

  // codigo a ejecutar en paralelo
  // =============================

#pragma omp parallel private(i,sumaL) shared(A,maxL,minL,maxLpos,minLpos)
{
  for (i=0; i<N; i++)
  {
    sumaL += A[i];
    if (A[i] > maxL)  { maxL = A[i]; maxLpos = i; }
    if (A[i] < minL)  { minL = A[i]; minLpos = i; }
  }
}

#pragma omp barrier

suma = suma + sumaL;

media = suma / N;

if(maxL<max) max=maxL;
if(minL>min) min= minL;

if (minpos < maxpos) { 
   a = minpos; 
   b = maxpos;
}
else { 
  a = maxpos; 
  b = minpos;
}


#pragma omp parallel for private(i) shared(A,media)
  for (i=a; i<b; i++){
    A[i] = A[i] - media;
}


  
  clock_gettime (CLOCK_REALTIME, &t1);
  tex = (t1.tv_sec - t0.tv_sec) + (t1.tv_nsec - t0.tv_nsec) / (double)1e9;

  printf ("\n Min: %6.2f  pos: %5d", min, minpos);
  printf ("\n Max: %6.2f  pos: %5d", max, maxpos);
  printf ("\n Media: %1.2f\n", media);
  printf ("\n Al finalizar  A[N/2] = %1.2f\n", A[N/2]);

  printf ("\n Tex paralelo = %1.3f ms\n", tex*1000);
  printf ("Threads:(%d)\n", omp_get_num_threads());
}

