/**
 * Class Record is the class that's used to record the largest p found and corresponding n 
 * during the iteration from lower-bound to upper-bound. 
 * 
 *
 * @author  Junan Zhao
 * @version 27-Sep-2018
 */
public class Record implements Cloneable
{
   private int largestP;   //the largest p found 
   private int nRec;      //the n corresponding to the largest p

   /**
    * Construct a new record, with initialization
    */
   public Record()
   {
      nRec = -1;
      largestP = -1;
   }

   
   
   /**
    * Construct a new record that is a deep copy of the given record.
    *
    * @param  reco  Record to copy.
    */
   public Record(Record reco)
   {
      copy(reco);
   }

   
   
   /**
    * Clear this record. Set the recorded data all to -1
    */
   public void clear()
   {
      nRec = -1;
      largestP = -1;
   }

   
   
   /**
    * Make this record be a deep copy of the given record.
    *
    * @param  reco  Record to copy.
    *
    * @return  This record.
    */
   public Record copy(Record reco)
   {
      this.nRec = reco.getN();
      this.largestP = reco.getP();
      return this;
   }
  
   
   
     /**
    * Create a clone of this record.
    *
    * @return  Clone.
    */
   public Object clone()
   {
      try
      {
         Record reco = (Record)super.clone();
         reco.copy(this);
         return reco;
      }
      catch(CloneNotSupportedException exc)
      {
         throw new RuntimeException ("Shouldn't happen", exc);
      }
   }

   
   
   /**
    * Given a new number, update the largestP and nRec if necessary.
    *
    * @param  n  the new number.
    *
    *  @see  #compareNewNum(int)
    */
   public void update(int n)
   {
      compareNewNum(n);
   }  
      
   
   
    /**
    * Fuse the given record to this one. The result would left the largestP with larger value
    * and corresponding nRec. If both largestP are of the same value, left nRec with larger value 
    *
    * @param  reco  Record to fuse.
    *
    */
   public void fuse(Record reco)
   {
      if(reco.getP()>largestP)
        {
           largestP = reco.getP();
           nRec = reco.getN();
        }
      else if(reco.getP()==largestP)
        {
           if(reco.getN()>nRec) nRec = reco.getN();
        }
   }


   
    /**
    * Given a new number n, 
    * compare its p and n value with largestP and nRec.
    * Update largestP and nRec if necessary
    * 
    * @param  n  the new number.
    */
   protected void compareNewNum( int n)
   {
      int p = findTheSmallestPForAnOdd(n);
      if(p>=largestP)
        {
           nRec = n;
           largestP = p;
        } 
   }   
   
   
   
   /**
    * Given a new number n, compute its smallest p value in the formula n = p + 2*q
    * 
    * @param  n  the number.
    * 
    * 
    * @return  the smallestP for input number
    */
   private static int findTheSmallestPForAnOdd( int n)
   {
      Prime.Iterator primeIterator = new Prime.Iterator();
      int p = -1;
      while(true)
        {
           p = primeIterator.next();
           int temp = n - p;
           if(temp%2==0)
             {
                if(Prime.isPrime(temp/2)) break;
             }
        }
      
      return p;
   }  
   
   
   //get the nRec
   public int getN()
   {
      return nRec;
   }
   
   
   //get the largestP
   public int getP()
   {
      return largestP;
   }
}