import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;

/**
 * Class LemoineSmp aims at finding an odd number between a lower-bound and an upperbound, such that 
 * the odd number fulfills the Lemoine's Conjecture, and it has the largest p when written in the formula n = p + 2*q,
 * where n>5, and p and q are primes. The primes p and q might or might not be the same.
 * It's the parallel version
 * 
 *
 * @author  Junan Zhao
 * @version 27-Sep-2018
 */
public class LemoineSmp extends Task
{
   RecordVbl recoVbl;  //global varaible 

   // Main program.
   public void main(String[] args) throws Exception
   {
      // Validate command line arguments.
      if(args.length!=2) usage();
      
      //Command line arguments.
      final int LOWER_BOUND = Integer.parseInt(args[0]);
      final int UPPER_BOUND = Integer.parseInt(args[1]);
      
      //check whether the inputs are valid
      if(LOWER_BOUND<=6 || UPPER_BOUND<LOWER_BOUND || LOWER_BOUND%2==0 || UPPER_BOUND%2==0) usage();
      
      //set up Record
      recoVbl = new RecordVbl();
      
      //parallelly loop thru the lower bound to upper bound, to find the largest p 
      int temp = (UPPER_BOUND - LOWER_BOUND)/2;    //temp is used to reach the uppper bound, see the run() method
      parallelFor(0,temp).exec( new Loop()
      {
         RecordVbl thrRecoVbl;  //thread variable
                    
         public void start()
         {
            thrRecoVbl = threadLocal(recoVbl);    
         }
         
         public void run(int x)
         {
             //x is used to set up the pace 
             //lower bound could be reached when x=0
             //upper bound could be reached when x=temp
            int i = 2*x + LOWER_BOUND;    //i would only be odd numbers between the lowerbound and the upperbound
            thrRecoVbl.reco.update(i);
         }
      });
      
      printEquation(recoVbl.reco.getN(),recoVbl.reco.getP());   // print the output
   }
   
   
   /**
	 * Print the Lemoine's Conjecture formula accroding to the given n and p
	 *
	 * @param  n  the number at the left in the formula   n = p + 2*q  
         * @param  p  the prime number p in the formula       n = p + 2*q
	 *
	 */
   private void printEquation( int n, int p)
   {
      int q = (n - p)/2;
      System.out.println(n + " = " + p + " + 2*" + q);
   }
   
   
   // Print a usage message and exit.
   private static void usage()
   {
      System.err.println("Usage: java pj2 LemoineSeq <lb> <ub>");
      System.err.println("<lb> is the lower bound integer to be examined (an odd integer greater than 5, type int).");
      System.err.println("<ub> is the upper bound integer to be examined (an odd integer greater than or equal to <lb>, type int).");
      terminate (1);
   }
}