import edu.rit.pj2.Vbl;
/**
 * Class RecordVbl is the reduction variable, wrapped class for Record.
 * Class RecordVbl supports the parallel reduction pattern. 
 * 
 *
 * @author  Junan Zhao
 * @version 27-Sep-2018
 */
public class RecordVbl implements Vbl
{
   public Record reco; // the inner record itsef

   /**
    * Construct a new record reduction variable wrapping a new record.
    */
   public RecordVbl()
   {
      reco = new Record(); 
   }

   
   
   /**
    * Construct a new record reduction variable wrapping a given record.
    */
   public RecordVbl(Record reco)
   {
      this.reco = reco;
   }

   
   
   /**
    * Create a clone of this shared variable.
    *
    * @return  The cloned object.
    */
    public Object clone()
    {
      try
      {
         RecordVbl vbl = (RecordVbl)super.clone();
         if(this.reco!=null) vbl.reco = (Record)this.reco.clone();
         return vbl;
      }
      catch(CloneNotSupportedException exc)
      {
         throw new RuntimeException ("Shouldn't happen", exc);
      }
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
      this.reco.copy(((RecordVbl)vbl).reco);
   }

   
   
   /**
    * Reduce the given record into this shared variable.
    * 
    * @param  reco  the record to be reduced.
    */
   public void reduce(Record reco)
   {
      this.reco.fuse(reco);
   }

   
   
   /**
    * Reduce the given shared variable into this shared variable. The two
    * variables are combined together using this shared variable's reduction
    * operation, and the result is stored in this shared variable.
    *
    * @param  vbl  Shared variable.
    *
    * @exception  ClassCastException
    *     (unchecked exception) Thrown if the class of vbl is not
    *     compatible with the class of this shared variable.
    */
   public void reduce(Vbl vbl)
   {
      reduce (((RecordVbl)vbl).reco);
   }
}
