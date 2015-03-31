package gnu.expr;

import gnu.lists.AbstractFormat;
import gnu.lists.Consumer;
import gnu.lists.VoidConsumer;
import gnu.mapping.CallContext;
import gnu.mapping.Environment;
import gnu.mapping.OutPort;
import java.io.Writer;
import kawa.Shell;

public class CompiledModule
{
  Object cookie;
  Language language;
  ModuleExp mexp;
  
  public CompiledModule(ModuleExp paramModuleExp, Object paramObject, Language paramLanguage)
  {
    this.mexp = paramModuleExp;
    this.cookie = paramObject;
    this.language = paramLanguage;
  }
  
  public static CompiledModule make(Class paramClass, Language paramLanguage)
  {
    return new CompiledModule(null, paramClass, paramLanguage);
  }
  
  public void evalModule(Environment paramEnvironment, CallContext paramCallContext)
    throws Throwable
  {
    Language localLanguage = Language.setSaveCurrent(this.language);
    Environment localEnvironment = Environment.setSaveCurrent(paramEnvironment);
    try
    {
      ModuleExp.evalModule2(paramEnvironment, paramCallContext, this.language, this.mexp, this.cookie);
      return;
    }
    finally
    {
      Language.restoreCurrent(localLanguage);
      Environment.restoreCurrent(localEnvironment);
    }
  }
  
  public void evalModule(Environment paramEnvironment, OutPort paramOutPort)
    throws Throwable
  {
    localCallContext = CallContext.getInstance();
    localConsumer = localCallContext.consumer;
    boolean bool = ModuleBody.getMainPrintValues();
    localAbstractFormat = paramOutPort.objectFormat;
    if (bool) {}
    for (Object localObject1 = Shell.getOutputConsumer(paramOutPort);; localObject1 = new VoidConsumer())
    {
      localCallContext.consumer = ((Consumer)localObject1);
      try
      {
        evalModule(paramEnvironment, localCallContext);
        return;
      }
      finally
      {
        if (!(localCallContext.consumer instanceof Writer)) {
          break;
        }
        ((Writer)localCallContext.consumer).flush();
        localCallContext.consumer = localConsumer;
        paramOutPort.objectFormat = localAbstractFormat;
      }
    }
  }
  
  public Object evalToResultValue(Environment paramEnvironment, CallContext paramCallContext)
    throws Throwable
  {
    int i = paramCallContext.startFromContext();
    try
    {
      evalModule(paramEnvironment, paramCallContext);
      Object localObject = paramCallContext.getFromContext(i);
      return localObject;
    }
    catch (Throwable localThrowable)
    {
      paramCallContext.cleanupFromContext(i);
      throw localThrowable;
    }
  }
}


/* Location:           C:\Users\Bonjour\Desktop\classes_dex2jar.jar
 * Qualified Name:     gnu.expr.CompiledModule
 * JD-Core Version:    0.7.0.1
 */