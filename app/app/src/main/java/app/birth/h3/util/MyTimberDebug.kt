package app.birth.h3.util

import timber.log.Timber

class MyTimberDebug  : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        var messageAt = message
        val thread = Throwable().stackTrace
        if (thread != null && thread.size >= 5) {
            val stack: StackTraceElement = thread[5]
            val className: String = stack.className
            val packageName: String = className.substring(0, className.lastIndexOf("."))
            val fileName: String = stack.fileName
            messageAt = message + String.format(" at %s(%s:%s)", packageName, fileName, stack.lineNumber)
        }
        super.log(priority, tag, messageAt, t)
    }
}
