package util;

import java.util.*;
import java.util.concurrent.*;

public class SingleThreadExecutorGroup {
  private List<ExecutorService> executors;

  public SingleThreadExecutorGroup(int size) {
    this.executors = new ArrayList<ExecutorService>();
    for (int i = 0; i < size; i++) {
      this.executors.add(Executors.newSingleThreadExecutor());
    }
  }

  public ExecutorService getExecutor() {
    return this.executors.get((int)(Math.random() * this.executors.size()));
  }
}
