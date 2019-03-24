import edu.rit.pj2.Tuple;
import edu.rit.pj2.Vbl;
import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import java.io.IOException;

/**
 * This is the reduction variable version of the Class Census.
 *
 * @author  Junan Zhao
 * @version 11-Dec-2018
 */
public class CensusVbl extends Tuple implements Vbl
{
 // Exported data members.
    /**
    * The census itself.
    */
   public Census census = new Census();

   
   /**
    * Construct a new census reduction variable wrapping a null census.
    */
   public CensusVbl()
   {
      
   }

   /**
    * Construct a new census reduction variable wrapping the given
    * census.
    */
   public CensusVbl(Census census)
   {
      this.census = census;
   }

 // Exported operations.
   
      /**
    * Create a clone of this shared variable.
    *
    * @return  The cloned object.
    */
   public Object clone()
   {
      CensusVbl vbl = (CensusVbl)super.clone();
      if(this.census!=null) vbl.census = (Census)this.census.clone();
      return vbl;
   }

 
      /**
    * Set this shared variable to the given shared variable.
    *
    * @param  vbl  Shared variable.
    *
    * @exception  ClassCastException
    *     (unchecked exception) Thrown if the class of <TT>vbl</TT> is not
    *     compatible with the class of this shared variable.
    */
   public void set(Vbl vbl)
   {
      this.census.copy(((CensusVbl)vbl).census);
   }

   
   /**
    * Reduce the given census into this shared variable. This shared
    * variable's field and the state argument are
    * combined together using this shared variable's reduction operation, and
    * the result is stored in the this.state field.
    * <P>
    * Class State's reduce() method adds the given census
    * into this variable's census.
    *
    * @param  Census  census.
    */
   public void reduce(Census census)
   {
      this.census.add(census);
   }

   
    /**
    * Reduce the given shared variable into this shared variable. The two
    * variables are combined together using this shared variable's reduction
    * operation, and the result is stored in this shared variable.
    *
    * @param  vbl  Shared variable.
    *
    * @exception  ClassCastException
    *     (unchecked exception) Thrown if the class of <TT>vbl</TT> is not
    *     compatible with the class of this shared variable.
    */
   public void reduce(Vbl vbl)
   {
      reduce(((CensusVbl)vbl).census);
   }

   
      /**
    * Write this census reduction variable to the given out stream.
    *
    * @param  out  Out stream.
    *
    * @exception  IOException
    *     Thrown if an I/O error occurred.
    */
   public void writeOut(OutStream out) throws IOException
   {
      out.writeObject(census);
   }

   
      /**
    * Read this census reduction variable from the given in stream.
    *
    * @param  in  In stream.
    *
    * @exception  IOException
    *     Thrown if an I/O error occurred.
    */
   public void readIn(InStream in) throws IOException
   {
      census = (Census)in.readObject();
   }
}