package gnu.expr;

import gnu.bytecode.ArrayType;
import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.PrimType;
import gnu.bytecode.Type;
import gnu.kawa.reflect.OccurrenceType;
import gnu.mapping.Values;

public class StackTarget
  extends Target
{
  Type type;
  
  public StackTarget(Type paramType)
  {
    this.type = paramType;
  }
  
  static boolean compileFromStack0(Compilation paramCompilation, Type paramType1, Type paramType2)
  {
    if (paramType2 == paramType1) {}
    label94:
    label106:
    for (;;)
    {
      return true;
      CodeAttr localCodeAttr = paramCompilation.getCode();
      if (paramType1.isVoid())
      {
        paramCompilation.compileConstant(Values.empty);
        paramType1 = Type.pointer_type;
        if (!(paramType1 instanceof ArrayType)) {
          break label94;
        }
        if ((paramType2 == Type.pointer_type) || ("java.lang.Cloneable".equals(paramType2.getName()))) {
          continue;
        }
      }
      for (;;)
      {
        if (!CodeAttr.castNeeded(paramType1.getImplementationType(), paramType2.getImplementationType())) {
          break label106;
        }
        return false;
        if ((!(paramType1 instanceof PrimType)) || (!(paramType2 instanceof PrimType))) {
          break;
        }
        localCodeAttr.emitConvert(paramType1, paramType2);
        return true;
        paramType2.emitConvertFromPrimitive(paramType1, localCodeAttr);
        paramType1 = localCodeAttr.topType();
      }
    }
  }
  
  public static void convert(Compilation paramCompilation, Type paramType1, Type paramType2)
  {
    if (!compileFromStack0(paramCompilation, paramType1, paramType2)) {
      emitCoerceFromObject(paramType2, paramCompilation);
    }
  }
  
  protected static void emitCoerceFromObject(Type paramType, Compilation paramCompilation)
  {
    CodeAttr localCodeAttr = paramCompilation.getCode();
    if ((paramType instanceof OccurrenceType))
    {
      paramCompilation.compileConstant(paramType, Target.pushObject);
      localCodeAttr.emitSwap();
      localCodeAttr.emitInvokeVirtual(ClassType.make("gnu.bytecode.Type").getDeclaredMethod("coerceFromObject", 1));
      return;
    }
    paramCompilation.usedClass(paramType);
    paramType.emitCoerceFromObject(localCodeAttr);
  }
  
  public static Target getInstance(Type paramType)
  {
    if (paramType.isVoid()) {
      return Target.Ignore;
    }
    if (paramType == Type.pointer_type) {
      return Target.pushObject;
    }
    return new StackTarget(paramType);
  }
  
  public void compileFromStack(Compilation paramCompilation, Type paramType)
  {
    if (!compileFromStack0(paramCompilation, paramType)) {
      emitCoerceFromObject(this.type, paramCompilation);
    }
  }
  
  protected boolean compileFromStack0(Compilation paramCompilation, Type paramType)
  {
    return compileFromStack0(paramCompilation, paramType, this.type);
  }
  
  public Type getType()
  {
    return this.type;
  }
}


/* Location:           C:\Users\Bonjour\Desktop\classes_dex2jar.jar
 * Qualified Name:     gnu.expr.StackTarget
 * JD-Core Version:    0.7.0.1
 */