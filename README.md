# Functions

## Executes

A set of functions for specific execution of closures

### mixed execute\_current([values...], closure):

Executes the given closure like execute(), but with current environment. WARNING: the closure must use already defined variables and procedures to avoid compilation errors.

## Threading

A set of functions for interacting with threads.

### array dump\_keys\_threads():

Returns an array of all threads keys that are currently running in the JVM.

### void x\_safe\_execute([values...], closure):

Executes the given closure. You can also send arguments to the closure, which it may or may not use, depending on the particular closure's definition. Unlike closure, it returns only void and will be executed even if the thread was stopped by a function x_stop_thread().

### boolean x\_stop\_thread(string id):

Stopping tracked thread named 'id'. If successful, returns true, else false. If the thread performs a x_safe_execute() function, the interrupting thread will wait for execution.

## Echo

A set of functions for logs

### void print(message):

Prints a message.

### void println(message):

Prints a message and then terminate the line.
