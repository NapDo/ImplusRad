package kawa.lib.kawa;

import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.kawa.functions.SetNamedPart;
import gnu.kawa.reflect.Invoke;
import gnu.kawa.reflect.StaticFieldLocation;
import gnu.kawa.util.AbstractHashTable;
import gnu.kawa.util.GeneralHashTable;
import gnu.kawa.util.HashNode;
import gnu.lists.FVector;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.CallContext;
import gnu.mapping.Location;
import gnu.mapping.Procedure;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Values;
import gnu.mapping.WrongType;
import java.util.Map.Entry;
import kawa.lib.lists;
import kawa.lib.misc;
import kawa.standard.thisRef;

public class hashtable
  extends ModuleBody
{
  public static final Location $Prvt$do;
  public static final Class $Prvt$hashnode;
  public static final Location $Prvt$let$St;
  public static final hashtable $instance;
  static final SimpleSymbol Lit0;
  static final SimpleSymbol Lit1 = (SimpleSymbol)new SimpleSymbol("hashtable-check-mutable").readResolve();
  public static final Class hashtable;
  public static final ModuleMethod hashtable$Mncheck$Mnmutable;
  
  static
  {
    Lit0 = (SimpleSymbol)new SimpleSymbol("mutable").readResolve();
    $Prvt$hashnode = HashNode.class;
    $instance = new hashtable();
    $Prvt$let$St = StaticFieldLocation.make("kawa.lib.std_syntax", "let$St");
    $Prvt$do = StaticFieldLocation.make("kawa.lib.std_syntax", "do");
    hashtable = HashTable.class;
    hashtable$Mncheck$Mnmutable = new ModuleMethod($instance, 1, Lit1, 4097);
    $instance.run();
  }
  
  public hashtable()
  {
    ModuleInfo.register(this);
  }
  
  public static void hashtableCheckMutable(HashTable paramHashTable)
  {
    if (!paramHashTable.mutable) {
      misc.error$V("cannot modify non-mutable hashtable", new Object[0]);
    }
  }
  
  public Object apply1(ModuleMethod paramModuleMethod, Object paramObject)
  {
    if (paramModuleMethod.selector == 1) {}
    try
    {
      HashTable localHashTable = (HashTable)paramObject;
      hashtableCheckMutable(localHashTable);
      return Values.empty;
    }
    catch (ClassCastException localClassCastException)
    {
      throw new WrongType(localClassCastException, "hashtable-check-mutable", 1, paramObject);
    }
    return super.apply1(paramModuleMethod, paramObject);
  }
  
  public int match1(ModuleMethod paramModuleMethod, Object paramObject, CallContext paramCallContext)
  {
    if (paramModuleMethod.selector == 1)
    {
      if (!(paramObject instanceof HashTable)) {
        return -786431;
      }
      paramCallContext.value1 = paramObject;
      paramCallContext.proc = paramModuleMethod;
      paramCallContext.pc = 1;
      return 0;
    }
    return super.match1(paramModuleMethod, paramObject, paramCallContext);
  }
  
  public final void run(CallContext paramCallContext) {}
  
  public class HashTable
    extends GeneralHashTable
  {
    public Procedure equivalenceFunction;
    public Procedure hashFunction;
    public boolean mutable;
    
    private void $finit$()
    {
      this.mutable = true;
    }
    
    public HashTable(Procedure paramProcedure)
    {
      $finit$();
      this.equivalenceFunction = this$1;
      this.hashFunction = paramProcedure;
    }
    
    public HashTable(Procedure paramProcedure, int paramInt)
    {
      super();
      $finit$();
      this.equivalenceFunction = this$1;
      this.hashFunction = paramProcedure;
    }
    
    public HashTable(boolean paramBoolean)
    {
      $finit$();
      Invoke localInvoke = Invoke.invokeSpecial;
      Object[] arrayOfObject = new Object[5];
      arrayOfObject[0] = hashtable.hashtable;
      arrayOfObject[1] = this;
      arrayOfObject[2] = this$1.equivalenceFunction.apply0();
      arrayOfObject[3] = this$1.hashFunction.apply0();
      arrayOfObject[4] = Integer.valueOf(100 + this$1.size());
      localInvoke.applyN(arrayOfObject);
      putAll(this$1);
      SetNamedPart localSetNamedPart = SetNamedPart.setNamedPart;
      thisRef localthisRef = thisRef.thisSyntax;
      SimpleSymbol localSimpleSymbol = hashtable.Lit0;
      if (paramBoolean) {}
      for (Boolean localBoolean = Boolean.TRUE;; localBoolean = Boolean.FALSE)
      {
        localSetNamedPart.apply3(localthisRef, localSimpleSymbol, localBoolean);
        return;
      }
    }
    
    public Object clone()
    {
      return new HashTable(this, true);
    }
    
    public Pair entriesVectorPair()
    {
      FVector localFVector1 = new FVector();
      FVector localFVector2 = new FVector();
      Map.Entry[] arrayOfEntry = this.table;
      try
      {
        HashNode[] arrayOfHashNode = (HashNode[])arrayOfEntry;
        for (int i = -1 + arrayOfHashNode.length; i >= 0; i--) {
          for (HashNode localHashNode = arrayOfHashNode[i]; localHashNode != null; localHashNode = getEntryNext(localHashNode))
          {
            localFVector1.add(localHashNode.getKey());
            localFVector2.add(localHashNode.getValue());
          }
        }
        return lists.cons(localFVector1, localFVector2);
      }
      catch (ClassCastException localClassCastException)
      {
        throw new WrongType(localClassCastException, "table", -2, arrayOfEntry);
      }
    }
    
    public Object fold(Procedure paramProcedure, Object paramObject)
    {
      Map.Entry[] arrayOfEntry = this.table;
      try
      {
        HashNode[] arrayOfHashNode = (HashNode[])arrayOfEntry;
        for (int i = -1 + arrayOfHashNode.length; i >= 0; i--) {
          for (HashNode localHashNode = arrayOfHashNode[i]; localHashNode != null; localHashNode = getEntryNext(localHashNode)) {
            paramObject = paramProcedure.apply3(localHashNode.getKey(), localHashNode.getValue(), paramObject);
          }
        }
        return paramObject;
      }
      catch (ClassCastException localClassCastException)
      {
        throw new WrongType(localClassCastException, "table", -2, arrayOfEntry);
      }
    }
    
    public int hash(Object paramObject)
    {
      return ((Number)this.hashFunction.apply1(paramObject)).intValue();
    }
    
    public FVector keysVector()
    {
      localFVector = new FVector();
      Map.Entry[] arrayOfEntry = this.table;
      try
      {
        HashNode[] arrayOfHashNode = (HashNode[])arrayOfEntry;
        for (int i = -1 + arrayOfHashNode.length; i >= 0; i--) {
          for (HashNode localHashNode = arrayOfHashNode[i]; localHashNode != null; localHashNode = getEntryNext(localHashNode)) {
            localFVector.add(localHashNode.getKey());
          }
        }
        return localFVector;
      }
      catch (ClassCastException localClassCastException)
      {
        throw new WrongType(localClassCastException, "table", -2, arrayOfEntry);
      }
    }
    
    public boolean matches(Object paramObject1, Object paramObject2)
    {
      return this.equivalenceFunction.apply2(paramObject1, paramObject2) != Boolean.FALSE;
    }
    
    public void putAll(HashTable paramHashTable)
    {
      Map.Entry[] arrayOfEntry = paramHashTable.table;
      try
      {
        HashNode[] arrayOfHashNode = (HashNode[])arrayOfEntry;
        for (int i = -1 + arrayOfHashNode.length; i >= 0; i--) {
          for (HashNode localHashNode = arrayOfHashNode[i]; localHashNode != null; localHashNode = paramHashTable.getEntryNext(localHashNode)) {
            put(localHashNode.getKey(), localHashNode.getValue());
          }
        }
        return;
      }
      catch (ClassCastException localClassCastException)
      {
        throw new WrongType(localClassCastException, "table", -2, arrayOfEntry);
      }
    }
    
    public Object toAlist()
    {
      localObject = LList.Empty;
      Map.Entry[] arrayOfEntry = this.table;
      try
      {
        HashNode[] arrayOfHashNode = (HashNode[])arrayOfEntry;
        for (int i = -1 + arrayOfHashNode.length; i >= 0; i--) {
          for (HashNode localHashNode = arrayOfHashNode[i]; localHashNode != null; localHashNode = getEntryNext(localHashNode)) {
            localObject = lists.cons(lists.cons(localHashNode.getKey(), localHashNode.getValue()), localObject);
          }
        }
        return localObject;
      }
      catch (ClassCastException localClassCastException)
      {
        throw new WrongType(localClassCastException, "table", -2, arrayOfEntry);
      }
    }
    
    public HashNode[] toNodeArray()
    {
      arrayOfHashNode1 = new HashNode[size()];
      int i = 0;
      Map.Entry[] arrayOfEntry = this.table;
      try
      {
        HashNode[] arrayOfHashNode2 = (HashNode[])arrayOfEntry;
        int j = -1 + arrayOfHashNode2.length;
        while (j >= 0)
        {
          HashNode localHashNode = arrayOfHashNode2[j];
          int m;
          for (int k = i; localHashNode != null; k = m)
          {
            arrayOfHashNode1[k] = localHashNode;
            m = k + 1;
            localHashNode = getEntryNext(localHashNode);
          }
          j--;
          i = k;
        }
        return arrayOfHashNode1;
      }
      catch (ClassCastException localClassCastException)
      {
        throw new WrongType(localClassCastException, "table", -2, arrayOfEntry);
      }
    }
    
    public LList toNodeList()
    {
      LList localLList = LList.Empty;
      Map.Entry[] arrayOfEntry = this.table;
      try
      {
        HashNode[] arrayOfHashNode = (HashNode[])arrayOfEntry;
        int i = -1 + arrayOfHashNode.length;
        Object localObject = localLList;
        while (i >= 0)
        {
          HashNode localHashNode = arrayOfHashNode[i];
          while (localHashNode != null)
          {
            Pair localPair = lists.cons(localHashNode, localObject);
            localHashNode = getEntryNext(localHashNode);
            localObject = localPair;
          }
          i--;
        }
        return (LList)localObject;
      }
      catch (ClassCastException localClassCastException)
      {
        throw new WrongType(localClassCastException, "table", -2, arrayOfEntry);
      }
    }
    
    public void walk(Procedure paramProcedure)
    {
      Map.Entry[] arrayOfEntry = this.table;
      try
      {
        HashNode[] arrayOfHashNode = (HashNode[])arrayOfEntry;
        for (int i = -1 + arrayOfHashNode.length; i >= 0; i--) {
          for (HashNode localHashNode = arrayOfHashNode[i]; localHashNode != null; localHashNode = getEntryNext(localHashNode)) {
            paramProcedure.apply2(localHashNode.getKey(), localHashNode.getValue());
          }
        }
        return;
      }
      catch (ClassCastException localClassCastException)
      {
        throw new WrongType(localClassCastException, "table", -2, arrayOfEntry);
      }
    }
  }
}


/* Location:           C:\Users\Bonjour\Desktop\classes_dex2jar.jar
 * Qualified Name:     kawa.lib.kawa.hashtable
 * JD-Core Version:    0.7.0.1
 */