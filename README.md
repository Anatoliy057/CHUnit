# Functions

Extension designed to work [MSUnit](https://github.com/Anatoliy057/MSUnit)

***

## Functions
A set of functions for manipulation of 'functions').

### void to\_func(procName, [...value]):
Save given procedure as variable with default arguments.

### void do\_func(func, [...value]):
Execution function with given arguments.

### void do\_func_array(func, valueArray):
Execution function with given array arguments.

***

## Echo
A set of functions for logs.

### void print(message):
Prints a message.

### void println(message):
Prints a message and then terminate the line.

***

## Environments
A set of functions for environment.

### void remove\_env(id):
Remove reference of environment by id.

### void save\_env(id):
Save reference of environment by id.

### void x\_replace\_procedure([id], proc, func):
Swaps one procedure for function in a saved environment (or current if two arguments passed).

### boolean x_remove_procedure([id], proc)
Remove procedure from a saved environment (or current if two arguments passed).

***

## Globals
Set of function for interactions with globals variables locally for threads

### void local\_export(key, value):
Stores a value in the local global for thread (like "export").

### mixed local\_import(key, [default]):
This function likes "import" but imports a value from the local thread global value.

***

## Threading
A set of functions for interacting with threads.

### array dump\_keys\_threads():
Returns an array of all threads keys that are currently running.

### void x\_safe\_execute([values...], closure):
Executes the given closure. You can also send arguments to the closure, which it may or may not use, depending on the particular closure's definition. Unlike closure, it returns only void and will be executed even if the thread was stopped by a functionx_stop_thread().

### boolean x\_stop\_thread(id):
Stopping tracked thread named 'id'. If successful returns true, else false. If the thread performs a x_safe_execute() function, the interrupting thread will wait for execution.
