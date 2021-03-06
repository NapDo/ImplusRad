package gnu.mapping;

import java.lang.ref.WeakReference;

class SymbolRef
  extends WeakReference
{
  SymbolRef next;
  
  SymbolRef(Symbol paramSymbol, Namespace paramNamespace)
  {
    super(paramSymbol);
  }
  
  Symbol getSymbol()
  {
    return (Symbol)get();
  }
  
  public String toString()
  {
    return "SymbolRef[" + getSymbol() + "]";
  }
}


/* Location:           C:\Users\Bonjour\Desktop\classes_dex2jar.jar
 * Qualified Name:     gnu.mapping.SymbolRef
 * JD-Core Version:    0.7.0.1
 */