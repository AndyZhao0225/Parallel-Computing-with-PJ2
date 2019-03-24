import edu.rit.io.Streamable;
import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import java.io.IOException;

/**
 * Census is a class help with storing all the necessary attributes of 
 * its corresponding (state,county) pair. 
 *
 * @author  Junan Zhao
 * @version 11-Dec-2018
 */
public class Census implements Cloneable, Streamable
{
   long[] n = new long[6]; //The array to store different racial people number 
                           //index meaning: 0-white, 1-black, 2 americanIndian, 3-asian, 4-nativeHawaiian, 5-multiRace 
   long total;             //total number of people 
   double diversityIndex;  // the county or state diversityindex 

   
   /**
    * Construct a default census object
    */
   public Census()
   {
      for( int i=0; i<=5; i++)
          n[i] = 0L;
      total = 0L;
      diversityIndex = 0;
   }
   
   
   /**
    * Construct a new census with the given array
    *
    * @param  newN   An array stored different race count 
    */
   public Census(long[] newN)
   {
      total = 0L;
      for( int i=0; i<=5; i++)
         {
            n[i] = newN[i];
            total += n[i];
         }
      diversityIndex = computeDiversityIndex();
   }

   
      /**
    * Construct a new census that is a deep copy of the given census.
    *
    * @param  census  Census to copy.
    */
   public Census(Census census)
   {
      copy(census);
   }
   
   
   //Update this census's "total" and "diversityIndex" value 
   public void update()
   {
      total = 0L;
      for( int i=0; i<=5; i++)
          total += n[i];
      diversityIndex = computeDiversityIndex();
   }
   
   
      /**
    * Compute a new diversity based on current data  
    *
    * @return    the new computed diversity index 
    */
   public double computeDiversityIndex()
   {
      double temp = 0;
      for( int i=0; i<=5; i++)
          temp += n[i]*(total - n[i]);
      return temp/(total*total*1.0);
   }

   
    /**
    * Clear this census data.
    */
   public void clear()
   {
      for( int i=0; i<=5; i++)
          n[i] = 0L;
      total = 0L;
      diversityIndex = 0;
   }

   
    /**
    * Make this census be a deep copy of the given state.
    *
    * @param  census  Census to copy.
    *
    * @return  this   This census.
    */
   public Census copy(Census census)
   { 
      for( int i=0; i<=5; i++)
          n[i] = census.n[i];
      total = census.total;
      diversityIndex = census.diversityIndex;
      return this;
   }

   
    /**
    * Create a clone of this census.
    *
    * @return  Clone.
    */
   public Object clone()
   {
      try
        {
           Census census = (Census)super.clone();
           census.copy(this);
           return census;
        }
      catch(CloneNotSupportedException exc)
        {
           throw new RuntimeException ("Shouldn't happen", exc);
        }
   }
   
   
      /**
    * Add the given census to this census. 
    * It would add all its attributes to this one.
    * census.
    *
    * @param  census  census to add.
    */
   public void add(Census census)
   {
      if(census==null) return;
      for( int i=0; i<=5; i++)
          n[i] += census.n[i];
      total += census.total;
      diversityIndex = computeDiversityIndex();
   }   

   
   /**
    * Write this census to the given out stream.
    *
    * @param  out  Out stream.
    *
    * @exception  IOException
    *     Thrown if an I/O error occurred.
    */
   public void writeOut(OutStream out) throws IOException
   {
      out.writeLongArray(n);
      out.writeLong(total);
      out.writeDouble(diversityIndex);
   }

   
   /**
    * Read this census from the given in stream.
    *
    * @param  in  In stream.
    *
    * @exception  IOException
    *     Thrown if an I/O error occurred.
    */
   public void readIn(InStream in) throws IOException
   {
      n = in.readLongArray(); 
      total = in.readLong();
      diversityIndex = in.readDouble();
   }
}