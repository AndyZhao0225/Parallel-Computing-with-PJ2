import edu.rit.io.*;
import java.io.IOException;

/**
 * Class Answer is the answer to Largest Triangle problem. It records a triangle's attributes.
 * The class which created Answer object(s) is responsible to have data structure to store corresponding 
 * Points.
 *
 * @author  Junan Zhao
 * @version 20-Oct-2018
 */
public class Answer implements Cloneable,Streamable
{
   private double area;   //the area of the triangle 
   private int i;         
   private int j;         //Integer i,j,k are the indexes of the points of the triangle, in the Points array 
   private int k;         //i, j, k must be in order that i < j < k
   private double ix;
   private double iy;
   private double jx;
   private double jy;
   private double kx;
   private double ky;        //ix through ky are coordinates for the three points of the triagnle
                              //e.g. (ix,iy) are the coordinates for the first point which is indexed by i in its Points array.
   
   /**
    * Construct a new answer, with initialization
    */
   public Answer()
   {
      area = -1;
      i = -1;
      j = -1;
      k = -1;
      ix = -1;
      iy = -1;
      jx = -1;
      jy = -1;
      kx = -1;
      ky = -1;
   }

   
   /**
    * Construct a new answer that is a deep copy of the given answer.
    *
    * @param  ans  Answer to copy.
    */
   public Answer(Answer ans)
   {
      copy(ans);
   }
   
   
    /**
    * Construct a new answer with the given value.
    *
    * @param area.
    * @param i.
    * @param j.
    * @param k.
    * @param ix.
    * @param iy.
    * @param jx.
    * @param jy.
    * @param kx.
    * @param ky.
    */
   public Answer( double area, int i, int j, int k, double ix, double iy, double jx, double jy, double kx, double ky)
   {
      this.area = area;
      this.i = i;
      this.j = j;
      this.k = k;
      this.ix = ix;
      this.iy = iy;
      this.jx = jx;
      this.jy = jy;
      this.kx = kx;
      this.ky = ky;
   }
   
   
   /**
    * Clear this answer. Set the recorded data all to -1
    */
   public void clear()
   {
      area = -1;
      i = -1;
      j = -1;
      k = -1;
      ix = -1;
      iy = -1;
      jx = -1;
      jy = -1;
      kx = -1;
      ky = -1;
   }

   
   /**
    * Make this answer be a deep copy of the given answer.
    *
    * @param  ans  Answer to copy.
    *
    * @return  This answer.
    */
   public Answer copy(Answer ans)
   {
      update(ans);
      return this;
   }

   
     /**
    * Create a clone of this Answer.
    *
    * @return  Clone.
    */
   public Object clone()
   {       
      try
      {
         Answer ans = (Answer)super.clone();
         ans.copy(this);
         return ans;
      }
      catch(CloneNotSupportedException exc)
      {
         throw new RuntimeException("Shouldn't happen", exc);
      }
   }
   
   
    /**
    * Fuse the given answer to this one. The answer would update itself with the answer value who has a larger area.
    * If both answer has the same area, the method will left the answer values with the smallest first index. 
    * If both answer has the same area and first index, the method will left the answer values with the smallest second index.
    * If both answer has the same area, first index and second index , the method will left the answer values with the smallest third index.
    *
    * @param  ans  Answer to fuse.
    */
   public void fuse(Answer ans)
   {
      if(ans.getArea()>area) update(ans);
      else if(ans.getArea()==area)
        {
           if(ans.getI()<i) update(ans);
           else if(ans.getI()==i)
             {
                if(ans.getJ()<j) update(ans);
                else if(ans.getJ()==j)
                  {
                     if(ans.getK()<k) update(ans);
                  }
             }
        }  
   }

   
   /**
    * Update this answer's attributes with the given answer's attributes
    *
    * @param  ans  the given answer.
    */
   public void update(Answer ans)
   {
      area = ans.getArea();
      i = ans.getI();
      j = ans.getJ();
      k = ans.getK();
      ix = ans.getIX();
      iy = ans.getIY();
      jx = ans.getJX();
      jy = ans.getJY();
      kx = ans.getKX();
      ky = ans.getKY();
   }
   
   
   /**
    * Get area.
    *
    * @return  area.
    */
   public double getArea()
   {
      return area;
   }
   
