import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.pj2.Tuple;
import edu.rit.pj2.Vbl;
import java.io.IOException;

/**
 * Class GraphColorVbl provides a reduction variable for a search object shared 
 * by multiple threads executing a {@linkplain edu.rit.pj2.ParallelStatement
 * ParallelStatement}. Class GraphColorVbl is a {@linkplain Tuple} wrapping an
 * instance of class {@linkplain GraphColor}, which is stored in the {@link 
 * #graphColor} field.
 *
 * @author  Junan Zhao
 * @version 21-Apr-2019
 */
public class GraphColorVbl extends Tuple implements Vbl
{
// Exported data members.
   public GraphColor graphColor = new GraphColor();  // the search state object

// Exported constructors.

   /**
    * Construct a new search state reduction variable wrapping a null search 
    * state.
    */
   public GraphColorVbl()
   {

   }

   
   /**
    * Construct a new search state reduction variable wrapping the given
    * search state.
    */
   public GraphColorVbl(GraphColor graphColor)
   {
      this.graphColor = graphColor;
   }

   
// Exported operations.

   /**
    * Create a clone of this shared variable.
    *
    * @return  The cloned object.
    */
   public Object clone()
   {
      GraphColorVbl vbl = (GraphColorVbl) super.clone();
      if(this.graphColor!=null) vbl.graphColor = (GraphColor) this.graphColor.clone();
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
      this.graphColor.copy(((GraphColorVbl)vbl).graphColor);
   }

   
   /**
    * Reduce the given graphColor into this shared variable. This shared
    * variable's {@link #graphColor graphColor} field and the 
    * <TT>graphColor</TT> argument are combined together using this shared
    * variable's reduction operation, and the result is stored in the 
    * {@link #graphColor graphColor} field.
    * <P>
    * Class GraphColorVbl's <TT>reduce()</TT> method fuses the given graphColor
    * into this variable's graphColor.
    *
    * @param  graphColor  GraphColor.
    */
   public void reduce(GraphColor graphColor)
   {
      this.graphColor.fuse(graphColor);
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
      reduce(((GraphColorVbl)vbl).graphColor);
   }

   
   /**
    * Write this search state reduction variable to the given out stream.
    *
    * @param  out  Out stream.
    *
    * @exception  IOException
    *     Thrown if an I/O error occurred.
    */
   public void writeOut(OutStream out) throws IOException
   {
      out.writeObject(graphColor);
   }

   
   /**
    * Read this search state reduction variable from the given in stream.
    *
    * @param  in  In stream.
    *
    * @exception  IOException
    *     Thrown if an I/O error occurred.
    */
   public void readIn(InStream in) throws IOException
   {
      graphColor = (GraphColor) in.readObject();
   }
}