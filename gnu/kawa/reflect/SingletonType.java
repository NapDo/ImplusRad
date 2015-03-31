package gnu.kawa.reflect;

import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.ObjectType;
import gnu.bytecode.Type;
import gnu.mapping.Values;

public class SingletonType
  extends ObjectType
{
  static final SingletonType instance = new SingletonType("singleton");
  
  public SingletonType(String paramString)
  {
    super(paramString);
  }
  
  public static Object coerceToSingleton(Object paramObject)
  {
    if ((paramObject instanceof Values)) {
      paramObject = ((Values)paramObject).canonicalize();
    }
    if ((paramObject == null) || ((paramObject instanceof Values))) {
      throw new ClassCastException("value is not a singleton");
    }
    return paramObject;
  }
  
  public static final SingletonType getInstance()
  {
    return instance;
  }
  
  public Object coerceFromObject(Object paramObject)
  {
    return coerceToSingleton(paramObject);
  }
  
  public int compare(Type paramType)
  {
    int i = -1;
    int j = OccurrenceType.itemCountRange(paramType);
    int k = j & 0xFFF;
    int m = j >> 12;
    if ((m == 0) || (k > 1)) {
      i = -3;
    }
    int n;
    do
    {
      return i;
      if ((k == 1) && (m == 1)) {
        return Type.pointer_type.compare(paramType);
      }
      n = Type.pointer_type.compare(paramType);
    } while ((n == 0) || (n == i));
    return -2;
  }
  
  public void emitCoerceFromObject(CodeAttr paramCodeAttr)
  {
    paramCodeAttr.emitInvokeStatic(ClassType.make("gnu.kawa.reflect.SingletonType").getDeclaredMethod("coerceToSingleton", 1));
  }
  
  public Type getImplementationType()
  {
    return Type.pointer_type;
  }
  
  public Class getReflectClass()
  {
    return getImplementationType().getReflectClass();
  }
  
  public boolean isInstance(Object paramObject)
  {
    return (paramObject != null) && (!(paramObject instanceof Values));
  }
}


/* Location:           C:\Users\Bonjour\Desktop\classes_dex2jar.jar
 * Qualified Name:     gnu.kawa.reflect.SingletonType
 * JD-Core Version:    0.7.0.1
 */