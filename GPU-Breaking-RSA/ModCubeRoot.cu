//The max possible number of roots
#define DEV_MAX_ROOTS_NUM 3

//Number of threads per block.
#define NT 1024

//The array index in global memory.
__device__ int devArrayIndex;

//The answer array stored roots in global memory
__device__ unsigned long long int devAnswer[DEV_MAX_ROOTS_NUM];

/**
 * Device kernel to find (a) cube root(s) for a given integer c with a modular n.
 * <P>
 * Called with a one-dimensional grid of one-dimensional blocks.
 *
 * @param  c        The mod cube (input).
 * @param  n        The module (input).
 *
 * @author  Junan Zhao
 * @version 26-Nov-2018
 */
extern "C" __global__ void modCubeRoot( int c, int n)
{
   //Determine number of threads and this thread's m (test number).
   unsigned long long m = blockIdx.x*NT + threadIdx.x;
   unsigned long long size = gridDim.x*NT;    
   for(; m<n; m+=size)   //use loop to cover all range of n if n is a pretty large integer cannot covered by one round
     {
        unsigned long long temp = m*m;
        temp = temp%n;
        temp = temp*m;
        temp = temp%n;
        if(c==(int)temp) //once found a root
          {             
             int oldIndex = atomicAdd(&devArrayIndex,1);
             devAnswer[oldIndex] = m; 
          }
     }
}