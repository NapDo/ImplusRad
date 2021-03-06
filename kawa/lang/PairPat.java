package kawa.lang;

import gnu.lists.Consumer;
import gnu.lists.Pair;
import gnu.text.Printable;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class PairPat
  extends Pattern
  implements Printable, Externalizable
{
  Pattern car;
  private int car_count;
  Pattern cdr;
  private int cdr_count;
  
  public PairPat() {}
  
  public PairPat(Pattern paramPattern1, Pattern paramPattern2)
  {
    this.car = paramPattern1;
    this.cdr = paramPattern2;
    this.car_count = paramPattern1.varCount();
    this.cdr_count = paramPattern2.varCount();
  }
  
  public static PairPat make(Pattern paramPattern1, Pattern paramPattern2)
  {
    return new PairPat(paramPattern1, paramPattern2);
  }
  
  public boolean match(Object paramObject, Object[] paramArrayOfObject, int paramInt)
  {
    if (!(paramObject instanceof Pair)) {}
    Pair localPair;
    do
    {
      return false;
      localPair = (Pair)paramObject;
    } while ((!this.car.match(localPair.getCar(), paramArrayOfObject, paramInt)) || (!this.cdr.match(localPair.getCdr(), paramArrayOfObject, paramInt + this.car_count)));
    return true;
  }
  
  public void print(Consumer paramConsumer)
  {
    paramConsumer.write("#<pair-pattern car: ");
    this.car.print(paramConsumer);
    paramConsumer.write(" cdr: ");
    this.cdr.print(paramConsumer);
    paramConsumer.write(62);
  }
  
  public void readExternal(ObjectInput paramObjectInput)
    throws IOException, ClassNotFoundException
  {
    this.car = ((Pattern)paramObjectInput.readObject());
    this.cdr = ((Pattern)paramObjectInput.readObject());
  }
  
  public int varCount()
  {
    return this.car_count + this.cdr_count;
  }
  
  public void writeExternal(ObjectOutput paramObjectOutput)
    throws IOException
  {
    paramObjectOutput.writeObject(this.car);
    paramObjectOutput.writeObject(this.cdr);
  }
}


/* Location:           C:\Users\Bonjour\Desktop\classes_dex2jar.jar
 * Qualified Name:     kawa.lang.PairPat
 * JD-Core Version:    0.7.0.1
 */