package gnu.text;

import java.io.PrintStream;
import java.io.PrintWriter;

public class SourceError
  implements SourceLocator
{
  public String code;
  public int column;
  public Throwable fakeException;
  public String filename;
  public int line;
  public String message;
  public SourceError next;
  public char severity;
  
  public SourceError(char paramChar, SourceLocator paramSourceLocator, String paramString)
  {
    this(paramChar, paramSourceLocator.getFileName(), paramSourceLocator.getLineNumber(), paramSourceLocator.getColumnNumber(), paramString);
  }
  
  public SourceError(char paramChar, String paramString1, int paramInt1, int paramInt2, String paramString2)
  {
    this.severity = paramChar;
    this.filename = paramString1;
    this.line = paramInt1;
    this.column = paramInt2;
    this.message = paramString2;
  }
  
  public SourceError(LineBufferedReader paramLineBufferedReader, char paramChar, String paramString)
  {
    this(paramChar, paramLineBufferedReader.getName(), 1 + paramLineBufferedReader.getLineNumber(), paramLineBufferedReader.getColumnNumber(), paramString);
    if (this.column >= 0) {
      this.column = (1 + this.column);
    }
  }
  
  public int getColumnNumber()
  {
    if (this.column == 0) {
      return -1;
    }
    return this.column;
  }
  
  public String getFileName()
  {
    return this.filename;
  }
  
  public int getLineNumber()
  {
    if (this.line == 0) {
      return -1;
    }
    return this.line;
  }
  
  public String getPublicId()
  {
    return null;
  }
  
  public String getSystemId()
  {
    return this.filename;
  }
  
  public boolean isStableSourceLocation()
  {
    return true;
  }
  
  public void print(PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print(this);
  }
  
  public void println(PrintStream paramPrintStream)
  {
    int i;
    for (String str = toString();; str = str.substring(i + 1))
    {
      i = str.indexOf('\n');
      if (i < 0)
      {
        paramPrintStream.println(str);
        return;
      }
      paramPrintStream.println(str.substring(0, i));
    }
  }
  
  public void println(PrintWriter paramPrintWriter)
  {
    int i;
    for (String str = toString();; str = str.substring(i + 1))
    {
      i = str.indexOf('\n');
      if (i < 0)
      {
        paramPrintWriter.println(str);
        return;
      }
      paramPrintWriter.println(str.substring(0, i));
    }
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (this.filename == null) {}
    for (String str = "<unknown>";; str = this.filename)
    {
      localStringBuffer.append(str);
      if ((this.line > 0) || (this.column > 0))
      {
        localStringBuffer.append(':');
        localStringBuffer.append(this.line);
        if (this.column > 0)
        {
          localStringBuffer.append(':');
          localStringBuffer.append(this.column);
        }
      }
      localStringBuffer.append(": ");
      if (this.severity == 'w') {
        localStringBuffer.append("warning - ");
      }
      localStringBuffer.append(this.message);
      if (this.code != null)
      {
        localStringBuffer.append(" [");
        localStringBuffer.append(this.code);
        localStringBuffer.append("]");
      }
      if (this.fakeException == null) {
        break;
      }
      StackTraceElement[] arrayOfStackTraceElement = this.fakeException.getStackTrace();
      for (int i = 0; i < arrayOfStackTraceElement.length; i++)
      {
        localStringBuffer.append("\n");
        localStringBuffer.append("    ");
        localStringBuffer.append(arrayOfStackTraceElement[i].toString());
      }
    }
    return localStringBuffer.toString();
  }
}


/* Location:           C:\Users\Bonjour\Desktop\classes_dex2jar.jar
 * Qualified Name:     gnu.text.SourceError
 * JD-Core Version:    0.7.0.1
 */