   /**
    * Set area with a new value.
    *
    * @param area.
    */
   public void setArea(double area)
   {
      this.area = area;
   }
  
   
   /**
    * Get i.
    *
    * @return  i.
    */
   public int getI()
   {
      return i;
   }
   
   
    /**
    * Set i with a new value.
    *
    * @param i.
    */
   public void setI(int i)
   {
      this.i = i;
   }
  
   
   /**
    * Get j.
    *
    * @return  j.
    */
   public int getJ()
   {
      return j;
   }
  
   
    /**
    * Set j with a new value.
    *
    * @param j.
    */
   public void setJ(int j)
   {
      this.j = j;
   }
   
   
   /**
    * Get k.
    *
    * @return  k.
    */
   public int getK()
   {
      return k;
   }
   
   
    /**
    * Set k with a new value.
    *
    * @param k.
    */
   public void setK(int k)
   {
      this.k = k;
   }
   
   
   /**
    * Get ix.
    *
    * @return  ix.
    */
   public double getIX()
   {
      return ix;
   }
   
   
    /**
    * Set ix with a new value.
    *
    * @param ix.
    */
   public void setIX(double ix)
   {
      this.ix = ix;
   }
   
   
   /**
    * Get iy.
    *
    * @return  iy.
    */
   public double getIY()
   {
      return iy;
   }
   
   
    /**
    * Set iy with a new value.
    *
    * @param iy.
    */
   public void setIY(double iy)
   {
      this.iy = iy;
   }
  
   
   /**
    * Get jx.
    *
    * @return  jx.
    */
   public double getJX()
   {
      return jx;
   }
   
   
    /**
    * Set jx with a new value.
    *
    * @param jx.
    */
   public void setJX(double jx)
   {
      this.jx = jx;
   }
   
   
   /**
    * Get jy.
    *
    * @return  jy.
    */
   public double getJY()
   {
      return jy;
   }
   
   
    /**
    * Set jy with a new value.
    *
    * @param jy.
    */
   public void setJY(double jy)
   {
      this.jy = jy;
   }
   
   
   /**
    * Get kx.
    *
    * @return  kx.
    */
   public double getKX()
   {
      return kx;
   }
   
   
    /**
    * Set kx with a new value.
    *
    * @param kx.
    */
   public void setKX(double kx)
   {
      this.kx = kx;
   }
   
   
    /**
    * Get ky.
    *
    * @return  ky.
    */
   public double getKY()
   {
      return ky;
   }
   
   
    /**
    * Set ky with a new value.
    *
    * @param ky.
    */
   public void setKY(double ky)
   {
      this.ky = ky;
   }
   
   
    /**
    * Write this answer to the given out stream.
    *
    * @param  out  Out stream.
    *
    * @exception  IOException
    *     Thrown if an I/O error occurred.
    */
   public void writeOut(OutStream out) throws IOException
   {
      out.writeDouble(area);
      out.writeInt(i);
      out.writeInt(j);
      out.writeInt(k);
      out.writeDouble(ix);
      out.writeDouble(iy);
      out.writeDouble(jx);
      out.writeDouble(jy);
      out.writeDouble(kx);
      out.writeDouble(ky);
   }

   
   /**
    * Read this answer from the given in stream.
    *
    * @param  in  In stream.
    *
    * @exception  IOException
    *     Thrown if an I/O error occurred.
    */
   public void readIn(InStream in) throws IOException
   {
      area = in.readDouble();
      i = in.readInt();
      j = in.readInt();
      k = in.readInt();
      ix = in.readDouble();
      iy = in.readDouble();
      jx = in.readDouble();
      jy = in.readDouble();
      kx = in.readDouble();
      ky = in.readDouble();
   }
}
