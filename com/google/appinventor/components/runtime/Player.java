package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Vibrator;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.errors.IllegalArgumentError;
import com.google.appinventor.components.runtime.util.FroyoUtil;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.SdkLevel;
import java.io.IOException;

@DesignerComponent(category=ComponentCategory.MEDIA, description="Multimedia component that plays audio and controls phone vibration.  The name of a multimedia field is specified in the <code>Source</code> property, which can be set in the Designer or in the Blocks Editor.  The length of time for a vibration is specified in the Blocks Editor in milliseconds (thousandths of a second).\n<p>For supported audio formats, see <a href=\"http://developer.android.com/guide/appendix/media-formats.html\" target=\"_blank\">Android Supported Media Formats</a>.</p>\n<p>This component is best for long sound files, such as songs, while the <code>Sound</code> component is more efficient for short files, such as sound effects.</p>", iconName="images/player.png", nonVisible=true, version=6)
@SimpleObject
@UsesPermissions(permissionNames="android.permission.VIBRATE, android.permission.INTERNET")
public final class Player
  extends AndroidNonvisibleComponent
  implements Component, MediaPlayer.OnCompletionListener, OnPauseListener, OnResumeListener, OnDestroyListener, OnStopListener, Deleteable
{
  private static final boolean audioFocusSupported = false;
  private final Activity activity;
  private Object afChangeListener;
  private AudioManager am;
  private boolean focusOn;
  private boolean loop;
  private boolean playOnlyInForeground;
  private MediaPlayer player;
  public int playerState;
  private String sourcePath;
  private final Vibrator vibe;
  
  static
  {
    if (SdkLevel.getLevel() >= 8)
    {
      audioFocusSupported = true;
      return;
    }
  }
  
  public Player(ComponentContainer paramComponentContainer)
  {
    super(paramComponentContainer.$form());
    this.activity = paramComponentContainer.$context();
    this.sourcePath = "";
    this.vibe = ((Vibrator)this.form.getSystemService("vibrator"));
    this.form.registerForOnDestroy(this);
    this.form.registerForOnResume(this);
    this.form.registerForOnPause(this);
    this.form.registerForOnStop(this);
    this.form.setVolumeControlStream(3);
    this.loop = false;
    this.playOnlyInForeground = false;
    this.focusOn = false;
    if (audioFocusSupported) {}
    for (AudioManager localAudioManager = FroyoUtil.setAudioManager(this.activity);; localAudioManager = null)
    {
      this.am = localAudioManager;
      boolean bool = audioFocusSupported;
      Object localObject = null;
      if (bool) {
        localObject = FroyoUtil.setAudioFocusChangeListener(this);
      }
      this.afChangeListener = localObject;
      return;
    }
  }
  
  private void abandonFocus()
  {
    FroyoUtil.abandonFocus(this.am, this.afChangeListener);
    this.focusOn = false;
  }
  
  private void prepare()
  {
    try
    {
      this.player.prepare();
      this.playerState = 1;
      return;
    }
    catch (IOException localIOException)
    {
      this.player.release();
      this.player = null;
      this.playerState = 0;
      Form localForm = this.form;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = this.sourcePath;
      localForm.dispatchErrorOccurredEvent(this, "Source", 702, arrayOfObject);
    }
  }
  
  private void prepareToDie()
  {
    if ((audioFocusSupported) && (this.focusOn)) {
      abandonFocus();
    }
    if (this.playerState != 0) {
      this.player.stop();
    }
    this.playerState = 0;
    if (this.player != null)
    {
      this.player.release();
      this.player = null;
    }
    this.vibe.cancel();
  }
  
  private void requestPermanentFocus()
  {
    if (FroyoUtil.focusRequestGranted(this.am, this.afChangeListener)) {}
    for (boolean bool = true;; bool = false)
    {
      this.focusOn = bool;
      if (!this.focusOn)
      {
        Form localForm = this.form;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = this.sourcePath;
        localForm.dispatchErrorOccurredEvent(this, "Source", 709, arrayOfObject);
      }
      return;
    }
  }
  
  @SimpleEvent
  public void Completed()
  {
    EventDispatcher.dispatchEvent(this, "Completed", new Object[0]);
  }
  
  @SimpleProperty(category=PropertyCategory.BEHAVIOR, description="Reports whether the media is playing")
  public boolean IsPlaying()
  {
    if ((this.playerState == 1) || (this.playerState == 2)) {
      return this.player.isPlaying();
    }
    return false;
  }
  
  @DesignerProperty(defaultValue="False", editorType="boolean")
  @SimpleProperty
  public void Loop(boolean paramBoolean)
  {
    if ((this.playerState == 1) || (this.playerState == 2) || (this.playerState == 3)) {
      this.player.setLooping(paramBoolean);
    }
    this.loop = paramBoolean;
  }
  
  @SimpleProperty(category=PropertyCategory.BEHAVIOR, description="If true, the player will loop when it plays. Setting Loop while the player is playing will affect the current playing.")
  public boolean Loop()
  {
    return this.loop;
  }
  
  @SimpleEvent(description="This event is signaled when another player has started (and the current player is playing or paused, but not stopped).")
  public void OtherPlayerStarted()
  {
    EventDispatcher.dispatchEvent(this, "OtherPlayerStarted", new Object[0]);
  }
  
  @SimpleFunction
  public void Pause()
  {
    if (this.player == null) {}
    boolean bool;
    do
    {
      do
      {
        return;
        bool = this.player.isPlaying();
      } while (this.playerState != 2);
      this.player.pause();
    } while (!bool);
    this.playerState = 3;
  }
  
  @DesignerProperty(defaultValue="False", editorType="boolean")
  @SimpleProperty
  public void PlayOnlyInForeground(boolean paramBoolean)
  {
    this.playOnlyInForeground = paramBoolean;
  }
  
  @SimpleProperty(category=PropertyCategory.BEHAVIOR, description="If true, the player will pause playing when leaving the current screen; if false (default option), the player continues playing whenever the current screen is displaying or not.")
  public boolean PlayOnlyInForeground()
  {
    return this.playOnlyInForeground;
  }
  
  @SimpleEvent(description="The PlayerError event is no longer used. Please use the Screen.ErrorOccurred event instead.", userVisible=false)
  public void PlayerError(String paramString) {}
  
  @SimpleProperty(category=PropertyCategory.BEHAVIOR)
  public String Source()
  {
    return this.sourcePath;
  }
  
  @DesignerProperty(defaultValue="", editorType="asset")
  @SimpleProperty
  public void Source(String paramString)
  {
    if (paramString == null) {
      paramString = "";
    }
    this.sourcePath = paramString;
    if ((this.playerState == 1) || (this.playerState == 2) || (this.playerState == 3))
    {
      this.player.stop();
      this.playerState = 0;
    }
    if (this.player != null)
    {
      this.player.release();
      this.player = null;
    }
    if (this.sourcePath.length() > 0)
    {
      this.player = new MediaPlayer();
      this.player.setOnCompletionListener(this);
    }
    try
    {
      MediaUtil.loadMediaPlayer(this.player, this.form, this.sourcePath);
      this.player.setAudioStreamType(3);
      if (audioFocusSupported) {
        requestPermanentFocus();
      }
      prepare();
      return;
    }
    catch (IOException localIOException)
    {
      this.player.release();
      this.player = null;
      Form localForm = this.form;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = this.sourcePath;
      localForm.dispatchErrorOccurredEvent(this, "Source", 701, arrayOfObject);
    }
  }
  
  @SimpleFunction
  public void Start()
  {
    if ((audioFocusSupported) && (!this.focusOn)) {
      requestPermanentFocus();
    }
    if ((this.playerState == 1) || (this.playerState == 2) || (this.playerState == 3) || (this.playerState == 4))
    {
      this.player.setLooping(this.loop);
      this.player.start();
      this.playerState = 2;
    }
  }
  
  @SimpleFunction
  public void Stop()
  {
    if ((audioFocusSupported) && (this.focusOn)) {
      abandonFocus();
    }
    if ((this.playerState == 2) || (this.playerState == 3) || (this.playerState == 4))
    {
      this.player.stop();
      prepare();
      this.player.seekTo(0);
    }
  }
  
  @SimpleFunction
  public void Vibrate(long paramLong)
  {
    this.vibe.vibrate(paramLong);
  }
  
  @DesignerProperty(defaultValue="50", editorType="non_negative_float")
  @SimpleProperty(description="Sets the volume to a number between 0 and 100")
  public void Volume(int paramInt)
  {
    if ((this.playerState == 1) || (this.playerState == 2) || (this.playerState == 3))
    {
      if ((paramInt > 100) || (paramInt < 0)) {
        throw new IllegalArgumentError("Volume must be set to a number between 0 and 100");
      }
      this.player.setVolume(paramInt / 100.0F, paramInt / 100.0F);
    }
  }
  
  public void onCompletion(MediaPlayer paramMediaPlayer)
  {
    Completed();
  }
  
  public void onDelete()
  {
    prepareToDie();
  }
  
  public void onDestroy()
  {
    prepareToDie();
  }
  
  public void onPause()
  {
    if (this.player == null) {}
    while ((!this.playOnlyInForeground) || (!this.player.isPlaying())) {
      return;
    }
    pause();
  }
  
  public void onResume()
  {
    if ((this.playOnlyInForeground) && (this.playerState == 4)) {
      Start();
    }
  }
  
  public void onStop()
  {
    if (this.player == null) {}
    while ((!this.playOnlyInForeground) || (!this.player.isPlaying())) {
      return;
    }
    pause();
  }
  
  public void pause()
  {
    if (this.player == null) {}
    while (this.playerState != 2) {
      return;
    }
    this.player.pause();
    this.playerState = 4;
  }
}


/* Location:           C:\Users\Bonjour\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.appinventor.components.runtime.Player
 * JD-Core Version:    0.7.0.1
 */