# Introduction #

From the source package or a SVN checkout you can compile the sources and execute them.
On Unix like systems use:
  * run.sh
On Windows like systems use:
  * run.bat

# Details #

## ShowTree ##
  * Unix/Linux: './run.sh ShowTree -k=5'
  * Windows: 'run ShowTree -k=5'

Shows an example of a genealogic tree for k leaves, generated using the basic coalescent.

## CreateMicsatInput ##

  * Unix/Linux: './run.sh CreateMicsatInput -k=50 -N=50000 -u=1e-5 -mrca="a(10)-g(10)-a(10)-g(10)"'
  * Windows: 'run CreateMicsatInput -k=50 -N=50000 -u=1e-5 -mrca="a(10)-g(10)-a(10)-g(10)"'

Creates an Input file for Micsat like:
```
10 8 10 12 
10 7 9 12 
10 9 10 12 
10 8 10 12 
10 8 10 12 
10 8 10 12 
10 8 10 12 
10 8 10 12 
11 8 10 12 
11 8 10 12 
10 8 10 12 
10 8 10 12 
11 8 10 12 
11 8 10 12 
10 8 10 12 
10 8 10 12 
10 7 9 12 
10 8 11 12 
10 8 11 12 
10 8 10 12 
10 8 9 12 
10 8 10 12 
10 7 9 12 
10 8 9 12 
10 8 10 12 
11 8 10 12 
9 8 10 12 
10 8 8 12 
9 9 10 12 
10 8 10 12 
10 8 9 12 
11 8 10 12 
10 8 10 12 
10 8 8 12 
10 9 10 12 
11 8 10 12 
11 8 10 12 
10 8 10 12 
11 7 9 12 
11 10 10 9 
10 9 10 12 
11 10 10 9 
10 8 10 12 
10 8 11 12 
10 8 10 12 
10 8 10 12 
10 8 10 12 
10 7 9 12 
10 9 10 12 
11 10 10 9 
```

## CreateSampleConfiguration ##
  * Unix/Linux: './run.sh CreateSampleConfiguration -k=10 -N=50000 -u=1e-5 -mrca="a(10)-g(10)-a(10)-g(10)"'
  * Windows: 'run CreateSampleConfiguration -k=10 -N=50000 -u=1e-5 -mrca="a(10)-g(10)-a(10)-g(10)"'

Creates a sample configuration like:
```
aaaaaaaaaa[6, 11] gggggggggg[] aaaaaaaaaaa[12] gggggggggg[7, 8] 
aaaaaaaa[1, 2] gggggggggg[3, 4] aaaaaaaaaaa[5] gggggggggg[] 
aaaaaaaaa[1, 2, 9] gggggggggg[3, 4] aaaaaaaaaaa[5] gggggggggg[] 
aaaaaaaa[1, 2] gggggggggg[3, 4] aaaaaaaaaaa[5] gggggggggg[] 
aaaaaaaaaaa[6, 13, 14] ggggggggggg[15] aaaaaaaaaaa[16] gggggggggg[7, 8] 
aaaaaaaaa[1, 2, 9] gggggggggg[3, 4] aaaaaaaaaaa[5] gggggggggg[] 
aaaaaaaaa[6] gggggggggg[] aaaaaaaaaaa[10] gggggggggg[7, 8] 
aaaaaaaaa[6] gggggggggg[] aaaaaaaaa[18] gggggggggg[7, 8] 
aaaaaaaaaa[6, 17] gggggggggg[] aaaaaaaaaa[] gggggggggg[7, 8] 
aaaaaaaaa[6] gggggggggg[] aaaaaaaaaa[] gggggggggg[7, 8] 

Haplotypes by Length (locus)
1:aaaaaaaaaa-gggggggggg-aaaaaaaaaaa-gggggggggg
3:aaaaaaaa-gggggggggg-aaaaaaaaaaa-gggggggggg
4:aaaaaaaaa-gggggggggg-aaaaaaaaaaa-gggggggggg
1:aaaaaaaaaaa-ggggggggggg-aaaaaaaaaaa-gggggggggg
1:aaaaaaaaa-gggggggggg-aaaaaaaaa-gggggggggg

Haplotypes by Length (multilocus)
1:aaaaaaaaaa-gggggggggg-aaaaaaaaaaa-gggggggggg
2:aaaaaaaa-gggggggggg-aaaaaaaaaaa-gggggggggg
3:aaaaaaaaa-gggggggggg-aaaaaaaaaaa-gggggggggg
1:aaaaaaaaaaa-ggggggggggg-aaaaaaaaaaa-gggggggggg
1:aaaaaaaaa-gggggggggg-aaaaaaaaa-gggggggggg
1:aaaaaaaaaa-gggggggggg-aaaaaaaaaa-gggggggggg
1:aaaaaaaaa-gggggggggg-aaaaaaaaaa-gggggggggg

Haplotypes by Identity
1:$a(10);6,11|0.5940976530835154-g(10);-a(11);12|0.9100800187640978-g(10);7,8|1.3461155510880674
2:$a(8);1,2|1.4163640083482751-g(10);3,4|1.6833704657844222-a(11);5|2.110117279855567-g(10);
2:$a(9);1,2,9|0.562640611545028-g(10);3,4|1.6833704657844222-a(11);5|2.110117279855567-g(10);
1:$a(11);6,13,14|0.5570636385962895-g(11);15|0.39260647602713905-a(11);16|0.6791597699270564-g(10);7,8|1.3461155510880674
1:$a(9);6|1.3833044717635907-g(10);-a(11);10|1.1822827885784961-g(10);7,8|1.3461155510880674
1:$a(9);6|1.3833044717635907-g(10);-a(9);18|0.08695044191513401-g(10);7,8|1.3461155510880674
1:$a(10);6,17|0.15695520549500058-g(10);-a(10);-g(10);7,8|1.3461155510880674
1:$a(9);6|1.3833044717635907-g(10);-a(10);-g(10);7,8|1.3461155510880674
```

