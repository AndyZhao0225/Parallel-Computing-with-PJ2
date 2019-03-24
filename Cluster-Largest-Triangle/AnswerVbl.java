import edu.rit.pj2.Vbl;
import edu.rit.io.*;
import edu.rit.pj2.Tuple;
import java.io.IOException;

/**
 * Class AnswerVbl is the reduction variable, wrapped class for Answer.
 * Class AnswerVbl supports the parallel reduction pattern. 
 * 
 * @author  Junan Zhao
 * @version 20-Oct-2018
 */
public class AnswerVbl extends Tuple implements Vbl
{
   public Answer ans;  // the inner answer itself  
    
   
   /**
    * Construct a new answer reduction variable wrapping a new answer.
    */
   public AnswerVbl()
   {
      ans = new Answer();
   }
   
   
   /**
    * Construct a new answer reduction variable wrapping a given answer.
    */
   public AnswerVbl(Answer ans)
   {
      this.ans = ans;
   }
   
   
   /**
    * Create a clone of this shared variable.
    *
    * @return  The cloned object.
    */
   public Object clone()
   {
      AnswerVbl vbl = (AnswerVbl)super.clone();
      if(this.ans!=null) vbl.ans = (Answer)this.ans.clone();
      return vbl;
   }
 
   
   /**
    * Set this shared variable to the given shared variable.
    *
    * @param  vbl  Shared variable.
    *
    * @exception  ClassCastException
    *     (unchecked exception) Thrown if the class of vbl is not
    *     compatible with the class of this shared variable.
    */ 
   public void set(Vbl vbl)
   {
      this.ans.copy(((AnswerVbl)vbl).ans);
   }
   

   /**
    * Reduce the given shared variable into this shared variable. The two
    * variables are combined together using this shared variable's reduction
    * operation, and the result is stored in this shared variable.
    *
    * @param  vbl  Shared variable.
    */
   public void reduce(Vbl vbl)
   {
      reduce(((AnswerVbl)vbl).ans);
   }
   
   
   /**
    * Reduce the given answer into this shared variable.
    * 
    * @param  ans  the answer to be reduced.
    */
   public void reduce(Answer ans)
   {
      this.ans.fuse(ans);
   }   
   
   
    /**
    * Read this answer reduction variable from the given in stream.
    *
    * @param  in  In stream.
    *
    * @exception  IOException
    *     Thrown if an I/O error occurred.
    */
   public void readIn(InStream in) throws IOException 
   { 
      ans = (Answer)in.readObject();
   }
   
   
   /**
    * Write this answer reduction variable to the given out stream.
    *
    * @param  out  Out stream.
    *
    * @exception  IOException
    *     Thrown if an I/O error occurred.
    */
   public void writeOut(OutStream out) throws IOException 
   { 
      out.writeObject(ans);   
   }
}
