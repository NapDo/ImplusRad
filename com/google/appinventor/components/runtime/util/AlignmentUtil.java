package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.runtime.LinearLayout;

public class AlignmentUtil
{
  LinearLayout viewLayout;
  
  public AlignmentUtil(LinearLayout paramLinearLayout)
  {
    this.viewLayout = paramLinearLayout;
  }
  
  public void setHorizontalAlignment(int paramInt)
    throws IllegalArgumentException
  {
    switch (paramInt)
    {
    default: 
      throw new IllegalArgumentException("Bad value to setHorizontalAlignment: " + paramInt);
    case 1: 
      this.viewLayout.setHorizontalGravity(3);
      return;
    case 2: 
      this.viewLayout.setHorizontalGravity(5);
      return;
    }
    this.viewLayout.setHorizontalGravity(1);
  }
  
  public void setVerticalAlignment(int paramInt)
    throws IllegalArgumentException
  {
    switch (paramInt)
    {
    default: 
      throw new IllegalArgumentException("Bad value to setVerticalAlignment: " + paramInt);
    case 1: 
      this.viewLayout.setVerticalGravity(48);
      return;
    case 2: 
      this.viewLayout.setVerticalGravity(16);
      return;
    }
    this.viewLayout.setVerticalGravity(80);
  }
}


/* Location:           C:\Users\Bonjour\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.appinventor.components.runtime.util.AlignmentUtil
 * JD-Core Version:    0.7.0.1
 */