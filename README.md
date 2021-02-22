# Functions

Extension designed to work [MSUnit](https://github.com/Anatoliy057/MSUnit)

> **Warning!** The extension overrides how the Globals class works.

***

## Closures
A set of functions for manipulation of closures

### void set\_current\_env():
Replaces the internal closing environment with the current one

***

## Echo
A set of functions for logs

### void print(message):
Prints a message

### void println(message):
Prints a message and then terminate the line.

***

## Environments
A set of functions for environment

### void remove\_env(id):
Remove reference of environment by id

### void save\_env(id):
Save reference of environment by id

### void x\_replace\_procedure(id, proc, replacement):
Swaps one procedure for another (or given closure) in a saved environment

***

## Global
A set of functions for globals

### void x_set_extend_daemon_manager():
Sets extend daemon manager, that clearing globals when threads deactivated.

### void x_set_auto_globals(auto):
ets automatically initialize globals for new threads.

### boolean is\_auto\_globals():
Returns whether globals for threads are automatically initialized.

### void keys\_globals():
Return array of keys globals

### void original\_export(key, value):
Stores a value in the original global storage register (like "export".

### mixed original\_import(key, [default]):
This function likes "import" but imports a value from the original global value register.

### void x\_init\_thread\_globals():
Initializes empty globals for the current thread.

### void x\_remove\_thread\_globals():
Delete globals for the current thread. If no globals for the thread are init, nothing happen.

***

## Threading
A set of functions for interacting with threads.

### array dump\_keys\_threads():
Returns an array of all threads keys that are currently running.

### void x\_safe\_execute([values...], closure):
Executes the given closure. You can also send arguments to the closure, which it may or may not use, depending on the particular closure's definition. Unlike closure, it returns only void and will be executed even if the thread was stopped by a functionx_stop_thread().

### boolean x\_stop\_thread(id):
Stopping tracked thread named 'id'. If successful returns true, else false. If the thread performs a x_safe_execute() function, the interrupting thread will wait for execution.
