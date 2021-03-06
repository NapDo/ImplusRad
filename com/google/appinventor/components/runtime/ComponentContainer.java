package com.google.appinventor.components.runtime;

import android.app.Activity;

public abstract interface ComponentContainer
{
  public abstract void $add(AndroidViewComponent paramAndroidViewComponent);
  
  public abstract Activity $context();
  
  public abstract Form $form();
  
  public abstract void setChildHeight(AndroidViewComponent paramAndroidViewComponent, int paramInt);
  
  public abstract void setChildWidth(AndroidViewComponent paramAndroidViewComponent, int paramInt);
}


/* Location:           C:\Users\Bonjour\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.appinventor.components.runtime.ComponentContainer
 * JD-Core Version:    0.7.0.1
 */