package gnu.lists;

public class ImmutablePair
  extends Pair
{
  public ImmutablePair() {}
  
  public ImmutablePair(Object paramObject1, Object paramObject2)
  {
    this.car = paramObject1;
    this.cdr = paramObject2;
  }
  
  public void setCar(Object paramObject)
  {
    throw new UnsupportedOperationException("cannot modify read-only pair");
  }
  
  public void setCdr(Object paramObject)
  {
    throw new UnsupportedOperationException("cannot modify read-only pair");
  }
}


/* Location:           C:\Users\Bonjour\Desktop\classes_dex2jar.jar
 * Qualified Name:     gnu.lists.ImmutablePair
 * JD-Core Version:    0.7.0.1
 */