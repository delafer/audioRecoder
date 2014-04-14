package org.delafer.recoder.test.listener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class ItemObservable extends Observable
{
  private static final List<String> jokes = Arrays.asList(
    "Sorry, aber du siehst so aus, wie ich mich fuefchle.",
    "Eine Null kann ein bestehendes Problem verzehnfachen.",
    "Wer zuletzt lacht, hat es nicht eher begriffen.",
    "Wer zuletzt lacht, stirbt wenigstens fruehlich.",
    "Unsere Luft hat einen Vorteil: Man sieht, was man einatmet."
  );

  public void doJob()
  {
    setChanged();
    Collections.shuffle( jokes );
    EventQ event = new EventQ();
    event.data = jokes.get(0);

    notifyListeners( event );
  }

	private void setChanged() {
	}
}