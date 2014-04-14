package org.delafer.recoder.test.listener;

import java.util.ArrayList;
import java.util.List;

public class Observable
{
  private final List<ListenerQ> listeners = new ArrayList<ListenerQ>();

  public void addListener( ListenerQ observer )
  {
    if ( ! listeners.contains( observer ) )
      listeners.add( observer );
  }

  public void deleteListener( ListenerQ observer )
  {
    listeners.remove( observer );
  }

  public void notifyListeners( EventQ arg )
  {
    for ( ListenerQ next : listeners )
    	next.handleEvent( arg );
  }
}
