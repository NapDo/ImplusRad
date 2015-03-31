package kawa.standard;

import gnu.expr.Expression;
import gnu.expr.ScopeExp;
import gnu.lists.LList;
import gnu.lists.Pair;
import java.util.Vector;
import kawa.lang.Syntax;
import kawa.lang.Translator;

public class module_compile_options
  extends Syntax
{
  public static final module_compile_options module_compile_options = new module_compile_options();
  
  static
  {
    module_compile_options.setName("module-compile-options");
  }
  
  public Expression rewriteForm(Pair paramPair, Translator paramTranslator)
  {
    return null;
  }
  
  public boolean scanForDefinitions(Pair paramPair, Vector paramVector, ScopeExp paramScopeExp, Translator paramTranslator)
  {
    if (with_compile_options.getOptions(paramPair.getCdr(), null, this, paramTranslator) != LList.Empty) {
      paramTranslator.error('e', getName() + " key must be a keyword");
    }
    return true;
  }
}


/* Location:           C:\Users\Bonjour\Desktop\classes_dex2jar.jar
 * Qualified Name:     kawa.standard.module_compile_options
 * JD-Core Version:    0.7.0.1
 */