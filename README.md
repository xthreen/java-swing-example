# Concurrency in javax.swing demo

A simple example GUI app which implements a few different types of IO-bound
and CPU-bound tasks, and demonstrates how to run them concurrently in a Swing app.

CommandJob implements a wrapper over ProcessBuilder, allowing the use of shell
commands as tasks. The demo is built around using adb and fastboot from the Android
SDK platform-tools, and an enum is used to handle input and help protect against
arbitrary shell injection.

The network IO tasks currently target recent Google Pixel factory images, but can 
be easily modified to target other devices.
