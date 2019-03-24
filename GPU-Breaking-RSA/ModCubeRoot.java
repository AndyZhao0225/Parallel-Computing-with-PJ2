import edu.rit.pj2.Task;
import edu.rit.gpu.Kernel;
import edu.rit.gpu.Module;
import edu.rit.gpu.Gpu;
import edu.rit.gpu.GpuIntVbl;
import edu.rit.gpu.GpuLongArray;
import java.util.Arrays;

/**
 * This class takes use of GPU parallel computing ability to find (a) cube root(s) m for 
 * a given integer <c> with a modular integer <n>, so that m^3 = c mod n. 
 * If no root found, the program would tell no cube root was found.
 * If roots were found more than one, they would be printed in an ascending order.
 * 
 * @author  Junan Zhao
 * @version 26-Nov-2018
 */
public class ModCubeRoot extends Task 
{ 
   private static final int BLOCK_MAX_DIM = 1024; //The max number of threads a block can carry 
   private static final int MAX_ROOTS_NUM = 3;    //The max number of possible roots
   
   /**
    * Kernel function interface.
    */
   private static interface ModCubeRootKernel extends Kernel
   {
      public void modCubeRoot( int c, int n);
   }
 
   
   /**
    * Task main program.
    */
   public void main(String[] args) throws Exception
   {
      // Validate command line arguments. 
      if(args.length!=2) usage();
      if(!isEveryCharDigit(args[0]) || !isEveryCharDigit(args[1])) usage();
      int c = Integer.parseInt(args[0]);  
      int n = Integer.parseInt(args[1]);
      if(c<0 || c>=n || n<2) usage();
      
      // Initialize GPU.
      Gpu gpu = Gpu.gpu();
      gpu.ensureComputeCapability(2,0);
      
      Module module = gpu.getModule("ModCubeRoot.ptx");
      ModCubeRootKernel kernel = module.getKernel(ModCubeRootKernel.class);
      
      GpuIntVbl arrayIndex = module.getIntVbl("devArrayIndex");  //the index indicated at the current vacant position in the answer array
      arrayIndex.item = 0;                                       //initialize with 0
      GpuLongArray answer = module.getLongArray("devAnswer",MAX_ROOTS_NUM);  //The array to store every roots   
      arrayIndex.hostToDev();
      answer.hostToDev();
      
      //Compute modular cube root
      kernel.setBlockDim(BLOCK_MAX_DIM);                //set up one-dimensional block 
      kernel.setGridDim(gpu.getMultiprocessorCount());  //set up one-dimensional grid 
      kernel.modCubeRoot(c,n);

      // Print results.
      arrayIndex.devToHost();
      answer.devToHost();
      printTheAnswer(answer,c,n,arrayIndex.item);
   }
   
   
    /**
      * Initialization at the task level
      * 
      * @param  answer       The array recorded roots
      * @param  c            The mod cube
      * @param  n            The module
      * @param  numOfRoots   How many roots the program found
      */
   public static void printTheAnswer(GpuLongArray answer, int c, int n, int numOfRoots)
   {
      if(numOfRoots==0) 
        {
           System.out.println("No cube roots of " + c + " (mod " + n + ")");
           return;
        }
      if(numOfRoots>=2) Arrays.sort(answer.item,0,numOfRoots); 
      for( int i=0; i<=numOfRoots-1; i++)
          System.out.println(answer.item[i] + "^3 = " + c +" (mod " + n + ")");     
   }
   
   
    /**
      * Check whether every character in a string is a digit. 
      * 
      * @param  text             The string to be tested
      * 
      * @return true/false       If yes, return true; else return false. 
      */
   private static boolean isEveryCharDigit(String text)
   {
      for( int i=0; i<=text.length()-1; i++)
          if(!Character.isDigit(text.charAt(i))) return false;
      return true;
   }
 
   
   /**
    * Print a usage message and exit.
    */
   private static void usage()
   {
      System.err.println("Usage: java pj2 ModCubeRoot <c> <n>");
      System.err.println("<c> is the number whose modular cube root(s) are to be found; "
                        + "it must be a decimal integer (type int) in the range 0 <= c <= n-1");
      System.err.println("<n> is the modulus; it must be a decimal integer (type int) >= 2. ");
      terminate(1);
   }

   
   /**
    * Specify that this task requires one core.
    */
   protected static int coresRequired()
   {
      return 1;
   }

   
   /**
    * Specify that this task requires one GPU accelerator.
    */
   protected static int gpusRequired()
   {
      return 1;
   }
}