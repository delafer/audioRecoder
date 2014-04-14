package org.delafer.recoder.test.listener;



class CustomListenerQ implements ListenerQ
{
  final private String name;

  CustomListenerQ( String name )
  {
    this.name = name;
  }

	@Override
	public void handleEvent(EventQ event) {
		System.out.println( name + " lacht ueber: \"" + event.data + "\"" );
	}
}
