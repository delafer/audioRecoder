package org.delafer.recoder.test.listener;


public class TestCode
{
  public static void main( String[] args )
  {
    ListenerQ listenerAlex    = new CustomListenerQ( "Alex" );
    ListenerQ listenerSergey  = new CustomListenerQ( "Sergey" );

    ItemObservable widgetItem  = new ItemObservable();

    widgetItem.addListener( listenerAlex );

    widgetItem.doJob();
    widgetItem.doJob();

    widgetItem.addListener( listenerSergey );

    widgetItem.doJob();

    widgetItem.deleteListener( listenerAlex );

    widgetItem.doJob();
  }
}