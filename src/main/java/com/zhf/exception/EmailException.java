package com.zhf.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @author: 曾鸿发
 * @create: 2022-06-13 17:29
 * @description：
 **/
public class EmailException extends Exception {

    public EmailException() {
    }

    public EmailException(String msg) {
        super(msg);
    }

    public EmailException(Throwable rootCause) {
        super(rootCause);
    }

    public EmailException(String msg, Throwable rootCause) {
        super(msg, rootCause);
    }

    @Override
    public void printStackTrace() {
        this.printStackTrace(System.err);
    }

    @Override
    public void printStackTrace(PrintStream out) {
        synchronized (out) {
            PrintWriter writer = new PrintWriter(out, false);
            this.printStackTrace(writer);
            writer.flush();
        }
    }

    @Override
    public void printStackTrace(PrintWriter out) {
        synchronized (out) {
            super.printStackTrace(out);
        }
    }


}