## MicrosatelliteExperiment ##
  * Unix/Linux: './run.sh MicrosatelliteExperiment -k=50 -N=50000 -u=1e-5 -mrca="a(10)-g(10)-a(10)-g(10)"'
  * Windows: 'run MicrosatelliteExperiment -k=50 -N=50000 -u=1e-5 -mrca="a(10)-g(10)-a(10)-g(10)"'

Runs a microsatellite experiment and print settings, stats and results (this may take a while):
```
0 [main] INFO mx.unam.ecologia.gye.coalescence.app.MicrosatelliteExperiment  - Initializing.
7 [main] INFO mx.unam.ecologia.gye.coalescence.app.MicrosatelliteExperiment  - SimulationParameters {k=50,N=50000,u=1.0E-5,theta=1.0,p=0.5,#MC Samples=2000,delfloor=2,mrca=aaaaaaaaaa-gggggggggg-aaaaaaaaaa-gggggggggg,haploid,multilocus,in-memory}
8 [main] INFO mx.unam.ecologia.gye.coalescence.app.MicrosatelliteExperiment  - Using theta=2*N*u
9 [main] INFO mx.unam.ecologia.gye.coalescence.app.MicrosatelliteExperiment  - Running Simulations.
###################153256 [main] INFO mx.unam.ecologia.gye.coalescence.app.MicrosatelliteExperiment  - Finished Simulations in 153 seconds.
153285 [main] INFO mx.unam.ecologia.gye.coalescence.app.MicrosatelliteExperiment  - Genealogy Expected Height     = 2.0
153286 [main] INFO mx.unam.ecologia.gye.coalescence.app.MicrosatelliteExperiment  - Genealogy Expected Height Var = 1.159
153286 [main] INFO mx.unam.ecologia.gye.coalescence.app.MicrosatelliteExperiment  - Genealogy Height Mean     = 1.9772803363025588
153287 [main] INFO mx.unam.ecologia.gye.coalescence.app.MicrosatelliteExperiment  - Genealogy Height Variance = 1.1490239285092378
153293 [main] INFO mx.unam.ecologia.gye.coalescence.app.MicrosatelliteExperiment  - Genealogy Expected TBL     = 8.978477340659357
153294 [main] INFO mx.unam.ecologia.gye.coalescence.app.MicrosatelliteExperiment  - Genealogy Expected TBL Var = 6.579
153294 [main] INFO mx.unam.ecologia.gye.coalescence.app.MicrosatelliteExperiment  - Genealogy TBL Mean = 9.010277600554303
153296 [main] INFO mx.unam.ecologia.gye.coalescence.app.MicrosatelliteExperiment  - Genealogy TBL Variance = 6.371071683196891
153312 [main] INFO mx.unam.ecologia.gye.coalescence.app.MicrosatelliteExperiment  - Number of Mutations Mean = 18.143
153313 [main] INFO mx.unam.ecologia.gye.coalescence.app.MicrosatelliteExperiment  - Number of Mutations Var  = 44.00855527763946
beta,N,k,u,p,NHL,NHLV,NEL,NELV,HEL,HELV,NHM,NHMV,NEM,NEMV,HEM,HEMV,NHI,NHIV,NEI,NEIV,HEI,HEIV,PHL,PHLV,PCL,PCLV,PHM,PHMV,PCM,PCMV,PHS,PHSV,PCS,PCSV,MFAL,MFAM,SHNH,SHNHV,MASHNH,MASHNHV,SASHNH,SASHNHV
-1.0,50000,50,1.0E-5,0.5,5.118500,1.757336,3.184306,0.907552,0.669727,0.012167,9.538000,5.391252,4.892875,2.806074,0.782813,0.009348,10.926500,6.540368,5.404326,3.526591,0.804104,0.008321,0.398100,0.034736,0.396930,0.038019,0.092897,0.015148,0.085080,0.012254,0.335181,0.033680,0.346050,0.038474,0.629184,0.748680,0.516567,0.017074,0.447990,0.018607,0.122683,0.010655
```

## RunExperiments ##

Will allow you to vary some parameters and run several experiments in a row.
e.g.:
```
./run.sh RunExperiments \
  -k=100,200,500 \
  -N=10000,50000,100000 \
  -u=1e-3,1e-4,1e-5,1e-6,1e-7,8e-5,6e-5,4e-5,2e-5,8e-6,6e-6,4e-6,2e-6,8e-7,6e-7,4e-7,2e-7 \
  -s=2000 \
  -output="cembroides_exp_1.csv" \
  -mrca="a(10)-a(6)-g(7)-t(10)-t(10)-t(10)"
```

