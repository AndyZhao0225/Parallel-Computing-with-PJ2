import edu.rit.pj2.Task;
/**
 * Class LemoineSmp aims at finding an odd number between a lower-bound and an upperbound, such that 
 * the odd number fulfills the Lemoine's Conjecture, and it has the largest p when written in the formula n = p + 2*q,
 * where n>5, and p and q are primes. The primes p and q might or might not be the same.
 * It's the sequential version
 * 
 *
 * @author  Junan Zhao
 * @version 27-Sep-2018
 */
public class LemoineSeq extends Task
{  
   Record reco;   //the record to record p and n

   //main method
   public void main(String[] args) throws Exception
   { 
       // Validate command line arguments.
      if(args.length!=2) usage();
      
      //Command line arguments.
      int lowerBound = Integer.parseInt(args[0]);
      int upperBound = Integer.parseInt(args[1]);
      
      //check whether the inputs are valid
      if(lowerBound<=6 || upperBound<lowerBound || lowerBound%2==0 || upperBound%2==0) usage();
      
      //set up Record
      reco = new Record();
      
      //loop thru the lowerbound to upperbound, to find the largest p
      for( int i=lowerBound; i<=upperBound; i=i+2)
          reco.compareNewNum(i);
      
      printEquation(reco.getN(),reco.getP());   // print the output
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

   
   // Specify that this task requires one core.
   protected static int coresRequired()
   {
      return 1;
   }
}