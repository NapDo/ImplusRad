package kawa.lang;

import gnu.expr.Compilation;
import gnu.expr.Language;
import gnu.expr.ModuleExp;
import gnu.expr.NameLookup;
import gnu.lists.LList;
import gnu.lists.PairWithPosition;
import gnu.mapping.CallContext;
import gnu.mapping.Environment;
import gnu.mapping.Procedure;
import gnu.mapping.Procedure1or2;
import gnu.text.SourceMessages;
import java.util.Stack;

public class Eval
  extends Procedure1or2
{
  public static final Eval eval = new Eval();
  static final String evalFunctionName = "atEvalLevel$";
  
  static
  {
    eval.setName("eval");
  }
  
  public static Object eval(Object paramObject, Environment paramEnvironment)
    throws Throwable
  {
    CallContext localCallContext = CallContext.getInstance();
    int i = localCallContext.startFromContext();
    try
    {
      eval(paramObject, paramEnvironment, localCallContext);
      Object localObject = localCallContext.getFromContext(i);
      return localObject;
    }
    catch (Throwable localThrowable)
    {
      localCallContext.cleanupFromContext(i);
      throw localThrowable;
    }
  }
  
  public static void eval(Object paramObject, Environment paramEnvironment, CallContext paramCallContext)
    throws Throwable
  {
    PairWithPosition localPairWithPosition;
    if ((paramObject instanceof PairWithPosition)) {
      localPairWithPosition = new PairWithPosition((PairWithPosition)paramObject, paramObject, LList.Empty);
    }
    for (;;)
    {
      evalBody(localPairWithPosition, paramEnvironment, new SourceMessages(), paramCallContext);
      return;
      localPairWithPosition = new PairWithPosition(paramObject, LList.Empty);
      localPairWithPosition.setFile("<eval>");
    }
  }
  
  public static Object evalBody(Object paramObject, Environment paramEnvironment, SourceMessages paramSourceMessages)
    throws Throwable
  {
    CallContext localCallContext = CallContext.getInstance();
    int i = localCallContext.startFromContext();
    try
    {
      evalBody(paramObject, paramEnvironment, paramSourceMessages, localCallContext);
      Object localObject = localCallContext.getFromContext(i);
      return localObject;
    }
    catch (Throwable localThrowable)
    {
      localCallContext.cleanupFromContext(i);
      throw localThrowable;
    }
  }
  
  public static void evalBody(Object paramObject, Environment paramEnvironment, SourceMessages paramSourceMessages, CallContext paramCallContext)
    throws Throwable
  {
    Language localLanguage = Language.getDefaultLanguage();
    Environment localEnvironment = Environment.getCurrent();
    if (paramEnvironment != localEnvironment) {}
    try
    {
      Environment.setCurrent(paramEnvironment);
      Translator localTranslator = new Translator(localLanguage, paramSourceMessages, NameLookup.getInstance(paramEnvironment, localLanguage));
      localTranslator.immediate = true;
      localTranslator.setState(3);
      localTranslator.setSharedModuleDefs(true);
      ModuleExp localModuleExp = localTranslator.pushNewModule((String)null);
      Compilation localCompilation = Compilation.setSaveCurrent(localTranslator);
      int i;
      StringBuilder localStringBuilder;
      int j;
      if (paramEnvironment == localEnvironment) {
        return;
      }
    }
    finally
    {
      try
      {
        i = localTranslator.formStack.size();
        localTranslator.scanBody(paramObject, localModuleExp, false);
        localTranslator.firstForm = i;
        localTranslator.finishModule(localModuleExp);
        Compilation.restoreCurrent(localCompilation);
        if ((paramObject instanceof PairWithPosition)) {
          localModuleExp.setFile(((PairWithPosition)paramObject).getFileName());
        }
        localStringBuilder = new StringBuilder().append("atEvalLevel$");
        j = 1 + ModuleExp.interactiveCounter;
        ModuleExp.interactiveCounter = j;
        localModuleExp.setName(j);
        ModuleExp.evalModule(paramEnvironment, paramCallContext, localTranslator, null, null);
        if (!paramSourceMessages.seenErrors()) {
          break label248;
        }
        throw new RuntimeException("invalid syntax in eval form:\n" + paramSourceMessages.toString(20));
      }
      finally
      {
        Compilation.restoreCurrent(localCompilation);
      }
      localObject1 = finally;
      if (paramEnvironment != localEnvironment) {
        Environment.setCurrent(localEnvironment);
      }
    }
    label248:
    Environment.setCurrent(localEnvironment);
  }
  
  public void apply(CallContext paramCallContext)
    throws Throwable
  {
    Procedure.checkArgCount(this, paramCallContext.count);
    Object localObject = paramCallContext.getNextArg();
    Environment localEnvironment = (Environment)paramCallContext.getNextArg(null);
    if (localEnvironment == null) {
      localEnvironment = Environment.user();
    }
    paramCallContext.lastArg();
    eval(localObject, localEnvironment, paramCallContext);
  }
  
  public Object apply1(Object paramObject)
    throws Throwable
  {
    return eval(paramObject, Environment.user());
  }
  
  public Object apply2(Object paramObject1, Object paramObject2)
    throws Throwable
  {
    return eval(paramObject1, (Environment)paramObject2);
  }
}


/* Location:           C:\Users\Bonjour\Desktop\classes_dex2jar.jar
 * Qualified Name:     kawa.lang.Eval
 * JD-Core Version:    0.7.0.1
 */