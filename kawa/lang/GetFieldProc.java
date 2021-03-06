package kawa.lang;

import gnu.bytecode.ArrayClassLoader;
import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.Type;
import gnu.expr.ApplyExp;
import gnu.expr.Compilation;
import gnu.expr.Expression;
import gnu.expr.Inlineable;
import gnu.expr.Target;
import gnu.mapping.Procedure1;

public class GetFieldProc
  extends Procedure1
  implements Inlineable
{
  ClassType ctype;
  gnu.bytecode.Field field;
  
  public GetFieldProc(ClassType paramClassType, String paramString)
  {
    this.ctype = paramClassType;
    this.field = gnu.bytecode.Field.searchField(paramClassType.getFields(), paramString);
  }
  
  public GetFieldProc(ClassType paramClassType, String paramString, Type paramType, int paramInt)
  {
    this.ctype = paramClassType;
    this.field = paramClassType.getField(paramString);
    if (this.field == null) {
      this.field = paramClassType.addField(paramString, paramType, paramInt);
    }
  }
  
  public GetFieldProc(Class paramClass, String paramString)
  {
    this((ClassType)Type.make(paramClass), paramString);
  }
  
  private gnu.bytecode.Field getField()
  {
    return this.field;
  }
  
  public Object apply1(Object paramObject)
  {
    try
    {
      Object localObject = this.field.getReflectField().get(paramObject);
      return localObject;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      throw new RuntimeException("no such field " + this.field.getSourceName() + " in " + this.ctype.getName());
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      throw new RuntimeException("illegal access for field " + this.field.getSourceName());
    }
  }
  
  public void compile(ApplyExp paramApplyExp, Compilation paramCompilation, Target paramTarget)
  {
    if ((this.ctype.getReflectClass().getClassLoader() instanceof ArrayClassLoader))
    {
      ApplyExp.compile(paramApplyExp, paramCompilation, paramTarget);
      return;
    }
    paramApplyExp.getArgs()[0].compile(paramCompilation, this.ctype);
    paramCompilation.getCode().emitGetField(this.field);
    paramTarget.compileFromStack(paramCompilation, this.field.getType());
  }
  
  public Type getReturnType(Expression[] paramArrayOfExpression)
  {
    return getField().getType();
  }
}


/* Location:           C:\Users\Bonjour\Desktop\classes_dex2jar.jar
 * Qualified Name:     kawa.lang.GetFieldProc
 * JD-Core Version:    0.7.0.1
 */